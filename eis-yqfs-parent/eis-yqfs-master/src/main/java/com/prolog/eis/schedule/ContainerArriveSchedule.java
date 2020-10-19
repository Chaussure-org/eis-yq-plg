package com.prolog.eis.schedule;

import com.prolog.eis.dto.base.ContainerDTO;
import com.prolog.eis.model.point.PointLocation;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.pickstation.service.IStationLxService;
import com.prolog.eis.service.pointlocation.IPointLocationService;
import com.prolog.eis.util.ContainerUtils;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.model.WCSCommand;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 料箱到位
 * @author chenyc
 */
@Component
public class ContainerArriveSchedule {

    @Autowired
    private IWCSCommandService commandService;
    @Autowired
    private IWCSTaskService taskService;
    @Autowired
    private IPointLocationService locationService;
    @Autowired
    private IStationLxService lxService;
    /**
     * 料箱进站调度
     * @throws Exception
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 2000)
    public void containerSchedule() throws Exception {
        try {
            requestContainer();
        } catch (Exception e) {
        }
    }


    /**
     * 1、检测各个站台的料箱位是否存在空位
     * 2、检测存在空位的站台bcr点位是否存在料箱
     * 3、生成行走任务，进料箱
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void requestContainer() throws Exception{
        List<StationLxPosition> list = lxService.getEmptyPositions();
        if(list.size()==0)
            return;

        Map<Integer,List<StationLxPosition>> map = new HashMap<>();
        list.forEach(x ->{
            List plist = map.get(x.getStationId());
            if(plist==null){
                plist = new ArrayList();
                map.put(x.getStationId(),plist);
            }else{
                plist = map.get(x.getStationId());
            }
            plist.add(x);
        });

        Iterator<Integer> itr = map.keySet().iterator();
        while(itr.hasNext()){
            int id = itr.next();
            List<StationLxPosition> values = map.get(id);

            List<StationLxPosition> dscsEmptyLocations = values.stream().sorted(Comparator.comparing(StationLxPosition::getDistributePriority).reversed()).collect(Collectors.toList());
            StationLxPosition v = dscsEmptyLocations.get(0);

            int stationId = v.getStationId();
            String position = v.getPositionDeviceNo();

            //根据站台过去料箱bcr点位
            List<PointLocation> locations  = locationService.getPointByStation(stationId,PointLocation.TYPE_CONTAINER_BCR);
            if(locations.size()==0)
                throw new RuntimeException("找不到站台("+stationId+")料箱bcr点位");

            String address = locations.get(0).getPointId();

            //寻找bcr等待的任务
            List<WCSTask> lastTasks = taskService.getByAdrress(address);
            if(lastTasks.size()!=0){
                for(WCSTask lastTask:lastTasks){

                    if(StringUtils.isBlank(lastTask.getTarget())){
                        //更新目标点
                        lastTask.setTarget(position);
                        ContainerDTO dto = ContainerUtils.parse(lastTask.getContainerNo());
                        lastTask.setContainerNo(dto.getNumber());
                        taskService.update(lastTask);
                        //发送行走命令
                        this.saveXZCommand(lastTask);
                        //预分配箱位
                        lxService.lxIn(dto.getNumber(),dto.getDirection(),v);
                    }

                }
            }
        }

    }

    private void saveXZCommand(WCSTask task) throws Exception{
        WCSCommand cmd = new WCSCommand(task.getId(),Constant.COMMAND_TYPE_XZ,task.getAddress(),task.getTarget(),task.getContainerNo());
        commandService.add(cmd);
    }

}
