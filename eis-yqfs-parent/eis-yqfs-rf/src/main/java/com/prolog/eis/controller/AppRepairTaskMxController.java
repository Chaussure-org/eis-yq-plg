package com.prolog.eis.controller;

import com.prolog.eis.service.AppRepairTaskMxService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Api(tags = "料箱查询")
@RequestMapping("/api/v1/app/repairtaskmx")
@CrossOrigin
public class AppRepairTaskMxController {

    @Autowired
    private AppRepairTaskMxService appRepairTaskMxService;

    @ApiOperation(value = "料箱查询", notes = "料箱查询")
    @PostMapping("/query")
    public RestMessage<List<HashMap<String, Object>>> query(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        List<HashMap<String, Object>> list = appRepairTaskMxService.query(containerNo);
        return RestMessage.newInstance(true, "操作成功!", list);
    }
}
