package com.prolog.eis.service.pd.impl;

import com.prolog.eis.dao.masterbase.ContainerMapper;
import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dao.masterbase.GoodsBarCodeMapper;
import com.prolog.eis.dao.pd.PdTaskDetailHisMapper;
import com.prolog.eis.dao.pd.PdTaskDetailMapper;
import com.prolog.eis.dao.pd.PdTaskMapper;
import com.prolog.eis.dao.store.ZtStoreMapper;
import com.prolog.eis.dto.pd.PdDwContainerDto;
import com.prolog.eis.dto.pd.PdDwContainerSubDto;
import com.prolog.eis.dto.pd.PdRecord;
import com.prolog.eis.model.masterbase.Container;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.masterbase.GoodsBarCode;
import com.prolog.eis.model.pd.PdTask;
import com.prolog.eis.model.pd.PdTaskDetail;
import com.prolog.eis.model.pd.PdTaskDetailHis;
import com.prolog.eis.model.point.PointLocation;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.ZtStore;
import com.prolog.eis.pickstation.dao.StationLxMapper;
import com.prolog.eis.pickstation.dao.StationMapper;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.service.pd.PdExecuteService;
import com.prolog.eis.service.pd.PdTaskService;
import com.prolog.eis.service.pd.SendPdLxService;
import com.prolog.eis.service.pointlocation.IPointLocationService;
import com.prolog.eis.util.Assert;
import com.prolog.eis.util.LightUtils;
import com.prolog.eis.util.PrologDateUtils;
import com.prolog.eis.util.TaskUtils;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Order;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/6/13 9:27
 */

@Service
public class PdExecuteServiceImpl implements PdExecuteService {

    private final Logger logger = LoggerFactory.getLogger(PdExecuteServiceImpl.class);

    @Autowired
    private IWCSTaskService taskService;

    @Autowired
    private IWCSCommandService commandService;

    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private ContainerMapper containerMapper;

    @Autowired
    private ContainerSubMapper containerSubMapper;

    @Autowired
    private GoodsBarCodeMapper goodsBarCodeMapper;

    @Autowired
    private SendPdLxService sendPdLxService;

    @Autowired
    private ZtStoreMapper ztStoreMapper;

    @Autowired
    private PdTaskDetailMapper pdTaskDetailMapper;

    @Autowired
    private PdTaskMapper pdTaskMapper;

    @Autowired
    private StationLxMapper stationLxMapper;

    //@Autowired
    //private PointLocationMapper pointLocationMapper;

    @Autowired
    private PdTaskDetailHisMapper pdTaskDetailHisMapper;
    @Autowired
    private PdTaskService pdTaskService;

    @Autowired
    private IPointLocationService locationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void PdDw(String containerNo, int stationId, String locationNo) throws Exception {
        //检查站台信息
        Station station = stationMapper.findById(stationId, Station.class);
        Assert.notNull(station, "站台[%s]数据异常", stationId);
        //修改盘点任务状态为盘点中
        pdTaskService.updateStatusByContainerNo(containerNo, PdTask.STATE_PROCESSING);

        List<StationLxPosition> stationLxPositions = stationLxMapper.findByMap(MapUtils.put("stationId", stationId).put("status", 20).getMap(), StationLxPosition.class);

        for (StationLxPosition stationLxPosition : stationLxPositions) {
            if (!containerNo.equals(stationLxPosition.getContainerNo())) {
                String containerNo2 = stationLxPosition.getContainerNo();
                if (pdTaskDetailMapper.pdCompleteContainerSubCount(containerNo2).size() > 0) {
                    PdDwContainerDto dwContainerDto = getPdDwContainerDto(containerNo);
                    sendPdLxService.sendPdLxTask(stationId, dwContainerDto);
                    return;
                }

            }
        }


        List<String> containerSubNos = pdTaskDetailMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), PdTaskDetail.class).stream().map(PdTaskDetail::getContainerSubNo).sorted().collect(Collectors.toList());
        for (StationLxPosition stationLxPosition : stationLxPositions) {
            if (containerNo.equals(stationLxPosition.getContainerNo())) {
                String containerSubNo;
                if (stationLxPosition.getContainerDirection().equals("A")) {
                    containerSubNo = Collections.max(containerSubNos);
                } else {
                    containerSubNo = Collections.min(containerSubNos);
                }

                PdDwContainerDto dwContainerDto = getPdDwContainerDto(containerNo);

                dwContainerDto.setContainerSubNo(containerSubNo);
                sendPdLxService.sendPdLxTask(stationId, dwContainerDto);
                ZtStore ztStore = ztStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), ZtStore.class).get(0);
                ztStore.setStatus(ZtStore.Status_Operation);
                ztStoreMapper.update(ztStore);
                this.light(containerSubNo, stationLxPosition);
                return;
            }
        }
    }


    //亮灯逻辑
    public void light(String containerSubNo, StationLxPosition stationLxPosition) {
        Station station = stationMapper.findById(stationLxPosition.getStationId(), Station.class);
        String[] lightPosition;
        if (StringUtils.isBlank(containerSubNo)) {
            lightPosition = new String[]{};
            try {
                String string ="";
                commandService.sendLightCommand(TaskUtils.gerenateTaskId(), station.getStationNo(), null);
            } catch (Exception e) {
                logger.info("发送亮灯指令异常", e);
            }
        } else {
            ContainerSub containerSub = containerSubMapper.findByMap(MapUtils.put("containerSubNo", containerSubNo).getMap(), ContainerSub.class).get(0);
            String containerNum = containerSub.getContainerNo();
            Container container = containerMapper.findByMap(MapUtils.put("containerNo", containerNum).getMap(), Container.class).get(0);
            lightPosition = LightUtils.getLightPosition(containerNum, containerSubNo, container.getLayoutType(), stationLxPosition.getContainerDirection(), Integer.parseInt(stationLxPosition.getPositionNo()));

            try {
//            WCSCommand cmd = new WCSCommand();
//            cmd.setTaskId(TaskUtils.gerenateTaskId());
//            cmd.setLights(String.join(",",lightPosition));
//            cmd.setStationNo(station.getStationNo());
//            cmd.setType(Constant.COMMAND_TYPE_LIGHT);
//            commandService.add(cmd);

                commandService.sendLightCommand(TaskUtils.gerenateTaskId(), station.getStationNo(), String.join(",", lightPosition));
            } catch (Exception e) {
                logger.info("发送亮灯指令异常", e);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String pdComplete(String containerNo, String containerSubNo, int storeCount) throws Exception {
        ContainerSub containerSub = containerSubMapper.findById(containerSubNo, ContainerSub.class);
        int referenceNum = containerSub.getReferenceNum();
        if (storeCount > referenceNum) {
            throw new Exception("数量超过上限" + referenceNum);
        }
        String light = "";
        //校验在途库存
        List<ZtStore> ztStores = ztStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), ZtStore.class);
        if (ztStores.size() == 0) {
            throw new Exception("料箱【" + containerNo + "】没有在途库存");
        }
        ZtStore ztStore = ztStores.get(0);
        //盘点次数校验...

        //获取盘点任务Id
        //Integer pdTaskId = pdTaskDetailMapper.getPdTaskId(containerSubNo);

        //修改盘点任务明细的数量
        pdTaskDetailMapper.updateModifyCount(containerSubNo, storeCount);

        //修改子容器真实数量
        containerSubMapper.updateContainerSubCommodityNum(storeCount, containerSubNo);

        //详情转历史
        List<PdTaskDetail> containerSubNo1 = pdTaskDetailMapper.findByMap(MapUtils.put("containerSubNo", containerSubNo).getMap(), PdTaskDetail.class);
        if (containerSubNo1.size()==0){
            throw new Exception("已盘点完成");
        }
        PdTaskDetail pdTaskDetail = containerSubNo1.get(0);


        int pdTaskId = pdTaskDetail.getPdTaskId();

        PdTaskDetailHis pdTaskDetailHis = new PdTaskDetailHis();
        pdTaskDetailHis.setId(pdTaskDetail.getId());
        pdTaskDetailHis.setContainerNo(pdTaskDetail.getContainerNo());
        pdTaskDetailHis.setContainerSubNo(pdTaskDetail.getContainerSubNo());
        pdTaskDetailHis.setBarCode(pdTaskDetail.getBarCode());
        pdTaskDetailHis.setGoodsNo(pdTaskDetail.getGoodsNo());
        pdTaskDetailHis.setGoodsName(pdTaskDetail.getGoodsName());
        pdTaskDetailHis.setModifyCount(pdTaskDetail.getModifyCount());
        pdTaskDetailHis.setOriginalCount(pdTaskDetail.getOriginalCount());
        pdTaskDetailHis.setCreateTime(new Date());

        pdTaskDetailHisMapper.save(pdTaskDetailHis);

        pdTaskDetailMapper.deleteById(pdTaskDetail.getId(), PdTaskDetail.class);

        long completeContainerSubCount = pdTaskDetailMapper.pdCompleteContainerSubCount(containerNo).size();

        //当前容器的子容器全部盘点完
        if (completeContainerSubCount == 0) {
            StationLxPosition stationLxPosition = new StationLxPosition();
            stationLxPosition.setStationId(ztStore.getStationId());
            //亮灯待开启
            this.light(null, stationLxPosition);
        }

        //盘点任务明细已都完成
        if (pdTaskDetailMapper.pdCompleteTaskCount(pdTaskId) == 0) {
            //盘点完成,修改盘点计划状态
            pdTaskMapper.updateMapById(pdTaskId, MapUtils.put("pdState", 40)
                    .put("endTime", PrologDateUtils.parseObject(new Date())).getMap(), PdTask.class);
            //pdTask转历史

        }


        int stationId = ztStore.getStationId();
        Criteria ctr = Criteria.forClass(StationLxPosition.class);
        ctr.setRestriction(Restrictions.and(Restrictions.eq("stationId", stationId), Restrictions.isNotNull("gmtInplaceTime")));
        List<StationLxPosition> stationLxPositions = stationLxMapper.findByCriteria(ctr);
        if (stationLxPositions.size() == 0) {
            return "";
        }
        stationLxPositions.sort(Comparator.comparing(StationLxPosition::getGmtInplaceTime));

        for (StationLxPosition stationLxPosition : stationLxPositions) {
            //容器号
            String containerNo2 = stationLxPosition.getContainerNo();
            if (StringUtils.isEmpty(containerNo2)) {
                continue;
            }
            //当前容器需要盘点的数据
            List<String> pdTaskDetails = pdTaskDetailMapper.pdCompleteContainerSubCount(containerNo2).stream().map(PdTaskDetail::getContainerSubNo).sorted().collect(Collectors.toList());
            if (pdTaskDetails.size() > 0) {
                if (stationLxPosition.getContainerDirection().equals("C")) {
                    light = pdTaskDetails.get(0);
                } else {
                    light = Collections.max(pdTaskDetails);
                }
                //亮灯
                this.light(light, stationLxPosition);
                return light;
            }
        }
        return "";
    }

    @Override
    public List<PdDwContainerDto> init(int stationId) throws Exception {
        String light = "";
        Station station = stationMapper.findById(stationId, Station.class);
        Assert.notNull(station, "站台[%s]数据异常", stationId);
        List<PdDwContainerDto> list = new ArrayList<>();

        Criteria ctr = Criteria.forClass(StationLxPosition.class);
        ctr.setRestriction(Restrictions.and(Restrictions.eq("stationId", stationId), Restrictions.isNotNull("gmtInplaceTime")));
        List<StationLxPosition> stationLxPositions = stationLxMapper.findByCriteria(ctr);

        if (stationLxPositions.size() == 0) {
            return list;
        }

        stationLxPositions.sort(Comparator.comparing(StationLxPosition::getGmtInplaceTime));

        boolean is = true;
        StationLxPosition sp = null;
        for (StationLxPosition stationLxPosition : stationLxPositions) {
            //容器号
            String containerNo = stationLxPosition.getContainerNo();
            if (StringUtils.isEmpty(containerNo)) {
                continue;
            }
            //当前容器需要盘点的数据
            List<String> pdTaskDetails = pdTaskDetailMapper.pdCompleteContainerSubCount(containerNo).stream().map(PdTaskDetail::getContainerSubNo).sorted().collect(Collectors.toList());
            PdDwContainerDto pdDwContainerDto = getPdDwContainerDto(containerNo);

            if (pdTaskDetails.size() != 0) {
                if (is) {
                    String face = stationLxPosition.getContainerDirection();
                    String containerSubNo;
                    if (face.equals("A")) {
                        //需要亮灯的子容器号
                        containerSubNo = Collections.max(pdTaskDetails);
                    } else {
                        containerSubNo = pdTaskDetails.get(0);
                    }
                    pdDwContainerDto.setContainerSubNo(containerSubNo);
                    light = containerSubNo;
                    sp = stationLxPosition;
                    is = false;
                }

            }
            list.add(pdDwContainerDto);
        }

        if (!StringUtils.isEmpty(light))
            this.light(light, sp);

        return list;
    }


    //离开站台
    @Override
    public void lxLeave(String containerNo, int stationId) throws Exception {
        Date date1 = new Date();

        int count = pdTaskDetailMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), PdTaskDetail.class).size();
        if (count > 0) {
            throw new Exception("当前容器" + containerNo + "未盘点");
        }

        if (StringUtils.isEmpty(containerNo)) {
            throw new Exception("当前位置无料箱可离开");
        }
        StationLxPosition stationLxPosition = stationLxMapper.findByMap(MapUtils.put("containerNo", containerNo).put("stationId", stationId).getMap(), StationLxPosition.class).get(0);
        List<PointLocation> pointLocations = locationService.getPointByStation(0, PointLocation.TYPE_EXCEPTION_CONTAINER);
        if (pointLocations.size() < 1)
            throw new Exception("找不到点位");


        String taskId = TaskUtils.gerenateTaskId();
        WCSTask newTask = new WCSTask();
        newTask.setId(taskId);
        newTask.setStationId(stationId);
        newTask.setEisType(SxStore.TASKTYPE_PD);
        newTask.setWcsType(Constant.TASK_TYPE_XZ);
        newTask.setStatus(WCSTask.STATUS_WAITTING);
        newTask.setContainerNo(containerNo);
        newTask.setAddress(stationLxPosition.getPositionDeviceNo());
        newTask.setTarget(pointLocations.get(0).getPointId());
        newTask.setGmtCreateTime(new Date());
        taskService.add(newTask);
        Date date2 = new Date();
        System.out.println("##############################" + (date1.getTime() - date2.getTime()));
        commandService.sendXZCommand(newTask.getId(), newTask.getAddress(), newTask.getTarget(), newTask.getContainerNo());


    }


    //封装料箱的基础信息
    public PdDwContainerDto getPdDwContainerDto(String containerNo) {
        PdDwContainerDto dwContainerDto = new PdDwContainerDto();
        Container containerInfo = containerMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), Container.class).get(0);
        dwContainerDto.setContainerNo(containerInfo.getContainerNo());
        dwContainerDto.setLayoutType(containerInfo.getLayoutType());
        List<String> containerSubNos = containerSubMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), ContainerSub.class).stream().map(ContainerSub::getContainerSubNo).collect(Collectors.toList());
        dwContainerDto.setContainerSubNos(containerSubNos);
        StationLxPosition stationLxPosition = stationLxMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), StationLxPosition.class).get(0);
        dwContainerDto.setSide(stationLxPosition.getContainerDirection());
        dwContainerDto.setPositionNo(stationLxPosition.getPositionNo());

        List<PdTaskDetail> pdTaskDetails = pdTaskDetailMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), PdTaskDetail.class).stream().filter(x -> x.getTaskState() != SxStore.TASKTYPE_PD).collect(Collectors.toList());
        List<PdDwContainerSubDto> containerSubDto = new ArrayList<>();
        for (PdTaskDetail pdTaskDetail : pdTaskDetails) {
            PdDwContainerSubDto pdDwContainerSubDto = containerSubMapper.findPdDwContainerSubDto(pdTaskDetail.getContainerSubNo());

            List<String> goodsBarCode = goodsBarCodeMapper.findByMap(MapUtils.put("goodsId", pdDwContainerSubDto.getGoodsId()).getMap(), GoodsBarCode.class).stream().map(GoodsBarCode::getBarCode).collect(Collectors.toList());
            pdDwContainerSubDto.setBarCode(goodsBarCode);
            containerSubDto.add(pdDwContainerSubDto);
        }
        dwContainerDto.setContainerSubDto(containerSubDto);

        return dwContainerDto;
    }


    //记录盘点过程
    public List<PdRecord> selectPdHis(String goodsNo, String containerSubNo) {
        // 商品号goodNo -- 库中真实数量 --  被修改后的真实数量 -- 误差值 -- 时间
        Criteria criteria = Criteria.forClass(PdTaskDetailHis.class);
        criteria.setRestriction(Restrictions.and(
                Restrictions.eq("goodsNo", goodsNo),
                Restrictions.eq("containerSubNo", containerSubNo)
        ));
        criteria.setOrder(Order.newInstance().desc("createTime"));
        List<PdTaskDetailHis> pdTaskDetailHis = pdTaskDetailHisMapper.findByCriteria(criteria);
        return pdTaskDetailHis.stream().map(x -> {
            PdRecord pdRecord = new PdRecord();
            pdRecord.setGoodsNo(x.getGoodsNo());
            pdRecord.setGoodsName(x.getGoodsName());
            pdRecord.setModifyCount(x.getModifyCount());
            pdRecord.setOriginalCount(x.getOriginalCount());
            pdRecord.setDifCount(x.getModifyCount() - x.getModifyCount());
            pdRecord.setPdType(x.getPdType());
            pdRecord.setUpdateDate(x.getCreateTime());
            return pdRecord;
        }).collect(Collectors.toList());
    }


}
