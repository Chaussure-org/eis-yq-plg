package com.prolog.eis.dto.tkdispatch;

import java.util.ArrayList;
import java.util.List;

public class TuiKuXiangKuDto {
	private List<TuiKuCengDto> cengList;

	public TuiKuXiangKuDto() {
		this.cengList = new ArrayList<TuiKuCengDto>();
	}

	public List<TuiKuCengDto> getCengList() {
		return cengList;
	}

	public void setCengList(List<TuiKuCengDto> cengList) {
		this.cengList = cengList;
	}

	private TuiKuCengDto FindBestCeng() {
		if (this.cengList == null || this.cengList.size() == 0)
			return null;

		TuiKuCengDto bestCeng = this.cengList.get(0);

		for (int i = 1; i < this.cengList.size(); i++) {
			TuiKuCengDto ceng = this.cengList.get(i);
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

	private TuiKuLxDto FindBestLx() throws Exception {
		TuiKuCengDto bestCeng = this.FindBestCeng();
		if (bestCeng == null)
			return null;

		if (bestCeng.getLxList().size() == 0)
			throw new Exception(bestCeng.getCeng() + "层没有料箱");

		TuiKuLxDto bestLx = bestCeng.getLxList().get(0);

		for (int i = 1; i < bestCeng.getLxList().size(); i++) {
			TuiKuLxDto lx = bestCeng.getLxList().get(i);
			if (lx.getPriority() < bestLx.getPriority()) {
				bestLx = lx;
			}
		}

		//移除所有同货位组的料箱，因为一个货位组只能发起一个出库任务
		for (int i = bestCeng.getLxList().size() - 1; i > -1; i--) {
			TuiKuLxDto lx = bestCeng.getLxList().get(i);

			if (lx.getHuoWeiGroupId() == bestLx.getHuoWeiGroupId())
				bestCeng.getLxList().remove(i);
		}
		
		if (bestCeng.getLxList().size() == 0)
			this.cengList.remove(bestCeng);

		return bestLx;
	}

	// 计算料箱的出库优先级
	public void ComputeLxPriority() {
		for (TuiKuCengDto ceng : this.getCengList()) {
			for (TuiKuLxDto lx : ceng.getLxList()) {
				int priority = lx.getMoveNum() * 100 + lx.getDepth();
				lx.setPriority(priority);
			}
		}
	}

	// 为任务最少的站台找一个任务数最少的层的最优的料箱
	public TuiKuLxDto FindTuiKuLxResult() throws Exception {
		TuiKuLxDto lx = this.FindBestLx();
		if (lx == null)
			return null;

		return lx;
	}
}
