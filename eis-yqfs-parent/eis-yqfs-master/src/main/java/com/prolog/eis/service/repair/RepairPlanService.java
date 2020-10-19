package com.prolog.eis.service.repair;

import java.util.List;
import java.util.Map;

import com.prolog.eis.dto.repair.RepairPlanDto;
import com.prolog.eis.dto.repair.RepairStoreDto;
import com.prolog.eis.model.repair.RepairPlanMx;
import com.prolog.framework.common.message.RestMessage;

public interface RepairPlanService {

	RepairPlanDto createRepairPlan(String goodsNo, String operator) throws Exception;

	List<RepairStoreDto> getAllStoreByGoodsNo(String goodsNo) throws Exception;

	void createRepairPlanMx(int repairId, List<RepairStoreDto> repairStoreDtos) throws Exception;

	RestMessage<Object> repairPlanCancel(int repairPlanId) ;

	List<RepairStoreDto> getRepairPlanMxByPlanId(int repairPlanId);

	List<RepairPlanMx> getRepairPlanMx(Map map) throws Exception;

	void changeStatus(String containerNo);
}
