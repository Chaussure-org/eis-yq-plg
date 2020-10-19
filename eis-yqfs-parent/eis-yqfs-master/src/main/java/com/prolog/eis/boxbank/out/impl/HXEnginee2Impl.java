package com.prolog.eis.boxbank.out.impl;

import com.prolog.eis.boxbank.out.HXEnginee2;
import com.prolog.eis.boxbank.out.IOutService;
import com.prolog.eis.dao.assembl.AssemblBoxHzMapper;
import com.prolog.eis.dao.assembl.AssemblBoxMxMapper;
import com.prolog.eis.model.assembl.AssemblBoxHz;
import com.prolog.eis.model.assembl.AssemblBoxMx;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.pickstation.model.Station;
import com.prolog.eis.pickstation.service.IStationService;
import com.prolog.eis.service.assembl.AssemblBoxService;
import com.prolog.eis.service.store.IStoreLocationService;
import com.prolog.eis.util.CollectionUtils;
import com.prolog.eis.wcs.model.WCSTask;
import com.prolog.eis.wcs.service.IWCSTaskService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HXEnginee2Impl implements HXEnginee2 {

    @Autowired
    private AssemblBoxHzMapper hzMapper;
    @Autowired
    private AssemblBoxMxMapper mxMapper;
    @Autowired
    private IStationService stationService;
    @Autowired
    private IWCSTaskService taskService;
    @Autowired
    private IOutService outService;
    @Autowired
    private IStoreLocationService locationService;
    @Autowired
    private AssemblBoxService assemblBoxService;

    /**
     * 查找合箱任务信息
     * 1.查找已下发的合箱任务
     * 2、先找合箱站台，如果只有一个，直接出
     * 3、如果有多个，计算出库任务 -》 入库任务 -》 最近的出库口
     */
    @Override
    @Transactional
    public void checkOut() throws Exception {
        //如果没有合箱站台，结束任务
        List<Station> stations = stationService.findByMap(MapUtils.put("stationTaskType",SxStore.TASKTYPE_HX).put("isClaim",1).put("isLock",2).getMap());
        if(stations.size()==0)
            return;

        List<Integer> stationIds = stations.stream().mapToInt(x->x.getId()).boxed().collect(Collectors.toList());
        //如果没有未完成的合箱任务，结束任务
        List<AssemblBoxHz> assemblTasks = hzMapper.findByMap(MapUtils.put("allIsOut",false).getMap(),AssemblBoxHz.class);
        if(assemblTasks.size()==0)
            return;

        //寻找执行中的合箱任务
        List<AssemblBoxHz> assemblTasks2 = assemblTasks.stream().filter(x->x.getTaskState()==AssemblBoxHz.TASK_STATE_EXECUTE).collect(Collectors.toList());
        if(assemblTasks2.size()>0){
            for(AssemblBoxHz hz : assemblTasks2){
                List<AssemblBoxMx> mxes = getMxsByHzId(hz.getId());
                if(mxes.size()>0){
                    for(AssemblBoxMx mx:mxes){
                        if(hz.getStationId()>0 && stationIds.contains(hz.getStationId()) && locationService.isUnlock(mx.getContainerNo())){
                            out(hz,mx,hz.getStationId());
                            return;
                        }
                    }
                }
            }
        }

        //寻找待下发的任务
        List<AssemblBoxHz> assemblTasks3 = assemblTasks.stream().filter(x->x.getTaskState()==AssemblBoxHz.TASK_STATE_ISSUE).collect(Collectors.toList());
        if(assemblTasks3.size()>0){
            int stationId = this.getBestStationId(stationIds);
            for(AssemblBoxHz hz : assemblTasks3){
                List<AssemblBoxMx> mxes2 = getMxsByHzId(hz.getId());
                if(mxes2.size()>0){
                    for(AssemblBoxMx mx:mxes2){
                        if(locationService.isUnlock(mx.getContainerNo())){
                            out(hz,mx,stationId);
                            return;
                        }
                    }
                }
            }

        }
    }

    /**
     * 寻找最佳站台
     * @param stationIds
     * @return
     */
    private int getBestStationId(List<Integer> stationIds){
        if(stationIds==null || stationIds.size()==0){
            return 0;
        }
        if(stationIds.size()==1){
            return stationIds.get(0);
        }
        List<WCSTask> outTasks = taskService.getOutboundTask(0);
        outTasks = outTasks.stream().filter(x->x.getStationId()!=0 && stationIds.contains(x.getStationId())).collect(Collectors.toList());
        if(outTasks==null || outTasks.size()==0){
            return stationIds.get(0);
        }

        Map<Integer,List<WCSTask>> maps = CollectionUtils.mapList(outTasks, x->x.getStationId(), x->1==1);
        int si = CollectionUtils.findSmallest(maps,x->x.size());
        return si;
    }


    /**
     * 找为出库明细
     * @param hzId
     * @return
     */
    private List<AssemblBoxMx> getMxsByHzId(int hzId){
        return mxMapper.findByMap(MapUtils.put("assemblBoxHzId",hzId).put("outed",false).getMap(),AssemblBoxMx.class);
    }


    /**
     * 出库
     * @param hz
     * @param mx
     * @param stationId
     * @throws Exception
     */
    private void out(AssemblBoxHz hz,AssemblBoxMx mx,int stationId) throws Exception{

        mx.setOuted(true);
        mx.setTaskState(AssemblBoxMx.TASK_STATE_JXZ);
        //出库前修改合箱明细料箱状态
        assemblBoxService.updateMx(mx);

        boolean isUpdate=false;
        if(hz.getTaskState()!=AssemblBoxHz.TASK_STATE_EXECUTE){
            isUpdate = true;
            hz.setTaskState(AssemblBoxHz.TASK_STATE_EXECUTE);
        }

        if(getMxsByHzId(hz.getId()).size()==0){
            hz.setAllIsOut(true);
            isUpdate=true;
        }
        if(hz.getStationId()==0){
            hz.setStationId(stationId);
            isUpdate=true;
        }
       if(isUpdate)
            assemblBoxService.update(hz);
        outService.checkOut(mx.getContainerNo(),SxStore.TASKTYPE_HX,stationId);
    }


}
