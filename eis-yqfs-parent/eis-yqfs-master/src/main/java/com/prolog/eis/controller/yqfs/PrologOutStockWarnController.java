package com.prolog.eis.controller.yqfs;

import com.prolog.eis.dto.yqfs.GoodsOutboundWarnDto;
import com.prolog.eis.service.yqfs.impl.OutStockWarnServiceImpl;
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

@RestController
@Api(tags = "缺货预警")
@RequestMapping("/api/v1/master/outStockWarn")
public class PrologOutStockWarnController {
	
	@Autowired
	private OutStockWarnServiceImpl outStockWarnService;

	@ApiOperation(value = "查询列表",notes = "查询列表")
	@PostMapping("/list")
	public RestMessage<List<GoodsOutboundWarnDto>> list(@RequestBody  String json)throws Exception {
		PrologApiJsonHelper helper=PrologApiJsonHelper.createHelper(json);
		int warnNum = helper.getInt("warnCount");
		List<GoodsOutboundWarnDto> list = outStockWarnService.getOutStockList(warnNum);
		return RestMessage.newInstance(true, "查询列表", list);
	}

}
