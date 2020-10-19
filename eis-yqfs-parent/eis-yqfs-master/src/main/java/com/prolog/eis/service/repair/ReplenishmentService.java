package com.prolog.eis.service.repair;

import com.prolog.eis.dto.yqfs.ReplenishmentConfirmDto;
import com.prolog.eis.model.repair.RepairTaskHz;
import com.prolog.eis.model.yqfs.RepairStation;
import com.prolog.framework.common.message.RestMessage;

import java.util.Map;

/**
 * @author
 * @description:
 * @date 2020/4/22 17:24
 */
public interface ReplenishmentService {
    /**
     * 容器到位
     * @param containerNo
     */
    RestMessage<Map<String, Object>> containerArrive(String containerNo) throws Exception;

    /**
     * 料箱补货完成
     * @param containerNo
     * @return
     */
    RestMessage<String> complete(String containerNo) throws Exception;

    /**
     * 站台刷新
     * @return
     */
    RestMessage<Map<String,Object>> stationRefresh() throws Exception;

    /**
     * 补货确认
     * @param dto
     * @return
     */
    RestMessage<String> confirm(ReplenishmentConfirmDto dto) throws Exception;

    /**
     * 容器过BCR转历史
     * @param containerNo
     */
    void repairTaskIntoHis(String containerNo);

    void deleteAllRepairTask(String containerNo);

}
