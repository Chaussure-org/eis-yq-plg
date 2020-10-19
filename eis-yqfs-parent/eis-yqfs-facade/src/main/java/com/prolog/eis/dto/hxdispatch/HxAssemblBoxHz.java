package com.prolog.eis.dto.hxdispatch;

import java.util.List;

public class HxAssemblBoxHz {
	private int hzId;
	private int spId;
	private int stationId;
	
	private List<HxAssemblBoxMx> mxList;
	
	//以下不需查询，计算得到..............
	private boolean isAllLiaoXiangArrive;
	private double avgCengChuKuCount;
	private double avgCengRuKuCount;
	
	public HxAssemblBoxHz() {
		this.isAllLiaoXiangArrive = false;//默认料箱未到齐
	}

	public int getHzId() {
		return hzId;
	}

	public void setHzId(int hzId) {
		this.hzId = hzId;
	}

	public int getSpId() {
		return spId;
	}

	public void setSpId(int spId) {
		this.spId = spId;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public boolean getIsAllLiaoXiangArrive() {
		return isAllLiaoXiangArrive;
	}

	public void setIsAllLiaoXiangArrive(boolean isAllLiaoXiangArrive) {
		this.isAllLiaoXiangArrive = isAllLiaoXiangArrive;
	}

	public double getAvgCengChuKuCount() {
		return avgCengChuKuCount;
	}

	public void setAvgCengChuKuCount(double avgCengChuKuCount) {
		this.avgCengChuKuCount = avgCengChuKuCount;
	}

	public double getAvgCengRuKuCount() {
		return avgCengRuKuCount;
	}

	public void setAvgCengRuKuCount(double avgCengRuKuCount) {
		this.avgCengRuKuCount = avgCengRuKuCount;
	}

	public List<HxAssemblBoxMx> getMxList() {
		return mxList;
	}

	public void setMxList(List<HxAssemblBoxMx> mxList) {
		this.mxList = mxList;
	}
	
	public void RecomputeCengTaskCount() {
		int totalCengChuKuCount = 0;
		int totalCengRuKuCount = 0;

//		if(mxList!=null){
//			for(HxAssemblBoxMx mx : this.mxList) {
//				totalCengChuKuCount += mx.getHxCeng().getCkLxCount();
//				totalCengRuKuCount += mx.getHxCeng().getRkLxCount();
//			}
//		}

		
		this.avgCengChuKuCount = (double)totalCengChuKuCount / this.mxList.size();
		this.avgCengRuKuCount = (double)totalCengRuKuCount / this.mxList.size();
	}
	
	public void RecomputeIsAllArrive() {
		for(HxAssemblBoxMx mx : this.mxList) {
			if(mx.getTaskState() == 0) {
				this.isAllLiaoXiangArrive = false;
				return;
			}else if(mx.getTaskState() == 1) {
				if(mx.getIsArrive() != 1) {
					this.isAllLiaoXiangArrive = false;
					return;
				}
			}
		}
		
		this.isAllLiaoXiangArrive = true;
	}
	
	public HxAssemblBoxMx GetBestChuKuMx() {
		return null;
	}
}
