package com.prolog.eis.controller.masterbase;

import java.util.List;
import java.util.Map;

import com.prolog.eis.dao.masterbase.GoodsBarCodeMapper;
import com.prolog.eis.service.masterbase.GoodsBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.service.masterbase.BasePagerService;
import com.prolog.eis.service.masterbase.GoodsService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("商品资料信息")
@RequestMapping("/api/v1/master/goods")
public class GoodsController {
	
	@Autowired
	public GoodsService goodsService;
	
	@Autowired
	private BasePagerService basePagerService;
	@Autowired
	private GoodsBarService goodsBarService;
	
	@ApiOperation(value = "加载分页", notes = "加载分页")
	@PostMapping("/loadPager")
	public RestMessage<DataEntityCollectionDto> loadPager(@RequestBody String json) throws Exception {
		String columns = "o.owner_no,o.owner_name,c.goods_name,o.zhuj_code,o.beactive,g.bar_code,c.length,c.width,c.height,c.unit_volume,c.create_time";
		String tableName = "goodsview";
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
		String columns = "o.owner_no,o.owner_name,c.goods_name,o.zhuj_code,o.beactive,g.bar_code,c.length,c.width,c.height,c.unit_volume,c.create_time";
		String tableName = "goodsview";
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
	

	@ApiOperation(value = "商品资料信息添加", notes = "商品资料信息添加")
	@PostMapping("/save")
	public RestMessage<List<Goods>> saveGoods(@RequestBody String json)throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Goods goods = helper.getObject("goods",Goods.class);
		goodsService.saveGoods(goods);
		return RestMessage.newInstance(true, "添加成功", null);
	}
	
	@ApiOperation(value = "商品资料信息修改" , notes = "商品资料信息修改")
	@PostMapping("/update")
	public RestMessage<List<Goods>> updateGoods(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		Goods goods = helper.getObject("goods",Goods.class);
		goodsService.updateGoods(goods);
		return RestMessage.newInstance(true, "修改成功", null);
	}
	
	@ApiOperation(value = "商品资料信息查询" , notes = "商品资料信息查询")
	@PostMapping("/queryAll")
	public RestMessage<List<Goods>> queryGoodsAll() throws Exception 
	{
		List<Goods> goodsList= goodsService.queryGoods();
		return RestMessage.newInstance(true, "查询成功", goodsList);
	}
	
	
	
	@ApiOperation(value = "商品资料删除" , notes = "商品资料删除")
	@PostMapping("/deleteAllGoods")
	public RestMessage<Object> deleteAllGoods() throws Exception 
	{

	    //goodsService.deleteAllGoods();
		return RestMessage.newInstance(true, "删除成功", null);
	}



}
