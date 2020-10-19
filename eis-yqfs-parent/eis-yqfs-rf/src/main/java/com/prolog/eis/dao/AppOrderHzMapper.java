package com.prolog.eis.dao;

import com.prolog.eis.model.base.SysUser;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jinxf
 */
public interface AppOrderHzMapper extends BaseMapper<SysUser> {
	
	@Results({
        @Result(property = "id",  column = "id"),@Result(property = "userDeptId",  column = "userdeptid"),
        @Result(property = "loginName",  column = "loginname"),@Result(property = "userName",  column = "username"),
        @Result(property = "userPassword",  column = "userpassword"),@Result(property = "roleId",  column = "roleid"),
        @Result(property = "sex",  column = "sex"),@Result(property = "mobile",  column = "mobile"),
        @Result(property = "lastLoginTime",  column = "lastlogintime"),@Result(property = "createTime",  column = "createtime"),
        @Result(property = "workNo",column = "WORK_NO")
	})
    @Select("select t.id, t.userdeptid, t.loginname,t.username, t.userpassword,t.roleid," + 
    		"t.sex,t.mobile, t.lastlogintime,t.createtime from sysuser t where t.loginname = #{userName}")
    public List<SysUser> findUser(@Param("userName") String userName);
	
}
	