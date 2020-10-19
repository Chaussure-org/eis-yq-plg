package com.prolog.eis.model.order;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 出库单明细表历史
 * @author tg
 *
 */
@Table("order_mx_history")
public class OrderMxHistory {

	@Column("id")
	@ApiModelProperty("id")
	private Integer id;

	@Column("outbound_task_hz_id")
	@ApiModelProperty("出库单汇总id")
	private Integer outboundTaskHzId;

	@Column("goods_id")
	@ApiModelProperty("商品id")
	private Integer goodsId;

	@Column("plan_num")
	@ApiModelProperty("计划数量")
	private Integer planNum;

	@Column("actual_num")
	@ApiModelProperty("实际数量")
	private Integer actualNum;

	@Column("is_complete")
	@ApiModelProperty("是否完成：（0：否，1：是）")
	private Integer isComplete;

	@Column("is_not_pick")
	@ApiModelProperty("是否短拣：（0：否，1：是）")
	private Integer isNotPick;

	@Column("create_time")
	@ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
	private Date createTime;

	@Column("update_time")
	@ApiModelProperty("修改时间（YYYY-MM-DD HH:MM:SS）")
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOutboundTaskHzId() {
		return outboundTaskHzId;
	}

	public void setOutboundTaskHzId(Integer outboundTaskHzId) {
		this.outboundTaskHzId = outboundTaskHzId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getPlanNum() {
		return planNum;
	}

	public void setPlanNum(Integer planNum) {
		this.planNum = planNum;
	}

	public Integer getActualNum() {
		return actualNum;
	}

	public void setActualNum(Integer actualNum) {
		this.actualNum = actualNum;
	}

	public Integer getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}

	public Integer getIsNotPick() {
		return isNotPick;
	}

	public void setIsNotPick(Integer isNotPick) {
		this.isNotPick = isNotPick;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public OrderMxHistory(Integer id, Integer outboundTaskHzId, Integer goodsId, Integer planNum,
						  Integer actualNum, Integer isComplete, Integer isNotPick, Date createTime, Date updateTime) {
		super();
		this.id = id;
		this.outboundTaskHzId = outboundTaskHzId;
		this.goodsId = goodsId;
		this.planNum = planNum;
		this.actualNum = actualNum;
		this.isComplete = isComplete;
		this.isNotPick = isNotPick;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public OrderMxHistory() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
