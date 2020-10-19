package com.prolog.eis.dto.yqfs;

import io.swagger.annotations.ApiModelProperty;

/**
 * 拣选单订单明细集合
 * @author tg
 *
 */
public class PickOrderMxDto {
	
	@ApiModelProperty("商品id")
	private int goodsId;
	
	@ApiModelProperty("商品名称")
	private String goodsName;
	
	@ApiModelProperty("商品条码")
	private String barCode;
	
	@ApiModelProperty("计划数量")
	private int planNum;
	
	@ApiModelProperty("实际数量")
	private int actualNum;
	
	@ApiModelProperty("订单明细ID")
	private int orderMxId;

	private int lackCount;
	public int getLackCount() {
		return lackCount;
	}

	public void setLackCount(int lackCount) {
		this.lackCount = lackCount;
	}



	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getPlanNum() {
		return planNum;
	}

	public void setPlanNum(int planNum) {
		this.planNum = planNum;
	}

	public int getActualNum() {
		return actualNum;
	}

	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}

	public int getOrderMxId() {
		return orderMxId;
	}

	public void setOrderMxId(int orderMxId) {
		this.orderMxId = orderMxId;
	}
	

}
