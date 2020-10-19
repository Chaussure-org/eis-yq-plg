package com.prolog.eis.controller.tk;

import java.util.*;

import com.prolog.eis.dao.tk.TKTaskMapper;
import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.tk.TKTask;
import com.prolog.eis.service.masterbase.BasePagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.service.tk.TkTaskService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author 孙四月
 */
@RestController
@Api(tags = "退库计划")
@RequestMapping("/api/v1/master/backStorePlan")
public class TkTaskController {

    @Autowired
    private TKTaskMapper tkTaskMapper;

    @Autowired
    private TkTaskService backStorePlanService;

    @ApiOperation(value = "查询退库任务", notes = "查询退库任务")
    @PostMapping("/queryTkTask")
    public RestMessage<List<TKTask>> QueryTkTask(@RequestBody String containerNo) throws Exception {
        List<TKTask> tkTaskList = tkTaskMapper.findByMap(null, TKTask.class);
        return new RestMessage(true, "查询成功", tkTaskList);
    }

    @ApiOperation(value = "批量添加退库任务", notes = "批量添加退库任务")
    @PostMapping("/addTkTask")
    public RestMessage AddTkTask(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        List<String> tkList=helper.getStringList("tkList");
        backStorePlanService.AddTkTask(tkList);
        return new RestMessage(true,"添加成功",null);
    }

    @ApiOperation(value = "删除退库任务", notes = "删除退库任务")
    @PostMapping("/deleteTask")
    public RestMessage deleteTask(@RequestBody int id)throws Exception{
        backStorePlanService.deleteTkPlan(id);
        return new RestMessage(true,"添加成功",null);
    }

    @ApiOperation(value = "下发退库任务", notes = "下发退库任务")
    @PostMapping("/releaseTask")
    public RestMessage reTask(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper=PrologApiJsonHelper.createHelper(json);
        List<Integer> idList = helper.getIntList("idList");
        backStorePlanService.releaseTask(idList);
        return RestMessage.newInstance(true,"下发成功",null);
    }


    @Autowired
    private BasePagerService basePagerService;

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
        return "id,container_no,status,gmt_create_time,gmt_update_time";
    }

    private String getTableName()
    {
        return "tktaskview";
    }

    //=============

    @ApiOperation(value = "未选择退库任务加载分页", notes = "未选择退库任务加载分页")
    @PostMapping("unSelect/loadPager")
    public RestMessage<DataEntityCollectionDto> unSelectloadPager(@RequestBody String json) throws Exception{

        String columns = this.getKuCunColumns();
        String tableName = this.getKuCunTableName();
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        Map<String, Object> reStr = helper.getPagerDto(columns, tableName, "condition", BasePagerDto.class);
        // 获得拼装后的JSON对象
        int startRowNum = (int)reStr.get("startRowNum");
        int endRowNum = (int)reStr.get("endRowNum");
        String condition = reStr.get("conditions").toString();
        String orders = reStr.get("orders").toString();
        if (condition.contains("and tkType = '0'")){
            tableName = this.getKuCunEmptyTableName();
        }
        condition = condition.replaceAll(" and tkType = '0'", "")
                .replaceAll(" and tkType = '1'", "");
        List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum, endRowNum);

        DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
        collectionDto.setRows(rows);
        return RestMessage.newInstance(true, "查询成功", collectionDto);
    }

    @ApiOperation(value = "未选择退库任务第一次加载分页", notes = "未选择退库任务第一次加载分页")
    @PostMapping("unSelect/firstLoadPager")
    public RestMessage<DataEntityCollectionDto> unSelectfirstLoadPager(@RequestBody String json) throws Exception{

        String columns = this.getKuCunColumns();
        String tableName = this.getKuCunTableName();
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        Map<String, Object> reStr = helper.getPagerDto(columns, tableName, "condition", BasePagerDto.class);
        // 获得拼装后的JSON对象
        int startRowNum = (int)reStr.get("startRowNum");
        int endRowNum = (int)reStr.get("endRowNum");
        String condition = reStr.get("conditions").toString();
        String orders = reStr.get("orders").toString();

        if (condition.contains("and tkType = '0'")){
            tableName = this.getKuCunEmptyTableName();
        }
        condition = condition.replaceAll(" and tkType = '0'", "")
                .replaceAll(" and tkType = '1'", "");

        // 第一次加载需要获得总数量
        int totalCount = basePagerService.getTotalCount(tableName, condition);
        List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum, endRowNum);
        DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
        collectionDto.setRows(rows);
        collectionDto.setTotalCount(totalCount);
        return RestMessage.newInstance(true, "查询成功", collectionDto);
    }

    private String getKuCunColumns()
    {
        return "goodsName,barCode,layer,x,y,containerNo,commodityNum," +
                "containerSubNo,expiryDate,storeState,createTime";
    }

    private String getKuCunTableName()
    {
        return "unselectebackmxview";
    }
    private String getKuCunEmptyTableName(){
        return "tkemptyview";
    }
}
