package com.prolog.eis.pickstation.dao;

import com.prolog.eis.dto.hxdispatch.StationLxPositionHxDto;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StationLxMapper extends BaseMapper<StationLxPosition>{

    @Select("SELECT\n" +
            "\tslp.container_no \n" +
            "FROM\n" +
            "\tstation_lx_position slp\n" +
            "\tINNER JOIN station s ON slp.station_id = s.id \n" +
            "WHERE\n" +
            "\ts.station_task_type = 2 \n" +
            "\tAND slp.station_id = #{stationId}")
    List<String> getInitStationLxPosition(@Param("stationId") int stationId);

    @Select("select * from station_lx_position where status=0 and position_device_no not in (select target from wcs_task )")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "position_no",property = "positionNo"),
            @Result(column = "station_id",property = "stationId"),
            @Result(column = "position_device_no",property = "positionDeviceNo"),
            @Result(column = "status",property = "status"),
            @Result(column = "container_no",property = "containerNo"),
            @Result(column = "container_direction",property = "containerDirection"),
            @Result(column = "distribute_priority",property = "distributePriority"),
            @Result(column = "gmt_distribute_time",property = "gmtDistribTime"),
            @Result(column = "gmt_inplace_time",property = "gmtInplaceTime")
    })
    List<StationLxPosition> findEmptyPosition();
    /**
     * 得到料箱的编号，面，类型
     */
    @Select("select c.container_no as containerNo,c.layout_type as layoutType,slp.container_direction as containerDirection,slp.position_no as positionNo," +
            "slp.station_id as stationId from container c left join station_lx_position slp on c.container_no = slp.container_no where " +
            "c.container_no = #{containerNo}")
    List<StationLxPositionHxDto> getHxContainerInfo(@Param("containerNo") String containerNo);

    @Select("SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\tstation_lx_position s\n" +
            "\tJOIN container_sub cs ON cs.container_no = s.container_no \n" +
            "WHERE\n" +
            "\tcs.container_sub_no = #{containerSubNo}")
    int getStationIdByContainerSubNo(@Param("containerSubNo") String containerSubNo);
}
