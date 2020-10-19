package com.prolog.eis.dto.bhdispatch;

public class BuHuoLxDto {

	private String lxNo;
	private int huoWeiId;
	private int huoWeiGroupId;//货位组Id
	private int ceng;
	private int huoWeiX;
	private int huoWeiY;	
	private int depth;//货位深度
	private int moveNum;//移位次数
	
	private int priority;//优先级（计算得到）

	public String getLxNo() {
		return lxNo;
	}

	public void setLxNo(String lxNo) {
		this.lxNo = lxNo;
	}

	public int getHuoWeiId() {
		return huoWeiId;
	}

	public void setHuoWeiId(int huoWeiId) {
		this.huoWeiId = huoWeiId;
	}

	public int getHuoWeiGroupId() {
		return huoWeiGroupId;
	}

	public void setHuoWeiGroupId(int huoWeiGroupId) {
		this.huoWeiGroupId = huoWeiGroupId;
	}

	public int getCeng() {
		return ceng;
	}

	public void setCeng(int ceng) {
		this.ceng = ceng;
	}

	public int getHuoWeiX() {
		return huoWeiX;
	}

	public void setHuoWeiX(int huoWeiX) {
		this.huoWeiX = huoWeiX;
	}

	public int getHuoWeiY() {
		return huoWeiY;
	}

	public void setHuoWeiY(int huoWeiY) {
		this.huoWeiY = huoWeiY;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getMoveNum() {
		return moveNum;
	}

	public void setMoveNum(int moveNum) {
		this.moveNum = moveNum;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
