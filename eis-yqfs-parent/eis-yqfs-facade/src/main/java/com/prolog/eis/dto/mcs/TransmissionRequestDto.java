package com.prolog.eis.dto.mcs;

public class TransmissionRequestDto {

	/**
	 * taskId
	 */
	private String taskId;
	
	/**
	 * 位置信息
	 */
	private String address;
	
	/**
	 * 容器号
	 */
	private String containerNo;
	
	/**
	 * 外形检测
	 */
	private String shapeInspect;
	
	/**
	 * 入库重量
	 */
	private String weight;

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

	public String getShapeInspect() {
		return shapeInspect;
	}

	public void setShapeInspect(String shapeInspect) {
		this.shapeInspect = shapeInspect;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	
	
}
