package com.prolog.eis.dao.store;

import com.prolog.eis.dto.store.LocationDetailDto;
import com.prolog.eis.dto.store.StoreLocationGroupDto;
import com.prolog.eis.dto.store.VerticalLocationDto;
import com.prolog.eis.model.store.SxStoreLocationGroup;
import org.apache.ibatis.annotations.*;

import com.prolog.eis.model.store.SxStoreLocation;
import com.prolog.framework.dao.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface StoreLocationMapper extends BaseMapper<SxStoreLocation> {

	  //查询当前层最最小可用的货位id(STORE_LOCATION_ID)
    @Select("SELECT\n" +
            "\tMIN( sl.id ) \n" +
            "FROM\n" +
            "\tsx_store_location sl\n" +
            "\tLEFT JOIN sx_store ss ON sl.id = ss.store_location_id \n" +
            "WHERE\n" +
            "\tsl.layer = (\n" +
            "\tSELECT\n" +
            "\t\tlc.layer \n" +
            "\tFROM\n" +
            "\t\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\ts1.layer,\n" +
            "\t\t\tcount( s2.container_no ) AS count \n" +
            "\t\tFROM\n" +
            "\t\t\tsx_store_location s1\n" +
            "\t\t\tLEFT JOIN sx_store s2 ON s1.id = s2.store_location_id \n" +
            "\t\tGROUP BY\n" +
            "\t\t\ts1.layer \n" +
            "\t\t) lc \n" +
            "\tORDER BY\n" +
            "\t\tlc.count ASC \n" +
            "\t\tLIMIT 1 \n" +
            "\t) \n" +
            "\tAND ss.id IS NULL")
    int getStoreLocationId();

    @Select("select sl.id,sl.STORE_LOCATION_GROUP_ID as storeLocationGroupId,sl.LAYER as layer ,sl.x,sl.y,sl.STORE_LOCATION_ID1 as storeLocationId1,\r\n" +
            "sl.STORE_LOCATION_ID2 as storeLocationId2 ,sl.ASCENT_LOCK_STATE as ascentLockState ,sl.LOCATION_INDEX as locationIndex ,sl.dept_num as deptNum\r\n" +
            "from sx_store_location sl \\r\\n "+
            "inner join sx_store s on s.STORE_LOCATION_ID = sl.ID\r\n" +
            "inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID\r\n" +
            "where sl.STORE_LOCATION_GROUP_ID = #{locationGroupId} and  ((slg.entrance1_property1 = #{taskProperty1} and slg.entrance1_property2 = #{taskProperty2}) \r\n" +
            "or  (slg.entrance2_property1 = #{taskProperty1} and slg.entrance2_property2 = #{taskProperty2})) and sl.dept_num = 0")
    List<SxStoreLocation> findByProperty(@Param("locationGroupId") Integer locationGroupId, @Param("taskProperty1") String taskProperty1, @Param("taskProperty2") String taskProperty2);


    @Select("select sl.id,sl.STORE_LOCATION_GROUP_ID as storeLocationGroupId,sl.LAYER as layer ,sl.x,sl.y,sl.STORE_LOCATION_ID1 as storeLocationId1,\r\n" +
            "sl.STORE_LOCATION_ID2 as storeLocationId2 ,sl.ASCENT_LOCK_STATE as ascentLockState ,sl.LOCATION_INDEX as locationIndex ,sl.dept_num as deptNum from sx_store_location sl \r\n" +
            "inner join sx_store s on s.STORE_LOCATION_ID = sl.ID\r\n" +
            "inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID\r\n" +
            "where sl.STORE_LOCATION_GROUP_ID = #{locationGroupId} and  slg.entrance1_property1 = #{taskProperty1} and slg.entrance1_property2 = #{taskProperty2}\r\n" +
            "and sl.dept_num = 0 ORDER BY sl.location_index limit 1")
    List<SxStoreLocation> findByProperty1(@Param("locationGroupId") Integer locationGroupId, @Param("taskProperty1") String taskProperty1, @Param("taskProperty2") String taskProperty2);

    @Select("select sl.id,sl.STORE_LOCATION_GROUP_ID as storeLocationGroupId,sl.LAYER as layer ,sl.x,sl.y,sl.STORE_LOCATION_ID1 as storeLocationId1,\r\n" +
            "sl.STORE_LOCATION_ID2 as storeLocationId2 ,sl.ASCENT_LOCK_STATE as ascentLockState ,sl.LOCATION_INDEX as locationIndex ,sl.dept_num as deptNum from sx_store_location sl \r\n" +
            "inner join sx_store s on s.STORE_LOCATION_ID = sl.ID\r\n" +
            "inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID\r\n" +
            "where sl.STORE_LOCATION_GROUP_ID = #{locationGroupId} and  \r\n" +
            "slg.entrance2_property1 = #{taskProperty1} and slg.entrance2_property2 = #{taskProperty2} and sl.dept_num = 0 ORDER BY sl.location_index desc limit 1" )
    List<SxStoreLocation> findByProperty2(@Param("locationGroupId") Integer locationGroupId, @Param("taskProperty1") String taskProperty1, @Param("taskProperty2") String taskProperty2);


    @Select("select sl.id,sl.STORE_LOCATION_GROUP_ID as storeLocationGroupId,sl.LAYER as layer ,sl.x,sl.y,sl.STORE_LOCATION_ID1 as storeLocationId1,\r\n" +
            "sl.STORE_LOCATION_ID2 as storeLocationId2 ,sl.ASCENT_LOCK_STATE as ascentLockState ,sl.LOCATION_INDEX as locationIndex ,sl.dept_num as deptNum from sx_store_location sl \r\n" +
            "inner join sx_store s on s.STORE_LOCATION_ID = sl.ID \r\n" +
            "where sl.STORE_LOCATION_GROUP_ID = #{storeLocationGroupId} and sl.dept_num = 0")
    List<SxStoreLocation> findStoreLocation(@Param("storeLocationGroupId") int id);

    @Delete("delete from sx_store_location")
    void deleteAll();

    @Insert("<script>insert into SX_STORE_LOCATION " +
            "(ascent_lock_state,layer,location_index,store_location_group_id,store_location_id1,store_location_id2,x,y,depth" +
            ",store_no,dept_num) values " +
            "<foreach collection='list' item='c' separator=','>" +
            "(0,#{c.layer}" +
            ",#{c.locationIndex},#{c.storeLocationGroupId},#{c.storeLocationId1},#{c.storeLocationId2}" +
            ",#{c.x},#{c.y},#{c.depth},#{c.storeNo},0)" +
            "</foreach></script>")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void saveBatchReturnKey(@Param("list") List<SxStoreLocation> storeLocationDtos);

    @Update("<script>UPDATE SX_STORE_LOCATION t," +
            "(<foreach collection = \"list\" separator = \"union all\" item = \"item\">\n" +
            "     select #{item.id} as id," +
            "            #{item.storeLocationId1} as storeLocationId1," +
            "            #{item.storeLocationId2} as storeLocationId2" +
            "  </foreach>) t2"+
            " set t.store_location_id1=t2.storeLocationId1,t.store_location_id2 = t2.storeLocationId2" +
            " where t.id = t2.id</script>")
    void updateAdjacentStore(@Param("list") List<SxStoreLocation> storeLocationDtos);

    @Update("<script>UPDATE SX_STORE_LOCATION t," +
            "(<foreach collection = \"list\" separator = \"union all\" item = \"item\">\n" +
            "     select #{item.storeLocationGroupId} as storeLocationGroupId," +
            "            #{item.x} as x," +
            "            #{item.y} as y," +
            "            #{item.locationIndex} as locationIndex" +
            "  </foreach>) t2"+
            " set t.x=t2.x,t.y=t2.y" +
            " where t.STORE_LOCATION_GROUP_ID = t2.storeLocationGroupId AND t.LOCATION_INDEX = t2.locationIndex</script>")
    void batchUpdateById(@Param("list") List<SxStoreLocation> list);

    @Update("<script>UPDATE SX_STORE_LOCATION t," +
            "(<foreach collection = \"list\" separator = \"union all\" item = \"item\">\n" +
            "     select #{item.id} as id," +
            " 			 #{item.wmsStoreNo} as wmsStoreNo" +
            "  </foreach>) t2"+
            " set t.wms_store_no=t2.wmsStoreNo" +
            " where t.id = t2.id</script>")
    void batchUpdateWmsHuoWei(@Param("list") List<SxStoreLocation> list);

    @Update("update sx_store_location sx set sx.dept_num = (sx.dept_num + 1) where sx.dept_num > 0 and sx.store_location_group_id = #{groupId}")
    void updateYKLocation(@Param("groupId") int groupId);

    @Select("select max(sx.dept_num) from sx_store_location sx where sx.store_location_group_id =#{sxStoreLocationGroupId}")
    Integer findMaxYkNum(@Param("sxStoreLocationGroupId") int sxStoreLocationGroupId);


    @Select("select sx.id,sx.store_location_group_id as storeLocationGroupId , sx.layer ,sx.x,sx.y ,sx.store_location_id1 as storeLocationId1,\r\n" +
            "sx.store_location_id2 as storeLocationId2 ,sx.ascent_lock_state as ascentLockState, sx.location_index as locationIndex,sx.dept_num as deptNum,\r\n" +
            "sx.create_time as createTime\r\n" +
            "from sx_store_location sx \r\n" +
            "inner join sx_store s on s.STORE_LOCATION_ID = sx.id where sx.store_location_group_id = #{sxStoreLocationGroupId}")
    List<SxStoreLocation> findHaveStore(@Param("sxStoreLocationGroupId") int sxStoreLocationGroupId);

    @Select("select count(*) from sx_store_location sx \r\n" +
            "inner join sx_store s on s.STORE_LOCATION_ID = sx.id where sx.store_location_group_id = #{sxStoreLocationGroupId} and sx.location_index > #{index}")
    Integer findBigIndexCount(@Param("sxStoreLocationGroupId") int sxStoreLocationGroupId, @Param("index") int index);

    @Select("select count(*) from sx_store_location sx \r\n" +
            "inner join sx_store s on s.STORE_LOCATION_ID = sx.id where sx.store_location_group_id = #{sxStoreLocationGroupId} and sx.location_index < #{index}")
    Integer findSmallIndexCount(@Param("sxStoreLocationGroupId") int sxStoreLocationGroupId, @Param("index") int index);


    @Select("select sx.id,sx.store_location_group_id as storeLocationGroupId , sx.layer ,sx.x,sx.y ,sx.store_location_id1 as storeLocationId1, \r\n" +
            "			sx.store_location_id2 as storeLocationId2 ,sx.ascent_lock_state as ascentLockState, sx.location_index as locationIndex,sx.dept_num as deptNum, \r\n" +
            "			sx.create_time as createTime \r\n" +
            "			from sx_store_location sx  \r\n" +
            "			inner join sx_store s on s.STORE_LOCATION_ID = sx.id where sx.store_location_group_id = #{sxStoreLocationGroupId} order by sx.location_index")
    List<SxStoreLocation> findMinHaveStore(@Param("sxStoreLocationGroupId") int sxStoreLocationGroupId);

    @Select("select sx.id,sx.store_location_group_id as storeLocationGroupId , sx.layer ,sx.x,sx.y ,sx.store_location_id1 as storeLocationId1, \r\n" +
            "			sx.store_location_id2 as storeLocationId2 ,sx.ascent_lock_state as ascentLockState, sx.location_index as locationIndex,sx.dept_num as deptNum, \r\n" +
            "			sx.create_time as createTime \r\n" +
            "			from sx_store_location sx  \r\n" +
            "			inner join sx_store s on s.STORE_LOCATION_ID = sx.id where sx.store_location_group_id = #{sxStoreLocationGroupId} order by sx.location_index desc")
    List<SxStoreLocation> findMaxHaveStore(int sxStoreLocationGroupId);

    @Select("select l.x,l.y,l.layer from sx_store_location l left join sx_store ss on l.id = ss.store_location_id where ss.container_no = #{containerNo}")
    Map<String,Integer> findXYLayer(@Param("containerNo") String containerNo);

    /**
     * 查询货位移库数、深度等信息
     * @param join
     * @return
     */
    @Select("select\r\n" +
            "	ss.container_no as containerNo,\r\n" +
            "	l.dept_num as deptNum,\r\n" +
            "	l.depth\r\n" +
            "from\r\n" +
            "	sx_store ss\r\n" +
            "left join sx_store_location l on\r\n" +
            "	ss.store_location_id = l.id\r\n" +
            "where\r\n" +
            "	find_in_set(ss.container_no, #{join})\r\n" +
            "order by\r\n" +
            "	l.dept_num,\r\n" +
            "	l.depth asc")
    List<LocationDetailDto> findLocationDetail(@Param("join") String join);


    @Select("select * from sx_store_location sl where sl.STORE_LOCATION_GROUP_ID = #{locationGroupId} and sl.limit_weight > #{weight} and sl.is_inBound_location = 1")
    List<SxStoreLocation> checkWeight(@Param("locationGroupId") Integer locationGroupId, @Param("weight") double weight);

    @Update("update sx_store_location sx set sx.is_inBound_location = 0 where sx.id in (${idstr}) ")
    void updateNotIsInboundLocation(@Param("idstr") String idstr);

    @Select("select * from sx_store_location sl where sl.STORE_LOCATION_GROUP_ID = #{sxStoreLocationGroupId} and sl.location_index != #{haveStoreIndex} ")
    List<SxStoreLocation> findNotIndex(@Param("sxStoreLocationGroupId") int sxStoreLocationGroupId, @Param("haveStoreIndex") Integer haveStoreIndex);

    @Select("select * from sx_store_location sl where sl.store_location_group_id  = #{sxStoreLocationGroupId}"
            + " and sl.location_index != #{bigHaveStoreIndex} and sl.location_index != #{smallHaveStoreIndex}")
    List<SxStoreLocation> findNotIndexTwo(@Param("sxStoreLocationGroupId") int sxStoreLocationGroupId, @Param("bigHaveStoreIndex") Integer bigHaveStoreIndex,
                                          @Param("smallHaveStoreIndex") Integer smallHaveStoreIndex);


    /**
     * 根据货位编号s查询
     * @param join
     * @return
     */
    @Select("select t.store_no\n" +
            "  from sx_store_location t where FIND_IN_SET (t.store_no,#{join})" )
    List<String> findByStoreNos(@Param("join") String join);

    @Update("<script>UPDATE SX_STORE_LOCATION t," +
            "(<foreach collection = \"list\" separator = \"union all\" item = \"item\">\n" +
            "     select #{item.id} as id," +
            "            #{item.limitWeight} as limitWeight," +
            "            0 as actualWeight," +
            "            #{item.storeNo} as storeNo" +
            "  </foreach>) t2"+
            " set t.actual_weight = t2.actualWeight," +
            "	t.limit_weight=t2.limitWeight, t.vertical_location_group_id = t2.id" +
            " where t.store_no = t2.storeNo</script>")
    void updateVerticalLocations(@Param("list") List<VerticalLocationDto> list);

    @Select("select g.belong_area from sx_store_location t \r\n" +
            "left join sx_store_location_group g on t.store_location_group_id = g.id \r\n" +
            "where t.x = #{x} and t.y = #{y} and t.layer = #{layer}")
    int findPositionRegion(@Param("x") int x, @Param("y") int y, @Param("layer") int layer);


    //查询所有的id和货位编号
    @Select("select s.id as id ,s.store_no as storeNo from sx_store_location s")
    List<SxStoreLocation> findIdAndSroreNo();
    //根据容器编号查id
    int findSxSroreIdByStroeNo(String storeNo);

    /**
     * 寻找可用货位
     * @return
     */
    @Select("select \n" +
            "a.id,\n" +
            "a.store_no,\n" +
            "a.store_location_group_id,\n" +
            "a.layer,\n" +
            "a.x,\n" +
            "a.y,\n" +
            "a.store_location_id1,\n" +
            "a.store_location_id2,\n" +
            "a.ascent_lock_state,\n" +
            "a.location_index,\n" +
            "a.dept_num ,\n" +
            "a.depth ,\n" +
            "a.create_time,\n" +
            "a.vertical_location_group_id,\n" +
            "a.actual_weight,\n" +
            "a.limit_weight,\n" +
            "a.is_inbound_location,\n" +
            "a.wms_store_no\n" +
            "from \n" +
            "(\n" +
            "select * from sx_store_location \n" +
            "where \n" +
            "ascent_lock_state =0 AND\n" +
            "layer in (select layer from sx_layer where lock_state=0) and\n" +
            "store_location_group_id in (select id from sx_store_location_group where ascent_lock_state=0 and is_lock=0 and ready_out_lock=0) and " +
            "id not in (select store_location_id from sx_store) \n" +
            ") a\n" +
            "left join \n" +
            "(select location_child_id,sum(location_lock) locks from sx_store_location_relation group by location_child_id) b \n" +
            "on a.id = b.location_child_id\n" +
            "where b.locks=0\n")
    @Results(id="storeLocationMap",value =
            {
                @Result(column = "id",property = "id"),
                @Result(column = "store_no",property = "storeNo"),
                @Result(column = "store_location_group_id",property = "storeLocationGroupId"),
                @Result(column = "layer",property = "layer"),
                @Result(column = "x",property = "x"),
                @Result(column = "y",property = "y"),
                @Result(column = "store_location_id1",property = "storeLocationId1"),
                @Result(column = "store_location_id2",property = "storeLocationId2"),
                @Result(column = "ascent_lock_state",property = "ascentLockState"),
                @Result(column = "location_index",property = "locationIndex"),
                @Result(column = "dept_num",property = "deptNum"),
                @Result(column = "depth",property = "depth"),
                @Result(column = "create_time",property = "createTime"),
                @Result(column = "vertical_location_group_id",property = "verticalLocationGroupId"),
                @Result(column = "actual_weight",property = "actualWeight"),
                @Result(column = "limit_weight",property = "limitWeight"),
                @Result(column = "is_inbound_location",property = "isInBoundLocation"),
                @Result(column = "wms_store_no",property = "wmsStoreNo"),
            }
    )
    List<SxStoreLocation> findAvailableLocation();

    /**
     * 根据层查找空货位
     * @param layer
     * @return
     */
    @Select("select \n" +
            "a.id,\n" +
            "a.store_no,\n" +
            "a.store_location_group_id,\n" +
            "a.layer,\n" +
            "a.x ,\n" +
            "a.y ,\n" +
            "a.store_location_id1,\n" +
            "a.store_location_id2,\n" +
            "a.ascent_lock_state,\n" +
            "a.location_index ,\n" +
            "a.dept_num ,\n" +
            "a.depth ,\n" +
            "a.create_time,\n" +
            "a.vertical_location_group_id ,\n" +
            "a.actual_weight ,\n" +
            "a.limit_weight ,\n" +
            "a.is_inbound_location ,\n" +
            "a.wms_store_no \n" +
            "from \n" +
            "(\n" +
            "select * from sx_store_location \n" +
            "where \n" +
            "ascent_lock_state =0 AND\n" +
            "layer in (select layer from sx_layer where lock_state=0) and\n" +
            "store_location_group_id in (select id from sx_store_location_group where ascent_lock_state=0 and is_lock=0 and ready_out_lock=0) and " +
            "id not in (select store_location_id from sx_store) \n" +
            ") a\n" +
            "left join \n" +
            "(select location_child_id,sum(location_lock) locks from sx_store_location_relation group by location_child_id) b \n" +
            "on a.id = b.location_child_id\n" +
            "where b.locks=0 and a.layer =#{layer} \n")
    @ResultMap("storeLocationMap")
    List<SxStoreLocation> findAvailableLocationByLayer(@Param("layer") int layer);


    /**
     * 查找可用空货位层
     * @return
     */
    @Select("SELECT\n" +
            "\ta.layer AS layer \n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\t* \n" +
            "\tFROM\n" +
            "\t\tsx_store_location \n" +
            "\tWHERE\n" +
            "\t\tascent_lock_state = 0 \n" +
            "\t\tAND layer IN ( SELECT layer FROM sx_layer WHERE lock_state = 0 and is_disable = 0) \n" +
            "\t\tAND store_location_group_id IN ( SELECT id FROM sx_store_location_group WHERE ascent_lock_state = 0 AND is_lock = 0 AND ready_out_lock = 0 ) \n" +
            "\t\tAND id NOT IN ( SELECT store_location_id FROM sx_store ) \n" +
            "\t) a\n" +
            "\tLEFT JOIN ( SELECT location_child_id, sum( location_lock ) locks FROM sx_store_location_relation GROUP BY location_child_id ) b ON a.id = b.location_child_id \n" +
            "WHERE\n" +
            "\tb.locks = 0 \n" +
            "GROUP BY\n" +
            "\ta.layer")
    List<Integer> findAvailableLayer();

}
