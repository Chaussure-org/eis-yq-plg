package com.prolog.eis.dao.yqfs;

import java.util.List;

import com.prolog.eis.model.yqfs.OutboundWarn;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.yqfs.GoodsOutboundWarnDto;

public interface OutboundWarnMapper extends BaseMapper<OutboundWarn>{
	
	//删除前30天记录数据
	@Delete("delete from outbound_warn where  last_update_time < DATE_SUB(now(),INTERVAL 1 MONTH)")
	void deleteHistoryData();
	
	@Select("SELECT\n" +
			"\t\t\tg.id AS goodsId,\n" +
			"\t\t\to.owner_name AS ownerName,\n" +
			"\t\t\tgoods_name AS goodsName,\n" +
			"\t\t\tcs.commodity_num AS kuCunCount,\n" +
			"\t\t\t( SELECT GROUP_CONCAT( gbc.bar_code ) FROM goods_bar_code gbc WHERE gbc.goods_id = cs.commodity_id ) AS barCode \n" +
			"\t\t\tFROM\n" +
			"\t\t\tsx_store st\n" +
			"\t\t\tINNER JOIN container_sub cs ON st.container_no=cs.container_no\n" +
			"\t\t\tINNER JOIN goods g ON g.id = cs.commodity_id\n" +
			"\t\t\tINNER JOIN OWNER o ON o.id = g.owner_id")
	List<GoodsOutboundWarnDto> getSrtoreCount();

	@Select("SELECT\n" +
			"\t\t\tg.id AS goodsId,\n" +
			"\t\t\to.owner_name AS ownerName,\n" +
			"\t\t\tgoods_name AS goodsName,\n" +
			"\t\t\tcs.commodity_num AS kuCunCount,\n" +
			"\t\t\t( SELECT GROUP_CONCAT( gbc.bar_code ) FROM goods_bar_code gbc WHERE gbc.goods_id = cs.commodity_id ) AS barCode \n" +
			"\t\t\tFROM\n" +
			"\t\t\tsx_store st\n" +
			"\t\t\tINNER JOIN container_sub cs ON st.container_no=cs.container_no\n" +
			"\t\t\tINNER JOIN goods g ON g.id = cs.commodity_id\n" +
			"\t\t\tINNER JOIN OWNER o ON o.id = g.owner_id\n" +
			"\t\t\tJOIN sx_store_location sl ON st.store_location_id = sl.id\n" +
			"\t\t\tJOIN sx_store_location_group sg ON sg.id = sl.store_location_group_id\n" +
			"\t\t\tWHERE st.task_type=0 and st.store_state=20 and sg.is_lock=0 AND sg.ascent_lock_state=0 and st.store_location_id not in (\n" +
			"\t\t\tselect sr.location_child_id FROM sx_store_location_relation sr where sr.location_lock=1)")
	List<GoodsOutboundWarnDto> getAbleSrtoreCount();

	@Select("SELECT\n" +
			"\tg.id AS goodsId,\n" +
			"\to.owner_name AS ownerName,\n" +
			"\tgoods_name AS goodsName,\n" +
			"\t( SELECT GROUP_CONCAT( gbc.bar_code ) FROM goods_bar_code gbc WHERE gbc.goods_id = g.id ) AS barCode \n" +
			"FROM goods g INNER JOIN OWNER o ON o.id = g.owner_id \n" +
			" WHERE\n" +
			"\tg.id =#{commodityId}")
	List<GoodsOutboundWarnDto> getCIdDto(@Param("commodityId")int commodityId);

	@Select("SELECT c.commodity_id as goodsId , SUM(c.commodity_num) as kucunSum from container_sub_info c GROUP BY c.commodity_id")
	List<GoodsOutboundWarnDto> getAllGoodsKucun() ;

	@Select("SELECT\n" +
			"\tcs.commodity_id AS goodsId,\n" +
			"\tsum( cm.binding_num ) AS orderNeedCount \n" +
			"FROM\n" +
			"\tcontainer_sub_binding_mx cm  JOIN\n" +
			"\tcontainer_sub cs\n" +
			"\tON cm.container_sub_no=cs.container_sub_no\n" +
			"GROUP BY\n" +
			"\tcs.commodity_id")
	List<GoodsOutboundWarnDto> getOrderCount();


	@Select("SELECT\n" +
			"\tg.id AS goodsId,\n" +
			"\to.owner_name AS ownerName,\n" +
			"\tgoods_name AS goodsName,\n" +
			"\tcs.commodity_num AS kuCunCount,\n" +
			"\t( SELECT GROUP_CONCAT( gbc.bar_code ) FROM goods_bar_code gbc WHERE gbc.goods_id = cs.commodity_id ) AS barCode \n" +
			"FROM\n" +
			"\tzt_store zs\n" +
			"\tINNER JOIN container_sub cs ON zs.container_no = cs.container_no\n" +
			"\tINNER JOIN goods g ON g.id = cs.commodity_id\n" +
			"\tINNER JOIN OWNER o ON o.id = g.owner_id;")
	List<GoodsOutboundWarnDto> getZtStoreCount();


	/**
	 * 获取指定天数的历史监控数据
	 * @param days
	 * @return
	 */
	@Select(" select t.goods_id as goodsId, sum(t.EXPECT_OUTNUM) as expectOutnum\n" +
			"  from OUTBOUND_WARN t\n" +
			" where t.LAST_UPDATE_TIME between (curdate() - #{days}) and (curdate() - 1)\n" +
			" group by t.goods_id")
	List<OutboundWarn> getHistoryDataByDay(int days);

	@Select("SELECT\n" +
			"\tg.id AS goodsId,\n" +
			"\tg.goods_name AS goodsName,\n" +
			"\tcs.commodity_num AS kuCunCount \n" +
			"FROM\n" +
			"\tsx_store s\n" +
			"\tINNER JOIN container_sub cs ON s.container_no = cs.container_no\n" +
			"\tINNER JOIN goods g ON g.id = cs.commodity_id\n" +
			"\tJOIN sx_store_location sl ON s.store_location_id = sl.id \n" +
			"\tJOIN sx_store_location_group slg ON slg.id = sl.store_location_group_id \n" +
			"WHERE\n" +
			"\ts.task_type = 0 \n" +
			"\tAND s.store_state = 20 \n" +
			"\tAND sl.is_exception = 0\n" +
			"\tAND slg.is_lock = 0\n" +
			"UNION ALL\n" +
			"SELECT\n" +
			"\tg.id AS goodsId,\n" +
			"\tg.goods_name AS goodsName,\n" +
			"\tcs.commodity_num AS kuCunCount \t\n" +
			"FROM\n" +
			"\tzt_store zs\n" +
			"\tINNER JOIN container_sub cs ON cs.container_no = zs.container_no\n" +
			"\tINNER JOIN goods g ON g.id = cs.commodity_id")
	List<GoodsOutboundWarnDto> getSpCount();
}
