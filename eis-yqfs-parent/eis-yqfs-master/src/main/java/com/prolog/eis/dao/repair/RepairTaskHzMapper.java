package com.prolog.eis.dao.repair;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.prolog.eis.model.repair.RepairTaskHz;
import com.prolog.framework.dao.mapper.BaseMapper;

/**
 * 补货汇总(RepairTaskHz)表数据库访问层
 *
 * @author panteng
 * @since 2020-04-24 16:11:42
 */
public interface RepairTaskHzMapper extends BaseMapper<RepairTaskHz>{

	@Insert("insert into repair_task_hz_his (select hz.* from repair_task_hz hz where hz.id = #{hzId})")
	long repairTaskHzToHis(@Param("hzId")Integer hzId);

	
	@Insert("insert into repair_task_mx_his (select mx.* from repair_task_mx mx where mx.repair_task_hz_id = #{hzId} )")
	long repairTaskMxToHis(@Param("hzId")Integer hzId);

}