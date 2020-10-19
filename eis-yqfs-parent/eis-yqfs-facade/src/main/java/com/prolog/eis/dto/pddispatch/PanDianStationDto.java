package com.prolog.eis.dto.pddispatch;

import java.util.HashSet;
import java.util.Set;

public class PanDianStationDto {
	private int stationId;
	private int maxLxCount;//最大料箱数 
	private Set<String> panDianLxNoSet;//当前正在盘点的料箱编号集合（包含出库中、在途的）
	
	public PanDianStationDto() {
		this.panDianLxNoSet = new HashSet<String>();
	}
	
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public int getMaxLxCount() {
		return maxLxCount;
	}

	public void setMaxLxCount(int maxLxCount) {
		this.maxLxCount = maxLxCount;
	}

	public Set<String> getPanDianLxNoSet() {
		return panDianLxNoSet;
	}
	public void setPanDianLxNoSet(Set<String> panDianLxNoSet) {
		this.panDianLxNoSet = panDianLxNoSet;
	}
}
