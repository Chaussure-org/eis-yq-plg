package com.prolog.eis.dto.hxdispatch;

public class HxCengDto {
	private int ceng;	
	private int ckLxCount;//出库料箱数
	private int rkLxCount;//入库料箱数
	
	public int getCeng() {
		return ceng;
	}
	public void setCeng(int ceng) {
		this.ceng = ceng;
	}
	public int getCkLxCount() {
		return ckLxCount;
	}
	public void setCkLxCount(int ckLxCount) {
		this.ckLxCount = ckLxCount;
	}
	public int getRkLxCount() {
		return rkLxCount;
	}
	public void setRkLxCount(int rkLxCount) {
		this.rkLxCount = rkLxCount;
	}
}
