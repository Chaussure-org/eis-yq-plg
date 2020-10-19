package com.prolog.eis.controller.yqfs;

import com.prolog.eis.dto.pickorder.SeedTaskDto;
import com.prolog.eis.dto.station.StationsDto;
import com.prolog.eis.dto.yqfs.PickBillDto;
import com.prolog.eis.pickstation.dao.StationMapper;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.pickorder.IPickOrderService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Api(tags = "StationPick管理")
@RequestMapping("/api/v1/master/stationpick")
public class PrologStationPickController {
	
	@Autowired
	private StationMapper stationMapper;
	@Autowired
	private IStationService stationService;
	@Autowired
	private IPickOrderService pickOrderService;
	@Autowired
	private IStationService iStationService;

	
	 @ApiOperation(value = "站台缺货查询",notes = "站台缺货查询")
	 @PostMapping("/lack")
	 public RestMessage<ConcurrentHashMap<String, List>> lack (@RequestBody String json) throws Exception{
		 PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		 int stationId = helper.getInt("stationId");
		 Station station = stationMapper.findById(stationId, Station.class);
		 if (station == null || station.getCurrentStationPickId() == 0) {
			//查询空站台 或为空拣选单
			 return RestMessage.newInstance(false, "该站台无拣选单任务", null);
		}
		 ConcurrentHashMap<String, List> lack = stationService.lack(station.getCurrentStationPickId());
		 return RestMessage.newInstance(true, "站台缺货查询", lack);
	 }

	 @ApiOperation(value = "站台拣选单缺货查询",notes = "站台拣选单缺货查询")
	 @PostMapping("/pickOrderInfo")
	public RestMessage<Map<String,List>> pickOrderInfo(@RequestBody String json) throws Exception{
	 	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
	 	String stationNo = helper.getString("stationNo");
		 Map<String, List> pickOrderInfo = pickOrderService.getPickOrderInfo(stationNo);
		 return RestMessage.newInstance(true,"查询成功",pickOrderInfo);
	 }

	 @ApiOperation(value = "获取所有站台",notes = "获取所有站台")
	 @PostMapping("station/allStation")
	public RestMessage<List<StationsDto>> getAllStation(@RequestBody String json) throws Exception{
	 	List<StationsDto> list = stationService.getAllStation();
	 	return RestMessage.newInstance(true,"获取站台成功",list);
	 }

	 //String wave_no, String dealer_id
	@PostMapping("pickBillDetail")
	 public RestMessage<List<PickBillDto>>	getPickBillDetail(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String waveNo = helper.getString("waveNo");
		String dealerId = helper.getString("dealerId");
		String billNo = helper.getString("billNo");
		List<PickBillDto> pickBillDto = stationService.getPickBillDto(waveNo, dealerId,billNo);
		return RestMessage.newInstance(true, "获取站台成功", pickBillDto);
	 }

	 @PostMapping("seedTask")
	@ApiOperation(value = "在做播种任务",notes = "在做播种任务")
	public RestMessage<List<SeedTaskDto>> getSeedTask(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String containerNo = helper.getString("containerNo");
		String goodsNo = helper.getString("goodsNo");
		String goodsName = helper.getString("goodsName");
		 List<SeedTaskDto> seedTask = stationService.getSeedTask(containerNo, goodsNo, goodsName);
		 return RestMessage.newInstance(true,"查询成功",seedTask);
	 }

	 @PostMapping("cutOrder")
	@ApiOperation(value = "截单",notes = "截单")
	public RestMessage<String> cutOrder(@RequestBody String json) throws Exception {
	 	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
	 	String stationNo = helper.getString("stationNo");
	 	stationService.cutOrder(stationNo);
	 return RestMessage.newInstance(true,"截单完成",null);
	 }

	 @PostMapping("orderInit")
	@ApiModelProperty(value = "初始化订单箱任务", notes = "初始化订单箱任务")
	public RestMessage<String> orderInit(@RequestBody String json) throws Exception{
	 	PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		 int positionNo = helper.getInt("positionNo");
		 int stationId = helper.getInt("stationId");
		 stationService.orderInit(positionNo,stationId);
		 return RestMessage.newInstance(true,"初始化成功");
	 }

}
