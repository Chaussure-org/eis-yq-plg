package com.prolog.eis.controller.pd;

import java.util.List;
import java.util.Map;

import com.prolog.eis.dao.pd.PdTaskDetailMapper;
import com.prolog.eis.model.pd.PdTaskDetail;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.dto.pd.PdTaskDetailDto;
import com.prolog.eis.service.masterbase.BasePagerService;
import com.prolog.eis.service.pd.PdTaskService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "盘点计划")
@RequestMapping("/api/v1/master/pdTask")
public class PrologPdTaskController {

    @Autowired
    private BasePagerService basePagerService;
    @Autowired
    private PdTaskService pdTaskService;

    @Autowired
    private PdTaskDetailMapper pdTaskDetailMapper;

    @ApiOperation(value = "添加盘点数据", notes = "添加盘点数据")
    @PostMapping("/savePd")
    public RestMessage<PdTaskDetailDto> savePd(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        List<PdTaskDetailDto> dto = helper.getObjectList("selectlxPdTasks", PdTaskDetailDto.class);
        String remark = helper.getString("remark");
        pdTaskService.savepdDetail(remark, dto);
        return RestMessage.newInstance(true, "添加成功", null);
    }

    @PostMapping("/cancelTask")
    @ApiOperation(value = "取消创建的任务", notes = "取消创建的任务")
    public RestMessage cancelTask(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int pdTaskId = helper.getInt("pdTaskId");
        pdTaskService.cancelTask(pdTaskId);
        return RestMessage.newInstance(true, "取消成功", null);
    }

    @ApiOperation(value = "下发任务", notes = "下发任务")
    @PostMapping("/publish/task")
    public RestMessage<String> publishTask(@RequestBody String json) throws Exception {

        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        List<Integer> pdTaskIds = helper.getIntList("pdTaskIds");
        pdTaskService.publishTask(pdTaskIds);
        return RestMessage.newInstance(true, "下发成功", null);
    }

    @ApiOperation(value = "获取可以创建盘点任务的商品")
    @GetMapping("/getPdTaskDetail")
    public RestMessage<List<PdTaskDetail>> getPdTaskDetail() {
        List<PdTaskDetail> pdTaskDetail = pdTaskService.getPdTaskDetail();
        return RestMessage.newInstance(true, "获取成功", pdTaskDetail);
    }

    @ApiOperation(value = "查询一个计划的明细总料箱")
    @PostMapping("/findTotalCount")
    public RestMessage<List<PdTaskDetail>> findTotalCount(@RequestBody String json) {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int id = helper.getInt("id");
        List<PdTaskDetail> details = pdTaskDetailMapper.findByMap(MapUtils.put("pdTaskId", id).getMap(), PdTaskDetail.class);
        return RestMessage.newInstance(true, "查询成功", details);
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

    private String getColumns() {
        return "id,PdNo,remark,pdState,pdType,totalBoxCount,createCount,xiafaCount,finishCount,createTime";
    }

    private String getTableName() {
        return "pdtaskview";
    }


}
