package com.prolog.eis.service;

import java.util.HashMap;
import java.util.List;

/**
 * @author jinxf
 */
public interface AppOutstockOrderRecheckService {
	public List<HashMap<String, Object>> query(String outboundCode) throws Exception;
}
