package com.prolog.eis.service.store.impl;

import com.prolog.eis.dao.store.*;
import com.prolog.eis.dto.store.LayerTaskCountDto;
import com.prolog.eis.dto.store.LayersStateDto;
import com.prolog.eis.dto.store.SxLayerDto;
import com.prolog.eis.dto.store.StoreTaskDto;
import com.prolog.eis.enums.HoisterEnum;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.store.*;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.eis.service.monitor.YqMonitorService;
import com.prolog.eis.service.store.IStoreService;
import com.prolog.eis.util.PrologDateUtils;
import com.prolog.eis.wcs.dao.WCSCommandMapper;
import com.prolog.eis.wcs.dto.CarInfoDTO;
import com.prolog.eis.wcs.impl.WCSServiceImpl;
import com.prolog.eis.wcs.model.WCSCommand;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.core.exception.ParameterException;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements IStoreService {

    private static final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private ZtStoreMapper ztStoreMapper;
    @Autowired
    private SxLayerMapper layerMapper;

    @Autowired
    private StoreLocationMapper sxStoreLocationYqfsMapper;
    @Autowired
    private ContainerSubService containerSubInfoService;

    @Autowired
    private WCSServiceImpl wcsService;
    @Autowired
    private IWCSTaskService iwcsTaskService;
    @Autowired
    private WCSCommandMapper wcsCommandMapper;
    @Autowired
    private IWCSService iwcsService;
    @Autowired
    private StoreLocationMapper sxStoreLocationMapper;
    @Autowired
    private StoreLocationGroupMapper sxStoreLocationGroupMapper;
    @Autowired
    private StoreLocationRelationMapper storeLocationRelationMapper;
    @Autowired
    private YqMonitorService yqMonitorService;
    @Autowired
    private ContainerSubService containerSubService;
    /**
     * 转在途库存
     *
     * @param storeId
     * @throws Exception
     */
    @Transactional
    @Override
    public int toZTStore(int storeId,String taskId) throws Exception {
        SxStore store = storeMapper.findById(storeId,SxStore.class);
        ZtStore ztStore = new ZtStore();

        ztStore.setContainerNo(store.getContainerNo());
        ztStore.setTaskType(store.getTaskType());
        ztStore.setStatus(ZtStore.Status_To_Station);
        ztStore.setStationId(store.getStationId());
        ztStore.setLastOutTime(store.getLastOutTime());
        ztStore.setLastUpdateTime(new Date());
        ztStore.setStoreTime(store.getStoreTime());
        ztStore.setLastPdTime(store.getLastPdTime());
        ztStore.setTaskId(taskId);
        ztStore.setCreateTime(new Date());

        storeMapper.deleteById(store.getId(),SxStore.class);
        ztStoreMapper.save(ztStore);
        return ztStore.getId();
    }

    /**
     * 转箱库库存
     *
     * @param ztStoreId
     * @throws Exception
     */
    @Transactional
    @Override
    public void toStore(int ztStoreId,String taskId) throws Exception {
        ZtStore ztStore = ztStoreMapper.findById(ztStoreId,ZtStore.class);
        SxStore store = new SxStore();
        store.setContainerNo(ztStore.getContainerNo());
        store.setCreateTime(new Date());
        store.setStoreTime(ztStore.getStoreTime());
        store.setLastOutTime(ztStore.getLastOutTime());
        store.setLastPdTime(ztStore.getLastPdTime());
        store.setLastUpdateTime(ztStore.getLastUpdateTime());
        store.setStationId(ztStore.getStationId());
        store.setStoreLocationId(0);
        store.setStoreState(SxStore.STATE_IN);
        store.setTaskId(taskId);
        store.setTaskType(SxStore.TASKTYPE_IN);

        storeMapper.save(store);
        ztStoreMapper.deleteById(ztStoreId,ZtStore.class);
    }

    /**
     * 根据料箱号转箱库库存
     *
     * @param containerNo
     * @param taskId
     * @throws Exception
     */
    @Override
    public void toStore(String containerNo, String taskId,int locationId,String pointId) throws Exception {
        List<ZtStore> ztStores = ztStoreMapper.findByMap(MapUtils.put("containerNo",containerNo).getMap(),ZtStore.class);
        if(ztStores.size()==0)
            throw new ParameterException("料箱号（"+containerNo+"）找不到在途库存");
        ZtStore ztStore = ztStores.get(0);

        SxStore store = new SxStore();
        store.setContainerNo(ztStore.getContainerNo());
        store.setCreateTime(new Date());
        store.setStoreTime(ztStore.getStoreTime());
        store.setLastOutTime(ztStore.getLastOutTime());
        store.setLastPdTime(ztStore.getLastPdTime());
        store.setLastUpdateTime(ztStore.getLastUpdateTime());
        store.setStationId(ztStore.getStationId());
        store.setStoreLocationId(locationId);
        store.setStoreState(SxStore.STATE_IN);
        store.setTaskId(taskId);
        store.setTaskType(ztStore.getTaskType());
        // 添加入库时箱库指定提升机
        store.setHoisterId(HoisterEnum.getValues(pointId));

        storeMapper.save(store);
        ztStoreMapper.deleteById(ztStore.getId(),ZtStore.class);
    }

    @Override
    public List<LayerTaskCountDto> getLayerTaskCount() {

        List<CarInfoDTO> carInfoList = wcsService.getCarInfo();
        List<LayerTaskCountDto> layerTaskCountList = storeMapper.getLayerTaskCount();
        /*if (carInfoList.size()==0){
            return null;
        }
        layerTaskCountList.stream().forEach(p->{
            CarInfoDTO carInfoDTO = carInfoList.stream().filter(x -> x.getLayer() == p.getLayer()).findFirst().get();
            p.setRgvId(carInfoDTO.getRgvId());
            p.setStatus(carInfoDTO.getStatus());
        });*/
        return layerTaskCountList;
    }

    /**
     * 根据Id更新状态
     *
     * @param id
     * @param status
     * @throws Exception
     */
    @Override
    public void updateStatusById(int id, int status) throws Exception {
        storeMapper.updateMapById(id,MapUtils.put("storeState",status).getMap(),SxStore.class);
    }

    /**
     * 根据容器编号更新状态
     *
     * @param containerNo
     * @param status
     * @throws Exception
     */
    @Override
    public void updateStatusByContainerNo(String containerNo, int status) throws Exception {
        Criteria ctr = Criteria.forClass(SxStore.class);
        ctr.setRestriction(Restrictions.eq("containerNo",containerNo));
        storeMapper.updateMapByCriteria(MapUtils.put("storeState",status).getMap(),ctr);
    }

    /**
     * 根据id更新在途库存状态
     *
     * @param id
     * @param status
     * @throws Exception
     */
    @Override
    public void updateZtStoreStatusById(int id, int status) throws Exception {
        ztStoreMapper.updateMapById(id,MapUtils.put("status",status).getMap(),ZtStore.class);
    }

    /**
     * 根据容器编号更新在途库存状态
     *
     * @param containerNo
     * @param status
     * @throws Exception
     */
    @Override
    public void updateZtStoreStatusByContainerNo(String containerNo, int status) throws Exception {
        Criteria ctr = Criteria.forClass(ZtStore.class);
        ctr.setRestriction(Restrictions.eq("containerNo",containerNo));
        storeMapper.updateMapByCriteria(MapUtils.put("status",status).getMap(),ctr);
    }


    /**
     * 更新出库状态
     *
     * @param id
     * @param taskId
     * @param taskType
     * @throws Exception
     */
    @Override
    public void updateOutStatus(int id, String taskId, int taskType,int stationId) throws Exception {
        storeMapper.updateMapById(id,MapUtils.put("taskId",taskId).put("taskType",taskType).put("storeState",SxStore.STATE_OUT).put("stationId",stationId).getMap(),SxStore.class);
    }

    /**
     * 根据容器号获取库存
     *
     * @param continerNo
     * @return
     */
    @Override
    public SxStore getByContinerNo(String continerNo) {
        List<SxStore> list = storeMapper.findByMap(MapUtils.put("containerNo", continerNo).getMap(), SxStore.class);
        return list.size()==0?null:list.get(0);
    }

    /**
     * 根据任务id获取库存
     *
     * @param taskId
     * @return
     */
    @Override
    public SxStore getByTaskId(String taskId) {
        List<SxStore> list = storeMapper.findByMap(MapUtils.put("taskId", taskId).getMap(), SxStore.class);
        return list.size()==0?null:list.get(0);
    }

    //新增库存
    @Override
    public void saveSxStore(List<ContainerSub> containerSubInfos) throws Exception {
        //更新子容器信息
        containerSubInfoService.updateContainerSubInfo(containerSubInfos);
        HashSet<String> containerNos = new HashSet<>();
        //分货位
        for (ContainerSub containerSubInfo : containerSubInfos) {
            containerNos.add(containerSubInfo.getContainerNo());
        }
        for (String containerNo : containerNos) {
            SxStore sxStore = new SxStore();
            sxStore.setContainerNo(containerNo);
//            sxStore.setContainerSubNo(containerSubInfo.getContainerSubNo());
            //分配货位
            sxStore.setStoreLocationId(sxStoreLocationYqfsMapper.getStoreLocationId());
            sxStore.setStationId(0);
            sxStore.setTaskType(SxStore.TASKTYPE_NULL);
            sxStore.setStoreState(SxStore.STATE_UP);
            sxStore.setCreateTime(PrologDateUtils.parseObject(new Date()));
            sxStore.setStoreTime(PrologDateUtils.parseObject(new Date()));
            storeMapper.save(sxStore);
        }
    }

    /**
     * 删除在途库存
     *
     * @param id
     */
    @Override
    public void deleteZtStore(int id) {
        ztStoreMapper.deleteById(id,ZtStore.class);
    }

    /**
     * 根据料箱号删除在途库存
     *
     * @param containerNo
     */
    @Override
    public void deleteZtStore(String containerNo) {
        ztStoreMapper.deleteByMap(MapUtils.put("containerNo",containerNo).getMap(),ZtStore.class);
    }

    /**
     * 删除库存
     *
     * @param id
     */
    @Override
    public void deleteStore(int id) {
        storeMapper.deleteById(id,  SxStore.class);
    }

    /**
     * 根据料箱号删除库存
     *
     * @param containerNo
     */
    @Override
    public void deleteStore(String containerNo) {
        storeMapper.deleteByMap(MapUtils.put("containerNo",containerNo).getMap(),SxStore.class);

    }

    @Override
    public void update(SxStore store) {
        storeMapper.update(store);
    }

    @Override
    public List<StoreTaskDto> findStoreTasksByHoisId(String hoisId){
        return storeMapper.findStoreTasksByHoisId(hoisId);
    }
    @Override
    public LayersStateDto getLayerState() throws Exception {
        List<SxLayerDto> layerInfo = storeMapper.getLayerInfo();
        LayersStateDto layersStateDto = new LayersStateDto();
        layersStateDto.setCengStateList(layerInfo);
        return layersStateDto;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAbnormalStore(String containerNo) throws Exception {
        /**
         * 上线后修改 删除货位bug 、解锁逻辑 增加删除料箱后结束当前任务
         */
        if (StringUtils.isBlank(containerNo))
            throw new Exception("料箱号不能为空");
        List<SxStore> sxStores = storeMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), SxStore.class);
        if (sxStores.size() == 0){
            return;
        }
        if (ztStoreMapper.findByMap(MapUtils.put("containerNo",containerNo).getMap(),ZtStore.class).size() > 0){
            throw new Exception("在途库存无法删除");
        }
        logger.info("++++++++++++++++++++++正在删除"+containerNo+"库存及任务++++++++++++++++++++++++");
        //删料箱货位
        storeMapper.deleteByMap(MapUtils.put("containerNo",containerNo).getMap(),SxStore.class);
        List<WCSTask> wcsTasks = iwcsTaskService.getTaskByContainer(containerNo);
        if (wcsTasks.size() > 0){
            //结束任务
            iwcsTaskService.finishTask(wcsTasks.get(0).getId(),true);
            //wcs删除任务

            iwcsService.deleteAbnormalContainerNo(wcsTasks.get(0).getId(),containerNo);

        }
        List<WCSCommand> wcsCommands = wcsCommandMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), WCSCommand.class);

        if (wcsCommands.size() > 0){
            wcsCommandMapper.deleteByMap(MapUtils.put("containerNo",containerNo).getMap(),WCSCommand.class);
        }
        //结束料箱任务

        yqMonitorService.deleteContainerNoTask(sxStores.get(0).getTaskType(),containerNo);
        //取消货位组深位锁
        int storeLocationId = sxStores.get(0).getStoreLocationId();
        SxStoreLocation location = sxStoreLocationMapper.findById(storeLocationId,SxStoreLocation.class);
        int groupId = location.getStoreLocationGroupId();
        sxStoreLocationGroupMapper.updateMapById(groupId,MapUtils.put("ascentLockState",0).getMap(), SxStoreLocationGroup.class);
        //取消四周锁
        storeLocationRelationMapper.unLockLocation(storeLocationId);
        //删除成功，此料箱与商品解除绑定料箱置空
        containerSubService.updateContainerSubByContainer(containerNo);



    }

    @Override
    public List<ZtStore> findZtStoreByContainerNo(String containerNo) throws Exception {
        return ztStoreMapper.findByMap(MapUtils.put("containerNo",containerNo).getMap(),ZtStore.class);
    }

    public boolean getTaskCountByLayer(int layer){
        List<LayerTaskCountDto> taskCountByLayer = storeMapper.getTaskCountByLayer(layer);
        LayerTaskCountDto layerTaskCountDto = taskCountByLayer.get(0);
        if (layerTaskCountDto.getInCount()==0&&layerTaskCountDto.getOutCount()==0){
            return false;
        }
        return true;
    }

    @Override
    public List<StoreTaskDto> findStoreTasks() {
        return storeMapper.findStoreTasks();
    }
}
