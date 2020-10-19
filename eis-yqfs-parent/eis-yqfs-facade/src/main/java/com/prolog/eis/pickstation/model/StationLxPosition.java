package com.prolog.eis.pickstation.model;

import java.util.Date;
import java.util.Objects;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("station_lx_position")
public class StationLxPosition {

	public final static int STATUS_LEISURE = 0;//空闲
	public final static int STATUS_ALLOCATION = 10;//分配箱位
	public final static int STATUS_ARRIVE = 20;//到位
	public final static int STATUS_LEAVE = 30;//离开中

	@Id
	@Column("id")
	@AutoKey(type=AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private int Id;

	@Column("position_no")
	@ApiModelProperty("作业位编号")
	private String positionNo;

	@Column("station_id")
	@ApiModelProperty("站台编号")
	private int stationId;

	@Column("position_device_no")
	@ApiModelProperty("作业位设备编号")
	private String positionDeviceNo;

	@Column("status")
	@ApiModelProperty("0 空闲 10 分配箱位 20 到位 30 离开中")
	private int status;

	@Column("container_no")
	@ApiModelProperty("容器编号")
	private String containerNo;

	@Column("container_direction")
	@ApiModelProperty("容器方向（面向人）")
	private String containerDirection;

	@Column("distribute_priority")
	@ApiModelProperty("优先级（级别高的优先分配）")
	private int distributePriority;

	@Column("gmt_distribute_time")
	@ApiModelProperty("分配时间")
	private Date gmtDistribTime;

	@Column("gmt_inplace_time")
	@ApiModelProperty("到位时间")
	private Date gmtInplaceTime;

	public StationLxPosition() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getPositionNo() {
		return positionNo;
	}

	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public String getPositionDeviceNo() {
		return positionDeviceNo;
	}

	public void setPositionDeviceNo(String positionDeviceNo) {
		this.positionDeviceNo = positionDeviceNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getContainerDirection() {
		return containerDirection;
	}

	public void setContainerDirection(String containerDirection) {
		this.containerDirection = containerDirection;
	}

	public int getDistributePriority() {
		return distributePriority;
	}

	public void setDistributePriority(int distributePriority) {
		this.distributePriority = distributePriority;
	}

	public Date getGmtDistribTime() {
		return gmtDistribTime;
	}

	public void setGmtDistribTime(Date gmtDistribTime) {
		this.gmtDistribTime = gmtDistribTime;
	}

	public Date getGmtInplaceTime() {
		return gmtInplaceTime;
	}

	public void setGmtInplaceTime(Date gmtInplaceTime) {
		this.gmtInplaceTime = gmtInplaceTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StationLxPosition that = (StationLxPosition) o;
		return Id == that.Id &&
				status == that.status &&
				distributePriority == that.distributePriority &&
				Objects.equals(positionNo, that.positionNo) &&
				Objects.equals(stationId, that.stationId) &&
				Objects.equals(positionDeviceNo, that.positionDeviceNo) &&
				Objects.equals(containerNo, that.containerNo) &&
				Objects.equals(containerDirection, that.containerDirection) &&
				Objects.equals(gmtDistribTime, that.gmtDistribTime) &&
				Objects.equals(gmtInplaceTime, that.gmtInplaceTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(Id, positionNo, stationId, positionDeviceNo, status, containerNo, containerDirection, distributePriority, gmtDistribTime, gmtInplaceTime);
	}

	@Override
	public String toString() {
		return "StationLxPosition{" +
				"Id=" + Id +
				", positionNo='" + positionNo + '\'' +
				", stationId='" + stationId + '\'' +
				", positionDeviceNo='" + positionDeviceNo + '\'' +
				", status=" + status +
				", containerNo='" + containerNo + '\'' +
				", containerDirection='" + containerDirection + '\'' +
				", distributePriority=" + distributePriority +
				", gmtDistribTime=" + gmtDistribTime +
				", gmtInplaceTime=" + gmtInplaceTime +
				'}';
	}
}