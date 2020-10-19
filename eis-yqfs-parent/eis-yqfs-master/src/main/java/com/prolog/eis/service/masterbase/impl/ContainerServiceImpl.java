package com.prolog.eis.service.masterbase.impl;

import com.prolog.eis.dao.masterbase.ContainerMapper;
import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dto.store.LayerContainerCountDto;
import com.prolog.eis.model.masterbase.Container;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prolog.eis.service.masterbase.ContainerService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ContainerServiceImpl implements ContainerService {

    @Autowired
    private ContainerMapper containerMapper;
    @Autowired
    private ContainerSubMapper containerSubMapper;
    @Autowired
    private ContainerSubService containerSubInfoService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createContainerNo(Integer startNo, Integer endNo, Integer layoutType) throws Exception {
        if (startNo < 1){
            startNo = 1;
        }
        List<Container> list = new ArrayList<>();
        for (int i = startNo; i <= endNo ; i++) {
            Container container = new Container();
            String format = String.format("%06d", i);
            container.setContainerNo(Container.CONTAINER_NO_SATRT+format);
            container.setLayoutType(layoutType);
            list.add(container);
        }
        //新增前删除容器
        containerSubMapper.deleteByMap(null, ContainerSub.class);
        containerMapper.deleteByMap(null,Container.class);
        //新增容器
        containerMapper.saveBatch(list);
        //新增子容器
        containerSubInfoService.createContainerSubNo();
    }
    @Override
    public List<Container> findByMap(Map map) {
        return containerMapper.findByMap(map,Container.class);
    }

    /**
     * 获取每层的料箱数
     *
     * @return
     */
    @Override
    public List<LayerContainerCountDto> getContainerCountPerLayer() {
        return containerMapper.findContainerCount();
    }

    /**
     * 获取指定层的料箱数
     *
     * @param goodsIds
     * @return
     */
    @Override
    public List<LayerContainerCountDto> getContainerCountPerLayer(int[] goodsIds) {
        StringBuffer sbf = new StringBuffer();
        for(int i =0 ;i<goodsIds.length;i++){
            sbf.append(goodsIds[i]);
            sbf.append(",");
        }
        String idstr = sbf.toString();
        idstr = idstr.substring(0,idstr.length()-1);
        return containerMapper.findContainerCountByGoods(idstr);
    }

    /**
     * 根据料箱号获取货格信息
     *
     * @param containerNo
     * @return
     */
    @Override
    public List<ContainerSub> getContainerSubs(String containerNo) {
        return containerSubMapper.findByMap(MapUtils.put("containerNo",containerNo).getMap(),ContainerSub.class);
    }
}
