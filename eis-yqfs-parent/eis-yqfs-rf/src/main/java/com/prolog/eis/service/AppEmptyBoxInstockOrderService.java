package com.prolog.eis.service;
import com.prolog.eis.model.apprf.AppEmptyContainerInboundTaskModel;

/**
 * @author jinxf
 */
public interface AppEmptyBoxInstockOrderService {

	public AppEmptyContainerInboundTaskModel add(String containerNo) throws Exception;

	public void cancel(String containerNo) throws Exception;
}
