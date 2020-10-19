package com.prolog.eis.pickstation.dto;

import java.util.List;

public class SendLxDetailsDto {

	private int id;
	
	/**
	 * 容器编号
	 */
	private String containerNo;
	
	/**
	 * 子容器编号
	 */
	private String containerSubNo;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 商品数量
	 */
	private int goodsNum;
	
	/**
	 * 商品条码
	 */
	private List<String> goodsBarCode;
	/**
	 * 是否是合箱货格
	 */
	private Boolean isHx = false;

	public SendLxDetailsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SendLxDetailsDto(int id, String containerNo, String containerSubNo, String goodsName, int goodsNum, List<String> goodsBarCode, Boolean isHx) {
		this.id = id;
		this.containerNo = containerNo;
		this.containerSubNo = containerSubNo;
		this.goodsName = goodsName;
		this.goodsNum = goodsNum;
		this.goodsBarCode = goodsBarCode;
		this.isHx = isHx;
	}

	public Boolean getHx() {
		return isHx;
	}

	public void setHx(Boolean hx) {
		isHx = hx;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getContainerSubNo() {
		return containerSubNo;
	}

	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public List<String> getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(List<String> goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((containerNo == null) ? 0 : containerNo.hashCode());
		result = prime * result + ((containerSubNo == null) ? 0 : containerSubNo.hashCode());
		result = prime * result + ((goodsBarCode == null) ? 0 : goodsBarCode.hashCode());
		result = prime * result + ((goodsName == null) ? 0 : goodsName.hashCode());
		result = prime * result + goodsNum;
		result = prime * result + id;
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
		SendLxDetailsDto other = (SendLxDetailsDto) obj;
		if (containerNo == null) {
			if (other.containerNo != null)
				return false;
		} else if (!containerNo.equals(other.containerNo))
			return false;
		if (containerSubNo == null) {
			if (other.containerSubNo != null)
				return false;
		} else if (!containerSubNo.equals(other.containerSubNo))
			return false;
		if (goodsBarCode == null) {
			if (other.goodsBarCode != null)
				return false;
		} else if (!goodsBarCode.equals(other.goodsBarCode))
			return false;
		if (goodsName == null) {
			if (other.goodsName != null)
				return false;
		} else if (!goodsName.equals(other.goodsName))
			return false;
		if (goodsNum != other.goodsNum)
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SendLxDetailsDto{" +
				"id=" + id +
				", containerNo='" + containerNo + '\'' +
				", containerSubNo='" + containerSubNo + '\'' +
				", goodsName='" + goodsName + '\'' +
				", goodsNum=" + goodsNum +
				", goodsBarCode=" + goodsBarCode +
				", isHx=" + isHx +
				'}';
	}
}
