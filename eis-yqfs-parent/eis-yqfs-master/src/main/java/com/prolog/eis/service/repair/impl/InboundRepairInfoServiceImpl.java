package com.prolog.eis.service.repair.impl;

import com.prolog.eis.dao.repair.InboundRepairInfoMapper;
import com.prolog.eis.model.repair.InboundRepairInfo;
import com.prolog.eis.service.repair.InboundRepairInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InboundRepairInfoServiceImpl implements InboundRepairInfoService {

    @Autowired
    private InboundRepairInfoMapper inboundRepairInfoMapper;
    @Override
    public void saveBatch(List<InboundRepairInfo> inboundRepairInfos) {
        inboundRepairInfoMapper.saveBatch(inboundRepairInfos);
    }

    @Override
    public void save(InboundRepairInfo inboundRepairInfo) {
        inboundRepairInfoMapper.save(inboundRepairInfo);
    }
}
