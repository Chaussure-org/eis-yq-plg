package com.prolog.eis.controller.masterbase;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.model.masterbase.Owner;
import com.prolog.eis.service.masterbase.BasePagerService;
import com.prolog.eis.service.masterbase.OwnerService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("业主信息")
@RequestMapping("/api/v1/master/owner")
public class OwnerController {
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private BasePagerService basePagerService;
	
	  @ApiOperation(value = "加载分页", notes = "加载分页")
	  @PostMapping("/loadPager") 
	  public RestMessage<DataEntityCollectionDto> loadPager(@RequestBody String json) throws Exception{ 
		  String columns = "id,ownerNo,ownerName,zhujCode,ownerSimplename,beactive,createTime,updateTime"; 
		  String tableName = "ownerview";
		  PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		  Map<String, Object> reStr = helper.getPagerDto(columns, tableName,"condition", BasePagerDto.class); // 获得拼装后的JSON对象 
		  int startRowNum = (int)reStr.get("startRowNum"); 
		  int endRowNum = (int)reStr.get("endRowNum");
		  String condition = reStr.get("conditions").toString(); 
		  String orders = reStr.get("orders").toString(); 
		  List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders,startRowNum, endRowNum); 
		  DataEntityCollectionDto collectionDto = new DataEntityCollectionDto(); 
		  collectionDto.setRows(rows); 
		  return RestMessage.newInstance(true, "查询成功", collectionDto);
	  
	  }
	  
	  @ApiOperation(value = "第一次加载分页", notes = "第一次加载分页")
	  @PostMapping("/firstLoadPager") 
	  public RestMessage<DataEntityCollectionDto> firstLoadPager(@RequestBody String json) throws Exception{ 
		  String columns = "id,ownerNo,ownerName,zhujCode,ownerSimplename,beactive,createTime,updateTime"; 
		  String tableName = "ownerview";
		  PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		  Map<String, Object> reStr = helper.getPagerDto(columns, tableName,"condition", BasePagerDto.class); // 获得拼装后的JSON对象 
		  int startRowNum = (int)reStr.get("startRowNum"); 
		  int endRowNum = (int)reStr.get("endRowNum");
		  String condition = reStr.get("conditions").toString(); 
		  String orders =reStr.get("orders").toString(); // 第一次加载需要获得总数量 
		  int totalCount = basePagerService.getTotalCount(tableName, condition); 
		  List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum, endRowNum);
		  DataEntityCollectionDto collectionDto = new DataEntityCollectionDto(); 
		  collectionDto.setRows(rows); 
		  collectionDto.setTotalCount(totalCount); 
		  return RestMessage.newInstance(true,"查询成功", collectionDto);
	  } 
	  
	  
	 
	
	@ApiOperation(value = "业主信息添加", notes = "业主信息添加")
	@PostMapping("/save")
	public RestMessage<List<Owner>> saveOwner(@RequestBody String json)throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Owner owner = helper.getObject("owner",Owner.class);
		ownerService.saveOwner(owner);
		return RestMessage.newInstance(true, "添加成功", null);
	}
	
	@ApiOperation(value = "业主信息修改" , notes = "业主信息修改")
	@PostMapping("/update")
	public RestMessage<List<Owner>> updateOwner(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Owner owner = helper.getObject("owner",Owner.class);
		ownerService.updateOwner(owner);
		return RestMessage.newInstance(true, "修改成功", null);
	}

	@ApiOperation(value = "查询全部业主" , notes = "查询全部业主")
	@PostMapping("/query")
	public RestMessage<List<Owner>> findAllOwner() throws Exception {

		List<Owner> allOwner = ownerService.findAllOwner();
		return RestMessage.newInstance(true, "查询成功", allOwner);
	}




}
