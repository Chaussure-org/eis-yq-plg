package com.prolog.eis.pickstation.client;

import com.prolog.eis.pickstation.dto.StationInfoDto;

import java.util.Date;

/**
 * @author SunPP and Rod Johnson
 * @date 2020/07/01 16:57
 */
public interface UpdateClientStationInfoService {
    /**
     * 与客户端同步站台信息
     * @param stationId
     */
    void sendStationInfoToClient(int stationId);

    StationInfoDto findStationSeedInfo(int stationId);

    void lightOn(int stationId,String containerNo,String containerSubNo,int containerLayout,String direction,String lxPositonNo,String orderLightNo);

     void sendHotpotClient(StationInfoDto stationInfoDto, int stationId,String url);

     void sendMsgToClient(String msg, int msgType, Date date,int stationId);

}
