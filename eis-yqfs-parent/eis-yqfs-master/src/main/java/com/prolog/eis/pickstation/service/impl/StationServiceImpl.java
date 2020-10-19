package com.prolog.eis.pickstation.service.impl;

import com.prolog.eis.dao.masterbase.ContainerMapper;
import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dao.masterbase.GoodsMapper;
import com.prolog.eis.dao.order.*;
import com.prolog.eis.dao.pickorder.PickOrderMapper;
import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.dao.store.ZtStoreMapper;
import com.prolog.eis.dto.pickorder.SeedTaskDto;
import com.prolog.eis.dto.station.StationsDto;
import com.prolog.eis.dto.yqfs.PickBillDto;
import com.prolog.eis.dto.yqfs.PickContainerDto;
import com.prolog.eis.dto.yqfs.PickOrderDetail;
import com.prolog.eis.dto.yqfs.StationDto;
import com.prolog.eis.model.masterbase.Container;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.order.*;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.ZtStore;
import com.prolog.eis.pickstation.client.SendCsClientService;
import com.prolog.eis.pickstation.client.UpdateClientStationInfoService;
import com.prolog.eis.pickstation.dao.StationLxMapper;
import com.prolog.eis.pickstation.dao.StationMapper;
import com.prolog.eis.pickstation.dao.StationOrderMapper;
import com.prolog.eis.pickstation.dto.LxDetailsDto;
import com.prolog.eis.pickstation.dto.LxSubDetailsDto;
import com.prolog.eis.pickstation.dto.SendLxDetailsDto;
import com.prolog.eis.pickstation.dto.SendLxDto;
import com.prolog.eis.pickstation.eis.EisStationInterfaceService;
import com.prolog.eis.pickstation.model.PickOrder;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.pickstation.model.StationOrderPosition;
import com.prolog.eis.pickstation.service.IStationDdxService;
import com.prolog.eis.pickstation.service.IStationLxService;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.util.LightUtils;
import com.prolog.eis.util.TaskUtils;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restriction;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class StationServiceImpl implements IStationService {
    @Autowired
    IStationLxService iStationLxService;

    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private StationLxMapper lxPositionMapper;
    @Autowired
    private StationOrderMapper orderPositionMapper;
    @Autowired
    private SendCsClientService sendCsClientService;
    @Autowired
    private OrderMxMapper orderMxMapper;
    @Autowired
    private OrderHzMapper orderHzMapper;
    @Autowired
    private EisStationInterfaceService eisStationInterfaceService;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private ZtStoreMapper ztStoreMapper;

    @Autowired
    private PickOrderMapper pickOrderMapper;

    @Autowired
    private ContainerBindingHzMapper containerBindingHzMapper;

    @Autowired
    private ContainerSubBindingMxMapper containerSubBindingMxMapper;

    @Autowired
    private ContainerSubMapper containerSubMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private IStationLxService stationLxService;

    @Autowired
    private IStationDdxService stationDdxService;

    @Autowired
    private ContainerMapper containerMapper;

    @Autowired
    private IWCSCommandService commandService;

    @Autowired
    private OrderMxBingdingMapper orderMxBingdingMapper;
    @Autowired
    private IWCSTaskService taskService;


    @Override
    public List<StationDto> queryStation() {
        List<StationDto> list =  stationMapper.getAllStation();
        return list;
    }

    @Override
    public Station getByNo(String stationNo) {
        List<Station> list =  stationMapper.findByMap(MapUtils.put("stationNo",stationNo).getMap(),Station.class);
        if(list.size()>0)
            return list.get(0);
        return null;
    }

    @Override
    public void insert(StationDto stationDto) throws Exception {
        Station station = new Station();
        station.setLxMaxCount(stationDto.getLxMaxCount());
        station.setIsClaim(stationDto.getIsClaim());
        station.setIsLock(stationDto.getIsLock());
        station.setStationTaskType(stationDto.getStationTaskType());
        station.setIpAddress(stationDto.getIpAddress());
        station.setCreateTime(new Date());

//		String hoisterNo = stationDto.getHoisterNo();
//		if(!StringUtils.isEmpty(hoisterNo)) {
//			List<SxHoister> sxHoisters = sxHoisterMapper.findByMap(MapUtils.put("hoisterNo", hoisterNo).getMap(), SxHoister.class);
//			if(sxHoisters == null || sxHoisters.size() < 1) {
//				throw new Exception("找不到编号为【"+hoisterNo+"】的提升机");
//			}
//			station.setHoisterId(sxHoisters.get(0).getId());
//		}

        stationMapper.save(station);
    }

    @Override
    public void update(StationDto stationDto) throws Exception {
        Station station = stationMapper.findById(stationDto.getId(), Station.class);
        if (stationDto.getIsClaim() == station.getIsClaim() && station.getIsClaim() == 1) {
            if (stationDto.getStationTaskType() != station.getStationTaskType()) {
                throw new Exception("需要先将站台调整为不索取任务");
            }
        } else {
            if (station.getIsClaim() == 1 && stationDto.getStationTaskType() == station.getStationTaskType() && stationDto.getIsClaim() == 2) {
                stationMapper.updateMapById(stationDto.getId(),
                        MapUtils.put("isClaim", stationDto.getIsClaim()).getMap(),
                        Station.class);
            } else if (station.getIsClaim() == 2) {
                stationMapper.updateMapById(stationDto.getId(),
                        MapUtils.put("lxMaxCount", stationDto.getLxMaxCount())
                                .put("isClaim", stationDto.getIsClaim())
                                .put("isLock", stationDto.getIsLock())
                                .put("stationTaskType", stationDto.getStationTaskType())
                                .put("updateTime", new Date())
                                .put("ipAddress", stationDto.getIpAddress()).getMap(),
                        Station.class);
            } else {
                stationMapper.updateMapById(stationDto.getId(),
                        MapUtils.put("lxMaxCount", stationDto.getLxMaxCount())
                                .put("isClaim", stationDto.getIsClaim())
                                .put("isLock", stationDto.getIsLock())
                                .put("stationTaskType", stationDto.getStationTaskType())
                                .put("updateTime", new Date())
                                .put("ipAddress", stationDto.getIpAddress()).getMap(),
                        Station.class);
            }
        }

        //1是索取 2是不索取

        //当前的任务类型
        int stationTaskType = station.getStationTaskType();
        //当前任务
        int storeCount = storeMapper.findByMap(
                MapUtils.put("taskType", stationTaskType)
                        .put("stationId", stationDto.getId())
                        .getMap(), SxStore.class).size();
        int zTCount = ztStoreMapper.findByMap(
                MapUtils.put("taskType", stationTaskType)
                        .put("stationId", stationDto.getId())
                        .getMap(), ZtStore.class).size();

        boolean flag = storeCount + zTCount > 0;
        if (flag) {
            throw new Exception("当前站台还有任务未完成");
        }

        //库存和在途的任务都已经执行完成，即可正常跟新所有数据

        stationMapper.updateMapById(stationDto.getId(),
                MapUtils.put("lxMaxCount", stationDto.getLxMaxCount())
                        .put("isClaim", stationDto.getIsClaim())
                        .put("isLock", stationDto.getIsLock())
                        .put("stationTaskType", stationDto.getStationTaskType())
                        .put("updateTime", new Date())
                        .put("ipAddress", stationDto.getIpAddress()).getMap(),
                Station.class);


    }

    @Override
    public void deleteStation(int stationId) throws Exception {
        long l = stationMapper.deleteById(stationId, Station.class);
        if(l != 1) {
            throw new Exception("删除失败，对应id的站台不存在");
        }
    }

    /**
     * 站台缺货查询
     */
    @Override
    public ConcurrentHashMap<String, List> lack(int pickId) throws Exception {
        long startTime = System.currentTimeMillis();
        ConcurrentHashMap<String , List> map = new ConcurrentHashMap(3);
        //查询拣选单订单集合
        map.put("PickOrderMxDto", orderMxMapper.findOrderMxByPick(pickId));
        //拣选单绑定集合
        map.put("pickLxBindDtos", orderMxMapper.findByPickId(pickId));
        //拣选单商品箱库库存集合
        map.put("pickSpCountDtos", orderMxMapper.findPickSpCountByPickId(pickId));
        return map;
    }

    @Override
    public StationDto getstationwithportbyips(String ipaddress) throws Exception {
        StationDto station = stationMapper.getstationwithportbyips(ipaddress);
        return station;
    }

    @Override
    public void add(Station station) throws Exception {
        stationMapper.save(station);
    }

    @Override
    public void deleteAll() {
        stationMapper.deleteByMap(null,Station.class);
    }

    @Override
    public Station getStation(int stationId) {
        return stationMapper.findById(stationId,Station.class);
    }

    @Override
    public void addBath(List<Station> stations) throws Exception {
        stationMapper.saveBatch(stations);
    }

    @Override
    public long countData() throws Exception {
        return stationMapper.findCountByMap(null,Station.class);
    }

    @Override
    public List<Station> findByMap(Map map) throws Exception {
        return stationMapper.findByMap(map,Station.class);
    }

    @Override
    public void update(Station station){
        stationMapper.update(station);
    }

    /**
     * 获取拣选站台ip
     *
     * @param stationId
     * @return
     */
    @Override
    public String getIp(int stationId) {
        Station station = this.getStation(stationId);
        return station==null?null:station.getIpAddress();
    }

    @Override
    public Map init(int stationId) throws Exception {
        Map resultMap = new HashMap();
        List<SendLxDto> listSendLxDto = new ArrayList<>();
        List<StationLxPosition> stationLxPositions = lxPositionMapper.findByMap(MapUtils.put("stationId",stationId).getMap(),StationLxPosition.class);
        for (StationLxPosition stationLxPosition : stationLxPositions) {
            if (!StringUtils.isBlank(stationLxPosition.getContainerNo())) {
                String containerNo = stationLxPosition.getContainerNo();
                LxDetailsDto lxDetailsDto = eisStationInterfaceService.queryLxDetails(containerNo);
                Station station = this.getStation(stationId);
                int taskType = lxDetailsDto.getTaskType();
                int goodsId = 0;
                SendLxDto sendLxDto = new SendLxDto();
                sendLxDto.setContainerNo(containerNo);
                sendLxDto.setLayoutType(lxDetailsDto.getLxType());
                sendLxDto.setLocaltion(Integer.parseInt(stationLxPosition.getPositionNo()));
                sendLxDto.setOrientation(stationLxPosition.getContainerDirection());
                sendLxDto.setStationId(Integer.parseInt(station.getStationNo()));
                sendLxDto.setTaskType(taskType);
                List<SendLxDetailsDto> list = new ArrayList<SendLxDetailsDto>();
                for (LxSubDetailsDto lxSubDetailsDto : lxDetailsDto.getLxSubDeatilsDtos()) {
                    SendLxDetailsDto sendLxDetailsDto = new SendLxDetailsDto();
                    sendLxDetailsDto.setContainerNo(containerNo);
                    sendLxDetailsDto.setContainerSubNo(lxSubDetailsDto.getLxSubNo());
                    sendLxDetailsDto.setGoodsBarCode(lxSubDetailsDto.getGoodsBarCodes());
                    sendLxDetailsDto.setGoodsName(lxSubDetailsDto.getGoodsName());
                    sendLxDetailsDto.setGoodsNum(lxSubDetailsDto.getGoodsNum());
                    sendLxDetailsDto.setId(Integer.valueOf(lxSubDetailsDto.getLxSubNo().substring(lxSubDetailsDto.getLxSubNo().length() - 1, lxSubDetailsDto.getLxSubNo().length())));
                    list.add(sendLxDetailsDto);
                }
                sendLxDto.setPickContainerSubInfos(list);
                listSendLxDto.add(sendLxDto);
            }
        }
        resultMap.put("pickContainerInfos",listSendLxDto);
        List<Map> listOrder = new ArrayList<>();
        List<StationOrderPosition> stationOrderPositions = orderPositionMapper.findByMap(MapUtils.put("stationId",stationId).getMap(),StationOrderPosition.class);
        for (StationOrderPosition stationOrderPosition : stationOrderPositions) {
            if (!StringUtils.isBlank(stationOrderPosition.getContainerNo())){
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", stationOrderPosition.getPositionNo());
                map.put("orderBoxNo", stationOrderPosition.getContainerNo());
                listOrder.add(map);
            }
        }
        resultMap.put("pickOrderBoxs",listOrder);
        return resultMap;
    }

    /**
     * 查找可拣选的站台
     *
     * @return
     */
    @Override
    public List<Station> getPickable() {
        Criteria ctr = Criteria.forClass(Station.class);
        Restriction r1 = Restrictions.eq("isLock",2);
        Restriction r2 = Restrictions.eq("stationTaskType",SxStore.TASKTYPE_BZ);
        ctr.setRestriction(Restrictions.and(r1,r2));
        return stationMapper.findByCriteria(ctr);
    }

    @Override
    public List<PickOrderDetail> getPickOrderDetail(String stationId) throws Exception {
        List<PickOrderDetail> pickOrderDetails = new ArrayList<>();
        List<Station> stations = stationMapper.findByMap(MapUtils.put("stationNo", stationId).getMap(), Station.class);
        if (stations.size() == 0) {
            throw new Exception("站台号" + stationId + "不存在");
        }
        Station station = stations.get(0);
        List<PickOrder> pickOrders = pickOrderMapper.findByMap(MapUtils.put("stationId", station.getId()).getMap(), PickOrder.class);
        if (pickOrders.size() == 0) {
            throw new Exception("当前站台无拣选单");
        }
        PickOrder pickOrder = pickOrders.get(0);
        int pickingOrderId = pickOrder.getId();

        List<Integer> orderHzIds = orderHzMapper.findByMap(MapUtils.put("pickingOrderId", pickingOrderId).getMap(), OrderHz.class).stream().map(OrderHz::getId).collect(Collectors.toList());
        for (Integer orderHzId : orderHzIds) {
            List<OrderMx> orderMxList = orderMxMapper.findByMap(MapUtils.put("orderHzId", orderHzId).getMap(), OrderMx.class);
            for (OrderMx orderMx : orderMxList) {
                List<ContainerSubBindingMx> containerSubBindingMxes = containerSubBindingMxMapper.findByMap(MapUtils.put("orderMxId", orderMx.getId()).getMap(), ContainerSubBindingMx.class);
                if (containerSubBindingMxes.size() > 0) {
                    ContainerSubBindingMx containerSubBindingMx = containerSubBindingMxes.get(0);
                    Goods goods = goodsMapper.findById(orderMx.getGoodsId(), Goods.class);
                    PickOrderDetail pickOrderDetail = new PickOrderDetail();
                    pickOrderDetail.setGoodsName(goods.getGoodsName());
                    pickOrderDetail.setContainerNo(containerSubBindingMx.getContainerNo());
                    pickOrderDetail.setContainerSubNo(containerSubBindingMx.getContainerSubNo());
                    pickOrderDetail.setBindingNum(orderMx.getPlanNum());
                    pickOrderDetail.setActualNum(orderMx.getActualNum());
                    pickOrderDetail.setStatus(orderMx.getIsComplete());
                    pickOrderDetails.add(pickOrderDetail);
                } else {
                    List<OrderMxBingding> orderMxBingdings = orderMxBingdingMapper.findByMap(MapUtils.put("orderMxId", orderMx.getId()).getMap(), OrderMxBingding.class);
                    if (orderMxBingdings.size() > 0) {
                        OrderMxBingding orderMxBingding = orderMxBingdings.get(0);
                        Goods goods = goodsMapper.findById(orderMx.getGoodsId(), Goods.class);
                        PickOrderDetail pickOrderDetail = new PickOrderDetail();
                        pickOrderDetail.setGoodsName(goods.getGoodsName());
                        pickOrderDetail.setContainerNo(orderMxBingding.getContainerNo());
                        pickOrderDetail.setContainerSubNo(orderMxBingding.getContainerSubNo());
                        pickOrderDetail.setBindingNum(orderMx.getPlanNum());
                        pickOrderDetail.setActualNum(orderMx.getActualNum());
                        pickOrderDetail.setStatus(orderMx.getIsComplete());
                        pickOrderDetails.add(pickOrderDetail);
                    }
                }
            }
        }

        return pickOrderDetails;
    }

    @Autowired
    private BillHzHistoryMapper billHzHistoryMapper;

    @Autowired
    private OrderBoxMxMapper orderBoxMxMapper;

    @Autowired
    private OrderBoxHzMapper orderBoxHzMapper;

    @Autowired
    private UpdateClientStationInfoService updateClientStationInfoService;


    @Override
    public List<PickBillDto> getPickBillDto(String waveNo, String dealerId,String billNo) throws Exception {

        List<PickBillDto> pickBillDtos = new ArrayList<>();
        List<BillHzHistory> billHzHistories = billHzHistoryMapper.findByMap(MapUtils.put("waveNo", waveNo).put("dealerId", dealerId).put("billNo",billNo).getMap(), BillHzHistory.class);
        if (billHzHistories.size() == 0) {
            throw new Exception("未查询到清单信息");
        }
        BillHzHistory billHzHistory = billHzHistories.get(0);
        //订单汇总Id
        int orderHzId = billHzHistory.getOrderHzId();
        Map<String, Integer> statusMap = ztStoreMapper.findByMap(MapUtils.put("taskType", SxStore.TASKTYPE_BZ).getMap(), ZtStore.class).stream().collect(Collectors.toMap(ZtStore::getContainerNo, ZtStore::getStatus));
        List<OrderMx> orderMxList = orderMxMapper.findByMap(MapUtils.put("orderHzId", orderHzId).getMap(), OrderMx.class);
        for (OrderMx orderMx : orderMxList) {

            List<ContainerSubBindingMx> orderMxId = containerSubBindingMxMapper.findByMap(MapUtils.put("orderMxId", orderMx.getId()).getMap(), ContainerSubBindingMx.class);
            if (orderMxId.size() > 0) {
                ContainerSubBindingMx containerSubBindingMx = orderMxId.get(0);
                Goods goods = goodsMapper.findById(orderMx.getGoodsId(), Goods.class);
                PickBillDto pickBillDto = new PickBillDto();
                pickBillDto.setGoodsName(goods.getGoodsName());
                pickBillDto.setGoodsNo(goods.getGoodsNo());
                pickBillDto.setPickNum(orderMx.getPlanNum());
                pickBillDto.setIsNotPick(orderMx.getIsNotPick());
                pickBillDto.setStatus(orderMx.getIsComplete());
                PickContainerDto pickContainerDto = new PickContainerDto();
                pickContainerDto.setContainerSubNo(containerSubBindingMx.getContainerSubNo());
                //OrderBoxMx orderBoxMx = orderBoxMxMapper.findByMap(MapUtils.put("orderMxId", orderMx.getId()).getMap(), OrderBoxMx.class).get(0);
                pickContainerDto.setBindingNum(orderMx.getPlanNum());
                String containerNo = containerSubBindingMx.getContainerNo();
                int status = 0;
                if (statusMap.containsKey(containerNo)) {
                    status = statusMap.get(containerNo);
                }
                //是否完成
                pickContainerDto.setStatus(status);
                pickBillDto.setPickContainerDto(pickContainerDto);
                pickBillDtos.add(pickBillDto);
            }else {
                List<OrderMxBingding> orderMxBingdings = orderMxBingdingMapper.findByMap(MapUtils.put("orderMxId", orderMx.getId()).getMap(), OrderMxBingding.class);
                if (orderMxBingdings.size() > 0) {
                    OrderMxBingding orderMxBingding = orderMxBingdings.get(0);
                    Goods goods = goodsMapper.findById(orderMx.getGoodsId(), Goods.class);
                    PickBillDto pickBillDto = new PickBillDto();
                    pickBillDto.setGoodsName(goods.getGoodsName());
                    pickBillDto.setGoodsNo(goods.getGoodsNo());
                    pickBillDto.setPickNum(orderMx.getPlanNum());
                    pickBillDto.setIsNotPick(orderMx.getIsNotPick());
                    pickBillDto.setStatus(orderMx.getIsComplete());
                    PickContainerDto pickContainerDto = new PickContainerDto();
                    pickContainerDto.setContainerSubNo(orderMxBingding.getContainerSubNo());
                    List<OrderBoxMx> orderMxId1 = orderBoxMxMapper.findByMap(MapUtils.put("orderMxId", orderMx.getId()).getMap(), OrderBoxMx.class);
                    if (orderMxId1.size() > 0) {
                        OrderBoxMx orderBoxMx = orderMxId1.get(0);
                        pickContainerDto.setOrderBoxNo(orderBoxMx.getOrderBoxNo());
                    }
                    pickContainerDto.setBindingNum(orderMx.getPlanNum());
                    String containerNo = orderMxBingding.getContainerNo();
                    int status = 0;
                    if (statusMap.containsKey(containerNo)) {
                        status = statusMap.get(containerNo);
                    }
                    //是否完成
                    pickContainerDto.setStatus(status);
                    pickBillDto.setPickContainerDto(pickContainerDto);
                    pickBillDtos.add(pickBillDto);
                }


            }
        }

        return pickBillDtos;
    }

    @Override
    public List<StationsDto> getAllStation() throws Exception {
        return stationMapper.getAllStations();
    }

    @Override
    public void seedLight(int stationId) throws Exception {
        Station station = stationMapper.findById(stationId, Station.class);
        List<StationOrderPosition> stationOrderPositions = stationDdxService.findByMap(MapUtils.put("stationId", stationId).getMap());
        List<StationOrderPosition> collect = stationOrderPositions.stream().filter(x -> x.getOrderId() != 0).collect(Collectors.toList());
        for (StationOrderPosition stationOrderPosition : collect) {
            List<ContainerSubBindingMx> containerSubBindingMxes = containerSubBindingMxMapper.findByMap(MapUtils.put("orderHzId", stationOrderPosition.getOrderId()).put("picking", true).getMap(), ContainerSubBindingMx.class);
            if (containerSubBindingMxes.size()==0) {
                continue;
            }else{
                ContainerSubBindingMx containerSubBindingMx = containerSubBindingMxes.get(0);
                Container container = containerMapper.findByMap(MapUtils.put("containerNo", containerSubBindingMx.getContainerNo()).getMap(), Container.class).get(0);
                List<StationLxPosition> ss = stationLxService.findByMap(MapUtils.put("containerNo", container.getContainerNo()).getMap());
                if (ss.size()==0) {
                    throw new Exception(container.getContainerNo()+"料箱不在"+station.getStationNo()+"号站台");
                }
                int type = container.getLayoutType();
                String[] containerNos = LightUtils.getLightPosition(container.getContainerNo(), containerSubBindingMx.getContainerSubNo(), type, ss.get(0).getContainerDirection(), Integer.valueOf(ss.get(0).getPositionNo()));
                String[] orderBoxNos = new String[]{stationOrderPosition.getLightNo()};
                String[] strings = Arrays.copyOf(containerNos, containerNos.length+1);
                strings[containerNos.length] = orderBoxNos[0];
                String taskId = TaskUtils.gerenateTaskId();
                commandService.sendLightCommand(taskId, station.getStationNo(), String.join(",", strings));
            }
        }
    }

    @Override
    public List<SeedTaskDto> getSeedTask(String containerNo, String goodsNo, String goodsName) {
        return stationMapper.getSeedTask(containerNo,goodsNo,goodsName);
    }


    @Override
    public void cutOrder(String stationNo) throws Exception  {

            List<Station> station = stationMapper.findByMap(MapUtils.put("stationNo", stationNo).getMap(), Station.class);
            int stationPickId = station.get(0).getCurrentStationPickId();//获取PickId

            List<PickOrder> pickOrders = pickOrderMapper.findByMap(MapUtils.put("id", stationPickId).getMap(), PickOrder.class);
            if(pickOrders != null){
                //料箱是否全部达到（1：是，0：否）
//                如果料箱已经全部到达，则可以截单.
                 PickOrder pickOrder = pickOrders.get(0);
                  int isAllArrive = pickOrder.getIsAllArrive();

                //未全部到达：查看该拣选单的任务是否已经完成
                if(isAllArrive==0){
                    //未完成
                    List<ContainerBindingHz> containerBindingHzList = containerBindingHzMapper.findByMap(MapUtils.put("pickingOrderId",pickOrder.getId()).getMap(),ContainerBindingHz.class);
                    if(containerBindingHzList != null && containerBindingHzList.size()!=0){
                        throw new Exception("该拣选单还有任务未完成");
                    }else {
                        //已完成
                        pickOrder.setIsAllArrive(1);
                        pickOrderMapper.update(pickOrder);

                        /*查看后面有没有未绑定orderId的汇总,如果有就说明后面还有未分配的任务进行，如果没有则
                        后面没有任务，需要手动发出指令让料箱前进。*/

                        //查出所有的OrderHz,过滤掉所有的pickOrderId!=null的LIST集合。
                        List<OrderHz> allOrderHz= orderHzMapper.findByMap(null,OrderHz.class);
                        if(allOrderHz.size() != 0){
                            if((allOrderHz.stream().filter(orderHz->(orderHz.getPickingOrderId()!=null)).collect(Collectors.toList())).size()== 0){
                                //size = 0 说明所有的OrderHz都绑定了pickOrderId,后面没有任务了 此时需要手动调用指令让料箱走
                                iStationLxService.cuttingBoxOp(pickOrder.getId());
                            }
                        }else{
                            throw new Exception("ORDER HZ为空！");
                        }

                    }

                }
            }

    }

    @Override
    @Transactional
    public void orderInit(int positionNo, int stationId) {
        List<StationOrderPosition> stationOrderPosition = stationDdxService.findByMap(MapUtils.put("stationId", stationId).put("positionNo", positionNo).getMap());
        if (stationOrderPosition!=null && stationOrderPosition.size()>0) {
            String point = stationOrderPosition.get(0).getDeviceNo();
            List<WCSTask> task = taskService.getByTarget(point);
            if (task!=null && task.size()>0){
                taskService.delete(task.get(0).getId());
                stationOrderPosition.get(0).setStatus((short) 0);
                stationDdxService.update(stationOrderPosition.get(0));
            }
        }
    }
}
