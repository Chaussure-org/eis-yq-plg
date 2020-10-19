package com.prolog.eis.dao.order;

import com.prolog.eis.dto.outbound.YkBalingBox;
import com.prolog.eis.model.order.BillFailing;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/8/5 19:36
 */
public interface BillFailingMapper extends BaseMapper<BillFailing> {

    @Select("SELECT\n" +
            "\tyk.bill_no AS billNo,\n" +
            "\tg.goods_name AS goodsName,\n" +
            "\tg.goods_no AS goodsNo,\n" +
            "\tyk.commodity_num AS commodityNum,\n" +
            "\tyk.order_box_no AS orderBoxNo,\n" +
            "\tyk.baling_box_no AS balingBoxNo,\n" +
            "\tyk.create_time AS createTime \n" +
            "FROM\n" +
            "\tyk_order_box yk\n" +
            "\tJOIN goods g ON g.id = yk.goods_id")
    List<YkBalingBox> getYkBalingBox();



}
