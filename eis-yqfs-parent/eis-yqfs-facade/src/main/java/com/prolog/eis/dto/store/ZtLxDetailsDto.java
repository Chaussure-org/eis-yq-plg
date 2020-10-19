package com.prolog.eis.dto.store;

public class ZtLxDetailsDto {

	/**
	 * 任务类型
	 */
	private int taskType;

	/**
	 * 商品数量
	 */
	private int goodsNum;
	
	/**
	 * 商品Id
	 */
	private int goodsId;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 子容器编号
	 */
	private String containerSubNo;
	
	/**
	 * 容器编号
	 */
	private String containerNo;
	
	/**
	 * 容器类型
	 */
	private int layoutType;

	public ZtLxDetailsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ZtLxDetailsDto(int taskType, int goodsNum, int goodsId, String goodsName, String containerSubNo,
			String containerNo, int layoutType) {
		super();
		this.taskType = taskType;
		this.goodsNum = goodsNum;
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.containerSubNo = containerSubNo;
		this.containerNo = containerNo;
		this.layoutType = layoutType;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getContainerSubNo() {
		return containerSubNo;
	}

	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public int getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(int layoutType) {
		this.layoutType = layoutType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((containerNo == null) ? 0 : containerNo.hashCode());
		result = prime * result + ((containerSubNo == null) ? 0 : containerSubNo.hashCode());
		result = prime * result + goodsId;
		result = prime * result + ((goodsName == null) ? 0 : goodsName.hashCode());
		result = prime * result + goodsNum;
		result = prime * result + layoutType;
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
		ZtLxDetailsDto other = (ZtLxDetailsDto) obj;
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
		if (goodsId != other.goodsId)
			return false;
		if (goodsName == null) {
			if (other.goodsName != null)
				return false;
		} else if (!goodsName.equals(other.goodsName))
			return false;
		if (goodsNum != other.goodsNum)
			return false;
		if (layoutType != other.layoutType)
			return false;
		if (taskType != other.taskType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZtLxDetailsDto [taskType=" + taskType + ", goodsNum=" + goodsNum + ", goodsId=" + goodsId
				+ ", goodsName=" + goodsName + ", containerSubNo=" + containerSubNo + ", containerNo=" + containerNo
				+ ", layoutType=" + layoutType + "]";
	}

}
