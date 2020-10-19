package com.prolog.eis.dao.pd;

import com.prolog.eis.model.pd.PdTaskDetail;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface PdTaskDetailMapper extends BaseMapper<PdTaskDetail> {


    @Update("UPDATE pd_task_detail SET modify_count = #{storeCount},task_state = 40 WHERE container_sub_no = #{containerSubNo}")
    long updateModifyCount(@Param("containerSubNo") String containerSubNo, @Param("storeCount") int storeCount);

    @Select("SELECT COUNT(1) FROM pd_task_detail WHERE pd_task_id = #{pdTaskId} AND task_state != 40")
    long pdCompleteTaskCount(@Param("pdTaskId") int pdTaskId);


    @Select("SELECT pd_task_id FROM pd_task_detail WHERE container_sub_no = #{containerSubOn} LIMIT 1")
    Integer getPdTaskId(@Param("containerSubOn") String containerSubOn);

    @Select("SELECT container_sub_no AS containerSubNo FROM pd_task_detail WHERE container_no = #{containerNo} AND task_state != 40")
    List<PdTaskDetail> pdCompleteContainerSubCount(@Param("containerNo") String containerNo);

    @Update("UPDATE pd_task_detail SET task_state = 10 WHERE pd_task_id = #{id}")
    long updatePdTaskDetailState(@Param("id") int id);

    @Update("UPDATE pd_task_detail SET task_state = 30 , pd_start_time = #{date} WHERE container_no = #{containerNo}")
    long updatePdTaskDetailStateUnderway(@Param("containerNo") String containerNo, @Param("date") Date date);

    @Select("SELECT\n" +
            "\tsx.id AS id,\n" +
            "\tcs.container_no AS containerNo,\n" +
            "\tcs.container_sub_no AS containerSubNo,\n" +
            "\tg.goods_no AS goodsNo,\n" +
            "\tg.goods_name AS goodsName,\n" +
            "\tcs.commodity_num AS commodityNum \n" +
            "FROM\n" +
            "\tgoods g\n" +
            "\tINNER JOIN container_sub cs ON g.id = cs.commodity_id\n" +
            "\tINNER JOIN sx_store sx ON sx.container_no = cs.container_no \n" +
            "WHERE\n" +
            "\tsx.store_state = 20 \n" +
            "\tAND sx.task_type = 0")
    List<PdTaskDetail> getPdTaskDetail();

}
