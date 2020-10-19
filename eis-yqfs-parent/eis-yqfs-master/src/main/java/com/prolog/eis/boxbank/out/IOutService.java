package com.prolog.eis.boxbank.out;

import com.prolog.eis.wcs.dto.TaskCallbackDTO;

public interface IOutService {

    boolean checkOut(String lxNo,int taskType,int stationId) throws Exception;
}
