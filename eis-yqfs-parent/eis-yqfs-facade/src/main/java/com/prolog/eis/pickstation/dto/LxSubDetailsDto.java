package com.prolog.eis.pickstation.dto;

import java.util.List;

public class LxSubDetailsDto {

	/**
	 * 料箱子容器编号
	 */
	private String lxSubNo; 
	
	/**
	 * 商品名字
	 */
	private String goodsName;
	
	/**
	 * 商品数量
	 */
	private int goodsNum;

	/**
	 * 商品条码
	 */
	private List<String> goodsBarCodes;

	public LxSubDetailsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LxSubDetailsDto(String lxSubNo, String goodsName, int goodsNum, List<String> goodsBarCodes) {
		super();
		this.lxSubNo = lxSubNo;
		this.goodsName = goodsName;
		this.goodsNum = goodsNum;
		this.goodsBarCodes = goodsBarCodes;
	}

	public String getLxSubNo() {
		return lxSubNo;
	}

	public void setLxSubNo(String lxSubNo) {
		this.lxSubNo = lxSubNo;
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

	public List<String> getGoodsBarCodes() {
		return goodsBarCodes;
	}

	public void setGoodsBarCodes(List<String> goodsBarCodes) {
		this.goodsBarCodes = goodsBarCodes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((goodsBarCodes == null) ? 0 : goodsBarCodes.hashCode());
		result = prime * result + ((goodsName == null) ? 0 : goodsName.hashCode());
		result = prime * result + goodsNum;
		result = prime * result + ((lxSubNo == null) ? 0 : lxSubNo.hashCode());
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
		LxSubDetailsDto other = (LxSubDetailsDto) obj;
		if (goodsBarCodes == null) {
			if (other.goodsBarCodes != null)
				return false;
		} else if (!goodsBarCodes.equals(other.goodsBarCodes))
			return false;
		if (goodsName == null) {
			if (other.goodsName != null)
				return false;
		} else if (!goodsName.equals(other.goodsName))
			return false;
		if (goodsNum != other.goodsNum)
			return false;
		if (lxSubNo == null) {
			if (other.lxSubNo != null)
				return false;
		} else if (!lxSubNo.equals(other.lxSubNo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LxSubDetailsDto [lxSubNo=" + lxSubNo + ", goodsName=" + goodsName + ", goodsNum=" + goodsNum
				+ ", goodsBarCodes=" + goodsBarCodes + "]";
	}

}
