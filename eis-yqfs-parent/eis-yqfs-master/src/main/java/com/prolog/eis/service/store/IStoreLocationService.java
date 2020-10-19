package com.prolog.eis.service.store;

import java.util.List;

import com.prolog.eis.dto.store.StoreLocationGroupDto;
import com.prolog.eis.model.store.SxStoreLocation;


public interface IStoreLocationService {
	/**
	 * 根据ID查找货位
	 * @param id
	 * @return
	 */
	SxStoreLocation getById(int id);

	/**
	 * 导入货位
	 * @param storeLocationGroupDtos
	 * @throws Exception
	 */
	void importStoreLocation(List<StoreLocationGroupDto> storeLocationGroupDtos)throws Exception;

	/**
	 * 根据货位组查找货位
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	List<SxStoreLocation> getByGroupId(int groupId) throws Exception;

	/**
	 * 初始化货位及货位组
	 * @param layerCount
	 * @param xCount
	 * @param yCount
	 * @throws Exception
	 */
	void initData(int layerCount,int xCount,int yCount) throws Exception;

	/**
	 * 查找总货位数
	 * @return
	 */
	long getTotalCount();

	/**
	 * 生成并保存容器编号四周关系
	 */
	void saveSxStoreRelation();

	/**
	 * 根据容器寻找最佳空货位
	 * @param containerNo
	 * @return
	 */
	SxStoreLocation getBestEmptyLocation(String containerNo,String pointId)  throws Exception;

	/**
	 * 根据商品寻找空货位层最佳层
	 * @param goodsIds
	 * @return
	 */
	int getBestEmpytLayer(int[] goodsIds,String pointId);

	/**
	 * 根据货位编号获取货位
	 * @param number
	 * @return
	 */
	SxStoreLocation getbyNumber(String number);

	/**
	 * 锁定货位
	 * @param locationId
	 * @throws Exception
	 */
	void lock(int locationId) throws Exception;

	/**
	 * 解除货位锁定
	 * @param locationId
	 * @throws Exception
	 */
	void unlock(int locationId) throws Exception;

	/**
	 * 判断货位是否被锁定
	 * @param locationId
	 * @throws Exception
	 */
	boolean isUnlock(int locationId) throws Exception;

	/**
	 * 判断货位是否被锁定，通过料箱号
	 * @param containerNo
	 * @throws Exception
	 */
	boolean isUnlock(String containerNo) throws Exception;

	/**
	 * 根据货位去锁当前深位
	 * @param storeNo
	 * @throws Exception
	 */
	void unlockRelate(String storeNo) throws Exception;

	List<SxStoreLocation> findAvailableLocationByLayer(int layer);
	void update(SxStoreLocation sxStoreLocation);
	List<Integer> findAvailableLayer();
	SxStoreLocation getBestLocation(int layer, int[] goodsIds);
}
