package com.prolog.eis.service.masterbase;


import com.prolog.eis.dto.store.LayerContainerCountDto;
import com.prolog.eis.model.masterbase.Container;
import com.prolog.eis.model.masterbase.ContainerSub;

import java.util.List;
import java.util.Map;

public interface ContainerService {

    /**
     * 生成容器编号
     * @param startNo  开始编号
     * @param endNo   结束编号
     * @param layoutType  料箱类型
     */
    void createContainerNo(Integer startNo,Integer endNo,Integer layoutType) throws Exception;

    List<Container> findByMap(Map map);

    /**
     * 获取每层的料箱数
     * @return
     */
    List<LayerContainerCountDto> getContainerCountPerLayer();

    /**
     * 获取指定层的料箱数
     * @param goodsIds
     * @return
     */
    List<LayerContainerCountDto> getContainerCountPerLayer(int[] goodsIds);

    /**
     * 根据料箱号获取货格信息
     * @param containerNo
     * @return
     */
    List<ContainerSub> getContainerSubs(String containerNo);


}
