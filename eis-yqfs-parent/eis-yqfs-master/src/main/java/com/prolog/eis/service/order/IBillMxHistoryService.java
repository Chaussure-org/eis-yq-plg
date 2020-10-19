package com.prolog.eis.service.order;

import com.prolog.eis.model.order.BillMx;

import java.util.List;

/**
 * @Author wangkang
 * @Description 清单明细服务类
 * @CreateTime 2020/7/10 18:54
 */
public interface IBillMxHistoryService {
    void toHistory(List<BillMx> billMxList);
}
