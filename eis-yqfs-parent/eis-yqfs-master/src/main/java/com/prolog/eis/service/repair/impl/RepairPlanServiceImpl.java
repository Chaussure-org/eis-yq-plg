package com.prolog.eis.service.repair.impl;

import com.prolog.eis.dao.masterbase.GoodsMapper;
import com.prolog.eis.dao.repair.RepairPlanHisMapper;
import com.prolog.eis.dao.repair.RepairPlanMapper;
import com.prolog.eis.dao.repair.RepairPlanMxHisMapper;
import com.prolog.eis.dao.repair.RepairPlanMxMapper;
import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.dto.repair.RepairPlanDto;
import com.prolog.eis.dto.repair.RepairStoreDto;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.repair.RepairPlan;
import com.prolog.eis.model.repair.RepairPlanMx;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.service.repair.RepairPlanService;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RepairPlanServiceImpl implements RepairPlanService {

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private StoreMapper storeMapper;
	@Autowired
	private RepairPlanMapper repairPlanMapper;

	@Autowired
	private RepairPlanMxMapper repairPlanMxMapper;

	@Autowired
	private RepairPlanHisMapper repairPlanHisMapper;

	@Autowired
	private RepairPlanMxHisMapper repairPlanMxHisMapper;

	@Override
	public RepairPlanDto createRepairPlan(String goodsNo, String operator) throws Exception {
		List<Goods> list = goodsMapper.findByMap(MapUtils.put("goodsNo", goodsNo).getMap(), Goods.class);
		if (list.isEmpty()) {
			throw new Exception("找不到物料" + goodsNo);
		}
		if (StringUtils.isEmpty(operator)) {
			throw new Exception("请提供操作人信息");
		}
		Goods goods = list.get(0);
		RepairPlan repairPlan = new RepairPlan();
		repairPlan.setGoodsId(goods.getId());
		repairPlan.setOperator(operator);
		repairPlan.setCreateTime(new Date());
		repairPlanMapper.save(repairPlan);

		RepairPlanDto dto = new RepairPlanDto();
		dto.setRepairPlanId(repairPlan.getId());
		dto.setGoodsNo(goods.getGoodsNo());
		dto.setGoodsName(goods.getGoodsName());
		dto.setRecomType(goods.getRecomType());
		dto.setRecomNumber(goods.getRecomNumber());

		return dto;
	}

	@Override
	public List<RepairStoreDto> getAllStoreByGoodsNo(String goodsNo) throws Exception {
		List<RepairStoreDto> list = repairPlanMapper.getAllStoreByGoodsNo(goodsNo);
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void createRepairPlanMx(int repairPlanId, List<RepairStoreDto> repairStoreDtos) throws Exception {
		RepairPlan repairPlan = repairPlanMapper.findById(repairPlanId, RepairPlan.class);
		if (repairPlan == null) {
			throw new Exception("repairPlanId:" + repairPlanId + "错误！");
		}
		List<RepairPlanMx> list = new ArrayList<>();
		for(RepairStoreDto dto : repairStoreDtos) {
			RepairPlanMx mx = new RepairPlanMx();
			mx.setRepairPlanId(repairPlanId);
			mx.setContainerNo(dto.getContainerNo());
			mx.setRepairStatus(RepairPlanMx.REPAIR_STATUS_ING);
			mx.setCreateTime(new Date());
			mx.setContainerSubNo(dto.getContainerSubNo());
			list.add(mx);
		}
		
		if(!list.isEmpty()) {
			repairPlanMxMapper.saveBatch(list);
			List<String> containers =  list.stream().map(RepairPlanMx::getContainerNo).collect(Collectors.toList());
			Criteria ctr=Criteria.forClass(SxStore.class);
			ctr.setRestriction(Restrictions.in("containerNo",containers.toArray()));
			storeMapper.updateMapByCriteria(MapUtils.put("taskType",SxStore.TASKTYPE_BH).getMap(),ctr);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public RestMessage<Object> repairPlanCancel(int repairPlanId) {
		RepairPlan repairPlan = repairPlanMapper.findById(repairPlanId, RepairPlan.class);
		if (repairPlan == null) {
			return RestMessage.newInstance(false, "repairPlanId:" + repairPlanId + "错误！", null);
		}
		List<RepairPlanMx> mxs = repairPlanMxMapper.findByMap(MapUtils.put("repairPlanId", repairPlanId).getMap(), RepairPlanMx.class);
		List<RepairPlanMx> repairPlanMxes = mxs.stream().filter(x -> x.getRepairStatus() != 1).collect(Collectors.toList());
		if (repairPlanMxes.size()>0){
			return RestMessage.newInstance(false,"repairPlanId:"+repairPlanId+"已有任务下发,不能执行取消操作");
		}else{
			for (RepairPlanMx mx : mxs) {
				repairPlanMxMapper.deleteById(mx.getId(),RepairPlanMx.class);
			}
			repairPlanMapper.deleteById(repairPlanId,RepairPlan.class);
		}
		return RestMessage.newInstance(true, "操作成功！", null);
	}

	@Override
	public List<RepairStoreDto> getRepairPlanMxByPlanId(int repairPlanId) {
		return repairPlanMxMapper.getRepairPlanMxByPlanId(repairPlanId);
	}

	@Override
	public List<RepairPlanMx> getRepairPlanMx(Map map) {
		return repairPlanMxMapper.findByMap(map,RepairPlanMx.class);
	}

	@Override
	public void changeStatus(String containerNo) {
		List<RepairPlanMx> repairPlanMxes = repairPlanMxMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), RepairPlanMx.class);
		RepairPlanMx repairPlanMx = repairPlanMxes.get(0);
		repairPlanMx.setRepairStatus(RepairPlanMx.REPAIR_STATUS_XF);
		repairPlanMxMapper.update(repairPlanMx);

	}

}
