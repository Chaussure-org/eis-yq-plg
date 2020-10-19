package com.prolog.eis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.prolog.eis.model.base.SysUser;
import com.prolog.eis.service.AppUserService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户管理
 * @author jinxf
 */
@RestController
@Api(tags="APP用户服务")
@RequestMapping("/api/v1/app/user")
@CrossOrigin
public class AppUserController {

	@Autowired
	private AppUserService  appUserService;

	@ApiOperation(value="用户登录",notes="用户登录")
	@PostMapping("/login")
	public RestMessage<SysUser> login(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String loginName = helper.getString("loginName");
		String userPassword = helper.getString("userPassword");
		SysUser sysUser = appUserService.login(loginName, userPassword);
		return RestMessage.newInstance(true, "登录成功", sysUser);
	}
}
