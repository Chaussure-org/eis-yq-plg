package com.prolog.eis.controller.yqfs;

import com.prolog.eis.dto.yqfs.PickOrderDetail;
import com.prolog.eis.dto.yqfs.StationDto;
import com.prolog.eis.pickstation.dao.StationMapper;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.utils.MapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "站台")
@RequestMapping("/api/v1/master/station")
public class PrologStationController {
 	@Autowired
 	private IStationService stationService;
 	@Autowired
 	private StationMapper stationMapper;
 	
 	@ApiOperation(value="站台信息查询",notes="站台信息查询")
	@PostMapping("/queryStation")
 	public RestMessage<List<StationDto>> queryStation(@RequestBody String json) throws Exception {
// 		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);

 		List<StationDto> list = stationService.queryStation();
		return RestMessage.newInstance(true, "查询成功", list);
	}
 	
	@ApiOperation(value="站台信息新增",notes="站台信息新增")
	@PostMapping("/addStation")
	public RestMessage<StationDto> addStaion(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		StationDto stationDto = helper.getObject("station", StationDto.class);
		stationService.insert(stationDto);
		return RestMessage.newInstance(true, "新增成功", null);
	}
	
	@ApiOperation(value="站台信息修改",notes="站台信息修改")
	@PostMapping("/updateStation")
	public RestMessage<StationDto> updateStaion(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		StationDto stationDto = helper.getObject("station", StationDto.class);
		stationService.update(stationDto);	
		return RestMessage.newInstance(true, "修改成功", null);
	}
	
	@ApiOperation(value="站台信息删除",notes="站台信息删除")
	@PostMapping("/deleteStation")
	public RestMessage<Object> deleteStaion(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int stationId = helper.getInt("stationId");
		stationService.deleteStation(stationId);
		return RestMessage.newInstance(true, "删除成功", null);
	}
	
	
	@ApiOperation(value="通过IP查询",notes="通过箱库Id和IP查询")
	@PostMapping("/getstationwithportbyips")
	public RestMessage<StationDto> getstationwithportbyips(@RequestBody String json) throws Exception{
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		List<String> ipaddress = helper.getStringList("ips");
		int userId = helper.getInt("userId");
		String userName = helper.getString("userName");
		StationDto station1 = new StationDto();
		for (String string : ipaddress) {
			station1  = stationService.getstationwithportbyips(string);
			if(station1!=null) {
				station1.setLoginUserid(userId);
				station1.setLoginUsername(userName);
				stationMapper.updateMapById(station1.getId(),MapUtils.put("loginUserid", userId).put("loginUsername", userName).getMap() , Station.class);
			}
		}
//		List<SysParame> sysParame = sysParameMapper.findByMap(MapUtils.put("parameNo", "STATION_PORT").getMap(), SysParame.class);
//		String port = sysParame.get(0).getParameValue();
//		stationDto.setPort(port);
//		if(stationDto.getId()==0) {
//			stationDto = null;
//		}
		return RestMessage.newInstance(true, "查询成功", station1);
	}


	@PostMapping("/pickorderdetail")
	@ApiOperation(value = "拣选单查看", notes = "拣选单查看")
	public RestMessage<List<PickOrderDetail>> getPickOrderDetail(@RequestBody String json) throws Exception {
		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		String stationId = helper.getString("stationId");
		List<PickOrderDetail> pickOrderDetail = stationService.getPickOrderDetail(stationId);
		return RestMessage.newInstance(true, "查询成功", pickOrderDetail);
	}

	@PostMapping("/seedLight")
	@ApiOperation(value = "亮灯操作", notes = "亮灯操作")
 	public RestMessage<String> seedLight(@RequestBody String json) throws Exception {
 		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
		int stationId = helper.getInt("stationId");
		stationService.seedLight(stationId);
		return RestMessage.newInstance(true,null);
	}

}
