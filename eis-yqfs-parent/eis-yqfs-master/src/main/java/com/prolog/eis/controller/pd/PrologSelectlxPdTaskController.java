package com.prolog.eis.controller.pd;

import com.prolog.eis.dao.pd.DxPdMapper;
import com.prolog.eis.dto.base.BasePagerDto;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.dto.base.DataEntityCollectionDto;
import com.prolog.eis.model.pd.DxPd;
import com.prolog.eis.service.masterbase.BasePagerService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.core.restriction.Criteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Api(tags = "SelectlxPdTask管理")
@RequestMapping("/api/v1/master/selectLxPdTask")
public class PrologSelectlxPdTaskController {

    @Autowired
    private BasePagerService basePagerService;

    @Autowired
    private DxPdMapper dxPdMapper;

    @ApiOperation(value = "加载分页", notes = "加载分页")
    @PostMapping("/loadPager")
    public RestMessage<DataEntityCollectionDto> loadPager(@RequestBody String json) throws Exception {
        String columns = this.getColumns();
        String tableName = this.getTableName();
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        int pdType = helper.getInt("pdType");
        Map<String, Object> reStr = helper.getPagerDto(columns, tableName, "condition", BasePagerDto.class);
        // 获得拼装后的JSON对象
        int startRowNum = (int) reStr.get("startRowNum");
        int endRowNum = (int) reStr.get("endRowNum");
        String condition = reStr.get("conditions").toString();
        boolean flag = condition.contains("and pdType = '2'");
        condition = condition.replaceAll(" and pdType = '1'", "")
                .replaceAll(" and pdType = '2'", "")
                .replaceAll(" and pdType = '3'", "")
                .replaceAll(" and pdType = '4'", "")
                .replaceAll(" and pdType = '5'", "");
        String orders = reStr.get("orders").toString();
        List<DataEntity> rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum,
                endRowNum);
        if (flag) {
            Criteria criteria = new Criteria(DxPd.class);
            Set<String> containerNos = dxPdMapper.findByCriteria(criteria).stream().map(DxPd::getContainerNo).collect(Collectors.toSet());

            Iterator<DataEntity> iterator = rows.iterator();
            while (iterator.hasNext()) {
                DataEntity dataEntity = iterator.next();
                Map<String, Object> map = dataEntity.getDatas();
                String containerNo = (String) map.get("containerNo");
                if (!containerNos.contains(containerNo)) {
                    iterator.remove();
                }
            }
        }else {



        }
        DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
        collectionDto.setRows(rows);
        return RestMessage.newInstance(true, "查询成功", collectionDto);
    }

    @ApiOperation(value = "第一次加载分页", notes = "第一次加载分页")
    @PostMapping("/firstLoadPager")
    public RestMessage<DataEntityCollectionDto> firstLoadPager(@RequestBody String json) throws Exception {
        String columns = this.getColumns();
        String tableName = this.getTableName();
        String dxTableName = this.geDxTableName();
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        Map<String, Object> reStr = helper.getPagerDto(columns, tableName, "condition", BasePagerDto.class);
        // 获得拼装后的JSON对象
        int startRowNum = (int) reStr.get("startRowNum");
        int endRowNum = (int) reStr.get("endRowNum");
        String condition = reStr.get("conditions").toString();
        boolean flag = condition.contains("and pdType = '2'");
        condition = condition.replaceAll(" and pdType = '1'", "")
                .replaceAll(" and pdType = '2'", "")
                .replaceAll(" and pdType = '3'", "")
                .replaceAll(" and pdType = '4'", "")
                .replaceAll(" and pdType = '5'", "");
        String orders = reStr.get("orders").toString();
        // 第一次加载需要获得总数量
        int totalCount ;
        List<DataEntity> rows ;
        if (flag) {
            totalCount = basePagerService.getTotalCount(dxTableName, condition);
            rows = basePagerService.getPagers(columns, dxTableName, condition, orders, startRowNum,
                    endRowNum);
        } else {
            totalCount = basePagerService.getTotalCount(tableName, condition);
            rows = basePagerService.getPagers(columns, tableName, condition, orders, startRowNum,
                    endRowNum);
        }

        DataEntityCollectionDto collectionDto = new DataEntityCollectionDto();
        collectionDto.setRows(rows);
        collectionDto.setTotalCount(totalCount);
        return RestMessage.newInstance(true, "查询成功", collectionDto);
    }

	private String getColumns() {
		return "id,containerNo,containerSubNo,goodsNo,goodsName,x,y,layer,originalCount,goodsBarCode";
	}

	private String getTableName() {
		return "pdtaskdetailview";
	}

    private String geDxTableName() {
        return "dxpdtaskdetailview";
    }
	

}
