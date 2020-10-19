package com.prolog.eis.controller.order;


import com.prolog.eis.dto.outbound.OutBoundTaskHzDto;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.order.Transshipment;
import com.prolog.eis.service.store.ITransshipmentService;
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
@Api("中转仓")
@RequestMapping("/api/v1/master/getAllStoreByGoods")
public class TransshipmentController {
    @Autowired
    private ITransshipmentService transshipmentService;

    @ApiOperation(value = "中转仓新增", notes = "中转仓新增")
    @PostMapping("/add")
    public RestMessage<Transshipment> saveTransshipment(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String transshipmentName = helper.getString("transshipmentName");
        String transshipmentNo = helper.getString("transshipmentNo");


        transshipmentService.saveTransshipment(transshipmentName,transshipmentNo);

        return RestMessage.newInstance(true, "新增成功", null);
    }

    @ApiOperation(value = "中转仓查询", notes = "中转仓查询")
    @PostMapping("/findAll")
    public RestMessage<List<Transshipment>> findTransshipment() throws Exception {


        List<Transshipment> allTransshipment = transshipmentService.findAllTransshipment();

        return RestMessage.newInstance(true, "查询成功", allTransshipment);
    }

    @ApiOperation(value = "删除中转仓", notes = "删除中转仓")
    @PostMapping("/delete")
    public RestMessage<Transshipment> deleteTransshipment(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int id = helper.getInt("transshipmentId");

        transshipmentService.deleteTransshipment(id);

        return RestMessage.newInstance(true, "删除成功", null);
    }

    @ApiOperation(value = "修改中转仓", notes = "修改中转仓")
    @PostMapping("/update")
    public RestMessage<Transshipment> updateTransshipment(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        Transshipment transshipment = helper.getObject("transshipment", Transshipment.class);

        transshipmentService.updateTransshipment(transshipment);

        return RestMessage.newInstance(true, "修改成功", null);
    }
}
