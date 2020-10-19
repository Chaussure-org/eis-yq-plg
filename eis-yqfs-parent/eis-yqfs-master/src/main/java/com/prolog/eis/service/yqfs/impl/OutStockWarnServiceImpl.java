package com.prolog.eis.service.yqfs.impl;

import com.prolog.eis.dao.masterbase.GoodsMapper;
import com.prolog.eis.dao.order.OrderMxMapper;
import com.prolog.eis.dao.yqfs.OutboundWarnMapper;
import com.prolog.eis.dto.yqfs.GoodsOutboundWarnDto;
import com.prolog.eis.model.order.OrderMx;
import com.prolog.eis.model.yqfs.OutboundWarn;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OutStockWarnServiceImpl {

    @Autowired
    private OutboundWarnMapper outboundWarnMapper;

    @Autowired
    private OrderMxMapper orderMxMapper;

    private GoodsMapper goodsMapper;

    /**
     * Spp
     * 缺货预警列表
     */
    public List<GoodsOutboundWarnDto> getOutStockList(int warnNum) {
        //库存里的数量
        List<GoodsOutboundWarnDto> goodsCount = outboundWarnMapper.getSrtoreCount();
        //商品id 商品库存
        Map<Integer, GoodsOutboundWarnDto> stroerCount = goodsCount.stream().collect(Collectors.toMap(GoodsOutboundWarnDto::getGoodsId, Function.identity(), (k1, k2) -> {
            k1.setKuCunCount(k1.getKuCunCount() + k2.getKuCunCount());
            return k1;
        }));
        List<GoodsOutboundWarnDto> goodsAbleCount = outboundWarnMapper.getAbleSrtoreCount();
        Map<Integer, GoodsOutboundWarnDto> stroerAbleCount = goodsAbleCount.stream().collect(Collectors.toMap(GoodsOutboundWarnDto::getGoodsId, Function.identity(), (k1, k2) -> {
            k1.setKuCunCount(k1.getKuCunCount() + k2.getKuCunCount());
            return k1;
        }));
        //在途库存的数量
//        Map<Integer, GoodsOutboundWarnDto> zTstroerCount = outboundWarnMapper.getZtStoreCount().stream().collect(Collectors.toMap(GoodsOutboundWarnDto::getGoodsId, Function.identity(), (k1, k2) -> {
//            k1.setKuCunCount(k1.getKuCunCount() + k2.getKuCunCount());
//            return k1;
//        }));
//        for (Integer key : stroerCount.keySet()) {
//            if (zTstroerCount.containsKey(key)) {
//                stroerCount.get(key).setKuCunCount(stroerCount.get(key).getKuCunCount() + zTstroerCount.get(key).getKuCunCount());
//            }
//        }
        List<OrderMx> mxList = orderMxMapper.findByMap(null, OrderMx.class);
        //商品id mxList
        Map<Integer, List<OrderMx>> mxGoodsIdMap = mxList.stream().collect(Collectors.groupingBy(OrderMx::getGoodsId));
        Map<Integer, GoodsOutboundWarnDto> stroerAbCount = new HashMap<>();
        stroerCount.forEach((k,v)->{
            if (mxGoodsIdMap.containsKey(k)){
                int orderNeedCount = mxList.stream().filter(p -> p.getGoodsId() == k.intValue()).mapToInt(OrderMx::getPlanNum).sum();
                v.setOrderNeedCount(orderNeedCount);
            }else{
                v.setOrderNeedCount(0);
            }
            if (stroerAbleCount.containsKey(k)){
                v.setKuCunCount(stroerAbleCount.get(k).getKuCunCount());
            }
            if (v.getKuCunCount()-v.getOrderNeedCount()<0) {
                v.setAbleKuCunCount(v.getKuCunCount()-v.getOrderNeedCount());
            }else{
                v.setAbleKuCunCount(0);
            }
            stroerAbCount.put(k, v);
        });
        //map  key：商品id
        mxGoodsIdMap.forEach((k, v) -> {
            int orderNeedCount = mxList.stream().filter(p -> p.getGoodsId() == k.intValue()).mapToInt(OrderMx::getPlanNum).sum();
            if (!stroerAbCount.containsKey(k)) {
                GoodsOutboundWarnDto goodsOutboundWarnDto = new GoodsOutboundWarnDto();
                List<GoodsOutboundWarnDto> cIdDto = outboundWarnMapper.getCIdDto(k);
                if (cIdDto.size()>0) {
                    goodsOutboundWarnDto.setGoodsId(cIdDto.get(0).getGoodsId());
                    goodsOutboundWarnDto.setOwnerName(cIdDto.get(0).getOwnerName());
                    goodsOutboundWarnDto.setBarCode(cIdDto.get(0).getBarCode());
                    goodsOutboundWarnDto.setGoodsName(cIdDto.get(0).getGoodsName());
                    goodsOutboundWarnDto.setKuCunCount(0);
                    goodsOutboundWarnDto.setAbleKuCunCount(0-orderNeedCount);
                    goodsOutboundWarnDto.setOrderNeedCount(orderNeedCount);
                    stroerAbCount.put(k, goodsOutboundWarnDto);
                }
            }

        });
        ArrayList<GoodsOutboundWarnDto> goodsOutboundWarnDtos = new ArrayList<>(stroerAbCount.values());
        // 修改预警时订单数量大于可出库数量
        List<GoodsOutboundWarnDto> warnDtos = goodsOutboundWarnDtos.stream().filter(
                t -> t.getKuCunCount() < warnNum || t.getOrderNeedCount()>t.getKuCunCount()).collect(Collectors.toList());
        return warnDtos;
    }

    /**
     * 获取指定天数的历史监控数据
     *
     * @param days
     * @param modulus
     * @return
     */
    private List<OutboundWarn> getHistoryDataByDay(int days, Double modulus) {
        List<OutboundWarn> historyInventory = outboundWarnMapper.getHistoryDataByDay(days);
        if (!CollectionUtils.isEmpty(historyInventory)) {
            //乘以预警系数
            historyInventory.stream().forEach(outboundWarn -> {
                if (outboundWarn.getExpectOutnum() != 0) {
                    int dayOut = outboundWarn.getExpectOutnum() / days;
                    outboundWarn.setDayOut(dayOut);
                    outboundWarn.setExpectOutnum((int) Math.ceil(dayOut * modulus));
                }
            });
        }
        return historyInventory;
    }

}
