package com.prolog.eis.dao.masterbase;



import com.prolog.eis.dto.store.LayerContainerCountDto;
import com.prolog.eis.model.masterbase.Container;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ContainerMapper extends BaseMapper<Container> {



    @Select("select count(ct.container_no) as containerCount,stl.layer from \n" +
            "(\n" +
            "select DISTINCT a.container_no from container a\n" +
            "left join container_sub csb on a.container_no = csb.container_no\n" +
            " where csb.commodity_id=#{goodsId}) ct\n " +
            "left join sx_store st on ct.container_no = st.container_no\n" +
            "left join sx_store_location stl on st.store_location_id = stl.id\n" +
            "where stl.layer>0 group by stl.layer")
    @Results(id="layerContainerCountDtoMap",value={
            @Result(column = "layer",property = "layer"),
            @Result(column = "containerCount",property = "containerCount")
    })
	List<LayerContainerCountDto> fintContainerCountPerLayer(@Param("goodsId") int goodsId);

    @Select("select count(ct.container_no) as containerCount,stl.layer from \n" +
            "(\n" +
            "select DISTINCT a.container_no from container a\n" +
            "left join container_sub csb on a.container_no = csb.container_no\n" +
            " where csb.commodity_id in (${goodsId}) ) ct\n " +
            "left join sx_store st on ct.container_no = st.container_no\n" +
            "left join sx_store_location stl on st.store_location_id = stl.id\n" +
            "where stl.layer>0 group by stl.layer")
	List<LayerContainerCountDto> findContainerCountByGoods(@Param("goodsId") String goodsIds);

    @Select("SELECT\n" +
            "\tss.layer AS layer,\n" +
            "\tcount( ss.layer ) AS containerCount \n" +
            "FROM\n" +
            "\tsx_store s\n" +
            "\tJOIN sx_store_location ss ON s.store_location_id = ss.id\n" +
            "\tJOIN sx_layer sa ON sa.layer = ss.layer \n" +
            "GROUP BY\n" +
            "\tss.layer")
    @ResultMap("layerContainerCountDtoMap")
    List<LayerContainerCountDto> findContainerCount();
	
}
