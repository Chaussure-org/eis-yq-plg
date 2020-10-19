package com.prolog.eis.dao.store;

import com.prolog.eis.dto.enginee.SxStoreDto;
import com.prolog.eis.dto.store.*;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.SxStoreLocationGroup;
import com.prolog.eis.model.store.SxStoreLock;
import com.prolog.eis.model.store.SxStoreLocksDto;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.kafka.common.protocol.types.Field;

import java.util.List;

public interface StoreMapper extends BaseMapper<SxStore> {

    /**
     * spp
     * 每层的 出库任务数量 30 31 和入库任务数量 10
     *
     * @return
     */
    @Select("SELECT\n" +
            "\tsl.layer,\n" +
            "\tSUM( CASE s.store_state WHEN 10 THEN 1 ELSE 0 END ) outCount,\n" +
            "\tSUM( CASE s.store_state WHEN 30 THEN 1 WHEN 31 THEN 1 ELSE 0 END ) inCount \n" +
            "FROM\n" +
            "sx_layer ly LEFT JOIN\n" +
            "sx_store_location sl ON ly.layer=sl.layer\n" +
            "LEFT JOIN sx_store s ON sl.id=s.store_location_id\n" +
            "GROUP BY\n" +
            "\tsl.layer \n" +
            "ORDER BY\n" +
            "\tsl.Layer")
    List<LayerTaskCountDto> getLayerTaskCount();

    /**
     * 查询每层任务数分组排序（过滤已上架）
     *
     * @return
     */
    @Select("select t.*,t.taskCount/t.carCount as agvTaskCount from (select \r\n" +
            "	   sl.layer,\r\n" +
            "	   count(ss.id) as taskCount,\r\n" +
            "	   (select count(1) from sx_car sc where sc.layer = sl.layer and (sc.car_state = 1 or sc.car_state = 2)) as carCount\r\n" +
            "from sx_store ss\r\n" +
            "left join sx_store_location sl on sl.id = ss.store_location_id \r\n" +
            "where (ss.store_state = 1 or ss.store_state = 3) \r\n" +
            "group by sl.layer) t order by agvTaskCount asc")
    List<LayerTaskGroupSortDto> findLayerTaskGroupSort();

    /**
     * 查询锁定状态
     *
     * @param containerNo
     * @return
     */
    @Select("select\n" +
            " sl.ascent_lock_state as ascentLockStat,\n" +
            " sslp.ascent_lock_state as ascentGroupLockState,\n" +
            " sslp.is_lock as isLock,\n" +
            " sl.dept_num as deptNum\n" +
            "from\n" +
            " sx_store ss\n" +
            "left join sx_store_location sl on\n" +
            " sl.id = ss.store_location_id\n" +
            "left join sx_store_location_group sslp on\n" +
            " sl.store_location_group_id = sslp.id\n" +
            "where\n" +
            " ss.container_no =\n" +
            " #{containerNo}")
    SxStoreLock findSxStoreLock(@Param("containerNo") String containerNo);

    /**
     * 查询锁定状态
     *
     * @param containerNo
     * @return
     */
    @Select("select\n" +
            " sl.ascent_lock_state as ascentLockStat,\n" +
            " sslp.ascent_lock_state as ascentGroupLockState,\n" +
            " sslp.is_lock as isLock,\n" +
            " ss.container_no as containerNo,\n" +
            " sl.dept_num as deptNum\n" +
            "from\n" +
            " sx_store ss\n" +
            "left join sx_store_location sl on\n" +
            " sl.id = ss.store_location_id\n" +
            "left join sx_store_location_group sslp on\n" +
            " sl.store_location_group_id = sslp.id\n" +
            "where\n" +
            " ${containerSql}")
    List<SxStoreLocksDto> findSxStoreLocks(@Param("containerSql") String containerSql);

    @Select("select\r\n" +
            "	sl.ascent_lock_state as ascentLockState,\r\n" +
            "	sl.dept_num as deptNum,\r\n" +
            "	sl.layer,\r\n" +
            "	sl.location_index as locationIndex,\r\n" +
            "	sl.store_location_group_id as storeLocationGroupId,\r\n" +
            "	sl.store_location_id1 as storeLocationId1,\r\n" +
            "	sl.store_location_id2 as storeLocationId2,\r\n" +
            "	sl.x,\r\n" +
            "	sl.y,\r\n" +
            "	ss.container_no as containerNo,\r\n" +
            "	ss.task_property1 as taskProperty1,\r\n" +
            "	ss.task_property2 as taskProperty2,\r\n" +
            "	ss.store_state as storeState,\r\n" +
            "	ss.id as sotreId\r\n" +
            "from\r\n" +
            "	sx_store_location sl\r\n" +
            "left join sx_store ss on\r\n" +
            "	sl.id = ss.store_location_id\r\n" +
            "where\r\n" +
            "	sl.store_location_group_id = (\r\n" +
            "	select\r\n" +
            "		t.store_location_group_id\r\n" +
            "	from\r\n" +
            "		sx_store_location t\r\n" +
            "	left join sx_store ss on\r\n" +
            "		t.id = ss.store_location_id\r\n" +
            "	where\r\n" +
            "		ss.container_no = #{containerNo} )\r\n" +
            "	and ss.id != 0")
    public List<SxStoreGroupDto> findStoreGroup(@Param("containerNo") String containerNo);

    @Update("update sx_store t set t.task_type = #{state} where t.container_no = #{containerNo}")
    public void updateStoreState(@Param("containerNo") String containerNo, @Param("state") int state);

    @Delete("delete from sx_store")
    void deleteAll();

    @Update("update sx_store t set t.store_state = #{state} where t.id = #{containerNo}")
    public void updateContainerNoState(@Param("containerNo") String containerNo, @Param("state") int state);

    @Update("update sx_store t set t.store_state = 20 , t.HOISTER_NO = NULL , t.CAR_NO = NULL , t.TASK_ID = NULL, t.source_location_id = NULL   where t.CONTAINER_NO = #{containerNo}")
    public void updateContainerGround(@Param("containerNo") String containerNo);

    /**
     * 查找托盘剁
     *
     * @param layer
     * @return
     */
    @Select("select \n" +
            "s.ID as id,\n" +
            "s.CONTAINER_NO  as containerNo,\n" +
            "sl.x as x,\n" +
            "sl.y as y,\n" +
            "sl.layer as layer,\n" +
            "s.STORE_LOCATION_ID  as storeLocationId\n" +
            "from sx_store s \n" +
            "join sx_store_location sl on sl.id = s.STORE_LOCATION_ID" +
            " join sx_store_location_group sg on sl.STORE_LOCATION_GROUP_ID = sg.id\n" +
            " where s.TASK_TYPE = -1 and s.STORE_STATE = 20 and sl.dept_num = 0 and " +
            " sg.IS_LOCK = 0 and sg.ASCENT_LOCK_STATE = 0" +
            " and sl.layer = #{layer}")
    List<SplitOutDto> findTraychop(@Param("layer") int layer);

    @Delete("delete from sx_store sx where sx.container_no = #{containerNo}")
    void deleteByContainer(@Param("containerNo") String containerNo);

    @Select("select\r\n" +
            "	b.layer,\r\n" +
            "	b.belong_area as belongArea,\r\n" +
            "	a.taskCount,\r\n" +
            "	b.carNoCount,\r\n" +
            "	a.taskCount / b.carNoCount as avgCount,\r\n" +
            "	b.carNoCount * 2 as maxCarTask\r\n" +
            "from\r\n" +
            "	(\r\n" +
            "	select\r\n" +
            "		l.layer,\r\n" +
            "		g.belong_area as belongArea,\r\n" +
            "		count(mx.id) as taskCount\r\n" +
            "	from\r\n" +
            "		sx_store s\r\n" +
            "	left join sx_store_location l on\r\n" +
            "		s.store_location_id = l.id\r\n" +
            "	left join sx_store_location_group g on\r\n" +
            "		l.store_location_group_id = g.id\r\n" +
            "	left join sx_path_planning_task_hz hz on\r\n" +
            "		hz.container_no = s.container_no\r\n" +
            "		\r\n" +
            "	left join sx_path_planning_task_mx mx on\r\n" +
            "		hz.id = mx.task_hz_id\r\n" +
            "	where\r\n" +
            "		mx.transportation_equipment = 2\r\n" +
            "		and (mx.is_complete = 20\r\n" +
            "		or mx.is_complete = 30)\r\n" +
            "	group by\r\n" +
            "		g.belong_area,\r\n" +
            "		l.layer) a\r\n" +
            "right join (\r\n" +
            "	select\r\n" +
            "		sc.belong_area,\r\n" +
            "		sc.layer,\r\n" +
            "		count(sc.id) as carNoCount\r\n" +
            "	from\r\n" +
            "		sx_car sc\r\n" +
            "	where\r\n" +
            "		sc.car_state = 1\r\n" +
            "		or sc.car_state = 2\r\n" +
            "	group by\r\n" +
            "		sc.belong_area,\r\n" +
            "		sc.layer) b on\r\n" +
            "	a.belongArea = b.belong_area\r\n" +
            "	and a.layer = b.layer\r\n" +
            "order by\r\n" +
            "	avgCount asc")
    List<SxStoreDto> findAreaLaryGroup();

    @Update("update sx_store t set t.car_no = #{carNo} where t.container_no = #{containerNo}")
    void updateStoreCar(@Param("carNo") String carNo, @Param("containerNo") String containerNo);

    @Select("select slg.id ,slg.reserved_location as reservedLocation from sx_store_location_group slg inner join sx_store_location sl on sl.store_location_group_id = slg.ID\n" +
            "inner join sx_store sx  on sx.STORE_LOCATION_ID = sl.id where sx.id = #{storeId}")
    SxStoreLocationGroup findAreaTypeById(@Param("storeId") Integer storeId);

    @Select("select s.id,s.CONTAINER_NO as containerNo, s.CONTAINER_SUB_NO as containerSubNo,s.STORE_LOCATION_ID as storeLocationId ,\n" +
            "s.TASK_TYPE as taskType, s.TASK_PROPERTY1 as taskProperty1,s.TASK_PROPERTY2 as taskProperty2,\n" +
            "s.STORE_STATE as storeState\n" +
            "from sx_store s \n" +
            "inner join sx_store_location sl on sl.id = s.STORE_LOCATION_ID \n" +
            "inner join sx_store_location_group slg on slg.ID = sl.store_location_group_id\n" +
            "where s.STORE_STATE = 31 and slg.ASCENT_LOCK_STATE = 1 and slg.READY_OUT_LOCK = 0")
    List<SxStore> findReadOutStore();

    /**
     * 托盘找库存
     *
     * @param join
     * @return
     */
    @Select("select t.CONTAINER_NO\n" +
            "  from sx_store t where FIND_IN_SET (t.CONTAINER_NO,#{join})" +
            " and t.STORE_STATE > 10")
    List<String> findByContainerCodes(@Param("join") String join);

    /**
     * 定位查询 库存信息
     *
     * @param coordinate
     * @return
     */
    @Select("select s.CONTAINER_NO as containerNo,\n" +
            " t.id as storeLocationId," +
            " s.STORE_STATE as storeState" +
            "  from sx_store_location t " +
            "  left join sx_store s on s.STORE_LOCATION_ID = t.id " +
            "  where t.store_no=#{coordinate}")
    SxStoreLocationDto getSxStoreLocationByCoord(@Param("coordinate") String coordinate);


    @Select("select sl.LAYER as layer,count(s.ID) as containerCount from sx_store_location sl \r\n" +
            "left join sx_store s on s.STORE_LOCATION_ID = sl.id \r\n" +
            "inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID\r\n" +
            "where slg.IS_LOCK = 0 and sl.LAYER NOT IN (select cl.LAYER from sx_ceng_lock cl) and sl.layer in (${layersStr})\r\n" +
            "GROUP BY sl.LAYER ORDER BY sl.LAYER")
    List<AllInStoreLocationLayersDto> findStoreLocation(@Param("layersStr") String layersStr);

    @Select("select sl.LAYER as layer,count(s.ID) as propertyCount from sx_store_location sl \r\n" +
            "left join sx_store s on s.STORE_LOCATION_ID = sl.id \r\n" +
            "where s.TASK_PROPERTY1 = #{taskProperty1} and s.TASK_PROPERTY2 = #{taskProperty2} and sl.LAYER in (${layersStr}) GROUP BY sl.LAYER")
    List<AllInStorePropertyCountDto> findAllPropertyLayer(@Param("layersStr") String layersStr, @Param("taskProperty1") String taskProperty1, @Param("taskProperty2") String taskProperty2);

    @Select("select sl.LAYER as layer,count(s.ID) as inTaskCount from sx_store_location sl \r\n" +
            "left join sx_store s on s.STORE_LOCATION_ID = sl.id \r\n" +
            "where s.STORE_STATE =10 and sl.layer in (${layersStr}) \r\n" +
            "GROUP BY sl.LAYER ORDER BY sl.LAYER")
    List<AllInStoreInTaskCountDto> findInTaskLayer(@Param("layersStr") String layersStr);

    @Select("select sl.LAYER as layer,count(s.ID) as outTaskCount from sx_store_location sl \r\n" +
            "left join sx_store s on s.STORE_LOCATION_ID = sl.id \r\n" +
            "where s.STORE_STATE > 20 and s.STORE_STATE < 40 and sl.layer in (${layersStr}) \r\n" +
            "GROUP BY sl.LAYER ORDER BY sl.LAYER")
    List<AllInStoreOutTaskCountDto> findOutTaskLayer(@Param("layersStr") String layersStr);

    @Select("select count(*) as emptyCount from sx_store_location sl \r\n" +
            "inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID\r\n" +
            "where not EXISTS (select s.STORE_LOCATION_ID from sx_store s where s.STORE_LOCATION_ID = sl.ID) \r\n" +
            "and slg.IS_LOCK = 0 and sl.ASCENT_LOCK_STATE = 0 and slg.ASCENT_LOCK_STATE = 0 \r\n" +
            "and sl.LAYER = #{layer}")
    Integer findEmptyLocation(@Param("layer") Integer layer);

    @Select("select slg.id as storeLocationGroupId,count(s.id) as containerCount,slg.IN_OUT_NUM as inOutNum ,slg.x,slg.y,slg.reserved_location as reservedLocation from sx_store_location sl \r\n" +
            "			left join sx_store s on s.STORE_LOCATION_ID = sl.ID \r\n" +
            "			inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID \r\n" +
            "			where slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0 \r\n" +
            "			and slg.location_num > (select count(*) from sx_store_location sl inner join sx_store s on \r\n" +
            "			s.STORE_LOCATION_ID = sl.ID where sl.STORE_LOCATION_GROUP_ID = slg.ID) \r\n" +
            "			and ((slg.entrance1_property1 = #{taskProperty1} and slg.entrance1_property2 = #{taskProperty2}) \r\n" +
            "			or (slg.entrance2_property1 =  #{taskProperty1} and slg.entrance2_property1 = #{taskProperty2} )) and slg.layer = #{layer} \r\n" +
            "			GROUP BY slg.ID ,slg.IN_OUT_NUM,slg.x,slg.y,slg.reserved_location")
    List<InStoreLocationGroupDto> findSamePropertyStoreLocationGroup(@Param("layer") Integer layer, @Param("taskProperty1") String taskProperty1, @Param("taskProperty2") String taskProperty2);

    @Select("select slg.id as storeLocationGroupId,count(s.id) as containerCount,slg.IN_OUT_NUM as inOutNum ,slg.x,slg.y,slg.reserved_location as reservedLocation,slg.entrance1_property1 as entrance1Property1 ,slg.entrance1_property2 as entrance1Property2 ,slg.entrance2_property1 as entrance2Property1 ,slg.entrance2_property2 as  entrance2Property2 from sx_store_location sl \n" +
            "						left join sx_store s on s.STORE_LOCATION_ID = sl.ID \n" +
            "						inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID \n" +
            "						where slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0  and sl.is_inBound_location = 1\n" +
            "						and slg.location_num > (select count(*) from sx_store_location sl inner join sx_store s on \n" +
            "						s.STORE_LOCATION_ID = sl.ID where sl.STORE_LOCATION_GROUP_ID = slg.ID) \n" +
            "						and slg.layer = #{layer} and EXISTS (select sl2.id from sx_store_location sl2 where sl2.limit_weight > #{weight} and sl2.STORE_LOCATION_GROUP_ID = slg.ID and sl2.is_inBound_location = 1 )\n" +
            "						GROUP BY slg.ID ,slg.IN_OUT_NUM,slg.IN_OUT_NUM,slg.x,slg.y,slg.reserved_location")
    List<InStoreLocationGroupDto> findStoreLocationGroup(@Param("layer") Integer layer, @Param("weight") Double weight);

    @Select("select slg.id as storeLocationGroupId,count(s.id) as containerCount,slg.IN_OUT_NUM as inOutNum ,slg.x,slg.y,slg.reserved_location as reservedLocation,slg.entrance1_property1 as entrance1Property1 ,slg.entrance1_property2 as entrance1Property2 ,slg.entrance2_property1 as entrance2Property1 ,slg.entrance2_property2 as  entrance2Property2 from sx_store_location sl \n" +
            "						left join sx_store s on s.STORE_LOCATION_ID = sl.ID \n" +
            "						inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID \n" +
            "						where slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0 and sl.is_inBound_location = 1\n" +
            "						and slg.location_num > (select count(*) from sx_store_location sl inner join sx_store s on \n" +
            "						s.STORE_LOCATION_ID = sl.ID where sl.STORE_LOCATION_GROUP_ID = slg.ID) \n" +
            "						and slg.layer = #{layer} and slg.belong_area = #{area} and EXISTS (select sl2.id from sx_store_location sl2 where sl2.limit_weight > #{weight} and sl2.STORE_LOCATION_GROUP_ID = slg.ID and sl2.is_inBound_location = 1 )\n" +
            "						GROUP BY slg.ID ,slg.IN_OUT_NUM,slg.IN_OUT_NUM,slg.x,slg.y,slg.reserved_location")
    List<InStoreLocationGroupDto> findStoreLocationGroupByArea(@Param("layer") Integer layer, @Param("area") Integer area, @Param("weight") Double weight);

    @Select("select slg.id as storeLocationGroupId,count(s.id) as containerCount,slg.IN_OUT_NUM as inOutNum ,slg.x,slg.y,slg.reserved_location as reservedLocation,slg.entrance1_property1 as entrance1Property1 ,slg.entrance1_property2 as entrance1Property2 ,slg.entrance2_property1 as entrance2Property1 ,slg.entrance2_property2 as  entrance2Property2 from sx_store_location sl  \r\n" +
            "									left join sx_store s on s.STORE_LOCATION_ID = sl.ID  \r\n" +
            "									inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID  \r\n" +
            "									where slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0  and sl.is_inBound_location = 1 \r\n" +
            "									and slg.location_num > (select count(*) from sx_store_location sl inner join sx_store s on  \r\n" +
            "									s.STORE_LOCATION_ID = sl.ID where sl.STORE_LOCATION_GROUP_ID = slg.ID)  \r\n" +
            "									and slg.layer = #{layer} and EXISTS (select sl2.id from sx_store_location sl2 where sl2.limit_weight > #{weight} and sl2.STORE_LOCATION_GROUP_ID = slg.ID and sl2.is_inBound_location = 1 ) \r\n" +
            "									and sl.id not in (select ssr.store_parent_id from sx_store_location_relation ssr where ssr.location_lock = 1 union select ssr2.store_childen_id from sx_store_location_relation ssr2 where ssr2.location_lock = 1)	GROUP BY slg.ID ,slg.IN_OUT_NUM,slg.IN_OUT_NUM,slg.x,slg.y,slg.reserved_location")
    List<InStoreLocationGroupDto> findStoreLocationGroupForLx(@Param("layer") Integer layer, @Param("weight") Double weight);

    @Select("select slg.id as storeLocationGroupId,count(s.id) as containerCount,slg.IN_OUT_NUM as inOutNum ,slg.x,slg.y,slg.reserved_location as reservedLocation,slg.entrance1_property1 as entrance1Property1 ,slg.entrance1_property2 as entrance1Property2 ,slg.entrance2_property1 as entrance2Property1 ,slg.entrance2_property2 as  entrance2Property2 from sx_store_location sl \r\n" +
            "								left join sx_store s on s.STORE_LOCATION_ID = sl.ID \r\n" +
            "								inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID \r\n" +
            "								where slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0 and sl.is_inBound_location = 1\r\n" +
            "								and slg.location_num > (select count(*) from sx_store_location sl inner join sx_store s on \r\n" +
            "								s.STORE_LOCATION_ID = sl.ID where sl.STORE_LOCATION_GROUP_ID = slg.ID) \r\n" +
            "								and slg.layer = #{layer} and slg.belong_area = #{area} and EXISTS (select sl2.id from sx_store_location sl2 where sl2.limit_weight > #{weight} and sl2.STORE_LOCATION_GROUP_ID = slg.ID and sl2.is_inBound_location = 1 ) and sl.id not in (select ssr.store_parent_id from sx_store_location_relation ssr where ssr.location_lock = 1 union select ssr2.store_childen_id from sx_store_location_relation ssr2 where ssr2.location_lock = 1)\r\n" +
            "								GROUP BY slg.ID ,slg.IN_OUT_NUM,slg.IN_OUT_NUM,slg.x,slg.y,slg.reserved_location")
    List<InStoreLocationGroupDto> findStoreLocationGroupByAreaForLx(@Param("layer") Integer layer, @Param("area") Integer area, @Param("weight") Double weight);


    @Select("select \n" +
            "st.id as id,\n" +
            "st.container_no as containerNo,\n" +
            "st.store_location_id as locationId,\n" +
            "task_type as taskType,\n" +
            "st.store_state as storeState,\n" +
            "st.station_id as stationId,\n" +
            "stl.layer as layer \n" +
            "from sx_store st,sx_store_location stl \n" +
            "where \n" +
            "st.store_location_id = stl.id AND\n" +
            "st.store_state >0 AND st.store_state !=20")
    List<StoreTaskDto> findStoreTasks();

    @Select("select \n" +
            "st.id as id,\n" +
            "st.container_no as containerNo,\n" +
            "st.store_location_id as locationId,\n" +
            "task_type as taskType,\n" +
            "st.store_state as storeState,\n" +
            "st.station_id as stationId,\n" +
            "stl.layer as layer \n" +
            "from sx_store st,sx_store_location stl \n" +
            "where \n" +
            "st.store_location_id = stl.id AND\n" +
            "st.hoister_id = #{hoisId} AND\n" +
            "st.store_state >0 AND st.store_state !=20")
    List<StoreTaskDto> findStoreTasksByHoisId(String hoisId);


    @Select("Select\n" +
            " g.id\n" +
            " FROM sx_store s \n" +
            " LEFT JOIN container_sub cs ON s.container_no=cs.container_no\n" +
            " LEFT JOIN goods g ON cs.commodity_id=g.id\n" +
            " WHERE g.goods_no='3' AND g.goods_name='微软平板' AND\n" +
            " s.store_time BETWEEN '2020-07-16 16:36:30' AND '2020-08-16 16:36:41';")
    List<Integer> findDisABleGoodsIds(@Param("goodsNo") String goodsNo,
                                      @Param("goodsName") String goodsName,
                                      @Param("startTime") String startTime,
                                      @Param("endTime") String endTime);

    @Select("SELECT\n" +
            "\tly.layer as layer,\n" +
            "\tSUM( CASE s.store_state WHEN 10 THEN 1 ELSE 0 END ) inboundCount,\n" +
            "\tSUM( CASE s.store_state WHEN 30 THEN 1 WHEN 31 THEN 1 ELSE 0 END ) outBoundCount,\n" +
            "\tly.lock_state as isLock\n" +
            "FROM\n" +
            "\tsx_layer ly\n" +
            "\tLEFT JOIN sx_store_location sl ON ly.layer = sl.layer\n" +
            "\tLEFT JOIN sx_store s ON sl.id = s.store_location_id \n" +
            "GROUP BY\n" +
            "\tly.layer")
    List<SxLayerDto> getLayerInfo() throws Exception;

    @Select("\n" +
            "( SELECT\n" +
            "\tgs.goods_name AS goodsName,\n" +
            "\tgs.goods_no AS barCode,\n" +
            "\tssln.layer AS layer,\n" +
            "\tssln.x AS x,\n" +
            "\tssln.y AS y,\n" +
            "\tcs.container_no AS containerNo,\n" +
            "\tcs.commodity_num AS commodityNum,\n" +
            "\tcs.container_sub_no AS containerSubNo,\n" +
            "\tcs.expiry_date AS expiryDate,\n" +
            "\tcs.inbound_time AS inboundTime,\n" +
            "\tssln.depth AS depth,\n" +
            "\tss.store_state AS storeState \n" +
            "\tFROM\n" +
            "\t\tsx_store ss\n" +
            "\t\tLEFT JOIN container_sub cs ON ss.container_no = cs.container_no\n" +
            "\t\tJOIN sx_store_location ssln ON ss.store_location_id = ssln.id\n" +
            "\t\tJOIN goods gs ON cs.commodity_id = gs.id \n" +
            "\t) UNION\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\tg.goods_name AS goodsName,\n" +
            "\t\tg.goods_no AS barCode,\n" +
            "\t\t0,\n" +
            "\t\t0,\n" +
            "\t\t0,\n" +
            "\t\tcs.container_no AS containerNo,\n" +
            "\t\t\t\tcs.commodity_num AS commodityNum,\n" +
            "\t\tcs.container_sub_no AS containerSubNo,\n" +
            "\t\tcs.expiry_date AS expiryDate,\n" +
            "\t\tcs.inbound_time AS inboundTime ,\n" +
            "\t\t0,\n" +
            "\t\t0\n" +
            "\tFROM\n" +
            "\t\tzt_store zt\n" +
            "\t\tJOIN container_sub cs ON cs.container_no = zt.container_no\n" +
            "\tJOIN goods g ON g.id = cs.commodity_id \n" +
            "\t)\n")
            List<ExcelStoreDto> getExcelStoreList();

    @Select("SELECT\n" +
            "\tsl.layer,\n" +
            "\tSUM( CASE s.store_state WHEN 10 THEN 1 ELSE 0 END ) outCount,\n" +
            "\tSUM( CASE s.store_state WHEN 30 THEN 1 WHEN 31 THEN 1 ELSE 0 END ) inCount \n" +
            "FROM\n" +
            "sx_layer ly LEFT JOIN\n" +
            "sx_store_location sl ON ly.layer=sl.layer\n" +
            "LEFT JOIN sx_store s ON sl.id=s.store_location_id\n" +
            "where sl.layer = #{layer} ")
    List<LayerTaskCountDto> getTaskCountByLayer(@Param("layer") int layer);
}
