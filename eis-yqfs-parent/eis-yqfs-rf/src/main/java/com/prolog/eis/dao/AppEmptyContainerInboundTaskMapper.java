package com.prolog.eis.dao;

import com.prolog.eis.model.apprf.AppEmptyContainerInboundTaskModel;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author jinxf
 */
public interface AppEmptyContainerInboundTaskMapper extends BaseMapper<AppEmptyContainerInboundTaskModel> {

    @Insert("insert into EMPTY_CONTAINER_INBOUND_TASK (id,container_no,create_time) values (EMPTY_CONTAINER_INBOUND_TASK_SEQ.NEXTVAL,#{containerNo},#{createTime})")
    public void saveAppEmpty(@Param("containerNo") String containerNo, @Param("createTime") Date createTime);


}
	