package com.prolog.eis.model.order;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("container_sub_binding_mx")
public class ContainerSubBindingMx {

	@Id
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	@Column("id")
	private int id;
	
	@Column("container_no")
	@ApiModelProperty("容器编号")
	private String containerNo;
	
	@Column("container_sub_no")
	@ApiModelProperty("容器编号")
	private String containerSubNo;
	
	@Column("order_hz_id")
	@ApiModelProperty("订单汇总Id")
	private int orderHzId;
	
	@Column("order_mx_id")
	@ApiModelProperty("订单明细Id")
	private int orderMxId;
	
	@Column("binding_num")
	@ApiModelProperty("绑定数量")
	private int bindingNum;
	
	@Column("is_finish")
	@ApiModelProperty("是否完成(0、未完成，1、完成)")
	private int isFinish;
	
	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	@Column("picking")
	@ApiModelProperty("是否正在拣选")
	private boolean picking;

	@Column("actual_num")
	@ApiModelProperty("实际数量")
	private int actualNum;


	public boolean isPicking() {
		return picking;
	}

	public void setPicking(boolean picking) {
		this.picking = picking;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the containerNo
	 */
	public String getContainerNo() {
		return containerNo;
	}

	/**
	 * @param containerNo the containerNo to set
	 */
	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	/**
	 * @return the containerSubNo
	 */
	public String getContainerSubNo() {
		return containerSubNo;
	}

	/**
	 * @param containerSubNo the containerSubNo to set
	 */
	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
	}

	/**
	 * @return the orderHzId
	 */
	public int getOrderHzId() {
		return orderHzId;
	}

	/**
	 * @param orderHzId the orderHzId to set
	 */
	public void setOrderHzId(int orderHzId) {
		this.orderHzId = orderHzId;
	}

	/**
	 * @return the orderMxId
	 */
	public int getOrderMxId() {
		return orderMxId;
	}

	/**
	 * @param orderMxId the orderMxId to set
	 */
	public void setOrderMxId(int orderMxId) {
		this.orderMxId = orderMxId;
	}

	/**
	 * @return the bindingNum
	 */
	public int getBindingNum() {
		return bindingNum;
	}

	/**
	 * @param bindingNum the bindingNum to set
	 */
	public void setBindingNum(int bindingNum) {
		this.bindingNum = bindingNum;
	}

	/**
	 * @return the isFinish
	 */
	public int getIsFinish() {
		return isFinish;
	}

	/**
	 * @param isFinish the isFinish to set
	 */
	public void setIsFinish(int isFinish) {
		this.isFinish = isFinish;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getActualNum() {
		return actualNum;
	}

	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}
}
