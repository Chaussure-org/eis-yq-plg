package com.prolog.eis.service.order;

import com.prolog.eis.model.order.OrderHz;
import com.prolog.eis.model.order.OrderHzHistory;

/**
 * @Author wangkang
 * @Description 订单汇总历史
 * @CreateTime 2020/6/20 12:46
 */
public interface IOrderHzHistoryService {
    void add(OrderHzHistory orderHzHistory);
    void toHistory(OrderHz orderHz)throws Exception;
}
