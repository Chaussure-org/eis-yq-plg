package com.prolog.eis.service.store;

import com.prolog.eis.dto.store.LayerTaskCountDto;
import com.prolog.eis.dto.store.LayersStateDto;
import com.prolog.eis.dto.store.StoreTaskDto;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.SxStoreLocation;
import com.prolog.eis.model.store.ZtStore;

import java.util.List;

public interface IStoreService {

    /**
     * 转在途库存
     * @param storeId
     * @throws Exception
     */
    int toZTStore(int storeId,String taskId) throws Exception;

    /**
     * 转箱库库存
     * @param ztStoreId
     * @throws Exception
     */
    void toStore(int ztStoreId,String taskId) throws Exception;

    /**
     * 根据料箱号转箱库库存
     * @param containerNo
     * @param taskId
     * @param locationId
     * @throws Exception
     */
    void toStore(String containerNo,String taskId,int locationId,String pointId) throws Exception;

    /**
     * sunPP
     * 查询层的出库和入库任务数亮
     * @return
     */
    List<LayerTaskCountDto> getLayerTaskCount();


    /**
     * 根据Id更新状态
     * @param id
     * @param status
     * @throws Exception
     */
    void updateStatusById(int id,int status) throws Exception;

    /**
     * 根据容器编号更新状态
     * @param containerNo
     * @param status
     * @throws Exception
     */
    void updateStatusByContainerNo(String containerNo,int status) throws Exception;

    /**
     * 根据id更新在途库存状态
     * @param id
     * @param status
     * @throws Exception
     */
    void updateZtStoreStatusById(int id,int status) throws Exception;

    /**
     * 根据容器编号更新在途库存状态
     * @param containerNo
     * @param status
     * @throws Exception
     */
    void updateZtStoreStatusByContainerNo(String containerNo,int status) throws Exception;

    /**
     * 更新出库状态
     * @param id
     * @param taskId
     * @param taskType
     * @param stationId
     *
     * @throws Exception
     */
    void updateOutStatus(int id,String taskId,int taskType,int stationId) throws Exception;

    /**
     * 根据容器号获取库存
     * @param continerNo
     * @return
     */
    SxStore getByContinerNo(String continerNo);

    /**
     * 根据任务id获取库存
     * @param taskId
     * @return
     */
    SxStore getByTaskId(String taskId);

    //新增库存
    void saveSxStore(List<ContainerSub> containerSubInfos) throws Exception;

    /**
     * 删除在途库存
     * @param id
     */
    void deleteZtStore(int id);

    /**
     * 根据料箱号删除在途库存
     * @param containerNo
     */
    void deleteZtStore(String containerNo);

    /**
     * 删除库存
     * @param id
     */
    void deleteStore(int id);
    /**
     * 根据料箱号删除库存
     * @param containerNo
     */
    void deleteStore(String containerNo);

    void update(SxStore store);

    List<StoreTaskDto> findStoreTasksByHoisId(String hoisId);

    LayersStateDto getLayerState() throws Exception;

    /**
     * 删除入库异常库存
     */
    void deleteAbnormalStore(String containerNo) throws Exception;
    boolean getTaskCountByLayer(int layer);

    List<StoreTaskDto> findStoreTasks();
    /**
     * 根据料箱查在途
     */
    List<ZtStore> findZtStoreByContainerNo(String containerNo) throws Exception;
}
