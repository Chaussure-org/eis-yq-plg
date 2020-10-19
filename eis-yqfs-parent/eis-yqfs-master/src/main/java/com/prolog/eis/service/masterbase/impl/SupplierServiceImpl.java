package com.prolog.eis.service.masterbase.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.prolog.eis.dao.masterbase.SupplierMapper;
import com.prolog.eis.model.masterbase.Supplier;
import com.prolog.eis.service.masterbase.SupplierService;
import com.prolog.eis.util.PrologDateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Order;
import com.prolog.framework.utils.MapUtils;


@Service
public class SupplierServiceImpl implements SupplierService {
	
	@Autowired
	private SupplierMapper supplierMapper;
	

	/**
	 * 查询全部信息
	 */
	@Override
	public List<Supplier> findAllSupplier() throws Exception {
		Criteria supplier = Criteria.forClass(Supplier.class);
		supplier.setOrder(Order.newInstance().asc("id"));
		List<Supplier> suppliers = supplierMapper.findByCriteria(supplier);
		return suppliers;
	}
	

	@Override
	public void saveSupplier(Supplier supplier) throws Exception {
		supplier.setCreateTime(PrologDateUtils.parseObject(new Date()));
		supplierMapper.save(supplier);
	}

	@Override
	public void deleteSupplier(int id) throws Exception {
		if (id == 0 ) {
			throw new Exception("id不能为空");
		}
		supplierMapper.deleteByMap(MapUtils.put("id", id).getMap(), Supplier.class);
		supplierMapper.deleteById(id, Supplier.class);
		
	}

	@Override
	public void updateSupplier(Supplier supplier) throws Exception {
		if (supplier.getId() == 0 ) {
			throw new Exception("供应商ID不能为空");
		}
		supplierMapper.update(supplier);
	}

	/**
	 * 根据id查询
	 */
	@Override
	public List<Supplier> findAllSupplierId(int id) throws Exception {
		if (StringUtils.isEmpty(id)) {
			throw new Exception("供应商id不能为空");
		}
		Criteria supplier = Criteria.forClass(Supplier.class);
		supplier.setOrder(Order.newInstance().asc("id"));
		List<Supplier> suppliers = supplierMapper.findByCriteria(supplier);
		return suppliers;
	}

}
