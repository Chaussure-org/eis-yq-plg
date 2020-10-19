package com.prolog.eis.service.order.impl;

import com.prolog.eis.dao.order.BillHzMapper;
import com.prolog.eis.model.order.BillHz;
import com.prolog.eis.service.order.IBillHzService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wangkang
 * @Description 实现类
 * @CreateTime 2020/7/11 12:36
 */
@Service
public class BillHzServiceImpl implements IBillHzService {

    @Autowired
    private BillHzMapper mapper;

    @Override
    public List<BillHz> getBillHz() {
        return mapper.findByMap(null,BillHz.class);
    }

    @Override
    public List<BillHz> getBillHz(String waveNo) {
        return mapper.findByMap(MapUtils.put("waveNo",waveNo).getMap(),BillHz.class);
    }

    @Override
    public List<BillHz> getBillHz(int dealerId, String waveNo) {
        return mapper.findByMap(MapUtils.put("waveNo",waveNo).put("dealerId",dealerId).getMap(),BillHz.class);
    }

    @Override
    public void deleteBatch(List<BillHz> billHzList) {
        for (BillHz billHz : billHzList) {
            mapper.deleteById(billHz.getId(),BillHz.class);
        }
    }
}
