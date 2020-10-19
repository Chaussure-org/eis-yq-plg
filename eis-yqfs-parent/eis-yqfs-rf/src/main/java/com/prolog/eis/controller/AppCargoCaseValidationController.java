package com.prolog.eis.controller;

import com.prolog.eis.model.apprf.AppContainerModel;
import com.prolog.eis.model.apprf.AppContainerSubModel;
import com.prolog.eis.service.AppCargoCaseValidationService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @验证
 * @author jinxf
 * @date 2020/4/22 17:53
 */
@RestController
@Api(tags="货格验证")
@RequestMapping("/api/v1/app/cargo")
@CrossOrigin
public class AppCargoCaseValidationController {
    @Autowired
    private AppCargoCaseValidationService appCargoCaseValidationService;
    @ApiOperation(value="货格料箱查扫描",notes="货格料箱查扫描")
    @PostMapping("/cargoCaseValidationQuery")
    public RestMessage<AppContainerModel> cargoCaseValidationQuery(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        AppContainerModel model = appCargoCaseValidationService.query(containerNo);
        return RestMessage.newInstance(true, "操作成功", model);
    }

    @ApiOperation(value="货格料箱验证",notes="货格料箱验证")
    @PostMapping("/cargoCaseValidationConfirm")
    public RestMessage<AppContainerSubModel> cargoCaseValidationConfirm(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        String containerSubNos = helper.getString("containerSubNos");
        String type = helper.getString("type");
        return appCargoCaseValidationService.save(containerNo, containerSubNos, type);
    }
}
