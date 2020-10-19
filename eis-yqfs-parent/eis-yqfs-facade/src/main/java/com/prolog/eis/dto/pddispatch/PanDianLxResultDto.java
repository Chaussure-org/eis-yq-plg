package com.prolog.eis.dto.pddispatch;

public class PanDianLxResultDto {
	private PanDianLxDto lx;
	private PanDianStationDto station;
	
	public PanDianLxDto getLx() {
		return lx;
	}
	public void setLx(PanDianLxDto lx) {
		this.lx = lx;
	}
	public PanDianStationDto getStation() {
		return station;
	}
	public void setStation(PanDianStationDto station) {
		this.station = station;
	}
}
