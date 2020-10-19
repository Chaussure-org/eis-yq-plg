package com.prolog.eis.service.masterbase.impl;

import com.prolog.eis.dao.masterbase.GoodsBarCodeMapper;
import com.prolog.eis.model.masterbase.GoodsBarCode;
import com.prolog.eis.service.masterbase.GoodsBarCodeService;
import com.prolog.eis.util.PrologDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsBarCodeServiceImpl implements GoodsBarCodeService {

    @Autowired
    private GoodsBarCodeMapper goodsBarCodeMapper;


    /**
     * 保存商品条码
     * @param goodsBarCode
     * @throws Exception
     */
    @Override
    public void saveGoodsBarCode(GoodsBarCode goodsBarCode) throws Exception {
        goodsBarCode.setCreateTime(PrologDateUtils.parseObject(new Date()));
        goodsBarCode.setIsdefault(0);
        goodsBarCodeMapper.save(goodsBarCode);
    }

    @Override
    public List<String> findByMap(Map map) {
        List<GoodsBarCode> goodsBarCodes = goodsBarCodeMapper.findByMap(map,GoodsBarCode.class);
        return goodsBarCodes.stream().map(GoodsBarCode::getBarCode).collect(Collectors.toList());

    }
}
