package com.prolog.eis.pickstation.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.prolog.eis.dto.base.ContainerDTO;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.order.*;
import com.prolog.eis.model.point.PointLocation;
import com.prolog.eis.pickstation.client.UpdateClientStationInfoService;
import com.prolog.eis.pickstation.dao.StationLxMapper;
import com.prolog.eis.pickstation.dao.StationOrderMapper;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.pickstation.model.StationOrderPosition;
import com.prolog.eis.pickstation.service.IStationDdxService;
import com.prolog.eis.pickstation.service.IStationLxService;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.eis.service.order.*;
import com.prolog.eis.service.pointlocation.IPointLocationService;
import com.prolog.eis.util.ContainerUtils;
import com.prolog.eis.util.GolableLockUtils;
import com.prolog.eis.util.TaskUtils;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.dto.BCRDataDTO;
import com.prolog.eis.wcs.dto.BoxCallbackDTO;
import com.prolog.eis.wcs.model.WCSCommand;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restriction;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.framework.utils.MapUtils;

@Service
public class StationDdxServiceImpl implements IStationDdxService {

    @Autowired
    private StationOrderMapper stationOrpositionMapper;
    @Autowired
    private UpdateClientStationInfoService updateClientStationInfoService;
    @Autowired
    private IStationLxService iStationLxService;
    @Autowired
    private IOrderHzService orderHzService;
	@Autowired
	private IPointLocationService pointLocationService;
	@Autowired
    private IWCSCommandService commandService;
	@Autowired
    private IStationService stationService;
	@Autowired
    private StationLxMapper stationLxMapper;
	@Autowired
    private IContainerSubBingdingMxService containerSubBingdingMxService;
	@Autowired
    private IOrderMxService orderMxService;
	@Autowired
    private ContainerSubService containerSubService;
	@Autowired
    private IWCSTaskService taskService;
	@Autowired
    private IOrderBoxService orderBoxService;
	@Autowired
    private IOrderBoxService orderBoxHzService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkDdxStatus(BCRDataDTO bcrDataDTO) throws Exception {
    	String ddxNo = bcrDataDTO.getContainerNo();
        if("NoRead".equals(ddxNo)){
            return false;
        }
        ContainerDTO containerDTO = ContainerUtils.parse(ddxNo);
        ddxNo = containerDTO.getNumber();
        List<OrderHz> orderHzs = orderHzService.findByMap(MapUtils.put("orderBoxNo", ddxNo).getMap());
        if (orderHzs.size() > 0) {
			throw new RuntimeException("该订单箱已存在");
		}
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ddxLeave(String ddxNo, int stationId) throws Exception {
        List<StationOrderPosition> ddxPositions = stationOrpositionMapper.findByMap(MapUtils.put("stationId", stationId).put("containerNo", ddxNo).getMap(), StationOrderPosition.class);
        if (ddxPositions.size() == 0) {
            throw new Exception("离开订单箱不在此站台，订单箱号：【" + ddxNo + "】站台号：【" + stationId + "】");
        }else if (ddxPositions.size() == 1) {
            StationOrderPosition entity = ddxPositions.get(0);
            if (!entity.isChanged()){
                entity.setOrderId(0);
            }
            entity.setChanged(false);
            entity.setContainerNo(null);
            entity.setStatus(StationOrderPosition.STATUS_IDLE);
            entity.setGmtDistributeTime(null);
            // 更新订单箱状态(空闲)
            stationOrpositionMapper.update(entity);
            updateClientStationInfoService.sendMsgToClient("订单箱"+ddxNo+"已离开",1,new Date(),stationId);

        } else {
            throw new Exception("离开订单箱有多条记录，订单箱号：【" + ddxNo + "】站台号：【" + stationId + "】");
        }

        //回告客户端信息
        updateClientStationInfoService.sendStationInfoToClient(stationId);
    }


    @Override
    public List<StationOrderPosition> updateStationOrposition() throws Exception {
        List<StationOrderPosition> stationOrderPositions = stationOrpositionMapper.findByMap(MapUtils.put("status", 0).getMap(), StationOrderPosition.class);
        for (StationOrderPosition stationOrderPosition : stationOrderPositions) {
            //更新订单箱状态(分配箱位)
            stationOrderPosition.setStatus(StationOrderPosition.STATUS_DISTRIBUTE);
            stationOrderPosition.setGmtDistributeTime(new Date());
            stationOrpositionMapper.update(stationOrderPosition);
        }
        return stationOrderPositions;
    }


    @Override
    @Transactional
    public void ddxInplace(BoxCallbackDTO boxCallbackDTO) throws Exception {
		String ddxNo = boxCallbackDTO.getContainerNo();
        //结束任务
        List<WCSTask> lastTasks = taskService.getByTarget(boxCallbackDTO.getAddress());
        if(lastTasks.size()>0){
            taskService.finishTask(lastTasks.get(0).getId(),true);
        }
        //修改订单箱位状态
        List<StationOrderPosition> stationOrderPositions = stationOrpositionMapper.findByMap(MapUtils.put("deviceNo", boxCallbackDTO.getAddress()).getMap(), StationOrderPosition.class);
        if (stationOrderPositions.size()==1) {
            StationOrderPosition stationOrderPosition = stationOrderPositions.get(0);
            stationOrderPosition.setStatus(StationOrderPosition.STATUS_INPLACE);
            stationOrderPosition.setContainerNo(ddxNo);
            stationOrderPosition.setGmtDistributeTime(new Date());
            stationOrpositionMapper.update(stationOrderPosition);
            //检查播种状态
            Station station = stationService.getStation(stationOrderPosition.getStationId());
            iStationLxService.checkBZStatus(station);
            updateClientStationInfoService.sendMsgToClient("订单箱"+ddxNo+"已到位",1,new Date(),stationOrderPosition.getStationId());
        } else {
            throw new Exception("查询到多个位置");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeOrderBox(String orderBoxNo, int positionNo,int num) throws Exception {


        List<StationOrderPosition> stationOrderPositions = stationOrpositionMapper.findByMap(MapUtils.put("containerNo",orderBoxNo).getMap(), StationOrderPosition.class);
        if (stationOrderPositions.size()==0){
            throw new Exception("订单箱位找不到当前订单箱");
        }
        if (Integer.valueOf(stationOrderPositions.get(0).getPositionNo()) != positionNo){
            throw new Exception("找到的订单箱与换箱的位置不符合");
        }

        ReentrantLock lock = GolableLockUtils.getInstance().getLock("station"+stationOrderPositions.get(0).getStationId());
        try {
            updateClientStationInfoService.sendMsgToClient("订单箱"+orderBoxNo+"执行换箱操作",1,new Date(),stationOrderPositions.get(0).getStationId());

            lock.tryLock(20000,TimeUnit.MILLISECONDS);
            // 更新订单箱状态(截箱)
            stationOrderPositions.get(0).setChanged(true);
            stationOrderPositions.get(0).setStatus(StationOrderPosition.STATUS_LEAVING);
            stationOrpositionMapper.update(stationOrderPositions.get(0));
            // 发送订单箱离开任务
            String taskId = TaskUtils.gerenateTaskId();
            List<PointLocation> pointLocations = pointLocationService.findByMap(MapUtils.put("pointType", PointLocation.TYPE_EXCEPTION_ORDER_BOX).getMap());
            WCSCommand cmd = new WCSCommand(taskId,Constant.COMMAND_TYPE_XZ,stationOrderPositions.get(0).getDeviceNo(),pointLocations.get(0).getPointId(),stationOrderPositions.get(0).getContainerNo());
            commandService.add(cmd);
            // 更新当前任务执行的数量
            int orderId = stationOrderPositions.get(0).getOrderId();
            int stationId = stationOrderPositions.get(0).getStationId();
            Station station = stationService.getStation(stationId);
            String containerNo = station.getCurrentLxNo();
            List<ContainerSubBindingMx> containerSubBindingMxes = containerSubBingdingMxService.findByMap(MapUtils.put("containerNo",containerNo).put("orderHzId",orderId).put("picking",true).getMap());
            if(containerSubBindingMxes.size()!=1){
                return;
            }
            ContainerSubBindingMx containerSubBindingMx = containerSubBindingMxes.get(0);
            OrderMx orderMx = orderMxService.findById(containerSubBindingMx.getOrderMxId());
            if((orderMx.getActualNum()+num)>orderMx.getPlanNum()){
                throw new RuntimeException("换箱数据错误");
            }
            orderMx.setActualNum(orderMx.getActualNum()+num);
            orderMxService.update(orderMx);

            containerSubBindingMx.setActualNum(containerSubBindingMx.getActualNum()+num);
            containerSubBindingMx.setPicking(false);
            if(containerSubBindingMx.getActualNum() == containerSubBindingMx.getBindingNum()){
                containerSubBingdingMxService.delete(containerSubBindingMx);
            }else{
                containerSubBingdingMxService.update(containerSubBindingMx);
            }

            // 更新货格对应商品数
            ContainerSub containerSub = containerSubService.findById(containerSubBindingMx.getContainerSubNo());
            containerSub.setCommodityNum(containerSub.getCommodityNum()-containerSubBindingMx.getActualNum());
            List<ContainerSub> list = new ArrayList<>();
            list.add(containerSub);
            containerSubService.updateContainerSubInfo(list);
            //判断订单完成情况
            iStationLxService.checkOrderStatus(orderId,false);
            // 截箱操作生成订单箱数据
            orderBoxService.update(orderBoxNo,orderMx.getOrderHzId(),orderMx.getId(),containerSub.getContainerSubNo(),num);
            // 继续发送下一条任务
            List<StationLxPosition> stationLxs = stationLxMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), StationLxPosition.class);
            String containerDirection = stationLxs.get(0).getContainerDirection();
            iStationLxService.sendNextSeed(containerDirection,containerNo,stationId,containerSubBindingMx.getContainerSubNo());
            stationOrderPositions.get(0).setOrderId(0);
            stationOrpositionMapper.update(stationOrderPositions.get(0));
        } catch (Exception e) {
            throw e;
        } finally {
            lock.unlock();
        }



    }

    /**
     * 根据站台获取
     *
     * @param stationIds
     * @return
     */
    @Override
    public List<StationOrderPosition> getByStationIds(List<Integer> stationIds) {
        Criteria ctr = Criteria.forClass(StationOrderPosition.class);
        ctr.setRestriction(Restrictions.in("stationId",stationIds.toArray()));
        return stationOrpositionMapper.findByCriteria(ctr);
    }

    /**
     * 获取空订单箱位
     *
     * @return
     */
    @Override
    public List<StationOrderPosition> getEmpty() {
        Criteria ctr = Criteria.forClass(StationOrderPosition.class);
        Restriction r1 = Restrictions.in("stationId","select id from station where is_lock=2 and station_task_type=20 and is_claim = 1");
        Restriction r2 = Restrictions.notIn("deviceNo","select target from wcs_task");
        Restriction r3 = Restrictions.isnull("containerNo");
        Restriction r4 = Restrictions.eq("status",StationOrderPosition.STATUS_IDLE);
        ctr.setRestriction(Restrictions.and(r1,r2,r3,r4));
        return stationOrpositionMapper.findByCriteria(ctr);
    }

    @Override
    public void saveStationOrder(StationOrderPosition stationOrderPosition) {
        stationOrpositionMapper.save(stationOrderPosition);
    }

    @Override
    public long countData() {
        return stationOrpositionMapper.findCountByMap(null, StationOrderPosition.class);
    }

    @Override
    public void deleteAll() {
        stationOrpositionMapper.deleteByMap(null, StationOrderPosition.class);
    }


    @Override
    public void addBath(List<StationOrderPosition> list) throws Exception {
        stationOrpositionMapper.saveBatch(list);
    }

    @Override
    public List<StationOrderPosition> getAll() {
        return stationOrpositionMapper.findByMap(null, StationOrderPosition.class);
    }

    @Override
    public List<StationOrderPosition> findByMap(Map map) {
        return stationOrpositionMapper.findByMap(map, StationOrderPosition.class);
    }

    @Override
    public void update(StationOrderPosition stationOrderPosition) {
        stationOrpositionMapper.update(stationOrderPosition);
    }
}
