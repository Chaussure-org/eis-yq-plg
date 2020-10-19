package com.prolog.eis.dto.hxdispatch;

import java.util.ArrayList;
import java.util.List;

public class HxXiangKuDto {
	private List<HxStationDto> stationList;// 当前合箱作业站台集合
	private List<HxCengDto> cengList;
	private List<HxAssemblBoxHz> canPickAssemblBoxHzList;// 可以索取的合箱任务集合

	public HxXiangKuDto() {
		this.stationList = new ArrayList<HxStationDto>();
		this.cengList = new ArrayList<HxCengDto>();
		this.canPickAssemblBoxHzList = new ArrayList<HxAssemblBoxHz>();
	}

	public List<HxStationDto> getStationList() {
		return stationList;
	}

	public void setStationList(List<HxStationDto> stationList) {
		this.stationList = stationList;
	}

	public List<HxCengDto> getCengList() {
		return cengList;
	}

	public void setCengList(List<HxCengDto> cengList) {
		this.cengList = cengList;
	}

	public List<HxAssemblBoxHz> getCanPickAssemblBoxHzList() {
		return canPickAssemblBoxHzList;
	}

	public void setCanPickAssemblBoxHzList(List<HxAssemblBoxHz> canPickAssemblBoxHzList) {
		this.canPickAssemblBoxHzList = canPickAssemblBoxHzList;
	}

	// 重新计算站台可出库的计划
	public void RecomputeStationChuKuHz() throws Exception {
		for (HxStationDto st : this.stationList) {
			st.RecomputeChuKuHz();
		}
	}

	public void init() {
		for (HxAssemblBoxHz hz : this.canPickAssemblBoxHzList) {
			hz.RecomputeCengTaskCount();
		}

		for (HxAssemblBoxHz hz : this.canPickAssemblBoxHzList) {
			hz.RecomputeIsAllArrive();
		}
	}

	// 检查站台是否能索取新的合箱任务
	private boolean CheckStationCanTakePlan(HxStationDto st) {
		for (HxAssemblBoxHz hz : st.getAssemblBoxHzList()) {
			if (!hz.getIsAllLiaoXiangArrive()) {
				return false;
			}
		}

		int stCacheLxCount = st.getArriveLxCount() + st.getChuKuLxCount();
		if (stCacheLxCount >= st.getMaxLxCacheCount())
			return false;

		return true;
	}

	// 查找一个可以绑定新计划的最优站台
	public HxStationDto FindBindingPlanBestStation() {
		HxStationDto bestStation = null;

		for (HxStationDto st : this.getStationList()) {
			boolean canTakePlan = this.CheckStationCanTakePlan(st);

			if (canTakePlan) {
				if (bestStation == null) {
					bestStation = st;
					continue;
				}

				int stCacheLxCount = st.getArriveLxCount() + st.getChuKuLxCount();
				int bestStCacheLxCount = bestStation.getArriveLxCount() + bestStation.getChuKuLxCount();
				if (stCacheLxCount < bestStCacheLxCount)
					bestStation = st;
			}
		}

		return bestStation;
	}

	// 索取最优的合箱任务
	public HxAssemblBoxHz TakeBestTask() {
		if (this.canPickAssemblBoxHzList == null || this.canPickAssemblBoxHzList.size() == 0)
			return null;

		HxAssemblBoxHz bestHz = this.canPickAssemblBoxHzList.get(0);
		int bestHzIndex = 0;

		for (int i = 1; i < this.canPickAssemblBoxHzList.size(); i++) {
			HxAssemblBoxHz hz = this.canPickAssemblBoxHzList.get(i);

			if (hz.getAvgCengChuKuCount() < bestHz.getAvgCengChuKuCount()) {
				bestHz = hz;
				bestHzIndex = i;
			} else if (hz.getAvgCengChuKuCount() == bestHz.getAvgCengChuKuCount()) {
				if (hz.getAvgCengRuKuCount() < bestHz.getAvgCengRuKuCount()) {
					bestHz = hz;
					bestHzIndex = i;
				}
			}
		}

		this.canPickAssemblBoxHzList.remove(bestHzIndex);
		return bestHz;
	}

	private HxStationDto FindBestChuKuStation() {
		HxStationDto bestSt = null;
		for (HxStationDto st : this.getStationList()) {
			// 判断站台有无可出库的合箱任务
			if (st.getChuKuHz() != null) {
				if (bestSt == null) {
					bestSt = st;
					continue;
				}

				if (st.GetCacheLxCount() < bestSt.GetCacheLxCount()) {
					bestSt = st;
					continue;
				}
			}
		}
		return bestSt;
	}

	// 为任务最少的站台的出库计划找一个未出库的任务数最少层的料箱
	public HxChuKuResultDto FindBestHxLxResult() {
		HxStationDto bestSt = this.FindBestChuKuStation();
		if (bestSt == null)
			return null;

		HxAssemblBoxMx mx = bestSt.getChuKuHz().GetBestChuKuMx();
		if (mx == null)
			return null;

		HxChuKuResultDto result = new HxChuKuResultDto();
		result.setStation(bestSt);
		result.setHz(bestSt.getChuKuHz());
		result.setMx(mx);

		return result;
	}
}
