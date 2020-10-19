package com.prolog.eis.service.store.impl;

import com.prolog.eis.dao.store.ZtStoreMapper;
import com.prolog.eis.dto.store.ZtLxDetailsDto;
import com.prolog.eis.model.store.ZtStore;
import com.prolog.eis.service.store.IZtSoreService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author wangkang
 * @Description 在途实现类
 * @CreateTime 2020/6/20 11:07
 */
@Service
public class ZtStoreServiceImpl implements IZtSoreService {

    @Autowired
    private ZtStoreMapper ztStoreMapper;

    @Override
    public List<ZtStore> findByMap(Map map) {
        return ztStoreMapper.findByMap(map, ZtStore.class);
    }

    @Override
    public void update(ZtStore ztStore) {
        ztStoreMapper.update(ztStore);
    }

    @Override
    public List<ZtLxDetailsDto> findZtLxDeails(String containerNo) {
        return ztStoreMapper.findZtLxDeails(containerNo);
    }

    @Override
    public void addZtStore(ZtStore ztStore) throws Exception {
        ztStoreMapper.save(ztStore);
    }

    /**
     * 根据料箱寻找在途
     *
     * @param containerNo
     * @return
     */
    @Override
    public ZtStore getByContainerNo(String containerNo) {
        List<ZtStore> list = ztStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(),ZtStore.class);
        return list.size()>0? list.get(0):null;
    }


}
