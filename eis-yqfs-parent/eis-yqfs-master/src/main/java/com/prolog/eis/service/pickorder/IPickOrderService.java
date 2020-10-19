package com.prolog.eis.service.pickorder;

import com.prolog.eis.dto.station.StationsDto;
import com.prolog.eis.pickstation.model.PickOrder;

import java.util.List;
import java.util.Map;

public interface IPickOrderService {

    /**
     * 新增拣选单
     * @param pickOrder
     * @throws Exception
     */
    void add(PickOrder pickOrder) throws Exception;

    PickOrder findById(int pickOrderId);

    /**
     * 根据料箱获取拣选单
     * @param stationId
     * @param containerNo
     * @return
     */
    PickOrder getByContainer(int stationId,String containerNo);

    /**
     * 根据订单获取拣选单
     * @param orderId
     * @return
     */
    PickOrder getByOrderId(int orderId);

    /**
     * 获取当前拣选单缺货信息
     * @param stationNo
     * @return
     */
    Map<String, List> getPickOrderInfo(String stationNo) throws Exception;
}
