package com.prolog.eis.service.masterbase;

import java.util.List;

import com.prolog.eis.model.masterbase.OrderBoxInfo;


public interface OrderBoxInfoService {

	
	// 查询全部订单箱信息
	public List<OrderBoxInfo> findAllOrderBoxInfo() throws Exception;

	// 保存订单箱信息
	public void saveOrderBoxInfo(int  orderFirst , int orderLast) throws Exception;

	//删除订单箱
	public void deleteOrderBoxInfo(List<String> orderBoxInfos)throws Exception;
	

}
