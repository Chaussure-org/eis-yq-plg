package com.prolog.eis.dao.repair;

import com.prolog.eis.dto.repair.RepairStoreDto;
import com.prolog.eis.model.repair.RepairPlanMx;
import com.prolog.framework.dao.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 补货计划明细(RepairPlanMx)表数据库访问层
 *
 * @author panteng
 * @since 2020-04-25 11:02:08
 */
public interface RepairPlanMxMapper extends BaseMapper<RepairPlanMx>{

    /**
     * 补货完成
     * @param containerNo
     */
    @Update("update repair_plan_mx t set t.repair_status = 2 where t.container_no = #{containerNo}")
    void updateByContainerNo(@Param("containerNo") String containerNo);

	/**
	 * 确认一个货格完成的时候 此货格的状态是已经完成的状态
	 * @param containerSubNo
	 */
	@Update("UPDATE repair_plan_mx t SET t.repair_status=3 WHERE t.container_sub_no=#{containerSubNo};")
	void  updateBycontainerSubNo(@Param("containerSubNo")String containerSubNo);

    @Select("SELECT\n" + 
    		"	t.container_no AS containerNo,\n" + 
    		"	c.layout_type AS recomType,\n" + 
    		"	g.goods_no AS goodsNo,\n" + 
    		"	g.goods_name AS goodsName,\n" + 
    		"	t.container_sub_no AS containerSubNo,\n" + 
    		"	cs.commodity_num AS storeCount,\n" + 
    		"	cs.reference_num - cs.commodity_num AS repairNumber,\n" +
    		"	s.STORE_STATE AS storeState \n" + 
    		"FROM\n" + 
    		"	repair_plan_mx t\n" + 
    		"	LEFT JOIN repair_plan p ON t.repair_plan_id = p.id\n" + 
    		"	LEFT JOIN container c ON t.container_no = c.container_no\n" + 
    		"	LEFT JOIN container_sub cs ON t.container_sub_no = cs.container_sub_no\n" + 
    		"	LEFT JOIN sx_store s ON t.container_no = s.CONTAINER_NO\n" + 
    		"	LEFT JOIN goods g ON p.goods_id = g.id \n" + 
    		"WHERE\n" + 
    		"	t.repair_plan_id = #{repairPlanId}")
	List<RepairStoreDto> getRepairPlanMxByPlanId(@Param("repairPlanId") int repairPlanId);
}