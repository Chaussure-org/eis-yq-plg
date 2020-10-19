package com.prolog.eis.dto.yqfs;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品库存预警
 * @author tg
 *
 */
public class OutboundWarnQtyDto {

	@ApiModelProperty("商品id")
	private int goodsId;

	@ApiModelProperty("预警计算天数")
	private int days;

	@ApiModelProperty("预警计算系数")
	private Double modulus;

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public Double getModulus() {
		return modulus;
	}

	public void setModulus(Double modulus) {
		this.modulus = modulus;
	}
	
}
