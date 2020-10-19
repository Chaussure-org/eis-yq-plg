package com.prolog.eis.controller.masterbase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.model.masterbase.Supplier;
import com.prolog.eis.service.masterbase.SupplierService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("供应商信息")
@RequestMapping("/api/v1/master/supplier")
public class SupplierController {
	
	@Autowired
	private SupplierService supplierService;
	
	@ApiOperation("查询全部供应商信息")
	@PostMapping("/query")
	public RestMessage<List<Supplier>> querySupplier() throws Exception {
		List<Supplier> suppliers = supplierService.findAllSupplier();
		return RestMessage.newInstance(true, "查询成功", suppliers);
		
	}
	
	@ApiOperation(value = "供应商信息添加",notes = "供应商信息添加")
	@PostMapping("/save")
	public RestMessage<List<Supplier>> saveSupplier(@RequestBody String json)throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Supplier supplier = helper.getObject("supplier",Supplier.class);
		supplierService.saveSupplier(supplier);
		return RestMessage.newInstance(true, "添加成功", null);
	}
	
	@ApiOperation(value = "供应商信息更新",notes = "供应商信息更新")
	@PostMapping("/update")
	public RestMessage<List<Supplier>> updateSupplier(@RequestBody String json)throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Supplier supplier = helper.getObject("supplier",Supplier.class);
		supplierService.updateSupplier(supplier);
		return RestMessage.newInstance(true, "修改成功", null);
	}
	
	@ApiOperation(value = "供应商信息删除",notes = "供应商信息删除")
	@PostMapping("/delete")
	public RestMessage<List<Supplier>> deleteSupplier(@RequestBody String json)throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int supplierId = helper.getInt("supplierId");
		supplierService.deleteSupplier(supplierId);
		return RestMessage.newInstance(true, "删除成功", null);
	}
	
}
