package com.prolog.eis.dto.hxdispatch;

public class HxAssemblBoxMx {
	private int assemblBoxHzId;
	private String lxNo;
	private int ceng;//料箱所在的层
	private int taskState;//任务状态 0未开始 1进行中 2已完成
	private int isArrive;//是否到达线体（在途库存）（0 未到达  1已到达）
	
	//以下为计算数据.............
	private HxCengDto hxCeng;

	public int getAssemblBoxHzId() {
		return assemblBoxHzId;
	}

	public void setAssemblBoxHzId(int assemblBoxHzId) {
		this.assemblBoxHzId = assemblBoxHzId;
	}

	public String getLxNo() {
		return lxNo;
	}

	public void setLxNo(String lxNo) {
		this.lxNo = lxNo;
	}

	public int getCeng() {
		return ceng;
	}

	public void setCeng(int ceng) {
		this.ceng = ceng;
	}

	public int getTaskState() {
		return taskState;
	}

	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}

	public int getIsArrive() {
		return isArrive;
	}

	public void setIsArrive(int isArrive) {
		this.isArrive = isArrive;
	}

	public HxCengDto getHxCeng() {
		return hxCeng;
	}

	public void setHxCeng(HxCengDto hxCeng) {
		this.hxCeng = hxCeng;
	}
}
