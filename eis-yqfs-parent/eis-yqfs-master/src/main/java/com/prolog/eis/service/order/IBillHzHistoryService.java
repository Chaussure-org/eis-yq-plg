package com.prolog.eis.service.order;

import com.prolog.eis.model.order.BillHz;

import java.util.List;

/**
 * @Author wangkang
 * @Description 清单历史服务
 * @CreateTime 2020/7/10 18:52
 */
public interface IBillHzHistoryService {
    void toHistory(List<BillHz> billHzList);
}
