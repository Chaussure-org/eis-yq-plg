package com.prolog.eis.schedule;

import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.model.StationLxPosition;
import com.prolog.eis.pickstation.model.StationOrderPosition;
import com.prolog.eis.pickstation.service.IStationDdxService;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.util.TaskUtils;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.model.WCSCommand;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.utils.DateUtils;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderBoxSchedule {

    private static final Logger logger = LoggerFactory.getLogger(OrderBoxSchedule.class);

    @Autowired
    private IWCSCommandService commandService;
    @Autowired
    private IWCSTaskService taskService;
    @Autowired
    private IStationDdxService stationDdxService;
    @Autowired
    private IStationService stationService;

    /**
     * 订单箱调度
     * 检测站台订单框点位，当为空并未发送请求任务时，请求一个订单框
     *
     * @throws Exception
     */
    @Scheduled(initialDelay = 6000, fixedDelay = 6000)
    public void orderBoxSchedule() throws Exception {
        try {
            requestOrderBox();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求料箱
     * 1、获取所有站台订单框位置信息
     * 2、过滤有订单框号的位置信息
     * 3、获取所有待执行的订单箱请求命令信息
     * 4、过滤已生成请求命令的订单框位置信息
     * 5、过滤任务信息
     * 5、根据最终的结果生成请求任务和请求命令
     *
     * @throws Exception
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public synchronized void requestOrderBox() throws Exception {

        //找出空位
        List<StationOrderPosition> positions = stationDdxService.getEmpty();
        if(positions.size()==0) {
            return;
        }

        //生成任务和命令
        for (StationOrderPosition p : positions) {
            String taskId = TaskUtils.gerenateTaskId();
            String target = p.getDeviceNo();
            //生成任务
            WCSTask task = new WCSTask();
            task.setId(taskId);
            task.setStationId(p.getStationId());
            task.setEisType(SxStore.TASKTYPE_BZ);
            task.setWcsType(Constant.TASK_TYPE_DDKJZ);
            task.setGmtStartTime(new Date());
            task.setTarget(target);
            task.setGmtCreateTime(new Date());
            taskService.add(task);
            //生成命令
            //订单箱去往地址
            logger.info(taskId+"订单箱"+target);
            List<StationOrderPosition> stationOrderPositions = stationDdxService.findByMap(MapUtils.put("deviceNo", target).getMap());
            stationOrderPositions.get(0).setStatus((short) 10);
            stationDdxService.update(stationOrderPositions.get(0));
            WCSCommand cmd = new WCSCommand(taskId, Constant.COMMAND_TYPE_REQUEST_ORDER_BOX, "", target, null);
            commandService.add(cmd);
        }
    }
}
