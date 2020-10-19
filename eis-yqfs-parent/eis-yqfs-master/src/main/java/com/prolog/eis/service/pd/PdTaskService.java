package com.prolog.eis.service.pd;

import java.util.List;

import com.prolog.eis.dto.pd.PdTaskDetailDto;
import com.prolog.eis.model.pd.PdTaskDetail;

public interface PdTaskService {

    void savepdDetail(String remark, List<PdTaskDetailDto> spList) throws Exception;

    //取消任务
    void cancelTask(int taskId) throws Exception;



    //下发任务
    void publishTask(List<Integer> pdTaskIds) throws Exception;

    //下发的校验
    void checkPublishTask(String containerNo) throws Exception;

    //获取能下发的盘点的商品
    List<PdTaskDetail> getPdTaskDetail();

    /**
     * 更新盘点任务状态
     * @param id
     * @param status
     */
    void updateStatus(int id,int status)  throws Exception;

    /**
     * 根据容器编号更新盘点任务状态
     * @param containerNo
     * @param status
     */
    void updateStatusByContainerNo(String containerNo,int status)  throws Exception;

}
