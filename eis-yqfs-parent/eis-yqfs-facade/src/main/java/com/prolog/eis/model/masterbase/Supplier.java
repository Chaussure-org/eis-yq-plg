package com.prolog.eis.model.masterbase;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商
 * @author tg
 *
 */
@Table("supplier")
public class Supplier {
	
	@Id
	@Column("id")
	@ApiModelProperty("id")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("supplier_no")
	@ApiModelProperty("供应商编号")
	private String supplierNo;//供应商编号
	
	@Column("supplier_name")
	@ApiModelProperty("供应商名称")
	private String supplierName;//供应商名称
	
	@Column("zhuj_code")
	@ApiModelProperty("助计码")
	private String zhujCode;//助计码
	
	@Column("beactive")
	@ApiModelProperty("是否活动：1、活动，2、不活动")
	private int beactive;//是否活动：1、活动，2、不活动
	
	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;//创建时间
	
	@Column("update_time")
	@ApiModelProperty("修改时间")
	private Date updateTime;//修改时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getZhujCode() {
		return zhujCode;
	}

	public void setZhujCode(String zhujCode) {
		this.zhujCode = zhujCode;
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

	public Supplier(int id, String supplierNo, String supplierName, String zhujCode, int beactive, Date createTime,
			Date updateTime) {
		super();
		this.id = id;
		this.supplierNo = supplierNo;
		this.supplierName = supplierName;
		this.zhujCode = zhujCode;
		this.beactive = beactive;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Supplier() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
