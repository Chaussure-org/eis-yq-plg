package com.prolog.eis.pickstation.client.impl;

import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dao.store.StoreLocationMapper;
import com.prolog.eis.model.store.SxStoreLocation;
import com.prolog.eis.service.masterbase.GoodsService;
import com.prolog.eis.service.store.IStoreLocationService;
import com.prolog.eis.service.store.IStoreTaskService;
import com.prolog.eis.service.store.impl.SxStoreLocationServiceImpl;
import com.prolog.eis.util.DistanceUtils;
import com.prolog.eis.util.TaskUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangkang
 * @Description 分配位置测试
 * @CreateTime 2020/7/5 12:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationTest {
    @Autowired
    private IStoreLocationService locationService;
    @Autowired
    private EisProperties properties;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SxStoreLocationServiceImpl storeLocationService;
    @Autowired
    private StoreLocationMapper storeLocationMapper;

    @Test
    public void testStartInboundTask() throws Exception {
        int layer = 1;
        SxStoreLocation bestLocation = storeLocationService.getBestLocation(layer,null);
        System.out.println(bestLocation.getStoreNo());
    }
}
