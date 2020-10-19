package com.prolog.eis.service.pd;

import com.prolog.eis.dto.pd.PdCompleteParam;
import com.prolog.eis.dto.pd.PdDwContainerDto;
import com.prolog.eis.dto.pd.PdRecord;
import com.prolog.eis.service.pd.impl.PdExecuteServiceImpl;

import java.util.List;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/6/13 9:24
 */
public interface PdExecuteService {


    /**
     * 料箱到达站台 调用前台接口推送数据
     *
     * @param containerNo 容器编号
     * @param stationId   站台Id
     * @throws Exception
     */
    void PdDw(String containerNo, int stationId, String locationNo) throws Exception;



    /**
     * 盘点确认
     *
     * @param containerNo    容器Id
     * @param containerSubNo 子容器Id
     * @param containerSubNo 真实数量
     * @throws Exception
     */
    String pdComplete(String containerNo, String containerSubNo, int storeCount) throws Exception;

    /**
     * 初始化站台数据，可能存在正在盘点的料箱数据
     *
     * @param stationId 站台Id
     * @throws Exception
     */
    List<PdDwContainerDto> init(int stationId) throws Exception;

    /**
     * 料箱离开
     *
     * @param containerNo
     * @param stationId
     * @throws Exception
     */
    void lxLeave(String containerNo, int stationId) throws Exception;

    public List<PdRecord> selectPdHis(String goodsNo, String containerSubNo);

}
