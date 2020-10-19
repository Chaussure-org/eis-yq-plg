package com.prolog.eis.dao;

import com.prolog.eis.dto.apprf.AppInstockOrderCcceptanceDto;
import com.prolog.eis.model.apprf.AppInboundTaskMxModel;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jinxf
 */
public interface AppInboundTaskMxMapper extends BaseMapper<AppInboundTaskMxModel> {
    @Select("SELECT \n"+
            "  h.id,\n"+
            "  h.`container_no` containerNo,"+
            "  SUBSTRING(d.`container_sub_no`, - 1) grid,\n"+
            "  d.`container_sub_no` containerSubNo,\n"+
            "  item.`id` goodsId,\n"+
            "  item.`goods_name` goodsName,\n"+
            "  item.`goods_no` goodsNo,\n"+
            "  d.goods_num goodsNum,\n"+
            "  d.`expiry_date` expiryDate \n"+
            "FROM\n"+
            "  INBOUND_TASK_MX d \n"+
            "  INNER JOIN INBOUND_TASK_HZ h \n"+
            "    ON d.inbound_task_hz_id = h.`id` \n"+
            "  LEFT JOIN GOODS item \n"+
            "    ON item.`id` = d.`goods_id` \n"+
            "WHERE h.`container_no` = #{containerNo}  ")
    public List<AppInstockOrderCcceptanceDto> query(@Param("containerNo") String containerNo);

}
	