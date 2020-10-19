package com.prolog.eis.pickstation.service.impl;

import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dao.pd.PdTaskDetailMapper;
import com.prolog.eis.dao.store.ZtStoreMapper;
import com.prolog.eis.dto.base.ContainerDTO;
import com.prolog.eis.dto.store.ZtLxDetailsDto;
import com.prolog.eis.model.masterbase.Container;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.order.ContainerBindingHz;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.eis.model.order.OrderHz;
import com.prolog.eis.model.order.OrderMx;
import com.prolog.eis.model.pd.PdTaskDetail;
import com.prolog.eis.model.point.PointLocation;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.ZtStore;
import com.prolog.eis.pickstation.client.SendCsClientService;
import com.prolog.eis.pickstation.client.UpdateClientStationInfoService;
import com.prolog.eis.pickstation.dao.StationLxMapper;
import com.prolog.eis.pickstation.dao.StationOrderMapper;
import com.prolog.eis.pickstation.model.*;
import com.prolog.eis.pickstation.service.ISeedInfoService;
import com.prolog.eis.pickstation.service.IStationHxService;
import com.prolog.eis.pickstation.service.IStationLxService;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.masterbase.ContainerService;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.eis.service.order.*;
import com.prolog.eis.service.pd.PdExecuteService;
import com.prolog.eis.service.pickorder.IPickOrderHistoryService;
import com.prolog.eis.service.pickorder.IPickOrderService;
import com.prolog.eis.service.pointlocation.IPointLocationService;
import com.prolog.eis.service.store.IStoreService;
import com.prolog.eis.service.store.IZtSoreService;
import com.prolog.eis.util.ContainerUtils;
import com.prolog.eis.util.GolableLockUtils;
import com.prolog.eis.util.StationLockUtils;
import com.prolog.eis.util.TaskUtils;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.dto.BCRDataDTO;
import com.prolog.eis.wcs.dto.BoxCallbackDTO;
import com.prolog.eis.wcs.model.WCSCommand;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.core.exception.ParameterException;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Order;
import com.prolog.framework.core.restriction.Restriction;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.prolog.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class StationLxServiceImpl implements IStationLxService {

    private final Logger logger = LoggerFactory.getLogger(StationLxServiceImpl.class);


    @Autowired
    private StationLxMapper stationLxMapper;
    @Autowired
    private IPointLocationService pointLocationService;
    @Autowired
    private SendCsClientService sendCsClientService;
    @Autowired
    private IZtSoreService ztSoreService;
    @Autowired
    private IContainerBindingHzService containerBindingHzService;
    @Autowired
    private IStationService stationService;
    @Autowired
    private StationOrderMapper stationOrderMapper;
    @Autowired
    private UpdateClientStationInfoService updateClientStationInfoService;
    @Autowired
    private IContainerSubBingdingMxService containerSubBingdingMxService;
    @Autowired
    private IPickOrderHistoryService pickOrderHistoryService;
    @Autowired
    private PdExecuteService pdExecuteService;
    @Autowired
    private IOrderMxService orderMxService;
    @Autowired
    private IPickOrderService pickOrderService;
    @Autowired
    private IOrderHzService orderHzService;
    @Autowired
    private IOrderHzHistoryService orderHzHistoryService;
    @Autowired
    private IWCSCommandService commandService;
    @Autowired
    private ContainerSubService containerSubService;
    @Autowired
    private IWCSTaskService taskService;
    @Autowired
    private IStoreService storeService;
    @Autowired
    private IOrderBoxService orderBoxService;
    @Autowired
    private IStationHxService stationHxService;
    @Autowired
    private IOrderMxHistoryService orderMxHistoryService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private UpdateClientStationInfoService clientStationInfoService;
    @Autowired
    private EisProperties properties;
    @Autowired
    private ISeedInfoService seedInfoService;

    /**
     * 料箱进站bcr回告
     *
     * @param bcrDataDTO
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lxApplyFor(BCRDataDTO bcrDataDTO) throws Exception {
        //获取点位信息
        String address = bcrDataDTO.getAddress();
        PointLocation point = pointLocationService.getPoint(address);
        if (point == null) {
            logger.error("BCR点位信息错误（" + address + "）");
            throw new RuntimeException("BCR点位信息错误（" + address + "）");
        }
        //添加站台锁
        int stationId = point.getStationId();
        Object obj = StationLockUtils.getInstance().getLock(stationId);
        synchronized (obj) {
            String containerNo = bcrDataDTO.getContainerNo(); //A
            ContainerDTO containerDTO = ContainerUtils.parse(containerNo);

            String taskId = TaskUtils.gerenateTaskId();
            WCSTask lastTask = null;
            //扫码错误处理
            if ("NoRead".equals(containerNo)) {
                List<PointLocation> pointLocation2 = pointLocationService.getPointByStation(0, PointLocation.TYPE_EXCEPTION_CONTAINER);
                String target2 = pointLocation2.get(0).getPointId();
                //若读到NoRead,则任务号为容器号
                //taskService.finishTask(lastTask.getId(), true);
                WCSCommand cmd = new WCSCommand();
                cmd.setTaskId(taskId);
                cmd.setType(Constant.COMMAND_TYPE_XZ);
                cmd.setAddress(address);
                cmd.setTarget(target2);
                cmd.setContainerNo(containerNo);
                commandService.add(cmd);
                return;
            }

            containerNo = containerDTO.getNumber();
            lastTask = taskService.getByContainerNo(containerNo);
            taskService.finishTask(lastTask.getId(), true);

            WCSTask task = new WCSTask();
            task.setId(taskId);
            task.setStationId(lastTask.getStationId());
            task.setEisType(lastTask.getEisType());
            task.setWcsType(Constant.COMMAND_TYPE_XZ);
            task.setStatus(WCSTask.STATUS_WAITTING);
            task.setContainerNo(bcrDataDTO.getContainerNo());//A
            task.setAddress(address);
            task.setTarget("");
            task.setGmtCreateTime(new Date());
            updateClientStationInfoService.sendMsgToClient("料箱"+containerNo+"到达LXJZ点位",0,new Date(),stationId);
            int sc = this.checkStationTaskType(stationId, containerNo);
            switch (sc) {
                case 1:
                    //创建进站任务
                    taskService.add(task);
                    break;
                case 2:
                    List<PointLocation> pointLocation = pointLocationService.getPointByStation(stationId, PointLocation.TYPE_IN);
                    String target = pointLocation.get(0).getPointId();
                    task.setTarget(target);
                    taskService.add(task);
                    updateClientStationInfoService.sendMsgToClient("料箱"+containerNo+"无拣货明细,直接回库",1,new Date(),stationId);
                    WCSCommand cmd = new WCSCommand();
                    cmd.setTaskId(taskId);
                    cmd.setType(Constant.COMMAND_TYPE_XZ);
                    cmd.setAddress(address);
                    cmd.setTarget(target);
                    cmd.setContainerNo(containerNo);
                    commandService.add(cmd);
                    break;
                case 3:
                    List<PointLocation> pointLocation2 = pointLocationService.getPointByStation(0, PointLocation.TYPE_EXCEPTION_CONTAINER);
                    String target2 = pointLocation2.get(0).getPointId();
                    task.setTarget(target2);
                    taskService.add(task);
                    updateClientStationInfoService.sendMsgToClient("料箱"+containerNo+"任务类型与站台不符,发往异常",0,new Date(),stationId);
                    WCSCommand cmd1 = new WCSCommand();
                    cmd1.setTaskId(taskId);
                    cmd1.setType(Constant.COMMAND_TYPE_XZ);
                    cmd1.setAddress(address);
                    cmd1.setTarget(target2);
                    cmd1.setContainerNo(containerNo);
                    commandService.add(cmd1);
                    break;
            }
        }
    }

    /**
     * 检验lx状态
     * 返回值
     * 1,代表可入站(分配箱位或者等待箱位)
     * 2,代表无绑定任务(可能是因为截箱情况,直接回库)
     * 3,不符合当前站台任务类型或者没有在途信息(发往异常口)
     *
     * @param stationId
     * @param containerNo
     * @return
     */
    private int checkStationTaskType(int stationId, String containerNo) {
        Station station = stationService.getStation(stationId);
        List<ZtLxDetailsDto> ztLxDeails = ztSoreService.findZtLxDeails(containerNo);
        if (ztLxDeails.size() > 0) {
            int containTaskType = ztLxDeails.get(0).getTaskType();
            int stationTaskType = station.getStationTaskType();
            if (stationTaskType == containTaskType) {
                if (containTaskType == SxStore.TASKTYPE_BZ) {
                    List<ContainerBindingHz> containerBindingHzs = containerBindingHzService.findByMap(MapUtils.put("containerNo", containerNo).getMap());
                    if (containerBindingHzs.size() > 0) {
                        return 1;
                    } else {
                        logger.info(containerNo+"没有绑定汇总");
                        return 2;
                    }
                } else {
                    return 1;
                }
            } else {
                return 3;
            }
        } else {
            return 3;
        }
    }

    /**
     * 料箱入站
     */
    @Override
    public void lxIn(String containerNo, String deriction, StationLxPosition position) throws Exception {
        int stationId = position.getStationId();
        // 更新料箱状态(分配箱位)
        position.setContainerNo(containerNo);
        position.setStatus(StationLxPosition.STATUS_ALLOCATION);
        position.setGmtDistribTime(new Date());
        position.setContainerDirection(deriction);
        this.update(position);
        // 判断当前站台有无绑定料箱
        Station station = stationService.getStation(stationId);
        List<ContainerSubBindingMx> containerSubBindingMxes = containerSubBingdingMxService.findByMap(MapUtils.put("picking", true).getMap());
        if (StringUtils.isBlank(station.getCurrentLxNo())&&containerSubBindingMxes.size()==0) {
            station.setCurrentLxNo(containerNo);
            stationService.update(station);
        }
        // 更新在途状态(进入播种位)
        storeService.updateZtStoreStatusByContainerNo(containerNo, ZtStore.Status_Waite_Operation);
        updateClientStationInfoService.sendMsgToClient("料箱"+containerNo+"开始入站",1,new Date(),stationId);
    }

    /**
     * 料箱出入站
     *
     * @param stationId
     * @param containerNo
     * @param address
     * @param isIn
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lxInOrOut(int stationId, String containerNo, String address, boolean isIn) throws Exception {
        if (stationId == 0) {
            return;
        }
        Object obj = StationLockUtils.getInstance().getLock(stationId);
        synchronized (obj) {
            if (isIn) {
                this.lxInPlace(stationId, containerNo, address);
            } else {
                this.lxLeave(containerNo, stationId);
            }
        }
    }

    /**
     * 料箱到位处理
     *
     * @param stationId
     * @param containerNo
     * @param address
     * @throws Exception
     */
    @Override
    public void lxInPlace(int stationId, String containerNo, String address) throws Exception {
        try{
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (StringUtils.isBlank(containerNo)) {
            throw new ParameterException("料箱号不能为空");
        }
        ContainerDTO containerDTO = ContainerUtils.parse(containerNo);
        containerNo = containerDTO.getNumber();
        //结束任务
        taskService.finishTaskByContainerNo(containerNo, true);
        //查找到达的料箱位
        List<StationLxPosition> stationLxPositions = stationLxMapper.findByMap(MapUtils.put("containerNo", containerNo).put("positionDeviceNo", address).getMap(), StationLxPosition.class);
        if (stationLxPositions.size() == 0) {
            throw new RuntimeException("料箱未绑定(lx:" + containerNo + "," + "address:" + address + ")");
        }
        //更新料箱位状态
        StationLxPosition entity = stationLxPositions.get(0);
        entity.setStatus(StationLxPosition.STATUS_ARRIVE);
        entity.setGmtInplaceTime(new Date());
        stationLxMapper.update(entity);

        // 更新在途状态(进入播种位)
        List<ZtStore> ztStores = ztSoreService.findByMap(MapUtils.put("containerNo", containerNo).getMap());
        if (ztStores.size() == 0) {
            throw new RuntimeException("找不到料箱号（" + containerNo + "）对应的在途库存");
        }
        ZtStore ztStore = ztStores.get(0);
        storeService.updateZtStoreStatusById(ztStore.getId(), ZtStore.Status_In_Operation_Location);

        //作业状态操作
        Station station = stationService.getStation(stationId);
        updateClientStationInfoService.sendMsgToClient("料箱"+containerNo+"到站",1,new Date(),stationId);
        if (ztStore.getTaskType() == SxStore.TASKTYPE_BZ) {
            //播种
            //设置当前作业拣选单
            changeJXD(station.getId());
            //播种自动发放逻辑
            checkBZStatus(station);
        } else if (ztStore.getTaskType() == SxStore.TASKTYPE_PD) {
            // 盘点
            pdExecuteService.PdDw(containerNo, stationId, address);
        } else if (ztStore.getTaskType() == SxStore.TASKTYPE_HX) {
            //合箱 推送消息
            stationHxService.sendHxToClient(stationId);
            //修改状态
            storeService.updateZtStoreStatusById(ztStore.getId(), ZtStore.Status_Operation);
        }
    }

    /**
     * 料箱离开
     *
     * @param lxNo
     * @param stationId
     * @throws Exception
     */
    @Override
    public void lxLeave(String lxNo, int stationId) throws Exception {
        List<StationLxPosition> lxPositions = stationLxMapper.findByMap(MapUtils.put("stationId", stationId).put("containerNo", lxNo).getMap(), StationLxPosition.class);
        if (lxPositions.size() == 0) {
            logger.info("离开料箱不在此站台，料箱号：【" + lxNo + "】站台号：【" + stationId + "】");
        } else if (lxPositions.size() == 1) {
            // 删除绑定明细汇总
            List<ContainerBindingHz> containerBindingHzs = containerBindingHzService.findByMap(MapUtils.put("containerNo", lxNo).getMap());
            containerBindingHzService.deleteBatch(containerBindingHzs);
            StationLxPosition entity = lxPositions.get(0);
            entity.setContainerNo(null);
            entity.setContainerDirection(null);
            // 更新料箱状态(空闲 移除拣选站料箱信息)
            entity.setStatus(0);
            stationLxMapper.update(entity);
            // 更新在途状态(离开站台)
            List<ZtStore> ztStoreList = ztSoreService.findByMap(MapUtils.put("containerNo", lxNo).getMap());
            ZtStore ztStore = ztStoreList.size() > 0 ? ztStoreList.get(0) : null;
            ztStore.setStatus(ZtStore.Status_Leave_Station);
            ztSoreService.update(ztStore);
            //切换当前拣选料箱
            List<StationLxPosition> stationIds = stationLxMapper.findByMap(MapUtils.put("stationId", stationId).getMap(), StationLxPosition.class);
            stationIds = stationIds.stream().filter(x->!StringUtils.isBlank(x.getContainerNo())).collect(Collectors.toList());
            Station station = stationService.getStation(stationId);
            List<ContainerSubBindingMx> containerSubBindingMxes = containerSubBingdingMxService.findByMap(MapUtils.put("picking", true).getMap());
            if (stationIds.size()>0){
                station.setCurrentLxNo(stationIds.get(0).getContainerNo());

            }else if (containerSubBindingMxes.size()==0){
                station.setCurrentLxNo(null);
            }
            stationService.update(station);
            // 切拣选单
            //判断是否为拣选任务
            if (ztStore.getTaskType() == SxStore.TASKTYPE_BZ) {
                changeJXD(stationId);
            }
        } else {
            throw new Exception("离开料箱有多条记录，料箱号：【" + lxNo + "】站台号：【" + stationId + "】");
        }
        //回告客户端信息
        updateClientStationInfoService.sendStationInfoToClient(stationId);
        updateClientStationInfoService.sendMsgToClient("料箱"+lxNo+"已离站",1,new Date(),stationId);
    }


    // 验证是否发送播种信息
    @Override
    public void checkBZStatus(Station station) throws Exception {
        int stationId = station.getId();
        Object obj = StationLockUtils.getInstance().getLock(stationId);
        synchronized (obj) {
            //校验拣选单
            if (station.getCurrentStationPickId() == 0) {
                updateClientStationInfoService.sendStationInfoToClient(stationId);
                return;
            }
            //验证是否有播种任务正在进行
            Criteria ctr = Criteria.forClass(ContainerSubBindingMx.class);
            Restriction r1 = Restrictions.in("orderHzId", "select id from order_hz where picking_order_id =" + station.getCurrentStationPickId());
            Restriction r2 = Restrictions.eq("picking", true);
            ctr.setRestriction(Restrictions.and(r2, r1));
            List<ContainerSubBindingMx> containerSubBindingMxList = containerSubBingdingMxService.findByCriteria(ctr);
            if (containerSubBindingMxList.size() > 0) {
                updateClientStationInfoService.sendStationInfoToClient(stationId);
                return;
            }

            //寻找料箱位上的料箱
            List<StationLxPosition> stationLxPositions = stationLxMapper.findByMap(MapUtils.put("stationId", stationId).put("status", StationLxPosition.STATUS_ARRIVE).getMap(), StationLxPosition.class);
            if (stationLxPositions.size() == 0) {
                updateClientStationInfoService.sendStationInfoToClient(stationId);
                return;
            }
            for (StationLxPosition stationLxPosition : stationLxPositions) {
                //过滤容器号为空的
                if (StringUtils.isBlank(stationLxPosition.getContainerNo())) {
                    continue;
                }
                //根据料箱获取绑定明细
                List<ContainerSubBindingMx> containerSubBindingMxes2 = new ArrayList<>();
                if ("C".equals(stationLxPosition.getContainerDirection())) {
                    containerSubBindingMxes2 = containerSubBingdingMxService.findMxByContainerNoAsc(stationLxPosition.getContainerNo());
                } else if ("A".equals(stationLxPosition.getContainerDirection())) {
                    containerSubBindingMxes2 = containerSubBingdingMxService.findMxByContainerNoDesc(stationLxPosition.getContainerNo());
                }
                boolean result = this.doBZCommand(stationId, stationLxPosition, containerSubBindingMxes2);
                if (result) {
                    //更新料箱在途作业状态
                    ZtStore ztStore = ztSoreService.getByContainerNo(stationLxPosition.getContainerNo());
                    ztStore.setStatus(ZtStore.Status_Operation);
                    ztSoreService.update(ztStore);
                    break;
                }
            }
            updateClientStationInfoService.sendStationInfoToClient(stationId);
        }
    }


    /**
     * 获取当前第一个进入位置的料箱位
     *
     * @param stationId
     * @return
     */
    private StationLxPosition getFirstContainerPosition(int stationId) {
        Criteria ctr = Criteria.forClass(StationLxPosition.class);
        ctr.setOrder(Order.newInstance().asc("gmtInplaceTime"));
        ctr.setRestriction(Restrictions.eq("stationId", stationId));
        List<StationLxPosition> stationLxPositions = stationLxMapper.findByCriteria(ctr);
        stationLxPositions = stationLxPositions.stream().filter(x -> !StringUtils.isBlank(x.getContainerNo()) && x.getStatus() == StationLxPosition.STATUS_ARRIVE).collect(Collectors.toList());
        return stationLxPositions.size() > 0 ? stationLxPositions.get(0) : null;
    }

    /**
     * 根据拣选单绑定订单到站台位
     *
     * @param stationId
     * @param pickOrderId
     */
    private void bindingOrder(int stationId, int pickOrderId) {
        //获取所有的订单箱位
        List<StationOrderPosition> list = stationOrderMapper.findByMap(MapUtils.put("stationId", stationId).getMap(), StationOrderPosition.class);
        //过滤已经绑定订单的位置
        list = list.stream().filter(x -> x.getOrderId() == 0&&!StringUtils.isBlank(x.getContainerNo())).collect(Collectors.toList());
        //获取拣选单未绑定的订单
        List<OrderHz> orders = orderHzService.getUnbindingOrdersByPickOrderId(pickOrderId);
        if (list.size() > 0 && orders.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < orders.size()) {
                    StationOrderPosition sp = list.get(i);
                    sp.setOrderId(orders.get(i).getId());
                    stationOrderMapper.update(sp);
                }
            }
        }
    }

    /**
     * 切换
     *
     * @param station
     */
    private void changePickOrderId(Station station) throws Exception {
        StationLxPosition spos = getFirstContainerPosition(station.getId());
        if (spos == null) {
            clearOperationInfo(station);
            return;
        }

        //根据料箱绑定拣选单
        PickOrder pickOrder = pickOrderService.getByContainer(station.getId(), spos.getContainerNo());
        if (pickOrder != null) {
            station.setCurrentStationPickId(pickOrder.getId());
            stationService.update(station);
            bindingOrder(station.getId(), pickOrder.getId());
        } else {
            //没有拣选任务，料箱离开
            if (station.getCurrentStationPickId() > 0) {
                clearOperationInfo(station);
            }
            List<PointLocation> pointLocations = pointLocationService.getPointByType(PointLocation.TYPE_EXCEPTION_CONTAINER);
            String taskId = TaskUtils.gerenateTaskId();
            WCSTask task = new WCSTask();
            task.setId(taskId);
            task.setStationId(spos.getStationId());
            task.setEisType(SxStore.TASKTYPE_BZ);
            task.setWcsType(Constant.COMMAND_TYPE_XZ);
            task.setStatus(WCSTask.STATUS_WAITTING);
            task.setContainerNo(spos.getContainerNo());
            task.setAddress(spos.getPositionDeviceNo());
            task.setTarget(pointLocations.get(0).getPointId());
            task.setGmtCreateTime(new Date());
            taskService.add(task);
            WCSCommand cmd = new WCSCommand(taskId, Constant.COMMAND_TYPE_XZ, task.getAddress(), task.getTarget(), spos.getContainerNo());
            commandService.add(cmd);
        }
    }

    // 切换拣选单

    /**
     * 切换拣选单（料箱到位和料箱离开时调用）
     * 1、判断当前站台是否有拣选单
     * 2、没拣选单则根据料箱绑定
     * 站台只有一个料箱，判断料箱拣选单和当前站台拣选单是否一致
     *
     * @param stationId
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeJXD(int stationId) throws Exception {
        // 找出当前站台绑定的拣选单
        Station station = stationService.getStation(stationId);
        int pickOrderId = station.getCurrentStationPickId();
        if (pickOrderId == 0) {
            //当前没有拣选单时,设置当前作业拣选单
            changePickOrderId(station);
        } else {

            //1、是否有两个料箱
            //2、否，无料箱，清理状态
            //3、有一个料箱，查看当前料箱的拣选单是否与当前一致，若一致则不处理，若不一致（）
            //4、有两个料箱，查看当前两个料箱的拣选单与当前一致，若一致则不处理，若不一致（）
            //有拣选单
            //判断当前拣选单是否完成

            List<StationLxPosition> stationLxPositions = stationLxMapper.findByMap(MapUtils.put("stationId",stationId).getMap(),StationLxPosition.class);
            List<StationLxPosition> sts = stationLxPositions.stream().filter(x->x.getStatus()==StationLxPosition.STATUS_ARRIVE && !StringUtils.isBlank(x.getContainerNo())).collect(Collectors.toList());
            if(sts.size()==0){
                //判断拣选单是否完成
                List<OrderHz> orderHzs = orderHzService.getByPickOrderId(pickOrderId);
                if (orderHzs.size() == 0) {
                    //完成拣选单
                    PickOrder pickOrder = pickOrderService.findById(pickOrderId);
                    pickOrderHistoryService.toHistory(pickOrder);
                    clearOperationInfo(station);
                }else{
                    return;
                }


            }else if(sts.size()==1){
                PickOrder pickOrder = pickOrderService.getByContainer(station.getId(), sts.get(0).getContainerNo());
                if(pickOrder.getId() == pickOrderId){
                    return;
                }else{
                    List<OrderHz> orderHzs = orderHzService.getByPickOrderId(pickOrderId);
                    if (orderHzs.size() == 0) {
                        //完成拣选单
                        PickOrder pickOrder2 = pickOrderService.findById(pickOrderId);
                        if (pickOrder2 != null) {
                            pickOrderHistoryService.toHistory(pickOrder2);
                        }
                        clearOperationInfo(station);
                    }else{
                        //截单
                        cuttingBoxOp(pickOrderId);
                        //切换新单
                        changePickOrderId(station);
                    }
                }
            }else{
                return;
            }
        }
    }

    /**
     * 清除作业信息
     *
     * @param station
     */
    private void clearOperationInfo(Station station) {
        station.setCurrentLxNo(null);
        station.setCurrentStationPickId(0);
        stationService.update(station);
    }

    /**
     * 截箱操作
     *
     * @param pickOrderId 拣选单id
     */
    @Override
    public void cuttingBoxOp(int pickOrderId) throws Exception {
        /**
         * 1.找到该拣选单下所有未完成的汇总,及明细
         * 2.通过明细找到对应的订单和订单明细
         * 3.订单明细设置为短捡完成,订单完成,并转历史
         * 4.汇总及明细删除
         */
        List<OrderHz> orderHzs = orderHzService.getByPickOrderId(pickOrderId);
        int[] orderIds = orderHzs.stream().mapToInt(x->x.getId()).toArray();

        //删除订单明细
        List<OrderMx> orderMxList = orderMxService.findByOrderHzIds(orderIds);
        if(orderMxList.size()>0){
            for(OrderMx mx:orderMxList){
                mx.setIsNotPick(1);
                mx.setIsComplete(1);
                orderMxHistoryService.toHistory(mx);
            }
        }
        //删除绑定汇总和明细
        containerBindingHzService.deleteByPickOrderId(pickOrderId);
        //删除订单汇总
        orderHzService.delete(orderIds);

        for (int i = 0; i < orderIds.length; i++) {
            orderBoxService.toHistoryByOrderId(orderIds[i]);
        }

        StringBuffer sbf = new StringBuffer();
        for(int i=0;i< orderIds.length;i++){
            sbf.append(orderIds[i]);
            if(i<orderIds.length-1)
                sbf.append(",");
        }
        Criteria ctr = Criteria.forClass(StationOrderPosition.class);
        ctr.setRestriction(Restrictions.in("orderId",String.join(",",sbf.toString())));
        List<StationOrderPosition> list = stationOrderMapper.findByCriteria(ctr);

        List<PointLocation> pointLocations = pointLocationService.getPointByType(PointLocation.TYPE_EXCEPTION_ORDER_BOX);
        PointLocation odpl = pointLocations.get(0);
        if(list.size()>0){
            for(StationOrderPosition op:list){
                String taskId = TaskUtils.gerenateTaskId();
                WCSCommand cmd = new WCSCommand(taskId,Constant.COMMAND_TYPE_XZ,op.getDeviceNo(),odpl.getPointId(),op.getContainerNo());
                if(commandService.getByContainerNo(op.getContainerNo()).size()<1){
                    commandService.add(cmd);
                }
            }
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class,timeout = 600)
    public void seedLogic(int stationId, String lightNo) throws Exception {
        Object obj = StationLockUtils.getInstance().getLock(stationId);
        //       ReentrantLock lock = GolableLockUtils.getInstance().getLock("station"+stationId);
//        try {
        synchronized (obj) {
            System.out.println(new Date());
            try {
                commandService.sendLightCommand(TaskUtils.gerenateTaskId(), stationService.getStation(stationId).getStationNo(), null);
            } catch (Exception e) {
                logger.info("发送亮灯失败," + e.getMessage());
            }
            //lock.tryLock(20000, TimeUnit.MILLISECONDS);
            // 获取当前订单
            List<StationOrderPosition> stationOrderPositions = stationOrderMapper.findByMap(MapUtils.put("stationId", stationId).put("lightNo", lightNo).getMap(), StationOrderPosition.class);
            if (stationOrderPositions.size() == 0) {
                throw new RuntimeException(String.format("找不到订单箱位（站台：%s,灯：%s）", stationId, lightNo));
            }
            //当前订单箱位
            StationOrderPosition orderPosition = stationOrderPositions.get(0);
            if (orderPosition.getGmtLastBzTime() != null) {
                Date d = new Date();
                System.out.println(d.getTime() - orderPosition.getGmtLastBzTime().getTime());
                if (d.getTime() - orderPosition.getGmtLastBzTime().getTime() < properties.getBzInterval()) {

                    throw new RuntimeException("拍灯频率超过限定值");
                }
            }
            orderPosition.setGmtLastBzTime(new Date());
            stationOrderMapper.update(orderPosition);

            int orderId = orderPosition.getOrderId();
            // 获取当前在订单的在播明细
            List<ContainerSubBindingMx> containerSubBindingMxList = containerSubBingdingMxService.findByMap(MapUtils.put("orderHzId", orderId).put("picking", true).getMap());
            if (containerSubBindingMxList.size() != 1) {
                throw new RuntimeException("没有找到该" + orderId + "订单下的在播明细");
            }
            ContainerSubBindingMx containerSubBindingMx = containerSubBindingMxList.get(0);
            String containerNo = containerSubBindingMx.getContainerNo();


            // 获取当前料箱号
//            Station station = stationService.getStation(stationId);
//            String containerNo = station.getCurrentLxNo();
//            if (StringUtils.isBlank(containerNo)) {
//                throw new RuntimeException(String.format("当前料箱位空（%s）", containerNo));
//            }
//
            //获取当前料箱位
            List<StationLxPosition> stationLxs = stationLxMapper.findByMap(MapUtils.put("containerNo", containerNo).put("status",StationLxPosition.STATUS_ARRIVE).getMap(), StationLxPosition.class);
            if (stationLxs.size() == 0) {
                throw new RuntimeException(String.format("找不到料箱位（料箱：%s）", containerNo));
            }
            StationLxPosition lxPosition = stationLxs.get(0);
            String containerDirection = lxPosition.getContainerDirection();

            // 获取容器的所有绑定明细
            List<ContainerSubBindingMx> containerSubBindingMxes = containerSubBingdingMxService.findByMap(MapUtils.put("containerNo", containerNo).getMap());
//
//            //获取当前作业的绑定明细
//            Optional<ContainerSubBindingMx> pickingHGOption = containerSubBindingMxes.stream().filter(x -> x.getOrderHzId() == orderId && x.isPicking()).findFirst();
//            //没有播种任务，
//            if (!pickingHGOption.isPresent()) {
//                throw new RuntimeException("料箱(" + containerNo + ")没有在播种的明细");
//            }
//            ContainerSubBindingMx containerSubBindingMx = pickingHGOption.get();

            //当前拣选的数量
            int pickCount = containerSubBindingMx.getBindingNum() - containerSubBindingMx.getActualNum();

            // 获取当前播种的订单明细
            OrderMx orderMx = orderMxService.findById(containerSubBindingMx.getOrderMxId());


            // 更新订单明细实际数量
            orderMx.setActualNum(orderMx.getActualNum() + pickCount);
            if (orderMx.getActualNum() == orderMx.getPlanNum()) {
                orderMx.setIsComplete(1);
            }
            orderMxService.update(orderMx);


            //更新订单框内数据
            orderBoxService.update(orderPosition.getContainerNo(), orderId, containerSubBindingMx.getOrderMxId(), containerSubBindingMx.getContainerSubNo(), pickCount);

            // 删除当前绑定明细
            containerSubBingdingMxService.delete(containerSubBindingMx);
            //删除绑定汇总
            if (containerSubBindingMxes.size() == 1) {
                containerBindingHzService.delete(containerSubBindingMx.getContainerNo());
            }

            // 更新货格对应商品数
            ContainerSub containerSub = containerSubService.findById(containerSubBindingMx.getContainerSubNo());
            containerSub.setCommodityNum(containerSub.getCommodityNum() - pickCount);
            containerSubService.update(containerSub);

            //判断订单是否完成播种
            checkOrderStatus(orderId, true);

            SeedInfo seedInfo = new SeedInfo(null,containerSubBindingMx.getContainerNo(),containerSubBindingMx.getOrderHzId(),containerSubBindingMx.getOrderMxId(),orderPosition.getContainerNo(),stationId,pickCount,new Date());
            seedInfoService.add(seedInfo);

            //判断料箱状态，发送相应指令
            sendNextSeed(containerDirection, containerSubBindingMx.getContainerNo(), stationId, containerSubBindingMx.getContainerSubNo());

            //为客户端发送指令
            updateClientStationInfoService.sendStationInfoToClient(stationId);
        }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            lock.unlock();
//        }

    }


    /**
     * 找到下一条播种信息
     *
     * @param containerDirection
     * @param containerNo
     * @param stationId
     * @param containerSubNo
     * @throws Exception
     */
    @Override
    public void sendNextSeed(String containerDirection, String containerNo, int stationId, String containerSubNo) throws Exception {
        // 获取货格绑定的其他明细(订单位顺序)
        List<ContainerSubBindingMx> containerSubBindingMxes1 = containerSubBingdingMxService.findMxAsc(containerSubNo);

        boolean turnOffAll = true;
        //获取当前料箱位
        List<StationLxPosition> stationLxPositions1 = stationLxMapper.findByMap(MapUtils.put("stationId", stationId).put("containerNo", containerNo).getMap(), StationLxPosition.class);
        StationLxPosition stationLxPosition = stationLxPositions1.get(0);

        if (containerSubBindingMxes1.size() > 0) {
            // 当前货格还有其他的订单要拣
            turnOffAll = !doBZCommand(stationId, stationLxPosition, containerSubBindingMxes1);
        } else {
            // 当前货格对应的订单都已经捡完,找到料箱所对应的所有货格(货格顺序,订单顺序)
            List<ContainerSubBindingMx> containerSubBindingMxes2 = new ArrayList<>();
            if ("C".equals(containerDirection)) {
                containerSubBindingMxes2 = containerSubBingdingMxService.findMxByContainerNoAsc(containerNo);
            } else if ("A".equals(containerDirection)) {
                containerSubBindingMxes2 = containerSubBingdingMxService.findMxByContainerNoDesc(containerNo);
            }
            if (containerSubBindingMxes2.size() > 0) {
                // 当前料箱还有其他货格要拣
                turnOffAll = !doBZCommand(stationId, stationLxPosition, containerSubBindingMxes2);
            } else {
                //当前料箱已经拣完或者换箱
                // 判断当前站台是否还有待拣料箱
                List<StationLxPosition> stationLxPositions = stationLxMapper.findByMap(MapUtils.put("stationId", stationId).getMap(), StationLxPosition.class);
                //过滤当前的料箱
                stationLxPositions = stationLxPositions.stream().filter(x -> !StringUtils.isBlank(x.getContainerNo()) && !x.getContainerNo().equals(containerNo)).collect(Collectors.toList());
                //有则发送另一个料箱的播种任务
                for (StationLxPosition stationLxPosition2 : stationLxPositions) {
                    List<ContainerSubBindingMx> containerSubBindingMxes3 = null;
                    if ("C".equals(stationLxPosition2.getContainerDirection())) {
                        containerSubBindingMxes3 = containerSubBingdingMxService.findMxByContainerNoAsc(stationLxPosition2.getContainerNo());
                    } else if ("A".equals(stationLxPosition2.getContainerDirection())) {
                        containerSubBindingMxes3 = containerSubBingdingMxService.findMxByContainerNoDesc(stationLxPosition2.getContainerNo());
                    }
                    boolean res = doBZCommand(stationId, stationLxPosition2, containerSubBindingMxes3);
                    if (res) {
                        turnOffAll = !res;
                        //更新料箱在途作业状态
                        ZtStore ztStore = ztSoreService.getByContainerNo(stationLxPosition.getContainerNo());
                        ztStore.setStatus(ZtStore.Status_Operation);
                        ztSoreService.update(ztStore);
                        break;
                    }
                }

                //判断是否还有绑定明细，若还存在则删除,考虑换箱
                List<ContainerSubBindingMx> existsList = containerSubBingdingMxService.findByMap(MapUtils.put("containerNo",stationLxPosition.getContainerNo()).getMap());

                if(existsList.size()==0){
                    //发送料箱离开任务
                    stationLxPosition.setStatus(StationLxPosition.STATUS_LEAVE);
                    stationLxMapper.update(stationLxPosition);

                    List<PointLocation> pointLocations = pointLocationService.getPointByType(PointLocation.TYPE_EXCEPTION_CONTAINER);
                    String taskId = TaskUtils.gerenateTaskId();
                    WCSTask task = new WCSTask();
                    task.setId(taskId);
                    task.setStationId(stationLxPosition.getStationId());
                    task.setEisType(SxStore.TASKTYPE_BZ);
                    task.setWcsType(Constant.COMMAND_TYPE_XZ);
                    task.setStatus(WCSTask.STATUS_WAITTING);
                    task.setContainerNo(containerNo);
                    task.setAddress(stationLxPosition.getPositionDeviceNo());
                    task.setTarget(pointLocations.get(0).getPointId());
                    task.setGmtCreateTime(new Date());
                    taskService.add(task);
                    WCSCommand cmd = new WCSCommand(taskId, Constant.COMMAND_TYPE_XZ, task.getAddress(), task.getTarget(), stationLxPosition.getContainerNo());
                    commandService.add(cmd);
                }
            }
        }

        if (turnOffAll) {
            try {
                commandService.sendLightCommand(TaskUtils.gerenateTaskId(), stationService.getStation(stationId).getStationNo(), null);
            } catch (Exception e) {
                logger.info("发送亮灯失败," + e.getMessage());
            }
        }
    }

    /**
     * 根据明细锁定料箱和订单框
     *
     * @param stationId
     * @param stationLxPosition
     * @param containerSubBindingMxes
     * @return
     */
    public boolean doBZCommand(int stationId, StationLxPosition stationLxPosition, List<ContainerSubBindingMx> containerSubBindingMxes) {
        Object obj = StationLockUtils.getInstance().getLock(stationId);
        synchronized (obj){
            if (containerSubBindingMxes == null || containerSubBindingMxes.size() == 0) {
                return false;
            }

            for (int i = 0; i < containerSubBindingMxes.size(); i++) {
                ContainerSubBindingMx mx = containerSubBindingMxes.get(i);
                List<StationOrderPosition> orderPositions = stationOrderMapper.findByMap(MapUtils.put("orderId", mx.getOrderHzId()).put("stationId", stationId).getMap(), StationOrderPosition.class);
                orderPositions = orderPositions.stream().filter(s -> !StringUtils.isBlank(s.getContainerNo()) && !s.isChanged()).collect(Collectors.toList());
                StationOrderPosition nextOrderPosition = null;

                if (orderPositions.size() == 0) {
                    //寻找空的订单框，并进行绑定
                    List<StationOrderPosition> emptyOrderPositions = stationOrderMapper.findByMap(MapUtils.put("orderId", 0).put("stationId", stationId).getMap(), StationOrderPosition.class);
                    emptyOrderPositions = emptyOrderPositions.stream().filter(s -> !StringUtils.isBlank(s.getContainerNo())).collect(Collectors.toList());
                    if (emptyOrderPositions.size() == 0)
                        continue;

                    //绑定订单
                    nextOrderPosition = emptyOrderPositions.get(0);
                    nextOrderPosition.setOrderId(mx.getOrderHzId());
                    stationOrderMapper.update(nextOrderPosition);
                } else {
                    nextOrderPosition = orderPositions.get(0);
                }
                //设置绑定明细状态
                containerSubBingdingMxService.setPicking(mx.getId(),stationId);
                //mx.setPicking(true);
                //containerSubBingdingMxService.update(mx);

                //切换作业料箱
                Station station = stationService.getStation(stationId);
                station.setCurrentLxNo(mx.getContainerNo());
                stationService.update(station);
                updateClientStationInfoService.sendMsgToClient("料箱"+mx.getContainerNo()+"开始播种",0,new Date(),stationId);
                //亮灯
                Container ct = containerService.findByMap(MapUtils.put("containerNo", mx.getContainerNo()).getMap()).get(0);
                updateClientStationInfoService.lightOn(stationId, ct.getContainerNo(), mx.getContainerSubNo(), ct.getLayoutType(), stationLxPosition.getContainerDirection(), stationLxPosition.getPositionNo(), nextOrderPosition.getLightNo());
                return true;
            }
            return false;
        }
    }


    public void checkOrderStatus(int orderId, boolean leave) throws Exception {
        List<OrderMx> mxList = orderMxService.findByMap(MapUtils.put("orderHzId", orderId).getMap());
        Optional<OrderMx> mxOptional = mxList.stream().filter(x -> x.getPlanNum() != x.getActualNum()).findFirst();
        if (mxOptional.isPresent())
            return;

        //订单完成转历史
        OrderHz orderHz = orderHzService.findById(orderId);
        orderHzHistoryService.toHistory(orderHz);
        //订单框转历史
        orderBoxService.toHistoryByOrderId(orderId);
        //更新订单箱状态(离开中)
        List<StationOrderPosition> stationOrderPositions = stationOrderMapper.findByMap(MapUtils.put("orderId", orderId).getMap(), StationOrderPosition.class);
        stationOrderPositions.get(0).setStatus(StationOrderPosition.STATUS_LEAVING);
        stationOrderPositions.get(0).setGmtDistributeTime(new Date());
        stationOrderMapper.update(stationOrderPositions.get(0));
        String taskId = TaskUtils.gerenateTaskId();
        List<PointLocation> pointLocations = pointLocationService.findByMap(MapUtils.put("pointType", PointLocation.TYPE_EXCEPTION_ORDER_BOX).getMap());
        List<StationOrderPosition> orderPositions = stationOrderMapper.findByMap(MapUtils.put("orderId", orderId).getMap(), StationOrderPosition.class);
//        WCSTask task = new WCSTask();
//        task.setId(taskId);
//        task.setStationId(orderPositions.get(0).getStationId());
//        task.setEisType(2);
//        task.setWcsType(Constant.COMMAND_TYPE_XZ);
//        task.setStatus(WCSTask.STATUS_WAITTING);
//        task.setContainerNo(orderPositions.get(0).getContainerNo());
//        task.setAddress(orderPositions.get(0).getDeviceNo());
//        task.setTarget(pointLocations.get(0).getPointId());
//        task.setGmtCreateTime(new Date());
//        taskService.add(task);

        if (!leave)
            return;
        // 订单箱任务
        WCSCommand cmd = new WCSCommand(taskId, Constant.COMMAND_TYPE_XZ, orderPositions.get(0).getDeviceNo(), pointLocations.get(0).getPointId(), orderPositions.get(0).getContainerNo());
        commandService.add(cmd);

    }


    @Override
    public void saveStationLx(StationLxPosition stationLxPosition) {
        stationLxMapper.save(stationLxPosition);
    }

    @Override
    public long countData() {
        return stationLxMapper.findCountByMap(null, StationLxPosition.class);
    }

    @Override
    public void deleteAll() {
        stationLxMapper.deleteByMap(null, StationLxPosition.class);
    }


    @Override
    public void addBath(List<StationLxPosition> list) {
        stationLxMapper.saveBatch(list);
    }

    @Override
    public List<StationLxPosition> findByMap(Map map) {
        return stationLxMapper.findByMap(map, StationLxPosition.class);
    }

    /**
     * 寻找空料箱位
     *
     * @return
     */
    @Override
    public List<StationLxPosition> getEmptyPositions() {
        return stationLxMapper.findEmptyPosition();
    }

    /**
     * 更新position
     *
     * @param position
     */
    @Override
    public void update(StationLxPosition position) {
        stationLxMapper.update(position);
    }

    /**
     * 根据satationId获取料箱位信息
     *
     * @param stationId
     * @return
     */
    @Override
    public List<StationLxPosition> getByStationId(int stationId) {
        return stationLxMapper.findByMap(MapUtils.put("stationId", stationId).getMap(), StationLxPosition.class);
    }

    @Override
    public void updateStationLxByContainer(String containerNo) throws Exception {
        Criteria criteria = Criteria.forClass(StationLxPosition.class);
        criteria.setRestriction(Restrictions.eq("containerNo", containerNo));
        stationLxMapper.updateMapByCriteria(MapUtils.put("status", StationLxPosition.STATUS_ARRIVE).getMap(), criteria);
    }
}
