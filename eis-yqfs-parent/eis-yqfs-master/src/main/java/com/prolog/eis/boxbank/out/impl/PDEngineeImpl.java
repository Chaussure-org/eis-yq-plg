package com.prolog.eis.boxbank.out.impl;

import com.prolog.eis.boxbank.out.IOutService;
import com.prolog.eis.boxbank.out.PDEnginee;
import com.prolog.eis.dao.enginee.EngineLxChuKuMapper;
import com.prolog.eis.dao.pdenginee.PdEngineInitMapper;
import com.prolog.eis.dto.pddispatch.*;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.pickstation.dao.StationMapper;
import com.prolog.eis.util.ListHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PDEngineeImpl implements PDEnginee {

    @Autowired
    private PdEngineInitMapper pdEngineInitMapper;
    @Autowired
    private IOutService outService;

    @Autowired
    private EngineLxChuKuMapper engineLxChuKuMapper;
    @Autowired
    private StationMapper stationMapper;

    @Override
    public PanDianXiangKuDto init() throws Exception {
        PanDianXiangKuDto panDianXiangKuDto = new PanDianXiangKuDto();
        //查找层
        List<PanDianCengDto> findPDCengs = pdEngineInitMapper.findPDCeng();
        //查找每层出库任务数
        List<PanDianTaskCountDto> findCkTaskCounts = pdEngineInitMapper.findCkTaskCount();
        //查找每层入库任务数
        List<PanDianTaskCountDto> findRkTaskCounts = pdEngineInitMapper.findRkTaskCount();

        List<PanDianLxDto> findLxDetails = pdEngineInitMapper.findLxDetail();


        for (PanDianCengDto findPDCeng : findPDCengs) {
            for (PanDianTaskCountDto findCkTaskCount : findCkTaskCounts) {
                if(findPDCeng.getCeng()== findCkTaskCount.getCeng()) {
                    findPDCeng.setCkLxCount(findCkTaskCount.getTaskCount());
                }
            }
            for (PanDianTaskCountDto findRkTaskCount : findRkTaskCounts) {
                if(findPDCeng.getCeng()== findRkTaskCount.getCeng()) {
                    findPDCeng.setRkLxCount(findRkTaskCount.getTaskCount());
                }
            }
            for (PanDianLxDto findLxDetail : findLxDetails) {
                if(findPDCeng.getCeng()== findLxDetail.getCeng()) {
                    findPDCeng.getLxList().add(findLxDetail);
                }
            }
        }
        Iterator<PanDianCengDto> it = findPDCengs.iterator();
        while (it.hasNext()) {
            PanDianCengDto findPDCeng = it.next();
            if(findPDCeng.getLxList().size() == 0 || findPDCeng.getLxList()==null) {
                it.remove();
            }
        }
        panDianXiangKuDto.setCengList(findPDCengs);
        List<PanDianStationDto> findStations = pdEngineInitMapper.findStation();
        List<PanDianStationLxDto> findStationLxs = pdEngineInitMapper.findStationLx();
        Map<Integer, List<PanDianStationLxDto>> pdLxMap = ListHelper.buildGroupDictionary(findStationLxs, p->p.getStationId());
        for (PanDianStationDto findStation : findStations) {
            List<PanDianStationLxDto> list = pdLxMap.get(findStation.getStationId());
            if(list!=null) {
                List<String> select = ListHelper.select(list, p->p.getBoxNo());
                Set<String> setList = new HashSet<String>(select);
                findStation.setPanDianLxNoSet(setList);
            }
        }
        panDianXiangKuDto.setStationList(findStations);

        return panDianXiangKuDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean chuku(PanDianLxDto lx, int stationId) throws Exception {
        return outService.checkOut(lx.getLxNo(),SxStore.TASKTYPE_PD,stationId);
    }
}
