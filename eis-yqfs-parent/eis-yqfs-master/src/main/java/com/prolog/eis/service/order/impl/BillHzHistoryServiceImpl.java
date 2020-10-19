package com.prolog.eis.service.order.impl;

import com.prolog.eis.dao.order.BillHzHistoryMapper;
import com.prolog.eis.model.order.BillHz;
import com.prolog.eis.model.order.BillHzHistory;
import com.prolog.eis.service.order.IBillHzHistoryService;
import com.prolog.eis.service.order.IBillHzService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangkang
 * @Description 实现类
 * @CreateTime 2020/7/10 18:55
 */
@Service
public class BillHzHistoryServiceImpl implements IBillHzHistoryService {

    @Autowired
    private IBillHzService billHzService;

    @Autowired
    private BillHzHistoryMapper mapper;

    @Override
    public void toHistory(List<BillHz> billHzList) {
        List<BillHzHistory> list = new ArrayList<>();
        for (BillHz billHz : billHzList) {
            BillHzHistory billHzHistory = new BillHzHistory();
            BeanUtils.copyProperties(billHz,billHzHistory);
            list.add(billHzHistory);
        }
        mapper.saveBatch(list);
        billHzService.deleteBatch(billHzList);
    }
}
