package com.prolog.eis.pickstation.service.impl;

import com.prolog.eis.pickstation.dao.SeedInfoMapper;
import com.prolog.eis.pickstation.model.SeedInfo;
import com.prolog.eis.pickstation.service.ISeedInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/8/14 15:13
 */
@Service
public class SeedInfoServiceImpl implements ISeedInfoService {

    @Autowired
    private SeedInfoMapper mapper;

    @Override
    public void add(SeedInfo seedInfo) {
        mapper.save(seedInfo);
    }
}
