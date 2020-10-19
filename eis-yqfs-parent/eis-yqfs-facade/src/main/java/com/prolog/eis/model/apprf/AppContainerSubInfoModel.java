package com.prolog.eis.model.apprf;

import com.prolog.eis.model.base.BaseModel;
import com.prolog.framework.core.annotation.AutoKey;
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
@Table(value = "CONTAINER_SUB_INFO")
public class AppContainerSubInfoModel extends BaseModel{

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
	
	@ApiModelProperty("批次号Id")
	@Column("LOT_ID")
	private Integer lotId;

	@ApiModelProperty("入库计划明细ID（可为空）")
	@Column("INBOUND_PLAN_DETAIL_ID")
	private Integer  inBoundPlanDetailId;

	@ApiModelProperty("入库计划数量（可为空）")
	@Column("INBOUND_PLAN_NUM")
	private Integer inBoundPlanNum;

//	@ApiModelProperty("创建日期")
//	@Column("CREATE_TIME")
//	private Date  createTime;

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

	public Integer getLotId() {
		return lotId;
	}

	public void setLotId(Integer lotId) {
		this.lotId = lotId;
	}

	public Integer getInBoundPlanDetailId() {
		return inBoundPlanDetailId;
	}

	public void setInBoundPlanDetailId(Integer inBoundPlanDetailId) {
		this.inBoundPlanDetailId = inBoundPlanDetailId;
	}

	public Integer getInBoundPlanNum() {
		return inBoundPlanNum;
	}

	public void setInBoundPlanNum(Integer inBoundPlanNum) {
		this.inBoundPlanNum = inBoundPlanNum;
	}


//	public Date getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}


}
