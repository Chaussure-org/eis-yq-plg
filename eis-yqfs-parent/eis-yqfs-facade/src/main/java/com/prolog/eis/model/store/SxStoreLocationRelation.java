package com.prolog.eis.model.store;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * 货物四周关系表
 */
@Table("sx_store_location_relation")
public class SxStoreLocationRelation {
	@Id
	@ApiModelProperty("id")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;

	@Column("location_parent_id")
	@ApiModelProperty("货位父Id")
	private int locationParentId;

	@Column("location_child_id")
	@ApiModelProperty("子货位主键Id")
	private int locationChildId;

	@Column("location_lock")
	@ApiModelProperty("锁(0：未加锁，1：加锁)")
	private int locationLock;

	public SxStoreLocationRelation(){}

	public SxStoreLocationRelation(int id, int locationParentId, int locationChildId, int locationLock) {
		this.id = id;
		this.locationParentId = locationParentId;
		this.locationChildId = locationChildId;
		this.locationLock = locationLock;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLocationParentId() {
		return locationParentId;
	}

	public void setLocationParentId(int locationParentId) {
		this.locationParentId = locationParentId;
	}

	public int getLocationChildId() {
		return locationChildId;
	}

	public void setLocationChildId(int locationChildId) {
		this.locationChildId = locationChildId;
	}

	public int getLocationLock() {
		return locationLock;
	}

	public void setLocationLock(int locationLock) {
		this.locationLock = locationLock;
	}
}
