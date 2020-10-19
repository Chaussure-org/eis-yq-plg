package com.prolog.eis.service.pickorder;

import com.prolog.eis.pickstation.model.PickOrder;

/**
 * @Author wangkang
 * @Description 拣选单历史服务
 * @CreateTime 2020/6/20 15:40
 */
public interface IPickOrderHistoryService {
    void toHistory(PickOrder pickOrder)throws Exception;
}
