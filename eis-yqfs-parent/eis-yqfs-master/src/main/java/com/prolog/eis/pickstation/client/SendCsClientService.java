package com.prolog.eis.pickstation.client;

import com.prolog.eis.pickstation.dto.SendLxDto;

import java.util.List;

public interface SendCsClientService {

	/**
	 * 推送料箱信息
	 * @param containerNo
	 * @throws Exception
	 */
	public void sendLxDetails(String containerNo,String locationNo,int stationId)throws Exception;
	
	/**
	 * 推送订单箱信息
	 * @param containerNo
	 * @throws Exception
	 */
	public void sendDdxDetails(String containerNo,String locationNo,int stationId)throws Exception;

	/**
	 * 当前拣选商品汇总消息
	 * @param containerNo
	 * @throws Exception
	 */
	public void sendCurrentStoreHz(int pickId,String containerNo,String containSubNo,int stationId)throws Exception;

	/**
	 * 当前播种消息
	 * @param containerNo
	 * @throws Exception
	 */
	public void sendCurrentSeed(String containerNo,String containSubNo,int stationId,String locationNo)throws Exception;


}
