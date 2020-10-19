package com.prolog.eis.dto.store;

public class AllInStoreLocationLayersDto {

	private int layer;
	private int propertyCount;
	private int containerCount;
	private int ckTaskCount;
	private int rkTaskCount;
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public int getPropertyCount() {
		return propertyCount;
	}
	public void setPropertyCount(int propertyCount) {
		this.propertyCount = propertyCount;
	}
	public int getCkTaskCount() {
		return ckTaskCount;
	}
	public void setCkTaskCount(int ckTaskCount) {
		this.ckTaskCount = ckTaskCount;
	}
	public int getRkTaskCount() {
		return rkTaskCount;
	}
	public void setRkTaskCount(int rkTaskCount) {
		this.rkTaskCount = rkTaskCount;
	}
	public int getContainerCount() {
		return containerCount;
	}
	public void setContainerCount(int containerCount) {
		this.containerCount = containerCount;
	}
	
	
}
