package com.prolog.eis.dao.repair;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.prolog.eis.model.repair.RepairPlanMxHis;
import com.prolog.framework.dao.mapper.BaseMapper;

/**
 * 补货计划明细(RepairPlanMx)表数据库访问层
 *
 * @author panteng
 * @since 2020-04-25 11:02:08
 */
public interface RepairPlanMxHisMapper extends BaseMapper<RepairPlanMxHis>{
	@Insert("insert into repair_plan_mx_his(id, repair_plan_id, CONTAINER_NO, repair_status," +
			" create_time, container_sub_no) select id, repair_plan_id, CONTAINER_NO, repair_status, create_time," +
			" container_sub_no from repair_plan_mx where id in (${ids})")
	void backup(@Param("ids") String ids);


	@Insert("insert into repair_plan_mx_his(id, repair_plan_id, CONTAINER_NO, repair_status," +
			" create_time, container_sub_no) select id, repair_plan_id, CONTAINER_NO, repair_status, create_time," +
			" container_sub_no from repair_plan_mx where repair_plan_id =#{id}")
	void mxIntHis(@Param("id") int id);
}