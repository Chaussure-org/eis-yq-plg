package com.prolog.eis.pickstation.client.impl;

import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dao.order.OrderMxMapper;
import com.prolog.eis.model.masterbase.Container;
import com.prolog.eis.model.masterbase.ContainerSub;

import com.prolog.eis.model.order.OrderMx;
import com.prolog.eis.pickstation.client.UpdateClientStationInfoService;
import com.prolog.eis.pickstation.dao.StationLxMapper;
import com.prolog.eis.pickstation.dao.StationMapper;
import com.prolog.eis.pickstation.dao.StationOrderMapper;
import com.prolog.eis.pickstation.dto.*;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.pickstation.model.StationOrderPosition;
import com.prolog.eis.pickstation.service.IStationDdxService;
import com.prolog.eis.pickstation.service.IStationLxService;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.masterbase.ContainerService;
import com.prolog.eis.util.LightUtils;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologHttpUtils;
import com.prolog.eis.util.TaskUtils;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.DateUtils;
import com.prolog.framework.utils.JsonUtils;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author SunPP and Rod Johnson
 * @date 2020/07/01 16:57
 */
@Service
public class UpdateClientStationInfoImpl implements UpdateClientStationInfoService {

    private final Logger logger = LoggerFactory.getLogger(SendCsClientServiceImpl.class);
    @Autowired
    private StationLxMapper stationLxMapper;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StationOrderMapper stationOrderMapper;

    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private IStationService stationService;

    @Autowired
    private EisProperties properties;

    @Autowired
    private IStationLxService stationLxService;

    @Autowired
    private IStationDdxService stationDdxService;

    @Autowired
    private OrderMxMapper orderMxMapper;
    @Autowired
    private ContainerService containerService;

    @Autowired
    private IWCSCommandService commandService;

    @Override
    public void sendStationInfoToClient(int stationId) {
        StationInfoDto stationSeedInfo = this.findStationSeedInfo(stationId);
        String seedUrl = "/GetStationInfo";
        this.sendHotpotClient(stationSeedInfo, stationId, seedUrl);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StationInfoDto findStationSeedInfo(int stationId) {
        List<StationLxPosition> lxPositionList = stationLxMapper.findByMap(MapUtils.put("stationId", stationId).getMap(), StationLxPosition.class);
        List<StationOrderPosition> orderPositionList = stationOrderMapper.findByMap(MapUtils.put("stationId", stationId).getMap(), StationOrderPosition.class);
        //查找到位料箱播种信息
        List<SeedContainerSubDto> seedContainerSubDtoList = stationMapper.getStationSeedinfo(stationId);

        List<SeedOrderBoxDto> seedOrderBoxDtos = stationMapper.getSeedOrderBoxInfo(stationId);
        //无当前播种货格
//        if (seedContainerSubDtoList.size() == 0) {
//            return;
//        }

        lxPositionList.stream().sorted(Comparator.comparing(StationLxPosition::getPositionNo));
        List<StationInfoLxPositionDto> lxPList = new ArrayList<>(2);
        for (int i = 0; i < lxPositionList.size(); i++) {
            StationInfoLxPositionDto stationInfoLxPositionDto = new StationInfoLxPositionDto();
            lxPList.add(stationInfoLxPositionDto);
        }
        //K 料箱号， value：子容器的
        Map<String, List<SeedContainerSubDto>> containerMap = seedContainerSubDtoList.stream().collect(Collectors.groupingBy(SeedContainerSubDto::getContainerNo));
        for (String k : containerMap.keySet()) {
            StationInfoLxDto stationInfoLxDto = new StationInfoLxDto();
            List<StationInfoSubLxDto> subLxDtoList = new ArrayList<>();
            for (SeedContainerSubDto subDto : containerMap.get(k)) {
                StationInfoSubLxDto subLxDto = new StationInfoSubLxDto();
                subLxDto.setBarCode(subDto.getBarCode());
                subLxDto.setGoodsName(subDto.getGoodsName());
                subLxDto.setSeedCount(subDto.getBindingNum());
                subLxDto.setIsFinish(subDto.getIsFinish());

                subLxDto.setPlanNum(
                        //所有的货格绑定明细 同一个货格的 统计总的计划数量
                        containerMap.get(k).stream().filter(p -> p.getContainerSubNo().equals(subDto.getContainerSubNo())
                        ).mapToInt(SeedContainerSubDto::getBindingNum).sum());
                subLxDto.setActualNum(
                        containerMap.get(k).stream().filter(p -> p.getContainerSubNo().equals(subDto.getContainerSubNo())
                        ).mapToInt(SeedContainerSubDto::getActualNum).sum());
                subLxDto.setIsCurrent(subDto.getPicking());
                //货格的库存数量-当前货格已经播过的数量
                subLxDto.setSubNum(subDto.getSubNum()-subLxDto.getActualNum());
                subLxDto.setSubContainerNo(subDto.getContainerSubNo());
                subLxDto.setOrderId(subDto.getOrderId());
                subLxDtoList.add(subLxDto);
                List<StationInfoSubLxDto> collect = subLxDtoList.stream().filter(
                        p -> p.getSubContainerNo().equals(subDto.getContainerSubNo())).collect(Collectors.toList());
                if (collect.size() > 1 && subDto.getPicking() != 1) {
                    subLxDtoList.remove(subLxDto);
                }
                if (collect.size() > 1 && subDto.getPicking() == 1){
                    subLxDtoList.remove(collect.stream().filter(p -> p.getIsCurrent() == 0).findFirst().get());
                }
            }
            //给料箱对象里的货格List  赋值
            stationInfoLxDto.setContainerNo(k);
            stationInfoLxDto.setLayoutType(containerMap.get(k).get(0).getLayoutType());

            stationInfoLxDto.setSubLxList(
                    subLxDtoList.stream().sorted(Comparator.comparing(StationInfoSubLxDto::getSubContainerNo)).collect(Collectors.toList())
            );

            //料箱对象应该放在哪个位置
            for (int i = 0; i < lxPositionList.size(); i++) {
                if (k.equals(lxPositionList.get(i).getContainerNo())) {
                    stationInfoLxDto.setContainerDirection(lxPositionList.get(i).getContainerDirection());
                    lxPList.get(i).setLiaoXiang(stationInfoLxDto);
                }
            }

        }

        StationInfoDto stationInfoDto = new StationInfoDto();
        stationInfoDto.setLxPositionList(lxPList);
        stationInfoDto.setStationId(stationId);
        List<StationInfoDdxPositionDto> DdxList = this.SetDdxInfo(seedOrderBoxDtos, lxPList);
        stationInfoDto.setDdxPositionList(DdxList);

        return stationInfoDto;
    }


    private List<StationInfoDdxPositionDto> SetDdxInfo(List<SeedOrderBoxDto> seedOrderBoxDtos, List<StationInfoLxPositionDto> lxPList) {
        List<String> hzids = seedOrderBoxDtos.stream().map(x -> x.getOrderId() + "").collect(Collectors.toList());
        String str = String.join(",", hzids);
        List<SeedNumDto> seedNumDtoList = stationMapper.getSeedNum(str);
        Criteria ctr = Criteria.forClass(OrderMx.class);
        ctr.setRestriction(Restrictions.in("orderHzId", hzids.toArray()));
        List<OrderMx> seedMxList = orderMxMapper.findByCriteria(ctr);

        List<StationInfoDdxPositionDto> stationInfoDdxPositionDtos = new ArrayList<>(5);
        boolean isCurrent = false;
        for (SeedOrderBoxDto seedOrdeDto : seedOrderBoxDtos) {
            StationInfoDdxPositionDto stationInfoDdxPositionDto = new StationInfoDdxPositionDto();
            stationInfoDdxPositionDto.setDdxNo(seedOrdeDto.getContainerNo());
            stationInfoDdxPositionDto.setPositionNo(seedOrdeDto.getPositionNo());
            //添加订单标识
            stationInfoDdxPositionDto.setType(seedOrdeDto.getType());
            //统计订单箱的 计划数量和实际数量
            int planNum = seedMxList.stream().filter(x -> x.getOrderHzId() == seedOrdeDto.getOrderId()).mapToInt(OrderMx::getPlanNum).sum();
            int actualNum = seedMxList.stream().filter(x -> x.getOrderHzId() == seedOrdeDto.getOrderId()).mapToInt(OrderMx::getActualNum).sum();

            stationInfoDdxPositionDto.setActualNum(actualNum);
            stationInfoDdxPositionDto.setPlanNum(planNum);

            //当前订单有多个订单箱
//            List<SeedNumDto> orderBoxFinishList = seedNumDtoList.stream().filter(x -> x.getHzId() == seedOrdeDto.getOrderId()).collect(Collectors.toList());
//            if ( orderBoxFinishList.size()> 1) {
//                int notCurrentNum = orderBoxFinishList.stream().filter(p->p.getOrderBoxNo()!=seedOrdeDto.getContainerNo()).mapToInt(SeedNumDto::getBoxNum).sum();
//                stationInfoDdxPositionDto.setActualNum(actualNum - notCurrentNum);
//                stationInfoDdxPositionDto.setPlanNum(planNum - notCurrentNum);
//            } else {
//                stationInfoDdxPositionDto.setActualNum(actualNum);
//                stationInfoDdxPositionDto.setPlanNum(planNum);
//            }


            //找出当前的正在播种的订单框
            if (!isCurrent) {
                for (StationInfoLxPositionDto lx : lxPList) {
                    if (lx.getLiaoXiang() == null) {
                        continue;
                    }
                    for (StationInfoSubLxDto sub : lx.getLiaoXiang().getSubLxList()) {
                        if (sub.getIsCurrent() == 1 && sub.getOrderId() == seedOrdeDto.getOrderId()) {
                            stationInfoDdxPositionDto.setIsCurrent(1);
                            isCurrent = true;
                            break;
                        }
                    }
                    if (isCurrent) {
                        break;
                    }
                }
            }

            stationInfoDdxPositionDto.setOrderId(seedOrdeDto.getOrderId());
            stationInfoDdxPositionDtos.add(stationInfoDdxPositionDto);
        }
        return stationInfoDdxPositionDtos;
    }


    public void sendHotpotClient(StationInfoDto stationInfoDto, int stationId,String url) {
        try {
            String stationInfoStr = PrologApiJsonHelper.toJson(stationInfoDto);
            String stationIpAddress = getStationIpAddress(stationId);
            String postUrl = String.format("http://%s:%s%s", stationIpAddress, properties.getClientPort(), url);
            logger.info("EIS -> EIS Client {} : params:{}", postUrl, stationInfoStr);
            String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(stationInfoStr), String.class);
            logger.info("EIS <- EIS Client {} : result:{}", postUrl, restJson);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
            Boolean ret = helper.getBoolean("Success");
            if (ret) {
                logger.info(stationId + "料箱详情发送成功");
            } else {
                logger.error(stationId + "料箱详情发送失败");
            }
        } catch (Exception e) {
            logger.error(stationId + "料箱详情发送失败" + e.toString());
        }
//        try {
//            this.lightOn(stationInfoDto, stationId);
//            logger.info("播种信息发送时亮灯接口调用成功");
//        } catch (Exception e) {
//            logger.info("播种信息发送时亮灯接口调用失败", e);
//        }
    }

    @Override
    public void sendMsgToClient(String msg, int msgType, Date date,int stationId) {
        try{
            String format = DateUtils.format(date, "yyyy/MM/dd HH:mm:ss");
            Map<String, Object> map = MapUtils.put("msg", msg).put("msgType", msgType).put("msgTime", format).getMap();
            String msgStr = PrologApiJsonHelper.toJson(map);
            String stationIpAddress = getStationIpAddress(stationId);
            String postUrl = String.format("http://%s:%s%s", stationIpAddress, properties.getClientPort(), "/StationMsg");
            logger.info("EIS -> EIS Client {} : params:{}", postUrl, msgStr);
            String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(msgStr), String.class);
            logger.info("EIS <- EIS Client {} : result:{}", postUrl, restJson);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
            Boolean ret = helper.getBoolean("Success");
            if (ret){
                logger.info("消息发送成功");
            }else{
                logger.info("消息发送失败");
            }
        }catch (Exception e){
            logger.info("消息发送失败",e);
        }
    }

    public void lightOn(StationInfoDto stationInfoDto, int stationId) throws Exception {
        if (stationInfoDto == null || stationInfoDto.getLxPositionList() == null || stationInfoDto.getLxPositionList().size() == 0 || stationInfoDto.getLxPositionList().get(0) == null) {
            return;
        }

        List<StationInfoLxPositionDto> pis = stationInfoDto.getLxPositionList().stream().filter(x -> {
            if (x.getLiaoXiang() == null) {
                return false;
            }
            if (x.getLiaoXiang().getSubLxList() == null || x.getLiaoXiang().getSubLxList().size() == 0) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());

        if (pis.size() == 0)
            return;

        StationInfoSubLxDto csb = null;
        StationInfoLxPositionDto sd = null;

        for (StationInfoLxPositionDto sp : pis) {
            Optional<StationInfoSubLxDto> csbop = sp.getLiaoXiang().getSubLxList().stream().filter(k -> k.getIsCurrent() > 0).findFirst();
            if (csbop.isPresent()) {
                csb = csbop.get();
                sd = sp;
                break;
            }
        }

        if (csb == null) {
            commandService.sendLightCommand(TaskUtils.gerenateTaskId(), stationService.getStation(stationId).getStationNo(), "");
            return;
        }

        StationInfoLxDto liaoXiang = sd.getLiaoXiang();
        List<StationLxPosition> stationLxPositions = stationLxService.findByMap(MapUtils.put("containerNo", liaoXiang.getContainerNo()).getMap());
        Optional<StationInfoDdxPositionDto> siddop = stationInfoDto.getDdxPositionList().stream().filter(x -> !StringUtils.isBlank(x.getDdxNo()) && x.getIsCurrent() > 0).findFirst();

        if (!siddop.isPresent()) {
            commandService.sendLightCommand(TaskUtils.gerenateTaskId(), stationService.getStation(stationId).getStationNo(), "");
            return;
        }

        StationInfoDdxPositionDto sidd = siddop.get();
        String ddxNo = sidd.getDdxNo();
        List<StationOrderPosition> stationOrderPositions = stationDdxService.findByMap(MapUtils.put("containerNo", ddxNo).getMap());

        String[] containerNos = LightUtils.getLightPosition(
                liaoXiang.getContainerNo(), liaoXiang.getSubLxList().get(0).getSubContainerNo(), liaoXiang.getLayoutType(),
                liaoXiang.getContainerDirection(), Integer.parseInt(stationLxPositions.get(0).getPositionNo()));


        String[] orderBoxNos = new String[]{stationOrderPositions.get(0).getLightNo()};
        String[] strings = Arrays.copyOf(containerNos, containerNos.length + 1);
        strings[containerNos.length] = orderBoxNos[0];
        String taskId = TaskUtils.gerenateTaskId();
        commandService.sendLightCommand(taskId, stationService.getStation(stationId).getStationNo(), String.join(",", strings));
    }

    /**
     * 亮灯
     *
     * @param stationId
     * @param containerNo
     * @param containerSubNo
     * @param containerLayout
     * @param direction
     * @param lxPositonNo
     * @param orderLightNo
     */
    @Override
    public void lightOn(int stationId, String containerNo, String containerSubNo, int containerLayout, String direction, String lxPositonNo, String orderLightNo) {
        String lights = "";
        if (!StringUtils.isBlank(containerNo)) {
            String[] lightArray = LightUtils.getLightPosition(containerNo, containerSubNo, containerLayout, direction, Integer.parseInt(lxPositonNo));
            List<String> llist = new ArrayList<>(Arrays.asList(lightArray));
            llist.add(orderLightNo);
            lights = String.join(",", llist);
        }
        try {
            commandService.sendLightCommand(TaskUtils.gerenateTaskId(), stationService.getStation(stationId).getStationNo(), lights);
        } catch (Exception e) {
            logger.info("发送亮灯失败," + e.getMessage());
        }
    }

    private String getStationIpAddress(int stationId) throws Exception {
        return stationService.getStation(stationId).getIpAddress();
    }
}

