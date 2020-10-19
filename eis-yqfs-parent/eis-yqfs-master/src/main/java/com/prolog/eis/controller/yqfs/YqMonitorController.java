package com.prolog.eis.controller.yqfs;

import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.dto.store.ZtBoxMxDto;
import com.prolog.eis.dto.store.ZtBoxStatisDto;
import com.prolog.eis.dto.store.ZtStationStatisDto;
import com.prolog.eis.dto.yqfs.StationDto;
import com.prolog.eis.model.log.Log;
import com.prolog.eis.service.log.ILogService;
import com.prolog.eis.service.masterbase.BasePagerService;
import com.prolog.eis.service.monitor.YqMonitorService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.utils.JsonUtils;
import com.prolog.framework.utils.MapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author panteng
 * @description: 一汽监控界面查询
 * @date 2020/4/29 10:42
 */
@RestController
@Api(tags = "一汽监控界面查询")
@RequestMapping("/api/v1/master/monitor")
public class YqMonitorController {

    @Autowired
    private BasePagerService basePagerService;

    @Autowired
    private YqMonitorService yqMonitorService;

    @Autowired
    private ILogService logService;

    private static final String kucun_view ="kucun_view";
    private static final String kucun_view_cloumn ="goodsName,barCode,layer,x,y,containerNo,commodityNum,containerSubNo," +
            "expiryDate,inboundTime,depth,storeState";

    private static final String zt_kucun_view ="ztkucun_view";
    private static final String zt_kucun_view_cloumn ="containerNo,containerSubNo,goodsName,barCode,status,taskType,commodityNum," +
            "gmtCreateTime,gmtLastUpdateTime";

    @ApiOperation(value = "库存查询加载分页", notes = "库存查询加载分页")
    @PostMapping("/kucun/loadPager")
    public RestMessage<DataEntityCollectionDto> loadPager(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        Map<String, Object> reStr = helper.getPagerDto(kucun_view_cloumn, kucun_view, "condition", BasePagerDto.class);
        // 获得拼装后的JSON对象
        int startRowNum = (int)reStr.get("startRowNum");
        int endRowNum = (int)reStr.get("endRowNum");
        String condition = reStr.get("conditions").toString();
        String orders = reStr.get("orders").toString();
        List<DataEntity> rows = basePagerService.getPagers(kucun_view_cloumn, kucun_view, condition, orders, startRowNum, endRowNum);
        DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
        String goodsName = helper.getString("goodsName");
        String barCode = helper.getString("barCode");
        if (goodsName!=null || barCode != null) {
            DataEntity dataEntity = new DataEntity();
            int sum = rows.stream().mapToInt(x -> (int) x.getDatas().get("commodityNum")).sum();
            dataEntity.setDatas(MapUtils.put("goodsName","合计").put("commodityNum",sum).getMap());
            rows.add(dataEntity);
        }
        collectionDto.setRows(rows);
        return RestMessage.newInstance(true, "查询成功", collectionDto);
    }

    @ApiOperation(value = "库存查询第一次加载分页", notes = "库存查询第一次加载分页")
    @PostMapping("/kucun/firstLoadPager")
    public RestMessage<DataEntityCollectionDto> firstLoadPager(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        Map<String, Object> reStr = helper.getPagerDto(kucun_view_cloumn, kucun_view, "condition", BasePagerDto.class);
        // 获得拼装后的JSON对象
        int startRowNum = (int)reStr.get("startRowNum");
        int endRowNum = (int)reStr.get("endRowNum");
        String condition = reStr.get("conditions").toString();
        String orders = reStr.get("orders").toString();
        // 第一次加载需要获得总数量
        int totalCount = basePagerService.getTotalCount(kucun_view, condition);
        List<DataEntity> rows = basePagerService.getPagers(kucun_view_cloumn, kucun_view, condition, orders, startRowNum, endRowNum);
        DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
        String goodsName = helper.getString("goodsName");
        String barCode = helper.getString("barCode");
        if (goodsName!=null || barCode != null) {
            DataEntity dataEntity = new DataEntity();
            int sum = rows.stream().mapToInt(x -> (int) x.getDatas().get("commodityNum")).sum();
            dataEntity.setDatas(MapUtils.put("goodsName","合计").put("commodityNum",sum).getMap());
            rows.add(dataEntity);
        }
        collectionDto.setRows(rows);
        collectionDto.setTotalCount(totalCount);
        return RestMessage.newInstance(true, "查询成功", collectionDto);
    }



    @ApiOperation(value = "在途库存查询加载分页", notes = "在途库存查询加载分页")
    @PostMapping("/zt/loadPager")
    public RestMessage<DataEntityCollectionDto> ztloadPager(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        Map<String, Object> reStr = helper.getPagerDto(zt_kucun_view_cloumn, zt_kucun_view, "condition", BasePagerDto.class);
        // 获得拼装后的JSON对象
        int startRowNum = (int)reStr.get("startRowNum");
        int endRowNum = (int)reStr.get("endRowNum");
        String condition = reStr.get("conditions").toString();
        String orders = reStr.get("orders").toString();
        List<DataEntity> rows = basePagerService.getPagers(zt_kucun_view_cloumn, zt_kucun_view, condition, orders, startRowNum, endRowNum);
        DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
        collectionDto.setRows(rows);
        return RestMessage.newInstance(true, "查询成功", collectionDto);
    }

    @ApiOperation(value = "在途库存查询第一次加载分页", notes = "在途库存查询第一次加载分页")
    @PostMapping("/zt/firstLoadPager")
    public RestMessage<DataEntityCollectionDto> ztfirstLoadPager(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        Map<String, Object> reStr = helper.getPagerDto(zt_kucun_view_cloumn, zt_kucun_view, "condition", BasePagerDto.class);
        // 获得拼装后的JSON对象
        int startRowNum = (int)reStr.get("startRowNum");
        int endRowNum = (int)reStr.get("endRowNum");
        String condition = reStr.get("conditions").toString();
        String orders = reStr.get("orders").toString();
        // 第一次加载需要获得总数量
        int totalCount = basePagerService.getTotalCount(zt_kucun_view, condition);
        List<DataEntity> rows = basePagerService.getPagers(zt_kucun_view_cloumn, zt_kucun_view, condition, orders, startRowNum, endRowNum);
        DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
        collectionDto.setRows(rows);
        collectionDto.setTotalCount(totalCount);
        return RestMessage.newInstance(true, "查询成功", collectionDto);
    }


    @ApiOperation(value = "在途料箱监控(任务数查询)", notes = "在途料箱监控(任务数查询)")
    @PostMapping("/zt/boxTasks")
    public RestMessage<ZtBoxStatisDto> boxTasks() throws Exception{
        ZtBoxStatisDto dto = yqMonitorService.boxTasks();
        return RestMessage.newInstance(true, "查询成功", dto);
    }

    @ApiOperation(value = "在途料箱监控(站台统计)", notes = "在途料箱监控(站台统计)")
    @PostMapping("/zt/station")
    public RestMessage<List<ZtStationStatisDto>> station() throws Exception{
        List<ZtStationStatisDto> dtos = yqMonitorService.station();
        return RestMessage.newInstance(true, "查询成功", dtos);
    }

    @ApiOperation(value = "在途料箱监控(在途料箱详情)", notes = "在途料箱监控(在途料箱详情)")
    @PostMapping("/zt/ztBoxMx")
    public RestMessage<List<ZtBoxMxDto>> ztBoxMx() throws Exception{
        List<ZtBoxMxDto> dto = yqMonitorService.ztBoxMx();
        return RestMessage.newInstance(true, "查询成功", dto);
    }

    @PostMapping("/deleteContainerNo")
    @ApiOperation(value = "删除在途的数据和业务数据", notes = "删除在途的数据和业务数据")
    public RestMessage deleteContainerNo(@RequestBody String json) throws Exception{
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        String containerNo = helper.getString("containerNo");
        Log log = new Log(null, containerNo, "删除在途", "EIS", null, null, null, null, new Date());
        try{
            yqMonitorService.deleteContainerNo(containerNo);
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
