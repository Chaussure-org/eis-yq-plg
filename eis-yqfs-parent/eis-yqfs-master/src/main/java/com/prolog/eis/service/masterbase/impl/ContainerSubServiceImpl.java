package com.prolog.eis.service.masterbase.impl;

import com.prolog.eis.dao.masterbase.ContainerMapper;
import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dto.hxdispatch.StationLxPositionHxDto;
import com.prolog.eis.dto.yqfs.ContainerSubInfoDto;
import com.prolog.eis.model.masterbase.Container;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.pickstation.dto.HxContainerInfoDto;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.eis.util.PrologDateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ContainerSubServiceImpl implements ContainerSubService {

    @Autowired
    private ContainerSubMapper containerSubInfoMapper;
    @Autowired
    private ContainerMapper containerMapper;
    @Override
    public List<ContainerSub> findContainerSubInfo() {
        List<ContainerSub> containerSubInfo = containerSubInfoMapper.findContainerSubInfo();
        return containerSubInfo;
    }

    @Override
    public List<ContainerSub> fiadAll() {
        return containerSubInfoMapper.findByMap(new HashMap<String, Object>(),ContainerSub.class);
    }

    @Override
    public List<ContainerSubInfoDto> findValidContainerSubInfo() {
        return containerSubInfoMapper.findValidContainerSubInfo();
    }

    @Override
    public void updateContainerSubInfo(List<ContainerSub> containerSubInfos) throws Exception{
        for (ContainerSub containerSubInfo : containerSubInfos) {
            containerSubInfo.setGmtCreateTime(PrologDateUtils.parseObject(new Date()));
            containerSubInfoMapper.update(containerSubInfo);
        }
    }

    /**
     * 更新货格
     *
     * @param containerSub
     * @throws Exception
     */
    @Override
    public void update(ContainerSub containerSub) throws Exception {
        containerSubInfoMapper.update(containerSub);
    }

    @Override
    public List<String> findContainerSubNo(int goodsId, String containerNo) {
        return containerSubInfoMapper.findContainerSubNo(goodsId,containerNo);
    }

    @Override
    public ContainerSub findById(String containSubNo) {
        return containerSubInfoMapper.findById(containSubNo,ContainerSub.class);
    }

    public void createContainerSubNo() throws Exception {
        List<ContainerSub> list = new ArrayList<>();
        List<Container> containers = containerMapper.findByMap(null, Container.class);
//        Criteria criteria = Criteria.forClass(Container.class);
//        criteria.setRestriction(Restrictions.gt("containerNo","6000000100"));
//        List<Container> containers = containerMapper.findByCriteria(criteria);
        for (Container container : containers) {
            if (container.getLayoutType() == 3){
                for (int i = 1; i < 5; i++) {
                    ContainerSub containerSub = new ContainerSub();
                    containerSub.setContainerNo(container.getContainerNo());
                    containerSub.setContainerSubNo(container.getContainerNo()+i);
                    list.add(containerSub);
                }

            }
        }
        containerSubInfoMapper.saveBatch(list);
    }

    /**
     * 设置货格商品状态
     *
     * @param disable
     * @param conatinerSubNo
     * @throws Exception
     */
    @Override
    public void setDisable(boolean disable, String... conatinerSubNo) throws Exception {
        Criteria ctr = Criteria.forClass(ContainerSub.class);
        ctr.setRestriction(Restrictions.in("containerSubNo",conatinerSubNo));
        containerSubInfoMapper.updateMapByCriteria(MapUtils.put("spDisable",disable).getMap(),ctr);
    }

    @Override
    public List<StationLxPositionHxDto> getHxContainerInfo(int goodsId, String containerNo) {
        List<StationLxPositionHxDto> hxContainerInfo = containerSubInfoMapper.getHxContainerInfo(goodsId, containerNo);
        return hxContainerInfo;
    }

    @Override
    public void updateContainerSubByContainer(String containerNo) throws Exception {
        Criteria criteria = Criteria.forClass(ContainerSub.class);
        criteria.setRestriction(Restrictions.eq("containerNo",containerNo));
        containerSubInfoMapper.updateMapByCriteria(MapUtils.put("commodityId",null).put("commodityNum",null).getMap(),criteria);
    }

    @Override
    public List<HxContainerInfoDto> findHxContainerSub(String containerNo) {
        return containerSubInfoMapper.findHxContainerSub(containerNo);
    }

    @Override
    public List<Integer> findByMap(Map map) {
        List<ContainerSub> list = containerSubInfoMapper.findByMap(map,ContainerSub.class);
        return list.stream().map(x->x.getCommodityNum()).collect(Collectors.toList());
    }
}
