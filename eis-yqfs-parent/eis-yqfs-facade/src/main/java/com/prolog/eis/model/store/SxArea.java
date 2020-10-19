package com.prolog.eis.model.store;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

@Table("sx_area")
public class SxArea {

	@Id
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;		//id
	
	@Column("area_type")
	private int areaType;	//区域
	
	@Column("layer")
	private int layer;		//层

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAreaType() {
		return areaType;
	}

	public void setAreaType(int areaType) {
		this.areaType = areaType;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	
}
