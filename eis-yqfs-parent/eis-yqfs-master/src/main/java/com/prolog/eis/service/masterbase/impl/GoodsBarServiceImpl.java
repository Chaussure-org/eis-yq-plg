package com.prolog.eis.service.masterbase.impl;

import com.prolog.eis.dao.masterbase.GoodsBarCodeMapper;
import com.prolog.eis.model.masterbase.GoodsBarCode;
import com.prolog.eis.service.masterbase.GoodsBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GoodsBarServiceImpl implements GoodsBarService {
    @Autowired
    private GoodsBarCodeMapper goodsBarCodeMapper;
    @Override
    public void deleteAllGoodsBra() throws Exception {
        goodsBarCodeMapper.deleteByMap(new HashMap<String, Object>(), GoodsBarCode.class);
    }
}
