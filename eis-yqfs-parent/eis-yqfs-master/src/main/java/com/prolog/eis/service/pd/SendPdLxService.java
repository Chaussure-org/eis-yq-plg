package com.prolog.eis.service.pd;

import com.prolog.eis.dto.pd.PdDwContainerDto;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/6/15 10:56
 */
public interface SendPdLxService {


    /**
     * 盘点到位向前端推送消息
     * @param stationId 站台id
     * @param requests  消息内容
     */
    public void sendPdLxTask(int stationId, PdDwContainerDto requests);

}
