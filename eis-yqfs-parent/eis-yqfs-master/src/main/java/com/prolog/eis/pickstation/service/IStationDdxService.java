package com.prolog.eis.pickstation.service;


import com.prolog.eis.pickstation.dto.ContainerLocationDto;
import com.prolog.eis.pickstation.model.StationOrderPosition;
import com.prolog.eis.wcs.dto.BCRDataDTO;
import com.prolog.eis.wcs.dto.BoxCallbackDTO;

import java.util.List;
import java.util.Map;

public interface IStationDdxService {

	/**
	 * 检查订单箱状态
	 * @param
	 * @return
	 */
	boolean checkDdxStatus(BCRDataDTO bcrDataDTO) throws Exception;
	
	/**
	 * 订单箱离开
	 * @throws Exception
	 */
	void ddxLeave(String ddxNo, int stationId) throws Exception;
	
	/**
	 * 修改空闲位置状态
	 * @return
	 * @throws Exception
	 */
	List<StationOrderPosition> updateStationOrposition() throws Exception;
	
	/**
	 * 订单箱到位
	 * @throws Exception
	 */
	void ddxInplace(BoxCallbackDTO boxCallbackDTO)throws Exception;
	void addBath(List<StationOrderPosition> list)throws Exception;
	void saveStationOrder(StationOrderPosition stationOrderPosition);
	long countData();
	void deleteAll();
	List<StationOrderPosition> getAll();
	List<StationOrderPosition> findByMap(Map map);

	/**
	 * 订单箱截箱操作
	 */
	void changeOrderBox(String orderBoxNo, int positionNo, int num)throws Exception;

	/**
	 * 根据站台获取
	 * @param stationIds
	 * @return
	 */
	List<StationOrderPosition> getByStationIds(List<Integer> stationIds);

	/**
	 * 获取空订单箱位
	 * @return
	 */
	List<StationOrderPosition> getEmpty();

	void update(StationOrderPosition stationOrderPosition);

}
