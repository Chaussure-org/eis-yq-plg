package com.prolog.eis.controller.yqfs;


import com.prolog.eis.wcs.dto.HoisterInfoDto;
import com.prolog.eis.wcs.service.IWCSService;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/master/hoister")
@Api(tags = "提升机接口")
public class HoisterController {

    @Autowired
    private IWCSService iwcsService;

    @PostMapping("/selectAllHoister")
    @ApiOperation(value = "获取所有的提升机信息")
    public RestMessage<List<HoisterInfoDto>> getAllHoister(){
        List<HoisterInfoDto> hoisterInfoDto = iwcsService.getHoisterInfoDto();
        return RestMessage.newInstance(true,"查询成功",hoisterInfoDto);
        
    }
}
