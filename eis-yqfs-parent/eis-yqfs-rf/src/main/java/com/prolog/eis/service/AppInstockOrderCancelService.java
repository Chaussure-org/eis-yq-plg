package com.prolog.eis.service;

import com.prolog.eis.dto.apprf.AppInstockOrderCcceptanceDto;

import java.util.List;

/**
 * @author jinxf
 */
public interface AppInstockOrderCancelService {

	public List<AppInstockOrderCcceptanceDto> query(String containerNo) throws Exception;

	public void cancel(String containerNo) throws Exception;

}
