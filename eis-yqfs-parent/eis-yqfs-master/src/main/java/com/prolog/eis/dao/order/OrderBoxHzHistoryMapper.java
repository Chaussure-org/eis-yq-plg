package com.prolog.eis.dao.order;

import com.prolog.eis.dto.outbound.OutBoundOrderBoxDto;
import com.prolog.eis.model.order.OrderBoxHzHistory;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author wangkang
 * @Description 数据层
 * @CreateTime 2020/7/4 12:13
 */
public interface OrderBoxHzHistoryMapper extends BaseMapper<OrderBoxHzHistory> {

    /**
     * 订单箱出库标签信息
     */
//    @Select("SELECT\n" +
//            "            bhh.wave_no AS bcNo,\n" +
//            "            ohh.order_box_no AS orderBoxNo,\n" +
//            "            d.id AS supplierNo,\n" +
//            "            dealer_name AS supplierName,\n" +
//            "            bhh.place AS area\n" +
//            "            FROM\n" +
//            "            order_box_hz_history ohh\n" +
//            "            LEFT JOIN bill_hz_history bhh ON ohh.order_hz_id = bhh.order_hz_id\n" +
//            "            LEFT JOIN dealer d ON d.id = bhh.dealer_id\n" +
//            "            WHERE\n" +
//            "            ohh.order_box_no = #{orderBoxNo}")
//    List<OutBoundOrderBoxDto> findOutBoundOrderBoxInfo(@Param("orderBoxNo") String orderBoxNo);
    @Select("SELECT\n" +
            "\tbhh.wave_no AS bcNo,\n" +
            "\tsi.order_box_no AS orderBoxNo,\n" +
            "\td.id AS supplierNo,\n" +
            "\td.dealer_name AS supplierName,\n" +
            "\tbhh.place AS area \n" +
            "FROM\n" +
            "\tbill_hz_history bhh\n" +
            "\tINNER JOIN dealer d ON bhh.dealer_id = d.id\n" +
            "\tJOIN seed_info si ON si.order_id = bhh.order_hz_id \n" +
            "WHERE\n" +
            "  \tsi.order_id = ( SELECT order_id FROM seed_info WHERE order_box_no = #{orderBoxNo} ORDER BY id DESC LIMIT 1 )")
    List<OutBoundOrderBoxDto> findOutBoundOrderBoxInfo(@Param("orderBoxNo") String orderBoxNo);
}
