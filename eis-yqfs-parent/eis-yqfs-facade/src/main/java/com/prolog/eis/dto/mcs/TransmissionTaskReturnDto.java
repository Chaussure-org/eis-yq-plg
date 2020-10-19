package com.prolog.eis.dto.mcs;

public class TransmissionTaskReturnDto {
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
	 * 任务状态   1开始 2完成
	 */
	private int taskStatus;

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

	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	
}
