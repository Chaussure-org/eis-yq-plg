package com.prolog.eis.service.order;

import com.prolog.eis.dto.outbound.OutBoundTaskHzDto;
import com.prolog.eis.model.order.OrderHz;
import com.prolog.framework.common.message.RestMessage;

import java.util.List;
import java.util.Map;
/**
 * @Author wangkang
 * @Description 订单汇总
 * @CreateTime 2020/6/20 11:55
 */
public interface IOrderHzService {
    OrderHz findById(Integer orderId);
    List<OrderHz> findByMap(Map map);
    void update(OrderHz orderHz);
    /**
     * 保存出库订单汇总明细
     */
    void saveOrderHzMx(OutBoundTaskHzDto outBoundTaskHzDtos) throws Exception;

    /**
     * 根据拣选单获取未绑定的订单
     * @param pickOrderId
     * @return
     */
    List<OrderHz> getUnbindingOrdersByPickOrderId(int pickOrderId);

    /**
     * 根据拣选单获取订单列表
     * @param pickOrderId
     * @return
     */
    List<OrderHz> getByPickOrderId(int pickOrderId);

    /**
     * 编辑订单优先级
     * spp
     * @param id 订单id
     * @param priority 优先级
     * @throws Exception
     */
    void updatePriority(int id,int priority) throws Exception;

    void save(OrderHz orderHz);

    RestMessage<String> createOrder() throws Exception;

    void delete(int[] ids) throws Exception;

    RestMessage<String> deleteBillMxHz() throws Exception;
}
