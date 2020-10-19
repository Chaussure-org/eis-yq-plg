package com.prolog.eis.dao.tk;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.pddispatch.PanDianTaskCountDto;
import com.prolog.eis.dto.tkdispatch.TuiKuCengDto;
import com.prolog.eis.dto.tkdispatch.TuiKuLxDto;

public interface TkEngineeInitMapper {
	
	@Select("select c.layer as ceng from sx_layer c where c.lock_state = 0")
	List<TuiKuCengDto> findCeng();

	
	@Select("select sl.layer as ceng  ,count(*) as taskCount from sx_store sx \r\n" + 
			"INNER JOIN sx_store_location sl on sl.id = sx.STORE_LOCATION_ID \r\n" + 
			"where sx.STORE_STATE = 10 GROUP BY sl.layer")
	public List<PanDianTaskCountDto> findRkTaskCount();
	
	@Select("select sl.layer as ceng  ,count(*) as taskCount from sx_store sx \r\n" + 
			"INNER JOIN sx_store_location sl on sl.id = sx.STORE_LOCATION_ID \r\n" + 
			"where sx.STORE_STATE = 30 GROUP BY sl.layer")
	public List<PanDianTaskCountDto> findCkTaskCount();

	@Select("select sx.CONTAINER_NO as lxNo ,sl.id as huoWeiId ,sl.store_location_group_id as huoWeiGroupId ,sl.layer as ceng,\r\n" +
			"			sl.x as huoWeiX , sl.y as huoWeiY ,sl.depth as depth,sl.dept_num as moveNum from sx_store sx \r\n" + 
			"			inner join sx_store_location sl on sl.id = sx.STORE_LOCATION_ID\r\n" + 
			"			inner join sx_store_location_group slg on slg.id = sl.store_location_group_id \r\n" + 
			"			where slg.ASCENT_LOCK_STATE = 0 and sx.TASK_TYPE = 50 and slg.IS_LOCK = 0 " +
			"and sl.id not in (select ssr2.location_child_id from sx_store_location_relation ssr2 where ssr2.location_lock = 1)")
	List<TuiKuLxDto> findLxListDetail();
}
