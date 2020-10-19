package com.prolog.eis.service.masterbase.impl;

import com.prolog.eis.dao.assembl.AssemblBoxHzMapper;
import com.prolog.eis.dao.assembl.AssemblBoxMxMapper;
import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dao.masterbase.GoodsBarCodeMapper;
import com.prolog.eis.dao.masterbase.GoodsMapper;
import com.prolog.eis.dao.order.ContainerBindingHzMapper;
import com.prolog.eis.dao.order.ContainerSubBindingMxMapper;
import com.prolog.eis.dao.order.OrderHzMapper;
import com.prolog.eis.dao.order.OrderMxMapper;
import com.prolog.eis.dao.pd.PdTaskDetailMapper;
import com.prolog.eis.dao.pd.PdTaskMapper;
import com.prolog.eis.dao.repair.RepairPlanMapper;
import com.prolog.eis.dao.repair.RepairPlanMxMapper;
import com.prolog.eis.dao.repair.RepairTaskHzMapper;
import com.prolog.eis.dao.repair.RepairTaskMxMapper;
import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.dao.store.ZtStoreMapper;
import com.prolog.eis.dao.yqfs.OutboundWarnMapper;
import com.prolog.eis.model.assembl.AssemblBoxHz;
import com.prolog.eis.model.assembl.AssemblBoxMx;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.masterbase.GoodsBarCode;
import com.prolog.eis.model.order.ContainerBindingHz;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.eis.model.order.OrderHz;
import com.prolog.eis.model.order.OrderMx;
import com.prolog.eis.model.pd.PdTask;
import com.prolog.eis.model.pd.PdTaskDetail;
import com.prolog.eis.model.repair.RepairPlan;
import com.prolog.eis.model.repair.RepairPlanMx;
import com.prolog.eis.model.repair.RepairTaskHz;
import com.prolog.eis.model.repair.RepairTaskMx;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.ZtStore;
import com.prolog.eis.model.yqfs.OutboundWarn;
import com.prolog.eis.service.masterbase.GoodsService;
import com.prolog.eis.util.PrologDateUtils;
import com.prolog.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
public class GoodsServiceImpl implements GoodsService{
	
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsBarCodeMapper goodsBarCodeMapper;
	@Autowired
	private AssemblBoxMxMapper assemblBoxMxMapper;
	@Autowired
	private AssemblBoxHzMapper assemblBoxHzMapper;

	@Autowired
	private ContainerSubBindingMxMapper containerSubBindingMxMapper;
	@Autowired
	private ContainerBindingHzMapper containerBindingHzMapper;
	@Autowired
	private OrderMxMapper orderMxMapper;
	@Autowired
	private OrderHzMapper orderHzMapper;
	@Autowired
	private OutboundWarnMapper outboundWarnMapper;
	@Autowired
	private PdTaskMapper pdTaskMapper;
	@Autowired
	private PdTaskDetailMapper pdTaskDetailMapper;
	@Autowired
	private RepairPlanMapper repairPlanMapper;
	@Autowired
	private RepairPlanMxMapper repairPlanMxMapper;
	@Autowired
	private RepairTaskHzMapper repairTaskHzMapper;
	@Autowired
	private RepairTaskMxMapper repairTaskMxMapper;
	@Autowired
	private ZtStoreMapper ztStoreMapper;
	@Autowired
	private ContainerSubMapper containerSubMapper;
	@Autowired
	private StoreMapper sxStoreMapper;
	@Override
	public void saveGoods(Goods goods) throws Exception {
		goods.setCreateTime(PrologDateUtils.parseObject(new Date()));
		goodsMapper.save(goods);
		
	}

	@Override
	public void updateGoods(Goods goods) throws Exception {
		if (goods.getId() == 0) {
			throw new Exception("业主id不能为空");
		}
		goodsMapper.update(goods);
	}

	@Override
	public List<Goods> queryGoods() throws Exception {
		return goodsMapper.findByMap(new HashMap<String, Object>(), Goods.class);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteAllGoods() throws Exception {
		//删除合箱明细
		assemblBoxMxMapper.deleteByMap(null, AssemblBoxMx.class);
		//s删除合箱汇总
		assemblBoxHzMapper.deleteByMap(null, AssemblBoxHz.class);

		//删除容器绑定明细
		containerSubBindingMxMapper.deleteByMap(null, ContainerSubBindingMx.class);
		//删除容器绑定汇总
		containerBindingHzMapper.deleteByMap(null, ContainerBindingHz.class);
		//删除出库单明细表,汇总表
		orderMxMapper.deleteByMap(null, OrderMx.class);
		orderHzMapper.deleteByMap(null, OrderHz.class);
		//删除出库预警表
		outboundWarnMapper.deleteByMap(null, OutboundWarn.class);
		//删除盘点明细表及历史表
		pdTaskDetailMapper.deleteByMap(null, PdTaskDetail.class);
		pdTaskMapper.deleteByMap(null,PdTask.class);
		//删除补货
		repairPlanMxMapper.deleteByMap(null, RepairPlanMx.class);
		repairPlanMapper.deleteByMap(null, RepairPlan.class);
		repairTaskMxMapper.deleteByMap(null, RepairTaskMx.class);
		repairTaskHzMapper.deleteByMap(null,RepairTaskHz.class);
		//删除在途库存
		ztStoreMapper.deleteByMap(null,ZtStore.class);
		//删除条码
		goodsBarCodeMapper.deleteByMap(new HashMap<String, Object>(), GoodsBarCode.class);
		goodsMapper.deleteByMap(new HashMap<String, Object>(), Goods.class);
		//修改容器绑定信息
		containerSubMapper.updateContainerSubIsNull();
		//删除箱库库存表
		sxStoreMapper.deleteByMap(null, SxStore.class);
	}

	@Override
	public Goods getGoodsById(int goodsId) {
		return goodsMapper.findById(goodsId,Goods.class);
	}

	@Override
	public List<String> getGoodsABCByIds(int[] goodsIds) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < goodsIds.length; i++) {
			Goods goods = goodsMapper.findById(goodsIds[i], Goods.class);
			if (goods != null&& !StringUtils.isBlank(goods.getAbc())){
				list.add(goods.getAbc());
			}
		}
		return list;
	}

}
