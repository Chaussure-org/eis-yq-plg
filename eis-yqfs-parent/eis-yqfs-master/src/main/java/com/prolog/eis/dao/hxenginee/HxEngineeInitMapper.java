package com.prolog.eis.dao.hxenginee;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.hxdispatch.HxStationDto;
import com.prolog.eis.dto.hxdispatch.StationLxCountDto;

public interface HxEngineeInitMapper {

	@Select("select s.id as zhanTaiId ,s.is_lock as isLock ,s.is_claim as isClaim ,\r\n" + 
			"s.lx_max_count as maxLxCacheCount from station s where s.station_task_type  = 3")
	List<HxStationDto> findStation();

	@Select("select zt.station as stationId ,count(*) as lxCount from zt_store zt where zt.zt_state in (1,2) GROUP BY zt.station")
	List<StationLxCountDto> findArriveLxCount();

	@Select("select sx.station_id as stationId ,count(*) as lxCount from sx_store sx \r\n" + 
			"INNER JOIN sx_store_location sl on sl.id = sx.STORE_LOCATION_ID\r\n" + 
			"where sx.TASK_TYPE = 6 and sx.STORE_STATE in (20,30) and sl.task_lock = 1 GROUP BY sx.station_id")
	List<StationLxCountDto> findChuKuLxCount();

}
