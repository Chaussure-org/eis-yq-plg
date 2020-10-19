package com.prolog.eis.controller.masterbase;

import com.prolog.eis.dto.yqfs.ContainerSubInfoDto;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api("容器查询")
@RequestMapping("/api/v1/master/containerSubInfo")
public class ContainerSubInfoController {

    @Autowired
    private ContainerSubService containerSubInfoService;
    @ApiOperation(value = "无效容器查询" , notes = "无效容器查询")
    @PostMapping("/find")
    public RestMessage<List<ContainerSub>> queryContainerSubInfo() throws Exception {
        List<ContainerSub> containerSubInfoList = containerSubInfoService.findContainerSubInfo();
        return RestMessage.newInstance(true, "查询成功",containerSubInfoList);
    }
    @ApiOperation(value = "容器商品查询" , notes = "容器商品查询")
    @PostMapping("/validFind")
    public RestMessage<List<ContainerSubInfoDto>> queryValidContainerSubInfo() throws Exception {
        List<ContainerSubInfoDto> containerSubInfoDtos = containerSubInfoService.findValidContainerSubInfo();
        return RestMessage.newInstance(true, "查询成功",containerSubInfoDtos);
    }



//    @PostMapping("/all")
//    public RestMessage<List<ContainerSub>> queryGoodsAll() throws Exception {
//        List<ContainerSub> containerSubInfoList = containerSubInfoService.fiadAll();
//        return RestMessage.newInstance(true, "查询成功",containerSubInfoList);
//    }


}
