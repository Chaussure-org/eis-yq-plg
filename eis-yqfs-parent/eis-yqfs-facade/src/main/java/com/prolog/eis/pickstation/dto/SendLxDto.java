package com.prolog.eis.pickstation.dto;

import java.util.List;
import java.util.Objects;

public class SendLxDto {

	/**
	 * 容器编号
	 */
	private String containerNo;
	
	/**
	 * 料箱类型
	 */
	private int layoutType;
	
	/**
	 * 站台编号
	 */
	private int stationId;
	
	/**
	 * 位置
	 */
	private int localtion;
	
	/**
	 * 料箱朝向(面向人)
	 */
	private String orientation;
	/**
	 * 任务类型(1 播种任务 2 盘点任务 3 退库任务 4 补货任务 5 入库换箱 6.合箱任务 7 空托盘入库任务')
	 */
	private int taskType;
	
	
	private List<SendLxDetailsDto> pickContainerSubInfos;


	public SendLxDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SendLxDto(String containerNo, int layoutType, int stationId, int localtion, String orientation, int taskType, List<SendLxDetailsDto> pickContainerSubInfos) {
		this.containerNo = containerNo;
		this.layoutType = layoutType;
		this.stationId = stationId;
		this.localtion = localtion;
		this.orientation = orientation;
		this.taskType = taskType;
		this.pickContainerSubInfos = pickContainerSubInfos;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public int getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(int layoutType) {
		this.layoutType = layoutType;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public int getLocaltion() {
		return localtion;
	}

	public void setLocaltion(int localtion) {
		this.localtion = localtion;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public List<SendLxDetailsDto> getPickContainerSubInfos() {
		return pickContainerSubInfos;
	}

	public void setPickContainerSubInfos(List<SendLxDetailsDto> pickContainerSubInfos) {
		this.pickContainerSubInfos = pickContainerSubInfos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SendLxDto sendLxDto = (SendLxDto) o;
		return layoutType == sendLxDto.layoutType &&
				localtion == sendLxDto.localtion &&
				taskType == sendLxDto.taskType &&
				stationId ==sendLxDto.stationId&&
				Objects.equals(containerNo, sendLxDto.containerNo) &&
				Objects.equals(orientation, sendLxDto.orientation) &&
				Objects.equals(pickContainerSubInfos, sendLxDto.pickContainerSubInfos);
	}

	@Override
	public int hashCode() {
		return Objects.hash(containerNo, layoutType, stationId, localtion, orientation, taskType, pickContainerSubInfos);
	}

	@Override
	public String toString() {
		return "SendLxDto{" +
				"containerNo='" + containerNo + '\'' +
				", layoutType=" + layoutType +
				", stationId='" + stationId + '\'' +
				", localtion='" + localtion + '\'' +
				", orientation='" + orientation + '\'' +
				", taskType=" + taskType +
				", pickContainerSubInfos=" + pickContainerSubInfos +
				'}';
	}
}
