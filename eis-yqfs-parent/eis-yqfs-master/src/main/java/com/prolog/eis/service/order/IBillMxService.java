package com.prolog.eis.service.order;

import com.prolog.eis.model.order.BillMx;

import java.util.List;

/**
 * @Author wangkang
 * @Description 清单明细服务接口
 * @CreateTime 2020/7/10 17:11
 */
public interface IBillMxService {

    List<BillMx> getBillMx(int billHzId);

    void deleteBatch(List<BillMx> list);

    List<BillMx> getAll();
}
