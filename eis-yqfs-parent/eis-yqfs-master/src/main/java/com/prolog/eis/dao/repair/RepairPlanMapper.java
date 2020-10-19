package com.prolog.eis.dao.repair;

import com.prolog.eis.dto.repair.RepairStoreDto;
import com.prolog.eis.model.repair.RepairPlan;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 补货计划(RepairPlan)表数据库访问层
 *
 * @author panteng
 * @since 2020-04-25 11:01:43
 */
public interface RepairPlanMapper extends BaseMapper<RepairPlan>{

	@Select("SELECT\n" +
			"\ts.CONTAINER_NO AS containerNo,\n" +
			"\tc.layout_type AS recomType,\n" +
			"\tg.goods_no AS goodsNo,\n" +
			"\tg.goods_name AS goodsName,\n" +
			"\tcs.container_sub_no AS containerSubNo,\n" +
			"\tcs.commodity_num AS storeCount,\n" +
			"\tg.recom_number - cs.commodity_num AS repairNumber,\n" +
			"\ts.STORE_STATE AS storeState,\n" +
			"\tcs.reference_num AS upLimit,\n" +
			"\tss.layer AS layer,\n" +
			"\tss.x AS x,\n" +
			"\tss.y AS y \n" +
			"FROM\n" +
			"\tsx_store s\n" +
			"\tLEFT JOIN container c ON s.CONTAINER_NO = c.container_no\n" +
			"\tLEFT JOIN container_sub cs ON c.container_no = cs.container_no\n" +
			"\tLEFT JOIN goods g ON cs.commodity_id = g.id\n" +
			"\tLEFT JOIN sx_store_location ss ON ss.id = s.store_location_id\n" +
			"\tLEFT JOIN sx_store_location_group lg ON ss.store_location_group_id = lg.id \n" +
			"WHERE\n" +
			"\tg.goods_no = #{goodsNo} \n" +
			"\tAND ISNULL( s.task_id ) \n" +
			"\tAND lg.ascent_lock_state = 0 \n" +
			"\tAND lg.is_lock = 0 \n" +
			"\tAND s.container_no NOT IN ( SELECT mx.container_no FROM repair_plan_mx mx WHERE mx.repair_status != 3 ) \n" +
			"ORDER BY\n" +
			"\ts.CONTAINER_NO;")
	List<RepairStoreDto> getAllStoreByGoodsNo(@Param("goodsNo") String goodsNo);

}