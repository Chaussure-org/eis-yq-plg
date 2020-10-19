package com.prolog.eis.pickstation.client.impl;

import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dao.assembl.AssemblBoxHzMapper;
import com.prolog.eis.dto.yqfs.PickOrderMxDto;
import com.prolog.eis.model.assembl.AssemblBoxHz;
import com.prolog.eis.model.masterbase.Container;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.pickstation.client.SendCsClientService;
import com.prolog.eis.pickstation.client.UpdateClientStationInfoService;
import com.prolog.eis.pickstation.dto.*;
import com.prolog.eis.pickstation.eis.EisStationInterfaceService;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.pickstation.model.StationOrderPosition;
import com.prolog.eis.pickstation.service.IStationDdxService;
import com.prolog.eis.pickstation.service.IStationHxService;
import com.prolog.eis.pickstation.service.IStationLxService;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.masterbase.ContainerService;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.eis.service.masterbase.GoodsBarCodeService;
import com.prolog.eis.service.masterbase.GoodsService;
import com.prolog.eis.service.order.IContainerSubBingdingMxService;
import com.prolog.eis.service.order.IOrderMxService;
import com.prolog.eis.util.LightUtils;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologHttpUtils;
import com.prolog.eis.util.TaskUtils;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class SendCsClientServiceImpl implements SendCsClientService {

    private final Logger logger = LoggerFactory.getLogger(SendCsClientServiceImpl.class);

    private static final String LOG_CLASS = "com.prolog.eis.service.pickstation.client.impl.SendCsClientServiceImpl";
    @Autowired
    private EisStationInterfaceService eisStationInterfaceService;
    @Autowired
    private IStationLxService stationLxService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IStationService stationService;
    @Autowired
    private ContainerSubService containerSubInfoService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsBarCodeService goodsBarCodeService;
    @Autowired
    private IOrderMxService orderMxService;
    @Autowired
    private AssemblBoxHzMapper assemblBoxHzMapper;
    @Autowired
    private IStationDdxService stationDdxService;
    @Autowired
    private IContainerSubBingdingMxService containerSubBingdingMxService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private IWCSCommandService commandService;
    @Autowired
    private IStationHxService stationHxService;
    @Autowired
    private EisProperties properties;

    @Autowired
    private UpdateClientStationInfoService updateClientStationInfoService;

    @Override
    public void sendLxDetails(String containerNo, String locationNo, int stationId) throws Exception {
        LxDetailsDto lxDetailsDto = eisStationInterfaceService.queryLxDetails(containerNo);
        List<StationLxPosition> stationLxPositions = stationLxService.findByMap(MapUtils.put("containerNo", containerNo).getMap());
        Station station = stationService.getStation(stationId);
        int taskType = lxDetailsDto.getTaskType();
        int goodsId = 0;
        //合箱执行
        List<String> containerSubNos = null;
        if (taskType == SxStore.TASKTYPE_HX) {
            //查询当前执行合箱计划的商品goodsId

            List<Integer> goodsIds = assemblBoxHzMapper.getGoodsId(containerNo);
           goodsId = goodsIds.get(0);
            //筛选出需要合箱的货格
            containerSubNos = containerSubInfoService.findContainerSubNo(goodsId, containerNo);
        }
        if (stationLxPositions.size() > 0) {
            StationLxPosition stationLxPosition = stationLxPositions.get(0);
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
                //任务类型为合箱执行
                    if (taskType == SxStore.TASKTYPE_HX){
                    for (String containerSubNo : containerSubNos) {
                        if (containerSubNo.equals(lxSubDetailsDto.getLxSubNo())) {
                            sendLxDetailsDto.setHx(true);

                        }
                    }
                }
                list.add(sendLxDetailsDto);
            }
            sendLxDto.setPickContainerSubInfos(list);
            // 发送请求
            String data = PrologApiJsonHelper.toJson(sendLxDto);
            //合箱亮灯
//            List<StationLxPosition> lxPositions = stationLxService.findByMap(MapUtils.put("stationId", stationId).put("status", StationLxPosition.STATUS_ARRIVE).getMap());
//            if (lxDetailsDto.getTaskType() == SxStore.TASKTYPE_HX && lxPositions.size() == 2 ) {
//                String[] lights = stationHxService.getStationLxLightsNo(lxPositions, goodsId);
//                String taskId = TaskUtils.gerenateTaskId();
//
//                try {
//                    commandService.sendLightCommand(taskId, station.getStationNo(), String.join(",", lights));
//                } catch (Exception e) {
//                    logger.info("亮灯失败",e);
//                }
//            }
            logger.info("发送lx详情" + data);
            //同步客户端状态
            updateClientStationInfoService.sendStationInfoToClient(stationId);
           /* try {
                String stationIpAddress = getStationIpAddress(stationId);
                String postUrl = String.format("http://%s:%s%s",stationIpAddress,properties.getClientPort(),"/GetPickContainerInfo");
                String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
                PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
                Boolean ret = helper.getBoolean("Success");
                if (ret) {
                    logger.info(containerNo + "料箱详情发送成功");
                } else {
                    logger.error(containerNo + "料箱详情发送失败");
                }
            }catch (Exception e){
                logger.warn(e.getMessage(),e);
            }*/

        }
    }

    @Override
    public void sendDdxDetails(String containerNo, String locationNo, int stationId) throws Exception {
        try {
            Map<String, String> map = new HashMap<String, String>();
            List<StationOrderPosition> stationOrderPositions = stationDdxService.findByMap(MapUtils.put("deviceNo", locationNo).getMap());
            map.put("id", stationOrderPositions.get(0).getPositionNo());
            map.put("orderBoxNo", containerNo);
            String data = PrologApiJsonHelper.toJson(map);
            logger.info("发送ddx详情" + map.toString());
            String stationIpAddress = getStationIpAddress(stationId);
            String postUrl = String.format("http://%s:%s%s",stationIpAddress,properties.getClientPort(),"/GetPickOrderBox");
            String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
            Boolean ret = helper.getBoolean("Success");
            if (ret) {
                logger.info(containerNo + "订单箱详情发送成功");
            } else {
                logger.error(containerNo + "订单箱详情发送失败");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
        }
    }

    @Override
    public void sendCurrentStoreHz(int pickId, String containerNo, String containSubNo, int stationId) throws Exception {
        // 货格的商品信息
        ContainerSub containerSubInfo = containerSubInfoService.findById(containSubNo);
        // 商品的信息
        Goods goods = goodsService.getGoodsById(containerSubInfo.getCommodityId());
        // 商品的条码信息
        List<String> goodsBarCodes = goodsBarCodeService.findByMap(MapUtils.put("goodId", containerSubInfo.getCommodityId()).getMap());

        //查找所有商品对应应播订单箱号
        List<Integer> orderBoxList = new ArrayList<>();
        List<ContainerSubBindingMx> containerSubBindingMxes = containerSubBingdingMxService.findByMap(MapUtils.put("containerSubNo", containSubNo).getMap());
        for (ContainerSubBindingMx containerSubBindingMx : containerSubBindingMxes) {
            List<StationOrderPosition> stationOrderPositions = stationDdxService.findByMap(MapUtils.put("orderId", containerSubBindingMx.getOrderHzId()).getMap());
            orderBoxList.get(Integer.parseInt(stationOrderPositions.get(0).getPositionNo()));
        }
        // 需要捡取数量
        int num = 0;
        List<PickOrderMxDto> pickOrderMxDtos = orderMxService.findOrderMxByPick(pickId);
        for (PickOrderMxDto pickOrderMxDto : pickOrderMxDtos) {
            if (pickOrderMxDto.getGoodsId() == goods.getId()) {
                num += pickOrderMxDto.getPlanNum();
            }
        }
        // 合成发送的消息
        try {
            SendCurrentStoreHzDto currentStoreHzDto = new SendCurrentStoreHzDto();
            currentStoreHzDto.setID(containerSubInfo.getCommodityId().toString());
            currentStoreHzDto.setGoodsName(goods.getGoodsName());
            currentStoreHzDto.setGoodsBarCode(goodsBarCodes);
            currentStoreHzDto.setContainerNo(containerNo);
            currentStoreHzDto.setPickTotal(num);
            currentStoreHzDto.setIds(orderBoxList);
            currentStoreHzDto.setContainerSubNo(containSubNo);
            String data = PrologApiJsonHelper.toJson(currentStoreHzDto);
            logger.info("发送商品汇总详情" + data);
            String stationIpAddress = getStationIpAddress(stationId);
            String postUrl = String.format("http://%s:%s%s",stationIpAddress,properties.getClientPort(),"/GetPickGoodsMsg");
            String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
            Boolean ret = helper.getBoolean("Success");
            if (ret) {
                logger.info(containerNo + "商品汇总详情发送成功");
            } else {
                logger.error(containerNo + "商品汇总详情发送失败");
            }
        } catch (Exception e) {
           logger.warn(e.getMessage(),e);
        }
    }

    @Override
    public void sendCurrentSeed(String containerNo, String containerSubNo, int stationId, String locationNo) throws Exception {
        ContainerSub containerSubInfo = containerSubInfoService.findById(containerSubNo);
        List<String> goodsBarCodes = goodsBarCodeService.findByMap(MapUtils.put("goodsId", containerSubInfo.getCommodityId()).getMap());
        Goods goods = goodsService.getGoodsById(containerSubInfo.getCommodityId());
        List<StationOrderPosition> stationOrderPositions = stationDdxService.findByMap(MapUtils.put("deviceNo", locationNo).getMap());
        List<ContainerSubBindingMx> containerSubBindingMxes = containerSubBingdingMxService.findByMap(MapUtils.put("orderHzId", stationOrderPositions.get(0).getOrderId()).getMap());
        SendCurrentSeedDto currentSeedDto = new SendCurrentSeedDto();
        currentSeedDto.setContainerNo(containerNo);
        currentSeedDto.setContainerSubNo(containerSubNo);
        currentSeedDto.setId(Integer.valueOf(stationOrderPositions.get(0).getPositionNo()));
        currentSeedDto.setGoodsName(goods.getGoodsName());
        currentSeedDto.setGoodsBarCode(goodsBarCodes);
        currentSeedDto.setOrderBox(stationOrderPositions.get(0).getContainerNo());
        currentSeedDto.setCount(containerSubBindingMxes.get(0).getBindingNum());
        String data = PrologApiJsonHelper.toJson(currentSeedDto);
        logger.info("发送播种消息" + data);
        try {
            String stationIpAddress = getStationIpAddress(stationId);
            String postUrl = String.format("http://%s:%s%s",stationIpAddress,properties.getClientPort(),"/GetPickContainerSubInfo");
            String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
            Boolean ret = helper.getBoolean("Success");
            if (ret) {
                logger.info(containerNo + "播种消息发送成功");
            } else {
                logger.error(containerNo + "播种消息发送失败");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
        }

        try {
            this.lightOn(currentSeedDto, stationId);
            logger.info("播种信息发送时亮灯接口调用成功");
        } catch (Exception e) {
            logger.info("播种信息发送时亮灯接口调用失败",e);
        }
    }

    public void lightOn(SendCurrentSeedDto currentSeedDto, int stationId) throws Exception {
        List<StationLxPosition> stationLxPositions = stationLxService.findByMap(MapUtils.put("containerNo", currentSeedDto.getContainerNo()).getMap());
        List<Container> containers = containerService.findByMap(MapUtils.put("containerNo", currentSeedDto.getContainerNo()).getMap());
        List<StationOrderPosition> stationOrderPositions = stationDdxService.findByMap(MapUtils.put("containerNo", currentSeedDto.getOrderBox()).getMap());
        String[] containerNos = LightUtils.getLightPosition(
                currentSeedDto.getContainerNo(), currentSeedDto.getContainerSubNo(), containers.get(0).getLayoutType(),
                stationLxPositions.get(0).getContainerDirection(), Integer.parseInt(stationLxPositions.get(0).getPositionNo()));
        String[] orderBoxNos = new String[]{stationOrderPositions.get(0).getLightNo()};
        String[] strings = Arrays.copyOf(containerNos, containerNos.length+1);
        strings[containerNos.length] = orderBoxNos[0];
        String taskId = TaskUtils.gerenateTaskId();
        commandService.sendLightCommand(taskId, stationService.getStation(stationId).getStationNo(), String.join(",", strings));
    }

    /**
     * 获取站台Ip地址
     *
     * @param stationId
     * @return
     * @throws Exception
     */
    private String getStationIpAddress(int stationId) throws Exception {
        return stationService.getStation(stationId).getIpAddress();
    }

}
