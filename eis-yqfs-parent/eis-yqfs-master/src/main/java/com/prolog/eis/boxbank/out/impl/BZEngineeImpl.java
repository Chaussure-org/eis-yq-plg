package com.prolog.eis.boxbank.out.impl;

import com.prolog.eis.boxbank.out.BZEnginee;
import com.prolog.eis.boxbank.out.IOutService;
import com.prolog.eis.boxbank.rule.LayerLockRule;
import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dao.enginee.EngineGetInitMapper;
import com.prolog.eis.dao.enginee.EngineLxChuKuMapper;
import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dao.order.ContainerBindingHzMapper;
import com.prolog.eis.dao.order.ContainerSubBindingMxMapper;
import com.prolog.eis.dao.order.OrderMxBingdingMapper;
import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.dto.enginee.*;
import com.prolog.eis.pickstation.dao.StationMapper;
import com.prolog.eis.service.store.IStoreTaskService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.ListHelper;
import com.prolog.eis.wcs.service.IWCSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BZEngineeImpl implements BZEnginee {
    private final  Logger logger = LoggerFactory.getLogger(BZEngineeImpl.class);

    @Autowired
    private EngineGetInitMapper engineGetInitMapper;

    @Autowired
    private EngineLxChuKuMapper engineLxChuKuMapper;


    @Autowired
    private BZEngineeChku bzEngineeChku;

    /**
     * 获取可用商品库存
     * @return
     * @throws Exception
     */
    public Map<Integer, Integer> getSpStockMap() throws Exception {
        // 查询已绑定拣选单的订单还需占用的商品库存
        List<KuCunTotalDto> findAllUsedKuCun = engineGetInitMapper.findAllUsedKuCun();
        // 查询已上架状态并且无锁定的箱库库存
        List<KuCunTotalDto> findAllXkKuCun = engineGetInitMapper.findAllXkKuCun();
        // 查询在途商品库存
        List<KuCunTotalDto> findAllztKuCun = engineGetInitMapper.findAllztKuCun();
        // 查询商品库存
        Map<Integer, Integer> spStockMap = getMap(findAllUsedKuCun, findAllXkKuCun, findAllztKuCun);

        return spStockMap;
    }
    /**
     * 初始化箱库数据
     *
     * @return
     * @throws Exception
     */
    @Override
    public XiangKuDto init() throws Exception {
        XiangKuDto xiangKuDto = new XiangKuDto();
        // 查询商品可用库存
        Map<Integer, Integer> spStockMap = this.getSpStockMap();
        // 根据站台分组所有料箱出库绑定的数量
        List<ZhanTaiLXNo> chuKuLxCount = engineGetInitMapper.findChuKuLxCount();
        // 根据站台分组所有料箱到达线体状态为1：前往站台和2：等到播种的数量
        List<ZhanTaiLXNo> arriveLxCount = engineGetInitMapper.findArriveLxCount();
        // 查询货格所绑定的订单明细Id和数量
        List<HuoGeDingDanDto> huoGeDingDanDtos = engineGetInitMapper.findHuoGeDingDan();
        // 查询查询料箱未全部到位的拣选单下的料箱下的货格
        List<HuoGeDto> huoGes = engineGetInitMapper.findHuoGe();
        // 查询料箱未全部到位的拣选单已绑定的料箱
        List<LiaoXiangDto> liaoXiangDtos = engineGetInitMapper.findAllLiaoXiang();
        // 查询料箱未全部到位的拣选单下的所有订单
        List<DingDanDto> dingDanDtos = engineGetInitMapper.findAllDingDan();// 只查非锁定巷道的站台
        // 查询料箱未全部到位的拣选单下的所有订单明细
        List<DingDanMxDto> dingDanMxDtos = engineGetInitMapper.findAllDingDanMx();
        // 查询订单明细下绑定的数量
        List<DingDanMxDto> bindingMxNumDtos = engineGetInitMapper.findBindingNum();
        // 查询拣选单
        List<JianXuanDanDto> jianXuanDanDtos = engineGetInitMapper.findAllJianXuanDan();
        // 当前播种作业站台集合
        List<ZhanTaiDto> ztList = engineGetInitMapper.findAllStation();

        List<HuoGeSpMxDto> newHuoGeMap = getNewHuoGeMap(huoGeDingDanDtos);
        Map<String, List<HuoGeSpMxDto>> huoGeDingDanDtoByHuoGeNo = ListHelper.buildGroupDictionary(newHuoGeMap, p -> p.getHuoGeNo());
        for (HuoGeDto huoGe : huoGes) {
            List<HuoGeSpMxDto> list = huoGeDingDanDtoByHuoGeNo.get(huoGe.getHuoGeNo());
            if (list!=null && list.size()>0) {
                huoGe.setLxDingDanMxBindingMap(list.get(0).getLxDingDanMxBindingMap());
            }
            for (LiaoXiangDto liaoXiangDto : liaoXiangDtos) {
                if(liaoXiangDto.getLiaoXiangNo() == huoGe.getLiaoXiangNo()) {
                    huoGe.setLiaoXiang(liaoXiangDto);
                }
            }
        }

        Map<String, List<HuoGeDto>> huoGesGroupByLiaoXiangNo = ListHelper.buildGroupDictionary(huoGes, p->p.getLiaoXiangNo());
        for (LiaoXiangDto liaoXiangDto : liaoXiangDtos) {
            List<HuoGeDto> list = huoGesGroupByLiaoXiangNo.get(liaoXiangDto.getLiaoXiangNo());
            if(list!=null && list.size()>0) {
                liaoXiangDto.setHuoGeList(list);
            }
        }
        //将订单明细绑定数量翻入订单明细集合中
        if (bindingMxNumDtos.size() > 0) {
            Map<Integer, List<DingDanMxDto>> lxBindingMxCountsByMxId = ListHelper
                    .buildGroupDictionary(bindingMxNumDtos, p -> p.getId());
            for (DingDanMxDto dingDanMxDto : dingDanMxDtos) {
                List<DingDanMxDto> lxBindingMxCounts1 = lxBindingMxCountsByMxId.get(dingDanMxDto.getId());
                if (lxBindingMxCounts1 != null && lxBindingMxCounts1.size() > 0) {
                    dingDanMxDto.setBindingCount(lxBindingMxCounts1.get(0).getBindingCount());
                }
            }
        }
        // 将订单明细按照订单编号进行分组放入订单集合中
        Map<Integer, List<DingDanMxDto>> dingDanMxByHzId = ListHelper.buildGroupDictionary(dingDanMxDtos, p -> p.getOrderHzId());
        for (DingDanDto dingDanDto : dingDanDtos) {
            List<DingDanMxDto> dingDanSps = dingDanMxByHzId.get(dingDanDto.getId());
            if (dingDanSps == null) {
                dingDanDto.setDingDanMxList(new ArrayList<DingDanMxDto>());
            } else {
                for (DingDanMxDto dingDanSp : dingDanSps) {
                    dingDanSp.setDingDan(dingDanDto);
                }
                dingDanDto.setDingDanMxList(dingDanSps);
            }
        }


        Map<Integer, List<LiaoXiangDto>> liaoXiangDtosGroupByJxdId = ListHelper.buildGroupDictionary(liaoXiangDtos, p->p.getJxdId());
        Map<Integer, List<DingDanDto>> dingDanDtosGroupByJxdId = ListHelper.buildGroupDictionary(dingDanDtos, p->p.getJxdId());
        for (JianXuanDanDto jianXuanDanDto : jianXuanDanDtos) {
            List<LiaoXiangDto> liaoXiangDtos1 = liaoXiangDtosGroupByJxdId.get(jianXuanDanDto.getJxdId());
            List<DingDanDto> danDtos = dingDanDtosGroupByJxdId.get(jianXuanDanDto.getJxdId());
            if(liaoXiangDtos1!=null && liaoXiangDtos1.size()>0) {
                jianXuanDanDto.setLiangXiangList(liaoXiangDtos1);
            }
            if(danDtos!=null && danDtos.size()>0) {
                jianXuanDanDto.setDdList(danDtos);
            }
        }
        Map<Integer, List<ZhanTaiLXNo>> chuKuLxCountByZt = ListHelper.buildGroupDictionary(chuKuLxCount, p->p.getZhanTaiId());
        Map<Integer, List<ZhanTaiLXNo>> arriveLxCountByZt = ListHelper.buildGroupDictionary(arriveLxCount, p->p.getZhanTaiId());
        Map<Integer, List<JianXuanDanDto>> jianXuanDanDtosByZt = ListHelper.buildGroupDictionary(jianXuanDanDtos, p->p.getZhanTaiId());
        for (ZhanTaiDto zt : ztList) {
            List<ZhanTaiLXNo> chuKuLx = chuKuLxCountByZt.get(zt.getZhanTaiId());
            List<ZhanTaiLXNo> arriveLx = arriveLxCountByZt.get(zt.getZhanTaiId());
            List<JianXuanDanDto> ztjDanDtos = jianXuanDanDtosByZt.get(zt.getZhanTaiId());
            if(chuKuLx!=null && chuKuLx.size()>0) {
                zt.setChuKuLxCount(chuKuLx.get(0).getCount());
            }
            if(arriveLx!=null && arriveLx.size()>0) {
                zt.setArriveLxCount(arriveLx.get(0).getCount());
            }
            if(ztjDanDtos!=null && ztjDanDtos.size()>0) {
                zt.setJxdList(ztjDanDtos);
            }
        }
        xiangKuDto.setZtList(ztList);
        xiangKuDto.setKuCunSPMap(spStockMap);
        xiangKuDto.setSpStockMap(spStockMap);
        return xiangKuDto;

    }

    /**
     * 料箱出库调度
     * @param spId
     * @param zhanTaiDto
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public boolean chuku(int spId, ZhanTaiDto zhanTaiDto) throws Exception {
            /**
             * 2020.4.20确认 step1.计算每层的出库作业数(找任务数少的) step2.计算每层的入库作业数(找任务数少的)
             * step3.计算商品在每层非锁定的料箱数量(优先找数量多的) step4.排列优先级 step5.选择总任务数小于小车数量*2(找层)
             * step6.找出离提升机(x、y)最近的非锁定的该商品料箱（用内存计算） step7.发送出库任务，路径规划
             */
            // 计算每层的总任务数
            List<CengLxTaskDto> totalTasks = engineLxChuKuMapper.findTotalTask();
            // 计算每层的出库作业数
            List<CengLxTaskDto> ckCengLxTaskDtos = engineLxChuKuMapper.findCkCengLxTaskDtos();
            // 计算每层的入库作业数
            List<CengLxTaskDto> rkCengLxTaskDtos = engineLxChuKuMapper.findRkCengLxTaskDtos();

            // 计算商品在每层非锁定的料箱数量
            List<AllHuoWeiDto> allHuoWeiDtos = engineLxChuKuMapper.findAllHuoWeiDto(spId);
            // 拼装集合
            Map<Integer, List<CengLxTaskDto>> ckCengLxTaskDtosByCeng = ListHelper.buildGroupDictionary(ckCengLxTaskDtos,
                    p -> p.getCeng());
            Map<Integer, List<CengLxTaskDto>> rkCengLxTaskDtosByCeng = ListHelper.buildGroupDictionary(rkCengLxTaskDtos,
                    p -> p.getCeng());
            Map<Integer, List<CengLxTaskDto>> totalTasksByCeng = ListHelper.buildGroupDictionary(totalTasks,
                    p -> p.getCeng());

            for (AllHuoWeiDto allHuoWeiDto : allHuoWeiDtos) {
                List<CengLxTaskDto> cengCkLxTaskDtos = ckCengLxTaskDtosByCeng.get(allHuoWeiDto.getCeng());
                List<CengLxTaskDto> cengRkLxTaskDtos = rkCengLxTaskDtosByCeng.get(allHuoWeiDto.getCeng());
                List<CengLxTaskDto> cengTotalLxTaskDtos = totalTasksByCeng.get(allHuoWeiDto.getCeng());

                if (cengCkLxTaskDtos != null && cengCkLxTaskDtos.size() > 0) {
                    allHuoWeiDto.setCkTaskCount(cengCkLxTaskDtos.get(0).getTaskCount());
                }

                if (cengRkLxTaskDtos != null && cengRkLxTaskDtos.size() > 0) {
                    allHuoWeiDto.setRkTaskCount(cengRkLxTaskDtos.get(0).getTaskCount());
                }

                if (cengTotalLxTaskDtos != null && cengTotalLxTaskDtos.size() > 0) {
                    allHuoWeiDto.setRkTaskCount(cengTotalLxTaskDtos.get(0).getTaskCount());
                }

            }
            getSortList(allHuoWeiDtos);

            if(allHuoWeiDtos==null || allHuoWeiDtos.size()==0) {
                logger.info("++++++++++++++++++没有找到" + spId + "商品对应的货格+++++++++++++++++++");
                return false;
            }

            for (int i = 0; i < allHuoWeiDtos.size(); i++) {
                AllHuoWeiDto allHuoWeiDto = allHuoWeiDtos.get(i);
                    // 出库
                try {
                    boolean isSuccessChuku = bzEngineeChku.cengChuku(allHuoWeiDto.getCeng(),spId,zhanTaiDto);
                    if (isSuccessChuku)
                        return true;
                }catch (Exception e){
                    continue;
                }

            }

            FileLogHelper.WriteLog("StoreCkError", "无法获得可用货位,当前商品为：" + spId);
            return false;


    }





    private Map<Integer, Integer> getMap(List<KuCunTotalDto> findAllUsedKuCun, List<KuCunTotalDto> findAllXkKuCun,
                                         List<KuCunTotalDto> findAllztKuCun) throws Exception {

        Map<Integer, Integer> spNumMap = new HashMap<Integer, Integer>();
        // 箱库库存+在途库存-待使用的库存
        List<KuCunTotalDto> xkAndztKuCun = new ArrayList<KuCunTotalDto>();
        if (findAllXkKuCun.size() == 0) {
            xkAndztKuCun.addAll(findAllztKuCun);
        } else if (findAllztKuCun.size() == 0) {
            xkAndztKuCun.addAll(findAllXkKuCun);
        } else {
            xkAndztKuCun.addAll(findAllztKuCun);
            xkAndztKuCun.addAll(findAllXkKuCun);
            // TODO
            Map<KuCunTotalDto, KuCunTotalDto> map = new HashMap<KuCunTotalDto, KuCunTotalDto>();
            for (KuCunTotalDto kuCunTotalDto : xkAndztKuCun) {
                if (map.containsKey(kuCunTotalDto)) {
                    map.put(kuCunTotalDto, KuCunTotalDto.merge(kuCunTotalDto, map.get(kuCunTotalDto)));
                } else {
                    map.put(kuCunTotalDto, kuCunTotalDto);
                }
            }
            Collection<KuCunTotalDto> values = map.values();
            if (values instanceof List)
                xkAndztKuCun = (List) values;
            else
                xkAndztKuCun = new ArrayList<KuCunTotalDto>(values);
        }

        if (findAllUsedKuCun.size() == 0) {
            for (KuCunTotalDto kuCunTotalDto : xkAndztKuCun) {
                spNumMap.put(kuCunTotalDto.getSpId(), kuCunTotalDto.getNum());
            }
        } else if (xkAndztKuCun.size() == 0) {
            for (KuCunTotalDto findAllUsedKuCunByHangdao1 : findAllUsedKuCun) {
                spNumMap.put(findAllUsedKuCunByHangdao1.getSpId(), 0 - findAllUsedKuCunByHangdao1.getNum());
            }
        } else {
            CopyOnWriteArrayList<KuCunTotalDto> findAllUsedKuCun2 = new CopyOnWriteArrayList<KuCunTotalDto>(
                    findAllUsedKuCun);
            CopyOnWriteArrayList<KuCunTotalDto> xkAndztKuCun2 = new CopyOnWriteArrayList<KuCunTotalDto>(xkAndztKuCun);
            // 用总库存减去目前订单数量为可用数量
            for (KuCunTotalDto kuCunTotalDto2 : xkAndztKuCun2) {
                for (KuCunTotalDto kuCunTotalDto1 : findAllUsedKuCun2) {
                    if (kuCunTotalDto1.getSpId() == kuCunTotalDto2.getSpId()) {
                        int num = kuCunTotalDto2.getNum() - kuCunTotalDto1.getNum();
                        spNumMap.put(kuCunTotalDto1.getSpId(), num);
                    } else {
                        int num = 0 - kuCunTotalDto1.getNum();
                        if (!spNumMap.containsKey((kuCunTotalDto1.getSpId()))) {
                            spNumMap.put(kuCunTotalDto1.getSpId(), num);
                        }
                        if (!spNumMap.containsKey(kuCunTotalDto2.getSpId())) {
                            spNumMap.put(kuCunTotalDto2.getSpId(), kuCunTotalDto2.getNum());
                        }
                    }
                }
            }
        }
        return spNumMap;
    }

    private List<HuoGeSpMxDto> getNewHuoGeMap(List<HuoGeDingDanDto> huoGeDingDanDtos)throws Exception{
        List<HuoGeSpMxDto> newHuoGeDingDans = new ArrayList<HuoGeSpMxDto>();
        Map<String, List<HuoGeDingDanDto>> buildGroupDictionary = ListHelper.buildGroupDictionary(huoGeDingDanDtos, p->p.getHuoGeNo());
        for(String huogeNo : buildGroupDictionary.keySet()) {
            HuoGeSpMxDto huoGeSpMxDto = new HuoGeSpMxDto();
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            List<HuoGeDingDanDto> list = buildGroupDictionary.get(huogeNo);
            for (HuoGeDingDanDto huoGeDingDanDto2 : list) {
                map.put(huoGeDingDanDto2.getDingDanMxId(), huoGeDingDanDto2.getNum());
            }
            huoGeSpMxDto.setHuoGeNo(huogeNo);
            huoGeSpMxDto.setLxDingDanMxBindingMap(map);
        }
        return newHuoGeDingDans;
    }

    private List<AllHuoWeiDto> getSortList(List<AllHuoWeiDto> allHuoWeiDtos) {
        Collections.sort(allHuoWeiDtos, new Comparator<AllHuoWeiDto>() {

            @Override
            public int compare(AllHuoWeiDto o1, AllHuoWeiDto o2) {
                // 如果巷道作业数排序相同，再按层出库任务数排序（升序）
                int j = o1.getCkTaskCount() - o2.getCkTaskCount();
                if (j == 0) {
                    // 如果层出库任务数相同，再按层入库任务数排序（升序）
                    int k = o1.getRkTaskCount() - o2.getRkTaskCount();
                    if (k == 0) {
                        // 如果层任务数相同，再按料箱数排序（降序）
                        return o2.getLxCount() - o1.getLxCount();
                    }
                    return k;
                }
                return j;
            }
        });
        return allHuoWeiDtos;
    }







}
