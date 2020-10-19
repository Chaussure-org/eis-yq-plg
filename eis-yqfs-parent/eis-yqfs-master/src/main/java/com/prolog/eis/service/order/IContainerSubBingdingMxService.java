package com.prolog.eis.service.order;

import com.prolog.eis.model.order.ContainerBindingHz;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.framework.core.restriction.Criteria;

import java.util.List;
import java.util.Map;

/**
 * @Author wangkang
 * @Description 货格绑定明细
 * @CreateTime 2020/6/20 11:33
 */
public interface IContainerSubBingdingMxService {
    List<ContainerSubBindingMx> findByMap(Map map);
    void update(ContainerSubBindingMx containerSubBindingMx);
    List<ContainerSubBindingMx> findMxAsc(String containerSubNo);
    List<ContainerSubBindingMx> findMxByContainerNoAsc(String containerNo);
    List<ContainerSubBindingMx> findMxByContainerNoDesc(String containerNo);
    void deleteBatch(List<ContainerSubBindingMx> list);
    void delete(ContainerSubBindingMx containerSubBindingMx);
    List<ContainerSubBindingMx> findByCriteria(Criteria ctr);

    void setPicking(int containerSubBindingMxId,int stationId);

}
