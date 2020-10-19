package com.prolog.eis.controller;

import com.prolog.eis.dto.apprf.AppInstockOrderCcceptanceDto;
import com.prolog.eis.service.AppInstockOrderCancelService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @入库取消
 * @author jinxf
 * @date 2020/4/23 17:53
 */
@RestController
@Api(tags="入库取消")
@RequestMapping("/api/v1/app/cancel")
@CrossOrigin
public class AppInstockOrderCancelController {

    @Autowired
    private AppInstockOrderCancelService appInstockOrderCancelService;

    @ApiOperation(value="查询货格详情",notes="查询货格详情")
    @PostMapping("/instockOrderCancelQuery")
    public RestMessage<List<AppInstockOrderCcceptanceDto>> instockOrderCancelQuery(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        List<AppInstockOrderCcceptanceDto> list = appInstockOrderCancelService.query(containerNo);
        return RestMessage.newInstance(true, "查询成功!", list);
    }

    @ApiOperation(value="入库取消",notes="入库取消")
    @PostMapping("/instockOrderCancel")
    public RestMessage<String> instockOrderCancel(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        appInstockOrderCancelService.cancel(containerNo);
        return RestMessage.newInstance(true, "操作成功!", null);
    }
}
