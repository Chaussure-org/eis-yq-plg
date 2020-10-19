package com.prolog.eis.dao.test;

import com.prolog.eis.wcs.dto.CarInfoDTO;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/8/10 0:33
 */
public interface RgvMapper extends BaseMapper<Rgv> {

    @Select("select rgv_id as rgvId,status,layer from rgv")
    List<CarInfoDTO> findAll();
}
