package com.prolog.eis.controller.order;


import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.dto.outbound.OutBoundTaskHzDto;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.order.OrderHz;
import com.prolog.eis.service.masterbase.BasePagerService;
import com.prolog.eis.service.order.IOrderHzService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Api("出库订单")
@RequestMapping("/api/v1/master/outBound")
public class OrderHzController {

    @Autowired
    private IOrderHzService orderHzService;

    @Autowired
    private BasePagerService basePagerService;


    @ApiOperation(value = "出库订单明细汇总增加", notes = "出库订单明细汇总增加")
    @PostMapping("/addHzMx")
    public RestMessage<List<Goods>> saveGoods(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        OutBoundTaskHzDto outboundTaskHz = helper.getObject("outboundTaskHz", OutBoundTaskHzDto.class);

        orderHzService.saveOrderHzMx(outboundTaskHz);

        return RestMessage.newInstance(true, "添加成功", null);
    }

    @PostMapping("orderHz/updatePriority")
    public RestMessage updatePriority(@RequestBody OrderHz orderHz) throws Exception {
        if (orderHz.getId()==0||orderHz.getPriority()==0){
            throw new Exception("传入参数有错误");
        }
        orderHzService.updatePriority(orderHz.getId(),orderHz.getPriority());
        return new  RestMessage(true,"更新成功",null);
    }

    @ApiOperation(value = "订单查询加载分页", notes = "订单查询加载分页")
    @PostMapping("orderHz/loadPager")
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
    @PostMapping("orderHz/firstLoadPager")
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

    @ApiOperation(value = "合单",notes = "合单")
    @PostMapping("createOrder")
    public RestMessage<String> createOrder() throws Exception{
        return orderHzService.createOrder();
    }

    @ApiOperation(value = "清空表数据",notes ="清空BillHzMx")
    @PostMapping("deleteBillMxHz")
    public RestMessage<String> deleteBillMxHz() throws Exception{
        return orderHzService.deleteBillMxHz();
    }

    private String getColumns() {
        return "id,pickingOrderId,expectTime,priority,pickDateTime,orderBoxNo,stationId,isAddPool,createTime,lastDateTime";
    }

    private String getTableName() {
        return "orderHzview";
    }
}
