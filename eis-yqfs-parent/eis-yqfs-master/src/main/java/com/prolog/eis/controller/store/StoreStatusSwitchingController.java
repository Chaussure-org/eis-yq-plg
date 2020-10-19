package com.prolog.eis.controller.store;

import com.prolog.eis.dao.masterbase.GoodsMapper;
import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.service.masterbase.BasePagerService;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
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
@Api(tags = "库存状态转换")
@RequestMapping("/api/v1/master/storeStatusSwitchin")
public class StoreStatusSwitchingController {

    @Autowired
    private ContainerSubService containerSubService;
    @Autowired
    private BasePagerService basePagerService;

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private StoreMapper storeMapper;

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
        List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum, endRowNum);
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
        List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum, endRowNum);
        DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
        collectionDto.setRows(rows);
        collectionDto.setTotalCount(totalCount);
        return RestMessage.newInstance(true, "查询成功", collectionDto);
    }

    private String getColumns() {
        return "commodityId,goodsName,goodsNo,goodsState,commodityNum,containerNo,containerSubNo,"
                + "expiryDate,gmtCreateTime";
    }

    private String getTableName() {
        return "disablecontainersubview";
    }

    @PostMapping("/SetDisableGoods")
    public RestMessage StoreStatusSwitch(@RequestBody String json)throws Exception {
    	PrologApiJsonHelper helper=PrologApiJsonHelper.createHelper(json);
		String goodsNo=helper.getString("goodsNo");
		String goodsName=helper.getString("goodsName");
		String startTime=helper.getString("startTime");
		String endTime=helper.getString("endTime");
        List<Integer> disABleGoodsIds = storeMapper.findDisABleGoodsIds(goodsNo,goodsName,startTime,endTime);
        if (disABleGoodsIds.size()>0){
			Criteria ctr=Criteria.forClass(Goods.class);
			ctr.setRestriction(Restrictions.in("id",disABleGoodsIds.toArray()));
			goodsMapper.updateMapByCriteria(MapUtils.put("goodsState",2).getMap(),ctr);
		}

       return RestMessage.newInstance(true,"更改为不合格成功",null);
    }

    @ApiOperation(value = "修改库存状态为合格", notes = "修改库存状态")
    @PostMapping("/update")
    public RestMessage<SxStore> updateStoreStatus(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerSubNo = helper.getString("containerSubNo");
        containerSubService.setDisable(false, containerSubNo);
        return RestMessage.newInstance(true, "修改成功", null);
    }

    @ApiOperation(value = "库存状态修改为不合格", notes = "库存状态修改为不合格")
    @PostMapping("/updateStatus")
    public RestMessage<SxStore> updateStatus(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerSubNo = helper.getString("containerSubNo");
        containerSubService.setDisable(true, containerSubNo);
        return RestMessage.newInstance(true, "修改成功", null);

    }

}
