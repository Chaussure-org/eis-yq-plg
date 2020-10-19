package com.prolog.eis.boxbank.out.impl;

import com.prolog.eis.boxbank.out.BHEnginee;
import com.prolog.eis.boxbank.out.IOutService;
import com.prolog.eis.dao.bhenginee.BhEngineeInitMapper;
import com.prolog.eis.dto.bhdispatch.BuHuoCengDto;
import com.prolog.eis.dto.bhdispatch.BuHuoLxDto;
import com.prolog.eis.dto.bhdispatch.BuHuoXiangKuDto;
import com.prolog.eis.dto.pddispatch.PanDianTaskCountDto;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.util.ListHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class BHEngineeImpl implements BHEnginee {
    private final Logger logger = LoggerFactory.getLogger(BHEnginee.class);

    @Autowired
    private BhEngineeInitMapper bhEngineeInitMapper;
    @Autowired
    private IOutService outService;



    @Override
    public BuHuoXiangKuDto init() throws Exception {
        BuHuoXiangKuDto buHuoXiangKuDto = new BuHuoXiangKuDto();
        List<BuHuoCengDto> findBHCeng = bhEngineeInitMapper.findBHCeng();
        //查找每层库存状态为出库状态的货位数
        List<PanDianTaskCountDto> findCkTaskCount = bhEngineeInitMapper.findCkTaskCount();
        ///查找每层库存状态为入库状态的货位数
        List<PanDianTaskCountDto> findRkTaskCount = bhEngineeInitMapper.findRkTaskCount();

        List<BuHuoLxDto> lxList = bhEngineeInitMapper.findLxListDetail();
        Map<Integer, List<BuHuoLxDto>> buildGroupDictionary = ListHelper.buildGroupDictionary(lxList, p->p.getCeng());
        for (BuHuoCengDto ceng : findBHCeng) {
            for (PanDianTaskCountDto findCkTask : findCkTaskCount) {
                if (ceng.getCeng() == findCkTask.getCeng()) {
                    ceng.setCkLxCount(findCkTask.getTaskCount());
                }
            }
            for (PanDianTaskCountDto findRkTask : findRkTaskCount) {
                if (ceng.getCeng() == findRkTask.getCeng()) {
                    ceng.setRkLxCount(findRkTask.getTaskCount());
                }
            }
            List<BuHuoLxDto> lxDtos = buildGroupDictionary.get(ceng.getCeng());
            if (lxDtos != null && lxDtos.size() > 0) {
                ceng.setLxList(lxDtos);
            }
        }
        Iterator<BuHuoCengDto> it = findBHCeng.iterator();
        while (it.hasNext()) {
            BuHuoCengDto findlxCeng = it.next();
            if(findlxCeng.getLxList().size() == 0 || findlxCeng.getLxList()==null) {
                it.remove();
            }
        }
        buHuoXiangKuDto.setCengList(findBHCeng);
        return buHuoXiangKuDto;
    }



    @Override
    @Transactional
    public boolean chuku(BuHuoLxDto buHuoLxDto) throws Exception {
        return outService.checkOut(buHuoLxDto.getLxNo(),SxStore.TASKTYPE_BH,0);
    }

}
