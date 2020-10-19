package com.prolog.eis.model.pd;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("pd_task_detail")
public class PdTaskDetail {
	  
	@Id
	@Column("id")
	@ApiModelProperty("id")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("pd_task_id")
	@ApiModelProperty("盘点任务id")
	private int pdTaskId;
	
	@Column("container_no")
	@ApiModelProperty("箱号")
	private String containerNo;
	
	@Column("container_sub_no")
	@ApiModelProperty("货格号")
	private String containerSubNo;
	
	@Column("goods_no")
	@ApiModelProperty("商品编号")
	private String goodsNo;
	
	@Column("goods_name")
	@ApiModelProperty("商品名称")
	private String goodsName;
	
	@Column("bar_code")
	@ApiModelProperty("商品条码")
	private String barCode;
	
	@Column("original_count")
	@ApiModelProperty("原始数量")
	private int originalCount;
	
	@Column("modify_count")
	@ApiModelProperty("修改数量")
	private int modifyCount;
	
	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime; 
	
	@Column("task_state")
	@ApiModelProperty("任务状态 0新建 10下发 20已出库 30进行中 40已完成")
	private int taskState;
	
	@Column("publish_time")
	@ApiModelProperty("下发时间")
	private Date publishTime;
	
	@Column("outbound_time")
	@ApiModelProperty("出库时间")
	private Date outboundTime;
	
	@Column("pd_start_time")
	@ApiModelProperty("盘点开始时间")
	private Date pdStartTime;
	
	@Column("pd_end_time")
	@ApiModelProperty("盘点结束时间")
	private Date pdEndTime;
	
	@Column("finish_reason")
	@ApiModelProperty("结束原因")
	private String finishReason;

	@ApiModelProperty("盘点类型[1:静态盘点,2:动态盘点]")
	@Column("pd_type")
	private int pdType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPdTaskId() {
		return pdTaskId;
	}

	public void setPdTaskId(int pdTaskId) {
		this.pdTaskId = pdTaskId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getContainerSubNo() {
		return containerSubNo;
	}

	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
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

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getOriginalCount() {
		return originalCount;
	}

	public void setOriginalCount(int originalCount) {
		this.originalCount = originalCount;
	}

	public int getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(int modifyCount) {
		this.modifyCount = modifyCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getTaskState() {
		return taskState;
	}

	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Date getOutboundTime() {
		return outboundTime;
	}

	public void setOutboundTime(Date outboundTime) {
		this.outboundTime = outboundTime;
	}

	public Date getPdStartTime() {
		return pdStartTime;
	}

	public void setPdStartTime(Date pdStartTime) {
		this.pdStartTime = pdStartTime;
	}

	public Date getPdEndTime() {
		return pdEndTime;
	}

	public void setPdEndTime(Date pdEndTime) {
		this.pdEndTime = pdEndTime;
	}

	public String getFinishReason() {
		return finishReason;
	}

	public void setFinishReason(String finishReason) {
		this.finishReason = finishReason;
	}

	public int getPdType() {
		return pdType;
	}

	public void setPdType(int pdType) {
		this.pdType = pdType;
	}
}
