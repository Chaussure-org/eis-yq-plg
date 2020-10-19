package com.prolog.eis.controller.masterbase;

import java.util.List;
import java.util.Map;

import com.prolog.eis.model.masterbase.Container;
import com.prolog.eis.service.masterbase.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.service.masterbase.BasePagerService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("料箱信息")
@RequestMapping("/api/v1/master/containerInfo")
public class ContainerInfoController {

	@Autowired
	private BasePagerService basePagerService;
	@Autowired
	private ContainerService containerInfoService;

	@ApiOperation(value = "加载分页", notes = "加载分页")
	@PostMapping("/loadPager")
	public RestMessage<DataEntityCollectionDto> loadPager(@RequestBody String json) throws Exception {
		String columns = "containerNo,layoutType,stationId,inBoundTime";
		String tableName = "containerinfoview";
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Map<String, Object> reStr = helper.getPagerDto(columns, tableName, "condition", BasePagerDto.class); // 获得拼装后的JSON对象
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
		String columns = "containerNo,layoutType";
		String tableName = "containerinfoview";
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Map<String, Object> reStr = helper.getPagerDto(columns, tableName, "condition", BasePagerDto.class); // 获得拼装后的JSON对象
		int startRowNum = (int) reStr.get("startRowNum");
		int endRowNum = (int) reStr.get("endRowNum");
		String condition = reStr.get("conditions").toString();
		String orders = reStr.get("orders").toString(); // 第一次加载需要获得总数量
		int totalCount = basePagerService.getTotalCount(tableName, condition);
		List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum,
				endRowNum);
		DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
		collectionDto.setRows(rows);
		collectionDto.setTotalCount(totalCount);
		return RestMessage.newInstance(true, "查询成功", collectionDto);
	}
	@ApiOperation(value = "生成容器编号及子容器编号" , notes = "生成容器编号及子容器编号")
	@PostMapping("/create")
	public RestMessage<List<Container>> queryValidContainerSubInfo(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int startNo = helper.getInt("startNo");
		int endNo = helper.getInt("endNo");
		int layoutType = helper.getInt("layoutType");
		containerInfoService.createContainerNo(startNo,endNo,layoutType);
		return RestMessage.newInstance(true, "新增成功",null);
	}

}
