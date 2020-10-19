package com.prolog.eis.service.order.impl;

import com.prolog.eis.dao.order.BillMxHistoryMapper;
import com.prolog.eis.model.order.BillMx;
import com.prolog.eis.model.order.BillMxHistory;
import com.prolog.eis.service.order.IBillMxHistoryService;
import com.prolog.eis.service.order.IBillMxService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangkang
 * @Description 实现类
 * @CreateTime 2020/7/10 19:04
 */
@Service
public class BillMxHistoryServiceImpl implements IBillMxHistoryService {

    @Autowired
    private IBillMxService billMxService;

    @Autowired
    private BillMxHistoryMapper mapper;

    @Override
    public void toHistory(List<BillMx> billMxList) {
        List<BillMxHistory> list = new ArrayList<>();
        for (BillMx billMx : billMxList) {
            BillMxHistory billMxHistory = new BillMxHistory();
            BeanUtils.copyProperties(billMx,billMxHistory);
            list.add(billMxHistory);
        }
        mapper.saveBatch(list);
        billMxService.deleteBatch(billMxList);
    }
}
