package com.prolog.eis.dao.order;

import com.prolog.eis.dto.yqfs.PickContainerSubBindingDto;
import com.prolog.eis.dto.yqfs.PickOrderMxDto;
import com.prolog.eis.dto.yqfs.PickSpCountDto;
import com.prolog.eis.model.order.OrderMx;
import com.prolog.eis.util.mapper.EisBaseMapper;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author panteng
 * @description:
 * @date 2020/4/19 16:14
 */
public interface OrderMxMapper extends EisBaseMapper<OrderMx> {
    //查询拣选单订单集合
    @Select("SELECT g.id as goodsId , g.goods_name as goodsName ,\r\n" +
            "o.plan_num as planNum , o.actual_num as actualNum , o.order_hz_id as orderMxId,\r\n" +
            "(SELECT GROUP_CONCAT(gbc.bar_code) from goods_bar_code gbc where gbc.goods_id = o.goods_id) as barCode\r\n" +
            " FROM order_mx o\r\n" +
            "	INNER JOIN goods g on o.goods_id = g.id\r\n" +
            "	INNER JOIN (SELECT * FROM order_hz hz WHERE hz.picking_order_id = #{pickId})h on o.order_hz_id = h.id ")
    List<PickOrderMxDto> findOrderMxByPick(int pickId);

    //拣选单商品箱库库存集合
    @Select("SELECT  s.commodity_id as goodsId , s.commodity_num as xkCount\r\n" +
            "	FROM container_sub_info s \r\n" +
            "	INNER JOIN \r\n" +
            "	(SELECT DISTINCT(mx.goods_id) FROM order_mx mx\r\n" +
            "	WHERE mx.order_hz_id in (SELECT hz.id FROM order_hz hz WHERE hz.picking_order_id = #{pickId})) t\r\n" +
            "	on t.goods_id = s.commodity_id")
    List<PickSpCountDto> findPickSpCountByPickId(int pickId);


    //拣选单绑定集合
    @Select("SELECT hz.xk_store_id as xkKuCunId , hz.zt_store_id as ztKuCunId ,\r\n" +
            "hz.container_no as liaoXiangNo , mx.order_mx_id as orderMxId ,\r\n" +
            "mx.binding_num as planNum , mx.is_finish as isFinish FROM container_sub_binding_mx mx \r\n" +
            "INNER JOIN container_binding_hz hz on mx.container_no = hz.container_no\r\n" +
            "WHERE hz.picking_order_id = #{pickId}")
    List<PickContainerSubBindingDto> findByPickId(int pickId);
}

