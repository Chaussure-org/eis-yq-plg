package com.prolog.eis.service.monitor;

import com.prolog.eis.dto.store.ZtBoxMxDto;
import com.prolog.eis.dto.store.ZtBoxStatisDto;
import com.prolog.eis.dto.store.ZtStationStatisDto;

import java.util.List;

/**
 * @author panteng
 * @description: 监控
 * @date 2020/5/9 9:31
 */
public interface YqMonitorService {
    /**
     * 在途料箱监控(任务数查询)
     * @return
     */
    ZtBoxStatisDto boxTasks();

    /**
     * 在途料箱监控(站台统计)
     * @return
     */
    List<ZtStationStatisDto> station();

    /**
     * 在途料箱监控(在途料箱详情)
     * @return
     */
    List<ZtBoxMxDto> ztBoxMx();


    /**
     * 删除在途及业务数据
     *
     * @param containerNo
     */
    void deleteContainerNo(String containerNo) throws Exception;


    /**
     *删此料箱任务
     */
    void deleteContainerNoTask(int taskType,String containerNo) throws Exception;
}
