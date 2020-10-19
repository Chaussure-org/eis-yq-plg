package com.prolog.eis.controller.store;


import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.dto.store.ExcelStoreDto;
import com.prolog.eis.dto.store.LayerTaskCountDto;
import com.prolog.eis.dto.store.LayersStateDto;
import com.prolog.eis.model.log.Log;
import com.prolog.eis.model.masterbase.Container;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.store.SxStoreLocationRelation;
import com.prolog.eis.service.log.ILogService;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.eis.service.store.IStoreService;
import com.prolog.eis.service.store.IStoreLocationService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@Api("商品入库存")
@RequestMapping("/api/v1/master/sxStore")
public class SxStoreController {

    @Autowired
    private IStoreService sxStoreService;
    @Autowired
    private ContainerSubService containerSubInfoService;
    @Autowired
    private IStoreLocationService locationService;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private ILogService logService;

    @ApiOperation(value = "商品入库添加并更新子容器", notes = "商品入库添加并更新子容器")
    @PostMapping("/save")
    public RestMessage<List<ContainerSub>> saveGoods(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        List<ContainerSub> containerSubInfos = helper.getObjectList("containerSubInfos", ContainerSub.class);

        sxStoreService.saveSxStore(containerSubInfos);

        return RestMessage.newInstance(true, "添加成功", null);
    }

    @ApiOperation(value = "层任务数", notes = "层任务数")
    @PostMapping("/layerCount")
    public RestMessage<List<LayerTaskCountDto>> getLayerCount() {
        List<LayerTaskCountDto> layerTaskCount = sxStoreService.getLayerTaskCount();
        return RestMessage.newInstance(true, "查询成功", layerTaskCount);
    }

    @ApiOperation(value = "层任务及状态", notes = "层任务及状态")
    @PostMapping("/layerStates")
    public RestMessage<LayersStateDto> getLayerStateInfo() throws Exception {
        LayersStateDto layerState = sxStoreService.getLayerState();
        return RestMessage.newInstance(true, "查询成功", layerState);
    }

    @PostMapping("/ExcelStore")
    public RestMessage<List<ExcelStoreDto>> getAllStore() {
        List<ExcelStoreDto> excelStoreList = storeMapper.getExcelStoreList();
        return RestMessage.newInstance(true, "导出成功", excelStoreList);
    }


    @ApiOperation(value = "删除入库异常库存", notes = "删除入库异常库存")
    @PostMapping("/deleteContainerNo")
    public RestMessage<LayersStateDto> deleteContainer(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        Log log = new Log(null, containerNo, "删除库存", "EIS", null, null, null, null, new Date());
        try{
            sxStoreService.deleteAbnormalStore(containerNo);
            log.setSuccess(true);
            logService.save(log);
            return RestMessage.newInstance(true, "删除成功", null);
        }catch(Exception e){
            log.setSuccess(false);
            log.setException(e.getMessage());
            logService.save(log);
            return RestMessage.newInstance(false, "删除失败,"+e.getMessage(), null);
        }

    }

}
