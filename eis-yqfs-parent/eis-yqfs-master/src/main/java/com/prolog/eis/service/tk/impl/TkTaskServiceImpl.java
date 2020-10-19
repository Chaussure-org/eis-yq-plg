package com.prolog.eis.service.tk.impl;

import com.prolog.eis.dao.tk.TKTaskHistoryMapper;

import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.dao.tk.TKTaskMapper;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.ZtStore;
import com.prolog.eis.model.tk.TKTask;
import com.prolog.eis.model.tk.TKTaskHistory;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.eis.service.monitor.YqMonitorService;
import com.prolog.eis.service.pd.PdTaskService;
import com.prolog.eis.service.store.IStoreService;
import com.prolog.eis.service.tk.TkTaskService;
import com.prolog.eis.util.ContainerUtils;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.dto.TaskCallbackDTO;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.core.exception.ParameterException;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class TkTaskServiceImpl implements TkTaskService {
    private static Logger logger = LoggerFactory.getLogger(TkTaskServiceImpl.class);
    @Autowired
    private TKTaskMapper tkTaskMapper;

    @Autowired
    private IStoreService storeService;

    @Autowired
    private StoreMapper sxStoreMapper;

    @Autowired
    private IWCSTaskService taskService;
    @Autowired
    private TKTaskHistoryMapper historyMapper;
    @Autowired
    private ContainerSubService containerSubService;
    @Autowired
    private PdTaskService pdTaskService;
    @Autowired
    private YqMonitorService yqMonitorService;

    @Override
    public void AddTkTask(List<String> tkList) throws Exception {
        //  Set<String> containerList = tkList.stream().map(SxStore::getContainerNo).collect(Collectors.toSet());
        List<TKTask> tkTaskList = new ArrayList<TKTask>();
        for (String container : tkList) {
            TKTask tkTask = new TKTask();
            tkTask.setContainerNo(container);
            tkTask.setGmtCreateTime(new Date());
            tkTask.setGmtUpdateTime(new Date());
            tkTaskList.add(tkTask);
        }
        tkTaskMapper.saveBatch(tkTaskList);
    }


    @Override
    public void deleteTkPlan(int id) throws Exception {
        //根据计划id删除
        TKTask tkTask = tkTaskMapper.findById(id, TKTask.class);
        if (tkTask.getStatus() > TKTask.STATUS_NEW) {
            throw new Exception(id + "该计划存在任务已下发，不能删除");
        } else {
            tkTaskMapper.deleteById(id, TKTask.class);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseTask(List<Integer> idList) throws Exception {
        if (idList.size() == 0) {
            throw new Exception("无下发退库任务");
        }
        List<String> list = new ArrayList<>();
        for (Integer id : idList) {
            TKTask tkTask = tkTaskMapper.findById(id, TKTask.class);
            //任务下发前校验
            pdTaskService.checkPublishTask(tkTask.getContainerNo());
            list.add(tkTask.getContainerNo());
        }
        String ids = StringUtils.join(idList, ",");
        tkTaskMapper.releaseBatch(ids);

        String[] containers = list.toArray(new String[list.size()]);
        Criteria ctr = Criteria.forClass(SxStore.class);
        ctr.setRestriction(Restrictions.in("containerNo",containers));
        sxStoreMapper.updateMapByCriteria(MapUtils.put("taskType", SxStore.TASKTYPE_TK).getMap(), ctr);

    }

    /**
     * 执行退库
     *
     * @param ids
     * @throws Exception
     */
    @Override
    public void processTask(int... ids) throws Exception {
        if (ids == null || ids.length == 0)
            throw new ParameterException("id不能为空");

        Integer[] idarray = Arrays.stream(ids).boxed().toArray(Integer[]::new);
        Criteria ctr = Criteria.forClass(TKTask.class);
        ctr.setRestriction(Restrictions.in("id", idarray));
        tkTaskMapper.updateMapByCriteria(MapUtils.put("status", TKTask.STATUS_PROCESSING).put("gmtUpdateTime", new Date()).getMap(), ctr);
    }

    /**
     * 根据料箱号，执行退库任务
     *
     * @param containerNo
     * @throws Exception
     */
    @Override
    public void processTask(String... containerNo) throws Exception {
        if (containerNo == null || containerNo.length == 0)
            throw new ParameterException("料箱编号不能为空");
        Criteria ctr = Criteria.forClass(TKTask.class);
        ctr.setRestriction(Restrictions.in("containerNo", containerNo));
        tkTaskMapper.updateMapByCriteria(MapUtils.put("status", TKTask.STATUS_PROCESSING).put("gmtUpdateTime", new Date()).getMap(), ctr);
    }

    /**
     * 完成退库任务
     *
     * @param ids
     * @throws Exception
     */
    @Override
    public void finishTask(int... ids) throws Exception {
        if (ids == null || ids.length == 0)
            throw new ParameterException("id不能为空");

        for(int id:ids){
            storeService.deleteZtStore(id);
            this.toHistory(id);
        }
    }

    /**
     * 完成退库任务
     *
     * @param containerNo
     * @throws Exception
     */
    @Override
    public void finishTask(String... containerNo) throws Exception {
        if(containerNo == null || containerNo.length==0){
            throw new ParameterException("料箱编号不能为空");
        }
        for(String number:containerNo){
            storeService.deleteZtStore(number);
            this.toHistoryByContainerNo(number);
            //料箱退库成功，此料箱与商品解除绑定料箱置空
            containerSubService.updateContainerSubByContainer(number);
            //料箱退库成功删除此料箱作业任务明细
            List<ZtStore> ztStores = storeService.findZtStoreByContainerNo(number);
            if (ztStores.size() > 0){
                yqMonitorService.deleteContainerNoTask(ztStores.get(0).getTaskType(),number);
            }

        }

    }

    /**
     * 转历史
     *
     * @param id
     * @throws Exception
     */
    @Override
    public void toHistory(int id) throws Exception {
        TKTask tkTask = tkTaskMapper.findById(id, TKTask.class);
        if (tkTask == null)
            throw new RuntimeException("找不到退库任务(" + id + ")");

        TKTaskHistory his = new TKTaskHistory();
        his.setContainerNo(tkTask.getContainerNo());
        his.setId(tkTask.getId());
        his.setGmtCreateTime(tkTask.getGmtCreateTime());
        his.setGmtUpdateTime(new Date());
        his.setStatus(TKTask.STATUS_FINISH);

        historyMapper.save(his);
        tkTaskMapper.deleteById(id, TKTask.class);

    }

    /**
     * 根据容器转历史
     *
     * @param containerNo
     * @throws Exception
     */
    @Override
    public void toHistoryByContainerNo(String containerNo) throws Exception {
        TKTask tkTask = this.getByContainerNo(containerNo);
        if (tkTask == null)
            throw new RuntimeException("找不到退库任务(容器号:" + containerNo + ")");

        TKTaskHistory his = new TKTaskHistory();
        his.setContainerNo(tkTask.getContainerNo());
        his.setId(tkTask.getId());
        his.setGmtCreateTime(tkTask.getGmtCreateTime());
        his.setGmtUpdateTime(new Date());
        his.setStatus(TKTask.STATUS_FINISH);

        historyMapper.save(his);
        tkTaskMapper.deleteById(tkTask.getId(), TKTask.class);
    }

    /**
     * 根据容器查找退库任务
     *
     * @param containerNo
     * @return
     */
    @Override
    public TKTask getByContainerNo(String containerNo) {
        List<TKTask> list = tkTaskMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), TKTask.class);
        return list.size() > 0 ? list.get(0) : null;
    }
}
      