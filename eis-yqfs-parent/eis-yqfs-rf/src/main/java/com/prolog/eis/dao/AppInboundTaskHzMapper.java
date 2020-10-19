package com.prolog.eis.dao;

import com.prolog.eis.model.apprf.AppInboundTaskModel;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * v
 *
 * @author jinxf
 */
public interface AppInboundTaskHzMapper extends BaseMapper<AppInboundTaskModel> {
    @Select("select 1 from sx_store t1 where t1.container_no=#{containerNo}\n" +
            "union all \n" +
            "select 1 from zt_store t1 where t1.container_no=#{containerNo}")
    public List<HashMap<String, Object>> checkContainer(@Param("containerNo") String containerNo);
}
	