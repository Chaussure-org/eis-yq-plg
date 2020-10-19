package com.prolog.eis.service.masterbase;

import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.masterbase.GoodsBarCode;

import java.util.List;
import java.util.Map;

public interface GoodsBarCodeService {

    //新增商品条码
    public void saveGoodsBarCode(GoodsBarCode goodsBarCode) throws Exception;

    List<String> findByMap(Map map);
}
