package com.prolog.eis.model.orderboxline;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单箱库存表
 * @author tg
 *
 */
@Table("order_box_store_info")
public class OrderBoxStoreInfo {
	
	@Id
	@Column("id")
	@ApiModelProperty("id")
	private int id;
	
	@Column("order_box_no")
	@ApiModelProperty("订单箱编号")
	private String orderBoxNo;
	
	@Column("commodity_id")
	@ApiModelProperty("商品ID")
	private int commoditId;
	
	@Column("commodity_num")
	@ApiModelProperty("商品数量")
	private int commodityNum;
	
	@Column("lot_id")
	@ApiModelProperty("批次号Id")
	private int lotId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderBoxNo() {
		return orderBoxNo;
	}

	public void setOrderBoxNo(String orderBoxNo) {
		this.orderBoxNo = orderBoxNo;
	}

	public int getCommoditId() {
		return commoditId;
	}

	public void setCommoditId(int commoditId) {
		this.commoditId = commoditId;
	}

	public int getCommodityNum() {
		return commodityNum;
	}

	public void setCommodityNum(int commodityNum) {
		this.commodityNum = commodityNum;
	}

	public int getLotId() {
		return lotId;
	}

	public void setLotId(int lotId) {
		this.lotId = lotId;
	}
	
	
	
}
