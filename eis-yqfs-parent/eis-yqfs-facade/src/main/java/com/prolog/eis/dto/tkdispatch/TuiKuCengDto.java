package com.prolog.eis.dto.tkdispatch;

import java.util.ArrayList;
import java.util.List;

public class TuiKuCengDto {
	private int ceng;	
	private int ckLxCount;//出库料箱数
	private int rkLxCount;//入库料箱数
	private List<TuiKuLxDto> lxList;//当前巷道、层的所有盘点任务的料箱（排除锁定）
	
	public TuiKuCengDto() {
		this.lxList = new ArrayList<TuiKuLxDto>();
	}

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

	public List<TuiKuLxDto> getLxList() {
		return lxList;
	}
	public void setLxList(List<TuiKuLxDto> lxList) {
		this.lxList = lxList;
	}
}
