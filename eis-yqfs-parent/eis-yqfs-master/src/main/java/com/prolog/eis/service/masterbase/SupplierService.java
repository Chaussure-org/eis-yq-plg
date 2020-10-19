package com.prolog.eis.service.masterbase;

import java.util.List;

import com.prolog.eis.model.masterbase.Supplier;


public interface SupplierService {
	
	/**
	 * 查询全部供应商信息
	 * @return
	 * @throws Exception
	 */
	public List<Supplier> findAllSupplier() throws Exception;
	
	/**
	 * 根据供应商ID查询信息
	 * @return
	 * @throws Exception
	 */
	public List<Supplier> findAllSupplierId(int id)throws Exception;
	
	/**
	 * 新增供应商信息
	 * @throws Exception
	 */
	public void saveSupplier(Supplier supplier)throws Exception;
	
	/**
	 * 删除供应商信息
	 * @throws Exception
	 */
	public void deleteSupplier(int id)throws Exception;
	
	/**
	 * 更新供应商信息
	 * @throws Exception
	 */
	public void updateSupplier( Supplier supplier)throws Exception;
	

}
