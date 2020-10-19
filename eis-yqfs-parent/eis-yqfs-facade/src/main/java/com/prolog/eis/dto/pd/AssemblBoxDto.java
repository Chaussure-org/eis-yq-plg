package com.prolog.eis.dto.pd;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class AssemblBoxDto {
	
	@ApiModelProperty("库存id")
	private int id;
	
	@ApiModelProperty("明细箱号")
	private String containerNo;

	@ApiModelProperty("商品ID")
	private int goodsId;
	
	@ApiModelProperty("商品编号")
	private String goodsNo;
	
	@ApiModelProperty("商品名称")
	private String goodsName;
	
	@ApiModelProperty("商品条码")
	private String goodsBarCode;
	
	@ApiModelProperty("入库时间")
	private Date inStoreTime;

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
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

	public Date getInStoreTime() {
		return inStoreTime;
	}

	public void setInStoreTime(Date inStoreTime) {
		this.inStoreTime = inStoreTime;
	}
	
	
	
}
