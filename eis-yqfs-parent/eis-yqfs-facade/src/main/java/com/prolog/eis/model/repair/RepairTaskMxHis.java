package com.prolog.eis.model.repair;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 补货明细表历史
 * 
 * @author tg
 *
 */
@Table("repair_task_mx_his")
public class RepairTaskMxHis {

	@Id
	@Column("id")
	@ApiModelProperty("id")
	private Integer id;

	@Column("repair_task_hz_id")
	@ApiModelProperty("入库单汇总id")
	private Integer repairTaskHzId;

	@Column("container_sub_no")
	@ApiModelProperty("子容器编号")
	private String containerSubNo;

	@Column("goods_id")
	@ApiModelProperty("商品id")
	private Integer goodsId;

	@Column("goods_num")
	@ApiModelProperty("商品数量")
	private Integer goodsNum;

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

	public Integer getRepairTaskHzId() {
		return repairTaskHzId;
	}

	public void setRepairTaskHzId(Integer repairTaskHzId) {
		this.repairTaskHzId = repairTaskHzId;
	}

	public String getContainerSubNo() {
		return containerSubNo;
	}

	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
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
	

}
