package com.prolog.eis.dao.assembl;

import com.prolog.eis.dto.assembl.AssemblBoxMxDto;
import com.prolog.eis.dto.hxdispatch.HxAssemblBoxMx;
import com.prolog.eis.model.assembl.AssemblBoxMx;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 合箱计划明细表(AssemblBoxMx)表数据库访问层
 *
 * @author panteng
 * @since 2020-05-03 16:57:02
 */
public interface AssemblBoxMxMapper extends BaseMapper<AssemblBoxMx>{

    /**
     * 当前站台已索取的合箱明细任务
     * @param hzIdsStr
     * @return
     */
    @Select("SELECT abh.assembl_box_hz_id AS assemblBoxHzId,\n" +
            "       abh.container_no AS lxNo,\n" +
            "       abh.task_state AS taskState,\n" +
            " if(exists(select 1 from zt_store zs where zs.container_no = abh.container_no),1,0) as isArrive,"+
            " ssln.layer as ceng "+
            " from assembl_box_mx abh" +
            "  join sx_store ss on ss.CONTAINER_NO = abh.container_no\n" +
            "  join sx_store_location ssln on ssln.id = ss.store_location_id \n" +
            "where find_in_set(abh.assembl_box_hz_id,#{hzIdsStr})")
    List<HxAssemblBoxMx> findByHzIdsStr(@Param("hzIdsStr") String hzIdsStr);


    /**
     * 根据汇总查看明细
     */
    @Select("SELECT\n" +
            "\tabh.id as assemblBoxHzId,\n" +
            "\tabm.container_no as containerNo,\n" +
            "\tcs.container_sub_no as containerSubNo,\n" +
            "\tg.goods_no as goodsNo,\n" +
            "\tg.goods_name as goodsName,\n" +
            "\tsx.layer as layer,\n" +
            "\t\n" +
            "\tsx.x as x,\n" +
            "\tsx.y as y,\n" +
            "\tcs.commodity_num as commodityNum,\n" +
            "\tcs.expiry_date as expiryDate,\n" +
            "\tcs.inbound_time as inboundTime,\n" +
            "\tcs.reference_num as referenceNum,\n" +
            "\tabm.task_state as taskType\n" +
            "\t\n" +
            "FROM\n" +
            "\tassembl_box_hz abh\n" +
            "\tLEFT JOIN assembl_box_mx abm ON abh.id = abm.assembl_box_hz_id\n" +
            "\tLEFT JOIN container_sub cs ON cs.container_no = abm.container_no\n" +
            "\tleft join goods g on g.id = cs.commodity_id \n" +
            "\tleft join sx_store as s on s.container_no = cs.container_no\n" +
            "\tleft join sx_store_location sx on sx.id = s.store_location_id\n" +
            "\twhere\n" +
            "\tabh.id = #{id}")
    List<AssemblBoxMxDto> findAssembBoxMxById(@Param("id") int id);

    @Select("SELECT\n" +
            "\thz.id \n" +
            "FROM\n" +
            "\tassembl_box_mx mx\n" +
            "\tLEFT JOIN assembl_box_hz hz ON mx.assembl_box_hz_id = hz.id \n" +
            "WHERE\n" +
            "\tmx.container_no = #{containerNo} \n" +
            "\tAND hz.task_state = 2;")
    int getAssemblBoxMxDetail(@Param("containerNo") String containerNo);


}