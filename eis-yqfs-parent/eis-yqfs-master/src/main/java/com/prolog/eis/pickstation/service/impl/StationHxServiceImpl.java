package com.prolog.eis.pickstation.service.impl;

import com.prolog.eis.dao.assembl.AssemblBoxHzHistoryMapper;
import com.prolog.eis.dao.assembl.AssemblBoxHzMapper;
import com.prolog.eis.dao.assembl.AssemblBoxMxHistoryMapper;
import com.prolog.eis.dao.assembl.AssemblBoxMxMapper;
import com.prolog.eis.dao.masterbase.ContainerMapper;
import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dao.pointlocation.PointLocationMapper;
import com.prolog.eis.dto.hxdispatch.StationLxPositionHxDto;
import com.prolog.eis.model.assembl.AssemblBoxHz;
import com.prolog.eis.model.assembl.AssemblBoxHzHistory;
import com.prolog.eis.model.assembl.AssemblBoxMx;
import com.prolog.eis.model.assembl.AssemblBoxMxHistory;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.point.PointLocation;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.ZtStore;
import com.prolog.eis.pickstation.client.UpdateClientStationInfoService;
import com.prolog.eis.pickstation.dao.StationLxMapper;
import com.prolog.eis.pickstation.dao.StationMapper;
import com.prolog.eis.pickstation.dto.*;
import com.prolog.eis.pickstation.eis.EisStationInterfaceService;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.pickstation.service.IStationHxService;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.assembl.IAssemblBoxOperateService;
import com.prolog.eis.service.masterbase.ContainerSubService;
import com.prolog.eis.service.store.IZtSoreService;
import com.prolog.eis.util.LightUtils;
import com.prolog.eis.util.TaskUtils;
import com.prolog.eis.util.TimeUtils;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StationHxServiceImpl implements IStationHxService {
    private final Logger logger = LoggerFactory.getLogger(StationHxServiceImpl.class);
    @Autowired
    private ContainerSubMapper containerSubInfoMapper;
    @Autowired
    private StationLxMapper stationLxMapper;
    @Autowired
    private EisStationInterfaceService eisStationInterfaceService;
    @Autowired
    private AssemblBoxHzMapper assemblBoxHzMapper;
    @Autowired
    private PointLocationMapper pointLocationMapper;
    @Autowired
    private IWCSService iwcsService;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private IWCSCommandService commandService;
    @Autowired
    private AssemblBoxMxMapper assemblBoxMxMapper;
    @Autowired
    private AssemblBoxHzHistoryMapper assemblBoxHzHistoryMapper;
    @Autowired
    private AssemblBoxMxHistoryMapper assemblBoxMxHistoryMapper;
    @Autowired
    private IZtSoreService ztSoreService;
    @Autowired
    private IWCSTaskService taskService;
    @Autowired
    private ContainerSubService containerSubInfoService;
    @Autowired
    private ContainerMapper containerMapper;
    @Autowired
    private IStationService stationService;
    @Autowired
    private UpdateClientStationInfoService updateClientStationInfoService;
    @Autowired
    private IAssemblBoxOperateService assemblBoxOperateService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateHx(String sourceContainerSubNo, String targetContainerSubNo, int commodityNum) throws Exception {
        if (StringUtils.isBlank(sourceContainerSubNo)) {
            throw new Exception("合箱原子容器为空");
        }
        if (StringUtils.isBlank(targetContainerSubNo)) {
            throw new Exception("合箱目标子容器为空");
        }
        if (commodityNum == 0) {
            throw new Exception("合箱需转移商品数量异常");
        }
        //原子容器信息
        ContainerSub sourceContainerSubInfo = containerSubInfoMapper.findById(sourceContainerSubNo, ContainerSub.class);
        //目标容器信息
        ContainerSub targetContainerSubInfo = containerSubInfoMapper.findById(targetContainerSubNo, ContainerSub.class);
        //合箱前有效期判断
       this.hxContainerSubCheck(sourceContainerSubInfo,targetContainerSubInfo);
        //原子容器商品数量
        int sourceCommodityNum = sourceContainerSubInfo.getCommodityNum();

        if (commodityNum > sourceCommodityNum) {
            throw new Exception("原子容器库存数量不足");
        }else if ((commodityNum + targetContainerSubInfo.getCommodityNum()) > sourceContainerSubInfo.getReferenceNum() ){
            throw new Exception("合箱数量超过货格上限无法合箱");
        } else if (commodityNum == sourceCommodityNum) {
            //原子容器商品全部转移，货格为空
            ContainerSub containerSubInfo = new ContainerSub();
            containerSubInfo.setContainerSubNo(sourceContainerSubInfo.getContainerSubNo());
            containerSubInfo.setContainerNo(sourceContainerSubInfo.getContainerNo());
            containerSubInfoMapper.update(containerSubInfo);
        } else {
            //跟新子容器信息
            containerSubInfoMapper.updateMapById(sourceContainerSubNo, MapUtils.put("commodityNum", sourceContainerSubInfo.getCommodityNum() - commodityNum).getMap(),
                    ContainerSub.class);
        }
        //更新目标容器数量
        //合箱货物入库时间按最先入库时间
        Date minTime = TimeUtils.getMinTime(sourceContainerSubInfo.getGmtCreateTime(), targetContainerSubInfo.getGmtCreateTime());
        if (minTime != null) {
            containerSubInfoMapper.updateMapById(targetContainerSubNo, MapUtils.put("commodityNum", targetContainerSubInfo.getCommodityNum() + commodityNum)
                            .put("gmtCreateTime", minTime).getMap(),
                    ContainerSub.class);
        }
        containerSubInfoMapper.updateMapById(targetContainerSubNo, MapUtils.put("commodityNum", targetContainerSubInfo.getCommodityNum() + commodityNum).getMap(),
                ContainerSub.class);

        //灭灯
        try {
            commandService.sendLightCommand(TaskUtils.gerenateTaskId(),this.getStationNoByContainerNo(sourceContainerSubInfo.getContainerNo()).get(0),null);
        } catch (Exception e) {
            logger.info("合箱灭灯失败", e);
        }
        //保存记录

    }

    @Override
    public StationInfoDto initHxDate(int stationId) throws Exception {
        Station station = stationMapper.findByMap(MapUtils.put("id", stationId).getMap(), Station.class).get(0);
        if (station.getStationTaskType() != SxStore.TASKTYPE_HX) {
            throw new Exception("当前拣选站任务类型不是合箱作业");
        }
        StationInfoDto hxInfo = this.findHxInfo(stationId);
        return hxInfo;
    }

    /**
     * 料箱放行
     * @param containerNo 容器编号
     * @param stationId 站台id
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lxLeave(String containerNo, int stationId) throws Exception {
        if (StringUtils.isBlank(containerNo))
            throw new Exception("拣选站无可离开料箱");
        List<StationLxPosition> stationLxPositions = stationLxMapper.findByMap(MapUtils.put("stationId", stationId).put("containerNo", containerNo).put("status",StationLxPosition.STATUS_ARRIVE).getMap(), StationLxPosition.class);
        if (stationLxPositions.size() == 0) {
            throw new Exception("拣选站料箱【"+containerNo+"】已离开");
        }
        StationLxPosition stationLxPosition = stationLxPositions.get(0);
        List<PointLocation> pointLocations = pointLocationMapper.findByMap(MapUtils.put("pointType", PointLocation.TYPE_EXCEPTION_CONTAINER).put("stationId", 0).getMap(), PointLocation.class);
        if (pointLocations.size() < 1) {
            throw new Exception("找不到入库点位");
        }
        //输送线行走(料箱放行)

        String taskId = TaskUtils.gerenateTaskId();
        WCSTask task = new WCSTask();
        task.setId(taskId);
        task.setStationId(stationId);
        task.setEisType(SxStore.TASKTYPE_HX);
        task.setWcsType(Constant.TASK_TYPE_XZ);
        task.setStatus(WCSTask.STATUS_WAITTING);
        task.setContainerNo(containerNo);
        task.setAddress(stationLxPosition.getPositionDeviceNo());
        task.setTarget(pointLocations.get(0).getPointId());
        task.setGmtCreateTime(new Date());
        taskService.add(task);
        commandService.sendXZCommand(taskId,task.getAddress(),task.getTarget(),containerNo);
        //修改合箱明细料箱状态
        Criteria criteria = Criteria.forClass(AssemblBoxMx.class);
        criteria.setRestriction(Restrictions.eq("containerNo", containerNo));
        assemblBoxMxMapper.updateMapByCriteria(MapUtils.put("taskState", AssemblBoxMx.TASK_STATE_YWC).getMap(), criteria);
        //修改拣选站箱位状态(离开中)
        stationLxPosition.setStatus(StationLxPosition.STATUS_LEAVE);
        stationLxMapper.update(stationLxPosition);
        //修改在途状态离开站台
        List<ZtStore> ztStores = ztSoreService.findByMap(MapUtils.put("containerNo", containerNo).getMap());
        ztStores.get(0).setStatus(ZtStore.Status_Leave_Station);
        ztSoreService.update(ztStores.get(0));

        //料箱为当前合箱计划最后一个料箱时，明细汇总转历史
//        List<AssemblBoxHz> assemblBoxHzs = assemblBoxHzMapper.findByMap(MapUtils.put("stationId", stationId).put("taskState", AssemblBoxHz.TASK_STATE_EXECUTE).getMap(), AssemblBoxHz.class);
        int assemblBoxHzId = assemblBoxMxMapper.getAssemblBoxMxDetail(containerNo);
//        Integer assemblBoxHzId = assemblBoxHzs.get(0).getId();
        List<AssemblBoxMx> assemblBoxMxes = assemblBoxMxMapper.findByMap(MapUtils.put("assemblBoxHzId", assemblBoxHzId).put("taskState", AssemblBoxMx.TASK_STATE_JXZ).getMap(), AssemblBoxMx.class);

        if (assemblBoxMxes.size() == 0) {
            //合箱计划完成
            if (assemblBoxMxMapper.findByMap(MapUtils.put("assemblBoxHzId", assemblBoxHzId).put("taskState", AssemblBoxMx.TASK_STATE_ERROR).getMap(), AssemblBoxMx.class).size() > 0){
                //合箱明细存在异常料箱，合箱汇总状态改为异常
                assemblBoxHzMapper.updateMapById(assemblBoxHzId, MapUtils.put("taskState", AssemblBoxHz.TASK_STATE_ERROR).getMap(), AssemblBoxHz.class);
            }else {
                assemblBoxHzMapper.updateMapById(assemblBoxHzId, MapUtils.put("taskState", AssemblBoxHz.TASK_STATE_FINISH).getMap(), AssemblBoxHz.class);
            }

            //明细转历史
            this.hxToHistory(assemblBoxHzId);
        }


    }

    @Override
    public void hxToHistory(int id) throws Exception {

        //查汇总
        AssemblBoxHz assemblBoxHz = assemblBoxHzMapper.findById(id, AssemblBoxHz.class);
        //查明细
        List<AssemblBoxMx> assemblBoxMxes = assemblBoxMxMapper.findByMap(MapUtils.put("assemblBoxHzId", id).getMap(), AssemblBoxMx.class);

//        汇总转历史
        AssemblBoxHzHistory assemblBoxHzHistory = new AssemblBoxHzHistory();
        BeanUtils.copyProperties(assemblBoxHz, assemblBoxHzHistory);
        assemblBoxHzHistoryMapper.save(assemblBoxHzHistory);
//        明细转历史
        List<AssemblBoxMxHistory> list = new ArrayList<>();
        for (AssemblBoxMx assemblBoxMx : assemblBoxMxes) {

            AssemblBoxMxHistory assemblBoxMxHistory = new AssemblBoxMxHistory();
            BeanUtils.copyProperties(assemblBoxMx, assemblBoxMxHistory);
            list.add(assemblBoxMxHistory);
        }
        assemblBoxMxHistoryMapper.saveBatch(list);
        //删明细，汇总
        assemblBoxMxMapper.deleteByMap(MapUtils.put("assemblBoxHzId", id).getMap(), AssemblBoxMx.class);
        assemblBoxHzMapper.deleteById(id, AssemblBoxHz.class);

    }

    /**
     * 得到拣选站台料箱的灯光编号
     *
     * @param stationLxPositions 站台箱位料箱
     * @param goodsId            商品id
     * @return
     * @throws Exception
     */
    @Override
    public String[] getStationLxLightsNo(List<StationLxPosition> stationLxPositions, int goodsId) throws Exception {
        String[] lights = {};
        for (StationLxPosition stationLxPosition : stationLxPositions) {
            String containerNo = stationLxPosition.getContainerNo();
            List<StationLxPositionHxDto> stationLxPositionHxDtos = containerSubInfoService.getHxContainerInfo(goodsId, containerNo);
            for (StationLxPositionHxDto dto : stationLxPositionHxDtos) {
                String[] light = LightUtils.getLightPosition(containerNo, dto.getContainerSubNo(), dto.getLayoutType(), dto.getContainerDirection(), Integer.parseInt(dto.getPositionNo()));
                String[] strings = Arrays.copyOf(lights, lights.length + light.length);
                System.arraycopy(light, 0, strings, lights.length, light.length);
                lights = strings;
            }
        }
        return lights;
    }

    /**
     * 合箱操作前校验
     * @param sourceContainerSub 原子容器货格
     * @param targetContainerSub 目标子容器货格
     * @throws Exception
     */
    @Override
    public void hxContainerSubCheck(ContainerSub sourceContainerSub, ContainerSub targetContainerSub) throws Exception {
        Date sourceDate = sourceContainerSub.getExpiryDate();
        Date targetDate = targetContainerSub.getExpiryDate();
        int sourceCommodityId = sourceContainerSub.getCommodityId();
        int sourceCommodityId1 = targetContainerSub.getCommodityId();
        if (sourceCommodityId != sourceCommodityId1) {
            throw new Exception("原子容器与目标子容器商品不一致");
        }

        //有效期判断
        boolean b = TimeUtils.compareTime(sourceDate, targetDate);
        if (!b) {
            throw new Exception("合箱的两个货格【" + sourceContainerSub.getContainerNo() + "】与【" + targetContainerSub.getContainerNo() + "】商品有效期不一致");
        }
    }

    /**
     * 亮灯
     * @param sourceContainerSubNo 原子容器货格
     * @param targetContainerSubNo 目标子容器货格
     * @throws Exception
     */
    @Override
    public void hxLight(String sourceContainerSubNo, String targetContainerSubNo) throws Exception {
        if (StringUtils.isBlank(sourceContainerSubNo) || StringUtils.isBlank(targetContainerSubNo)) {
            throw new Exception("参数传递错误，需要两个货格编号");
        }
        if (sourceContainerSubNo.equals(targetContainerSubNo))
            throw new Exception("源货格号和目标货格号不能一致");
        //原子容器信息
        ContainerSub sourceContainerSubInfo = containerSubInfoMapper.findById(sourceContainerSubNo, ContainerSub.class);
        //目标容器信息
        ContainerSub targetContainerSubInfo = containerSubInfoMapper.findById(targetContainerSubNo, ContainerSub.class);
        //合箱前校验规格
        this.hxContainerSubCheck(sourceContainerSubInfo,targetContainerSubInfo);
        StationLxPositionHxDto sourceContainer = stationLxMapper.getHxContainerInfo(sourceContainerSubInfo.getContainerNo()).get(0);
        StationLxPositionHxDto targetContainer = stationLxMapper.getHxContainerInfo(targetContainerSubInfo.getContainerNo()).get(0);
        //获取两个合箱货格的编号

        String[] light1 = LightUtils.getLightPosition(sourceContainerSubInfo.getContainerNo(), sourceContainerSubNo, sourceContainer.getLayoutType(),
                sourceContainer.getContainerDirection(), Integer.parseInt(sourceContainer.getPositionNo()));
        String[] light2 = LightUtils.getLightPosition(targetContainerSubInfo.getContainerNo(), targetContainerSubNo, targetContainer.getLayoutType(),
                targetContainer.getContainerDirection(), Integer.parseInt(targetContainer.getPositionNo()));
        String[] strings = Arrays.copyOf(light1, light1.length + light2.length);
        System.arraycopy(light2,0,strings,light1.length,light2.length);
        Station station = stationMapper.findById(sourceContainer.getStationId(), Station.class);
        //亮灯接口
        try {
            commandService.sendLightCommand(TaskUtils.gerenateTaskId(),station.getStationNo(), String.join(",", strings));
        } catch (Exception e) {
            logger.info("合箱亮灯失败", e);
        }

    }

    @Override
    public StationInfoDto findHxInfo(int stationId) throws Exception {
        List<StationLxPosition> lxPositionList = stationLxMapper.findByMap(MapUtils.put("stationId", stationId).getMap(), StationLxPosition.class);

        lxPositionList.stream().sorted(Comparator.comparing(StationLxPosition::getPositionNo));
        List<StationInfoLxPositionDto> lxPList = new ArrayList<>(2);
        for (int i = 0; i < lxPositionList.size(); i++) {

            StationInfoLxPositionDto stationInfoLxPositionDto = new StationInfoLxPositionDto();
            lxPList.add(stationInfoLxPositionDto);
        }

        List<StationLxPosition> stationLxPositions = lxPositionList.stream().filter(x -> x.getContainerNo() != null && !("".equals(x.getContainerNo()))).collect(Collectors.toList());
        for (StationLxPosition stationLxPosition : stationLxPositions) {
            String containerNo = stationLxPosition.getContainerNo();
            if (stationLxMapper.findByMap(MapUtils.put("containerNo",containerNo).put("status",StationLxPosition.STATUS_ARRIVE).getMap(),StationLxPosition.class).size() == 0){
                continue;
            }
            List<StationLxPositionHxDto> stationLxPositionHxDto = stationLxMapper.getHxContainerInfo(stationLxPosition.getContainerNo());

            //料箱信息
            StationInfoLxDto stationInfoHxDto = this.getHxContainerInfo(stationLxPositionHxDto.get(0),containerNo);

            for (int i = 0; i < lxPositionList.size(); i++) {
                if (lxPositionList.get(i).getPositionNo().equals(stationInfoHxDto.getPositionNo())){
                    lxPList.get(i).setLiaoXiang(stationInfoHxDto);
                }
            }
        }

        StationInfoDto stationInfoDto = new StationInfoDto();
        stationInfoDto.setStationId(stationId);
        stationInfoDto.setLxPositionList(lxPList);
        return stationInfoDto;

        }






    @Override
    public StationInfoLxDto getHxContainerInfo(StationLxPositionHxDto stationLxPositionHxDto, String containerNo) throws Exception {
        //料箱信息
        StationInfoLxDto stationInfoHxDto = new StationInfoLxDto();
        stationInfoHxDto.setPositionNo(stationLxPositionHxDto.getPositionNo());
        stationInfoHxDto.setContainerDirection(stationLxPositionHxDto.getContainerDirection());
        stationInfoHxDto.setContainerNo(containerNo);
        stationInfoHxDto.setLayoutType(stationLxPositionHxDto.getLayoutType());
        //查询当前执行合箱计划的商品goodsId

        List<Integer> goodsIds = assemblBoxHzMapper.getGoodsId(containerNo);
        Integer goodsId = goodsIds.get(0);
        //筛选出需要合箱的货格号
        List<String> containerSubNos = containerSubInfoService.findContainerSubNo(goodsId, containerNo);
        List<HxContainerInfoDto> containerSubs = containerSubInfoService.findHxContainerSub(containerNo);
        List<StationInfoSubLxDto> stationHx = new ArrayList<>();
        //封装货格信息
        for (HxContainerInfoDto containerSub : containerSubs) {
            StationInfoSubLxDto stationInfoHxSubDto = new StationInfoSubLxDto();
            stationInfoHxSubDto.setBarCode(containerSub.getGoodsBarCode());
            stationInfoHxSubDto.setSeedCount(containerSub.getCommodityNum());
            stationInfoHxSubDto.setGoodsName(containerSub.getGoodsName());
            stationInfoHxSubDto.setSubContainerNo(containerSub.getContainerSubNo());
            //筛选需要合箱的子容器，1表示合箱
            for (String containerSubNo : containerSubNos) {
                if (containerSubNo.equals(containerSub.getContainerSubNo())) {
                    stationInfoHxSubDto.setIsIntegrationLx(1);
                }
            }
            stationHx.add(stationInfoHxSubDto);
        }
        stationInfoHxDto.setSubLxList(stationHx);
        return stationInfoHxDto;
    }

    @Override
    public List<String> getStationNoByContainerNo(String containerNo) throws Exception {
        return stationMapper.getStationNo(containerNo);
    }

    @Override
    public void sendHxToClient(int stationId) throws Exception {
        StationInfoDto stationInfoDto = this.findHxInfo(stationId);
        String url = "/initHx";
        updateClientStationInfoService.sendHotpotClient(stationInfoDto,stationId,url);
    }


}
