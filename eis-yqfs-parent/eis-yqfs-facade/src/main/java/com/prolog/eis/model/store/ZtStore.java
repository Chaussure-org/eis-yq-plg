package com.prolog.eis.model.store;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("zt_store")
public class ZtStore {

	//1：去往站台，2：等待播种，3：进入播种位，4：播种中，5：离开站台，6:入库中
	public final static int Status_To_Station = 1;
	public final static int Status_Waite_Operation = 2;
	public final static int Status_In_Operation_Location = 3;
	public final static int Status_Operation = 4;
	public final static int Status_Leave_Station = 5;
	public final static int Status_In_Store = 6;
	@Id
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("container_no")
	@ApiModelProperty("容器编号")
	private String containerNo;
	
	@Column("status")
	@ApiModelProperty("在途状态（1：去往站台，2：等待播种，3：进入播种位，4：播种中，5：离开站台，6:入库中）")
	private int status;
	
	@Column("task_type")
	@ApiModelProperty("料箱任务类型")
	private int taskType;

	@Column("task_id")
	@ApiModelProperty("任务id")
	private String taskId;

	@Column("station_id")
	@ApiModelProperty("站台Id")
	private int stationId;

	@Column("gmt_last_out_time")
	@ApiModelProperty("最后一次出库时间")
	private Date lastOutTime;
	
	@Column("gmt_store_time")
	@ApiModelProperty("第一次入库时间")
	private Date storeTime;

	@Column("gmt_last_pd_time")
	@ApiModelProperty("上次盘点时间")
	private Date lastPdTime;
	
	@Column("gmt_create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;
	
	@Column("gmt_last_update_time")
	@ApiModelProperty("最后修改时间")
	private Date lastUpdateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public Date getLastOutTime() {
		return lastOutTime;
	}

	public void setLastOutTime(Date lastOutTime) {
		this.lastOutTime = lastOutTime;
	}

	public Date getStoreTime() {
		return storeTime;
	}

	public void setStoreTime(Date storeTime) {
		this.storeTime = storeTime;
	}

	public Date getLastPdTime() {
		return lastPdTime;
	}

	public void setLastPdTime(Date lastPdTime) {
		this.lastPdTime = lastPdTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
