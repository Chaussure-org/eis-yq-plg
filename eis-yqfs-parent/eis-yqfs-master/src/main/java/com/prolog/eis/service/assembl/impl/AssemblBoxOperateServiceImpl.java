package com.prolog.eis.service.assembl.impl;

import com.prolog.eis.dao.assembl.AssemblBoxMxMapper;
import com.prolog.eis.dao.assembl.AssemblBoxOperateMapper;
import com.prolog.eis.model.assembl.AssemblBoxOperate;
import com.prolog.eis.pickstation.dao.StationLxMapper;
import com.prolog.eis.pickstation.dao.StationMapper;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.service.assembl.IAssemblBoxOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class AssemblBoxOperateServiceImpl implements IAssemblBoxOperateService {
    @Autowired
    private AssemblBoxOperateMapper assemblBoxOperateMapper;
    @Autowired
    private StationLxMapper stationLxMapper;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private AssemblBoxMxMapper assemblBoxMxMapper;
    @Override
    public void saveAssemblBoxOperate(String sourceContainerSubNo, String targetContainerSubNo, int commodityNum) throws Exception {
        AssemblBoxOperate assemblBoxOperate = new AssemblBoxOperate();
        int stationId = stationLxMapper.getStationIdByContainerSubNo(sourceContainerSubNo);
        Station station = stationMapper.findById(stationId, Station.class);
        assemblBoxOperate.setAssemblBoxHzId(assemblBoxMxMapper.getAssemblBoxMxDetail(sourceContainerSubNo.substring(0,sourceContainerSubNo.length() - 1)));
        assemblBoxOperate.setCreateTime(new Date());
        assemblBoxOperate.setGoodsNum(commodityNum);
        assemblBoxOperate.setOperatorStaff(station.getLoginUsername());
        assemblBoxOperate.setStationNo(station.getStationNo());
        assemblBoxOperate.setSourceSubContainerNo(sourceContainerSubNo);
        assemblBoxOperate.setTargetSubContainerNo(targetContainerSubNo);
        assemblBoxOperateMapper.save(assemblBoxOperate);
    }
}
