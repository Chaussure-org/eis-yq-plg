package com.prolog.eis.controller.yqfs;

import com.prolog.eis.schedule.CarReturnSchedule;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author wangkang
 * @Description 手动换层
 * @CreateTime 2020-09-04 11:19
 */
@RestController
@RequestMapping("/api/v1/master/cars")
public class PrologCarChangeController {

    @Autowired
    private CarReturnSchedule carReturnSchedule;

    @ApiModelProperty(value = "手动换层", notes="手动换层")
    @PostMapping("/changeCarToLayer")
    public RestMessage<String> changeCarToLayer(@RequestBody String json){
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int source = helper.getInt("source");
        int target = helper.getInt("target");
        try {
            carReturnSchedule.changeCarToLayer(source, target);
            return RestMessage.newInstance(true, "换层任务发起成功");
        }catch (Exception e){
            return RestMessage.newInstance(false,e.getMessage());
        }
    }

}
