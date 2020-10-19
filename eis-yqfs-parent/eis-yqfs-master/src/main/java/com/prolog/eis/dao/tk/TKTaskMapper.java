package com.prolog.eis.dao.tk;

import com.prolog.eis.model.tk.TKTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TKTaskMapper extends BaseMapper<TKTask> {
    /**
     * 批量下发
     * @param ids
     */
    @Update("update tk_task t set t.status=1 where FIND_IN_SET(t.id,#{ids})")
    long releaseBatch(@Param("ids") String ids);
}
