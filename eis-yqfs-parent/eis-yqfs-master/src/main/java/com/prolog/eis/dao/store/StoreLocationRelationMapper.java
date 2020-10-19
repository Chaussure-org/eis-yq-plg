package com.prolog.eis.dao.store;

import com.prolog.eis.model.store.SxStoreLocationRelation;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface StoreLocationRelationMapper extends BaseMapper<SxStoreLocationRelation> {

	@Update("update sx_store_location_relation ssr set ssr.location_lock = 1 WHERE ssr.location_parent_id = #{locationId}")
	long lockLocation(@Param("locationId") Integer locationId);

	@Update("update sx_store_location_relation ssr set ssr.location_lock = 0 WHERE ssr.location_parent_id = #{locationId}")
	long unLockLocation(@Param("locationId") Integer locationId);
}
