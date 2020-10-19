package com.prolog.eis.dto.mcs;

public class TransmissionSendTaskDto {

	/**
	 * 任务Id
	 */
	private String taskId;
	
	/**
	 * 请求位置
	 */
	private String address;
	
	/**
	 * 容器号
	 */
	private String containerNo;
	
	/**
	 * 类型1.入库；2.出库；3.拣选站
	 */
	private int type;
	
	/**
	 * 目的位置
	 */
	private String target;

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

	@Override
	public String toString() {
		return "TransmissionSendTaskDto [taskId=" + taskId + ", address=" + address + ", containerNo=" + containerNo
				+ ", type=" + type + ", target=" + target + "]";
	}

	public TransmissionSendTaskDto(String taskId, String address, String containerNo, int type, String target) {
		super();
		this.taskId = taskId;
		this.address = address;
		this.containerNo = containerNo;
		this.type = type;
		this.target = target;
	}

	public TransmissionSendTaskDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
