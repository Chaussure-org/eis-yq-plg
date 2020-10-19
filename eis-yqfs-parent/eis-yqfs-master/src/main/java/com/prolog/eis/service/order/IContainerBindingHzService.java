package com.prolog.eis.service.order;

import com.prolog.eis.model.order.ContainerBindingHz;
import com.prolog.eis.model.store.ZtStore;

import java.util.List;
import java.util.Map;

/**
 * @Author wangkang
 * @Description 料箱绑定汇总
 * @CreateTime 2020/6/20 11:17
 */
public interface IContainerBindingHzService {
    List<ContainerBindingHz> findByMap(Map map);
    List<Integer> findOrderIdAsc(String containerNo);
    List<Integer> findOrderIdDesc(String containerNo);
    void deleteBatch(List<ContainerBindingHz> list);
    void delete(String containerNo);

    /**
     * 根据拣选单删除绑定汇总（同时删除绑定明细）
     * @param pickOrderId
     */
    void deleteByPickOrderId(int pickOrderId);
}
