package com.prolog.eis.dao.assembl;

import com.prolog.eis.dto.hxdispatch.HxAssemblBoxHz;
import com.prolog.eis.dto.hxdispatch.HxCengDto;
import com.prolog.eis.model.assembl.AssemblBoxHz;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 合箱计划汇总表(AssemblBoxHz)表数据库访问层
 *
 * @author panteng
 * @since 2020-05-03 16:56:41
 */
public interface AssemblBoxHzMapper extends BaseMapper<AssemblBoxHz>{

    /**
     * 当前站台已索取的合箱任务
     * @param ztIdsStr
     * @return
     */
    @Select("SELECT abh.goods_id AS spId,\n" +
            "       abh.station_id AS stationId,\n" +
            "       abh.id AS hzId\n" +
            "from assembl_box_hz abh\n" +
            "where find_in_set(abh.station_id,#{ztIdsStr})")
    List<HxAssemblBoxHz> findByZtIdsStr(@Param("ztIdsStr") String ztIdsStr);

    /**
     * 可以索取的合箱任务集合
     * @return
     */
    @Select("SELECT abh.goods_id AS spId,\n" +
            "       abh.station_id AS stationId,\n" +
            "       abh.id AS hzId\n" +
            "from assembl_box_hz abh\n" +
            "where abh.station_id = 0")
    List<HxAssemblBoxHz> CanPickAssemblBoxHzList();

    /**
     * 查询层集合
     * @return
     */
    @Select("select\n" +
            "sx.layer as ceng,\n" +
            "       count(IF(t.STORE_STATE = 10, 1, 0)) as rkLxCount,\n" +
            "       count(IF(t.STORE_STATE in (30,31),1,0)) as ckLxCount\n" +
            "from sx_layer sx\n" +
            "left join\n" +
            "    (select ss.STORE_STATE,s.layer\n" +
            "        from sx_store ss\n" +
            "        join sx_store_location s on ss.STORE_LOCATION_ID = s.id\n" +
            "        where ss.STORE_STATE in (10,30,31)) t on t.layer = sx.layer\n" +
            "group by sx.layer")
    List<HxCengDto> getCengList();

    /**
     *查询合箱计划单下发的商品id
     */
    @Select("select h.goods_id as goodsId from assembl_box_hz h join assembl_box_mx m on m.assembl_box_hz_id = h.id" +
            " and  h.task_state = 2 and m.container_no = #{containerNo}")
    List<Integer> getGoodsId(@Param("containerNo") String containerNo);

}