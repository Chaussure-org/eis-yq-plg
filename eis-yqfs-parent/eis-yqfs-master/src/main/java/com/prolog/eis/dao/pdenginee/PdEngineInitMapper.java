package com.prolog.eis.dao.pdenginee;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.pddispatch.PanDianCengDto;
import com.prolog.eis.dto.pddispatch.PanDianLxDto;
import com.prolog.eis.dto.pddispatch.PanDianStationDto;
import com.prolog.eis.dto.pddispatch.PanDianStationLxDto;
import com.prolog.eis.dto.pddispatch.PanDianTaskCountDto;
import com.prolog.eis.dto.pddispatch.PanDianXiangKuDto;

public interface PdEngineInitMapper {

	@Select("select distinct sl.layer as ceng from sx_store_location sl ")
	List<PanDianCengDto> findPDCeng();
	
	@Select("select sl.layer as ceng  ,count(*) as taskCount from sx_store sx \r\n" + 
			"INNER JOIN sx_store_location sl on sl.id = sx.STORE_LOCATION_ID \r\n" + 
			"where sx.STORE_STATE = 10 GROUP BY sl.layer")
	List<PanDianTaskCountDto> findRkTaskCount();
	
	@Select("select sl.layer as ceng  ,count(*) as taskCount from sx_store sx \r\n" + 
			"INNER JOIN sx_store_location sl on sl.id = sx.STORE_LOCATION_ID \r\n" + 
			"where sx.STORE_STATE = 30 GROUP BY sl.layer")
	List<PanDianTaskCountDto> findCkTaskCount();
	
	@Select("select sx.CONTAINER_NO as lxNo ,sl.id as huoWeiId ,sl.store_location_group_id as huoWeiGroupId ,sl.layer as ceng ,\r\n" + 
			"sl.x as huoWeiX , sl.y as huoWeiY ,sl.depth as depth,sl.dept_num as moveNum from sx_store sx \r\n" + 
			"inner join sx_store_location sl on sl.id = sx.STORE_LOCATION_ID\r\n" + 
			"inner join sx_store_location_group slg on slg.id = sl.store_location_group_id \r\n" + 
			"where slg.ASCENT_LOCK_STATE = 0 and sx.TASK_TYPE = 40 and slg.IS_LOCK = 0 " +
			"and sl.id not in (select ssr.location_parent_id from sx_store_location_relation ssr where ssr.location_lock = 1 union select ssr2.location_child_id from sx_store_location_relation ssr2 where ssr2.location_lock = 1)")
	List<PanDianLxDto> findLxDetail();

	@Select("select s.id as stationId ,s.lx_max_count as maxLxCount from station s where s.IS_CLAIM = 1 and s.IS_LOCK = 2 and s.STATION_TASK_TYPE  = 40 ")
	List<PanDianStationDto> findStation();
	
	@Select("select sx.station_id as stationId ,sx.CONTAINER_NO as boxNo from sx_store sx where sx.TASK_TYPE = 40  and sx.STORE_STATE = 30 \r\n" +
			"union select zt.station_id as stationId , zt.CONTAINER_NO as boxNo from zt_store zt where zt.task_type = 40 and (zt.status = 1 or zt.status = 2)")
	List<PanDianStationLxDto> findStationLx();

}
