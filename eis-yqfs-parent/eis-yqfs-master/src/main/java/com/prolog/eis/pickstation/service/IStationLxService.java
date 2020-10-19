package com.prolog.eis.pickstation.service;


import com.prolog.eis.pickstation.dto.ContainerLocationDto;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.wcs.dto.BCRDataDTO;
import com.prolog.eis.wcs.dto.BoxCallbackDTO;
import com.prolog.framework.common.message.RestMessage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface IStationLxService {


	void lxIn(String containerNo, String deriction, StationLxPosition position) throws Exception;

	void lxInOrOut(int stationId,String containerNo,String address,boolean isIn) throws Exception;

	/**
	 * 申请入站
	 * @return
	 */
	void lxApplyFor(BCRDataDTO bcrDataDTO) throws Exception;
	
	/**
	 * 料箱离开
	 * @param lxNo
	 * @throws Exception
	 */
	void lxLeave(String lxNo, int stationId) throws Exception;
	
	/**
	 * 料箱到位
	 * @throws Exception
	 */
	void lxInPlace(int stationId,String containerNo,String address) throws Exception;

	/**
	 * 验证是否发送播种信息
	 * @throws Exception
	 */
	void checkBZStatus(Station station) throws Exception;

	/**
	 * 拍灯后播种逻辑
	 * @param stationId
	 * @throws Exception
	 */
	void seedLogic(int stationId,String lightNo) throws Exception;

	/**
	 *	发送下一条播种信息
	 * @param
	 */
	void sendNextSeed(String containerDirection,String containerNo, int stationId,String containerSubNo)throws Exception;

	/**
	 * 检测订单状态
	 * @param orderId
	 * @param leave
	 * @throws Exception
	 */
	public void checkOrderStatus(int orderId,boolean leave) throws Exception;

	/**
	 * 初始化页面数据
	 * @param 
	 */
	void saveStationLx(StationLxPosition stationLxPosition);
	long countData();
	void deleteAll();
	void addBath(List<StationLxPosition> list);
	List<StationLxPosition> findByMap(Map map);

	/**
	 * 寻找空料箱位
	 * @return
	 */
	List<StationLxPosition> getEmptyPositions();

	/**
	 * 更新position
	 * @param position
	 */
	void update(StationLxPosition position);

	/**
	 * 根据satationId获取料箱位信息
	 * @param stationId
	 * @return
	 */
	List<StationLxPosition> getByStationId(int stationId);

	void updateStationLxByContainer(String container) throws Exception;

	void cuttingBoxOp(int pickOrderId) throws Exception;
}
