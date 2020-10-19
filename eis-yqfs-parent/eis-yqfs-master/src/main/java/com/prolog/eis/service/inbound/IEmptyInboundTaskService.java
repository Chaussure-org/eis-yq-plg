package com.prolog.eis.service.inbound;

import com.prolog.eis.model.inbound.EmptyInboundTask;

import java.util.List;
import java.util.Map;

public interface IEmptyInboundTaskService {

    List<EmptyInboundTask> getEmptyInboundTask(Map map) throws Exception;
    /**
     * 空箱入库转历史
     */
    void toEmptyInboundHis(String containerNo) throws Exception;
}
