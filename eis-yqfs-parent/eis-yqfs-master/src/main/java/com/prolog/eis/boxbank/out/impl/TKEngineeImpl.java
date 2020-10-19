package com.prolog.eis.boxbank.out.impl;

import com.prolog.eis.boxbank.out.IOutService;
import com.prolog.eis.boxbank.out.TKEnginee;
import com.prolog.eis.dao.tk.TkEngineeInitMapper;
import com.prolog.eis.dto.pddispatch.PanDianTaskCountDto;
import com.prolog.eis.dto.tkdispatch.TuiKuCengDto;
import com.prolog.eis.dto.tkdispatch.TuiKuLxDto;
import com.prolog.eis.dto.tkdispatch.TuiKuXiangKuDto;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.service.store.IStoreService;
import com.prolog.eis.service.tk.TkTaskService;
import com.prolog.eis.util.ListHelper;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.dto.TaskCallbackDTO;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class TKEngineeImpl implements TKEnginee {
    @Autowired
    private TkEngineeInitMapper tkEngineeInitMapper;
    @Autowired
    private IOutService outService;
    @Autowired
    private TkTaskService tkTaskService;



    @Override
    public TuiKuXiangKuDto init() throws Exception {
        TuiKuXiangKuDto tuiKuXiangKuDto = new TuiKuXiangKuDto();
        List<TuiKuCengDto> cengList = tkEngineeInitMapper.findCeng();

        List<PanDianTaskCountDto> findCkTaskCount = tkEngineeInitMapper.findCkTaskCount();
        List<PanDianTaskCountDto> findRkTaskCount = tkEngineeInitMapper.findRkTaskCount();
        List<TuiKuLxDto> lxList = tkEngineeInitMapper.findLxListDetail();
        Map<Integer, List<TuiKuLxDto>> buildGroupDictionary = ListHelper.buildGroupDictionary(lxList, p -> p.getCeng());
        for (TuiKuCengDto ceng : cengList) {
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
            List<TuiKuLxDto> lxDtos = buildGroupDictionary.get(ceng.getCeng());
            if (lxDtos != null && lxDtos.size() > 0) {
                ceng.setLxList(lxDtos);
            }
        }
        Iterator<TuiKuCengDto> it = cengList.iterator();
        while (it.hasNext()) {
            TuiKuCengDto findtkCeng = it.next();
            if(findtkCeng.getLxList()==null || findtkCeng.getLxList().size() == 0) {
                it.remove();
            }
        }
        tuiKuXiangKuDto.setCengList(cengList);
        return tuiKuXiangKuDto;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean chuku(TuiKuLxDto tuiKuLxDto) throws Exception {
        boolean success = outService.checkOut(tuiKuLxDto.getLxNo(),SxStore.TASKTYPE_TK,0);
        if(success){
            tkTaskService.processTask(tuiKuLxDto.getLxNo());
        }
        return success;
    }


}
