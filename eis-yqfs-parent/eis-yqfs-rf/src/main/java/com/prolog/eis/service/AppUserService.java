package com.prolog.eis.service;
import com.prolog.eis.model.base.SysUser;

/**
 * @author jinxf
 */
public interface AppUserService {

	/**
    * @author jinxf
	* @date 2020年4月20日 下午8:05:40
	* @param userName 账号
	* @param pwd 密码
	 */
	public SysUser login(String userName, String pwd) throws Exception;

}
