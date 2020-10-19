package com.prolog.eis.controller;
import com.prolog.eis.service.AppOutstockOrderRecheckService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author linxf
 * 出库复核
 * @date 2020/4/28 10:11
 */
@RestController
@Api(tags="APP出库复核服务")
@RequestMapping("/api/v1/app/outstock")
@CrossOrigin
public class AppOutstockOrderRecheckController {

    @Autowired
    private AppOutstockOrderRecheckService appOutstockOrderRecheckService;

    @ApiOperation(value="出库复核查询",notes="出库复核查询")
    @PostMapping("/outstockOrderRecheckList")
    public RestMessage<List<HashMap<String, Object>>> outstockOrderRecheckList(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String outboundCode = helper.getString("orderBoxNo");
//        String page = helper.getString("page");
//        String pageSize = helper.getString("pageSize");

        List<HashMap<String, Object>> list = appOutstockOrderRecheckService.query(outboundCode);
        return RestMessage.newInstance(true, "操作成功!", list);
    }

}
