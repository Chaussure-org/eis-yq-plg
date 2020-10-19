package com.prolog.eis.dto.store;

import com.prolog.eis.model.store.SxStoreLocation;
import com.prolog.eis.model.store.SxStoreLocationGroup;

import java.util.List;

public class YqfsStoreLocationGroupDto extends SxStoreLocationGroup {
	private List<SxStoreLocation> sxStoreLocationList;

	public List<SxStoreLocation> getSxStoreLocationList() {
		return sxStoreLocationList;
	}

	public void setSxStoreLocationList(List<SxStoreLocation> sxStoreLocationList) {
		this.sxStoreLocationList = sxStoreLocationList;
	}
	
}
