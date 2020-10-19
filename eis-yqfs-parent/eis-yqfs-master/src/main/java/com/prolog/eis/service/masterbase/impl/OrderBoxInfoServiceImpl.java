package com.prolog.eis.service.masterbase.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prolog.eis.dao.masterbase.OrderBoxInfoMapper;
import com.prolog.eis.model.masterbase.OrderBoxInfo;
import com.prolog.eis.service.masterbase.OrderBoxInfoService;

@Service
public class OrderBoxInfoServiceImpl implements OrderBoxInfoService{
	
	@Autowired
	private OrderBoxInfoMapper orderBoxInfoMapper;
	

	@Override
	public List<OrderBoxInfo> findAllOrderBoxInfo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrderBoxInfo(int orderFirst , int orderLast) throws Exception {
		ArrayList<OrderBoxInfo> orderBoxInfos = new ArrayList<>();
		int j = orderLast;
		for (int i= orderFirst;i<=j;i++){
		   OrderBoxInfo orderBoxInfo = new OrderBoxInfo();
		   orderBoxInfo.setOrderBoxNo(String.format("%08d",i));
		   orderBoxInfos.add(orderBoxInfo);
		}
		orderBoxInfoMapper.saveBatch(orderBoxInfos);
	}
  
	@Override
	public void deleteOrderBoxInfo(List<String> orderBoxInfos) throws Exception {
		
		for (String orderBoxNo : orderBoxInfos) {
			
			OrderBoxInfo or = orderBoxInfoMapper.findById(orderBoxNo, OrderBoxInfo.class);
			
			if ( or == null ) {
				
				throw new Exception(orderBoxNo+"订单箱号不存在");
			}else if (or.getOutBoundOrderId() != 0) {
				throw new Exception(orderBoxNo+"订单箱有出库任务，不能删除");
			}else {
				orderBoxInfoMapper.deleteById(orderBoxNo, OrderBoxInfo.class);
			}
		
			
		}
		
		 
		
	}


}
