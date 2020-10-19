package com.prolog.eis.dao.store;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.store.ZtBoxMxDto;
import com.prolog.eis.dto.store.ZtBoxStatisDto;
import com.prolog.eis.dto.store.ZtLxDetailsDto;
import com.prolog.eis.dto.store.ZtStationStatisDto;
import com.prolog.eis.model.store.ZtStore;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface ZtStoreMapper extends BaseMapper<ZtStore>{

    /**
     * 在途料箱监控(任务数查询)
     * @return
     */
    @Select("select\n" +
            "count(if(zt.task_type = 1,1,0)) as sowingTasks,\n" +
            "count(if(zt.task_type = 2,1,0)) as inventoryTasks,\n" +
            "count(if(zt.task_type = 3,1,0)) as returnTask,\n" +
            "count(if(zt.task_type = 4,1,0)) as replenishmentTasks,\n" +
            "count(if(zt.task_type = 5,1,0)) as arehousingTasks,\n" +
            "count(if(zt.task_type = 6,1,0)) as boxingTasks,\n" +
            "count(if(zt.task_type = 7,1,0)) as emptytraystorageTasks\n" +
            "from zt_store zt")
    ZtBoxStatisDto boxTasks();

    /**
     * ztStoreMapper
     * @return
     */
    @Select("select s.id                                                          as stationId,\n" +
            "       s.is_claim                                                    as isClaim,\n" +
            "       s.is_lock                                                     as isLock,\n" +
            "       s.station_task_type                                           as stationTaskType,\n" +
            "       (select count(1) from zt_store zt where zt.station_id = s.id) as boxs\n" +
            "from station s")
    List<ZtStationStatisDto> getStation();

    /**
     * ztBoxMx
     * @return
     */
    @Select("SELECT\n" +
			"\ts.station_id AS stationId,\n" +
			"\t(select station_no from station where id = s.station_id) AS stationNo,\n" +
			"\ts.task_type AS containerTaskType,\n" +
			"\ts.container_no AS containerNo,\n" +
			"\ts.gmt_create_time AS createTime,\n" +
			"\ts.`status` AS ztState,\n" +
			"\tcsi.commodity_num AS commodityNum,\n" +
			"\tcsi.container_sub_no AS containerSubNo,\n" +
			"\t( SELECT GROUP_CONCAT( gbc.bar_code ) FROM goods_bar_code gbc WHERE gbc.goods_id = csi.commodity_id ) AS goodsBarCode,\n" +
			"\t( SELECT g.goods_name FROM goods g WHERE g.id = csi.commodity_id ) AS goodsName \n" +
			"FROM\n" +
			"\tzt_store s\n" +
			"\tLEFT JOIN container_sub csi ON csi.container_no = s.container_no \n" +
			"WHERE\n" +
			"\ts.station_id IS NOT NULL")
    List<ZtBoxMxDto> ztBoxMx();
    
    @Select("SELECT\n" +
			"\tt.task_type AS taskType,+ csi.commodity_num AS goodsNum,\n" +
			"\tg.goods_name AS goodsName,\n" +
			"\tcsi.container_sub_no AS containerSubNo,\n" +
			"\tcsi.container_no AS containerNo,\n" +
			"\tcsi.commodity_id AS goodsId,\n" +
			"\tci.layout_type AS layoutType \n" +
			"FROM\n" +
			"\tzt_store t\n" +
			"\tLEFT JOIN container ci ON t.container_no = ci.container_no\n" +
			"\tLEFT JOIN container_sub csi ON t.container_no = csi.container_no\n" +
			"\tLEFT JOIN goods g ON csi.commodity_id = g.id \n" +
			"WHERE\n" +
			"\tt.container_no = #{containerNo}")
    List<ZtLxDetailsDto> findZtLxDeails(@Param("containerNo") String containerNo);
}
