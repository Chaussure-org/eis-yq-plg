package com.prolog.eis.service.order.impl;

import com.prolog.eis.dao.order.OrderMxHistoryMapper;
import com.prolog.eis.dao.order.OrderMxMapper;
import com.prolog.eis.model.order.OrderHz;
import com.prolog.eis.model.order.OrderMx;
import com.prolog.eis.model.order.OrderMxHistory;
import com.prolog.eis.service.order.IOrderHzService;
import com.prolog.eis.service.order.IOrderMxHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author wangkang
 * @Description 订单历史服务实现类
 * @CreateTime 2020/6/20 15:04
 */
@Service
public class OrderMxHistoryServiceImpl implements IOrderMxHistoryService {

    @Autowired
    private OrderMxMapper orderMxMapper;
    @Autowired
    private OrderMxHistoryMapper orderMxHistoryMapper;
    @Override
    public void toHistory(OrderMx orderMx) throws Exception {
        OrderMxHistory orderMxHistory = new OrderMxHistory();
        BeanUtils.copyProperties(orderMx,orderMxHistory);
        orderMxHistory.setOutboundTaskHzId(orderMx.getOrderHzId());
        try {
            orderMxHistoryMapper.save(orderMxHistory);
            orderMxMapper.deleteById(orderMx.getId(), OrderMx.class);
        }catch (Exception e){
            throw new Exception("订单明细转历史失败");
        }

    }
}
