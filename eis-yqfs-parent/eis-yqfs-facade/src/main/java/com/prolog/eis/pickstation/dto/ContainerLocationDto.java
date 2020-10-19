package com.prolog.eis.pickstation.dto;

import java.util.Objects;

public class ContainerLocationDto {

	/**
	 * 是否进入拣选站
	 */
	private boolean isEnter = false;
	
	/**
	 * 容器编号
	 */
	private String containerNo;

	/**
	 * 拣选站ID
	 */
	private int stationId;
	
	/**
	 * 分配容器拣选站位置
	 */
	private String locationNo;
	
	/**
	 * 起点位置
	 */
	private String startLocationNo;
	

	public ContainerLocationDto() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ContainerLocationDto(boolean isEnter, String containerNo, int stationId, String locationNo,
			String startLocationNo) {
		super();
		this.isEnter = isEnter;
		this.containerNo = containerNo;
		this.stationId = stationId;
		this.locationNo = locationNo;
		this.startLocationNo = startLocationNo;
	}


	public boolean isEnter() {
		return isEnter;
	}


	public void setEnter(boolean isEnter) {
		this.isEnter = isEnter;
	}


	public String getContainerNo() {
		return containerNo;
	}


	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}


	public int getStationId() {
		return stationId;
	}


	public void setStationId(int stationNo) {
		this.stationId = stationId;
	}


	public String getLocationNo() {
		return locationNo;
	}


	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}


	public String getStartLocationNo() {
		return startLocationNo;
	}


	public void setStartLocationNo(String startLocationNo) {
		this.startLocationNo = startLocationNo;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ContainerLocationDto that = (ContainerLocationDto) o;
		return isEnter == that.isEnter &&
				stationId == that.stationId &&
				Objects.equals(containerNo, that.containerNo) &&
				Objects.equals(locationNo, that.locationNo) &&
				Objects.equals(startLocationNo, that.startLocationNo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(isEnter, containerNo, stationId, locationNo, startLocationNo);
	}

	@Override
	public String toString() {
		return "ContainerLocationDto [isEnter=" + isEnter + ", containerNo=" + containerNo + ", stationId=" + stationId
				+ ", locationNo=" + locationNo + ", startLocationNo=" + startLocationNo + "]";
	}

}
