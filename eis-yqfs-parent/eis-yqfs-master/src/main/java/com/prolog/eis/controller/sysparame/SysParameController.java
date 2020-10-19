package com.prolog.eis.controller.sysparame;

import com.prolog.eis.dao.base.SysParameMapper;
import com.prolog.eis.init.SysIsRunningService;
import com.prolog.eis.model.base.SysParame;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author SunPP and Rod Johnson
 * @date 2020/06/24 11:18
 */
@RestController
@Api(tags = "系统配置参数")
@RequestMapping("/api/v1/master/sysparame")
public class SysParameController {
    @Autowired
    private SysParameMapper sysParameMapper;

    @Autowired
    private SysIsRunningService sysIsRunningService;
    @ApiOperation(value = "全局任务查询开关", notes = "全局查询下发开关")
    @PostMapping("/isRunning")
    public RestMessage<String> findSysRunning() throws Exception {
        String isrunning=sysIsRunningService.findSysIsrunning();
        return RestMessage.newInstance(true, "查询成功", isrunning);
    }

    @ApiOperation(value = "全局任务更新开关", notes = "全局更新下发开关")
    @PostMapping("/updateIsRunning")
    public RestMessage updateIsRunning(@RequestBody String json){
        PrologApiJsonHelper helper=PrologApiJsonHelper.createHelper(json);
        String value = helper.getString("paramevalue");
        Criteria ctr = Criteria.forClass(SysParame.class);
        ctr.setRestriction(Restrictions.eq("parameNo","sys_running"));
        sysParameMapper.updateMapByCriteria(MapUtils.put("parameValue",value).getMap(),ctr);
        return new RestMessage(true,"更新成功",null);
    }

}
