package com.prolog.eis.model.store;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("Sx_Car")
public class SxCar {

	@Id
	@ApiModelProperty("小车编号")
	private String id;

	@Column("LAYER")
	@ApiModelProperty("层")
	private int layer;
	
	@Column("CAR_STATE")
	@ApiModelProperty("小车状态(1.工作，2.空闲,3.跨层任务执行中,4 故障中)")
	private int carState;

	@Column("belong_area")
	@ApiModelProperty("所属区域")
	private int belongArea;

	@Column("curr_coord")
	@ApiModelProperty("当前坐标")
	private String currCoord;

	@Column("alarm")
	@ApiModelProperty("故障信息 0 正常, 非0都是异常")
	private int alarm;

	@Column("last_update_time")
	@ApiModelProperty("最后更新时间")
	private Date lastUpdateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getCarState() {
		return carState;
	}

	public void setCarState(int carState) {
		this.carState = carState;
	}

	public int getBelongArea() {
		return belongArea;
	}

	public void setBelongArea(int belongArea) {
		this.belongArea = belongArea;
	}

	public String getCurrCoord() {
		return currCoord;
	}

	public void setCurrCoord(String currCoord) {
		this.currCoord = currCoord;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	public String getBelongAreaAndlayer(){
		return layer + "/" + belongArea;
	}
}
