package com.prolog.eis.dao.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.prolog.eis.dto.outbound.LxBindingHzCheckAllArriveDto;
import com.prolog.eis.model.order.ContainerBindingHz;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface ContainerBindingHzMapper extends BaseMapper<ContainerBindingHz>{

    @Update("update container_binding_hz hz set hz.xk_store_id = null ,hz.zt_store_id = #{ztStoreId} where hz.container_no = #{containerNo}")
    long updateStore(@Param("ztStoreId")Integer ztStoreId, @Param("containerNo")String containerNo);

    @Select("SELECT odmx.id as mxId,\r\n" +
            "			      odmx.plan_num as planNum,\r\n" +
            "			      odmx.actual_num as actualNum,\r\n" +
            "			      a.bindCount \r\n" +
            "			      FROM order_hz odhz \r\n" +
            "			      INNER JOIN order_mx odmx ON odhz.id=odmx.outbound_task_hz_id\r\n" +
            "			      LEFT JOIN (SELECT bdmx.order_mx_id,SUM(bdmx.binding_num) as bindCount FROM container_binding_hz bdhz\r\n" +
            "			      INNER JOIN container_sub_binding_mx bdmx ON bdhz.container_no = bdmx.container_no\r\n" +
            "			      WHERE bdhz.xk_store_id is null\r\n" +
            "			      GROUP BY bdmx.order_mx_id) a ON odmx.id=a.order_mx_id\r\n" +
            "			      WHERE odhz.picking_order_id = #{pickingOrderId}")
    List<LxBindingHzCheckAllArriveDto> checkIsAllArrive(@Param("pickingOrderId")int pickingOrderId);


    @Select("select\r\n" +
            "	oth.id\r\n" +
            "from\r\n" +
            "	container_binding_hz cbh\r\n" +
            "left join order_hz oth on\r\n" +
            "	cbh.picking_order_id = oth.picking_order_id\r\n" +
            "where\r\n" +
            "	cbh.container_no =\r\n" +
            "	#{containerNo}")
    List<Integer> findOrderId(@Param("containerNo") String containerNo);

    @Select("select\r\n" +
            "	cm.order_hz_id\r\n" +
            "from\r\n" +
            "	container_binding_hz ch\r\n" +
            "left join container_sub_binding_mx cm on\r\n" +
            "	cm.container_no = ch.container_no\r\n" +
            "where\r\n" +
            "	ch.container_no =\r\n" +
            "	#{containerNo} order by cm.container_sub_no asc")
    List<Integer> findOrderIdAsc(@Param("containerNo")String containerNo);

    @Select("select\r\n" +
            " cm.order_hz_id\r\n" +
            "from\r\n" +
            "	container_binding_hz ch\r\n" +
            "left join container_sub_binding_mx cm on\r\n" +
            "	cm.container_no = ch.container_no\r\n" +
            "where\r\n" +
            "	ch.container_no =\r\n" +
            "	#{containerNo} order by cm.container_sub_no asc")
    List<Integer> findOrderIdDesc(@Param("containerNo")String containerNo);
}

