package com.prolog.eis.dto.enginee;

import java.util.List;

import com.prolog.eis.model.masterbase.ContainerSub;

public class SxStoreDto {
	private int id;

	private String containerNo;
	
	private List<ContainerSub> containerSubInfo;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the containerNo
	 */
	public String getContainerNo() {
		return containerNo;
	}

	/**
	 * @param containerNo the containerNo to set
	 */
	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	/**
	 * @return the containerSubInfo
	 */
	public List<ContainerSub> getContainerSubInfo() {
		return containerSubInfo;
	}

	/**
	 * @param containerSubInfo the containerSubInfo to set
	 */
	public void setContainerSubInfo(List<ContainerSub> containerSubInfo) {
		this.containerSubInfo = containerSubInfo;
	}
	
	
}
