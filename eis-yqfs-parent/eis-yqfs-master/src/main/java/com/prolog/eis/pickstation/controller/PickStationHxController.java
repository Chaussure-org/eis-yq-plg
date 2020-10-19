package com.prolog.eis.pickstation.controller;

import com.prolog.eis.dto.hxdispatch.StationLxPositionHxDto;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.pickstation.dto.SendLxDto;
import com.prolog.eis.pickstation.dto.StationInfoDto;
import com.prolog.eis.pickstation.service.IStationHxService;
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
@Api(tags = "拣选站合箱任务")
@RequestMapping("/api/v1/master/pickStation/hx")
public class PickStationHxController {

    @Autowired
    private IStationHxService iStationHxService;
    @ApiOperation(value = "子容器数量合并", notes = "子容器数量合并")
    @PostMapping("/calculateHx")
    public RestMessage<List<Goods>> saveCalculateHx(@RequestBody String json)throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String sourceContainerSubNo = helper.getString("sourceContainerSubNo");
        String targetContainerSubNo = helper.getString("targetContainerSubNo");
        int commodityNum = helper.getInt("commodityNum");
        iStationHxService.calculateHx(sourceContainerSubNo,targetContainerSubNo,commodityNum);


        return RestMessage.newInstance(true, "合箱成功", null);
    }
    @ApiOperation(value = "初始化合箱页面", notes = "初始化合箱页面")
    @PostMapping("/initHx")
    public RestMessage<StationInfoDto> initHxPage(@RequestBody String json)throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int stationId = helper.getInt("stationId");

        StationInfoDto hxInfo = iStationHxService.initHxDate(stationId);


        return RestMessage.newInstance(true, "页面初始化完成", hxInfo);
    }


    @ApiOperation(value = "料箱离开", notes = "料箱离开")
    @PostMapping("/lxLeave")
    public RestMessage<List<SendLxDto>> lxLeave(@RequestBody String json)throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int stationId = helper.getInt("stationId");
        String containerNo = helper.getString("containerNo");


        iStationHxService.lxLeave(containerNo,stationId);


        return RestMessage.newInstance(true, "料箱离开完成", null);
    }
    @ApiOperation(value = "合箱亮灯", notes = "合箱亮灯")
    @PostMapping("/hxLight")
    public RestMessage<StationLxPositionHxDto> hxLight(@RequestBody String json)throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String sourceContainerSubNo = helper.getString("startSubContainerNo");
        String targetContainerSubNo = helper.getString("endSubContainerNo");

        iStationHxService.hxLight(sourceContainerSubNo,targetContainerSubNo);


        return RestMessage.newInstance(true, "亮灯完成", null);
    }
}
