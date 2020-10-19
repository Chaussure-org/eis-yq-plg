package com.prolog.eis.pickstation.model;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 站台拣选单历史表
 * @author tg
 *
 */
@Table("pick_order")
public class PickOrder {

	@Id
	@Column("id")
	@AutoKey(type=AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private Integer id;
	
	@Column("station_id")
	@ApiModelProperty("站台Id")
	private int stationId;
	
	@Column("is_picking")
	@ApiModelProperty("是否当前站台正在拣选（1：是，2：否）")
	private int isPicking;
	
	@Column("is_ck_complete")
	@ApiModelProperty("是否出库完成（1：是，2：否）")
	private int isCkComplete;
	
	@Column("is_all_arrive")
	@ApiModelProperty("料箱是否全部达到（1：是，0：否）")
	private int isAllArrive;
	
	@Column("gmt_create_time")
	@ApiModelProperty("创建时间")
	private Date gmtCreateTime;
	
	@Column("gmt_update_time")
	@ApiModelProperty("修改时间")
	private Date gmtUpdateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public int getIsPicking() {
		return isPicking;
	}

	public void setIsPicking(int isPicking) {
		this.isPicking = isPicking;
	}

	public int getIsCkComplete() {
		return isCkComplete;
	}

	public void setIsCkComplete(int isCkComplete) {
		this.isCkComplete = isCkComplete;
	}

	public int getIsAllArrive() {
		return isAllArrive;
	}

	public void setIsAllArrive(int isAllArrive) {
		this.isAllArrive = isAllArrive;
	}

	public Date getGmtCreateTime() {
		return gmtCreateTime;
	}

	public void setGmtCreateTime(Date gmtCreateTime) {
		this.gmtCreateTime = gmtCreateTime;
	}

	public Date getGmtUpdateTime() {
		return gmtUpdateTime;
	}

	public void setGmtUpdateTime(Date gmtUpdateTime) {
		this.gmtUpdateTime = gmtUpdateTime;
	}
}
