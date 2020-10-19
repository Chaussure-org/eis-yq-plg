package com.prolog.eis.pickstation.controller;

import com.prolog.eis.pickstation.client.UpdateClientStationInfoService;
import com.prolog.eis.pickstation.dto.StationInfoDto;
import com.prolog.eis.pickstation.service.IStationDdxService;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/6/18 10:27
 */
@RestController
@Api(tags = "拣选站播种任务")
@RequestMapping("/api/v1/master/pickStation/pick")
public class PickStationPickController {

    @Autowired
    private IStationDdxService stationDdxService;
    @Autowired
    private IStationService stationService;
    @Autowired
    private UpdateClientStationInfoService updateClientStationInfoService;

    @ApiModelProperty(value = "初始化页面信息" ,notes = "初始化页面信息")
    @PostMapping("/init")
    public RestMessage<Map> initData(@RequestBody String json) {
        try {
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
            int stationId = helper.getInt("stationId");
            Map data = stationService.init(stationId);
            return new RestMessage<Map>(true,"初始化成功",data);
        }catch (Exception e){
            throw new RuntimeException("料箱,订单箱数据初始化失败");
        }
    }

    @ApiModelProperty(value = "订单箱换箱操作" , notes = "订单箱换箱操作")
    @PostMapping("/changeOrderBox")
    public RestMessage<String> changeOrderBox(@RequestBody String json) throws Exception {
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
            String orderBoxNo = helper.getString("orderBoxNo");
            int positionNo = helper.getInt("positionNo");
            int num = helper.getInt("num");
            stationDdxService.changeOrderBox(orderBoxNo,positionNo,num);
            return RestMessage.newInstance(true,"换箱成功");
    }

    @PostMapping("/initSeedStation")
    public RestMessage<StationInfoDto> initSeedStation(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper=PrologApiJsonHelper.createHelper(json);
        int stationId=helper.getInt("stationId");
        updateClientStationInfoService.sendMsgToClient("初始化站台信息",0,new Date(),stationId);
        StationInfoDto stationInfoDto = updateClientStationInfoService.findStationSeedInfo(stationId);
        return RestMessage.newInstance(true,"查询成功",stationInfoDto);
    }
}
