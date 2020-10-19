package com.prolog.eis.service.repair;

import com.prolog.eis.model.repair.InboundRepairInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface InboundRepairInfoService {

    void  saveBatch(List<InboundRepairInfo> inboundRepairInfos);


    void  save(InboundRepairInfo inboundRepairInfo);
}
