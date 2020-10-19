package com.prolog.eis.service.order.impl;

import com.prolog.eis.dao.order.ContainerSubBindingMxMapper;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.eis.service.order.IContainerSubBingdingMxService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Order;
import com.prolog.framework.core.restriction.Restriction;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author wangkang
 * @Description 货格绑定明细实现类
 * @CreateTime 2020/6/20 11:34
 */
@Service
public class ContainerSubBingdingMxServiceImpl implements IContainerSubBingdingMxService {

    @Autowired
    private ContainerSubBindingMxMapper containerSubBindingMxMapper;
    @Override
    public List<ContainerSubBindingMx> findByMap(Map map) {
        return containerSubBindingMxMapper.findByMap(map,ContainerSubBindingMx.class);
    }

    @Override
    public void update(ContainerSubBindingMx containerSubBindingMx) {
        containerSubBindingMxMapper.update(containerSubBindingMx);
    }

    @Override
    public List<ContainerSubBindingMx> findMxAsc(String containerSubNo) {
        return containerSubBindingMxMapper.findMxAsc(containerSubNo);
    }

    @Override
    public List<ContainerSubBindingMx> findMxByContainerNoAsc(String containerNo) {
        Criteria ctr = Criteria.forClass(ContainerSubBindingMx.class);
        Restriction r1 = Restrictions.eq("picking",0);
        Restriction r2 = Restrictions.eq("containerNo",containerNo);
        Restriction r3 = Restrictions.notIn("orderHzId","select order_id from station_order_position where is_change=1");
        ctr.setRestriction(Restrictions.and(r1,r2,r3));
        ctr.setOrder(Order.newInstance().asc("containerSubNo").asc("orderHzId"));
        return containerSubBindingMxMapper.findByCriteria(ctr);
       // return containerSubBindingMxMapper.findMxByContainerNoAsc(containerNo);
    }

    @Override
    public List<ContainerSubBindingMx> findMxByContainerNoDesc(String containerNo) {
        Criteria ctr = Criteria.forClass(ContainerSubBindingMx.class);
        Restriction r1 = Restrictions.eq("picking",0);
        Restriction r2 = Restrictions.eq("containerNo",containerNo);
        Restriction r3 = Restrictions.notIn("orderHzId","select order_id from station_order_position where is_change=1");
        ctr.setRestriction(Restrictions.and(r1,r2,r3));
        ctr.setOrder(Order.newInstance().desc("containerSubNo").asc("orderHzId"));
        return containerSubBindingMxMapper.findByCriteria(ctr);
    }

    @Override
    public void deleteBatch(List<ContainerSubBindingMx> list) {
        List<Integer> collect = list.stream().map(s -> s.getOrderMxId()).collect(Collectors.toList());
        Integer[] array = collect.toArray(new Integer[collect.size()]);
        Criteria criteria = new Criteria(ContainerSubBindingMx.class);
        criteria.setRestriction(Restrictions.in("orderMxId",array));
        containerSubBindingMxMapper.deleteByCriteria(criteria);
    }

    @Override
    public void delete(ContainerSubBindingMx containerSubBindingMx) {
        containerSubBindingMxMapper.deleteByMap(MapUtils.put("id",containerSubBindingMx.getId()).getMap(),ContainerSubBindingMx.class);
    }

    @Override
    public List<ContainerSubBindingMx> findByCriteria(Criteria ctr) {
        return containerSubBindingMxMapper.findByCriteria(ctr);
    }

    @Override
    @Transactional
    public void setPicking(int containerSubBindingMxId, int stationId) {
        Criteria ctr = Criteria.forClass(ContainerSubBindingMx.class);
        ctr.setRestriction(Restrictions.in("containerNo","select container_no from container_binding_hz where station_id = "+stationId));
        containerSubBindingMxMapper.updateMapByCriteria(MapUtils.put("picking",false).getMap(),ctr);
        Criteria ctr2 = Criteria.forClass(ContainerSubBindingMx.class);
        ctr2.setRestriction(Restrictions.eq("id",containerSubBindingMxId));
        containerSubBindingMxMapper.updateMapByCriteria(MapUtils.put("picking",true).getMap(),ctr2);

    }
}
