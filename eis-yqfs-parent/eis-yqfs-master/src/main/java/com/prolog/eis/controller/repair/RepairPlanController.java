package com.prolog.eis.controller.repair;

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
import com.prolog.eis.dto.repair.RepairPlanDto;
import com.prolog.eis.dto.repair.RepairStoreDto;
import com.prolog.eis.service.masterbase.BasePagerService;
import com.prolog.eis.service.repair.RepairPlanService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author
 * @description: 补货计划
 * @date 2020/4/22 16:57
 */
@RestController
@Api(tags = "补货计划")
@RequestMapping("/api/v1/master/repairPlan")
public class RepairPlanController {
	
	@Autowired
	private RepairPlanService repairPlanService;

	@Autowired
	private BasePagerService basePagerService;

	@ApiOperation(value = "加载分页", notes = "加载分页")
	@PostMapping("/loadPager")
	public RestMessage<DataEntityCollectionDto> loadPager(@RequestBody String json) throws Exception {
		String columns = this.getColumns();
		String tableName = this.getTableName();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Map<String, Object> reStr = helper.getPagerDto(columns, tableName, "condition", BasePagerDto.class);
		// 获得拼装后的JSON对象
		int startRowNum = (int) reStr.get("startRowNum");
		int endRowNum = (int) reStr.get("endRowNum");
		String condition = reStr.get("conditions").toString();
		String orders = reStr.get("orders").toString();
		List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum,
				endRowNum);
		DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
		collectionDto.setRows(rows);
		return RestMessage.newInstance(true, "查询成功", collectionDto);
	}

	@ApiOperation(value = "第一次加载分页", notes = "第一次加载分页")
	@PostMapping("/firstLoadPager")
	public RestMessage<DataEntityCollectionDto> firstLoadPager(@RequestBody String json) throws Exception {
		String columns = this.getColumns();
		String tableName = this.getTableName();
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Map<String, Object> reStr = helper.getPagerDto(columns, tableName, "condition", BasePagerDto.class);
		// 获得拼装后的JSON对象
		int startRowNum = (int) reStr.get("startRowNum");
		int endRowNum = (int) reStr.get("endRowNum");
		String condition = reStr.get("conditions").toString();
		String orders = reStr.get("orders").toString();
		// 第一次加载需要获得总数量
		int totalCount = basePagerService.getTotalCount(tableName, condition);
		List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum,
				endRowNum);
		DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
		collectionDto.setRows(rows);
		collectionDto.setTotalCount(totalCount);
		return RestMessage.newInstance(true, "查询成功", collectionDto);
	}

	private String getColumns() {
		return "repairPlanId,goodsId,goodsNo,goodsName,createTime,unstartCount,publishedCount,finishedCount,operator";
	}

	private String getTableName() {
		return "repairplanview";
	}
	
	@ApiOperation(value = "创建补货计划", notes = "创建补货计划")
	@PostMapping("/createRepairPlan")
	public RestMessage<RepairPlanDto> createRepairPlan(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String goodsNo = helper.getString("goodsNo");
		String operator = helper.getString("operator");
		RepairPlanDto dto = repairPlanService.createRepairPlan(goodsNo, operator);
		
		return RestMessage.newInstance(true, "创建成功", dto);
	}
	
	@ApiOperation(value = "根据商品编号获取所有库存", notes = "根据商品编号获取所有库存")
	@PostMapping("/getAllStoreByGoods")
	public RestMessage<List<RepairStoreDto>> getAllStoreByGoodsNo(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String goodsNo = helper.getString("goodsNo");
		
		List<RepairStoreDto> list = repairPlanService.getAllStoreByGoodsNo(goodsNo);
		
		return RestMessage.newInstance(true, "获取成功", list);
	}
	
	@ApiOperation(value = "创建补货计划明细", notes = "创建补货计划明细")
	@PostMapping("/createRepairPlanMx")
	public RestMessage<Object> createRepairPlanMx(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int repairPlanId = helper.getInt("repairPlanId");
		List<RepairStoreDto> list = helper.getObjectList("repairPlanMxList", RepairStoreDto.class);
		
		repairPlanService.createRepairPlanMx(repairPlanId, list);
		
		return RestMessage.newInstance(true, "创建成功", null);
	}
	
	@ApiOperation(value = "取消计划", notes = "取消计划")
	@PostMapping("/repairPlanCancel")
	public RestMessage<Object> repairPlanCancel(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int repairPlanId = helper.getInt("id");
		
		return repairPlanService.repairPlanCancel(repairPlanId);
	}
	
	@ApiOperation(value = "获取补货计划明细", notes = "获取补货计划明细")
	@PostMapping("/getRepairPlanMxByPlanId")
	public RestMessage<List<RepairStoreDto>> getRepairPlanMxByPlanId(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int repairPlanId = helper.getInt("id");
		List<RepairStoreDto> list = repairPlanService.getRepairPlanMxByPlanId(repairPlanId);
		
		return RestMessage.newInstance(true, "查询成功", list);
	}
}
