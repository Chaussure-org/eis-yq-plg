package com.prolog.eis.dao.orderboxline;

import com.prolog.eis.model.orderboxline.OrderboxLineTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单箱线任务表(OrderboxLineTask)表数据库访问层
 *
 * @author panteng
 * @since 2020-05-04 16:20:45
 */
public interface OrderboxLineTaskMapper extends BaseMapper<OrderboxLineTask>{

    /**
     *
     * @param tasks
     * @return
     */
//    @Insert("insert into orderbox_line_task(store_index,station_no,task_id,address, create_time, task_status, send_count)\n" +
//            " select b.taskId,b.address,b.taskStatus,b.sendCount,b.createTime from " +
//            " (<foreach collection = \"list\" separator = \"union all\" item = \"item\">\n" +
//            "   select \n" +
//            "              #{item.storeIndex}       as storeIndex,\n" +
//            "              #{item.stationNo}       as stationNo,\n" +
//            "              #{item.taskId}       as taskId,\n" +
//            "              #{item.address}     as address,\n" +
//            "              #{item.taskStatus}     as taskStatus,\n" +
//            "              #{item.sendCount}       as sendCount,\n" +
//            "              #{item.createTime}   as createTime\n" +
//            "         from dual </foreach>) b\n" +
//            " where NOT EXISTS (SELECT 1 FROM orderbox_line_task WHERE task_id = b.taskId)" )
//    long saveBatchNoExistAddress(List<OrderboxLineTask> tasks);

    /**
     * 查找需要重发的任务
     * @return
     */
    @Select("SELECT pso.task_id as taskId,pso.address as address,pso.create_time as createTime\n" +
            "from orderbox_line_task pso\n" +
            "where (pso.task_status = 0 or pso.task_status = 2) and pso.send_count < 20")
    List<OrderboxLineTask> findReSendTasks();
}