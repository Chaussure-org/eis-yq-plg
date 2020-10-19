package com.prolog.eis.dao;

import com.prolog.eis.model.apprf.AppOrderMxModel;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * @author jinxf
 */
public interface AppOrderMxMapper extends BaseMapper<AppOrderMxModel> {

//	@Select("SELECT \n" +
//			"  (@sort := @sort + 1) sort,\n" +
//			"  m.order_hz_id outboundTaskHzId,\n" +
//			"  item.`goods_name` goodsName,\n" +
//			"  item.`goods_no` goodsNo,\n" +
//			"  m.actual_num actualNum\t\t\n" +
//			"FROM order_hz h\n" +
//			"INNER JOIN   order_mx m ON h.`picking_order_id` = m.`order_hz_id` \n" +
//			"  LEFT JOIN goods item \n" +
//			"    ON m.`goods_id` = item.`id`\n" +
//			"  LEFT JOIN \n" +
//			"    (SELECT \n" +
//			"      @sort := 0) AS sort \n" +
//			"    ON 1 = 1 \n" +
//			"WHERE h.`outbound_code` = #{outboundCode}\n" +
//			"  LIMIT #{page}, #{pageSize} ")
//@Select("select bhh.bill_no as orderNo, g.goods_name as goodsName ,g.goods_no as goodsNo ,omh.commodity_num as count \n" +
//		"from order_box_hz_history ohh \n" +
//		"LEFT JOIN order_box_mx_history omh on omh.order_box_no = ohh.order_box_no \n" +
//		"LEFT JOIN bill_hz_history bhh on bhh.order_hz_id = ohh.order_hz_id \n" +
//		"left join goods g on g.id = omh.commodity_id \n" +
//		"WHERE omh.order_box_no = #{orderBoxNo} \n" +
//		"LIMIT #{page}, #{pageSize}")
//	List<HashMap<String, Object>> query(@Param("orderBoxNo") String code, @Param("page") Integer page, @Param("pageSize") Integer pageSize);
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
			"\tsi.order_box_no AS orderBoxNo,\n" +
			"\tmh.plan_num AS planNum \n" +
			"FROM\n" +
			"\torder_mx_history mh\n" +
			"\tLEFT JOIN seed_info si ON mh.id = si.order_mx_id\n" +
			"\tJOIN goods g ON g.id = mh.goods_id \n" +
			"WHERE\n" +
			"\tsi.order_id = ( SELECT order_id FROM seed_info WHERE order_box_no = #{orderBoxNo} ORDER BY id DESC LIMIT 1 ) \n" +
			"\tAND si.order_box_no = #{orderBoxNo} \n")
	List<HashMap<String, Object>> query(@Param("orderBoxNo") String code);
}
	