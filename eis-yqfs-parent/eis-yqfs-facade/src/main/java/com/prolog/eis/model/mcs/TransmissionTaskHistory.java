package com.prolog.eis.model.mcs;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("transmission_task_history")
public class TransmissionTaskHistory {
	@Id
	@Column("id")
	private int id;
	
	@Column("task_id")
	@ApiModelProperty("任务Id")
	private String taskId;
	
	@Column("address")
	@ApiModelProperty("请求位置")
	private String address;
	
	@Column("container_no")
	@ApiModelProperty("容器号")
	private String containerNo;
	
	@Column("type")
	@ApiModelProperty("类型1.入库；2.出库；3.拣选站")
	private int type;
	
	@Column("target")
	@ApiModelProperty("目的位置")
	private String target;
	
	/**
	 * 任务状态(1.完成、2.失败)
	 */
	@Column("task_state")
	private int taskState;
	
	@Column("send_count")
	private int sendCount;	
	
	@Column("err_msg")
	private String errMsg;
	
	@Column("create_time")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getTaskState() {
		return taskState;
	}

	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
