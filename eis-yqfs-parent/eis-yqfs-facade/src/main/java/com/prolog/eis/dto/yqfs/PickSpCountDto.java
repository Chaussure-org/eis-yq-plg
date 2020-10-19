package com.prolog.eis.dto.yqfs;

import io.swagger.annotations.ApiModelProperty;

/**
 * 拣选单商品箱库库存集合
 * @author tg
 *
 */
public class PickSpCountDto {

	@ApiModelProperty("商品id")
	private int goodsId;
	@ApiModelProperty("箱库库存数量")
	private int xkCount;
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public int getXkCount() {
		return xkCount;
	}
	public void setXkCount(int xkCount) {
		this.xkCount = xkCount;
	}
	

}
