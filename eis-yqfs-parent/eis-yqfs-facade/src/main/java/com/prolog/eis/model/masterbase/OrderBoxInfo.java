package com.prolog.eis.model.masterbase;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("order_box_info")
public class OrderBoxInfo {
	
	@Id
	@Column("order_box_no")
	@ApiModelProperty("订单箱编号")
	private String orderBoxNo;
	
	@Column("outBound_order_id")
	@ApiModelProperty("出库订单ID")
	private int outBoundOrderId;

	public OrderBoxInfo(String orderBoxNo, int outBoundOrderId) {
		super();
		this.orderBoxNo = orderBoxNo;
		this.outBoundOrderId = outBoundOrderId;
	}

	public OrderBoxInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOrderBoxNo() {
		return orderBoxNo;
	}

	public void setOrderBoxNo(String orderBoxNo) {
		this.orderBoxNo = orderBoxNo;
	}

	public int getOutBoundOrderId() {
		return outBoundOrderId;
	}

	public void setOutBoundOrderId(int outBoundOrderId) {
		this.outBoundOrderId = outBoundOrderId;
	}
	
}
