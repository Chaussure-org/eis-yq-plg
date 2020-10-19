package com.prolog.eis.model.order;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("container_binding_hz")
public class ContainerBindingHz {

	@Id
	@Column("container_no")
	@ApiModelProperty("容器编号")
	private String containerNo;
	
	@Column("picking_order_id")
	@ApiModelProperty("拣选单ID")
	private int pickingOrderId;
	
	@Column("xk_store_id")
	@ApiModelProperty("箱库库存Id")
	private Integer xkStoreId;
	
	@Column("zt_store_id")
	@ApiModelProperty("在途库存ID")
	private Integer ztStoreId;
	
	@Column("station_id")
	@ApiModelProperty("站台ID")
	private int stationId;
	


	public ContainerBindingHz() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ContainerBindingHz(String containerNo, int pickingOrderId, Integer xkStoreId, Integer ztStoreId,
			int stationId, int isComplete) {
		super();
		this.containerNo = containerNo;
		this.pickingOrderId = pickingOrderId;
		this.xkStoreId = xkStoreId;
		this.ztStoreId = ztStoreId;
		this.stationId = stationId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public int getPickingOrderId() {
		return pickingOrderId;
	}

	public void setPickingOrderId(int pickingOrderId) {
		this.pickingOrderId = pickingOrderId;
	}

	public Integer getXkStoreId() {
		return xkStoreId;
	}

	public void setXkStoreId(Integer xkStoreId) {
		this.xkStoreId = xkStoreId;
	}

	public Integer getZtStoreId() {
		return ztStoreId;
	}

	public void setZtStoreId(Integer ztStoreId) {
		this.ztStoreId = ztStoreId;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((containerNo == null) ? 0 : containerNo.hashCode());
		result = prime * result + pickingOrderId;
		result = prime * result + stationId;
		result = prime * result + ((xkStoreId == null) ? 0 : xkStoreId.hashCode());
		result = prime * result + ((ztStoreId == null) ? 0 : ztStoreId.hashCode());
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
		ContainerBindingHz other = (ContainerBindingHz) obj;
		if (containerNo == null) {
			if (other.containerNo != null)
				return false;
		} else if (!containerNo.equals(other.containerNo))
			return false;
		if (pickingOrderId != other.pickingOrderId)
			return false;
		if (stationId != other.stationId)
			return false;
		if (xkStoreId == null) {
			if (other.xkStoreId != null)
				return false;
		} else if (!xkStoreId.equals(other.xkStoreId))
			return false;
		if (ztStoreId == null) {
			if (other.ztStoreId != null)
				return false;
		} else if (!ztStoreId.equals(other.ztStoreId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ContainerBindingHz [containerNo=" + containerNo + ", pickingOrderId=" + pickingOrderId + ", xkStoreId="
				+ xkStoreId + ", ztStoreId=" + ztStoreId + ", stationId=" + stationId
				+ "]";
	}

}
