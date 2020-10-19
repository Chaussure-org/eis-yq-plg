package com.prolog.eis.dao.store;

import com.prolog.eis.model.store.SxCeng;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

public interface SxLayerMapper extends BaseMapper<SxCeng> {

    @Update("update sx_layer set lock_state = #{lockState} where layer = #{layer}")
    public void updateByLayer(SxCeng sxCeng);


    @Update("update sx_layer set lock_state = #{lockState},is_disable=#{isDisable} where layer = #{layer}")
    public void updateByDoor(SxCeng sxCeng);
}
