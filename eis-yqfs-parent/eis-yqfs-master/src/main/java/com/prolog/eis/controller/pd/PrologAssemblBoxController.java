package com.prolog.eis.controller.pd;

import java.util.List;
import java.util.Map;

import com.prolog.eis.dto.assembl.AssemblBoxMxDto;
import com.prolog.eis.model.assembl.AssemblBoxHz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.dto.pd.AssemblBoxDto;
import com.prolog.eis.service.assembl.AssemblBoxService;
import com.prolog.eis.service.masterbase.BasePagerService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "合箱计划")
@RequestMapping("/api/v1/master/assemblBox")
public class PrologAssemblBoxController {

	@Autowired
	private BasePagerService basePagerService;
	@Autowired
	private AssemblBoxService assemblBoxService;
	
	@ApiOperation(value = "添加合箱数据", notes = "添加合箱数据")
	@PostMapping("/saveAssemblBox")
	public RestMessage<AssemblBoxDto> saveAssemblBox(@RequestBody String json)throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		List<AssemblBoxDto> dto = helper.getObjectList("selectlxAssemblTasks",AssemblBoxDto.class);
		assemblBoxService.saveAssemblBox(dto);
		return RestMessage.newInstance(true, "添加成功", null);
	}

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

	@ApiOperation(value = "下发合箱计划", notes = "下发合箱计划")
	@PostMapping("/updateAssemblBoxTaskState")
	public RestMessage<AssemblBoxHz> updateAssemblBoxTaskState(@RequestBody String json)throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");
		assemblBoxService.updateAssemblBoxTaskState(id);
		return RestMessage.newInstance(true, "下发成功", null);
	}

	@ApiOperation(value = "删除合箱计划", notes = "删除合箱计划")
	@PostMapping("/deleteAssemblBoxTask")
	public RestMessage<AssemblBoxHz> deleteAssemblBoxTask(@RequestBody String json)throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");
		assemblBoxService.deleteAssemblBoxTask(id);
		return RestMessage.newInstance(true, "删除成功", null);
	}

	@ApiOperation(value = "查看明细", notes = "查看明细")
	@PostMapping("/findMxByHzId")
	public RestMessage<List<AssemblBoxMxDto>> findAssemblBoxMxHz(@RequestBody String json)throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int id = helper.getInt("id");
		List<AssemblBoxMxDto> assembBoxMxInfo = assemblBoxService.findAssembBoxMxInfo(id);
		return RestMessage.newInstance(true, "查询成功", assembBoxMxInfo);
	}


	private String getColumns() {
		return "id,goodsName,goodsNo,unStartCount,startCount,completeCount,createTime";
	}

	private String getTableName() {
		return "assemblboxview";
	}

}
