package com.prolog.eis.dto.hxdispatch;

public class HxChuKuResultDto {
	private HxStationDto station;
	private HxAssemblBoxHz hz;
	private HxAssemblBoxMx mx;
	
	public HxStationDto getStation() {
		return station;
	}
	public void setStation(HxStationDto station) {
		this.station = station;
	}
	public HxAssemblBoxHz getHz() {
		return hz;
	}
	public void setHz(HxAssemblBoxHz hz) {
		this.hz = hz;
	}
	public HxAssemblBoxMx getMx() {
		return mx;
	}
	public void setMx(HxAssemblBoxMx mx) {
		this.mx = mx;
	}
}
