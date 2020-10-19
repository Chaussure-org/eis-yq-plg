package com.prolog.eis.dto.hxdispatch;

import java.util.ArrayList;
import java.util.List;

public class HxStationDto {
	private int zhanTaiId;
	// 是否锁定
	private int isLock;

	// 是否索取
	private int isClaim;

	// 在途料箱最大缓存数
	private int maxLxCacheCount;

	// 在途库存状态为出库完成（刚到达线体）的料箱数
	private int arriveLxCount;

	// 出库中或待出库的料箱数
	private int chuKuLxCount;

	private List<HxAssemblBoxHz> assemblBoxHzList;// 当前站台已索取的合箱任务

	// 以下不需查询，计算得到..............
	// 当前站台可以出库的合箱任务
	private HxAssemblBoxHz chuKuHz;

	public HxStationDto() {
		this.assemblBoxHzList = new ArrayList<HxAssemblBoxHz>();		
	}

	public int getZhanTaiId() {
		return zhanTaiId;
	}

	public void setZhanTaiId(int zhanTaiId) {
		this.zhanTaiId = zhanTaiId;
	}

	public int getIsLock() {
		return isLock;
	}

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}

	public int getIsClaim() {
		return isClaim;
	}

	public void setIsClaim(int isClaim) {
		this.isClaim = isClaim;
	}

	public int getMaxLxCacheCount() {
		return maxLxCacheCount;
	}

	public void setMaxLxCacheCount(int maxLxCacheCount) {
		this.maxLxCacheCount = maxLxCacheCount;
	}

	public int getArriveLxCount() {
		return arriveLxCount;
	}

	public void setArriveLxCount(int arriveLxCount) {
		this.arriveLxCount = arriveLxCount;
	}

	public int getChuKuLxCount() {
		return chuKuLxCount;
	}

	public void setChuKuLxCount(int chuKuLxCount) {
		this.chuKuLxCount = chuKuLxCount;
	}

	public List<HxAssemblBoxHz> getAssemblBoxHzList() {
		return assemblBoxHzList;
	}

	public void setAssemblBoxHzList(List<HxAssemblBoxHz> assemblBoxHzList) {
		this.assemblBoxHzList = assemblBoxHzList;
	}

	public HxAssemblBoxHz getChuKuHz() {
		return chuKuHz;
	}

	public void setChuKuHz(HxAssemblBoxHz chuKuHz) {
		this.chuKuHz = chuKuHz;
	}
	
	public int GetCacheLxCount() {
		return this.arriveLxCount + this.chuKuLxCount;
	}

	public void RecomputeChuKuHz() throws Exception {
		List<HxAssemblBoxHz> chuKuHzList = new ArrayList<HxAssemblBoxHz>();
		for (HxAssemblBoxHz hz : this.getAssemblBoxHzList()) {
			if (!hz.getIsAllLiaoXiangArrive())
				chuKuHzList.add(hz);
		}

		if (chuKuHzList.size() > 1)
			throw new Exception("站台" + this.getZhanTaiId() + "可出库的订单数量为" + chuKuHzList.size() + "，大于1个!");

		if (chuKuHzList.size() == 0)
			this.chuKuHz = null;
		else
			this.chuKuHz = chuKuHzList.get(0);
	}
}
