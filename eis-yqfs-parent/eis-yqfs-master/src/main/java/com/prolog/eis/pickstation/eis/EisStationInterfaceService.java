package com.prolog.eis.pickstation.eis;


import com.prolog.eis.pickstation.dto.LxDetailsDto;

public interface EisStationInterfaceService {

	/**
	 * 获取料箱信息
	 * @param LxNo
	 * @throws Exception
	 */
	public LxDetailsDto queryLxDetails(String LxNo)throws Exception;
}
