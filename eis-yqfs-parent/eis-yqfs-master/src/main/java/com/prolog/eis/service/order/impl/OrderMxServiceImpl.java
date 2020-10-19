package com.prolog.eis.service.order.impl;

import com.prolog.eis.dao.order.OrderMxMapper;
import com.prolog.eis.dto.yqfs.PickOrderMxDto;
import com.prolog.eis.model.order.OrderHz;
import com.prolog.eis.model.order.OrderMx;
import com.prolog.eis.service.order.IOrderMxService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author wangkang
 * @Description 实现类
 * @CreateTime 2020/6/20 12:06
 */
@Service
public class OrderMxServiceImpl implements IOrderMxService {
    @Autowired
    private OrderMxMapper mapper;

    @Override
    public void update(OrderMx orderMx) {
        mapper.update(orderMx);
    }

    @Override
    public OrderMx findById(Integer orderMxId) {
        return mapper.findById(orderMxId, OrderMx.class);
    }

    @Override
    public List<PickOrderMxDto> findOrderMxByPick(int pickOrderId) {
        return mapper.findOrderMxByPick(pickOrderId);
    }

    @Override
    public List<OrderMx> findByMap(Map map) {
        return mapper.findByMap(map, OrderMx.class);
    }

    @Override
    public void deleteBatch(List<OrderMx> list) {
        if (list == null || list.size() == 0)
            return;
        List<Integer> collect = list.stream().map(s -> s.getId()).collect(Collectors.toList());
        Integer[] array = collect.toArray(new Integer[collect.size()]);
        mapper.deleteByIds(array, OrderMx.class);
    }

    @Override
    public void saveBatch(List<OrderMx> orderMxList) {
        mapper.saveBatch(orderMxList);
    }

    @Override
    public List<OrderMx> findByOrderHzIds(int[] orderIds) {
        Criteria ctr = Criteria.forClass(OrderMx.class);
        Object[] oids = Arrays.stream(orderIds).boxed().toArray();
        ctr.setRestriction(Restrictions.in("orderHzId",oids));
        return mapper.findByCriteria(ctr);
    }
}
