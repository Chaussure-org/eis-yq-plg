package com.prolog.eis.pickstation.model;

import java.util.Date;

import com.prolog.eis.model.base.BaseModel;
import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Table("station")
@ApiModel
public class Station extends BaseModel{

	@Id
	@Column("id")
	@AutoKey(type=AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("站台ID")
	private int id;

	@Column("station_no")
	@ApiModelProperty("站台编号")
	private String stationNo;

	
	@ApiModelProperty("当前料箱")
	@Column("current_lx_no")
	private String currentLxNo;
	
	@ApiModelProperty("当前拣选单ID")
	@Column("current_station_pick_id")
	private int currentStationPickId;
	
	@ApiModelProperty("料箱最大数量")
	@Column("lx_max_count")
	private int lxMaxCount;
	
	@Column("is_claim")
	@ApiModelProperty("是否索取：1、索取订单，2、不索取任务 ")
	private int isClaim;
	
	@Column("is_lock")
	@ApiModelProperty("是否锁定：1、锁定，2、不锁定")
	private int isLock;	 //是否锁定：1、锁定，2、不锁定
	
	@Column("station_task_type")
	@ApiModelProperty("站台作业类型")
	private int stationTaskType;
	
	@ApiModelProperty("创建时间")
	@Column("create_time")
	private Date createTime;   //创建时间
	
	@ApiModelProperty("修改时间")
	@Column("update_time")
	private Date updateTime;   //修改时间

	@Column("login_user_id")
	@ApiModelProperty("当前登陆用户")
	private int loginUserid;	//当前登陆用户ID
	
	@Column("login_username")
	@ApiModelProperty("当前登陆用户姓名")
	private String loginUsername;		//当前登陆用户姓名

	@Column("ip_address")
	@ApiModelProperty("站台ip地址")
	private String ipAddress;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStationNo() {
		return stationNo;
	}

	public void setStationNo(String stationNo) {
		this.stationNo = stationNo;
	}

	public String getCurrentLxNo() {
		return currentLxNo;
	}

	public void setCurrentLxNo(String currentLxNo) {
		this.currentLxNo = currentLxNo;
	}

	public int getCurrentStationPickId() {
		return currentStationPickId;
	}

	public void setCurrentStationPickId(int currentStationPickId) {
		this.currentStationPickId = currentStationPickId;
	}

	public int getLxMaxCount() {
		return lxMaxCount;
	}

	public void setLxMaxCount(int lxMaxCount) {
		this.lxMaxCount = lxMaxCount;
	}

	public int getIsClaim() {
		return isClaim;
	}

	public void setIsClaim(int isClaim) {
		this.isClaim = isClaim;
	}

	public int getIsLock() {
		return isLock;
	}

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}

	public int getStationTaskType() {
		return stationTaskType;
	}

	public void setStationTaskType(int stationTaskType) {
		this.stationTaskType = stationTaskType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getLoginUserid() {
		return loginUserid;
	}

	public void setLoginUserid(int loginUserid) {
		this.loginUserid = loginUserid;
	}

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
