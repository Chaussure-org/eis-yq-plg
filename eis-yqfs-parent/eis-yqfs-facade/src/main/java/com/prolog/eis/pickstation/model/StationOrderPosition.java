package com.prolog.eis.pickstation.model;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("station_order_position")
public class StationOrderPosition {

	public static final short STATUS_IDLE = 0;
	public static final short STATUS_DISTRIBUTE = 10;
	public static final short STATUS_INPLACE = 20;
	public static final short STATUS_LEAVING = 30;

	@Id
	@Column("id")
	@AutoKey(type=AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private int id;

	@Column("position_no")
	@ApiModelProperty("作业位编号")
	private String positionNo;
	
	@Column("station_id")
	@ApiModelProperty("站台id")
	private int stationId;
	
	@Column("device_no")
	@ApiModelProperty("作业位设备编号")
	private String deviceNo;

	@Column("light_no")
	@ApiModelProperty("灯编号")
	private String lightNo;
	
	@Column("status")
	@ApiModelProperty("0 空闲 10 分配箱位 20 到位 30 离开中")
	private  short status;

	@Column("order_id")
	@ApiModelProperty("订单id")
	private int orderId;
	
	@Column("container_no")
	@ApiModelProperty("容器编号")
	private String containerNo;
	
	@Column("distribute_priority")
	@ApiModelProperty("优先级（级别高的优先分配）")
	private int distributePriority;
	
	@Column("gmt_distribute_time")
	@ApiModelProperty("分配时间")
	private Date gmtDistributeTime;
	
	@Column("gmt_inplace_time")
	@ApiModelProperty("到位时间")
	private Date gmtInplaceTime;

	@Column("is_change")
	@ApiModelProperty("是否截箱")
	private boolean changed;

	@Column("gmt_last_bz_time")
	private Date gmtLastBzTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getLightNo() {
		return lightNo;
	}

	public void setLightNo(String lightNo) {
		this.lightNo = lightNo;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public int getDistributePriority() {
		return distributePriority;
	}

	public void setDistributePriority(int distributePriority) {
		this.distributePriority = distributePriority;
	}

	public Date getGmtDistributeTime() {
		return gmtDistributeTime;
	}

	public void setGmtDistributeTime(Date gmtDistributeTime) {
		this.gmtDistributeTime = gmtDistributeTime;
	}

	public Date getGmtInplaceTime() {
		return gmtInplaceTime;
	}

	public void setGmtInplaceTime(Date gmtInplaceTime) {
		this.gmtInplaceTime = gmtInplaceTime;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public Date getGmtLastBzTime() {
		return gmtLastBzTime;
	}

	public void setGmtLastBzTime(Date gmtLastBzTime) {
		this.gmtLastBzTime = gmtLastBzTime;
	}
}
