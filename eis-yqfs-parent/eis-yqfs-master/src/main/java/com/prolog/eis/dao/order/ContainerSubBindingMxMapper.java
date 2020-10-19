package com.prolog.eis.dao.order;

import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContainerSubBindingMxMapper extends BaseMapper<ContainerSubBindingMx>{

    @Select("SELECT cm.id as id,\r\n" +
            "cm.container_no as containerNo,\r\n" +
            "cm.container_sub_no as containerSubNo,\r\n" +
            "cm.order_hz_id as orderHzId,\r\n" +
            "cm.order_mx_id as orderMxId,\r\n" +
            "cm.binding_num as bindingNum,\r\n" +
            "cm.actual_num as actualNum,\r\n" +
            "cm.is_finish as isFinish,\r\n" +
            "cm.create_time as createTime,\r\n" +
            "cm.picking as picking\r\n" +
            "FROM container_sub_binding_mx cm\r\n" +
            "LEFT JOIN station_order_position sp on sp.order_id = cm.order_hz_id\r\n"+
            "where cm.container_sub_no = #{containerSubNo}\r\n" +
            "and cm.picking = 0 \r\n" +
            "and sp.is_change = 0 \r\n" +
            "ORDER BY sp.position_no asc")
    List<ContainerSubBindingMx> findMxAsc(@Param("containerSubNo")String containerSubNo);



}
