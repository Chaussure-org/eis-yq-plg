package com.prolog.eis.dao.order;

import com.prolog.eis.dto.orderpool.OpOrderHz;
import com.prolog.eis.model.order.OrderHz;
import com.prolog.framework.dao.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author panteng
 * @description: TODO
 * @date 2020/4/17 15:13
 */
public interface OrderHzMapper extends BaseMapper<OrderHz> {
    /**
     * 获取订单
     *
     * @param isAddPool
     * @return
     */
    @Select("        select\n" +
            "                id as  id,\n" +
            "                picking_order_id as  pickingOrderId,\n" +
            "                outbound_code as  outboundCode,\n" +
            "                expect_time as  expectTime,\n" +
            "                priority as  priority,\n" +
            "                order_box_no as  orderBoxNo,\n" +
            "                station_id as  stationId\n" +
            "        from order_hz \n" +
            "        where is_add_pool=#{isAddPool} and picking_order_id is null")
    List<OpOrderHz> getOrderToOrderPool(int isAddPool);

    /**
     * 将订单标记位已加入订单池
     * @param idsStr
     */
    @Update("update order_hz t set t.is_add_pool = 1,t.last_date_time = now()" +
            " where find_in_set(t.id,#{idsStr})")
    void updateOrder2Pool(String idsStr);

    @Select("select hz.id from order_hz hz where hz.id in (${idStr}) and hz.picking_order_id is not null")
    List<OrderHz> findOrderHzByIds(@Param("idStr")String idStr);

    @Update("update order_hz hz set hz.picking_order_id = #{pickingId},hz.station_id = #{zhanTaiId} where hz.id in (${idStr})")
    long updateBatchPicking(@Param("idStr")String idStr,@Param("zhanTaiId") int zhanTaiId, @Param("pickingId")int pickingId);
}

