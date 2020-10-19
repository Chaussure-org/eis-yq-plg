package com.prolog.eis.service.inbound.impl;

import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dao.inbound.InboundTaskHistoryMapper;
import com.prolog.eis.dao.inbound.InboundTaskMapper;
import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dto.container.ContainerSubIRDto;
import com.prolog.eis.dto.store.StoreTaskDto;
import com.prolog.eis.dto.yqfs.ContainerSubDto;
import com.prolog.eis.enums.HoisterEnum;
import com.prolog.eis.model.inbound.InboundTask;
import com.prolog.eis.model.inbound.InboundTaskHistory;
import com.prolog.eis.model.log.Log;
import com.prolog.eis.model.point.PointLocation;
import com.prolog.eis.model.repair.InboundRepairInfo;
import com.prolog.eis.model.repair.RepairPlanMx;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.ZtStore;
import com.prolog.eis.service.inbound.IEmptyInboundTaskService;
import com.prolog.eis.service.inbound.IInboundService;
import com.prolog.eis.service.log.ILogService;
import com.prolog.eis.service.pointlocation.IPointLocationService;
import com.prolog.eis.service.repair.InboundRepairInfoService;
import com.prolog.eis.service.repair.RepairPlanService;
import com.prolog.eis.service.repair.ReplenishmentService;
import com.prolog.eis.service.store.IStoreLocationService;
import com.prolog.eis.service.store.IStoreService;
import com.prolog.eis.service.store.IZtSoreService;
import com.prolog.eis.util.ContainerUtils;
import com.prolog.eis.util.TaskUtils;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.dto.BCRDataDTO;
import com.prolog.eis.wcs.dto.HoisterInfoDto;
import com.prolog.eis.wcs.model.WCSCommand;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.utils.MapUtils;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InboundTaskServiceImpl implements IInboundService {
    private final Logger logger = LoggerFactory.getLogger(InboundTaskServiceImpl.class);
    @Autowired
    private IWCSCommandService commandService;
    @Autowired
    private IWCSTaskService taskService;
    @Autowired
    private IPointLocationService pointService;
    @Autowired
    private RepairPlanService repairPlanService;
    @Autowired
    private InboundTaskMapper inboundTaskMapper;
    @Autowired
    private IEmptyInboundTaskService emptyInboundTaskService;
    @Autowired
    private IZtSoreService ztSoreService;
    @Autowired
    private IStoreService storeService;
    @Autowired
    private EisProperties properties;
    @Autowired
    private InboundTaskHistoryMapper inboundTaskHistoryMapper;
    @Autowired
    private ReplenishmentService replenishmentService;
    @Autowired
    private IWCSService iwcsService;
    @Autowired
    private IStoreLocationService storeLocationService;
    @Autowired
    private ContainerSubMapper containerSubMapper;
    @Autowired
    private InboundRepairInfoService inboundRepairInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inboundTaskCallback(BCRDataDTO bcrDataDTO) throws Exception {
        WCSCommand wcsCommand = null;
        String containerNo = bcrDataDTO.getContainerNo();
        containerNo = ContainerUtils.parse(containerNo).getNumber();



        List<PointLocation> pointLocations = pointService.getPointByStation(0,PointLocation.TYPE_IN_BCR);
        if(pointLocations.size()==0)
            throw new RuntimeException("找不到入口点位");

        String address = pointLocations.get(0).getPointId();

        //外形检测不合格
        if(!bcrDataDTO.isShapeInspect()){
            this.exitContainer(address,containerNo);
            logger.info("体积不合格");
            return;
        }

        //重量检测不合格
        double weight = Double.parseDouble(bcrDataDTO.getWeightInspect());
        if(weight>properties.getLimitWeight()){
            this.exitContainer(address,containerNo);
            logger.info("重量不合格{}>{}",weight,properties.getLimitWeight());
            return;
        }

        //查询补货任务表，判断是否为补货任务

        List<RepairPlanMx> repairPlanMxList = repairPlanService.getRepairPlanMx(MapUtils.put("containerNo", containerNo).getMap());
        List<RepairPlanMx> repairPlanMxs = repairPlanMxList.stream().filter(x -> x.getRepairStatus() == 3).collect(Collectors.toList());
        //查询入库料箱是否已存在库存或在途
        SxStore storeContinerNo = storeService.getByContinerNo(containerNo);
        //1、有补货明细且库存存在剔除   2、无补货明细且有在途剔除 3、无补货明细且有库存剔除
        List<ZtStore> ztStores = ztSoreService.findByMap(MapUtils.put("containerNo", containerNo).getMap());
        if ((repairPlanMxs.size() > 0 && storeContinerNo != null) || (repairPlanMxs.size() == 0 && (storeContinerNo != null || ztStores.size() > 0))){
                List<PointLocation> pointByStation = pointService.getPointByStation(0, PointLocation.TYPE_EXCEPTION_CONTAINER);
                if (pointByStation.size() == 0){
                    throw new RuntimeException("找不到异常剔除口点位");
                }
                wcsCommand = new WCSCommand(TaskUtils.gerenateTaskId(),Constant.COMMAND_TYPE_XZ,address,pointByStation.get(0).getPointId(),containerNo);
                commandService.add(wcsCommand);
                logger.info("料箱库【{}】已存在，入库失败已剔除异常口",containerNo);
                return;
        }



        int taskType = SxStore.TASKTYPE_IN;
        //获取入库点位
        String target = taskService.getBestInboundLocation().getPointId();

        //提升机点位
        String hoisterId = HoisterEnum.getValues(target);
        //所有可用层
        List<Integer> availableLayer = storeLocationService.findAvailableLayer();
        //可用层对应提升机的任务数
        if (availableLayer.size() == 0) throw new Exception("没有可用层");
        int maxSize = availableLayer.size()*2;

        List<StoreTaskDto> storeTasks =  storeService.findStoreTasksByHoisId(hoisterId);
        if (storeTasks.size() >= maxSize){
            //如果一号提升机对应缓存任务数满了,就走二号提升机
            target = "R02";
            hoisterId = HoisterEnum.getValues(target);
        }
        //所有提升机的信息
        List<HoisterInfoDto> hoisterInfoDto = iwcsService.getHoisterInfoDto();
        //status=1 表示可用
        List<String> goodsHoister = hoisterInfoDto.stream().filter(x -> x.getStatus() == 1 && x.getCode() == 0).map(HoisterInfoDto::getHoist).collect(Collectors.toList());
        if (!goodsHoister.contains(hoisterId)) {
            target = HoisterEnum.getAddress(goodsHoister.get(0));
        }
        //判断改点位对应提升机是否故障，是则切换位置
        String taskId = TaskUtils.gerenateTaskId();

        if(repairPlanMxs.size()>0){
            taskType = SxStore.TASKTYPE_BH;
            //结束补货行走任务
            WCSTask btask = taskService.getByContainerNo(containerNo);
            if(btask!=null){
                taskService.finishTask(btask.getId(),true);
            }
            replenishmentService.deleteAllRepairTask(containerNo);
            //空箱入库、料箱入库
        }else if(
                inboundTaskMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), InboundTask.class).size()>0 ||
                        emptyInboundTaskService.getEmptyInboundTask(MapUtils.put("containerNo", containerNo).getMap()).size()>0
                ){
            taskType = SxStore.TASKTYPE_IN;
            //若存在生成在途库存，选择合适入库点
            ZtStore ztStore = new ZtStore();
            ztStore.setContainerNo(containerNo);
            ztStore.setTaskId(taskId);
            ztStore.setStatus(ZtStore.Status_To_Station);
            ztStore.setTaskType(SxStore.TASKTYPE_IN);
            ztStore.setStoreTime(new Date());
            ztStore.setLastUpdateTime(new Date());
            ztStore.setCreateTime(new Date());
            ztSoreService.addZtStore(ztStore);


            if ( emptyInboundTaskService.getEmptyInboundTask(MapUtils.put("containerNo", containerNo).getMap()).size()>0){

                //空箱入库转历史
                emptyInboundTaskService.toEmptyInboundHis(containerNo);
            }else {
                //料箱入库转历史
                this.toTaskHis(containerNo);
            }
            //9.4保存入库记录
            this.saveInboundInfo(containerNo);
        }else{
            //若都不存在，剔除异常口
            exitContainer(address,containerNo);
            return;
        }

        Integer stationId = pointService.getPoint(target).getStationId();

        //生成行走任务
        WCSTask task = new WCSTask();
        task.setId(taskId);
        task.setStationId(0);
        task.setEisType(taskType);
        task.setWcsType(Constant.TASK_TYPE_XZ);
        task.setStatus(WCSTask.STATUS_WAITTING);
        task.setContainerNo(containerNo);
        task.setAddress(address);
        task.setStationId(stationId);
        task.setTarget(target);
        task.setGmtCreateTime(new Date());
        taskService.add(task);

        wcsCommand = new WCSCommand(taskId,Constant.COMMAND_TYPE_XZ,address,target,containerNo);
        commandService.add(wcsCommand);
    }

    @Override
    public void toTaskHis(String containerNo) throws Exception {
        List<InboundTask> inboundTasks = inboundTaskMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), InboundTask.class);
        InboundTask inboundTask = inboundTasks.get(0);
        InboundTaskHistory inboundTaskHistory = new InboundTaskHistory();
        BeanUtils.copyProperties(inboundTask,inboundTaskHistory);
        inboundTaskHistory.setInboundStatus(2);
        inboundTaskHistoryMapper.save(inboundTaskHistory);
        inboundTaskMapper.deleteByMap(MapUtils.put("containerNo",containerNo).getMap(),InboundTask.class);
    }

    @Override
    public void saveInboundInfo(String containerNo) {
        List<ContainerSubIRDto> containerSubInfos = containerSubMapper.getContainerSubInfo(containerNo);
        if (containerSubInfos.size() == 0){
            return;
        }
        List<InboundRepairInfo> inboundRepairInfos = new ArrayList<>();
        for (ContainerSubIRDto containerSubInfo : containerSubInfos) {
            InboundRepairInfo inboundRepairInfo = new InboundRepairInfo();
            inboundRepairInfo.setContainerNo(containerSubInfo.getContainerNo());
            inboundRepairInfo.setContainerSubNo(containerSubInfo.getContainerSubNo());
            inboundRepairInfo.setCommodityNum(containerSubInfo.getCommodityNum());
            inboundRepairInfo.setGoodsId(containerSubInfo.getGoodsId());
            inboundRepairInfo.setCreateTime(new Date());
            inboundRepairInfo.setInboundStatu(InboundRepairInfo.INBOUND_STATUS);
            inboundRepairInfos.add(inboundRepairInfo);
        }
        inboundRepairInfoService.saveBatch(inboundRepairInfos);

    }

    /**
     * 剔除
     */
    private void exitContainer(String address,String containerNo) throws Exception {
        List<PointLocation> ep = pointService.getPointByStation(0,PointLocation.TYPE_EXCEPTION_CONTAINER);
        if(ep.size()==0)
            throw new RuntimeException("找不到异常剔除口点位");
        String target = ep.get(0).getPointId();
        WCSCommand wcsCommand = new WCSCommand(TaskUtils.gerenateTaskId(),Constant.COMMAND_TYPE_XZ,address,target,containerNo);
        commandService.add(wcsCommand);
    }

}
