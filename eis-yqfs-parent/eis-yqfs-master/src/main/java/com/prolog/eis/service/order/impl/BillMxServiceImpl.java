package com.prolog.eis.service.order.impl;

import com.prolog.eis.dao.order.BillMxMapper;
import com.prolog.eis.model.order.BillMx;
import com.prolog.eis.service.order.IBillMxService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wangkang
 * @Description 实现类
 * @CreateTime 2020/7/10 17:12
 */
@Service
public class BillMxServiceImpl implements IBillMxService {

    @Autowired
    private BillMxMapper mapper;
    @Override
    public List<BillMx> getBillMx(int billHzId) {
        return mapper.findByMap(MapUtils.put("billHzId",billHzId).getMap(),BillMx.class);
    }

    @Override
    public void deleteBatch(List<BillMx> list) {
        for (BillMx billMx : list) {
            mapper.deleteById(billMx.getId(),BillMx.class);
        }
    }

    @Override
    public List<BillMx> getAll() {
        return mapper.findByMap(null,BillMx.class);
    }
}
