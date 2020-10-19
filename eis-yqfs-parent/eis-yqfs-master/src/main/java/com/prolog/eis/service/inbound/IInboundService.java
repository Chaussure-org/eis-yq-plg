package com.prolog.eis.service.inbound;

import com.prolog.eis.model.inbound.InboundTask;
import com.prolog.eis.wcs.dto.BCRDataDTO;

import java.util.List;
import java.util.Map;

public interface IInboundService {
    /**
     * bcr入库检测
     * @param bcrDataDTO
     * @throws Exception
     */
    void inboundTaskCallback(BCRDataDTO bcrDataDTO) throws Exception;

    /**
     * 料箱入库转历史
     */
    void toTaskHis(String containerNo) throws Exception;


    /**
     * 入库记录保存
     */
    void saveInboundInfo(String containerNo);
}
