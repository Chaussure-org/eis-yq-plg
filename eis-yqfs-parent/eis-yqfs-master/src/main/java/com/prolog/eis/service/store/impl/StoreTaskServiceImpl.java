package com.prolog.eis.service.store.impl;

import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dao.order.ContainerBindingHzMapper;
import com.prolog.eis.dao.order.ContainerSubBindingMxMapper;
import com.prolog.eis.dao.order.OrderMxMapper;
import com.prolog.eis.dao.pd.DxPdMapper;
import com.prolog.eis.dao.pickorder.PickOrderMapper;
import com.prolog.eis.dao.store.StoreLocationGroupMapper;
import com.prolog.eis.dao.store.StoreLocationMapper;
import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.order.ContainerBindingHz;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.eis.model.order.OrderMx;
import com.prolog.eis.model.pd.DxPd;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.SxStoreLocation;
import com.prolog.eis.model.store.SxStoreLocationGroup;
import com.prolog.eis.pickstation.model.PickOrder;
import com.prolog.eis.service.masterbase.ContainerService;
import com.prolog.eis.service.store.IStoreLocationService;
import com.prolog.eis.service.store.IStoreService;
import com.prolog.eis.service.store.IStoreTaskService;
import com.prolog.eis.util.CollectionUtils;
import com.prolog.eis.util.ListHelper;
import com.prolog.framework.core.exception.ParameterException;
import com.prolog.framework.utils.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreTaskServiceImpl implements IStoreTaskService {

	@Autowired
	private StoreLocationGroupMapper sxStoreLocationGroupMapper;
	@Autowired
	private IStoreLocationService locationService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private EisProperties properties;
    @Autowired
    private StoreLocationMapper sxStoreLocationMapper;
    @Autowired
    private StoreMapper sxStoreMapper;

    @Autowired
    private ContainerSubBindingMxMapper csbMapper;
    @Autowired
    private ContainerBindingHzMapper cbhMapper;
    @Autowired
    private OrderMxMapper orderMxMapper;
    @Autowired
	private ContainerService containerService;

    @Autowired
    private DxPdMapper dxPdMapper;

	@Override
	@Transactional
	public void computeLocation(int locationId) throws Exception {
		SxStoreLocation sxStoreLocation = locationService.getById(locationId);
		SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper
				.findById(sxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);
		int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
		if (sxStoreLocationGroup.getEntrance() == 1) {
			// 入库朝上的 找最小的有货的索引值，其他的货位减索引值
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findMinHaveStore(sxStoreLocationGroupId);
			Integer haveStoreIndex = 0;
			if (sxStoreLocations.size() > 0) {
				SxStoreLocation sxStoreLocation3 = sxStoreLocations.get(0);
				for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
					int index = sxStoreLocation2.getLocationIndex();
					int count = Math.abs(index - sxStoreLocation3.getLocationIndex());
					sxStoreLocation2.setDeptNum(count);
					if (count == 0) {
						haveStoreIndex = index - 1;
					}
					sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
							MapUtils.put("deptNum", count).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations2 = ListHelper.where(sxStoreLocations, p -> p.getDeptNum() == 0);
				// 找移位数位0的，应该为1个
				List<SxStore> sxStores2 = sxStoreMapper.findByMap(
						MapUtils.put("storeLocationId", sxStoreLocations2.get(0).getId()).getMap(), SxStore.class);
				SxStore sxStore2 = sxStores2.get(0);
//				sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
//						MapUtils.put("entrance1Property1", sxStore2.getTaskProperty1())
//								.put("entrance1Property2", sxStore2.getTaskProperty2()).getMap(),
//						SxStoreLocationGroup.class);
			} else {
				// 找索引最大的货位
				haveStoreIndex = sxStoreLocationGroup.getLocationNum();
				sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
						MapUtils.put("entrance1Property1", null).put("entrance1Property2", null).getMap(),
						SxStoreLocationGroup.class);
			}
			List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
					.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", haveStoreIndex).getMap(),
					SxStoreLocation.class);
			if (sxStoreLocations2.size() == 1) {
				sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
						MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
			}
			List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
					haveStoreIndex);
			if (sxStoreLocations3.size() > 0) {
				List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
				String idstr = StringUtils.join(ids, ",");
				sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
			}
		} else if (sxStoreLocationGroup.getEntrance() == 2) {
			Integer haveStoreIndex = 0;
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findMaxHaveStore(sxStoreLocationGroupId);
			if (sxStoreLocations.size() > 0) {
				SxStoreLocation sxStoreLocation3 = sxStoreLocations.get(0);
				for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
					int index = sxStoreLocation2.getLocationIndex();
					int count = Math.abs(index - sxStoreLocation3.getLocationIndex());
					sxStoreLocation2.setDeptNum(count);
					if (count == 0) {
						haveStoreIndex = index + 1;
					}
					sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
							MapUtils.put("deptNum", count).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations2 = ListHelper.where(sxStoreLocations, p -> p.getDeptNum() == 0);
				// 找移位数位0的，应该为1个
				List<SxStore> sxStores2 = sxStoreMapper.findByMap(
						MapUtils.put("storeLocationId", sxStoreLocations2.get(0).getId()).getMap(), SxStore.class);
				SxStore sxStore2 = sxStores2.get(0);
//				sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
//						MapUtils.put("entrance2Property1", sxStore2.getTaskProperty1())
//								.put("entrance2Property2", sxStore2.getTaskProperty2()).getMap(),
//						SxStoreLocationGroup.class);
			} else {
				haveStoreIndex = 1;
				List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(
						MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", 1).getMap(),
						SxStoreLocation.class);
				sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
						MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
						MapUtils.put("entrance1Property1", null).put("entrance1Property2", null).getMap(),
						SxStoreLocationGroup.class);
			}
			List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
					.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", haveStoreIndex).getMap(),
					SxStoreLocation.class);
			if (sxStoreLocations2.size() == 1) {
				sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
						MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
			}
			List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
					haveStoreIndex);
			if (sxStoreLocations3.size() > 0) {
				List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
				String idstr = StringUtils.join(ids, ",");
				sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
			}

		} else if (sxStoreLocationGroup.getEntrance() == 3) {
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findHaveStore(sxStoreLocationGroupId);
			if (sxStoreLocations.size() > 0) {
				Integer bigHaveStoreIndex = 0;
				Integer smallHaveStoreIndex = 0;
				for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
					int index = sxStoreLocation2.getLocationIndex();
					Integer bigCount = sxStoreLocationMapper.findBigIndexCount(sxStoreLocationGroupId, index);
					Integer smallCount = sxStoreLocationMapper.findSmallIndexCount(sxStoreLocationGroupId, index);
					if (bigCount > smallCount) {
						sxStoreLocation2.setDeptNum(smallCount);
						if (smallCount == 0) {
							smallHaveStoreIndex = index - 1;
						}
						sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
								MapUtils.put("deptNum", smallCount).getMap(), SxStoreLocation.class);
					} else if (bigCount == smallCount && bigCount == 0) {
						smallHaveStoreIndex = index - 1;
						bigHaveStoreIndex = index + 1;
						sxStoreLocation2.setDeptNum(0);
						sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
								MapUtils.put("deptNum", 0).getMap(), SxStoreLocation.class);
					} else {
						if (bigCount == 0) {
							bigHaveStoreIndex = index + 1;// 8
						}
						sxStoreLocation2.setDeptNum(bigCount);
						sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
								MapUtils.put("deptNum", bigCount).getMap(), SxStoreLocation.class);
					}
				}
				// 找出移库数为0的两个或一个，将入库属性赋值
				List<SxStoreLocation> sxStoreLocations2 = ListHelper.where(sxStoreLocations, p -> p.getDeptNum() == 0);
				if (sxStoreLocations2.size() == 1) {
					List<SxStore> sxStores2 = sxStoreMapper.findByMap(
							MapUtils.put("storeLocationId", sxStoreLocations2.get(0).getId()).getMap(), SxStore.class);
					SxStore sxStore2 = sxStores2.get(0);
//					sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
//							MapUtils.put("entrance2Property1", sxStore2.getTaskProperty1())
//									.put("entrance2Property2", sxStore2.getTaskProperty2())
//									.put("entrance1Property1", sxStore2.getTaskProperty1())
//									.put("entrance1Property2", sxStore2.getTaskProperty2()).getMap(),
//							SxStoreLocationGroup.class);
				} else if (sxStoreLocations2.size() == 2) {
					SxStoreLocation sxStoreLocation2 = sxStoreLocations2.get(0);
					SxStoreLocation sxStoreLocation3 = sxStoreLocations2.get(1);
					List<SxStore> sxStores2 = sxStoreMapper.findByMap(
							MapUtils.put("storeLocationId", sxStoreLocation2.getId()).getMap(), SxStore.class);
					SxStore sxStore2 = sxStores2.get(0);
					List<SxStore> sxStores3 = sxStoreMapper.findByMap(
							MapUtils.put("storeLocationId", sxStoreLocation3.getId()).getMap(), SxStore.class);
					SxStore sxStore3 = sxStores3.get(0);
					if (sxStoreLocation2.getLocationIndex() < sxStoreLocation3.getLocationIndex()) {
//						sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
//								MapUtils.put("entrance2Property1", sxStore3.getTaskProperty1())
//										.put("entrance2Property2", sxStore3.getTaskProperty2())
//										.put("entrance1Property1", sxStore2.getTaskProperty1())
//										.put("entrance1Property2", sxStore2.getTaskProperty2()).getMap(),
//								SxStoreLocationGroup.class);
					} else {
//						sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
//								MapUtils.put("entrance2Property1", sxStore2.getTaskProperty1())
//										.put("entrance2Property2", sxStore2.getTaskProperty2())
//										.put("entrance1Property1", sxStore3.getTaskProperty1())
//										.put("entrance1Property2", sxStore3.getTaskProperty2()).getMap(),
//								SxStoreLocationGroup.class);
					}
				} else {
					throw new Exception("移库数的货位有误，货位组ID为【" + sxStoreLocationGroupId + "】");
				}
				// 重新计算入库货位
				List<SxStoreLocation> sxStoreLocationsBig = sxStoreLocationMapper
						.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
								.put("locationIndex", bigHaveStoreIndex).getMap(), SxStoreLocation.class);
				List<SxStoreLocation> sxStoreLocationsSmall = sxStoreLocationMapper
						.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
								.put("locationIndex", smallHaveStoreIndex).getMap(), SxStoreLocation.class);
				if (sxStoreLocationsBig.size() == 1) {
					sxStoreLocationMapper.updateMapById(sxStoreLocationsBig.get(0).getId(),
							MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				}
				if (sxStoreLocationsSmall.size() == 1) {
					sxStoreLocationMapper.updateMapById(sxStoreLocationsSmall.get(0).getId(),
							MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndexTwo(sxStoreLocationGroupId,
						bigHaveStoreIndex, smallHaveStoreIndex);
				if (sxStoreLocations3.size() > 0) {
					List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
					String idstr = StringUtils.join(ids, ",");
					sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
				}
			} else {
				// 奇数货位则最中间为入库货位
				int locationNum = sxStoreLocationGroup.getLocationNum();
				if (locationNum % 2 == 0) {
					int index1 = locationNum / 2;
					List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index1).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					int index2 = locationNum / 2 + 1;
					List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index2).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations3.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					List<SxStoreLocation> sxStoreLocations33 = sxStoreLocationMapper
							.findNotIndexTwo(sxStoreLocationGroupId, index1, index2);
					if (sxStoreLocations33.size() > 0) {
						List<Integer> ids = ListHelper.select(sxStoreLocations33, p -> p.getId());
						String idstr = StringUtils.join(ids, ",");
						sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
					}
				} else {
					int index = locationNum / 2 + 1;
					List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
							index);
					if (sxStoreLocations3.size() > 0) {
						List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
						String idstr = StringUtils.join(ids, ",");
						sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
					}
				}

				sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
						MapUtils.put("entrance2Property1", null).put("entrance2Property2", null)
								.put("entrance1Property1", null).put("entrance1Property2", null).getMap(),
						SxStoreLocationGroup.class);

			}
		}

	}



	/**
	 * 开始出库任务
	 * @param store
	 * @param taskType
	 * @param stationId
	 * @param newTaskId
	 * @throws Exception
	 */
    @Override
//    @Transactional
    public void startOutboundTask(SxStore store, int taskType, int stationId,String newTaskId) throws Exception {
		if(store==null)
			throw new RuntimeException("库存不能为空");
    	//加锁(四周锁  货位组锁)
		locationService.lock(store.getStoreLocationId());
		//存储taskId到store中，并修改任务类型和状态
		storeService.updateOutStatus(store.getId(),newTaskId,taskType,stationId);

		if(store.getTaskType()==SxStore.TASKTYPE_BZ){
			//根据料箱号、站台ID获取绑定明细
			List<ContainerBindingHz> hzList = cbhMapper.findByMap(MapUtils.put("containerNo",store.getContainerNo()).put("stationId",store.getStationId()).getMap(),ContainerBindingHz.class);
			if(hzList.size()>0){
				ContainerBindingHz hz = hzList.get(0);
				hz.setXkStoreId(store.getId());
				cbhMapper.update(hz);
			}

		}
    }

    @Autowired
    private PickOrderMapper pickOrderMapper;
    /**
     * 结束出库任务
     *
     * @throws Exception
     */
	/**
	 * 结束出库任务
	 * @param containerNo
	 * @param newTaskId
	 * @throws Exception
	 */
    @Override
    @Transactional
    public void finishOutboundTask(String containerNo,String newTaskId) throws Exception {
        //出库完成
        SxStore store = storeService.getByContinerNo(containerNo);
        if(store==null){
            throw new ParameterException("根据容器号（"+containerNo+"）找不到库存");
        }
        //解除货位锁
        locationService.unlock(store.getStoreLocationId());

        //设置出库数量
		ContainerBindingHz hz=null;
        if(store.getTaskType()==SxStore.TASKTYPE_BZ){
			//根据料箱号、站台ID获取绑定明细
			List<ContainerBindingHz> hzList = cbhMapper.findByMap(MapUtils.put("containerNo",store.getContainerNo()).put("stationId",store.getStationId()).getMap(),ContainerBindingHz.class);
			if(hzList.size()>0){
				hz = hzList.get(0);
				List<ContainerSubBindingMx> mxList = csbMapper.findByMap(MapUtils.put("containerNo",store.getContainerNo()).getMap(),ContainerSubBindingMx.class);
				if(mxList.size()>0){
					Map<Integer,Long> smp = CollectionUtils.mapSum(mxList, x->x.getOrderMxId(), y->(long)y.getBindingNum());
					smp.forEach((mxId,bnum)->{
						OrderMx odmx = orderMxMapper.findById(mxId,OrderMx.class);
						if(odmx!=null){
							odmx.setOutNum(odmx.getOutNum()+Integer.parseInt(bnum+"") );
							orderMxMapper.update(odmx);
						}
					});
				}
				//判断拣选单是否所有料箱都出库
				int pickOrderId = hz.getPickingOrderId();
				if(!pickOrderMapper.isNotAllArrived(pickOrderId)){
					pickOrderMapper.updateMapById(pickOrderId,MapUtils.put("isAllArrive",1).getMap(),PickOrder.class);
				}

			}
			//动销盘点，只查询有过播种的数据料箱
			List<DxPd> dxPds = dxPdMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), DxPd.class);
			/*if (dxPds.size() > 0) {
				Criteria criteria = Criteria.forClass(DxPd.class);
				criteria.setRestriction(Restrictions.eq("containerNo", containerNo));
				dxPdMapper.updateMapByCriteria(MapUtils.put("updateTime", new Date()).getMap(), criteria);
			}*/
			if (dxPds.size() == 0) {
				DxPd dxPd = new DxPd();
				dxPd.setContainerNo(containerNo);
				dxPd.setUpdateTime(new Date());
				dxPdMapper.save(dxPd);
			}

		}

        //转在途库存
        int ztStoreId = storeService.toZTStore(store.getId(),newTaskId);
		if(hz!=null){
			//更新在途库存id
			cbhMapper.updateStore(ztStoreId,hz.getContainerNo());
		}
    }

    /**
     * 开始入库任务
     *
     * @throws Exception
     */
    @Override
//	@Transactional
    public SxStoreLocation startInboundTask(String containerNo,String newTaskId,String pointId) throws Exception {
		SxStoreLocation location = locationService.getBestEmptyLocation(containerNo,pointId);
		if (location==null){
			List<ContainerSub> subs = containerService.getContainerSubs(containerNo);
			int[] goodsIds = null;
			if (subs.size() > 0) {
				try {
					goodsIds = subs.stream().filter(x -> x.getCommodityId() > 0).mapToInt(x -> x.getCommodityId()).toArray();
				} catch (Exception e) {
				}
			}
			List<Integer> availableLayer = locationService.findAvailableLayer();
			for (Integer integer : availableLayer) {
				location = locationService.getBestLocation(integer, goodsIds);
				if (location!=null){
					break;
				}
			}
		}
    	//加锁
		locationService.lock(location.getId());
    	//转库存
		storeService.toStore(containerNo, newTaskId,location.getId(),pointId);
		return location;
    }

    /**
     * 结束入库任务
     *
     * @throws Exception
     */
    @Override
    public void finishInboundTask(String containerNo) throws Exception {
		SxStore store = storeService.getByContinerNo(containerNo);
		if(store==null)
			throw new ParameterException("找不到料箱("+containerNo+")库存");
		store.setTaskType(SxStore.TASKTYPE_NULL);
		store.setStoreState(SxStore.STATE_UP);
		store.setTaskId(null);
		store.setStationId(0);
		store.setHoisterId(null);
		//解锁
		locationService.unlock(store.getStoreLocationId());
		storeService.update(store);
    }

    @Override
	@Transactional
	public void computeIsInBoundLocation() throws Exception {
		List<SxStoreLocationGroup> sxStoreLocationGroups = sxStoreLocationGroupMapper
				.findByMap(new HashMap<String, Object>(), SxStoreLocationGroup.class);
		for (SxStoreLocationGroup sxStoreLocationGroup : sxStoreLocationGroups) {
			if (sxStoreLocationGroup.getEntrance() == 3) {
				// 奇数货位则最中间为可用货位
				int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
				int locationNum = sxStoreLocationGroup.getLocationNum();
				if (locationNum % 2 == 0) {
					int index1 = locationNum / 2;
					List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index1).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					int index2 = locationNum / 2 + 1;
					List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index2).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations3.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					List<SxStoreLocation> sxStoreLocationsNotIndex = sxStoreLocationMapper
							.findNotIndexTwo(sxStoreLocationGroupId, index1, index2);
					if (sxStoreLocationsNotIndex.size() > 0) {
						List<Integer> ids = ListHelper.select(sxStoreLocationsNotIndex, p -> p.getId());
						String idstr = StringUtils.join(ids, ",");
						sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
					}
				} else {
					int index = locationNum / 2 + 1;
					List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
							index);
					if (sxStoreLocations3.size() > 0) {
						List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
						String idstr = StringUtils.join(ids, ",");
						sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
					}
				}
			} else if (sxStoreLocationGroup.getEntrance() == 1) {
				int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
				int haveStoreIndex = sxStoreLocationGroup.getLocationNum();
				List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper
						.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
								.put("locationIndex", haveStoreIndex).getMap(), SxStoreLocation.class);
				if (sxStoreLocations2.size() == 1) {
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
						haveStoreIndex);
				if (sxStoreLocations3.size() > 0) {
					List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
					String idstr = StringUtils.join(ids, ",");
					sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
				}
			} else if (sxStoreLocationGroup.getEntrance() == 2) {
				int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
				int haveStoreIndex = 1;
				List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper
						.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
								.put("locationIndex", haveStoreIndex).getMap(), SxStoreLocation.class);
				if (sxStoreLocations2.size() == 1) {
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
						haveStoreIndex);
				if (sxStoreLocations3.size() > 0) {
					List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
					String idstr = StringUtils.join(ids, ",");
					sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
				}
			}
		}
	}

}
