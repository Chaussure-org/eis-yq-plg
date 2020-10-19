package com.prolog.eis.util.mapper;

import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author panteng
 * @description: eis扩展BaseMapper
 * @date 2020/4/17 18:21
 */
public interface EisBaseMapper<T> extends BaseMapper<T> {

    @SelectProvider(
            method = "findByEisQuery",
            type = EisSqlFactory.class
    )
    List<T> findByEisQuery(@Param(Query.QUERY) Query qry);
}
