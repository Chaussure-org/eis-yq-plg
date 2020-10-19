package com.prolog.eis.dao;

import com.prolog.eis.dto.apprf.AppInstockOrderCcceptanceDto;
import com.prolog.eis.model.apprf.AppContainerSubModel;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author jinxf
 */
public interface AppContainerSubInfoMapper extends BaseMapper<AppContainerSubModel> {
    @Select("select t4.id,t1.container_no containerNo,\n" +
            "\tSUBSTRING(t1.container_sub_no, - 1) grid,\n" +
            "    t1.container_sub_no containerSubNo,\n" +
            "    t2.id goodsId,\n" +
            "    t2.goods_name goodsName,\n" +
            "    t2.goods_no goodsNo,\n" +
            "    t3.bar_code goodsBarCode,\n" +
            "    t1.commodity_num goodsNum,\n" +
            "    t1.expiry_date expiryDate,\n" +
            "    t1.inbound_time inboundTime,\n" +
            "    t1.reference_num referenceNum\n" +
            "from container_sub t1\n" +
            "inner join goods t2 on t2.id=t1.commodity_id\n" +
            "inner join goods_bar_code t3 on t3.goods_id=t2.id\n" +
            "inner join inbound_task t4 on t4.container_no=t1.container_no " +
            "WHERE t4.container_no = #{containerNo}  ")
    public List<AppInstockOrderCcceptanceDto> query(@Param("containerNo") String containerNo);

    @Update("update container_sub\n" +
            "set commodity_id=null,commodity_num=null,expiry_date=null\n" +
            "where container_no=#{containerNo}")
    public void cancel(@Param("containerNo") String containerNo);
}
	