package com.prolog.eis.model.tk;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 退库计划
 * @author tg
 *
 */
@Table("back_store_plan")
public class BackStorePlan {
	
	@Id
	@Column("id")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private int id;
	
	@Column("name")
	@ApiModelProperty("退库计划名称")
	private String name;
	
	@Column("remark")
	@ApiModelProperty("备注")
	private String remark;
	
	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;
	
	@Column("state")
	@ApiModelProperty("计划状态(0新建;10已下发;20执行中)")
	private int state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	
	

}
