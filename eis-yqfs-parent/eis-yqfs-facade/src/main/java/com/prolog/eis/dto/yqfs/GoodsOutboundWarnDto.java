package com.prolog.eis.dto.yqfs;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品缺货预警Dto
 * @author tg
 *
 */
public class GoodsOutboundWarnDto {
	
	@ApiModelProperty("商品id")
	private int goodsId;

	@ApiModelProperty("商品名称")
	private String goodsName;

	@ApiModelProperty("货主名称")
	private String ownerName;

	@ApiModelProperty("商品预警值")
	private int expectOutnum;

	@ApiModelProperty("商品条码")
	private String barCode;

	@ApiModelProperty("库存数量")
	private int kuCunCount;

	@ApiModelProperty("可用库存数量")
	private int ableKuCunCount;

	@ApiModelProperty("订单需求数量")
	private int orderNeedCount;

	@ApiModelProperty("建议补货数量")
	private int adviceReplenishment;

	@ApiModelProperty("日均出库")
	private int dayOut;

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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getExpectOutnum() {
		return expectOutnum;
	}

	public void setExpectOutnum(int expectOutnum) {
		this.expectOutnum = expectOutnum;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getKuCunCount() {
		return kuCunCount;
	}

	public void setKuCunCount(int kuCunCount) {
		this.kuCunCount = kuCunCount;
	}

	public int getAbleKuCunCount() {
		return ableKuCunCount;
	}

	public void setAbleKuCunCount(int ableKuCunCount) {
		this.ableKuCunCount = ableKuCunCount;
	}

	public int getOrderNeedCount() {
		return orderNeedCount;
	}

	public void setOrderNeedCount(int orderNeedCount) {
		this.orderNeedCount = orderNeedCount;
	}

	public int getAdviceReplenishment() {
		return adviceReplenishment;
	}

	public void setAdviceReplenishment(int adviceReplenishment) {
		this.adviceReplenishment = adviceReplenishment;
	}

	public int getDayOut() {
		return dayOut;
	}

	public void setDayOut(int dayOut) {
		this.dayOut = dayOut;
	}
	
	
	
	

}
