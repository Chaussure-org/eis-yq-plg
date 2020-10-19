package com.prolog.eis.controller.store;


import java.util.List;
import java.util.Map;

import com.prolog.eis.service.store.IStoreTaskService;
import com.prolog.eis.dto.store.StoreLocationGroupDto;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.model.store.SxStoreLocation;
import com.prolog.eis.service.masterbase.BasePagerService;
import com.prolog.eis.service.store.IStoreLocationService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("货位信息")
@RequestMapping("/api/v1/master/sxStoreLocation")
public class SxStoreLocationController {

	@Autowired
	private IStoreLocationService sxStoreLocationService;

	@Autowired
	private BasePagerService basePagerService;
	@Autowired
	private IStoreTaskService sxStoreTaskFinishService;

	@ApiOperation(value = "导入货位", notes = "导入货位")
	@PostMapping ("/importLocation")
	public RestMessage<Object> importLocation(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		List<StoreLocationGroupDto> storeLocationGroupDtos = helper.getObjectList("storeLocationGroups",StoreLocationGroupDto.class);
		if(CollectionUtils.isEmpty(storeLocationGroupDtos)){
			return RestMessage.newInstance(false, "空数据", null);
		}
		//500个保存
		List<List<StoreLocationGroupDto>> partition = Lists.partition(storeLocationGroupDtos, 500);
		for (List<StoreLocationGroupDto> locationGroupDtos : partition) {
			sxStoreLocationService.importStoreLocation(locationGroupDtos);
		}
		return RestMessage.newInstance(true, "导入成功", null);
	}

	@ApiOperation(value = "通过货位组Id查询货位", notes = "通过货位组Id查询货位")
	@PostMapping ("/getByGroupId")
	public RestMessage<Object> findByGroupId(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int groupId = helper.getInt("groupId");
		List<SxStoreLocation> sxStoreLocations = sxStoreLocationService.getByGroupId(groupId);
		return RestMessage.newInstance(true, "导入成功", sxStoreLocations);
	}




	
	@ApiOperation(value = "加载分页", notes = "加载分页")
	@PostMapping("/loadPager")
	public RestMessage<DataEntityCollectionDto> loadPager(@RequestBody String json) throws Exception{
		String columns = this.getColumns();
		String tableName = this.getTableName();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Map<String, Object> reStr = helper.getPagerDto(columns, tableName, "condition", BasePagerDto.class);
		// 获得拼装后的JSON对象
		int startRowNum = (int)reStr.get("startRowNum");
		int endRowNum = (int)reStr.get("endRowNum");
		String condition = reStr.get("conditions").toString();
		String orders = reStr.get("orders").toString();
		List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum, endRowNum);
		DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
		collectionDto.setRows(rows);
		return RestMessage.newInstance(true, "查询成功", collectionDto);
	}

	@ApiOperation(value = "第一次加载分页", notes = "第一次加载分页")
	@PostMapping("/firstLoadPager")
	public RestMessage<DataEntityCollectionDto> firstLoadPager(@RequestBody String json) throws Exception{
		String columns = this.getColumns();
		String tableName = this.getTableName();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Map<String, Object> reStr = helper.getPagerDto(columns, tableName, "condition", BasePagerDto.class);
		// 获得拼装后的JSON对象
		int startRowNum = (int)reStr.get("startRowNum");
		int endRowNum = (int)reStr.get("endRowNum");
		String condition = reStr.get("conditions").toString();
		String orders = reStr.get("orders").toString();
		// 第一次加载需要获得总数量
		int totalCount = basePagerService.getTotalCount(tableName, condition);
		List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum, endRowNum);
		DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
		collectionDto.setRows(rows);
		collectionDto.setTotalCount(totalCount);
		return RestMessage.newInstance(true, "查询成功", collectionDto);
	}

	private String getColumns()
	{
		return "Id,StoreNo,StoreLocationGroupId,Layer,X,Y,StoreLocationId1,StoreLocationId2,"
				+ "AscentLockState,LocationIndex,Depth,DeptNum,CreateTime,"
				+ "VerticalLocationGroupId,ActualWeight,LimitWeight,IsInboundLocation,WmsStoreNo";
	}

	private String getTableName()
	{
		return "sxStoreLocationview";
	}

}
