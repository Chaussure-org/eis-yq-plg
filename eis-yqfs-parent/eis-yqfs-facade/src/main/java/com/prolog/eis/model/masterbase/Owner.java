package com.prolog.eis.model.masterbase;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * 业主资料
 * @author tg
 *
 */
@Table("Owner")
public class Owner {
	
	@Id
	@ApiModelProperty("id")
	@Column("id")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("owner_no")
	@ApiModelProperty("业主编号")
	private String ownerNo;
	
	@Column("owner_name")
	@ApiModelProperty("业主名称")
	private String ownerName;
	
	@Column("zhuj_code")
	@ApiModelProperty("助计码")
	private String zhujCode;
	
	@Column("owner_simplename")
	@ApiModelProperty("业主简称")
	private String ownerSimplename;
	
	@Column("beactive")
	@ApiModelProperty("是否活动：1、活动，2、不活动")
	private int beactive;
	
	@Column("create_time")
	@ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
	private Date createTime;
	
	@Column("update_time")
	@ApiModelProperty("修改时间（YYYY-MM-DD HH:MM:SS）")
	private Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String ownerNo) {
		this.ownerNo = ownerNo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getZhujCode() {
		return zhujCode;
	}

	public void setZhujCode(String zhujCode) {
		this.zhujCode = zhujCode;
	}

	public String getOwnerSimplename() {
		return ownerSimplename;
	}

	public void setOwnerSimplename(String ownerSimplename) {
		this.ownerSimplename = ownerSimplename;
	}

	public int getBeactive() {
		return beactive;
	}

	public void setBeactive(int beactive) {
		this.beactive = beactive;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Owner(int id, String ownerNo, String ownerName, String zhujCode, String ownerSimplename, int beactive,
			Date createTime, Date updateTime) {
		super();
		this.id = id;
		this.ownerNo = ownerNo;
		this.ownerName = ownerName;
		this.zhujCode = zhujCode;
		this.ownerSimplename = ownerSimplename;
		this.beactive = beactive;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Owner() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
