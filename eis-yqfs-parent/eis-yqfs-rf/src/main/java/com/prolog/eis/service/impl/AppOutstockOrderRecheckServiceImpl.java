package com.prolog.eis.service.impl;

import com.prolog.eis.dao.AppOrderMxMapper;
import com.prolog.eis.service.AppOutstockOrderRecheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
  * @author jinxf
  * @time 2020/4/22 20:01
  */
@Service
public class AppOutstockOrderRecheckServiceImpl implements AppOutstockOrderRecheckService {

	@Autowired
	private AppOrderMxMapper appOutboundTaskMxMapper;

	/**
	 * @title 查询出库复核信息
	 * @author jinxf
	 * @Param: outboundCode 出库编号
	 * @Param: page 当前页
	 * @Param: pageSize 每页数量
	 * @time 2020/4/22 20:02
	 */
	@Override
	public List<HashMap<String, Object>> query(String outboundCode) throws Exception {
		if (StringUtils.isEmpty(outboundCode)) {
			throw new Exception("出库单编号为空！");
		}
//
//		if (StringUtils.isEmpty(page)) {
//			page = "1";
//		}
//
//		if (StringUtils.isEmpty(pageSize)) {
//			pageSize = "10";
//		}
//
//		Integer pageI = 1;
//		Integer pageSizeI = 10;
//
//		try {
//			pageI = Integer.parseInt(page);
//
//			if (pageI < 1) {
//				pageI = 1;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//
//		try {
//			pageSizeI = Integer.parseInt(pageSize);
//
//			if (pageSizeI < 1) {
//				pageSizeI = 10;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			pageSizeI = 10;
//		}
//		pageI = (pageI - 1) * pageSizeI;
		List<HashMap<String, Object>> query = appOutboundTaskMxMapper.query(outboundCode);
		return  query;
	}
}
