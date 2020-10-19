package com.prolog.eis.controller.repair;

import com.prolog.eis.dto.yqfs.ReplenishmentConfirmDto;
import com.prolog.eis.service.repair.ReplenishmentService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author panteng
 * @description: wcs - eis 补货
 * @date 2020/4/22 16:57
 */
@RestController
@Api(tags = "wcs - eis 补货作业")
@RequestMapping("/api/v1/master/replenishment")
@CrossOrigin
public class ReplenishmentController {

    @Autowired
    private ReplenishmentService replenishmentService;

    @ApiOperation(value = "容器到位", notes = "容器到位")
    @PostMapping("/containerArrive")
    public RestMessage<Map<String, Object>> containerArrive(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerSubNo = helper.getString("containerNo");
        if(StringUtils.isEmpty(containerSubNo) || containerSubNo.length() < 2){
            return RestMessage.newInstance(false,"容器号:"+json + "异常",null);
        }
        String containerNo = containerSubNo.substring(0,containerSubNo.length()-1);
        return replenishmentService.containerArrive(containerNo);
    }

    @ApiOperation(value = "站台刷新", notes = "站台刷新")
    @PostMapping("/stationRefresh")
    public RestMessage<Map<String, Object>> stationRefresh() throws Exception {
        return replenishmentService.stationRefresh();
    }


    @ApiOperation(value = "补货确认", notes = "补货确认")
    @PostMapping("/confirm")
    public RestMessage<String> confirm(@Valid @RequestBody ReplenishmentConfirmDto dto) throws Exception {
        try {
            return replenishmentService.confirm(dto);
        }catch (Exception e){
            return RestMessage.newInstance(false,"错误"+e.getMessage(),null);
        }
    }

    @ApiOperation(value = "料箱补货完成", notes = "料箱补货完成")
    @PostMapping("/complete")
    public RestMessage<String> complete(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        if(StringUtils.isEmpty(containerNo)){
            return RestMessage.newInstance(false,"参数无效，请检查： " + json,null);
        }
        return replenishmentService.complete(containerNo);
    }
}
