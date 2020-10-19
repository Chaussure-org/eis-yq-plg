package com.prolog.eis.service.store;

import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.SxStoreLocation;

public interface IStoreTaskService {

    /**
     * 开始出库任务
     * @param store
     * @param taskType
     * @param stationId
     * @param newTaskId
     * @throws Exception
     */
    void startOutboundTask(SxStore store, int taskType, int stationId,String newTaskId) throws Exception;

    /**
     * 结束出库任务
     * @param containerNo
     * @param newTaskId
     * @throws Exception
     */
    void finishOutboundTask(String containerNo,String newTaskId) throws Exception;

    /**
     * 开始入库任务
     * @param containerNo
     * @param newTaskId
     * @throws Exception
     */
    SxStoreLocation startInboundTask(String containerNo, String newTaskId, String pointId) throws Exception;

    /**
     * 结束入库任务
     * @throws Exception
     */
    void finishInboundTask(String containerNo) throws Exception;


	/**
	 * 计算所有货位的入库货位
	 * @throws Exception
	 */
	void computeIsInBoundLocation()throws Exception;

    /**
     * 重新计算货位
     * @param locationId
     * @throws Exception
     */
    void computeLocation(int locationId)throws Exception;


}
