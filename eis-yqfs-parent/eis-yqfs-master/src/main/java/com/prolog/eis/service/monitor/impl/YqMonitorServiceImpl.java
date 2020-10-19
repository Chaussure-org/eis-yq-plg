package com.prolog.eis.service.monitor.impl;

import com.prolog.eis.dao.assembl.AssemblBoxHzHistoryMapper;
import com.prolog.eis.dao.assembl.AssemblBoxHzMapper;
import com.prolog.eis.dao.assembl.AssemblBoxMxHistoryMapper;
import com.prolog.eis.dao.assembl.AssemblBoxMxMapper;
import com.prolog.eis.dao.pd.PdTaskDetailHisMapper;
import com.prolog.eis.dao.pd.PdTaskDetailMapper;
import com.prolog.eis.dao.repair.RepairPlanMapper;
import com.prolog.eis.dao.repair.RepairPlanMxMapper;
import com.prolog.eis.dao.store.ZtStoreMapper;
import com.prolog.eis.dao.tk.TKTaskMapper;
import com.prolog.eis.dto.store.ZtBoxMxDto;
import com.prolog.eis.dto.store.ZtBoxStatisDto;
import com.prolog.eis.dto.store.ZtStationStatisDto;
import com.prolog.eis.model.assembl.AssemblBoxHz;
import com.prolog.eis.model.assembl.AssemblBoxHzHistory;
import com.prolog.eis.model.assembl.AssemblBoxMx;
import com.prolog.eis.model.assembl.AssemblBoxMxHistory;
import com.prolog.eis.model.pd.PdTaskDetail;
import com.prolog.eis.model.pd.PdTaskDetailHis;
import com.prolog.eis.model.repair.RepairPlanMx;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.ZtStore;
import com.prolog.eis.model.tk.TKTask;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.pickstation.service.IStationLxService;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.eis.service.monitor.YqMonitorService;
import com.prolog.eis.wcs.dao.WCSTaskMapper;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCallbackService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.utils.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author panteng
 * @description: 监控
 * @date 2020/5/9 9:32
 */
@Service
public class YqMonitorServiceImpl implements YqMonitorService {

    private static final Logger logger = LoggerFactory.getLogger(YqMonitorServiceImpl.class);
    @Autowired
    private ZtStoreMapper ztStoreMapper;
    @Autowired
    private PdTaskDetailMapper pdTaskDetailMapper;

    @Autowired
    private PdTaskDetailHisMapper pdTaskDetailHisMapper;

    @Autowired
    private AssemblBoxHzMapper assemblBoxHzMapper;

    @Autowired
    private AssemblBoxHzHistoryMapper assemblBoxHzHistoryMapper;

    @Autowired
    private AssemblBoxMxMapper assemblBoxMxMapper;

    @Autowired
    private AssemblBoxMxHistoryMapper assemblBoxMxHistoryMapper;

    @Autowired
    private IWCSCallbackService iwcsCallbackService;


    @Autowired
    private IWCSTaskService iwcsTaskService;
    @Autowired
    private ContainerSubService containerSubService;

    @Override
    public ZtBoxStatisDto boxTasks() {
        return ztStoreMapper.boxTasks();
    }

    @Override
    public List<ZtStationStatisDto> station() {
        return ztStoreMapper.getStation();
    }

    @Override
    public List<ZtBoxMxDto> ztBoxMx() {
        return ztStoreMapper.ztBoxMx();
    }

    @Autowired
    private WCSTaskMapper wcsTaskMapper;

    @Autowired
    private RepairPlanMapper repairPlanMapper;

    @Autowired
    private RepairPlanMxMapper repairPlanMxMapper;
    @Autowired
    private TKTaskMapper tkTaskMapper;

    @Autowired
    private IStationLxService stationLxService;

    /**
     * 删除在途及业务数据
     *
     * @param containerNo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContainerNo(String containerNo) throws Exception {
        logger.info("++++++++++++++++++++++++++++正在删除"+containerNo+"的相关在途信息+++++++++++++++++++++++++");
        List<ZtStore> ztStores = ztStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), ZtStore.class);
        if (CollectionUtils.isEmpty(ztStores)) {
            throw new Exception("该料箱无在途数据");
        }
        ZtStore ztStore = ztStores.get(0);
        //20 - 播种  30 - 合箱  40 - 盘点

        this.deleteContainerNoTask(ztStore.getTaskType(),containerNo);

        // 若该料箱还存在异常数据在拣选位上,则修改该拣选位,方便其他料箱继续进行作业
        List<StationLxPosition> stationLxPositions = stationLxService.findByMap(MapUtils.put("containerNo", containerNo).getMap());
        if (stationLxPositions.size()>0){
            StationLxPosition stationLxPosition = stationLxPositions.get(0);
            stationLxPosition.setStatus(0);
            stationLxPosition.setContainerNo(null);
            stationLxPosition.setContainerDirection(null);
            stationLxService.update(stationLxPosition);
        }

        //删除在途数据
        ztStoreMapper.deleteById(ztStore.getId(), ZtStore.class);
        String taskId = ztStore.getTaskId();
        if (wcsTaskMapper.findById(taskId, WCSTask.class) != null) {
            iwcsTaskService.finishTask(taskId, true);
        }

        //料箱退库成功，此料箱与商品解除绑定料箱置空
        containerSubService.updateContainerSubByContainer(containerNo);


    }

    @Override
    public void deleteContainerNoTask(int taskType,String containerNo) throws Exception {
        switch (taskType) {

            case SxStore.TASKTYPE_PD:
                List<PdTaskDetail> pdTaskDetails = pdTaskDetailMapper.findByMap(
                        MapUtils.put("containerNo", containerNo).getMap(), PdTaskDetail.class);
                if (CollectionUtils.isEmpty(pdTaskDetails)) {
                    break;
                }

                PdTaskDetailHis pdTaskDetailHis = new PdTaskDetailHis();
                PdTaskDetail source = pdTaskDetails.get(0);
                BeanUtils.copyProperties(source, pdTaskDetailHis);
                pdTaskDetailMapper.deleteById(source.getId(), PdTaskDetail.class);
                pdTaskDetailHisMapper.save(pdTaskDetailHis);

                break;
            case SxStore.TASKTYPE_HX:
                //当前执行料箱的汇总Id
                int hzId = assemblBoxMxMapper.getAssemblBoxMxDetail(containerNo);

                List<AssemblBoxMx> mx = assemblBoxMxMapper.findByMap(MapUtils.put("containerNo", containerNo).put("assemblBoxHzId", hzId).getMap(), AssemblBoxMx.class);
                if (CollectionUtils.isEmpty(mx)) {
                    break;
                }

                AssemblBoxMx assemblBoxMx = mx.get(0);
                assemblBoxMx.setTaskState(AssemblBoxMx.TASK_STATE_ERROR);
                assemblBoxMxMapper.update(assemblBoxMx);

                List<AssemblBoxMx> assemblBoxHzId = assemblBoxMxMapper.findByMap(MapUtils.put("assemblBoxHzId", hzId).getMap(), AssemblBoxMx.class);
                long count = assemblBoxHzId.stream().filter(x -> x.getTaskState() == AssemblBoxMx.TASK_STATE_WKS || x.getTaskState() == AssemblBoxMx.TASK_STATE_JXZ).count();
                //可以转历史
                if (count == 0) {
                    List<AssemblBoxMxHistory> histories = new ArrayList<>();
                    for (AssemblBoxMx boxMx : assemblBoxHzId) {
                        AssemblBoxMxHistory history = new AssemblBoxMxHistory();
                        BeanUtils.copyProperties(boxMx, histories);
                        history.setCreateTime(new Date());
                        histories.add(history);
                    }
                    //删除数据，转历史
                    assemblBoxMxHistoryMapper.saveBatch(histories);
                    assemblBoxMxMapper.deleteByMap(MapUtils.put("assemblBoxHzId", hzId).getMap(), AssemblBoxMx.class);

                    AssemblBoxHzHistory hzHistory = new AssemblBoxHzHistory();
                    AssemblBoxHz assemblBoxHz = assemblBoxHzMapper.findById(hzId, AssemblBoxHz.class);
                    BeanUtils.copyProperties(assemblBoxHz, hzHistory);
                    hzHistory.setTaskState(AssemblBoxHz.TASK_STATE_FINISH);
                    assemblBoxHzHistoryMapper.save(hzHistory);
                    assemblBoxHzMapper.deleteById(assemblBoxHz.getId(), AssemblBoxHz.class);
                    break;
                }
            case SxStore.TASKTYPE_BZ:
                //删除数据转历史
                iwcsCallbackService.deleteBz(containerNo,1);
                break;
            case SxStore.TASKTYPE_BH:
                List<RepairPlanMx> repairPlanMxs = repairPlanMxMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), RepairPlanMx.class);
                if (repairPlanMxs.size() > 0){
                    repairPlanMxMapper.deleteByMap(MapUtils.put("containerNo", containerNo).getMap(), RepairPlanMx.class);
                }

                break;
                //退库
            case SxStore.TASKTYPE_TK:
                List<TKTask> taskList = tkTaskMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), TKTask.class);
                if (taskList.size() > 0){
                    tkTaskMapper.deleteByMap(MapUtils.put("containerNo", containerNo).getMap(), TKTask.class);
                }


        }
    }
}
