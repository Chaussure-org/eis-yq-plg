package com.prolog.eis.dao.repair;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.prolog.eis.model.repair.RepairPlanHis;
import com.prolog.framework.dao.mapper.BaseMapper;

/**
 * 补货计划(RepairPlan)表数据库访问层
 *
 * @author panteng
 * @since 2020-04-25 11:01:43
 */
public interface RepairPlanHisMapper extends BaseMapper<RepairPlanHis>{
	
	@Insert("insert into repair_plan_his(id, goods_id, operator, CREATE_TIME) select id, goods_id, operator, CREATE_TIME from repair_plan where id = #{id}")
	void backup(@Param("id") int id);
}