package com.prolog.eis.service.masterbase;

import java.util.List;

import com.prolog.eis.model.masterbase.Owner;


public interface OwnerService {
	
	//查询全部业主信息
	public List<Owner> findAllOwner()throws Exception;
	
	//保存业主信息
	public void saveOwner(Owner owner)throws Exception;
	
	//更新业主信息
	public void updateOwner(Owner owner)throws Exception;

}
