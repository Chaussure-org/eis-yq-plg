package com.prolog.eis.service.pd.impl;

import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dao.pd.PdTaskDetailMapper;
import com.prolog.eis.dao.pd.PdTaskMapper;
import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.dto.pd.PdTaskDetailDto;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.pd.PdTask;
import com.prolog.eis.model.pd.PdTaskDetail;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.service.pd.PdTaskService;
import com.prolog.framework.core.exception.ParameterException;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import com.prolog.framework.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PdTaskServiceImpl implements PdTaskService {

    @Autowired
    private PdTaskMapper pdTaskMapper;
    @Autowired
    private PdTaskDetailMapper pdTaskDetailMapper;
    @Autowired
    private StoreMapper sxStoreMapper;
    @Autowired
    private ContainerSubMapper containerSubMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savepdDetail(String remark, List<PdTaskDetailDto> spList) throws Exception {
        //盘点需要添加字段
        //coding ...

        // 数据插入pdTask
        PdTask pdTask = new PdTask();
        pdTask.setPdNo("EIS" + UUIDUtils.generateUUID().getMostSignificantBits());// 随机生成盘点单据号
        pdTask.setRemark(remark);// 备注
        pdTask.setPdState(10);// 默认状态10已生成
        pdTask.setPdType(1);// 默认1Eis内部盘点
        pdTask.setCreateTime(new Date());// 创建时间当前时间
        // 保存到pdTask
        pdTaskMapper.save(pdTask);
        //再保存盘点明细数据
        ArrayList<PdTaskDetail> pDetails = new ArrayList<>();
        // 此次保存的货格号集合
        List<String> subNos = new ArrayList<String>();
        // 明细插入数据
        for (PdTaskDetailDto dto : spList) {
            //判断当前list有没有重复货格号
            if (subNos.contains(dto.getContainerSubNo())) {
                // 当前保存的数据有重复的货格号
                throw new Exception("货格号重复");
            }
            subNos.add(dto.getContainerSubNo());
            PdTaskDetail pdTaskDetail = new PdTaskDetail();
            pdTaskDetail.setPdTaskId(pdTask.getId());
            pdTaskDetail.setContainerNo(dto.getContainerNo());
            String containerSubNo = dto.getContainerSubNo();
            ContainerSub containerSub = containerSubMapper.findById(containerSubNo, ContainerSub.class);
            pdTaskDetail.setContainerSubNo(containerSubNo);
            pdTaskDetail.setGoodsNo(dto.getGoodsNo());
            pdTaskDetail.setGoodsName(dto.getGoodsName());
            pdTaskDetail.setBarCode(dto.getGoodsBarCode());
            pdTaskDetail.setOriginalCount(containerSub.getCommodityNum());
            pdTaskDetail.setCreateTime(new Date());
            pdTaskDetail.setPdType(dto.getPdType());
            pDetails.add(pdTaskDetail);
        }
        // 批量插入数据
        if (pDetails.size() > 0) {
            pdTaskDetailMapper.saveBatch(pDetails);
        } else {
            pdTaskMapper.deleteById(pdTask.getId(), PdTask.class);
        }
    }

    @Override
    @Transactional
    public void cancelTask(int pdTaskId) throws Exception{

        PdTask pdTask = pdTaskMapper.findById(pdTaskId, PdTask.class);
        if (pdTask.getPdState() != 10) {
            throw new Exception("任务已下发无法取消");
        }
        pdTaskDetailMapper.deleteByMap(MapUtils.put("pdTaskId", pdTaskId).getMap(), PdTaskDetail.class);
        pdTaskMapper.deleteById(pdTaskId, PdTask.class);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishTask(List<Integer> pdTaskIds) throws Exception {

        List<String> pdContainerNos = new ArrayList<>();
        for (Integer pdTaskId : pdTaskIds) {
            List<String> pdTaskIdList = pdTaskDetailMapper.findByMap(MapUtils.put("pdTaskId", pdTaskId).getMap(), PdTaskDetail.class).stream().map(PdTaskDetail::getContainerNo).collect(Collectors.toList());
            pdContainerNos.addAll(pdTaskIdList);

            Date date = new Date();
            pdTaskMapper.updateMapById(pdTaskId, MapUtils.put("pdState", 20).put("xiafaTime", date).getMap(), PdTask.class);
            pdTaskDetailMapper.updatePdTaskDetailState(pdTaskId);
        }
        //对容器进行校验
        for (String pdContainerNo : pdContainerNos) {
            checkPublishTask(pdContainerNo);
            sxStoreMapper.updateStoreState(pdContainerNo, SxStore.TASKTYPE_PD);
        }

    }

    //校验任务是否可以下发
    @Override
    public void checkPublishTask(String containerNo) throws Exception {

        List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), SxStore.class);
        if (sxStores.size() == 0) {
            throw new Exception("[" + containerNo + "]" + "容器不在库存中，无法下发");
        }
        SxStore sxStore = sxStores.get(0);
        if (!StringUtils.isEmpty(sxStore.getTaskId())) {
            throw new Exception("[" + containerNo + "]" + "容器存在其他任务，无法下发");
        }
        if (sxStore.getStoreState() != SxStore.STATE_UP) {
            throw new Exception("[" + containerNo + "]" + "容器未上架，无法下发");
        }
        if (sxStore.getTaskType() != SxStore.TASKTYPE_NULL) {
            throw new Exception("[" + containerNo + "]" + "容器存在其他任务，无法下发");
        }

    }

    //获取哪些可以下发盘点的容器
    public List<PdTaskDetail> getPdTaskDetail() {
        return pdTaskDetailMapper.getPdTaskDetail();

    }

    /**
     * 更新盘点任务状态
     *
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(int id, int status) {
        pdTaskMapper.updateMapById(id, MapUtils.put("pdState", status).getMap(), PdTask.class);
    }

    /**
     * 根据容器编号更新盘点任务状态
     *
     * @param containerNo
     * @param status
     */
    @Override
    public void updateStatusByContainerNo(String containerNo, int status) throws Exception {
        List<PdTaskDetail> list = pdTaskDetailMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), PdTaskDetail.class);
        if (list.size() == 0)
            throw new ParameterException("找不到容器相关盘点任务");

        this.updateStatus(list.get(0).getPdTaskId(), status);
    }


}
