package com.prolog.eis.service.order;

import com.prolog.eis.dto.yqfs.PickOrderMxDto;
import com.prolog.eis.model.order.OrderMx;

import java.util.List;
import java.util.Map;

/**
 * @Author wangkang
 * @Description 订单明细服务
 * @CreateTime 2020/6/20 12:05
 */
public interface IOrderMxService {
    void update(OrderMx orderMx);
    OrderMx findById(Integer orderMxId);
    List<PickOrderMxDto> findOrderMxByPick(int pickOrderId);
    List<OrderMx> findByMap(Map map);
    void deleteBatch(List<OrderMx> list);
    void saveBatch(List<OrderMx> orderMxList);

    List<OrderMx> findByOrderHzIds(int[] orderIds);
}
