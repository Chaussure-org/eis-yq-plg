package com.prolog.eis.dto.enginee;

import java.util.ArrayList;
import java.util.List;

public class LiaoXiangDto {
	public LiaoXiangDto() {
		super();		
		this.huoGeList = new ArrayList<HuoGeDto>();
	}

	private int jxdId;
	private Integer xkKuCunId;
	private Integer ztKuCunId;
	private Integer stationId;
	private String liaoXiangNo;
	
	private List<HuoGeDto> huoGeList;

	public Integer getXkKuCunId() {
		return xkKuCunId;
	}

	public void setXkKuCunId(Integer xkKuCunId) {
		this.xkKuCunId = xkKuCunId;
	}

	public Integer getZtKuCunId() {
		return ztKuCunId;
	}

	public void setZtKuCunId(Integer ztKuCunId) {
		this.ztKuCunId = ztKuCunId;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}	

	public String getLiaoXiangNo() {
		return liaoXiangNo;
	}

	public void setLiaoXiangNo(String liaoXiangNo) {
		this.liaoXiangNo = liaoXiangNo;
	}

	public int getJxdId() {
		return jxdId;
	}

	public void setJxdId(int jxdId) {
		this.jxdId = jxdId;
	}

	public List<HuoGeDto> getHuoGeList() {
		return huoGeList;
	}

	public void setHuoGeList(List<HuoGeDto> huoGeList) {
		this.huoGeList = huoGeList;
	}
}
