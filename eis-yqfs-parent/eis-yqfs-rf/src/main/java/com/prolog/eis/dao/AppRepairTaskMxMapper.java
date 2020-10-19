package com.prolog.eis.dao;

import com.prolog.eis.model.apprf.AppContainerSubModel;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

public interface AppRepairTaskMxMapper extends BaseMapper<AppContainerSubModel> {
    @Select("select t1.repair_plan_id, t1.container_no,t1.container_sub_no,t6.goods_id,t3.goods_no,t4.bar_code,t3.goods_name,t5.expiry_date,t1.repair_status,t2.layout_type from repair_plan_mx t1 \n" +
            "          JOIN repair_plan t6 on t6.id = t1.repair_plan_id\n" +
            "          inner join container t2 on t2.container_no=t1.container_no \n" +
            "            inner join container_sub t5 on t5.container_sub_no=t1.container_sub_no \n" +
            "            inner join goods t3 on t3.id=t6.goods_id \n" +
            "            inner join goods_bar_code t4 on t4.goods_id=t6.goods_id \n" +
            "            where t1.container_no=#{containerNo} ")
    public List<HashMap<String,Object>> query(@Param("containerNo") String containerNo);

    @Select("update repair_station set curr_work_container_no = #{containerNo}")
    public void updateStation(@Param("containerNo")String containerNo);
}
