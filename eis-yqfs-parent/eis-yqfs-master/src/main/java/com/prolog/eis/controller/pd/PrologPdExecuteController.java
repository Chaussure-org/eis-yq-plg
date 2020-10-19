package com.prolog.eis.controller.pd;

import com.prolog.eis.dto.pd.PdDwContainerDto;
import com.prolog.eis.dto.pd.PdRecord;
import com.prolog.eis.service.pd.PdExecuteService;
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

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/6/16 9:46
 */

@RestController
@Api(tags = "盘点执行")
@RequestMapping("/api/v1/master/pdExecute")
public class PrologPdExecuteController {

    @Autowired
    private PdExecuteService pdExecuteService;

    @ApiOperation(value = "确认盘点结果", notes = "确认盘点结果")
    @PostMapping("/complete")
    public RestMessage<String> pdComplete(@RequestBody String json) throws Exception {

        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        String containerSubNo = helper.getString("containerSubNo");
        int storeCount = helper.getInt("storeCount");

        String light = pdExecuteService.pdComplete(containerNo, containerSubNo, storeCount);

        return RestMessage.newInstance(true, "执行成功", light);
    }

    @ApiOperation(value = "初始化盘点站台", notes = "初始化盘点站台")
    @PostMapping("/initStation")
    public RestMessage<List<PdDwContainerDto>> initPdStation(@RequestBody String json) throws Exception {

        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int stationId = helper.getInt("stationId");

        List<PdDwContainerDto> result = pdExecuteService.init(stationId);

        return RestMessage.newInstance(true, "查询成功", result);
    }


    @ApiOperation("容器离开")
    @PostMapping("lxLeave")
    public RestMessage lxLeave(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int stationId = helper.getInt("stationId");
        String containerNo = helper.getString("containerNo");
        pdExecuteService.lxLeave(containerNo, stationId);
        return RestMessage.newInstance(true, "查询成功", null);
    }


    @ApiOperation("容器历史")
    @PostMapping("selectPdHis")
    public RestMessage<List<PdRecord>> selectPdHis(String goodsNo, String containerSubNo) {
        List<PdRecord> pdRecords = pdExecuteService.selectPdHis(goodsNo, containerSubNo);
        return RestMessage.newInstance(true, "查询成功", pdRecords);
    }





}
