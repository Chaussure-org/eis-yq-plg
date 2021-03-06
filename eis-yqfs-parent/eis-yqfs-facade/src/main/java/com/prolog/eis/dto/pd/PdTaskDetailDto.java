package com.prolog.eis.dto.pd;


import io.swagger.annotations.ApiModelProperty;

public class PdTaskDetailDto {
	
	@ApiModelProperty("库存id")
	private int id;
	
	@ApiModelProperty("明细箱号")
	private String containerNo;
	
	@ApiModelProperty("货格号")
	private String containerSubNo;
	
	@ApiModelProperty("商品编号")
	private String goodsNo;
	
	@ApiModelProperty("商品名称")
	private String goodsName;
	
	@ApiModelProperty("商品条码")
	private String goodsBarCode;
	
	@ApiModelProperty("原始数量")
	private int originalCount;

	@ApiModelProperty("盘点类型: 1.普通盘点;2.动销盘点")
	private int pdType;
	

	public int getOriginalCount() {
		return originalCount;
	}

	public void setOriginalCount(int originalCount) {
		this.originalCount = originalCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getContainerSubNo() {
		return containerSubNo;
	}

	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
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

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public int getPdType() {
		return pdType;
	}

	public void setPdType(int pdType) {
		this.pdType = pdType;
	}
}
