package com.prolog.eis.pickstation.dto;

import java.util.List;

public class LxDetailsDto {

	/**
	 * 任务类型
	 */
	private int taskType;
	
	/**
	 * 料箱编号
	 */
	private String lxNo;
	
	/**
	 * 料箱类型(1、整箱，2、日字格，3、田字格)
	 */
	private int lxType;
	
	private List<LxSubDetailsDto> lxSubDeatilsDtos;

	public LxDetailsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LxDetailsDto(int taskType, String lxNo, int lxType, List<LxSubDetailsDto> lxSubDeatilsDtos) {
		super();
		this.taskType = taskType;
		this.lxNo = lxNo;
		this.lxType = lxType;
		this.lxSubDeatilsDtos = lxSubDeatilsDtos;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getLxNo() {
		return lxNo;
	}

	public void setLxNo(String lxNo) {
		this.lxNo = lxNo;
	}

	public int getLxType() {
		return lxType;
	}

	public void setLxType(int lxType) {
		this.lxType = lxType;
	}

	public List<LxSubDetailsDto> getLxSubDeatilsDtos() {
		return lxSubDeatilsDtos;
	}

	public void setLxSubDeatilsDtos(List<LxSubDetailsDto> lxSubDeatilsDtos) {
		this.lxSubDeatilsDtos = lxSubDeatilsDtos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lxNo == null) ? 0 : lxNo.hashCode());
		result = prime * result + ((lxSubDeatilsDtos == null) ? 0 : lxSubDeatilsDtos.hashCode());
		result = prime * result + lxType;
		result = prime * result + taskType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LxDetailsDto other = (LxDetailsDto) obj;
		if (lxNo == null) {
			if (other.lxNo != null)
				return false;
		} else if (!lxNo.equals(other.lxNo))
			return false;
		if (lxSubDeatilsDtos == null) {
			if (other.lxSubDeatilsDtos != null)
				return false;
		} else if (!lxSubDeatilsDtos.equals(other.lxSubDeatilsDtos))
			return false;
		if (lxType != other.lxType)
			return false;
		if (taskType != other.taskType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LxDetailsDto [taskType=" + taskType + ", lxNo=" + lxNo + ", lxType=" + lxType + ", lxSubDeatilsDtos="
				+ lxSubDeatilsDtos + "]";
	}
}
