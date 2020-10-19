package com.prolog.eis.service.order.impl;

import com.prolog.eis.dao.order.OrderHzHistoryMapper;
import com.prolog.eis.dao.order.OrderHzMapper;
import com.prolog.eis.dao.order.OrderMxMapper;
import com.prolog.eis.model.order.OrderHz;
import com.prolog.eis.model.order.OrderHzHistory;
import com.prolog.eis.model.order.OrderMx;
import com.prolog.eis.service.order.IOrderHzHistoryService;
import com.prolog.eis.service.order.IOrderMxHistoryService;
import com.prolog.eis.service.order.IOrderMxService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author wangkang
 * @Description 订单汇总历史实现
 * @CreateTime 2020/6/20 12:47
 */
@Service
public class OrderHzHistoryServiceImpl implements IOrderHzHistoryService {

    @Autowired
    private OrderHzHistoryMapper mapper;
    @Autowired
    private OrderHzMapper orderHzMapper;
    @Autowired
    private IOrderMxService mxService;
    @Autowired
    private IOrderMxHistoryService mxHistoryService;

    @Override
    public void add(OrderHzHistory orderHzHistory) {
        mapper.save(orderHzHistory);
    }

    @Override
    public void toHistory(OrderHz orderHz) throws Exception {
        List<OrderMx> mxs = mxService.findByMap(MapUtils.put("orderHzId",orderHz.getId()).getMap());
        if(mxs.size()>0){
            for(OrderMx mx:mxs){
                mxHistoryService.toHistory(mx);
            }
        }
        OrderHzHistory orderHzHistory = new OrderHzHistory();
        BeanUtils.copyProperties(orderHz,orderHzHistory);
        try{
            mapper.save(orderHzHistory);
            orderHzMapper.deleteById(orderHz.getId(),OrderHz.class);
        }catch (Exception e){
            throw new Exception("订单汇总转历史失败");
        }

    }

}
