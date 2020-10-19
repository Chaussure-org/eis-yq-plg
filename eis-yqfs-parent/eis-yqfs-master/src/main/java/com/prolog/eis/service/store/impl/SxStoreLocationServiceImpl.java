package com.prolog.eis.service.store.impl;

import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dao.store.StoreLocationGroupMapper;
import com.prolog.eis.dao.store.StoreLocationMapper;
import com.prolog.eis.dao.store.StoreLocationRelationMapper;
import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.dto.store.LayerContainerCountDto;
import com.prolog.eis.dto.store.LayerStoreTaskDto;
import com.prolog.eis.dto.store.StoreLocationGroupDto;
import com.prolog.eis.dto.store.StoreTaskDto;
import com.prolog.eis.enums.HoisterEnum;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.store.SxStoreLocation;
import com.prolog.eis.model.store.SxStoreLocationGroup;
import com.prolog.eis.model.store.SxStoreLocationRelation;
import com.prolog.eis.service.masterbase.ContainerService;
import com.prolog.eis.service.masterbase.GoodsService;
import com.prolog.eis.service.store.IStoreLocationService;
import com.prolog.eis.service.store.IStoreTaskService;
import com.prolog.eis.util.DistanceUtils;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.SxStoreRelationUtil;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.FieldSelector;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SxStoreLocationServiceImpl implements IStoreLocationService {

    private  final Logger logger = LoggerFactory.getLogger(SxStoreLocationServiceImpl.class);

    @Autowired
    private StoreLocationRelationMapper storeLocationRelationMapper;
    @Autowired
    private StoreLocationGroupMapper sxStoreLocationGroupMapper;
    @Autowired
    private StoreLocationMapper sxStoreLocationMapper;
    @Autowired
    private IStoreTaskService sxStoreTaskFinishService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private EisProperties properties;
    @Autowired
    private GoodsService goodsService;

    @Override
    public SxStoreLocation getById(int id) {
        return sxStoreLocationMapper.findById(id,SxStoreLocation.class);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void importStoreLocation(List<StoreLocationGroupDto> storeLocationGroupDtos) throws Exception {
        List<StoreLocationGroupDto> saveList = checkStoreLocationGroup(storeLocationGroupDtos);
        if(CollectionUtils.isEmpty(saveList)){
            return;
        }
        //寻找中间货位
        findXy(saveList);
        //保存货位组
        sxStoreLocationGroupMapper.saveBatchReturnKey(saveList);
        //保存货位
        saveStoreLocation(saveList);
    }

    /**
     * 效验货位组  目前货位更新  只更新了xy 指定货位索引更新
     * 是否全量更新
     * @param storeLocationGroupDtos
     */
    private List<StoreLocationGroupDto> checkStoreLocationGroup(List<StoreLocationGroupDto> storeLocationGroupDtos) throws Exception {
        //查找已存在的货位组
        List<String> groupNos = storeLocationGroupDtos.stream().map(StoreLocationGroupDto::getGroupNo).collect(Collectors.toList());
        String groupNosStr = String.join(",",groupNos);
        List<StoreLocationGroupDto> existgroups = sxStoreLocationGroupMapper.findByGroupNos(groupNosStr);
        if(CollectionUtils.isEmpty(existgroups)){
            //没有已存在的货位组
            return storeLocationGroupDtos;
        }
        Map<String, StoreLocationGroupDto> map = existgroups.stream().collect(Collectors.toMap(StoreLocationGroupDto::getGroupNo, Function.identity()));

        for (StoreLocationGroupDto storeLocationGroupDto : storeLocationGroupDtos) {
            if(map.containsKey(storeLocationGroupDto.getGroupNo())){
                StoreLocationGroupDto dto = map.get(storeLocationGroupDto.getGroupNo());
                if(dto.getLocationNum() != storeLocationGroupDto.getStoreLocations().size()){
                    throw new Exception(MessageFormat.format("货位组数量不一致,导入货位组数据:{0},数据库货位组货位数量:{1}", PrologApiJsonHelper.toJson(storeLocationGroupDto),PrologApiJsonHelper.toJson(dto)));
                }
                storeLocationGroupDto.setId(dto.getId());
            }
        }
        //效验通过
        List<StoreLocationGroupDto> list = storeLocationGroupDtos.stream().filter(t -> map.containsKey(t.getGroupNo())).collect(Collectors.toList());
        //更新已存在的货位组
        updateExistData(list);
        List<StoreLocationGroupDto> saceList = storeLocationGroupDtos.stream().filter(t -> !map.containsKey(t.getGroupNo())).collect(Collectors.toList());
        return saceList;
    }

    //更新已存在的货位组
    private void updateExistData(List<StoreLocationGroupDto> list) {
        List<SxStoreLocation> storeLocations = new ArrayList<>();
        list.stream().forEach(t -> {
            List<SxStoreLocation> storeLocation = t.getStoreLocations();
            storeLocation.stream().forEach(k -> k.setStoreLocationGroupId(t.getId()));
            storeLocations.addAll(storeLocation);
        });
        findXy(list);
        //根据id批量更新货位组
        sxStoreLocationGroupMapper.batchUpdateById(list);
        //更新货位
        sxStoreLocationMapper.batchUpdateById(storeLocations);
    }

    private void saveStoreLocation(List<StoreLocationGroupDto> storeLocationGroupDtos) {
        List<SxStoreLocation> storeLocations = new ArrayList<>();
        storeLocationGroupDtos.stream().forEach(t -> {
            List<SxStoreLocation> storeLocation = t.getStoreLocations();
            int id = t.getId();
            storeLocation.stream().forEach(s -> {
                s.setStoreLocationGroupId(id);
                s.setLayer(t.getLayer());
                //一汽添加 02 03
                s.setStoreNo(PrologCoordinateUtils.splicingStr(s.getX(),s.getY(),t.getLayer()) + s.getDirectionCoding());
            });
            storeLocations.addAll(storeLocation);
        });
        sxStoreLocationMapper.saveBatchReturnKey(storeLocations);
        //找相邻货位
        findAdjacentStore(storeLocations);
        //入库货位计算出来
        CompletableFuture.runAsync(() -> {
            try {
                sxStoreTaskFinishService.computeIsInBoundLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void findAdjacentStore(List<SxStoreLocation> storeLocations) {
        Map<Integer, List<SxStoreLocation>> collect = storeLocations.stream().collect(Collectors.groupingBy(SxStoreLocation::getStoreLocationGroupId));
        collect.forEach((k, v) -> {
            if (v.size() > 1) {
                List<SxStoreLocation> locations = v.stream().sorted(Comparator.comparing(SxStoreLocation::getLocationIndex)).collect(Collectors.toList());
                for(int i=0;i<locations.size();i++){
                    SxStoreLocation sxStoreLocation = locations.get(i);
                    if(i != 0){
                        sxStoreLocation.setStoreLocationId1(locations.get(i-1).getId());
                    }
                    if(i != locations.size()-1){
                        sxStoreLocation.setStoreLocationId2(locations.get(i+1).getId());
                    }
                }
            }
        });
        sxStoreLocationMapper.updateAdjacentStore(storeLocations);
    }

    private void findXy(List<StoreLocationGroupDto> storeLocationGroupDtos) {
        for (StoreLocationGroupDto storeLocationGroupDto : storeLocationGroupDtos) {
            List<SxStoreLocation> storeLocations = storeLocationGroupDto.getStoreLocations();
            if (CollectionUtils.isEmpty(storeLocations)) {
                continue;
            }
            storeLocationGroupDto.setInOutNum(storeLocationGroupDto.getEntrance()<3?1:2);
            storeLocationGroupDto.setLocationNum(storeLocations.size());
            int size = storeLocations.size();
            int ceil = (int) Math.ceil(size / 2);
            //获取货位
            SxStoreLocation dto = storeLocations.stream().filter(t -> t.getLocationIndex() == ceil).findFirst().orElse(storeLocations.get(0));
            //赋值
            storeLocationGroupDto.setX(dto.getX());
            storeLocationGroupDto.setY(dto.getY());
            //深度赋值
            depthCalc(storeLocationGroupDto,storeLocations);
        }
    }

    private void depthCalc(StoreLocationGroupDto storeLocationGroupDto, List<SxStoreLocation> storeLocations) {
        // 入口类型：1、仅入口1 向上，2、仅入口2  向下，3、入口1+入口2
        int entrance = storeLocationGroupDto.getEntrance();
        int orginIndex;
        if(entrance == 1){
            orginIndex = storeLocations.stream().min(Comparator.comparing(SxStoreLocation::getLocationIndex)).get().getLocationIndex();
            getDepthByOneOrgin(orginIndex,storeLocations);
        }else if(entrance == 2){
            orginIndex = storeLocations.stream().max(Comparator.comparing(SxStoreLocation::getLocationIndex)).get().getLocationIndex();
            getDepthByOneOrgin(orginIndex,storeLocations);
        }else if(entrance == 3){
            getDepthByTwoOrgin(storeLocations);
        }
    }

    //获取深度  两个入口
    private void getDepthByTwoOrgin(List<SxStoreLocation> storeLocations) {
        int minIndex = storeLocations.stream().min(Comparator.comparing(SxStoreLocation::getLocationIndex)).get().getLocationIndex();
        int maxIndex = storeLocations.stream().max(Comparator.comparing(SxStoreLocation::getLocationIndex)).get().getLocationIndex();
        storeLocations.stream().forEach(t ->{
            int min = Math.min(Math.abs(t.getLocationIndex() - minIndex), Math.abs(t.getLocationIndex() - maxIndex));
            t.setDepth(min);
        });
    }

    //获取深度  一个入口
    private void getDepthByOneOrgin(int orginIndex, List<SxStoreLocation> storeLocations) {
        storeLocations.stream().forEach(t ->{
            t.setDepth(Math.abs(t.getLocationIndex()-orginIndex));
        });
    }

	@Override
	public List<SxStoreLocation> getByGroupId(int groupId) throws Exception {
		List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(MapUtils.put("storeLocationGroupId", groupId).getMap(), SxStoreLocation.class);
		return sxStoreLocations;
	}


    @Override
    //@Transactional
    public void initData(int layerCount,int xCount,int yCount) throws Exception {
//        int layerCount = 13;
////        int xCount = 24;
////        int yCount=63;
        int groupNo=1;
        List<Integer> explist = Arrays.asList(0,34,62,63);
        for(int i=1;i<14;i++){

            for(int x =0;x<xCount;x++){

                    for(int y=0;y<yCount;y++){
                        if(explist.contains(y)){
                            continue;
                        }

                        String layer = StringUtils.padLeft(i+"",2,"0");
                        String xstr = StringUtils.padLeft(x+"",4,"0");
                        String ystr = StringUtils.padLeft(y+"",4,"0");
                        String point1 = String.format("%s%s%s%s",layer,xstr,ystr,"02");
                        String point2 = String.format("%s%s%s%s",layer,xstr,ystr,"03");

                        this.addLocation2(groupNo,i,x,y,2,point1);
                        groupNo ++;
                        this.addLocation2(groupNo,i,x,y,3,point2);
                        groupNo ++;
                    }
            }
        }
    }


    private void addLocation2(int groupNo,int layer,int x,int y,int pad,String point){
        SxStoreLocationGroup group = new SxStoreLocationGroup();
        group.setGroupNo(groupNo+"");
        group.setEntrance(1);
        group.setInOutNum(1);
        group.setIsLock(0);
        group.setAscentLockState(0);
        group.setReadyOutLock(0);
        group.setLayer(layer);
        group.setX(x*10+pad);
        group.setY(y);
        group.setLocationNum(1);
        group.setCreateTime(new Date());
        sxStoreLocationGroupMapper.save(group);

        SxStoreLocation location = new SxStoreLocation();
        location.setStoreNo(point);
        location.setStoreLocationGroupId(group.getId());
        location.setLayer(layer);
        location.setX(x);
        location.setY(y);
        location.setAscentLockState(0);
        location.setLocationIndex(1);
        location.setDepth(1);
        location.setDeptNum(1);
        location.setIsInBoundLocation(1);
        location.setCreateTime(new Date());

        sxStoreLocationMapper.save(location);
        logger.info("add location "+point);



    }

    @Override
    public long getTotalCount() {
        return sxStoreLocationMapper.findCountByMap(null,SxStoreLocation.class);
    }

    /**
     * 生成并保存容器编号四周关系
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public void saveSxStoreRelation() {
        //查询id和容器编号
        List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(null, SxStoreLocation.class);

        //z轴标识"02"代表在x轴左边"03"代表在x轴右边
        String zFlag2 = "02";
        String zFlag3 = "03";



        for (SxStoreLocation sxStoreLocation : sxStoreLocations) {
            int stroeParentId = sxStoreLocation.getId();
            String storeNo = sxStoreLocation.getStoreNo();
            int lay = Integer.parseInt(storeNo.substring(0, 2));
            int x = Integer.parseInt(storeNo.substring(2, 6));
            int y = Integer.parseInt(storeNo.substring(6, 10));
            String z = storeNo.substring(10, 12);
            //母容器对应四周子容器方位
            String leftFrontStoreNo = "";//左前
            String leftStoreNo = "";//左
            String leftRearStoreNo = "";//左后
            String rearStoreNo = "";//后
            String rightRearStoreNo = "";//右后
            String frontStoreNo = "";//前
            String rightFrontStoreNo = "";//右前
            String rightStoreNo = "";//右
            if (z.equals(zFlag2)) {
                //母容器在x轴左边 z===>02

                //判断母容器是否在最左边，是则左边无子容器
                //左
                leftStoreNo = SxStoreRelationUtil.xMinusOne(lay, x, y, zFlag3);

                //左前   y轴最大值无则无左前
                leftFrontStoreNo = SxStoreRelationUtil.xMinusOneYPlusOne(lay, x, y, zFlag3);

                //左后  y轴第一列无左后
                leftRearStoreNo = SxStoreRelationUtil.xMinusOneYMinusOne(lay, x, y, zFlag3);


                //判断母容器是否在y轴第一列
                //后
                rearStoreNo = SxStoreRelationUtil.yMinusOne(lay, x, y, zFlag2);
                //右后
                rightRearStoreNo = SxStoreRelationUtil.yMinusOne(lay, x, y, zFlag3);

                //前
                frontStoreNo = SxStoreRelationUtil.yPlusOne(lay, x, y, zFlag2);
                //右前
                rightFrontStoreNo = SxStoreRelationUtil.yPlusOne(lay, x, y, zFlag3);

                //右
                rightStoreNo = SxStoreRelationUtil.notMinusPlus(lay, x, y, zFlag3);


            } else {
                //母容器在x轴右边  z==>03

                //判断x是否在最右边，是则右边无子容器
                //右
                rightStoreNo = SxStoreRelationUtil.xPlusOne(lay, x, y, zFlag2);

                //右后
                rightRearStoreNo = SxStoreRelationUtil.xPlusOneYJMinusOne(lay, x, y, zFlag2);


                //右前
                rightFrontStoreNo = SxStoreRelationUtil.xPlusOneYPlusOne(lay, x, y, zFlag2);


                //后
                rearStoreNo = SxStoreRelationUtil.yMinusOne(lay, x, y, zFlag3);
                //左右
                leftRearStoreNo = SxStoreRelationUtil.yMinusOne(lay, x, y, zFlag2);

                //前
                frontStoreNo = SxStoreRelationUtil.yPlusOne(lay, x, y, zFlag3);
                //左前
                leftFrontStoreNo = SxStoreRelationUtil.yPlusOne(lay, x, y, zFlag2);

                leftStoreNo = SxStoreRelationUtil.notMinusPlus(lay, x, y, zFlag2);
            }
            String[] strStoreNos = {leftFrontStoreNo, leftStoreNo, leftRearStoreNo, rearStoreNo, rightRearStoreNo, frontStoreNo, rightFrontStoreNo, rightStoreNo};
            Criteria criteria = Criteria.forClass(SxStoreLocation.class);
            criteria.setRestriction(Restrictions.in("storeNo", strStoreNos));
            FieldSelector fieldSelector = FieldSelector.newInstance();
            fieldSelector.include("id");
            List<SxStoreLocation> sxStoreLocationList = sxStoreLocationMapper.findFieldsByCriteria(fieldSelector, criteria);
            List<SxStoreLocationRelation> sxStoreRelationList = new ArrayList<>();
            for (SxStoreLocation storeLocation : sxStoreLocationList) {
                int stroeChildId = storeLocation.getId();//子容器id
                sxStoreRelationList.add(new SxStoreLocationRelation(0, stroeParentId, stroeChildId, 0));
            }


            storeLocationRelationMapper.saveBatch(sxStoreRelationList);
        }
    }

    /**
     * 根据容器寻找最佳空货位
     *
     * @param containerNo
     * @return
     */
    @Override
    public synchronized SxStoreLocation getBestEmptyLocation(String containerNo,String pointId) throws Exception {
        List<ContainerSub> subs = containerService.getContainerSubs(containerNo);
        int[] goodsIds = null;
        if (subs.size() > 0) {
            try {
                goodsIds = subs.stream().filter(x -> x.getCommodityId() > 0).mapToInt(x -> x.getCommodityId()).toArray();
            } catch (Exception e) {
                logger.warn("容器货格无商品");
            }
        }
        int layer = this.getBestEmpytLayer(goodsIds,pointId);

        return this.getBestLocation(layer,goodsIds);
    }

    public SxStoreLocation getBestLocation(int layer, int[] goodsIds) {
        List<SxStoreLocation> list = sxStoreLocationMapper.findAvailableLocationByLayer(layer);
        if (goodsIds!=null && goodsIds.length!=0) {
            List<String> abc = goodsService.getGoodsABCByIds(goodsIds);
            if (abc != null && abc.size() > 0) {
                list = getABCStore(list, abc);
            }
        }
        SxStoreLocation slocation = null;
        double distance = 0;
        for (SxStoreLocation location : list) {
            if (slocation == null) {
                slocation = location;
                distance = DistanceUtils.toDistance(properties.getBasePointX(), properties.getBasePointY(), location.getX(), location.getY());
                continue;
            } else {
                double d = DistanceUtils.toDistance(properties.getBasePointX(), properties.getBasePointY(), location.getX(), location.getY());
                if (d < distance) {
                    distance = d;
                    slocation = location;
                }
            }
        }

        return slocation;
    }

    public List<SxStoreLocation> getABCStore(List<SxStoreLocation> list, List<String> abc) {
        if (abc.contains("A")){
            list = list.stream().filter(x->x.getX()==6||x.getX()==8||x.getX()==10).collect(Collectors.toList());
            if (list.size() == 0){
                list = list.stream().filter(x->x.getX()==5||x.getX()==7||x.getX()==9||x.getX()==11).collect(Collectors.toList());
                if (list.size() == 0){
                    list = list.stream().filter(x->x.getX()==0||x.getX()==1||x.getX()==2||x.getX()==3||x.getX()==4).collect(Collectors.toList());
                }
            }
        }else if (abc.contains("B")){
            list = list.stream().filter(x->x.getX()==5||x.getX()==7||x.getX()==9||x.getX()==11).collect(Collectors.toList());
            if (list.size() == 0){
                list = list.stream().filter(x->x.getX()==0||x.getX()==1||x.getX()==2||x.getX()==3||x.getX()==4).collect(Collectors.toList());
                if (list.size() == 0){
                    list = list.stream().filter(x->x.getX()==6||x.getX()==8||x.getX()==10).collect(Collectors.toList());
                }
            }
        }else{
            list = list.stream().filter(x->x.getX()==0||x.getX()==1||x.getX()==2||x.getX()==3||x.getX()==4).collect(Collectors.toList());
            if (list.size() == 0){
                list = list.stream().filter(x->x.getX()==5||x.getX()==7||x.getX()==9||x.getX()==11).collect(Collectors.toList());
                if (list.size() == 0){
                    list = list.stream().filter(x->x.getX()==6||x.getX()==8||x.getX()==10).collect(Collectors.toList());
                }
            }
        }
        return list;
    }

    /**
     * 根据商品寻找空货位层最佳层
     *
     * @param goodsIds
     * @return
     */
    @Override
    public int getBestEmpytLayer(int[] goodsIds,String pointId) {
        //寻找可用层
        List<Integer> layers = sxStoreLocationMapper.findAvailableLayer();
        if (layers.size() == 0)
            return 0;

        List<Integer> lastLayers = new ArrayList<>();
        //添加提升机任务相关
        List<StoreTaskDto> storeTasks = new ArrayList<>();
        if (pointId == null) {
            storeTasks = storeMapper.findStoreTasks();
        }else{
            String hoisId = HoisterEnum.getValues(pointId);
            storeTasks = storeMapper.findStoreTasksByHoisId(hoisId);
        }
        if (storeTasks.size() == 0) {
            //没有任务
            //找商品料箱数最少的层
            return this.getGoodsLayers(layers, goodsIds);
        } else {
            //寻找入库任务最少的层
            Map<Integer, List<StoreTaskDto>> inTaskMap = com.prolog.eis.util.CollectionUtils.mapList(storeTasks, task -> task.getLayer(), task -> task.getStoreState() == SxStore.STATE_IN);
            if (inTaskMap.size() < 1) {
                //没有入库任务
                lastLayers.addAll(layers);
            } else if (inTaskMap.size() == 1) {
                for (Integer layer : layers) {
                    if (!inTaskMap.containsKey(layer)){
                        return layer;
                    }
                }
                //直接返回
                Iterator<Integer> itr = inTaskMap.keySet().iterator();
                return itr.next();
            }

            //寻找入库任务最少的层
            List<Integer> inlayers = this.findLayerByMinTaskCount(layers, inTaskMap,1);
            if (inlayers.size() > 1) {
                lastLayers.clear();
                lastLayers.addAll(inlayers);
            } else if (inlayers.size() == 1) {
                //直接返回
                return inlayers.get(0);
            }

            //出库任务
            Map<Integer, List<StoreTaskDto>> outTaskList = com.prolog.eis.util.CollectionUtils.mapList(storeTasks, task -> task.getLayer(), task -> task.getStoreState() == SxStore.STATE_OUT);
            List<Integer> outlayers = this.findLayerByMinTaskCount(lastLayers, outTaskList,0);
            if (outlayers.size() == 0) {
                return this.getGoodsLayers(lastLayers, goodsIds);
            } else if (outlayers.size() == 1) {
                return outlayers.get(0);
            } else {
                return this.getGoodsLayers(outlayers, goodsIds);
            }
       }
    }

    /**
     * 根据货位编号获取货位
     *
     * @param number
     * @return
     */
    @Override
    public SxStoreLocation getbyNumber(String number) {
        List<SxStoreLocation> list = sxStoreLocationMapper.findByMap(MapUtils.put("storeNo",number).getMap(),SxStoreLocation.class);
        return list.size()>0?list.get(0):null;
    }

    /**
     * 锁定货位
     *
     * @param locationId
     * @throws Exception
     */
    @Override
//    @Transactional
    public void lock(int locationId) throws Exception {
        //// 锁定四周货位
        storeLocationRelationMapper.lockLocation(locationId);
        //货位组深位锁
        SxStoreLocation location = sxStoreLocationMapper.findById(locationId,SxStoreLocation.class);
        int groupId = location.getStoreLocationGroupId();
        sxStoreLocationGroupMapper.updateMapById(groupId,MapUtils.put("ascentLockState",1).getMap(),SxStoreLocationGroup.class);
    }

    /**
     * 解除货位锁定
     *
     * @param locationId
     * @throws Exception
     */
    @Override
//    @Transactional
    public void unlock(int locationId) throws Exception {
        //取消四周锁
        storeLocationRelationMapper.unLockLocation(locationId);
        //取消货位组深位锁
        SxStoreLocation location = sxStoreLocationMapper.findById(locationId,SxStoreLocation.class);
        int groupId = location.getStoreLocationGroupId();
        sxStoreLocationGroupMapper.updateMapById(groupId,MapUtils.put("ascentLockState",0).getMap(),SxStoreLocationGroup.class);
    }

    /**
     * 判断货位是否被锁定
     *
     * @param locationId
     * @throws Exception
     */
    @Override
    public boolean isUnlock(int locationId) throws Exception {
        SxStoreLocation location = sxStoreLocationMapper.findById(locationId,SxStoreLocation.class);
        if(location==null){
            throw new RuntimeException("找不到货位("+locationId+")");
        }

        int groupId = location.getStoreLocationGroupId();
        SxStoreLocationGroup group = sxStoreLocationGroupMapper.findById(groupId,SxStoreLocationGroup.class);
        if(group==null){
            throw new RuntimeException("找不到货位组("+groupId+")");
        }

        if(group.getAscentLockState()==1 || group.getIsLock()==1){
            return false;
        }

        List<SxStoreLocationRelation> rs = storeLocationRelationMapper.findByMap(MapUtils.put("locationChildId",locationId).put("locationLock",1).getMap(),SxStoreLocationRelation.class);
        if(rs.size()>0)
            return false;


        return true;
    }

    /**
     * 判断货位是否被锁定，通过料箱号
     *
     * @param containerNo
     * @throws Exception
     */
    @Override
    public boolean isUnlock(String containerNo) throws Exception {
       List<SxStore> list = storeMapper.findByMap(MapUtils.put("containerNo",containerNo).getMap(),SxStore.class);
       if(list.size()==0)
           throw new RuntimeException("容器("+containerNo+")找不到库存");

       return this.isUnlock(list.get(0).getStoreLocationId());
    }

    @Override
    public void unlockRelate(String storeNo) throws Exception {
        List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(MapUtils.put("storeNo", storeNo).getMap(), SxStoreLocation.class);
        if (sxStoreLocations.size() == 0)
            throw new Exception("找不到该货位信息");
        sxStoreLocations.get(0).setException(true);
        sxStoreLocationMapper.update(sxStoreLocations.get(0));
        List<SxStoreLocationGroup> sxStoreLocationGroups = sxStoreLocationGroupMapper.findByMap(MapUtils.put("id", sxStoreLocations.get(0).getStoreLocationGroupId()).getMap(), SxStoreLocationGroup.class);
        sxStoreLocationGroups.get(0).setIsLock(1);
        sxStoreLocationGroupMapper.update(sxStoreLocationGroups.get(0));
        //接四周锁
        storeLocationRelationMapper.unLockLocation(sxStoreLocations.get(0).getId());
    }

    @Override
    public List<SxStoreLocation> findAvailableLocationByLayer(int layer) {
        return sxStoreLocationMapper.findAvailableLocationByLayer(layer);
    }

    @Override
    public void update(SxStoreLocation sxStoreLocation) {
        sxStoreLocationMapper.update(sxStoreLocation);
    }


    public int getGoodsLayers (List < Integer > layers,int[] goodsIds){
        List<Integer> goodsLayers = findBestGoodsLayers(layers, goodsIds);
        if (goodsLayers.size() < 1) {
            //寻找料箱最少的层
            List<Integer> containerLayers = this.findBestGoodsLayers(layers, null);
            if (containerLayers.size() < 1) {
                return layers.get(0);
            } else {
                return containerLayers.get(0);
            }
        } else if (goodsLayers.size() == 1) {
            return goodsLayers.get(0);
        } else {
            //找层料箱数最少的层
            List<Integer> containerLayers = this.findBestGoodsLayers(goodsLayers, null);
            if (containerLayers.size() < 1) {
                return goodsLayers.get(0);
            } else {
                return containerLayers.get(0);
            }
        }
    }


    /**
     * 寻找任务最少的层
     * @return
     */
    public List<Integer> findLayerByMinTaskCount(List<Integer> layers, Map<Integer, List<StoreTaskDto>> tempTaskList, int type) {
        List<Integer> res = new ArrayList<>();
        List<LayerStoreTaskDto> taskList = new ArrayList<>();
        if (type == 1) {
            tempTaskList.forEach((k, v) -> {
                List<StoreTaskDto> collect = v.stream().filter(x -> x.getTaskType() == 60).collect(Collectors.toList());
                LayerStoreTaskDto ltask = new LayerStoreTaskDto();
                ltask.setLayer(k);
                ltask.setStoreTasks(collect);
                taskList.add(ltask);
            });
        } else {
            tempTaskList.forEach((k, v) -> {
                LayerStoreTaskDto ltask = new LayerStoreTaskDto();
                ltask.setLayer(k);
                ltask.setStoreTasks(v);
                taskList.add(ltask);
            });
        }

        com.prolog.eis.util.CollectionUtils.sort(taskList, x -> x.getStoreTasks().size(), true);

        int count = 0;
        List<Integer> temp = new ArrayList<>();
        List<Integer> temp2 = new ArrayList<>();
        temp.addAll(layers);
        temp2.addAll(layers);
        for (LayerStoreTaskDto layerStoreTaskDto : taskList) {
            if (layerStoreTaskDto.getStoreTasks().size()>1){
                temp2.remove(Integer.valueOf(layerStoreTaskDto.getLayer()));
            }
            if (temp.contains(layerStoreTaskDto.getLayer())){
                temp.remove(Integer.valueOf(layerStoreTaskDto.getLayer()));
            }
        }
        // 7.28 修改 如果有没有任务的层,那么直接返回
        if (temp.size()>0){
            res.addAll(temp);
            return res;
        }
        // 7.28 修改 如果每层任务数都大于1的 ,直接返回可用层
        if (temp2.size() == 0){
            res.addAll(layers);
            return res;
        }else {
            //如果有没有大于1的,那么计算任务数
            for (int i = 0; i < taskList.size(); i++) {
                if (temp2.contains(Integer.valueOf(taskList.get(i).getLayer()))) {
//                    if (layers.contains(Integer.valueOf(taskList.get(i).getLayer()))) {
                    if (count == 0) {
                        count = taskList.get(i).getStoreTasks().size();
                        res.add(taskList.get(i).getLayer());
                    } else {
                        if (count == taskList.get(i).getStoreTasks().size()) {
                            res.add(taskList.get(i).getLayer());
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        return res;
    }

    /**
     * 找商品料箱数最少的层
     * @param layers
     * @param goodsIds
     * @return
     */
    private List<Integer> findBestGoodsLayers (List < Integer > layers,int[] goodsIds){
        List<LayerContainerCountDto> ctlist = null;
        if (goodsIds == null || goodsIds.length == 0) {
            ctlist = containerService.getContainerCountPerLayer();
        } else {
            ctlist = containerService.getContainerCountPerLayer(goodsIds);
        }
        List<Integer> res = new ArrayList<>();
        if (ctlist.size() == 0)
            return res;

        com.prolog.eis.util.CollectionUtils.sort(ctlist, x -> x.getContainerCount(), true);

        int count = 0;
        for (int i = 0; i < ctlist.size(); i++) {
            if (layers.contains(ctlist.get(i).getLayer())) {
                if (count == 0) {
                    count = ctlist.get(i).getContainerCount();
                    res.add(ctlist.get(i).getLayer());
                } else {
                    if (count == ctlist.get(i).getContainerCount()) {
                        res.add(ctlist.get(i).getLayer());
                    } else {
                        break;
                    }
                }
            }
        }

        return res;
    }


    public List<Integer> findAvailableLayer(){
        return sxStoreLocationMapper.findAvailableLayer();
    }



}
