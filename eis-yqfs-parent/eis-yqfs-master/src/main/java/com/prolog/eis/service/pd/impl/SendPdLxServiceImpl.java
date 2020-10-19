package com.prolog.eis.service.pd.impl;

import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dto.pd.PdDwContainerDto;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.pd.SendPdLxService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/6/15 10:59
 */

@Service
public class SendPdLxServiceImpl implements SendPdLxService {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(SendPdLxServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EisProperties properties;
    @Autowired
    private IStationService stationService;

    @Override
    public void sendPdLxTask(int stationId, PdDwContainerDto requests) {
        try {
            String ip = stationService.getIp(stationId);

            String postUrl = String.format("http://%s:%s%s",ip,properties.getClientPort(),"/lxPdArrive");
            String data = PrologApiJsonHelper.toJson(requests);
            String result = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(result);
            boolean success = helper.getBoolean("Success");
            if (!success) {
                String message = helper.getString("Data");
                throw new Exception("盘点推送播种站台失败------>" + message);
            }else {
                logger.info("sendPdLxTask,推送盘点任务 JSON："+data);
            }
        } catch (Exception e) {
            logger.info("sendPdLxTask失败"+e.getMessage(),e);
        }
    }
}
