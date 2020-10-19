package com.prolog.eis.controller;

import com.prolog.eis.dao.AppEmptyBoxInstockOrderMapper;
import com.prolog.eis.model.apprf.AppEmptyContainerInboundTaskModel;
import com.prolog.eis.model.base.SysUser;
import com.prolog.eis.service.AppEmptyBoxInstockOrderService;
import com.prolog.eis.service.AppUserService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author linxf
 * @空箱入库
 * @date 2020/4/28 10:11
 */
@RestController
@Api(tags="空箱入库")
@RequestMapping("/api/v1/app/emptyBoxInstock")
@CrossOrigin
public class AppEmptyBoxInstockOrderController {

    @Autowired
    private AppEmptyBoxInstockOrderService appEmptyBoxInstockOrderService;

    @ApiOperation(value="空箱入库保存",notes="空箱入库保存")
    @PostMapping("/emptyBoxInstockOrderAdd")
    public RestMessage<AppEmptyContainerInboundTaskModel> emptyBoxInstockOrderAdd(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        AppEmptyContainerInboundTaskModel model = appEmptyBoxInstockOrderService.add(containerNo);
        return RestMessage.newInstance(true, "操作成功!", model);
    }
    @ApiOperation(value="空箱入库取消",notes="空箱入库取消")
    @PostMapping("/emptyBoxInstockOrderCancel")
    public RestMessage<String> emptyBoxInstockOrderCancel(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        appEmptyBoxInstockOrderService.cancel(containerNo);
        return RestMessage.newInstance(true, "操作成功!", null);
    }

}
