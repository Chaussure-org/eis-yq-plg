package com.prolog.eis.schedule;

import com.prolog.eis.boxbank.rule.CarTaskCountRule;
import com.prolog.eis.boxbank.rule.StoreLocationDTO;
import com.prolog.eis.dao.store.SxLayerMapper;
import com.prolog.eis.dao.test.RgvMapper;
import com.prolog.eis.dto.store.StoreTaskDto;
import com.prolog.eis.model.store.SxCeng;
import com.prolog.eis.service.store.IStoreService;
import com.prolog.eis.util.TaskUtils;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.dto.CarInfoDTO;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.core.annotation.Table;
import com.prolog.framework.utils.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author wangkang
 * @Description 还车调度
 * @CreateTime 2020/8/9 23:57
 */
@Component
public class CarReturnSchedule {

    private static final Logger logger = LoggerFactory.getLogger(CarReturnSchedule.class);

    @Autowired
    private IWCSService wcsService;
    @Autowired
    private IWCSTaskService taskService;
    @Autowired
    private SxLayerMapper sxLayerMapper;
    @Autowired
    private IWCSCommandService commandService;
    @Autowired
    private CarTaskCountRule carTaskCountRule;
    @Autowired
    private IStoreService storeService;
    @Autowired
    private RgvMapper rgvMapper;

    @Scheduled(initialDelay = 15000, fixedDelay = 15000)
    public synchronized void schedule(){
        List<CarInfoDTO> cars = wcsService.getCarInfo();
        //List<CarInfoDTO> cars = rgvMapper.findAll();
        try {
            toReturnCar(cars);
            getCar(cars);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //@Scheduled(initialDelay = 1000, fixedDelay = 1000)
//    public void getCar() throws Exception {
//        Thread[] threads = new Thread[100];
//        for (int i = 0; i < 10; i++) {
//            threads[i] = new Thread(()->{
//                    this.schedule();
//            });
//            threads[i].start();
//        }
//    }

    @Transactional(rollbackFor = Exception.class)
    public void getCar(List<CarInfoDTO> cars) throws Exception{
        /**
         * 1.找到无车层的集合,并遍历
         * 2.根据该层去找相应的箱库是否存在出入库任务
         * 3.判断系统中是否有一条换层任务
         * 4.有出入库任务,但没有换层任务的情况,调用最近一层没有任务的车做换层任务
         */
        List<WCSTask> wcsTasks = taskService.getTaskByMap(MapUtils.put("wcsType", Constant.COMMAND_TYPE_HC).getMap());
        if (wcsTasks.size()>0) {
            throw new Exception("已存在一个换层任务");
        }
        List<Integer> layers = cars.stream().map(CarInfoDTO::getLayer).distinct().collect(Collectors.toList());
        List<SxCeng> sxCengs = sxLayerMapper.findByMap(null, SxCeng.class);
        List<Integer> list = sxCengs.stream().map(SxCeng::getLayer).collect(Collectors.toList());
        list.removeAll(layers);
        if (list.size()>0){
            for (Integer layer : list) {
                if (storeService.getTaskCountByLayer(layer)){
                    int sourceLayer = layerRule(layer,cars);
                    if(sourceLayer != 0){
                        // 可以换层,生成任务,然后调用换层接口
                        doCrossLayer(sourceLayer,layer);
                        return;
                    }
                }
            }
        }
    }

    private int layerRule(int layer,List<CarInfoDTO> cars) {
        List<CarInfoDTO> collect = cars.stream().filter(x -> x.getStatus() == 1 || x.getStatus() == 2).collect(Collectors.toList());
        List<Integer> layers = collect.stream().map(CarInfoDTO::getLayer).collect(Collectors.toList());
        int sourceLayer = 0;
        List<StoreTaskDto> storeTasks = storeService.findStoreTasks();
        if (storeTasks.size()>0){
            Map<Integer, List<StoreTaskDto>> taskMap = com.prolog.eis.util.CollectionUtils.mapList(storeTasks, task -> task.getLayer(), task->task.getTaskType()!=0);
            for (Map.Entry<Integer, List<StoreTaskDto>> listEntry : taskMap.entrySet()) {
                layers.remove(listEntry.getKey());
            }
            if (layers.size()>0){
                sourceLayer= compute(sourceLayer, layer, layers);
                return sourceLayer;
            }else {
                return 0;
            }
        }else {
            sourceLayer = compute(sourceLayer,layer,layers);
            return sourceLayer;
        }

    }

    private int compute(int sourceLayer,int layer,List<Integer> layers){
        int diffNum = Math.abs(layers.get(0) - layer);
        for (Integer integer : layers){
            int diffNumTemp = Math.abs(integer - layer);
            if (diffNumTemp <= diffNum) {
                diffNum = diffNumTemp;
                sourceLayer = integer;
            }
        }
        return sourceLayer;
    }

    // 当某层有两台车时,优先还车
    @Transactional(rollbackFor = Exception.class)
    public  void toReturnCar(List<CarInfoDTO> cars) throws Exception {
        List<WCSTask> wcsTasks = taskService.getTaskByMap(MapUtils.put("wcsType", Constant.COMMAND_TYPE_HC).getMap());
        if (wcsTasks.size()>0) {
            return;
        }
        Map<Integer, Long> layers = cars.stream().collect(Collectors.groupingBy(e->e.getLayer(),Collectors.counting()));
        for (Map.Entry<Integer, Long> entry : layers.entrySet()) {
            if (entry.getValue()==2){
                int layer = entry.getKey();
                int layerTarget = findEmptyLayer(cars);
                if (layerTarget != 0){
                    // 判断是否有换层任务
                    doCrossLayer(layer,layerTarget);
                    break;
                }
            }
        }
    }


    // 找到无车层
    private int findEmptyLayer(List<CarInfoDTO> cars) {
        List<Integer> layers = cars.stream().map(CarInfoDTO::getLayer).distinct().collect(Collectors.toList());
        List<SxCeng> cengs = sxLayerMapper.findByMap(null, SxCeng.class);
        for (SxCeng ceng : cengs) {
            if (!layers.contains(ceng.getLayer())){
                return ceng.getLayer();
            }
        }
        return 0;
    }

    @Transactional
    public void doCrossLayer(int sourceLayer, int layer) throws Exception {
        // 锁层
        SxCeng layer1 = new SxCeng();
        layer1.setLayer(layer);
        layer1.setLockState(1);
        sxLayerMapper.updateByLayer(layer1);
        SxCeng layer2 = new SxCeng();
        layer2.setLayer(sourceLayer);
        layer2.setLockState(1);
        sxLayerMapper.updateByLayer(layer2);
        // 记录并发起换层任务
        String taskId = TaskUtils.gerenateTaskId();
        WCSTask task = new WCSTask();
        task.setId(taskId);
        task.setEisType(Constant.TASK_TYPE_HC);
        task.setWcsType(Constant.COMMAND_TYPE_HC);
        task.setStatus(WCSTask.STATUS_WAITTING);
        task.setAddress(String.valueOf(sourceLayer));
        task.setTarget(String.valueOf(layer));
        task.setGmtCreateTime(new Date());
        taskService.add(task);
        boolean b = commandService.sendHCCommand(taskId,sourceLayer,layer);
        if (!b){
            throw new Exception("换层任务"+sourceLayer+"到"+layer+"发送不成功");
        }
    }

    public synchronized void changeCarToLayer(int source, int target) throws Exception {
        List<WCSTask> wcsTasks = taskService.getTaskByMap(MapUtils.put("wcsType", Constant.COMMAND_TYPE_HC).getMap());
        if (wcsTasks.size()>0) {
            throw new Exception("已存在一个换层任务");
        }
        List<CarInfoDTO> cars = wcsService.getCarInfo();
        List<CarInfoDTO> collect = cars.stream().filter(x -> x.getLayer() == source&& x.getStatus() != 3&& x.getStatus() != 4).collect(Collectors.toList());
        if (collect.size()==0) {
            throw new Exception("源层"+source+"没有可用车辆");
        }
        List<CarInfoDTO> carInfoDTOS = cars.stream().filter(x -> x.getLayer() != target).collect(Collectors.toList());
        if (storeService.getTaskCountByLayer(source)&&carInfoDTOS.size()==0){
            // 可以换层,生成任务,然后调用换层接口
            doCrossLayer(source,target);
        }
    }
}

