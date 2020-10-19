package com.prolog.eis.service.order;

import com.prolog.eis.model.order.BillHz;

import java.util.List;

/**
 * @Author wangkang
 * @Description 清单服务接口
 * @CreateTime 2020/7/10 17:10
 */
public interface IBillHzService {

    List<BillHz> getBillHz();
    List<BillHz> getBillHz(String waveNo);
    List<BillHz> getBillHz(int dealerId,String waveNo);
    void deleteBatch(List<BillHz> billHzList);
}
