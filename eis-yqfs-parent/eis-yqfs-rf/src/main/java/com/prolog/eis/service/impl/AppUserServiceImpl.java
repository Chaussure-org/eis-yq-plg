package com.prolog.eis.service.impl;

import com.prolog.eis.dao.AppUserMapper;
import com.prolog.eis.model.base.SysUser;
import com.prolog.eis.service.AppUserService;
import com.prolog.eis.util.PrologDateUtils;
import com.prolog.eis.util.PrologMd5Util;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService {
	@Autowired
	private AppUserMapper appUserMapper;

	/**
	 * @title
	 * @author jinxf
	 * @Param: userName 账号
	 * @Param: pwd  	密码
	 * @time 2020/4/22 20:04
	 * @retrun
	 */
	@Override
	@Transactional
	public SysUser login(String userName, String pwd) throws Exception {
		if(StringUtils.isEmpty(userName)) {
			throw new Exception("用户姓名不能为空！");
		}
		if(StringUtils.isEmpty(pwd)) {
			throw new Exception("密码不能为空！");
		}
		pwd = PrologMd5Util.md5(pwd);
		List<SysUser> user = appUserMapper.findUser(userName);
		if(user.size()>1){
			throw new Exception("请联系管理员，存在多个用户！");
		}
		if(0 == user.size()) {
			throw new Exception("账户或密码不正确!");
		}
		
		SysUser currUser =new SysUser();
		if(1 == user.size()){
			if(user.get(0).getUserPassword().equals(pwd)) {
				currUser = user.get(0) ;
				Date lastLoginTime = PrologDateUtils.parseObject(new Date());
				appUserMapper.updateMapById(currUser.getId(), MapUtils.put("lastLoginTime", lastLoginTime).getMap(), SysUser.class);
			}else {
				throw new Exception("账户或密码不正确!");
			}
		}
		return currUser;
	}
	
}
