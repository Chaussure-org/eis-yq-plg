package com.prolog.eis.service;

import com.prolog.eis.dto.apprf.AppInstockOrderCcceptanceDto;
import com.prolog.eis.model.apprf.AppGoodsModel;

import java.util.List;

/**
 * @title 入库取消管理
 * @author jinxf
 * @time 2020/4/28 17:06
 */
public interface AppInstockOrderCcceptanceService {

	public List<AppInstockOrderCcceptanceDto> query(String containerNo) throws Exception;

	public void cancel(String containerNo, String containerSubNo) throws Exception;

	public AppGoodsModel goodsQuery(String goodsNo) throws Exception;

	public String boxQuery(String containerNo, String userId) throws Exception;

	public void add(String containerNo, String containerSubNo, String goodsId, String expiryDate, String goodsNum, String inboundTime, String referenceNum) throws Exception;
}
