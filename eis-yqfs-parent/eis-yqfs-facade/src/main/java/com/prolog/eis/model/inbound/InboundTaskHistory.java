package com.prolog.eis.model.inbound;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 入库汇总历史表
 * @author tg
 *
 */
@Table("inbound_task_his")
public class InboundTaskHistory {
	
	@Id
	@ApiModelProperty("id")
	@Column("id")
	private int id;
	
	@Column("container_no")
	@ApiModelProperty("容器编号")
	private String containerNo;

	@Column("operator")
	@ApiModelProperty("操作人")
	private String operator;

	@Column("inbound_status")
	@ApiModelProperty("状态（1：进行中，2：已完成）")
	private int inboundStatus;

	@Column("create_time")
	@ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getInboundStatus() {
		return inboundStatus;
	}

	public void setInboundStatus(int inboundStatus) {
		this.inboundStatus = inboundStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public InboundTaskHistory(int id, String containerNo, String operator, int inboundStatus, Date createTime) {
		super();
		this.id = id;
		this.containerNo = containerNo;
		this.operator = operator;
		this.inboundStatus = inboundStatus;
		this.createTime = createTime;
	}

	public InboundTaskHistory() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
