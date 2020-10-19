package com.prolog.eis.model.store;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("sx_store")
public class SxStore {

	//10：入库中、 20：已上架、 30：出库中、31:待出库、40：移位中
	public static final int STATE_IDLE =0;
	public static final int STATE_IN =10;
	public static final int STATE_UP =20;
	public static final int STATE_OUT =30;
	public static final int STATE_OUT_WAITING =31;
	public static final int STATE_MOVING =40;

	public static final int TASKTYPE_NULL =0;
	public static final int TASKTYPE_BH =10;
	public static final int TASKTYPE_BZ =20;
	public static final int TASKTYPE_HX =30;
	public static final int TASKTYPE_PD =40;
	public static final int TASKTYPE_TK =50;
	public static final int TASKTYPE_IN =60;

	@Id
	@AutoKey(type=AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("四向库库存表")
	private int id;		//四向库库存表
	
	@Column("container_no")
	@ApiModelProperty("容器编号")
	private String containerNo;		//容器编号

	@Column("store_location_id")
	@ApiModelProperty("货位ID")
	private Integer storeLocationId;		//货位ID

	@Column("task_type")
	@ApiModelProperty("任务类型")
	private int taskType;		
	
	@Column("store_state")
	@ApiModelProperty("库存状态(10：入库中、 20：已上架、 30：出库中、31:待出库、40：移位中)")
	private int storeState;		

	@Column("store_time")
	@ApiModelProperty("入库时间")
	private Date storeTime;	//入库时间

	@Column("task_id")
	@ApiModelProperty("任务Id")
	private String taskId;		//任务Id
	

	@Column("source_location_id")
	@ApiModelProperty("源货位Id(移位用)")
	private Integer sourceLocationId;		//源货位Id(移位用)
	
	@Column("gmt_create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;	//创建时间

	@Column("station_id")
	@ApiModelProperty("站台Id")
	private int stationId;	//站台Id

	@Column("gmt_last_out_time")
	@ApiModelProperty("上次盘点时间")
	private Date lastOutTime;

	@Column("gmt_last_pd_time")
	@ApiModelProperty("上次盘点时间")
	private Date lastPdTime;

	@Column("gmt_last_update_time")
	@ApiModelProperty("上次更新时间")
	private Date lastUpdateTime;

	@Column("hoister_id")
	@ApiModelProperty("入库提升机号")
	private String hoisterId;

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

	public Integer getStoreLocationId() {
		return storeLocationId;
	}

	public void setStoreLocationId(Integer storeLocationId) {
		this.storeLocationId = storeLocationId;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getStoreState() {
		return storeState;
	}

	public void setStoreState(int storeState) {
		this.storeState = storeState;
	}

	public Date getStoreTime() {
		return storeTime;
	}

	public void setStoreTime(Date storeTime) {
		this.storeTime = storeTime;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getSourceLocationId() {
		return sourceLocationId;
	}

	public void setSourceLocationId(Integer sourceLocationId) {
		this.sourceLocationId = sourceLocationId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public Date getLastPdTime() {
		return lastPdTime;
	}

	public void setLastPdTime(Date lastPdTime) {
		this.lastPdTime = lastPdTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getHoisterId() {
		return hoisterId;
	}

	public void setHoisterId(String hoisterId) {
		this.hoisterId = hoisterId;
	}
}
