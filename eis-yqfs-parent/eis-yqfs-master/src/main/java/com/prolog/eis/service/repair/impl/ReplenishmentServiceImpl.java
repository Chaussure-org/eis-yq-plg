package com.prolog.eis.service.repair.impl;

import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dao.masterbase.GoodsMapper;
import com.prolog.eis.dao.repair.*;
import com.prolog.eis.dao.yqfs.RepairStationMapper;
import com.prolog.eis.dto.repair.RepairStoreDto;
import com.prolog.eis.dto.yqfs.ContainerSubDto;
import com.prolog.eis.dto.yqfs.ReplenishmentConfirmDto;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.masterbase.Goods;
import com.prolog.eis.model.repair.*;
import com.prolog.eis.model.yqfs.RepairStation;
import com.prolog.eis.service.repair.InboundRepairInfoService;
import com.prolog.eis.service.repair.ReplenishmentService;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author panteng
 * @description: 补货
 * @date 2020/4/22 17:25
 */

@Service
public class ReplenishmentServiceImpl implements ReplenishmentService {

    @Autowired
    private ContainerSubMapper containerSubInfoMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RepairTaskHzMapper repairTaskHzMapper;
    @Autowired
    private RepairTaskMxMapper RepairTaskMxMapper;
    @Autowired
    private RepairStationMapper repairStationMapper;

    @Autowired
    private RepairPlanMapper repairPlanMapper;
    @Autowired
    private RepairPlanMxMapper repairPlanMxMapper;

    @Autowired
    private RepairPlanHisMapper repairPlanHisMapper;
    @Autowired
    private RepairPlanMxHisMapper repairPlanMxHisMapper;
    @Autowired
    private InboundRepairInfoService inboundRepairInfoService;


    /**
     *
     2、根据条码查询入库计划查询接口（只能查询入库未收完计划明细）（前台->java）
     3、货格补货确认接口（校验入库计划是否完成） （前台->java）
     4、整体补货完成确认接口（java自查）
     */

    /**
     * 1、料箱详情推送接口（包含货格存储信息，空货格也算、是否需要补货；）（Java->前台）
     *
     * @param containerNo
     */
    @Override
    public RestMessage<Map<String, Object>> containerArrive(String containerNo) throws Exception {
        RepairStation repairStation = getRepairStation();
        String currWorkContainerNo = repairStation.getCurrWorkContainerNo();
        //当扫描料箱号的时候 判断当前补货站台是否存在补货料箱
        if (StringUtils.isNotEmpty(currWorkContainerNo) && !containerNo.equals(currWorkContainerNo)) {
            return RestMessage.newInstance(false, MessageFormat.format("当前补货站台存在未补货完成的料相：{0}，" +
                    "请先补货完成", currWorkContainerNo), null);
        }
        //查询子容器信息,返回前台
        List<ContainerSubDto> list = containerSubInfoMapper.findSubContainerDetailByContainerNo(containerNo);
        if (CollectionUtils.isEmpty(list)) {
            return RestMessage.newInstance(false, MessageFormat.format("容器号：{0}，未查询到相关的基础信息数据，请检查子容器，商品，商品条码等信息", containerNo), null);
        }
        Map<String, Object> map = new HashMap<>(3);
        ContainerSubDto containerSubDto = list.get(0);
        map.put("containerNo", containerSubDto.getContainerNo());
        map.put("layoutType", containerSubDto.getLayoutType());
        map.put("subContainers", list);

        //设置当前补货站台的料箱
        if (StringUtils.isEmpty(currWorkContainerNo)) {
            repairStation.setCurrWorkContainerNo(containerNo);
            repairStation.setLastUpdateDate(new Date());
            repairStationMapper.update(repairStation);
        }
        return RestMessage.newInstance(true, "查询成功", map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestMessage<String> confirm(ReplenishmentConfirmDto dto) throws Exception {
        //效验
        ContainerSub subInfo = containerSubInfoMapper.findByMap(MapUtils.put("containerSubNo", dto.getContainerSubNo()).getMap(), ContainerSub.class)
                .stream().findFirst().orElse(null);
        if (subInfo == null) {
            return RestMessage.newInstance(false, MessageFormat.format("子容器号：{0},未查询到基础资料", dto.getContainerSubNo()), null);
        }
        if (!subInfo.getContainerNo().equals(dto.getContainerNo())) {
            return RestMessage.newInstance(false, MessageFormat.format("子容器号：{0},与容器编号:{1},不存在对应关系", dto.getContainerSubNo(), dto.getContainerNo()), null);
        }
        //效验商品是否在 商品资料表中
        Goods goods = goodsMapper.findByMap(MapUtils.put("goodsNo", dto.getGoodsNo()).getMap(), Goods.class)
                .stream().findFirst().orElse(null);
        if (goods == null) {
            return RestMessage.newInstance(false, MessageFormat.format("商品编号：{0},未查询到基础资料", dto.getGoodsNo()), null);
        }
        Integer commodityId = subInfo.getCommodityId();
        if (commodityId != null && goods.getId() != commodityId) {
            return RestMessage.newInstance(false, MessageFormat.format("子容器号：{0},补货商品id：{1}，与原有商品id不一致：{2}", dto.getContainerSubNo(), goods.getId(), commodityId), null);
        }

        //补货后数量
        int count = subInfo.getCommodityNum() + dto.getGoodsNum();
        //子容器上线
        int referenceNum = subInfo.getReferenceNum();
        if (count > referenceNum) {
            throw new Exception("数量超过货格上线");
        }

        //添加补货任务 汇总和明细，如果此料箱在补货任务中没有则新建
        RepairTaskHz taskHz = repairTaskHzMapper.findByMap(MapUtils.put("containerNo", goods.getId()).getMap(), RepairTaskHz.class)
                .stream().findFirst().orElse(null);
        if (null == taskHz) {
            taskHz = new RepairTaskHz();
            taskHz.setContainerNo(dto.getContainerNo());
            taskHz.setOperator(dto.getOperator());
            taskHz.setRepairStatus(RepairTaskHz.REPAIRTASK_ING);
            taskHz.setCreateTime(new Date());
            repairTaskHzMapper.save(taskHz);
        }
        //添加补货任务明细
        RepairTaskMx repairTaskMx = new RepairTaskMx();
        repairTaskMx.setRepairTaskHzId(taskHz.getId());
        repairTaskMx.setContainerNo(dto.getContainerNo());
        repairTaskMx.setContainerSubNo(dto.getContainerSubNo());
        repairTaskMx.setGoodsId(goods.getId());
        repairTaskMx.setRepairStatus(RepairTaskMx.MXSTATUS_COMPLETE);
        repairTaskMx.setCreateTime(new Date());
        repairTaskMx.setRepairCount(dto.getGoodsNum());
        RepairTaskMxMapper.save(repairTaskMx);

        //当一个货格补货完成时，更改货格的数量
        Map<String, Object> map = MapUtils.put("commodityNum", subInfo.getCommodityNum() + dto.getGoodsNum()).getMap();
        //如果商品此货格的商品id是null，则换位新的商品id
        if (commodityId == null) {
            map.put("commodityId", goods.getId());
        }
        //如果货格的效期为空 或者 新效期小于当前容器的效期，则更新效期
        if (dto.getExpiryDate() != null) {
            if (subInfo.getExpiryDate() == null || dto.getExpiryDate().getTime() < subInfo.getExpiryDate().getTime()) {
                map.put("expiryDate", dto.getExpiryDate());
            }
        }
        //更新容器的数量和效期
        containerSubInfoMapper.updateMapById(subInfo.getContainerSubNo(), map, ContainerSub.class);
        //更新补货计划明细状态为完成
        repairPlanMxMapper.updateBycontainerSubNo(repairTaskMx.getContainerSubNo());

        //判断料箱里的 货格 是否全部完成，更新补货任务状态
        if (RepairTaskMxMapper.findByMap(MapUtils.put("containerNo", taskHz.getContainerNo()).getMap(), RepairTaskMx.class).
                stream().allMatch(p -> p.getRepairStatus().equals(RepairTaskMx.MXSTATUS_COMPLETE))) {
            //将补货任务 的状态改为已完成
            Criteria ctr = Criteria.forClass(RepairTaskHz.class);
            ctr.setRestriction(Restrictions.eq("containerNo", taskHz.getContainerNo()));
            repairTaskHzMapper.updateMapByCriteria(MapUtils.put("repairStatus", RepairTaskHz.REPAIRTASK_COMPLETE).getMap(), ctr);
        }

        List<RepairPlanMx> planMxs = repairPlanMxMapper.findByMap(MapUtils.put("containerSubNo", repairTaskMx.getContainerSubNo()).getMap(), RepairPlanMx.class);
        //补货计划明细 都完成时，将补货计划和明细转历史
        if (repairPlanMxMapper.findByMap(MapUtils.put("repairPlanId", planMxs.get(0).getRepairPlanId()).getMap(), RepairPlanMx.class).
                stream().allMatch(p -> p.getRepairStatus().equals(RepairTaskMx.MXSTATUS_COMPLETE))) {
            repairPlanMxHisMapper.mxIntHis(planMxs.get(0).getRepairPlanId());
            repairPlanMxMapper.deleteById(planMxs.get(0).getRepairPlanId(),RepairPlanMx.class);
            //将计划汇总转历史,是自动确认完成，还是前台确认完成，已确认后台转历史，2020/9/2？
            repairPlanHisMapper.backup(planMxs.get(0).getRepairPlanId());
            repairPlanMapper.deleteById(planMxs.get(0).getRepairPlanId(),RepairPlan.class);
        }
        RepairStation repairStation = getRepairStation();
        String currWorkContainerNo = repairStation.getCurrWorkContainerNo();
        String containerNo = dto.getContainerNo();
        //先验证完成的补货料箱与当前站台的料箱是否一致
        if (!containerNo.equals(currWorkContainerNo)) {
            throw new Exception( MessageFormat.format("完成料箱：{0}，与当前补货站台工作料相：{1}，" +
                    "不一致", containerNo, currWorkContainerNo), null);
        }
        //设置当前补货站台的 当前料箱为空
        repairStation.setCurrWorkContainerNo(null);
        repairStation.setLastUpdateDate(new Date());
        repairStationMapper.update(repairStation);

        //9.4新增保存补货记录
        InboundRepairInfo inboundRepairInfo = new InboundRepairInfo();
        inboundRepairInfo.setContainerNo(dto.getContainerNo());
        inboundRepairInfo.setContainerSubNo(dto.getContainerSubNo());
        inboundRepairInfo.setCommodityNum(dto.getGoodsNum());
        inboundRepairInfo.setCreateTime(new Date());
        inboundRepairInfo.setGoodsId(goods.getId());
        inboundRepairInfo.setInboundStatu(InboundRepairInfo.REPQAIR_STATUS);//补货
        inboundRepairInfoService.save(inboundRepairInfo);

        return RestMessage.newInstance(true, "成功", null);
    }

    /**
     * 补货任务hz 和 mx 转历史
     *
     * @param containerNo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repairTaskIntoHis(String containerNo) {
        List<RepairTaskMx> mxs = RepairTaskMxMapper.findByMap(MapUtils.put("container", containerNo).getMap(), RepairTaskMx.class);
        List<Integer> ids = mxs.stream().map(RepairTaskMx::getId).collect(Collectors.toList());
        int hzId = mxs.get(0).getRepairTaskHzId();
        //补货任务明细转历史
        repairTaskHzMapper.repairTaskMxToHis(hzId);
        RepairTaskMxMapper.deleteByIds(ids.toArray(), RepairTaskMx.class);

        repairTaskHzMapper.repairTaskHzToHis(hzId);
        repairTaskHzMapper.deleteById(hzId, RepairTaskHz.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestMessage<String> complete(String containerNo) throws Exception {
        RepairStation repairStation = getRepairStation();
        String currWorkContainerNo = repairStation.getCurrWorkContainerNo();
        //先验证完成的补货料箱与当前站台的料箱是否一致
        if (!containerNo.equals(currWorkContainerNo)) {
            return RestMessage.newInstance(false, MessageFormat.format("完成料箱：{0}，与当前补货站台工作料相：{1}，" +
                    "不一致", containerNo, currWorkContainerNo), null);
        }
        //设置当前补货站台的 当前料箱为空
        repairStation.setCurrWorkContainerNo(null);
        repairStation.setLastUpdateDate(new Date());
        repairStationMapper.update(repairStation);

        return RestMessage.newInstance(true, "完成成功", null);
    }

    @Override
    public RestMessage<Map<String, Object>> stationRefresh() throws Exception {
        RepairStation repairStation = getRepairStation();
        String currWorkContainerNo = repairStation.getCurrWorkContainerNo();
        if (StringUtils.isEmpty(currWorkContainerNo)) {
            return RestMessage.newInstance(false, "当前站台没有正在补货的料箱，请扫码", null);
        }
        return containerArrive(currWorkContainerNo);
    }

    @Override
    public void deleteAllRepairTask(String containerNo) {
        List<RepairPlanMx> repairPlanMxes = repairPlanMxMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), RepairPlanMx.class);
        RepairPlanMx repairPlanMx = repairPlanMxes.get(0);
        repairPlanMx.setRepairStatus(RepairPlanMx.REPAIR_STATUS_FINISH);
        repairPlanMxMapper.update(repairPlanMx);
        int repairPlanId = repairPlanMx.getRepairPlanId();
        List<RepairPlanMx> mxs = repairPlanMxMapper.findByMap(MapUtils.put("repairPlanId", repairPlanId).getMap(), RepairPlanMx.class);
        List<RepairPlanMxHis> mxHis = new ArrayList<RepairPlanMxHis>();
        List<RepairPlanMx> collect = mxs.stream().filter(x -> x.getRepairStatus() == 3).collect(Collectors.toList());
        if (mxs.size() == collect.size()) {
            for (RepairPlanMx mx : mxs) {
                //把所有的明细转历史
                RepairPlanMxHis planMxHis = new RepairPlanMxHis();
                planMxHis.setContainerNo(mx.getContainerNo());
                planMxHis.setId(mx.getId());
                planMxHis.setContainerSubNo(mx.getContainerSubNo());
                planMxHis.setRepairPlanId(mx.getRepairPlanId());
                planMxHis.setRepairStatus(mx.getRepairStatus());
                planMxHis.setCreateTime(mx.getCreateTime());
                mxHis.add(planMxHis);
            }
            repairPlanHisMapper.backup(repairPlanId);
            if (mxHis!=null &&mxHis.size()>0){
                if(!mxs.isEmpty()) {
                    repairPlanMxHisMapper.saveBatch(mxHis);
                    List<Integer> mxIds= mxs.stream().map(RepairPlanMx::getId).collect(Collectors.toList());
                    repairPlanMxMapper.deleteByIds(mxIds.toArray(), RepairPlanMx.class);
                }
            }
            repairPlanMapper.deleteById(repairPlanId, RepairPlan.class);
        }
    }

    /**
     * 当前补货站台的料箱
     *
     * @return
     * @throws Exception
     */
    private RepairStation getRepairStation() throws Exception {
        List<RepairStation> list = repairStationMapper.findByMap(null, RepairStation.class);
        if (CollectionUtils.isEmpty(list)) {
            throw new Exception("补货站台数据未配置");
        }
        return list.get(0);
    }
}
