package com.prolog.eis.service.pickorder;

import com.prolog.eis.dao.pickorder.PickOrderMapper;
import com.prolog.eis.dto.pickorder.PickLxBindDto;
import com.prolog.eis.dto.pickorder.PickOrderDetailDto;
import com.prolog.eis.dto.pickorder.PickOrderMxDto;
import com.prolog.eis.dto.pickorder.PickSpCountDto;
import com.prolog.eis.dto.station.StationsDto;
import com.prolog.eis.dto.yqfs.PickOrderDetail;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.order.ContainerBindingHz;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.eis.pickstation.model.PickOrder;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.eis.service.masterbase.GoodsBarCodeService;
import com.prolog.eis.service.masterbase.GoodsService;
import com.prolog.eis.service.order.IContainerBindingHzService;
import com.prolog.eis.service.order.IContainerSubBingdingMxService;
import com.prolog.eis.service.store.IStoreService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restriction;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PickOrderServiceImpl implements IPickOrderService {
    @Autowired
    private PickOrderMapper mapper;
    @Autowired
    private IStationService stationService;
    @Autowired
    private IContainerBindingHzService containerBindingHzService;
    @Autowired
    private IContainerSubBingdingMxService containerSubBingdingMxService;
    @Autowired
    private ContainerSubService containerSubService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsBarCodeService goodsBarCodeService;
    @Autowired
    private IStoreService storeService;

    /**
     * 新增拣选单
     *
     * @param pickOrder
     * @throws Exception
     */
    @Override
    public void add(PickOrder pickOrder) throws Exception {
        mapper.save(pickOrder);
    }

    @Override
    public PickOrder findById(int pickOrderId) {
        return mapper.findById(pickOrderId, PickOrder.class);
    }

    /**
     * 根据料箱获取拣选单
     *
     * @param containerNo
     * @return
     */
    @Override
    public PickOrder getByContainer(int stationId, String containerNo) {
        Criteria ctr = Criteria.forClass(PickOrder.class);
        Restriction r1 = Restrictions.in("id", "select picking_order_id from container_binding_hz where container_no='" + containerNo + "' and station_id=" + stationId);
        ctr.setRestriction(r1);
        List<PickOrder> list = mapper.findByCriteria(ctr);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 根据订单获取拣选单
     *
     * @param orderId
     * @return
     */
    @Override
    public PickOrder getByOrderId(int orderId) {
        Criteria ctr = Criteria.forClass(PickOrder.class);
        Restriction r1 = Restrictions.in("id", "select picking_order_id from order_hz where id=" + orderId);
        ctr.setRestriction(r1);
        List<PickOrder> list = mapper.findByCriteria(ctr);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public Map<String, List> getPickOrderInfo(String stationNo) throws Exception {
        Map<String, List> map = new HashMap<>();
        Station station = stationService.getByNo(stationNo);
        int currentStationPickId = station.getCurrentStationPickId();
        if (currentStationPickId == 0) {
            throw new Exception("当前站台无拣选单");
        }
        List<PickOrderDetailDto> pickOrderInfo = mapper.getPickOrderInfo(currentStationPickId);
        List<Integer> list = new ArrayList<>();
        List<PickLxBindDto> pickLxBindDtos = new ArrayList<>();
        List<PickOrderMxDto> pickOrderMxDtos = new ArrayList<>();
        for (PickOrderDetailDto pickOrderDetailDto : pickOrderInfo) {
            pickLxBindDtos.add(this.getPickLxBindDtos(pickOrderDetailDto));
            pickOrderMxDtos.add(this.getPickOrderMxDtos(pickOrderDetailDto));
            list.add(pickOrderDetailDto.getCommodityId());
        }
        list = list.stream().distinct().collect(Collectors.toList());
        List<PickSpCountDto> pickSpCountDtos = this.getPickSpCountDtos(list);
        map.put("pickLxBindDtos",pickLxBindDtos);
        map.put("pickOrderMxDtos",pickOrderMxDtos);
        map.put("pickSpCountDtos",pickSpCountDtos);
        System.out.println(new Date().getTime());
        return map;
    }


    PickLxBindDto getPickLxBindDtos(PickOrderDetailDto pickOrderDetailDto) {
        PickLxBindDto pickLxBindDto = new PickLxBindDto();
        pickLxBindDto.setLiaoXiangNo(pickOrderDetailDto.getContainerNo());
        if (pickOrderDetailDto.getXkStoreId()!=null){
            pickLxBindDto.setXkKuCunId(pickOrderDetailDto.getXkStoreId());
        }else {
            pickLxBindDto.setZtKuCunId(pickOrderDetailDto.getZtStoreId());
        }
        pickLxBindDto.setOrderMxId(pickOrderDetailDto.getOrderMxId());
        pickLxBindDto.setPlanNum(pickOrderDetailDto.getBindingNum());
        pickLxBindDto.setFinish(pickOrderDetailDto.getIsFinish());
        return pickLxBindDto;
    }

    PickOrderMxDto getPickOrderMxDtos(PickOrderDetailDto pickOrderDetailDto) throws Exception {
        PickOrderMxDto pickOrderMxDto = new PickOrderMxDto();
        pickOrderMxDto.setCommodityName(pickOrderDetailDto.getGoodsName());
        pickOrderMxDto.setCommodityId(pickOrderDetailDto.getCommodityId());
        pickOrderMxDto.setBarCode(pickOrderDetailDto.getGoodsBarCode());
        pickOrderMxDto.setOrderMxId(pickOrderDetailDto.getOrderMxId());
        pickOrderMxDto.setPlanNum(pickOrderDetailDto.getBindingNum());
        pickOrderMxDto.setActualNum(pickOrderDetailDto.getActualNum());
        return pickOrderMxDto;
    }

    List<PickSpCountDto> getPickSpCountDtos(List<Integer> list) {
        List<PickSpCountDto> spCountDtoList = new ArrayList<>();
        for (Integer integer : list) {
            PickSpCountDto pickSpCountDto = new PickSpCountDto();
            List<Integer> commodityNums = containerSubService.findByMap(MapUtils.put("commodityId", integer).getMap());
            int x = 0;
            for (Integer count : commodityNums) {
                x = x+count;
            }
            pickSpCountDto.setCommodityId(integer);
            pickSpCountDto.setXkCount(x);
            spCountDtoList.add(pickSpCountDto);
        }
        return  spCountDtoList;
    }

}
