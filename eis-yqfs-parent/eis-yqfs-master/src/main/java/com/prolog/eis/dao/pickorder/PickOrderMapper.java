package com.prolog.eis.dao.pickorder;

import com.prolog.eis.dto.pickorder.PickOrderDetailDto;
import com.prolog.eis.pickstation.model.PickOrder;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PickOrderMapper extends BaseMapper<PickOrder> {

    @Select("select exists(\n" +
            "          select * from \n" +
            "          (\n" +
            "          select (a.plan_num-a.out_num) as ct from order_mx a\n" +
            "          where a.order_hz_id in (select id from order_hz where picking_order_id = #{id}\n" +
            "          )\n" +
            ") c where c.ct>0 )")
    boolean isNotAllArrived(@Param("id") int id);

    @Select("select c.container_no AS containerNo,\n" +
            "\tc.xk_store_id AS xkStoreId,\n" +
            "\tc.zt_store_id AS ztStoreId,\n" +
            "\tcm.order_mx_id AS orderMxId,\n" +
            "\tcm.is_finish AS isFinish,\n" +
            "\tcm.binding_num AS bindingNum,\n" +
            "\tcm.actual_num AS actualNum,\n" +
            "\t(select cs.commodity_id from container_sub cs WHERE cs.container_sub_no = cm.container_sub_no) as commodityId,\n" +
            "\t(select g.goods_name from goods g WHERE commodityId = g.id) as goodsName,\n" +
            "\t(select gbc.bar_code from goods_bar_code gbc WHERE gbc.goods_id = commodityId LIMIT 1) as goodsBarCode\n" +
            "from container_binding_hz c\n" +
            "right join container_sub_binding_mx cm ON cm.container_no = c.container_no\n" +
            "where c.picking_order_id=#{pickOrderId}")
    List<PickOrderDetailDto> getPickOrderInfo(@Param("pickOrderId") int pickOrderId);
}
