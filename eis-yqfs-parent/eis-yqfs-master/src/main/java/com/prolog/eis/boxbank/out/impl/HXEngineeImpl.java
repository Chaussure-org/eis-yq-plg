package com.prolog.eis.boxbank.out.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.prolog.eis.boxbank.out.IOutService;
import com.prolog.eis.dao.assembl.AssemblBoxHzMapper;
import com.prolog.eis.dao.assembl.AssemblBoxMxMapper;
import com.prolog.eis.dto.hxdispatch.*;
import com.prolog.eis.model.assembl.AssemblBoxHz;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.pickstation.dao.StationMapper;
import com.prolog.eis.util.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.boxbank.out.HXEnginee;
import com.prolog.framework.utils.MapUtils;

@Service
public class HXEngineeImpl implements HXEnginee {

	@Autowired
	private IOutService outService;
	@Autowired
	private StationMapper stationMapper;
	@Autowired
	private AssemblBoxHzMapper assemblBoxHzMapper;
	@Autowired
	private AssemblBoxMxMapper assemblBoxMxMapper;


	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean chuku(String lxNo,int stationId) throws Exception {
		return outService.checkOut(lxNo,SxStore.TASKTYPE_HX,stationId);
	}




	@Override
	public HxXiangKuDto init() {
		HxXiangKuDto hxXiangKuDto = new HxXiangKuDto();
		//当前合箱作业站台集合
		List<HxStationDto> stationDtos = stationMapper.getHxStations();
		//当前站台已索取的合箱任务
		getAssemblBoxHzList(stationDtos);
		hxXiangKuDto.setStationList(stationDtos);

		//可以索取的合箱任务集合
		List<HxAssemblBoxHz> canPickAssemblBoxHzList = getCanPickAssemblBoxHzList();
		hxXiangKuDto.setCanPickAssemblBoxHzList(canPickAssemblBoxHzList);

		//查询层集合
		List<HxCengDto> cengList = assemblBoxHzMapper.getCengList();
		hxXiangKuDto.setCengList(cengList);
		return hxXiangKuDto;
	}

	@Override
	public void hzBindStation(int hzId, int stationId) throws Exception {
		Assert.isTrue(stationId != 0,"绑定站台id不能为空");
		AssemblBoxHz assemblBoxHz = assemblBoxHzMapper.findById(hzId, AssemblBoxHz.class);
		assemblBoxHzMapper.updateMapById(hzId, MapUtils.put("stationId",stationId).put("taskState",AssemblBoxHz.TASK_STATE_EXECUTE).getMap(),AssemblBoxHz.class);
	}

	/**
	 * 可以索取的合箱任务集合
	 *
	 * @return
	 */
	private List<HxAssemblBoxHz> getCanPickAssemblBoxHzList() {
		List<HxAssemblBoxHz> assemblBoxHzList = assemblBoxHzMapper.CanPickAssemblBoxHzList();
		//当前站台已索取的合箱明细任务
		if (CollectionUtils.isEmpty(assemblBoxHzList)) {
			return assemblBoxHzList;
		}
		//若超过50 分批查询
		List<List<HxAssemblBoxHz>> partitions = Lists.partition(assemblBoxHzList, 50);
		partitions.stream().forEach(t -> getHxMxList(t));
		return assemblBoxHzList;
	}

	/**
	 * 当前站台已索取的合箱任务
	 *
	 * @param stationDtos
	 */
	private void getAssemblBoxHzList(List<HxStationDto> stationDtos) {
		if (CollectionUtils.isEmpty(stationDtos)) {
			return;
		}
		//当前站台已索取的合箱任务
		List<Integer> ztIds = stationDtos.stream().map(HxStationDto::getZhanTaiId).collect(Collectors.toList());
		String ztIdsStr = StringUtils.join(ztIds, ',');
		List<HxAssemblBoxHz> assemblBoxHzList = assemblBoxHzMapper.findByZtIdsStr(ztIdsStr);
		if (CollectionUtils.isEmpty(assemblBoxHzList)) {
			return;
		}

		//当前站台已索取的合箱明细任务
		getHxMxList(assemblBoxHzList);

		Map<Integer, List<HxAssemblBoxHz>> hzMap = assemblBoxHzList.stream().collect(Collectors.groupingBy(HxAssemblBoxHz::getStationId));

		stationDtos.stream().forEach(t -> {
			if (hzMap.containsKey(t.getZhanTaiId())) {
				t.setAssemblBoxHzList(hzMap.get(t.getZhanTaiId()));
			}
		});

	}

	private void getHxMxList(List<HxAssemblBoxHz> assemblBoxHzList) {
		List<Integer> hzIds = assemblBoxHzList.stream().map(HxAssemblBoxHz::getHzId).collect(Collectors.toList());
		String hzIdsStr = StringUtils.join(hzIds, ',');
		List<HxAssemblBoxMx> mxList = assemblBoxMxMapper.findByHzIdsStr(hzIdsStr);

		Map<Integer, List<HxAssemblBoxMx>> mxMap = mxList.stream().collect(Collectors.groupingBy(HxAssemblBoxMx::getAssemblBoxHzId));
		assemblBoxHzList.stream().forEach(t -> {
			if (mxMap.containsKey(t.getHzId())) {
				t.setMxList(mxMap.get(t.getHzId()));
			}
		});
	}

}
