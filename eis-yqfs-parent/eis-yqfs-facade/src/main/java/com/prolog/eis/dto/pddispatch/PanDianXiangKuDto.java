package com.prolog.eis.dto.pddispatch;

import java.util.ArrayList;
import java.util.List;

public class PanDianXiangKuDto {
	private List<PanDianCengDto> cengList;
	private List<PanDianStationDto> stationList;// 盘点作业站台集合

	public PanDianXiangKuDto() {
		this.cengList = new ArrayList<PanDianCengDto>();
	}

	public List<PanDianCengDto> getCengList() {
		return cengList;
	}

	public void setCengList(List<PanDianCengDto> cengList) {
		this.cengList = cengList;
	}

	public List<PanDianStationDto> getStationList() {
		return stationList;
	}

	public void setStationList(List<PanDianStationDto> stationList) {
		this.stationList = stationList;
	}

	// 查找盘点料箱数最少的站台
	private PanDianStationDto FindBestStation() {
		if (this.stationList == null || this.stationList.size() == 0)
			return null;

		List<PanDianStationDto> stList = new ArrayList<PanDianStationDto>();
		for (PanDianStationDto st : this.stationList) {
			// 判断该站台的出库盘点料箱是否达到最大盘点料箱数
			if (st.getPanDianLxNoSet().size() < st.getMaxLxCount())
				stList.add(st);
		}

		if (stList == null || stList.size() == 0)
			return null;

		PanDianStationDto bestStation = stList.get(0);

		for (int i = 1; i < stList.size(); i++) {
			PanDianStationDto station = stList.get(i);

			if (station.getPanDianLxNoSet().size() < bestStation.getPanDianLxNoSet().size()) {
				bestStation = station;
			}
		}
		return bestStation;
	}

	private PanDianCengDto FindBestCeng() {
		if (this.cengList == null || this.cengList.size() == 0)
			return null;

		PanDianCengDto bestCeng = this.cengList.get(0);

		for (int i = 1; i < this.cengList.size(); i++) {
			PanDianCengDto ceng = this.cengList.get(i);
			if (ceng.getCkLxCount() < bestCeng.getCkLxCount()) {
				bestCeng = ceng;
			} else if (ceng.getCkLxCount() == bestCeng.getCkLxCount()) {
				if (ceng.getRkLxCount() < bestCeng.getRkLxCount()) {
					bestCeng = ceng;
				}
			}
		}
		return bestCeng;
	}

	private PanDianLxDto FindBestLx() throws Exception {
		PanDianCengDto bestCeng = this.FindBestCeng();
		if (bestCeng == null)
			return null;

		if (bestCeng.getLxList().size() == 0)
			throw new Exception(bestCeng.getCeng() + "层没有料箱");

		PanDianLxDto bestLx = bestCeng.getLxList().get(0);

		for (int i = 1; i < bestCeng.getLxList().size(); i++) {
			PanDianLxDto lx = bestCeng.getLxList().get(i);
			if (lx.getPriority() < bestLx.getPriority()) {
				bestLx = lx;
			}
		}

		//移除所有同货位组的料箱，因为一个货位组只能发起一个出库任务
		for (int i = bestCeng.getLxList().size() - 1; i > -1; i--) {
			PanDianLxDto lx = bestCeng.getLxList().get(i);

			if (lx.getHuoWeiGroupId() == bestLx.getHuoWeiGroupId())
				bestCeng.getLxList().remove(i);
		}

		if (bestCeng.getLxList().size() == 0)
			this.cengList.remove(bestCeng);

		return bestLx;
	}

	// 计算料箱的出库优先级
	public void ComputeLxPriority() {
		for (PanDianCengDto ceng : this.getCengList()) {
			for (PanDianLxDto lx : ceng.getLxList()) {
				int priority = lx.getMoveNum() * 100 + lx.getDepth();
				lx.setPriority(priority);
			}
		}
	}

	// 为任务最少的站台找一个任务数最少的层的最优的料箱
	public PanDianLxResultDto FindPanDianLxResult() throws Exception {
		PanDianStationDto station = this.FindBestStation();
		if (station == null)
			return null;
		PanDianLxDto lx = this.FindBestLx();
		if (lx == null)
			return null;

		PanDianLxResultDto result = new PanDianLxResultDto();
		result.setStation(station);
		result.setLx(lx);

		// 把当前查找的料箱添加到站台的盘点出库料箱
		station.getPanDianLxNoSet().add(lx.getLxNo());

		return result;
	}
}
