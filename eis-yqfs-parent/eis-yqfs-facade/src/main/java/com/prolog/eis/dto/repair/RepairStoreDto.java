package com.prolog.eis.dto.repair;

import io.swagger.annotations.ApiModelProperty;

public class RepairStoreDto {
	
	@ApiModelProperty("料箱号")
	private String containerNo;

	@ApiModelProperty("货格布局")
	private int recomType;

	@ApiModelProperty("商品编号")
	private String goodsNo;

	@ApiModelProperty("商品名称")
	private String goodsName;

	@ApiModelProperty("货格号")
	private String containerSubNo;

	@ApiModelProperty("库存数量")
	private int storeCount;

	@ApiModelProperty("可补数量")
	private int repairNumber;

	@ApiModelProperty("库存状态")
	private int storeState;

	@ApiModelProperty("货格上限")
	private int upLimit;

	@ApiModelProperty("层")
	private int layer;

	@ApiModelProperty("x")
	private int x;

	@ApiModelProperty("y")
	private int y;

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public int getRecomType() {
		return recomType;
	}

	public void setRecomType(int recomType) {
		this.recomType = recomType;
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

	public String getContainerSubNo() {
		return containerSubNo;
	}

	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
	}

	public int getStoreCount() {
		return storeCount;
	}

	public void setStoreCount(int storeCount) {
		this.storeCount = storeCount;
	}

	public int getRepairNumber() {
		return repairNumber;
	}

	public void setRepairNumber(int repairNumber) {
		this.repairNumber = repairNumber;
	}

	public int getStoreState() {
		return storeState;
	}

	public void setStoreState(int storeState) {
		this.storeState = storeState;
	}

	public int getUpLimit() {
		return upLimit;
	}

	public void setUpLimit(int upLimit) {
		this.upLimit = upLimit;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
