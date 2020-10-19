package com.prolog.eis.dto.yqfs;

import io.swagger.annotations.ApiModelProperty;

/**
 * 拣选单绑定集合
 * @author tg
 *
 */
public class PickContainerSubBindingDto {

	@ApiModelProperty("箱库库存ID")
	private int xkKuCunId;
	@ApiModelProperty("在途库存ID")
	private int ztKuCunId;
	@ApiModelProperty("料箱编号")
	private String liaoXiangNo;
	@ApiModelProperty("订单明细ID")
	private int orderMxId;
	@ApiModelProperty("绑定数量")
	private int planNum;
	@ApiModelProperty("是否完成")
	private Integer isFinish;
	public int getXkKuCunId() {
		return xkKuCunId;
	}
	public void setXkKuCunId(int xkKuCunId) {
		this.xkKuCunId = xkKuCunId;
	}
	public int getZtKuCunId() {
		return ztKuCunId;
	}
	public void setZtKuCunId(int ztKuCunId) {
		this.ztKuCunId = ztKuCunId;
	}
	public String getLiaoXiangNo() {
		return liaoXiangNo;
	}
	public void setLiaoXiangNo(String liaoXiangNo) {
		this.liaoXiangNo = liaoXiangNo;
	}
	public int getOrderMxId() {
		return orderMxId;
	}
	public void setOrderMxId(int orderMxId) {
		this.orderMxId = orderMxId;
	}
	public int getPlanNum() {
		return planNum;
	}
	public void setPlanNum(int planNum) {
		this.planNum = planNum;
	}
	public Integer getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(Integer isFinish) {
		this.isFinish = isFinish;
	}
	
	

}
