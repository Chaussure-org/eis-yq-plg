package com.prolog.eis.service;
import com.prolog.eis.model.apprf.AppContainerModel;
import com.prolog.eis.model.apprf.AppContainerSubModel;
import com.prolog.framework.common.message.RestMessage;

/**
 * @author jinxf
 */
public interface AppCargoCaseValidationService {

	public AppContainerModel query(String containerNo) throws Exception;

	public RestMessage<AppContainerSubModel> save(String containerNo, String containerSubNos, String type) throws Exception;

}
