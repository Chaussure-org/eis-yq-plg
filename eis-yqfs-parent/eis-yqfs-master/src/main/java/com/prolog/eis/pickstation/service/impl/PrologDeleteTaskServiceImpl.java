package com.prolog.eis.pickstation.service.impl;

import com.prolog.eis.dao.assembl.AssemblBoxMxMapper;
import com.prolog.eis.dao.pd.PdTaskDetailMapper;
import com.prolog.eis.dao.pd.PdTaskMapper;
import com.prolog.eis.dao.repair.RepairPlanMxMapper;
import com.prolog.eis.dao.store.StoreMapper;
import com.prolog.eis.dao.tk.TKTaskMapper;
import com.prolog.eis.model.assembl.AssemblBoxMx;
import com.prolog.eis.model.pd.PdTask;
import com.prolog.eis.model.pd.PdTaskDetail;
import com.prolog.eis.model.repair.RepairPlanMx;
import com.prolog.eis.model.store.SxStore;
import com.prolog.eis.model.tk.TKTask;
import com.prolog.eis.pickstation.service.PrologDeleteTaskService;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/7/25 18:38
 */

@Service
public class PrologDeleteTaskServiceImpl implements PrologDeleteTaskService {

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private RepairPlanMxMapper repairPlanMxMapper;

    @Autowired
    private PdTaskDetailMapper pdTaskDetailMapper;

    @Autowired
    private AssemblBoxMxMapper assemblBoxMxMapper;

    @Autowired
    private TKTaskMapper tkTaskMapper;

    @Autowired
    private PdTaskMapper pdTaskMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(String containerNo, int type) throws Exception {

        List<SxStore> sxStores = storeMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), SxStore.class);
        if (sxStores.size() == 0) {
            throw new Exception("料箱号有误不在库存内");
        }
        SxStore sxStore = sxStores.get(0);
        if (!StringUtils.isBlank(sxStore.getTaskId())) {
            throw new Exception("料箱正在出库中");
        }
        //跟新SxStore状态数据
        sxStore.setTaskType(SxStore.TASKTYPE_NULL);
        sxStore.setStoreState(SxStore.STATE_UP);
        sxStore.setStationId(0);
        storeMapper.update(sxStore);

        //删补货、合箱、盘点、退库
        switch (type) {
            case SxStore.TASKTYPE_BH:
                repairPlanMxMapper.deleteByMap(MapUtils.put("containerNo", containerNo).getMap(), RepairPlanMx.class);
                break;
            case SxStore.TASKTYPE_PD:
                List<PdTaskDetail> pdTaskDetails = pdTaskDetailMapper.findByMap(MapUtils.put("containerNo", containerNo).put("taskState", 20).getMap(), PdTaskDetail.class);
                if (pdTaskDetails.size() == 0) {
                    throw new Exception("未下发,可直接取消盘点料箱");
                }
                int pdTaskId = pdTaskDetails.get(0).getPdTaskId();
                pdTaskDetailMapper.deleteByMap(MapUtils.put("containerNo", containerNo).getMap(), PdTaskDetail.class);
                List<PdTaskDetail> pdTaskDetailList = pdTaskDetailMapper.findByMap(MapUtils.put("pdTaskId", pdTaskId).getMap(), PdTaskDetail.class);
                if (pdTaskDetailList.size() == 0) {
                    pdTaskMapper.deleteById(pdTaskId, PdTask.class);
                }
                break;
            case SxStore.TASKTYPE_HX:
                assemblBoxMxMapper.deleteByMap(MapUtils.put("containerNo", containerNo).getMap(), AssemblBoxMx.class);
                break;
            case SxStore.TASKTYPE_TK:
                tkTaskMapper.deleteByMap(MapUtils.put("containerNo", containerNo).getMap(), TKTask.class);
                break;

        }
    }
}
