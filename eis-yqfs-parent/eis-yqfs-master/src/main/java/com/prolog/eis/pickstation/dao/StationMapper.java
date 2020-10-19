package com.prolog.eis.pickstation.dao;

import com.prolog.eis.dto.hxdispatch.HxStationDto;
import com.prolog.eis.dto.pickorder.SeedTaskDto;
import com.prolog.eis.dto.station.StationsDto;
import com.prolog.eis.dto.yqfs.StationDto;
import com.prolog.eis.model.order.OrderMx;
import com.prolog.eis.pickstation.dto.SeedContainerSubDto;
import com.prolog.eis.pickstation.dto.SeedNumDto;
import com.prolog.eis.pickstation.dto.SeedOrderBoxDto;
import com.prolog.eis.pickstation.model.Station;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StationMapper extends BaseMapper<Station>{
    @Select("SELECT\n" +
            "\tt.id,\n" +
            "\tt.current_lx_no AS currentLxNo,\n" +
            "\tt.station_no AS stationNo,\n" +
            "\tt.current_station_pick_id AS currentStationPickId,\n" +
            "\tT.lx_max_count AS lxMaxCount,\n" +
            "\tt.is_claim AS isClaim,\n" +
            "\tt.is_lock AS isLock,\n" +
            "\tt.station_task_type AS stationTaskType,\n" +
            "\tt.create_time AS createTime,\n" +
            "\tt.update_time AS updateTime,\n" +
            "\tt.login_user_id AS loginUserid,\n" +
            "\tt.login_username AS loginUsername,\n" +
            "\tt.ip_address AS ipAddress \n" +
            "FROM\n" +
            "\tstation t;")
    List<StationDto> getAllStation();

    /**
     * 当前合箱作业站台集合
     * @return
     */
    @Select("select s.id AS zhanTaiId,\n" +
            "                   s.is_claim AS isClaim,\n" +
            "                   s.lx_max_count AS maxLxCacheCount,\n" +
            "                   (select count(1) from zt_store zs where zs.station_id = s.id and zs.status = 1) as arriveLxCount,\n" +
            "                   (select count(1) from sx_store ss where ss.station_id = s.id and ss.store_state in (30, 31)) as chuKuLxCount\n" +
            "            from station s\n" +
            "            left join zt_store zs on zs.station_id = s.id\n" +
            "            where s.station_task_type = 30 and s.is_lock = 2")
    List<HxStationDto> getHxStations();

    @Select("			SELECT\r\n" +
            "				t.id,\r\n" +
            	"				t.station_no AS stationNo,\r\n" +
            //	"				h.HOISTER_NO AS hoisterNo,\r\n" +
            "				t.current_lx_no AS currentLxNo,\r\n" +
            "				t.current_station_pick_id AS currentStationPickId,\r\n" +
            "				T.lx_max_count AS lxMaxCount,\r\n" +
            "				t.is_claim AS isClaim,\r\n" +
            "				t.is_lock AS isLock,\r\n" +
            "				t.station_task_type AS stationTaskType,\r\n" +
            "				t.create_time AS createTime,\r\n" +
            "				t.update_time AS updateTime,\r\n" +
            "				t.login_user_id AS loginUserid,\r\n" +
            "				t.login_username AS loginUsername ,\r\n" +
            "				t.ip_address as ipAddress\r\n" +
            "			FROM\r\n" +
            "				station t\r\n" +
            //"				LEFT JOIN sx_hoister h ON t.hoister_id = h.id " +
            "				where t.ip_address = #{ipLocation}")
    StationDto getstationwithportbyips(@Param("ipLocation")String ipLocation);


    @Update("update station s\r\n" +
            "set s.current_lx_no = null\r\n" +
            "where s.id = #{stationId}")
    public void updateStationLxToEmpty(@Param("stationId")int stationId);

    @Select("SELECT\n" +
            "\tcs.container_sub_no AS containerSubNo,\n" +
            "cs.container_no AS containerNo,\n" +
            "c.layout_type AS layoutType,"+
            "cs.commodity_id AS commodityId,\n" +
            "cs.commodity_num AS commodityNo,\n" +
            "csbm.order_hz_id AS orderId,"+
            "csb.commodity_num AS subNum,"+
            "gd.goods_name AS goodsName,\n" +
            "(\n" +
            "\tSELECT\n" +
            "\t\tgroup_concat( `gbc`.`bar_code` SEPARATOR ',' ) \n" +
            "\tFROM\n" +
            "\t\t`goods_bar_code` `gbc` \n" +
            "\tWHERE\n" +
            "\t( `gbc`.`goods_id` = `cs`.`commodity_id` )) AS `barCode`,\n" +
            "\tcsbm.binding_num AS bindingNum,\n" +
            "csbm.actual_num AS actualNum,"+
            "\tcsbm.is_finish AS isFinish,\n" +
            "\tcsbm.picking AS picking\n" +
            "FROM\n" +
            "\tcontainer_sub cs\n" +
            "LEFT JOIN container c on cs.container_no=c.container_no"+
            "\tLEFT JOIN goods gd ON cs.commodity_id = gd.id\n" +
            "\tLEFT JOIN container_sub csb ON cs.container_sub_no = csb.container_sub_no\n" +
            "\tLEFT JOIN container_sub_binding_mx csbm ON cs.container_sub_no = csbm.container_sub_no \n" +
            "WHERE\n" +
            "\tcs.container_no IN ( SELECT slp.container_no FROM station_lx_position slp WHERE slp.station_id = #{stationId} and slp.status=20)")
    List<SeedContainerSubDto> getStationSeedinfo(@Param("stationId")int StationId);


    @Select("SELECT\n" +
            "\tsop.container_no AS containerNo,\n" +
            "sop.position_no AS positionNo,"+
            "IF\n" +
            "\t( hz.order_box_no IS NOT NULL, 1, 0 ) AS isCurrent,\n" +
            "\thz.id AS orderId ,\n" +
            "IF(hz.sto_no IS NULL,'销售','移库') AS type "+
            "FROM\n" +
            "\tstation_order_position sop LEFT\n" +
            "\tJOIN order_hz hz ON sop.order_id = hz.id \n" +
            "WHERE\n" +
            "\tsop.station_id = #{stationId}")
    List<SeedOrderBoxDto> getSeedOrderBoxInfo(@Param("stationId")int stationId);


    @Select("SELECT\n" +
            "\tobz.order_hz_id AS hzId,\n" +
            "\tobz.order_box_no AS orderBoxNo,\n" +
            "\t( SELECT SUM( omx.commodity_num ) FROM order_box_mx omx WHERE omx.order_box_no = obz.order_box_no ) AS boxNum \n" +
            "FROM\n" +
            "\torder_box_hz obz\n" +
            "\tLEFT JOIN order_hz hz ON obz.order_hz_id = hz.id \n" +
            "WHERE\n" +
            "\tFIND_IN_SET(\n" +
            "\thz.id,\n" +
            "\t#{strs})")
    List<SeedNumDto>getSeedNum(@Param("strs")String strs);

    @Select("\tSELECT * from\n" +
            "\torder_mx mx WHERE FIND_IN_SET(\n" +
            "mx.order_hz_id,\n" +
            "\t#{strs})")
    List<OrderMx>getSeedMx(@Param("strs")String strs);


    @Select("select s.station_no as stationNo from station_lx_position slp join station s on s.id = slp.station_id where slp.container_no = #{containerNo}")
    List<String> getStationNo(@Param("containerNo") String containerNo);

    @Select("select s.id as stationId,s.station_no as stationNo,s.station_task_type as stationType from station s")
    List<StationsDto> getAllStations();

    List<SeedTaskDto> getSeedTask(@Param("containerNo") String containerNo, @Param("goodsNo") String goodsNo, @Param("goodsName") String goodsName);
}
