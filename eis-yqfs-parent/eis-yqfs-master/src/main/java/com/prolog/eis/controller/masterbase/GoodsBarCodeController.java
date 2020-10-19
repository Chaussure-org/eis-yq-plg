package com.prolog.eis.controller.masterbase;


import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.masterbase.GoodsBarCode;
import com.prolog.eis.service.masterbase.GoodsBarCodeService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api("商品条码信息")
@RequestMapping("/api/v1/master/goodsBarCode")
public class GoodsBarCodeController {

    @Autowired
    private GoodsBarCodeService goodsBarCodeService;

    @ApiOperation(value = "商品条码信息添加", notes = "商品条码信息添加")
    @PostMapping("/save")
    public RestMessage<List<Goods>> saveGoods(@RequestBody String json)throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        GoodsBarCode goodsBarCode = helper.getObject("goodsBarCode", GoodsBarCode.class);
        goodsBarCodeService.saveGoodsBarCode(goodsBarCode);

        return RestMessage.newInstance(true, "添加成功", null);
    }
}
