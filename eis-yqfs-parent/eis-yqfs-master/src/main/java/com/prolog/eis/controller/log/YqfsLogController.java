package com.prolog.eis.controller.log;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "一汽日志")
@RequestMapping("/api/v1/master/yqfslog")
public class YqfsLogController {

//	@Autowired
//	private MongoTemplate mongoTemplate;
//
//	@ApiOperation(value = "第一次加载分页", notes = "第一次加载分页")
//	@PostMapping("/gcsEisLog/gcsFirsLoadPager")
//	public RestMessage<DataEntityCollectionDto> gcsFirsLoadPager(@RequestBody String json) throws Exception {
//		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
//		DataEntityCollectionDto dataEntityCollectionDto = MongoDBPageUtils.firstLoadPager(mongoTemplate, helper,
//				GcsLog.class);
//		return RestMessage.newInstance(true, "查询成功", dataEntityCollectionDto);
//	}
//
//	@ApiOperation(value = "加载分页", notes = "加载分页")
//	@PostMapping("/gcsEisLog/gcsLoadPager")
//	public RestMessage<DataEntityCollectionDto> gcsLoadPager(@RequestBody String json) throws Exception {
//		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
//		DataEntityCollectionDto dataEntityCollectionDto = MongoDBPageUtils.loadPager(mongoTemplate, helper,
//				GcsLog.class);
//		return RestMessage.newInstance(true, "查询成功", dataEntityCollectionDto);
//	}
//
//	@ApiOperation(value = "第一次加载分页", notes = "第一次加载分页")
//	@PostMapping("/mcsEisLog/mcsFirsLogPager")
//	public RestMessage<DataEntityCollectionDto> mcsFirsLogPager(@RequestBody String json) throws Exception {
//		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
//		DataEntityCollectionDto dataEntityCollectionDto = MongoDBPageUtils.firstLoadPager(mongoTemplate, helper,
//				McsLog.class);
//		return RestMessage.newInstance(true, "查询成功", dataEntityCollectionDto);
//	}
//
//	@ApiOperation(value = "加载分页", notes = "加载分页")
//	@PostMapping("/mcsEisLog/mcsLoadPager")
//	public RestMessage<DataEntityCollectionDto> mcsLoadPager(@RequestBody String json) throws Exception {
//		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
//		DataEntityCollectionDto dataEntityCollectionDto = MongoDBPageUtils.loadPager(mongoTemplate, helper,
//				McsLog.class);
//		return RestMessage.newInstance(true, "查询成功", dataEntityCollectionDto);
//	}
//
//	@ApiOperation(value = "第一次加载分页", notes = "第一次加载分页")
//	@PostMapping("/transmissionEisLog/transmissionFirsLogPager")
//	public RestMessage<DataEntityCollectionDto> transmissionFirsLogPager(@RequestBody String json) throws Exception {
//		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
//		DataEntityCollectionDto dataEntityCollectionDto = MongoDBPageUtils.firstLoadPager(mongoTemplate, helper,
//				TransmissionLog.class);
//		return RestMessage.newInstance(true, "查询成功", dataEntityCollectionDto);
//	}
//
//	@ApiOperation(value = "加载分页", notes = "加载分页")
//	@PostMapping("/transmissionEisLog/transmissionLoadPager")
//	public RestMessage<DataEntityCollectionDto> transmissionLoadPager(@RequestBody String json) throws Exception {
//		PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
//		DataEntityCollectionDto dataEntityCollectionDto = MongoDBPageUtils.loadPager(mongoTemplate, helper,
//				TransmissionLog.class);
//		return RestMessage.newInstance(true, "查询成功", dataEntityCollectionDto);
//	}

}
