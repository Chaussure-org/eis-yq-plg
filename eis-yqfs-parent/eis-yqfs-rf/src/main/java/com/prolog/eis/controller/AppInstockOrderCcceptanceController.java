package com.prolog.eis.controller;
import com.prolog.eis.dto.apprf.AppInstockOrderCcceptanceDto;
import com.prolog.eis.model.apprf.AppGoodsModel;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.service.AppInstockOrderCcceptanceService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 手动入库收货
 * @author jinxf
 * @date 2020/4/26 15:55
 */
@RestController
@Api(tags="手动入库收货")
@RequestMapping("/api/v1/app/ceptance")
@CrossOrigin
public class AppInstockOrderCcceptanceController {

    @Autowired
    private AppInstockOrderCcceptanceService appEmptyBoxInstockOrderService;

    @ApiOperation(value="料箱类型查询",notes="料箱类型查询")
    @PostMapping("/instockOrderCcceptanceBoxQuery")
    public RestMessage<String> instockOrderCcceptanceBoxQuery(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        String userId = helper.getString("userId");
        String type = appEmptyBoxInstockOrderService.boxQuery(containerNo,userId);
        return RestMessage.newInstance(true, "查询成功!", type);
    }

    @ApiOperation(value="查询货格详情",notes="查询货格详情")
    @PostMapping("/instockOrderCcceptanceQuery")
    public RestMessage<List<AppInstockOrderCcceptanceDto>> instockOrderCcceptanceQuery(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        List<AppInstockOrderCcceptanceDto> list = appEmptyBoxInstockOrderService.query(containerNo);
        return RestMessage.newInstance(true, "查询成功!", list);
    }

    @ApiOperation(value="查询商品信息",notes="查询商品信息")
    @PostMapping("/instockOrderCcceptanceGoodsQuery")
    public RestMessage<AppGoodsModel> instockOrderCcceptanceGoodsQuery(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String goodsNo = helper.getString("goodsNo");
        AppGoodsModel goods = appEmptyBoxInstockOrderService.goodsQuery(goodsNo);
        return RestMessage.newInstance(true, "查询成功!", goods);
    }

    @ApiOperation(value="换箱入库确认",notes="换箱入库确认")
    @PostMapping("/instockOrderCcceptanceConfirm")
    public RestMessage<String> instockOrderCcceptanceConfirm(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("container_no");
        String containerSubNo = helper.getString("container_sub_no");
        String goodsId = helper.getString("commodity_id");
        String expiryDate = helper.getString("expiry_date");
        String goodsNum = helper.getString("commodity_num");
        String inboundTime = helper.getString("inbound_time");
        String referenceNum = helper.getString("reference_num");

        appEmptyBoxInstockOrderService.add(containerNo,containerSubNo,goodsId,expiryDate,goodsNum,inboundTime,referenceNum);
        return RestMessage.newInstance(true, "操作成功!", null);
    }

    @ApiOperation(value="换箱入库货格取消",notes="换箱入库货格取消")
    @PostMapping("/instockOrderCcceptanceCancel")
    public RestMessage<String> instockOrderCcceptanceCancel(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        String containerSubNo = helper.getString("containerSubNo");

        appEmptyBoxInstockOrderService.cancel(containerNo,containerSubNo);
        return RestMessage.newInstance(true, "操作成功!", null);
    }

}
