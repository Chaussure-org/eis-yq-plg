package com.prolog.eis.service.assembl.impl;

import com.prolog.eis.dao.assembl.AssemblBoxHzMapper;
import com.prolog.eis.dao.assembl.AssemblBoxMxMapper;
import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.dto.assembl.AssemblBoxMxDto;
import com.prolog.eis.dto.pd.AssemblBoxDto;
import com.prolog.eis.model.assembl.AssemblBoxHz;
import com.prolog.eis.model.assembl.AssemblBoxMx;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.service.assembl.AssemblBoxService;
import com.prolog.eis.service.pd.PdTaskService;
import com.prolog.eis.util.PrologDateUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.FieldSelector;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssemblBoxServiceImpl implements AssemblBoxService{

	@Autowired
	private AssemblBoxHzMapper assemblBoxHzMapper;
	
	@Autowired
	private AssemblBoxMxMapper assemblBoxMxMapper;
	@Autowired
    private ContainerSubMapper containerSubInfoMapper;
	@Autowired
	private StoreMapper sxStoreMapper;
	@Autowired
	private PdTaskService pdTaskService;

	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveAssemblBox(List<AssemblBoxDto> boxMxs)throws Exception {

		if(boxMxs.size()==0)
			throw new Exception("参数为空");

		//先生成合箱计划数据
		AssemblBoxHz assemblBoxHz = new AssemblBoxHz();
		assemblBoxHz.setTaskState(AssemblBoxHz.TASK_STATE_CREAYE);//默认0创建状态
		assemblBoxHz.setCreateTime(PrologDateUtils.parseObject(new Date()));//创建时间,默认当前时间
		int goodsId = boxMxs.get(0).getGoodsId();
		if(goodsId==0)
			throw new Exception("商品id不能为空");
		assemblBoxHz.setGoodsId(goodsId);
		assemblBoxHzMapper.save(assemblBoxHz);
		//再保存合箱明细数据
		List<AssemblBoxMx> list = new ArrayList<>();
		List<String> containerNos = new ArrayList<>();
		for (AssemblBoxDto dto : boxMxs) {
			//合箱前判断商品是否是同品种
			if (goodsId != dto.getGoodsId()){
				throw new Exception("合箱商品不是同一种商品");
			}
			AssemblBoxMx mx = new AssemblBoxMx();
			mx.setAssemblBoxHzId(assemblBoxHz.getId());
			mx.setContainerNo(dto.getContainerNo());
			mx.setTaskState(AssemblBoxMx.TASK_STATE_WKS);//0未开始
			mx.setCreateTime(PrologDateUtils.parseObject(new Date()));//创建时间,默认当前时间

			containerNos.add(dto.getContainerNo());
			list.add(mx);
		}
		//合箱计划创建前先判定是否符合合箱条件

		String[] objects = containerNos.toArray(new String[containerNos.size()]);
		Criteria criteria = Criteria.forClass(ContainerSub.class);
		criteria.setRestriction(Restrictions.and(Restrictions.in("containerNo",objects),Restrictions.eq("commodityId",goodsId)));
		List<ContainerSub> dates = containerSubInfoMapper.findByCriteria(criteria);

		int size = dates.size();//4
		//过滤null
		List<ContainerSub> containerSubList = dates.stream().filter(x -> x.getExpiryDate() != null).collect(Collectors.toList());//3
		int count = containerSubList.size();
		if (count != 0 ){
			//去重
			int notNull = size - count;
			int size1 = containerSubList.stream().map(ContainerSub::getExpiryDate).collect(Collectors.toSet()).size();//2
			if (size1 != 1){
				throw new Exception("商品有效期不同，创建合箱计划单失败");
			}else if (size1 == 1 && notNull != 0){
				throw new Exception("商品有效期不同，创建合箱计划单失败");
			}
		}
		assemblBoxMxMapper.saveBatch(list);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateAssemblBoxTaskState(int id) throws Exception {
		List<AssemblBoxMx> assemblBoxMxs = assemblBoxMxMapper.findByMap(MapUtils.put("assemblBoxHzId", id).getMap(), AssemblBoxMx.class);

		//任务下发前校验
		List<String> list = new ArrayList<>();
		for (AssemblBoxMx assemblBoxMx : assemblBoxMxs) {
			pdTaskService.checkPublishTask(assemblBoxMx.getContainerNo());

			String containerNo = assemblBoxMx.getContainerNo();
			list.add(containerNo);
		}
		//更改状态(计划单状态)
		assemblBoxHzMapper.updateMapById(id,MapUtils.put("taskState",AssemblBoxHz.TASK_STATE_ISSUE).put("releaseTime",PrologDateUtils.parseObject(new Date())).getMap(),AssemblBoxHz.class);
		Criteria criteria = Criteria.forClass(AssemblBoxMx.class);
		criteria.setRestriction(Restrictions.eq("assemblBoxHzId",id));
		assemblBoxMxMapper.updateMapByCriteria(MapUtils.put("taskState",AssemblBoxMx.TASK_STATE_JXZ).getMap(),criteria);

		//修改料箱状态
		String[] containerNoStr = list.toArray(new String[list.size()]);
		Criteria criteria1 = Criteria.forClass(SxStore.class);
		criteria1.setRestriction(Restrictions.in("containerNo",containerNoStr));
		sxStoreMapper.updateMapByCriteria(MapUtils.put("taskType",SxStore.TASKTYPE_HX).getMap(),criteria1);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteAssemblBoxTask(int id) throws Exception {
		if (id == 0)
			throw new Exception("删除失败，没有传递参数");
		AssemblBoxHz assemblBoxHz = assemblBoxHzMapper.findById(id, AssemblBoxHz.class);
		if (assemblBoxHz.getTaskState() != 0)
			throw new Exception("任务不是创建状态无法删除");
		assemblBoxMxMapper.deleteByMap(MapUtils.put("assemblBoxHzId",id).getMap(),AssemblBoxMx.class);
		assemblBoxHzMapper.deleteById(id,AssemblBoxHz.class);
	}

	@Override
	public void updateAssemblBoxMxTaskState(String containerNo, int id,int taskState) throws Exception {
		Criteria criteria = Criteria.forClass(AssemblBoxMx.class);
		criteria.setRestriction(Restrictions.and(Restrictions.eq("containerNo",containerNo),Restrictions.eq("assemblBoxHzId",id)));
		assemblBoxMxMapper.updateMapByCriteria(MapUtils.put("taskState",taskState).getMap(),criteria);
	}

	@Override
	public List<AssemblBoxMxDto> findAssembBoxMxInfo(int id) throws Exception {
		return assemblBoxMxMapper.findAssembBoxMxById(id);
	}

	@Override
	public void updateMx(AssemblBoxMx mx) throws Exception {
		assemblBoxMxMapper.update(mx);
	}

	@Override
	public void update(AssemblBoxHz hz) throws Exception {
		assemblBoxHzMapper.update(hz);
	}
}
