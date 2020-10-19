package com.prolog.eis.model.masterbase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;


import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("container_sub")
public class ContainerSub {

	@Id
	@Column("container_sub_no")
	@ApiModelProperty("子容器编号")
	private String containerSubNo;
	
	@Column("container_no")
	@ApiModelProperty("容器编号")
	private String containerNo;
	
	@Column("commodity_id")
	@ApiModelProperty("商品Id")
	private Integer commodityId;
	
	@Column("commodity_num")
	@ApiModelProperty("商品数量")
	private Integer commodityNum;

	@Column("sp_disable")
	@ApiModelProperty("不可用")
	private boolean spDisable;
	
	@Column("expiry_date")
	@ApiModelProperty("有效日期")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date expiryDate;

	@Column("gmt_create_time")
	@ApiModelProperty("创建时间")
	private Date gmtCreateTime;

	@ApiModelProperty("入库时间")
	@Column("inbound_time")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date inboundTime;

	@ApiModelProperty("货格放置商品参考数量")
	@Column("reference_num")
	private int referenceNum;

	public Date getInboundTime() {
		return inboundTime;
	}

	public void setInboundTime(Date inboundTime) {
		this.inboundTime = inboundTime;
	}

	public int getReferenceNum() {
		return referenceNum;
	}

	public void setReferenceNum(int referenceNum) {
		this.referenceNum = referenceNum;
	}

	public String getContainerSubNo() {
		return containerSubNo;
	}

	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public Integer getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(Integer commodityId) {
		this.commodityId = commodityId;
	}

	public Integer getCommodityNum() {
		return commodityNum;
	}

	public void setCommodityNum(Integer commodityNum) {
		this.commodityNum = commodityNum;
	}

	public boolean isSpDisable() {
		return spDisable;
	}

	public void setSpDisable(boolean spDisable) {
		this.spDisable = spDisable;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getGmtCreateTime() {
		return gmtCreateTime;
	}

	public void setGmtCreateTime(Date gmtCreateTime) {
		this.gmtCreateTime = gmtCreateTime;
	}
}
