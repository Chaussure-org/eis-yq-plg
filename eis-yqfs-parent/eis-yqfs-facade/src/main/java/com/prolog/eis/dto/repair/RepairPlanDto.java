package com.prolog.eis.dto.repair;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class RepairPlanDto {
	
	@ApiModelProperty("补货计划id")
    @NotNull(message = "补货计划id不能为空")
	private int repairPlanId;
	
	@ApiModelProperty("商品编号")
	private String goodsNo;
	
	@ApiModelProperty("商品名称")
	private String goodsName;
	
	@ApiModelProperty("推荐货格")
	private Integer recomType;
	
	@ApiModelProperty("推存数量")
	private Integer recomNumber;

	public int getRepairPlanId() {
		return repairPlanId;
	}

	public void setRepairPlanId(int repairPlanId) {
		this.repairPlanId = repairPlanId;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getRecomType() {
		return recomType;
	}

	public void setRecomType(Integer recomType) {
		this.recomType = recomType;
	}

	public Integer getRecomNumber() {
		return recomNumber;
	}

	public void setRecomNumber(Integer recomNumber) {
		this.recomNumber = recomNumber;
	}
}
