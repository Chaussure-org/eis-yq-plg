package com.prolog.eis.model.store;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("distribute_roadway")
public class DistributeRoadway {

	@Column("container_no")
	@ApiModelProperty("容器号")
	private String containerNo;
	
	@Column("inBound_time")
	@ApiModelProperty("分配时间")
	private Date inBoundTime;
	
	@Column("roadway")
	@ApiModelProperty("巷道")
	private int roadway;

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public Date getInBoundTime() {
		return inBoundTime;
	}

	public void setInBoundTime(Date inBoundTime) {
		this.inBoundTime = inBoundTime;
	}

	public int getRoadway() {
		return roadway;
	}

	public void setRoadway(int roadway) {
		this.roadway = roadway;
	}

	@Override
	public String toString() {
		return "DistributeRoadway [containerNo=" + containerNo + ", inBoundTime=" + inBoundTime + ", roadway=" + roadway
				+ "]";
	}

	public DistributeRoadway() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DistributeRoadway(String containerNo, Date inBoundTime, int roadway) {
		super();
		this.containerNo = containerNo;
		this.inBoundTime = inBoundTime;
		this.roadway = roadway;
	}
	
	
}
