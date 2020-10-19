package com.prolog.eis.service.order.impl;

import com.prolog.eis.dao.order.*;
import com.prolog.eis.dao.yqfs.OutboundWarnMapper;
import com.prolog.eis.dto.outbound.OutBoundTaskHzDto;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.order.*;
import com.prolog.eis.service.masterbase.GoodsService;
import com.prolog.eis.service.monitor.ISpCountService;
import com.prolog.eis.service.order.*;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restriction;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author wangkang
 * @Description 订单汇总实现类
 * @CreateTime 2020/6/20 11:55
 */
@Service
public class OrderHzServiceImpl implements IOrderHzService {
    @Autowired
    private OrderHzMapper mapper;
    @Autowired
    private OrderMxMapper orderMxMapper;
    @Autowired
    private OrderHzMapper orderHzMapper;
    @Autowired
    private IBillHzService billHzService;

    @Autowired
    private IBillMxService billMxService;

    @Autowired
    private BillHzMapper billHzMapper;

    @Autowired
    private BillMxMapper billMxMapper;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private IOrderMxService orderMxService;

    @Autowired
    private IBillHzHistoryService billHzHistoryService;

    @Autowired
    private IBillMxHistoryService billMxHistoryService;

    @Autowired
    private OutboundWarnMapper outboundWarnMapper;

    @Autowired
    private BillFailingMapper billFailingMapper;
    @Autowired
    private ISpCountService spCountService;

    @Override
    public OrderHz findById(Integer orderId) {
        return mapper.findById(orderId,OrderHz.class);
    }

    @Override
    public List<OrderHz> findByMap(Map map) {
        return mapper.findByMap(map,OrderHz.class);
    }

    @Override
    public void update(OrderHz orderHz) {
        mapper.update(orderHz);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrderHzMx(OutBoundTaskHzDto outBoundTaskHzDtos) throws Exception {
        List<OrderMx> orderMxs = outBoundTaskHzDtos.getOrderMxs();
        OrderHz orderHz = new OrderHz();
        BeanUtils.copyProperties(outBoundTaskHzDtos,orderHz);
        orderHz.setConfirmTime(new Date());
        orderHz.setDealTime(new Date());
        orderHz.setOrderCreateTime(new Date());
        orderHz.setCreateTime(new Date());
        orderHz.setLastDateTime(new Date());
        mapper.save(orderHz);
        Integer id = orderHz.getId();
        for (OrderMx orderMx : orderMxs) {
            orderMx.setOrderHzId(id);
            orderMx.setCreateTime(new Date());
            orderMx.setUpdateTime(new Date());
            orderMxMapper.save(orderMx);
        }
    }

    /**
     * 根据拣选单获取未绑定的订单
     *
     * @param pickOrderId
     * @return
     */
    @Override
    public List<OrderHz> getUnbindingOrdersByPickOrderId(int pickOrderId) {
       Criteria ctr = Criteria.forClass(OrderHz.class);
       Restriction r1 = Restrictions.eq("pickingOrderId",pickOrderId);
        Restriction r2 = Restrictions.notIn("id","select order_id from station_order_position");
       ctr.setRestriction(Restrictions.and(r1,r2));
        return mapper.findByCriteria(ctr);
    }

    /**
     * 根据拣选单获取订单列表
     *
     * @param pickOrderId
     * @return
     */
    @Override
    public List<OrderHz> getByPickOrderId(int pickOrderId) {
        return mapper.findByMap(MapUtils.put("pickingOrderId",pickOrderId).getMap(),OrderHz.class);
    }

    @Override
    public void updatePriority(int id, int priority) throws Exception {
        Criteria ctr=Criteria.forClass(OrderHz.class);
        ctr.setRestriction(Restrictions.eq("id",id));
        orderHzMapper.updateMapByCriteria(MapUtils.put("priority",priority).getMap(),ctr);
    }

    @Override
    public void save(OrderHz orderHz) {
        orderHzMapper.save(orderHz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestMessage<String> createOrder() throws Exception {
        // 7.29  修改库存验证
//        List<GoodsOutboundWarnDto> spCount = outboundWarnMapper.getSpCount();
//        Map<Integer, GoodsOutboundWarnDto> stroerCount = spCount.stream().collect(Collectors.toMap(GoodsOutboundWarnDto::getGoodsId, Function.identity(), (k1, k2) -> {
//            k1.setKuCunCount(k1.getKuCunCount() + k2.getKuCunCount());
//            return k1;
//        }));
        Map<Integer, Integer> spCount = spCountService.getSpCount();

        List<BillMx> all = billMxService.getAll();


        //Map<Integer, BillMx> collect = all.stream().collect(Collectors.toMap(BillMx::getId, x -> x));
        List<BillFailing> billFailings = new ArrayList<>();
        Date date = new Date();

        for (BillMx billMx : all) {

            if (!spCount.containsKey(billMx.getGoodsId())) {
                //商品不存在
                int billHzId = billMx.getBillHzId();
                BillHz billHz = billHzMapper.findById(billHzId, BillHz.class);
                BillFailing billFailing = new BillFailing();
                billFailing.setBillNo(billHz.getBillNo());
                billFailing.setDealerId(billHz.getDealerId());
                Goods goods = goodsService.getGoodsById(billMx.getGoodsId());
                billFailing.setWaveNo(billHz.getWaveNo());
                billFailing.setPlanNum(billMx.getPlanNum());
                billFailing.setGoodsNo(goods.getGoodsNo());
                billFailing.setGoodsName(goods.getGoodsName());
                billFailing.setCreateTime(date);
                billFailings.add(billFailing);
                //删除明细
                billMxMapper.deleteById(billMx.getId(), BillMx.class);
                //没有明细后删除汇总信息
                if (billMxMapper.findByMap(MapUtils.put("billHzId", billHzId).getMap(), BillMx.class).size() == 0) {
                    billHzMapper.deleteById(billHzId, BillHz.class);
                }

            } else {
                //商品存在

                //商品库存数量\
                //当前明细需要的数量
                int kuCunCount = spCount.get(billMx.getGoodsId());
                int planNum = billMx.getPlanNum();

                if (kuCunCount >= planNum) {
                    spCount.put(billMx.getGoodsId(),kuCunCount-planNum);
                } else {
                    //不满足出库删除明细
                    int billHzId = billMx.getBillHzId();
                    BillHz billHz = billHzMapper.findById(billHzId, BillHz.class);
                    BillFailing billFailing = new BillFailing();
                    billFailing.setBillNo(billHz.getBillNo());
                    billFailing.setDealerId(billHz.getDealerId());
                    Goods goods = goodsService.getGoodsById(billMx.getGoodsId());
                    billFailing.setGoodsNo(goods.getGoodsNo());
                    billFailing.setWaveNo(billHz.getWaveNo());
                    billFailing.setGoodsName(goods.getGoodsName());
                    billFailing.setPlanNum(billMx.getPlanNum());
                    billFailing.setCreateTime(date);
                    billFailings.add(billFailing);
                    //删除明细
                    billMxMapper.deleteById(billMx.getId(), BillMx.class);
                    //没有明细后删除汇总信息
                    if (billMxMapper.findByMap(MapUtils.put("billHzId", billHzId).getMap(), BillMx.class).size() == 0) {
                        billHzMapper.deleteById(billHzId, BillHz.class);
                    }
                }
            }
        }
        if (billFailings.size()!=0) {
            billFailingMapper.saveBatch(billFailings);
        }
        //1.找到当前所有的清单,已波次和客户分类
        List<BillHz> billHzs = billHzService.getBillHz();
        if (billHzs.size() > 0) {
            List<String> waveNoList = billHzs.stream().map(x -> x.getWaveNo()).collect(Collectors.toList());
            waveNoList = waveNoList.stream().distinct().collect(Collectors.toList());
            for (String s : waveNoList) {
                //同一波次的所有客户
                if (StringUtils.isBlank(s)) {
                    throw new Exception("未找到对应波次");
                }
                List<BillHz> billHzes = billHzService.getBillHz(s);
                List<Integer> custList = billHzes.stream().map(x -> x.getDealerId()).collect(Collectors.toList());
                custList = custList.stream().distinct().collect(Collectors.toList());
                for (int dealerId : custList) {
                    //同一波次同一用户生成一个订单汇总
                    List<BillHz> billHz = billHzService.getBillHz(dealerId, s);
                    OrderHz orderHz = new OrderHz();
                    orderHz.setExpectTime(billHz.get(0).getExpectTime());
                    orderHz.setPriority(billHz.get(0).getPriority());
                    orderHz.setIsAddPool(0);
                    orderHz.setCreateTime(new Date());
                    mapper.save(orderHz);
                    int orderHzId = orderHz.getId();
                    List<OrderMx> orderMxList = new ArrayList<>();
                    for (BillHz hz : billHz) {
                        hz.setOrderHzId(orderHzId);
                        List<BillMx> billMxs = billMxService.getBillMx(hz.getId());
                        if (billMxs.size() == 0) {
                            throw new Exception(hz.getBillNo() + "该清单未导入清单明细");
                        }
                        for (BillMx billMx : billMxs) {
                            //找到订单对应的明细
                            OrderMx orderMx = new OrderMx();
                            orderMx.setOrderHzId(orderHzId);
                            orderMx.setGoodsId(billMx.getGoodsId());
                            orderMx.setPlanNum(billMx.getPlanNum());
                            orderMx.setIsComplete(0);
                            orderMx.setActualNum(0);
                            orderMx.setIsNotPick(0);
                            orderMx.setOutNum(0);
                            orderMx.setCreateTime(new Date());
                            orderMxList.add(orderMx);
                        }
                        //清单明细转历史
                        billMxHistoryService.toHistory(billMxs);
                    }
                    Map<Integer, OrderMx> collect = orderMxList.stream().collect(Collectors.toMap(OrderMx::getGoodsId, Function.identity(),(k1,k2)->{
                        k1.setPlanNum(k1.getPlanNum()+k2.getPlanNum());
                        return k1;
                    }));
                    List<OrderMx> orderMxes = new ArrayList<>(collect.values());
                    orderMxService.saveBatch(orderMxes);
                    //清单汇总转历史
                    billHzHistoryService.toHistory(billHz);
                }
            }
        }
        //1.分别插入订单汇总和
        return RestMessage.newInstance(true, "合单成功");
    }

    @Override
    public void delete(int[] ids) throws Exception {
        Object[] idArray = Arrays.stream(ids).boxed().toArray();
        orderHzMapper.deleteByIds(idArray,OrderHz.class);
    }
    @Override
    public RestMessage<String> deleteBillMxHz() throws Exception {
        billHzMapper.deleteByMap(null,BillHz.class);
        billMxMapper.deleteByMap(null,BillMx.class);
        return RestMessage.newInstance(true,"删除BillMxHz成功");
    }
}
