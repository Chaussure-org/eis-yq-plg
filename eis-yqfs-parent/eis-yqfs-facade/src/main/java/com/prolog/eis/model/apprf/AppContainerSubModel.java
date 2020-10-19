package com.prolog.eis.model.apprf;

import com.prolog.eis.model.base.BaseModel;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @title 子容器表
 * @author jinxf
 * @time 2020/4/23 11:06
 */
@Table(value = "CONTAINER_SUB")
public class AppContainerSubModel extends BaseModel {

	@Id
	@ApiModelProperty("子容器编号")
	@Column("CONTAINER_SUB_NO")
	private String containerSubNo;

	@ApiModelProperty("容器编号")
	@Column("CONTAINER_NO")
	private String containerNo;

	@ApiModelProperty("商品Id")
	@Column("COMMODITY_ID")
	private Integer commodityId;

	@ApiModelProperty("商品数量")
	@Column("COMMODITY_NUM")
	private Integer commodityNum;

	@ApiModelProperty("有效期")
	@Column("EXPIRY_DATE")
	private Date expiryDate;

	@ApiModelProperty("有效期")
	@Column("GMT_CREATE_TIME")
	private Date gmtCreateTime;

	@ApiModelProperty("入库时间")
	@Column("INBOUND_TIME")
	private Date inboundTime;

	@ApiModelProperty("货格放置商品参考数量")
	@Column("reference_num")
	private Integer referenceNum;

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

	public Date getInboundTime() {
		return inboundTime;
	}

	public void setInboundTime(Date inboundTime) {
		this.inboundTime = inboundTime;
	}

	public Integer getReferenceNum() {
		return referenceNum;
	}

	public void setReferenceNum(Integer referenceNum) {
		this.referenceNum = referenceNum;
	}
}
