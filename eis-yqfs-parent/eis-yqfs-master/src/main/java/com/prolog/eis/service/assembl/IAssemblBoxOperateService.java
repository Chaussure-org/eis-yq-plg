package com.prolog.eis.service.assembl;

import com.prolog.eis.model.assembl.AssemblBoxOperate;

public interface IAssemblBoxOperateService {
    /**
     * 保存操作记录
     */
    void saveAssemblBoxOperate(String sourceContainerSubNo, String targetContainerSubNo, int commodityNum) throws Exception;
}
