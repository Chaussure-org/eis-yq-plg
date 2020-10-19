package com.prolog.eis.model.point;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("POINT_LOCATION")
public class PointLocation {

	public static final int TYPE_IN =1; //入库接驳口
	public static final int TYPE_OUT =2; //出库接驳口
	public static final int TYPE_CONTAINER_BCR =3; //料箱bcr
	public static final int TYPE_ORDER_BOX_BCR =4; //订单箱bcr
	public static final int TYPE_CONTAINER =5; //料箱位
	public static final int TYPE_ORDER_BOX =6; //订单框位
	public static final int TYPE_SHAPE_INSPECT =7; //外形检测
	public static final int TYPE_EXCEPTION_CONTAINER=8; //异常出库口
	public static final int TYPE_EXCEPTION_ORDER_BOX=9; //异常出库口
	public static final int TYPE_IN_BCR=10; //入库BCR
	public static final int TYPE_OUT_STORE=11; //出库箱库坐标
	public static final int TYPE_IN_STORE=12; //入库箱库坐标



	@Column("POINT_ID")
	@ApiModelProperty("点位ID")
	@Id
	private String pointId;
	
	@Column("POINT_NAME")
	@ApiModelProperty("POINT名称")
	private String pointName;
	
	@Column("POINT_TYPE")
	@ApiModelProperty("点位类型")
	private int pointType;
	
	@Column("STATION_ID")
	@ApiModelProperty("站台ID")
	private Integer stationId;
	@Column("door_no")
	@ApiModelProperty("安全门编号")
	private String doorNo;
	@Column("disable")
	@ApiModelProperty("是否禁用")
	private boolean disable;


	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public int getPointType() {
		return pointType;
	}

	public void setPointType(int pointType) {
		this.pointType = pointType;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public String getDoorNo() {
		return doorNo;
	}

	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	@Override
	public String toString() {
		return "PointLocation{" +
				"pointId='" + pointId + '\'' +
				", pointName='" + pointName + '\'' +
				", pointType=" + pointType +
				", stationId=" + stationId +
				", doorNo=" + doorNo +
				", disable=" + disable +
				'}';
	}
}
