package com.prolog.eis.boxbank.out;

import com.prolog.eis.dto.hxdispatch.HxXiangKuDto;

public interface HXEnginee {
	/**
	 * 初始化数据
	 * @return
	 */
	HxXiangKuDto init();
	/**
	 * 合箱计划绑定站台
	 * @param hzId    计划汇总id
	 * @param stationId  站台id
	 */
	void hzBindStation(int hzId,int stationId) throws Exception;

	boolean chuku(String lxNo,int stationId)throws Exception;
	
}
