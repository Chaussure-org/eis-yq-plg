package com.prolog.eis.wcs.impl;

import com.prolog.eis.boxbank.rule.CarTaskCountRule;
import com.prolog.eis.boxbank.rule.StoreLocationDTO;
import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dto.base.ContainerDTO;
import com.prolog.eis.dto.store.StoreTaskDto;
import com.prolog.eis.enums.HoisterEnum;
import com.prolog.eis.model.log.Log;
import com.prolog.eis.model.order.ContainerBindingHz;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.eis.model.point.PointLocation;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.SxStoreLocation;
import com.prolog.eis.pickstation.client.UpdateClientStationInfoService;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.pickstation.model.StationOrderPosition;
import com.prolog.eis.pickstation.service.IStationDdxService;
import com.prolog.eis.pickstation.service.IStationLxService;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.inbound.IInboundService;
import com.prolog.eis.service.log.ILogService;
import com.prolog.eis.service.order.*;
import com.prolog.eis.service.pickorder.IPickOrderHistoryService;
import com.prolog.eis.service.pickorder.IPickOrderService;
import com.prolog.eis.service.pointlocation.IPointLocationService;
import com.prolog.eis.service.store.IStoreLocationService;
import com.prolog.eis.service.store.IStoreService;
import com.prolog.eis.service.store.IStoreTaskService;
import com.prolog.eis.service.tk.TkTaskService;
import com.prolog.eis.util.CollectionUtils;
import com.prolog.eis.util.ContainerUtils;
import com.prolog.eis.util.DistanceUtils;
import com.prolog.eis.util.TaskUtils;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.dto.*;
import com.prolog.eis.wcs.model.WCSCommand;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCallbackService;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.core.exception.ParameterException;
import com.prolog.framework.utils.JsonUtils;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WCSCallbackServiceImpl implements IWCSCallbackService {
    private final Logger logger = LoggerFactory.getLogger(WCSCallbackServiceImpl.class);

    @Autowired
    private IStationLxService stationLxService;
    @Autowired
    private IStationDdxService stationDdxService;
    @Autowired
    private IStationService stationService;
    @Autowired
    private IWCSCommandService commandService;
    @Autowired
    private TkTaskService tkTaskService;
    @Autowired
    private IInboundService inboundService;
    @Autowired
    private IWCSTaskService taskService;
    @Autowired
    private IStoreTaskService storeTaskService;
    @Autowired
    private IPointLocationService pointService;
    @Autowired
    private IStoreService storeService;
    @Autowired
    private IStoreLocationService locationService;
    @Autowired
    private IStoreLocationService storeLocationService;
    @Autowired
    private IPointLocationService pointLocationService;
    @Autowired
    private EisProperties properties;
    @Autowired
    private IContainerBindingHzService containerBindingHzService;
    @Autowired
    private IContainerSubBingdingMxService containerSubBingdingMxService;
    @Autowired
    private IPickOrderHistoryService pickOrderHistoryService;
    @Autowired
    private IOrderMxService orderMxService;
    @Autowired
    private IOrderHzService orderHzService;
    @Autowired
    private IOrderMxHistoryService orderMxHistoryService;
    @Autowired
    private IOrderHzHistoryService orderHzHistoryService;
    @Autowired
    private IPickOrderService pickOrderService;
    @Autowired
    private IWCSService iwcsService;
    @Autowired
    private ILogService logService;
    @Autowired
    private CarTaskCountRule carTaskCountRule;
    @Autowired
    private UpdateClientStationInfoService updateClientStationInfoService;


    private final RestMessage<String> success = RestMessage.newInstance(true, "200", "操作成功", null);
    private final RestMessage<String> faliure = RestMessage.newInstance(false, "500", "操作失败", null);
    private final RestMessage<String> out = RestMessage.newInstance(false, "300", "订单箱异常", null);

    private void pwait() {
         /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 任务回告
     *
     * @param taskCallbackDTO
     * @return
     */
    @Override
    public RestMessage<String> executeTaskCallback(TaskCallbackDTO taskCallbackDTO) {
        pwait();
        if (taskCallbackDTO == null) {
            return success;
        }
        Log log = null;
        String number = null;
        if (taskCallbackDTO.getType() != 4) {
            try {
                log = new Log(null, ContainerUtils.parse(taskCallbackDTO.getContainerNo()).getNumber(), null, "WCS -> EIS", (int) taskCallbackDTO.getType(), JsonUtils.toString(taskCallbackDTO), null, null, new Date());
            } catch (Exception e) {
                return success;
            }
            if (taskCallbackDTO.getContainerNo() == null || "NoRead".equals(taskCallbackDTO.getContainerNo())) {
                toExecptionPoint(taskCallbackDTO.getAddress(), taskCallbackDTO.getContainerNo());
                return success;
            }
            number = ContainerUtils.parse(taskCallbackDTO.getContainerNo()).getNumber();
        } else {
            try {
                log = new Log(null, null, null, "WCS -> EIS", (int) taskCallbackDTO.getType(), JsonUtils.toString(taskCallbackDTO), null, null, new Date());
            }catch (Exception e){
                return success;
            }
        }

        switch (taskCallbackDTO.getType()) {
            case Constant.TASK_TYPE_CK:
                try {
                    log.setDescri("出库任务回告");
                    this.doOutboundTask(taskCallbackDTO);
                    log.setSuccess(true);
                    logService.save(log);
                } catch (Exception e) {
                    toExecptionPoint(taskCallbackDTO.getAddress(),number);
                    logger.error("发送料箱到异常口失败",e);
                    logger.error("出库任务回调失败", e);
                    log.setSuccess(false);
                    log.setException(e.getMessage());
                    logService.save(log);
                    return success;

                }
                break;
            case Constant.TASK_TYPE_XZ:
                try {
                    log.setDescri("行走任务回告");
                    this.doXZTask(taskCallbackDTO);
                    log.setSuccess(true);
                    logService.save(log);
                } catch (Exception e) {
                    logger.error(e.getStackTrace().toString());
                    logger.error(log.getContainerNo()+"行走任务回调失败", e);
                    log.setSuccess(false);
                    log.setException(e.getMessage());
                    logService.save(log);
                    return success;

                }
                break;
            case Constant.TASK_TYPE_RK:
                try {
                    log.setDescri("入库任务回告");
                    this.doInboundTask(taskCallbackDTO);
                    log.setSuccess(true);
                    logService.save(log);
                } catch (Exception e) {
                    logger.error("入库任务回调失败", e);
                    log.setSuccess(false);
                    log.setException(e.getMessage());
                    logService.save(log);
                    return success;

                }
                break;
            case Constant.TASK_TYPE_HC:
                try {
                    log.setDescri("换层任务回告");
                    this.doHcTask(taskCallbackDTO);
                    log.setSuccess(true);
                    logService.save(log);
                } catch (Exception e) {
                    logger.error("换层任务回调失败", e);
                    log.setSuccess(false);
                    log.setException(e.getMessage());
                    logService.save(log);
                    return success;
                }
                break;
        }

        return success;
    }




    /**
     * BCR 回告
     *
     * @param bcrDataDTO
     * @return
     */
    @Override
    public RestMessage<String> executeBCRCallback(BCRDataDTO bcrDataDTO) {
        pwait();
        if (bcrDataDTO == null) {
            return success;
        }
        List<PointLocation> pointLocation2 = pointLocationService.getPointByStation(0, PointLocation.TYPE_EXCEPTION_CONTAINER);
        String number = ContainerUtils.parse(bcrDataDTO.getContainerNo()).getNumber();
        Log log = null;
        try {
            log = new Log(null, ContainerUtils.parse(bcrDataDTO.getContainerNo()).getNumber(), null, "WCS -> EIS", (int) bcrDataDTO.getType(), JsonUtils.toString(bcrDataDTO), null, null, new Date());
            switch (bcrDataDTO.getType()) {
                case Constant.TASK_TYPE_LXJZ:
                    // 料箱申请进站
                    log.setDescri("料箱申请进站回告");
                    stationLxService.lxApplyFor(bcrDataDTO);
                    log.setSuccess(true);
                    logService.save(log);
                    return success;
                case Constant.TASK_TYPE_DDKJZ:
                    // 申请订单箱入站
                    log.setDescri("订单箱申请进站回告");
                    boolean b = stationDdxService.checkDdxStatus(bcrDataDTO);
                    log.setSuccess(true);
                    logService.save(log);
                    if (b) {
                        return success;
                    } else {
                        return out;
                    }
                case Constant.TASK_TYPE_SHAPE_INSPECT:
                    log.setDescri("体积检测回告");
                    this.shapeInspect(bcrDataDTO);
                    log.setSuccess(true);
                    logService.save(log);
                    return success;
                case Constant.TASK_TYPE_RKK:
                    log.setDescri("bcr入库任务回告");
                    inboundService.inboundTaskCallback(bcrDataDTO);
                    log.setSuccess(true);
                    logService.save(log);
                    return success;
                default:
                    return success;
            }
        } catch (Exception e) {
            toExecptionPoint(bcrDataDTO.getAddress(),number);
            logger.error("发送料箱到异常口失败");
            logger.error("bcr回告处理失败，" + e.getMessage(), e);
            log.setSuccess(false);
            log.setException(e.getMessage());
            logService.save(log);
            return success;
        }
    }

    /**
     * 料箱、订单框到位回告
     *
     * @param boxCallbackDTO
     * @return
     */
    @Override
    public RestMessage<String> executeBoxArriveCallback(BoxCallbackDTO boxCallbackDTO) {
        pwait();
        if (boxCallbackDTO == null) {
            return success;
        }
        Log log = null;
        PointLocation point = pointService.getPoint(boxCallbackDTO.getAddress());
        int stationId = point.getStationId();
        try {
            log = new Log(null, ContainerUtils.parse(boxCallbackDTO.getContainerNo()).getNumber(), null, "WCS -> EIS", (int) boxCallbackDTO.getType(), JsonUtils.toString(boxCallbackDTO), null, null, new Date());
            if (boxCallbackDTO.getContainerNo()==null || "NoRead".equals(boxCallbackDTO.getContainerNo())){
                toExecptionPoint(boxCallbackDTO.getAddress(),boxCallbackDTO.getContainerNo());
                if (boxCallbackDTO.getType()==Constant.TASK_TYPE_DDK_ARRIVE){
                    List<WCSTask> tasks = taskService.getByTarget(boxCallbackDTO.getAddress());
                    taskService.finishTask(tasks.get(0).getId(),false);
                }
                return success;
            }
            switch (boxCallbackDTO.getType()) {
                case Constant.TASK_TYPE_LX_ARRIVE:
                    // 料箱到位
                    log.setDescri("料箱到位任务回告");
                    stationLxService.lxInOrOut(stationId, boxCallbackDTO.getContainerNo(), point.getPointId(), true);
                    log.setSuccess(true);
                    logService.save(log);
                    return success;
                case Constant.TASK_TYPE_DDK_ARRIVE:
                    // 订单箱到位
                    log.setDescri("订单箱到位任务回告");
                    stationDdxService.ddxInplace(boxCallbackDTO);
                    log.setSuccess(true);
                    logService.save(log);
                    return success;
                default:
                    return success;
            }

        } catch (Exception e) {
            logger.error("箱体到位回告处理失败，" + e.getMessage(), e);
            log.setSuccess(false);
            log.setException(e.getMessage());
            logService.save(log);
            updateClientStationInfoService.sendMsgToClient("箱体"+boxCallbackDTO.getContainerNo()+"到位失败",0,new Date(),stationId);

            return success;
        }
    }

    /**
     * 料箱弹出完成回告
     *
     * @param boxCompletedDTO
     * @return
     */
    @Override
    public RestMessage<String> executeCompleteBoxCallback(BoxCompletedDTO boxCompletedDTO) {
        pwait();
        if (boxCompletedDTO == null) {
            return null;
        }
        Log log = null;
        PointLocation point = pointService.getPoint(boxCompletedDTO.getAddress());
        int stationId = point.getStationId();
        try {
            log = new Log(null, null, null, "WCS -> EIS", null, JsonUtils.toString(boxCompletedDTO), null, null, new Date());

            String containerNo = null;
            switch (point.getPointType()) {
                case PointLocation.TYPE_CONTAINER:
                    StationLxPosition stationLxPosition = stationLxService.findByMap(MapUtils.put("positionDeviceNo", boxCompletedDTO.getAddress()).getMap()).get(0);
                    containerNo = stationLxPosition.getContainerNo();
                    log.setDescri("料箱弹出完成任务回告");
                    log.setContainerNo(containerNo);
                    stationLxService.lxInOrOut(stationId, containerNo, point.getPointId(), false);
                    log.setSuccess(true);
                    logService.save(log);
                    return success;
                case PointLocation.TYPE_ORDER_BOX:
                    StationOrderPosition stationOrderPosition = stationDdxService.findByMap(MapUtils.put("deviceNo", boxCompletedDTO.getAddress()).getMap()).get(0);
                    containerNo = stationOrderPosition.getContainerNo();
                    if (containerNo==null||"NoRead".equals(containerNo)){
                        stationOrderPosition.setStatus((short) 0);
                        stationDdxService.update(stationOrderPosition);
                        return success;
                    }
                    log.setDescri("订单箱弹出完成任务回告");
                    log.setContainerNo(containerNo);
                    stationDdxService.ddxLeave(containerNo, stationId);
                    log.setSuccess(true);
                    logService.save(log);
                    return success;
                default:
                    return success;
            }
        } catch (Exception e) {
            logger.error("执行箱体弹出回调失败", e);
            completeExceptionToDao(boxCompletedDTO,point.getPointType());
            log.setSuccess(false);
            log.setException(e.getMessage());
            logService.save(log);
            updateClientStationInfoService.sendMsgToClient(boxCompletedDTO.getAddress()+"位置弹箱失败",0,new Date(),stationId);

            return success;
        }
    }

    private void completeExceptionToDao(BoxCompletedDTO boxCompletedDTO, int pointType) {
        switch (pointType){
            case PointLocation.TYPE_CONTAINER:
                StationLxPosition stationLxPosition = stationLxService.findByMap(MapUtils.put("positionDeviceNo", boxCompletedDTO.getAddress()).getMap()).get(0);
                stationLxPosition.setStatus(0);
                stationLxPosition.setContainerNo(null);
                stationLxPosition.setContainerDirection(null);
                stationLxService.update(stationLxPosition);
                break;
            case PointLocation.TYPE_ORDER_BOX:
                StationOrderPosition stationOrderPosition = stationDdxService.findByMap(MapUtils.put("deviceNo", boxCompletedDTO.getAddress()).getMap()).get(0);
                stationOrderPosition.setStatus((short)0);
                stationOrderPosition.setContainerNo(null);
                if (!stationOrderPosition.isChanged()) {
                    stationOrderPosition.setOrderId(0);
                }
                stationDdxService.update(stationOrderPosition);
                break;
            default:
                logger.error("处理错误");
        }
    }

    /**
     * 拍灯回告
     *
     * @param lightDTO
     * @return
     */
    @Override
    public RestMessage<String> executeLightCallback(LightDTO lightDTO) {
        if (lightDTO == null) {
            return success;
        }
        Log log = null;
        String stationNo = lightDTO.getStationNo();
        List<Station> stations = new ArrayList<>();
        try {
            stations = stationService.findByMap(MapUtils.put("stationNo", stationNo).getMap());
        }catch (Exception e){
        }
        try {
            log = new Log(null, null, "拍灯任务回告", "WCS -> EIS", null, JsonUtils.toString(lightDTO), null, null, new Date());

            String lightNo = lightDTO.getLightNo();
            stationLxService.seedLogic(stations.get(0).getId(), lightNo);
            log.setSuccess(true);
            logService.save(log);
            return success;
        } catch (Exception e) {
            logger.error("执行拍灯回调失败," + e.getMessage(), e);
            log.setSuccess(false);
            log.setException(e.getMessage());
            logService.save(log);
            updateClientStationInfoService.sendMsgToClient("拍灯成功",0,new Date(),stations.get(0).getId());
            return success;
        }
    }


    /**
     * 出库任务
     *
     * @param taskCallbackDTO
     */
    @Transactional(rollbackFor = Exception.class,timeout = 600)
    public void doOutboundTask(TaskCallbackDTO taskCallbackDTO) throws Exception {
        //校验数据
        if (taskCallbackDTO == null || taskCallbackDTO.getType() != Constant.TASK_TYPE_CK) {
            return;
        }

        WCSTask task = taskService.getTask(taskCallbackDTO.getTaskId());
        String containerNo = ContainerUtils.parse(taskCallbackDTO.getContainerNo()).getNumber();
        if (!task.getContainerNo().equals(containerNo))
            throw new ParameterException("容器号与任务号无法对应");

        //逻辑处理
        if (taskCallbackDTO.getStatus() == Constant.TASK_STATUS_START) {
            taskService.startTask(task.getId());
        } else if(taskCallbackDTO.getStatus() == Constant.TASK_STATUS_FINISH){
            String newTaskId = TaskUtils.gerenateTaskId();
            storeTaskService.finishOutboundTask(task.getContainerNo(), newTaskId);
            //完成上一个任务
            taskService.finishTask(task.getId(), true);

            //寻找新的目标点
            String address = null;
            List<PointLocation> locations = pointService.getPointByStation(task.getStationId(), PointLocation.TYPE_OUT);
            updateClientStationInfoService.sendMsgToClient("料箱"+containerNo+"到达出库接驳口",1,new Date(),task.getStationId());
            if (locations.size() > 0 ){
                //回告点位和查询点位不一致
                if(!locations.get(0).getPointId().equals(taskCallbackDTO.getAddress())){
                    address = taskCallbackDTO.getAddress();
                }else {
                    address = locations.get(0).getPointId();
                }
            }else {
                address = taskCallbackDTO.getAddress();
            }
//            address = locations.size() > 0 ? locations.get(0).getPointId() : taskCallbackDTO.getAddress();

            String target = this.findNextLocation(task);

            //生成新命令
            WCSCommand cmd = new WCSCommand(newTaskId, Constant.COMMAND_TYPE_XZ, address, target, task.getContainerNo());
            //如果是退库任务，直接发送指令，完成退库任务
            if (task.getEisType() == SxStore.TASKTYPE_TK) {
                tkTaskService.finishTask(task.getContainerNo());
            } else {
                //生成新的行走任务
                WCSTask newTask = new WCSTask();
                newTask.setId(newTaskId);
                newTask.setStationId(task.getStationId());
                newTask.setEisType(task.getEisType());
                newTask.setWcsType(Constant.TASK_TYPE_XZ);
                newTask.setStatus(WCSTask.STATUS_RUNNING);
                newTask.setContainerNo(taskCallbackDTO.getContainerNo());
                newTask.setAddress(address);
                newTask.setTarget(target);
                newTask.setGmtStartTime(new Date());
                newTask.setGmtCreateTime(new Date());
                taskService.add(newTask);
            }
            //发送行走指令
            commandService.add(cmd);
        }else if (taskCallbackDTO.getStatus() == Constant.TASK_STATUS_EXCEPTION){
            // 取货无货异常处理
            /**
             * 1.锁异常货位
             * 2.任务变异常,转历史
             * 3.考虑播种能重新找个箱子(如果找,则需要考虑是否争对当前类型)
             */
            SxStore sx = storeService.getByContinerNo(containerNo);
            sx.setTaskId(null);
            sx.setHoisterId(null);
            sx.setStoreState(SxStore.STATE_UP);
            storeService.update(sx);
            if (sx.getTaskType()==20) {
                this.deleteBz(containerNo,0);
            }
            updateClientStationInfoService.sendMsgToClient("料箱"+containerNo+"取货失败,",1,new Date(),task.getStationId());

            locationService.unlockRelate(task.getAddress());
            taskService.finishTask(task.getId(),false);
        }
    }

    @Override
    public void deleteBz(String containerNo,int i) throws Exception{
        logger.error("++++++++++++++++++++++++++++播种流程取货无货异常及删除在途处理++++++++++++++++++++++++++");
        List<ContainerBindingHz> containerHz = containerBindingHzService.findByMap(MapUtils.put("containerNo", containerNo).getMap());
        if (containerHz.size() == 1) {
            containerBindingHzService.delete(containerNo);
            List<ContainerSubBindingMx> containerNoAsc = containerSubBingdingMxService.findByMap(MapUtils.put("containerNo",containerNo).getMap());
            containerSubBingdingMxService.deleteBatch(containerNoAsc);
        }
    }

    /**
     * 行走任务回告,入库点
     *
     * @param taskCallbackDTO
     */
    @Transactional(rollbackFor = Exception.class,timeout = 600)
    public void doXZTask(TaskCallbackDTO taskCallbackDTO) throws Exception {
        String containerNo = taskCallbackDTO.getContainerNo();
        containerNo = ContainerUtils.parse(containerNo).getNumber();
        WCSTask task = taskService.getByContainerNo(containerNo);
        if (task == null) {
            logger.warn("料箱号({})找不到相应任务", containerNo);
            return;
        }
        if (StringUtils.isBlank(task.getTarget())) {
            logger.warn("找不到任务目标点位");
            return;
        }

        if (taskCallbackDTO.getStatus() == Constant.TASK_STATUS_FINISH) {
            List<PointLocation> points = pointService.findByMap(MapUtils.put("pointType", PointLocation.TYPE_IN).getMap());
            //boolean isInbound = CollectionUtils.contains(points,x->x.getPointId().equals(task.getTarget()) && task.getStationId() == x.getStationId() );
            boolean isInbound = CollectionUtils.contains(points, x -> x.getPointId().equals(taskCallbackDTO.getAddress().trim()));
            if (isInbound) {
                //寻找箱库入库坐标
                PointLocation p = points.stream().filter(x -> x.getPointId().equals(taskCallbackDTO.getAddress().trim())).findFirst().get();
                List<PointLocation> pos = pointService.getPointByStation(p.getStationId(), PointLocation.TYPE_IN_STORE);
                if (pos.size() == 0)
                    throw new RuntimeException("找不到箱库入库点位");

                String address = pos.get(0).getPointId();
                //完成上一个任务
                taskService.finishTask(task.getId(), true);

                String taskId = TaskUtils.gerenateTaskId();
                //找库位 转库存
                SxStoreLocation location = storeTaskService.startInboundTask(containerNo, taskId,taskCallbackDTO.getAddress().trim());

                //入库触发自动换层
                StoreLocationDTO locationDTO = new StoreLocationDTO();
                locationDTO.setLayer(location.getLayer());
//                if(!carTaskCountRule.execute(locationDTO,null)){
//                    throw new RuntimeException("小车任务数到达上限，稍后重试");
//                }

                //入库任务
                WCSTask newtask = new WCSTask();
                newtask.setId(taskId);
                newtask.setStationId(0);
                newtask.setEisType(task.getEisType());
                newtask.setWcsType(Constant.TASK_TYPE_RK);
                newtask.setStatus(WCSTask.STATUS_WAITTING);
                newtask.setContainerNo(containerNo);
                newtask.setAddress(address);
                newtask.setTarget(location.getStoreNo());
                newtask.setGmtCreateTime(new Date());
                taskService.add(newtask);

                WCSCommand wcsCommand = new WCSCommand(taskId, Constant.COMMAND_TYPE_RK, address, location.getStoreNo(), containerNo,0);
                commandService.add(wcsCommand);

            }
        }

    }

    /**
     * 外形检测结果处理
     *
     * @param bcrDataDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void shapeInspect(BCRDataDTO bcrDataDTO) throws Exception {
        //根据检测结果判断下一步点位，若不合格则为异常剔除扣
        //若合格根据点位找到对应的入口点位
        String containerNo = bcrDataDTO.getContainerNo();
        ContainerDTO containerDTO = ContainerUtils.parse(containerNo);
        containerNo = containerDTO.getNumber();
        String taskId = TaskUtils.gerenateTaskId();
        String address = bcrDataDTO.getAddress();
        WCSTask ltask = taskService.getByContainerNo(containerNo);
        if ("NoRead".equals(containerNo) || !bcrDataDTO.getTaskId().equals(containerNo)) {
            List<PointLocation> pointLocation2 = pointLocationService.getPointByStation(0, PointLocation.TYPE_EXCEPTION_CONTAINER);
            String target2 = pointLocation2.get(0).getPointId();
            commandService.sendXZCommand(taskId, address, target2, containerNo);
            return;
        }


        if (ltask == null) {
            throw new RuntimeException("找不到料箱(" + containerNo + ")对应的任务");
        }
        if (ltask.getAddress().equals(pointLocationService.getPointByStation(ltask.getStationId(),PointLocation.TYPE_CONTAINER_BCR).get(0).getPointId()) &&
        ltask.getTarget().equals("C04")){
            List<PointLocation> pointLocation2 = pointLocationService.getPointByStation(0, PointLocation.TYPE_EXCEPTION_CONTAINER);
            String target2 = pointLocation2.get(0).getPointId();
            commandService.sendXZCommand(taskId, address, target2, containerNo);
            updateClientStationInfoService.sendMsgToClient("料箱"+containerNo+"发往异常口",0,new Date(),ltask.getStationId());
            return;
        }
        //结束上一个任务
        taskService.finishTask(ltask.getId(), true);


        String target = "";
        if (bcrDataDTO.isShapeInspect()) {
            PointLocation point = pointService.getPoint(address);

            List<PointLocation> points = pointService.getPointByStation(ltask.getStationId(), PointLocation.TYPE_IN);
            // 安全门
            points = points.stream().filter(x->!x.isDisable()).collect(Collectors.toList());
            if (points.size() == 0) {
                points = pointService.getPointByType(PointLocation.TYPE_IN);
                points = points.stream().filter(x->!x.isDisable()).collect(Collectors.toList());
                if (points.size() == 0){
                    throw new ParameterException("找不到站台对应的入库点位");
                }
            }
            //判断该点位的提升机是否故障，故障则切换其他入库口
            target = points.get(0).getPointId();

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
            // 若该回库料箱还残留播种任务相关数据,那么直接删除后回库
            List<ContainerBindingHz> containerBindingHzs = containerBindingHzService.findByMap(MapUtils.put("containerNo", containerNo).getMap());
            List<ContainerSubBindingMx> containerSubBindingMxes = containerSubBingdingMxService.findByMap(MapUtils.put("containerNo", containerNo).getMap());
            if (containerBindingHzs.size()>0){
                containerBindingHzService.deleteBatch(containerBindingHzs);
            }
            if (containerSubBindingMxes.size()>0){
                containerSubBingdingMxService.deleteBatch(containerSubBindingMxes);
            }
            updateClientStationInfoService.sendMsgToClient("料箱"+containerNo+"发往入库口",1,new Date(),ltask.getStationId());
            WCSTask task = new WCSTask();
            task.setId(taskId);
            task.setStationId(ltask.getStationId());
            task.setEisType(ltask.getEisType());
            task.setWcsType(Constant.TASK_TYPE_XZ);
            task.setStatus(WCSTask.STATUS_WAITTING);
            task.setContainerNo(containerNo);
            task.setAddress(address);
            task.setTarget(target);
            task.setGmtCreateTime(new Date());
            taskService.add(task);


        } else {
            List<PointLocation> pos = pointService.getPointByStation(0, PointLocation.TYPE_EXCEPTION_CONTAINER);
            if (pos.size() == 0)
                throw new ParameterException("找不到异常剔除口");
            target = pos.get(0).getPointId();
        }

        //生成行走命令 - 异步
        WCSCommand cmd = new WCSCommand(taskId, Constant.COMMAND_TYPE_XZ, address, target, containerNo);
        commandService.add(cmd);
    }

    /**
     * 入库任务回告
     *
     * @param taskCallbackDTO
     */
    @Transactional(rollbackFor = Exception.class,timeout = 600)
    public void doInboundTask(TaskCallbackDTO taskCallbackDTO) throws Exception {
        String containerNo = taskCallbackDTO.getContainerNo();
        containerNo = ContainerUtils.parse(containerNo).getNumber();
        WCSTask task = taskService.getByContainerNo(containerNo);
        if (task == null) {
            logger.warn("料箱号({})找不到相应任务", containerNo);
            return;
        }

        if (taskCallbackDTO.getStatus() == Constant.TASK_STATUS_START) {
            taskService.startTask(task.getId());
        } else if (taskCallbackDTO.getStatus() == Constant.TASK_STATUS_FINISH) {
            storeTaskService.finishInboundTask(containerNo);
            List<WCSTask> tasks = taskService.getTaskByMap(MapUtils.put("containerNo", containerNo).getMap());
            for (WCSTask wcsTask : tasks) {
                if (task.getId().equals(wcsTask.getId())) {
                    taskService.finishTask(wcsTask.getId(),true);
                }else{
                    taskService.finishTask(wcsTask.getId(),false);
                }
            }
            taskService.finishTaskByContainerNo(containerNo,true);
        } else if (taskCallbackDTO.getStatus() == Constant.TASK_STATUS_EXCEPTION) {
            /**
             * 入库异常
             * 1.锁住当前异常位
             * 2.找到另一个合适的地址
             * 3.修改当前任务地址
             * 4.生成一条新的入库命令(添加status)
             */
            locationService.unlockRelate(task.getTarget());
            SxStoreLocation sxStoreLocation = locationService.getbyNumber(task.getTarget());
            //找到同一层的另一个位置
            int layer = sxStoreLocation.getLayer();
            List<SxStoreLocation> list = storeLocationService.findAvailableLocationByLayer(layer);
            SxStoreLocation location = null;
            double distance = 0;
            for (SxStoreLocation alocation : list) {
                if (location == null) {
                    location = alocation;
                    distance = DistanceUtils.toDistance(properties.getBasePointX(), properties.getBasePointY(), alocation.getX(), alocation.getY());
                    continue;
                } else {
                    double d = DistanceUtils.toDistance(properties.getBasePointX(), properties.getBasePointY(), alocation.getX(), alocation.getY());
                    if (d < distance) {
                        distance = d;
                        location = alocation;
                    }
                }
            }
            //加锁
            locationService.lock(location.getId());
            SxStore store = storeService.getByTaskId(task.getId());
            store.setStoreLocationId(location.getId());
            storeService.update(store);
            task.setTarget(location.getStoreNo());
            taskService.update(task);
            WCSCommand wcsCommand = new WCSCommand(task.getId(), Constant.COMMAND_TYPE_RK, task.getAddress(), location.getStoreNo(), containerNo,1);
            commandService.add(wcsCommand);
        }
    }

    /**
     * 换层任务回告
     * @param taskCallbackDTO
     */
    private void doHcTask(TaskCallbackDTO taskCallbackDTO) throws Exception {
        String taskId = taskCallbackDTO.getTaskId();
        WCSTask task = taskService.getTask(taskId);
        if (task == null) {
            throw new Exception("没有找到"+taskId+"对应的任务");
        }
        taskService.finishTask(taskId,true);
    }

    /**
     * 寻找下一个出库任务目标点
     *
     * @param task
     * @return
     */
    private String findNextLocation(WCSTask task) {
        int eisType = task.getEisType();
        String target = "";
        if (eisType == SxStore.TASKTYPE_BH) {
            // 补货
            List<PointLocation> locations = pointService.getPointByStation(0, PointLocation.TYPE_IN_BCR);
            target = locations.size() > 0 ? locations.get(0).getPointId() : "";
        } else if (eisType == SxStore.TASKTYPE_TK) {
            //退库
            List<PointLocation> locations = pointService.getPointByStation(0, PointLocation.TYPE_EXCEPTION_CONTAINER);
            target = locations.size() > 0 ? locations.get(0).getPointId() : "";
        } else if (eisType == SxStore.TASKTYPE_BZ || eisType == SxStore.TASKTYPE_HX || eisType == SxStore.TASKTYPE_PD) {
            //播种 合箱 盘点
            List<PointLocation> locations = pointService.getPointByStation(task.getStationId(), PointLocation.TYPE_CONTAINER_BCR);
            target = locations.size() > 0 ? locations.get(0).getPointId() : "";
        }
        return target;
    }

    private void toExecptionPoint(String address,String number){
        try {
            List<PointLocation> pointLocation = null;
            PointLocation point = pointLocationService.getPoint(address);
            if (point.getPointType()==PointLocation.TYPE_ORDER_BOX||point.getPointType()==PointLocation.TYPE_ORDER_BOX_BCR) {
                pointLocation = pointLocationService.getPointByStation(0, PointLocation.TYPE_EXCEPTION_ORDER_BOX);
            } else {
                pointLocation = pointLocationService.getPointByStation(0, PointLocation.TYPE_EXCEPTION_CONTAINER);
            }
            String target = pointLocation.get(0).getPointId();
            String taskId = TaskUtils.gerenateTaskId();
            commandService.sendXZCommand(taskId, address, target, number);
        }catch(Exception e1){
            logger.error("发送料箱到异常口失败",e1);
        }
    }
}
