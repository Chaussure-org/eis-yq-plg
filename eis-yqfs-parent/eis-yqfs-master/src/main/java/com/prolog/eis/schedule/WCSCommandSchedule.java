package com.prolog.eis.schedule;

import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.model.point.PointLocation;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.pointlocation.IPointLocationService;
import com.prolog.eis.util.PointLockUtils;
import com.prolog.eis.wcs.common.Constant;
import com.prolog.eis.wcs.model.WCSCommand;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSCommandService;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class WCSCommandSchedule implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(WCSCommandSchedule.class);

    @Autowired
    private EisProperties properties;

    @Autowired
    private IWCSCommandService commandService;
    @Autowired
    private IPointLocationService pointService;
    @Autowired
    private IWCSTaskService taskService;
    @Autowired
    private IStationService stationService;


    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        doCommandSchedule();
    }

    /**
     * 调度方法2
     */
    public void doCommandSchedule2(){

        //List<WCSCommand> list = commandService.getAll();
        List<PointLocation> outPoints = pointService.getPointByType(PointLocation.TYPE_CONTAINER_BCR);
        List<PointLocation> inPoints = pointService.getPointByType(PointLocation.TYPE_IN);
        //目标点为料箱bcr
        for(PointLocation point:outPoints){
            List<PointLocation> pos = new ArrayList<>();
            pos.add(point);
            CmdThread2 thread = new CmdThread2(pos,1);
            thread.setName("Thread-command-send-"+point.getPointId());
            thread.start();
        }
        //目标点为料箱入库点
        for(PointLocation point:inPoints){
            List<PointLocation> pos = new ArrayList<>();
            pos.add(point);
            CmdThread2 thread = new CmdThread2(pos,2);
            thread.setName("Thread-command-send-"+point.getPointId());
            thread.start();
        }

        outPoints.addAll(inPoints);
        CmdThread2 thread = new CmdThread2(outPoints,3);
        thread.setName("Thread-command-send-others");
        thread.start();
    }

    /**
     * 调度方法2的执行方法
     * @param points
     * @param type
     */
    public void startPointThread(List<PointLocation> points,int type){
        if(points.size()<1)
            return;

        while(true){
            try {
                List<WCSCommand> olist = null;
                PointLocation point = null;
                if(type==3){
                    Stream<String> ts = points.stream().map(x->x.getPointId());
                    String[] targets = ts.toArray(String[]::new);
                    olist = commandService.getExceptTargets(targets);
                }else{
                    point = points.get(0);
                    olist = commandService.getByTarget(point.getPointId());
                }
                if(olist.size()>0){
                    int i =0;
                    if(type!=3){
                        List<WCSTask> tks =null;
                        tks = taskService.getTaskByMap(MapUtils.put("target",point.getPointId()).getMap());

                        if(type==1){
                            //出到bcr
                            i = properties.getMaxLxCountPerStation() - tks.size();
                        }else if(type==2){
                            //入库
                            i =  properties.getMaxInLxCountPerStation() - tks.size ();
                        }
                    }else {
                        i=100000000;
                    }

                    //发送命令
                    if(i>0){
                        int k=1;
                        for (WCSCommand cmd:olist){
                            if(k>i){
                                break;
                            }
                            try {
                                commandService.sendCommand(cmd);
                                Thread.sleep(200);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            k++;
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(),e);
            }
            //线程等待
            try {
                Thread.sleep(properties.getCommandExucteRate());
            }catch (Exception e0){
            }
        }
    }

    /**
     * 调度方法2的自定义线程
     */
    class CmdThread2 extends Thread{
        private List<PointLocation> points;
        private int type;

        public CmdThread2(List<PointLocation> points,int type){
            this.points = points;
            this.type=type;
        }
        @Override
        public void run() {
            startPointThread(points,type);
//            System.out.println("111111");
        }
    }


    /**
     * 调度方法1
     */
    public void doCommandSchedule(){
        int threadCount = properties.getCommandSheduleThreadCount();
        List<PointLocation> allPoints = pointService.findByMap(null);
        for(int i=0;i<threadCount;i++){
            CmdThread thread = new CmdThread(threadCount,i,allPoints);
            thread.setName("WCS-Command-Thrd-"+i);
            thread.start();
        }
    }

    /**
     * 调度方法1执行命令
     * @param threadCount
     * @param
     */
    private void execute(int threadCount,int i,List<PointLocation> allPoints){
        List<WCSCommand> list = null;
        try {
            list = commandService.getByMod(threadCount,i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(list==null || list.size()==0) {
            return;
        }

        for(WCSCommand cmd :list){
            try {
                WCSCommand c = commandService.getById(cmd.getId());
                validatePoint(c,allPoints);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * 调度方法1自定义线程
     */
    class CmdThread extends Thread{
        private int count;
        private int tid;
        private List<PointLocation> allPoints;
        public CmdThread(int count,int tid,List<PointLocation> allPoints){
            this.tid = tid;
            this.count = count;
            this.allPoints = allPoints;
        }
        @Override
        public void run() {
            while (true){
                try {
                    execute(count,tid,allPoints);
                    Thread.sleep(properties.getCommandExucteRate());
                }catch (Exception e0){
                }
            }
        }
    }

    private void validatePoint(WCSCommand command,List<PointLocation> allPoints) throws Exception{
        if(StringUtils.isBlank(command.getTarget())){
            commandService.sendCommand(command);
            return;
        }
        PointLocation point = null;
        if (command.getType() == Constant.COMMAND_TYPE_RK){
            point = findInListByTarget(command.getAddress(), allPoints);
        }else {
            point = findInListByTarget(command.getTarget(),allPoints);
        }
        if(point==null){
            return;
        }
        //按点位进行锁定
        Object lockObj = PointLockUtils.getInstance().getLock(point.getPointId());
        synchronized (lockObj){
            if(point.getPointType() == PointLocation.TYPE_CONTAINER_BCR){
                //如果目标点位是料箱进站bcr
                long count = taskService.getTaskCountByMap(MapUtils.put("target",point.getPointId()).getMap());
                if(count < properties.getMaxLxCountPerStation()){
                    commandService.sendCommand(command);
                    return ;
                }
            }else if(point.getPointType() == PointLocation.TYPE_IN){
                //如果目标点位入库口
                long count =  taskService.getTaskCountByMap(MapUtils.put("target",point.getPointId()).getMap());
                if(count < properties.getMaxInLxCountPerStation()){
                    commandService.sendCommand(command);
                    return ;
                }
            }else{
                commandService.sendCommand(command);
                return;
            }
        }
    }


    /**
     * 在集合中寻找目标点
     * @param target
     * @param allPoints
     * @return
     */
    private PointLocation findInListByTarget(String target,List<PointLocation> allPoints){
        Optional<PointLocation> opt = allPoints.stream().filter(x->x.getPointId().equals(target)).findFirst();
        return opt.isPresent()?opt.get():null;
    }
}
