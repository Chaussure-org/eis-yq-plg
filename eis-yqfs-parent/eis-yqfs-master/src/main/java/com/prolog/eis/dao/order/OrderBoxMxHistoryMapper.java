package com.prolog.eis.dao.order;

import com.prolog.eis.dto.outbound.OrderBoxDto;
import com.prolog.eis.dto.outbound.OrderBoxMxDto;
import com.prolog.eis.model.order.OrderBoxMxHistory;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author wangkang
 * @Description 数据层
 * @CreateTime 2020/7/4 12:15
 */
public interface OrderBoxMxHistoryMapper extends BaseMapper<OrderBoxMxHistory> {

    /**
     * 查询出库订单箱详情
     */
//    @Select("SELECT\n" +
//            "\t(select GROUP_CONCAT(bhh.bill_no SEPARATOR ',') from bill_hz_history bhh join bill_mx_history bmh on bmh.bill_hz_id = bhh.id where bhh.order_hz_id = obh.order_hz_id and bmh.goods_id = obm.commodity_id ) as orderNo,\n" +
//            "\tg.goods_name as goodsName,\n" +
//            "\tg.goods_no as goodsNo,\n" +
//            "\tobm.commodity_num as count,\n" +
//            "\tobm.order_box_no as orderBoxNo\n" +
//            "\t\n" +
//            "FROM\n" +
//            "\torder_box_mx_history obm\n" +
//            "\tJOIN order_box_hz_history obh ON obm.order_box_no = obh.order_box_no\n" +
//            "\tjoin goods g on g.id = obm.commodity_id\n" +
//            "WHERE\n" +
//            "\tobm.order_box_no = #{orderBoxNo}")
//    List<OrderBoxMxDto> findOrderBoxInfo(@Param("orderBoxNo") String orderBoxNo);

    @Select("SELECT\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\tGROUP_CONCAT( bhh.bill_no SEPARATOR ',' ) \n" +
            "\tFROM\n" +
            "\t\tbill_hz_history bhh\n" +
            "\t\tJOIN bill_mx_history bmh ON bmh.bill_hz_id = bhh.id \n" +
            "\tWHERE\n" +
            "\t\tbhh.order_hz_id = si.order_id \n" +
            "\t\tAND bmh.goods_id = mh.goods_id \n" +
            "\t) AS orderNo,\n" +
            "\tg.goods_name AS goodsName,\n" +
            "\tg.goods_no AS goodsNo,\n" +
            "\tsi.num AS count,\n" +
            "\tsi.order_box_no AS orderBoxNo \n" +
            "FROM\n" +
            "\tseed_info si\n" +
            "\tLEFT JOIN order_mx_history mh ON mh.id = si.order_mx_id\n" +
            "\tJOIN goods g ON g.id = mh.goods_id \n" +
            "WHERE\n" +
            "\tsi.order_id = ( SELECT order_id FROM seed_info WHERE order_box_no = #{orderBoxNo} ORDER BY id DESC LIMIT 1 ) \n" +
            "\tAND order_box_no = #{orderBoxNo} ")
    List<OrderBoxMxDto> findOrderBoxInfo(@Param("orderBoxNo") String orderBoxNo);
}
