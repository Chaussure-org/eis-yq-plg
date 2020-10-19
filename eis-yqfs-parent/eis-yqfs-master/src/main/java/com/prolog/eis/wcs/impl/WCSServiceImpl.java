package com.prolog.eis.wcs.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dao.store.SxLayerMapper;
import com.prolog.eis.model.log.Log;
import com.prolog.eis.model.point.PointLocation;
import com.prolog.eis.model.store.SxCeng;
import com.prolog.eis.service.log.ILogService;
import com.prolog.eis.service.pointlocation.IPointLocationService;
import com.prolog.eis.util.ContainerUtils;
import com.prolog.eis.util.HttpUtils;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.wcs.dto.CarInfoDTO;
import com.prolog.eis.wcs.dto.CarListDTO;
import com.prolog.eis.wcs.dto.HoisterInfoDto;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSService;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.utils.JsonUtils;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WCSServiceImpl implements IWCSService {
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private EisProperties properties;
    private final Logger logger = LoggerFactory.getLogger(WCSServiceImpl.class);
    @Autowired
    private IPointLocationService pointLocationService;
    @Autowired
    private ILogService logService;
    @Autowired
    private SxLayerMapper sxLayerMapper;
    @Autowired
    private IWCSCommandService commandService;

    /**
     * 获取完整路径
     * @param path
     * @return
     */
    private String getUrl(String path){
        return String.format("http://%s:%s%s",properties.getWcs().getHost(),properties.getWcs().getPort(),path);
    }
    /**
     * 获取每层小车信息
     *
     * @return
     */
    @Override
    public List<CarInfoDTO> getCarInfo() {
        String url = this.getUrl(properties.getWcs().getRequestCarUrl());
        Log log = new Log(null,null,"发起查询小车信息任务","EIS -> WCS",null,null,null,null,new Date());
        logger.info("EIS -> WCS 请求小车信息:{}",url);
        try {
            RestMessage<CarListDTO> result = httpUtils.post(url, null, new TypeReference<RestMessage<CarListDTO>>() {});
            return result.getData().getCarryList();
        } catch (Exception e) {
            logger.warn("EIS -> WCS 请求小车信息异常",e);
            log.setException(e.getMessage());
            log.setSuccess(false);
            logService.save(log);
            return null;
        }
//        List<CarInfoDTO> list = new ArrayList<>();
//        CarInfoDTO carInfoDTO = new CarInfoDTO();
//        carInfoDTO.setStatus(1);
//        carInfoDTO.setLayer(14);
//        carInfoDTO.setRgvId("223");
//        list.add(carInfoDTO);
//        return list;
    }

    /**
     * 获取三个提升机的信息
     *
     * @return
     */

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<HoisterInfoDto> getHoisterInfoDto() {
        String url = this.getUrl(properties.getWcs().getRequestHoisterUrl());
        Log log = new Log(null,null,"发起查询提升机任务","EIS -> WCS",null,null,null,null,new Date());
        logger.info("EIS -> WCS 获取提升机信息:{}", url);

        try {
            String data = this.restTemplate.postForObject(url, null, String.class);
            logger.info("+++++++++++++++提升机:"+data+"++++++++++++++++++");
            log.setSuccess(true);
            logService.save(log);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(data);
            return helper.getObjectList("data", HoisterInfoDto.class);

        } catch (Exception e) {
            log.setException(e.getMessage());
            log.setSuccess(false);
            logService.save(log);
            logger.warn("EIS -> WCS 提升机信息异常", e);
            return null;
        }
    }


    /**
     * 小车换层
     *
     * @param taskId
     * @param carId
     * @param sourceLayer
     * @param targetLayer
     * @return
     */
    @Override
    public  RestMessage<String> moveCar(String taskId, int carId, int sourceLayer, int targetLayer) {
        String url = this.getUrl(properties.getWcs().getMoveCarUrl());
        Log log = new Log(null,null,"发起小车换层任务","EIS -> WCS",null,null,null,null,new Date());
        log.setParams("{\"taskId\":\""+taskId+"\",\"carId\":\""+carId+"\",\"sourceLayer\":\""+sourceLayer+"\",\"targetLayer\":\""+targetLayer+"\"}");
        logger.info("EIS -> WCS 请求小车换层:{}",url);
        try {
            RestMessage<String> result = httpUtils.post(url, MapUtils.put("taskId", taskId).put("source", sourceLayer).put("target", targetLayer).put("bankId", properties.getWcs().getBankId()).getMap(), new TypeReference<RestMessage<String>>() {});
            log.setSuccess(true);
            logService.save(log);
            return result;
        } catch (Exception e) {
            logger.warn("EIS -> WCS 请求小车换层异常",e);
            log.setException(e.getMessage());
            log.setSuccess(false);
            logService.save(log);
            return RestMessage.newInstance(false,"500",e.getMessage(),null);
        }
    }

    /**
     * 输送线行走
     *
     * @param taskId
     * @param address
     * @param target
     * @param containerNo
     * @param type
     * @return
     */
    @Override
    public RestMessage<String> lineMove(String taskId, String address, String target, String containerNo, int type,int i) {
        String url = this.getUrl(properties.getWcs().getLineMoveUrl());
        Log log = new Log(null,containerNo,"发起输送线行走任务","EIS -> WCS",type,null,null,null,new Date());
        log.setParams("{\"taskId\":\""+taskId+"\",\"address\":\""+address+"\",\"target\":\""+target+"\",\"containerNo\":\""+containerNo+"\",\"type\":\""+type+"\"}");
        logger.info("EIS -> WCS 输送线行走:{}",url);
        try {
            RestMessage<String> result = httpUtils.post(url,MapUtils.put("taskId",taskId).put("address",address).put("containerNo",containerNo).put("type",type).put("target",target).getMap(),new TypeReference<RestMessage<String>>() {});
            log.setSuccess(true);
            logService.save(log);
            if (i==0){
                if (!result.isSuccess()){
                    return this.lineMove(taskId,address,target,containerNo,type,1);
                }else {
                    return result;
                }
            }else{
                return result;
            }
        } catch (Exception e) {
            logger.warn("EIS -> WCS 请求输送线行走异常",e);
            log.setException(e.getMessage());
            log.setSuccess(false);
            logService.save(log);
            return RestMessage.newInstance(false,"500",e.getMessage(),null);
        }
    }

    /**
     * 请求订单箱
     *
     * @param taskId
     * @param address
     * @return
     */
    @Override
    public RestMessage<String> requestOrderBox(String taskId, String address) {
        String url = this.getUrl(properties.getWcs().getOrderBoxReqUrl());
        Log log = new Log(null,null,"订单箱请求","EIS -> WCS",null,null,null,null,new Date());
        logger.info("EIS -> WCS 订单框请求:{}",url);
        log.setParams("{\"taskId\":\""+taskId+"\",\"address\":\""+address+"\"}");
        try {
            RestMessage<String> result = httpUtils.post(url,MapUtils.put("taskId",taskId).put("address",address).getMap(),new TypeReference<RestMessage<String>>() {});
            log.setSuccess(true);
            logService.save(log);
            return result;
        } catch (Exception e) {
            logger.warn("EIS -> WCS 请求订单框异常",e);
            log.setException(e.getMessage());
            log.setSuccess(false);
            logService.save(log);
            return RestMessage.newInstance(false,"500",e.getMessage(),null);
        }
    }

    /**
     * 亮灯
     *
     * @param pickStationNo
     * @param lights
     * @return
     */
    @Override
    public RestMessage<String> light(String pickStationNo, String[] lights) {
        String url = this.getUrl(properties.getWcs().getLightControlUrl());
        Log log = new Log(null,null,"发起亮灯任务","EIS -> WCS",null,null,null,null,new Date());
        log.setParams("{\"pickStationNo\":\""+pickStationNo+"\",\"light\":\""+Arrays.toString(lights)+"\"}");
        logger.info("EIS -> WCS 灯光控制请求:{}",url);
        try {
            RestMessage<String> result = httpUtils.post(url,MapUtils.put("stationNo",pickStationNo).put("lights",lights).getMap(),new TypeReference<RestMessage<String>>() {});
            log.setSuccess(true);
            logService.save(log);
            return result;
        } catch (Exception e) {
            logger.warn("EIS -> WCS 请求灯光异常",e);
            log.setException(e.getMessage());
            log.setSuccess(false);
            logService.save(log);
            return RestMessage.newInstance(false,"500",e.getMessage(),null);
        }
    }

    /**
     * 出入库指令
     *
     * @param taskId
     * @param type
     * @param containerNo
     * @param address
     * @param target
     * @param weight
     * @param priority
     * @return
     */
    @Override
    public RestMessage<String> sendContainerTask(String taskId, int type, String containerNo, String address, String target, String weight, String priority,int status) {
        String url = this.getUrl(properties.getWcs().getTaskUrl());
        Log log = new Log(null,containerNo,"发起小车接送料箱任务","EIS -> WCS",type,null,null,null,new Date());
        logger.info("EIS -> WCS 任务请求:{}",url);
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String, Object> map = MapUtils.put("taskId", taskId)
                .put("type", type)
                .put("bankId", properties.getWcs().getBankId())
                .put("containerNo", containerNo)
                .put("address", address)
                .put("target", target)
                .put("weight", weight)
                .put("priority", priority)
                .put("status",status)
                .getMap();
        list.add(map);
        log.setParams(Arrays.toString(list.toArray()));
        try {
            RestMessage<String> result = httpUtils.post(url,MapUtils.put("carryList",list.toArray()).getMap(),new TypeReference<RestMessage<String>>() {});
            log.setSuccess(true);
            logService.save(log);
            return result;
        } catch (Exception e) {
            logger.warn("EIS -> WCS 任务请求异常",e);
            log.setException(e.getMessage());
            log.setSuccess(false);
            logService.save(log);

            return RestMessage.newInstance(false,"500",e.getMessage(),null);
        }
    }

    /**
     * 安全门控制
     * @param doorNo
     * @param open
     * @throws Exception
     */
    @Override
    public void openDoor(String doorNo,boolean open) throws Exception{
        if(StringUtils.isBlank(doorNo)){
            throw new Exception("安全门编号不能为空");
        }
        Log log = null;
        try {
            log = new Log(null, null, "安全门调用", "WCS -> EIS", 0, "{\"doorNo\":"+doorNo+",\"open\":"+open+"}", null, null, new Date());
        } catch (Exception e) {
            logger.info("日志生成错误");
        }
        logService.save(log);
        switch (doorNo){
            case "004":
                for (int i = 1; i < 6; i++) {
                    SxCeng sxCeng = new SxCeng();
                    sxCeng.setLayer(i);
                    if (open) {
                        sxCeng.setLockState(1);
                    }else {
                        sxCeng.setLockState(0);
                    }
                    sxCeng.setDisable(open);
                    sxLayerMapper.updateByDoor(sxCeng);
                }
                break;
            case "005":
                for (int i = 6; i < 10; i++) {
                    SxCeng sxCeng = new SxCeng();
                    sxCeng.setLayer(i);
                    if (open) {
                        sxCeng.setLockState(1);
                    }else {
                        sxCeng.setLockState(0);
                    }
                    sxCeng.setDisable(open);
                    sxLayerMapper.updateByDoor(sxCeng);
                }
                break;
            case "006":
                for (int i = 10; i < 14; i++) {
                    SxCeng sxCeng = new SxCeng();
                    sxCeng.setLayer(i);
                    if (open) {
                        sxCeng.setLockState(1);
                    }else {
                        sxCeng.setLockState(0);
                    }
                    sxCeng.setDisable(open);
                    sxLayerMapper.updateByDoor(sxCeng);
                }
                break;
            default:
                List<PointLocation> list = pointLocationService.findByMap(MapUtils.put("doorNo",doorNo).getMap());
                if(list.size()==0){
                    return;
                }
                for(PointLocation point:list){
                    point.setDisable(open);
                    pointLocationService.update(point);
                }
        }


    }

    @Override
    public RestMessage<String> deleteAbnormalContainerNo(String taskId, String containerNo) throws Exception {
        String url = this.getUrl(properties.getWcs().getRequestBankTaskUrl());
        Log log = new Log(null,containerNo,"删除异常库存料箱","EIS -> WCS",null,null,null,null,new Date());
        logger.info("EIS -> WCS 删除异常入库料箱:{}", url);

        try {
            RestMessage<String> result = httpUtils.post(url,MapUtils.put("taskId",taskId).put("containerNo",containerNo).getMap(),new TypeReference<RestMessage<String>>() {});
            log.setSuccess(true);
            logService.save(log);
            return result;

        } catch (Exception e) {
            log.setException(e.getMessage());
            log.setSuccess(false);
            logService.save(log);
            logger.warn("EIS -> WCS 删除异常入库料箱", e);
            return null;
        }
    }
}
