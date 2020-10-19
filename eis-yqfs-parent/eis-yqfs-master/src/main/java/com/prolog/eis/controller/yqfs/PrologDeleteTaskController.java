package com.prolog.eis.controller.yqfs;

import com.prolog.eis.pickstation.service.PrologDeleteTaskService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/7/25 18:31
 */

@RestController
@Api(tags = "料箱删除")
@RequestMapping("/api/v1/master/task")
public class PrologDeleteTaskController {

    @Autowired
    private PrologDeleteTaskService service;

    @PostMapping("deleteContainerNo")
    public RestMessage<String> deleteContainerNo(String json) throws Exception {

        PrologApiJsonHelper helper=PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        int type = helper.getInt("Type");
        service.deleteTask(containerNo,type);
        return RestMessage.newInstance(true,null);
    }
}
