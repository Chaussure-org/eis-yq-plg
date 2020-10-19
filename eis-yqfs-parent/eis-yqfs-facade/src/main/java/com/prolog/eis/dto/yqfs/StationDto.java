package com.prolog.eis.dto.yqfs;

import java.util.Date;

public class StationDto {

	// 站台ID
	private int id;


	// 提升机id
	private Integer hoisterId;

	/**
	 * 提升机编号
	 */
	private String hoisterNo;
	
	// 当前料箱
	private String currentLxNo;
	
	// 当前拣选单ID
	private Integer currentStationPickId;
	
	// 料箱最大数量
	private Integer lxMaxCount;
	
	// 是否索取：1、索取订单，2、不索取任务 3 索取盘点任务
	private int isClaim;
	
	// 是否锁定：1、锁定，2、不锁定
	private int isLock;
	
	// 站台作业类型 0:空闲  1:拣选作业  2 盘点作业
	private int stationTaskType;
	
	private Date createTime;   //创建时间
	
	private Date updateTime;   //修改时间

	private int loginUserid;	//当前登陆用户ID
	
	private String loginUsername;		//当前登陆用户姓名

	private String ipAddress;		//ip地址
	private String stationNo;

	public String getStationNo() {
		return stationNo;
	}

	public void setStationNo(String stationNo) {
		this.stationNo = stationNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getHoisterId() {
		return hoisterId;
	}

	public void setHoisterId(Integer hoisterId) {
		this.hoisterId = hoisterId;
	}

	public String getHoisterNo() {
		return hoisterNo;
	}

	public void setHoisterNo(String hoisterNo) {
		this.hoisterNo = hoisterNo;
	}

	public String getCurrentLxNo() {
		return currentLxNo;
	}

	public void setCurrentLxNo(String currentLxNo) {
		this.currentLxNo = currentLxNo;
	}

	public Integer getCurrentStationPickId() {
		return currentStationPickId;
	}

	public void setCurrentStationPickId(Integer currentStationPickId) {
		this.currentStationPickId = currentStationPickId;
	}

	public Integer getLxMaxCount() {
		return lxMaxCount;
	}

	public void setLxMaxCount(Integer lxMaxCount) {
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
