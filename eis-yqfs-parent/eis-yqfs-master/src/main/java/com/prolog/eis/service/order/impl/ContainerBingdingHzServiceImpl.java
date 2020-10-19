package com.prolog.eis.service.order.impl;

import com.prolog.eis.dao.order.ContainerBindingHzMapper;
import com.prolog.eis.dao.order.ContainerSubBindingMxMapper;
import com.prolog.eis.model.order.ContainerBindingHz;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.eis.model.store.ZtStore;
import com.prolog.eis.service.order.IContainerBindingHzService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restriction;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author wangkang
 * @Description 料箱绑定汇总实现类
 * @CreateTime 2020/6/20 11:18
 */
@Service
public class ContainerBingdingHzServiceImpl implements IContainerBindingHzService {

    @Autowired
    private ContainerBindingHzMapper containerBindingHzMapper;
    @Autowired
    private ContainerSubBindingMxMapper mxMapper;
    @Override
    public List<ContainerBindingHz> findByMap(Map map) {
        return containerBindingHzMapper.findByMap(map,ContainerBindingHz.class);
    }

    @Override
    public List<Integer> findOrderIdAsc(String containerNo) {
        return containerBindingHzMapper.findOrderIdAsc(containerNo);
    }

    @Override
    public List<Integer> findOrderIdDesc(String containerNo) {
        return containerBindingHzMapper.findOrderIdDesc(containerNo);
    }

    @Override
    public void deleteBatch(List<ContainerBindingHz> list) {
        List<String> stringList = list.stream().map(s -> s.getContainerNo()).collect(Collectors.toList());
        String[] ts = stringList.toArray(new String[stringList.size()]);
        Criteria criteria = new Criteria(ContainerBindingHz.class);
        criteria.setRestriction(Restrictions.in("containerNo",ts));
        containerBindingHzMapper.deleteByCriteria(criteria);
    }

    @Override
    public void delete(String containerNo) {
        containerBindingHzMapper.deleteById(containerNo,ContainerBindingHz.class);
    }

    /**
     * 根据拣选单删除绑定汇总（同时删除绑定明细）
     *
     * @param pickOrderId
     */
    @Override
    @Transactional
    public void deleteByPickOrderId(int pickOrderId) {
        Criteria ctr = Criteria.forClass(ContainerSubBindingMx.class);
        ctr.setRestriction(Restrictions.in("containerNo","select container_no from container_binding_hz where picking_order_id = "+pickOrderId));
        mxMapper.deleteByCriteria(ctr);
        containerBindingHzMapper.deleteByMap(MapUtils.put("pickingOrderId",pickOrderId).getMap(),ContainerBindingHz.class);
    }
}
