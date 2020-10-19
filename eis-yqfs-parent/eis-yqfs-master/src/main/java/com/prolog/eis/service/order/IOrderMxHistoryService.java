package com.prolog.eis.service.order;

import com.prolog.eis.model.order.OrderMx;

/**
 * @Author wangkang
 * @Description 订单明细历史服务
 * @CreateTime 2020/6/20 15:04
 */
public interface IOrderMxHistoryService {

    void toHistory(OrderMx orderMx)throws Exception;
}
