package com.prolog.eis.pickstation.service;


import com.prolog.eis.dto.hxdispatch.StationLxPositionHxDto;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.pickstation.dto.SendLxDto;
import com.prolog.eis.pickstation.dto.StationInfoDto;
import com.prolog.eis.pickstation.dto.StationInfoLxDto;
import com.prolog.eis.pickstation.model.StationLxPosition;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IStationHxService {
    /**
     * 计算合箱
     * @param sourceContainerSubNo 原子容器
     * @param targetContainerSubNo 目标子容器
     * @param commodityNum 需转移商品数量
     * @throws Exception
     */
    void calculateHx(String sourceContainerSubNo,String targetContainerSubNo,int commodityNum) throws Exception;

    /**
     * 初始化拣选站合箱页面
     * @param stationId 站台id
     * @throws Exception
     */
    StationInfoDto initHxDate(int stationId) throws Exception;

    /**
     * 料箱放行
     * @param containerNo 容器编号
     * @param stationId 站台id
     * @throws Exception
     */
    void lxLeave(String containerNo,int stationId) throws Exception;

    /**
     * 合箱汇总及明细转历史
     * @param id 合箱汇总id
     */
    void hxToHistory(int id) throws Exception;

    /**
     * 得到拣选站台料箱的灯光编号
     * @param stationLxPositions 站台箱位料箱
     * @param goodsId 商品id
     * @return
     * @throws Exception
     */
    String[] getStationLxLightsNo(List<StationLxPosition> stationLxPositions,int goodsId) throws Exception;

    /**
     * 校验两个货格能否合箱
     * @param sourceContainerSub 原子容器货格
     * @param targetContainerSub 目标子容器货格
     * @throws Exception
     */
    void hxContainerSubCheck(ContainerSub sourceContainerSub, ContainerSub targetContainerSub) throws Exception;


    /**
     * 前端请求亮灯
     * @param sourceContainerSubNo 原子容器货格
     * @param targetContainerSubNo 目标子容器货格
     * @throws Exception
     */
    void hxLight(String sourceContainerSubNo,String targetContainerSubNo) throws Exception;

    /**
     * 查询站台数据合箱数据
     * @param stationId 站台id
     * @throws Exception
     */
    StationInfoDto findHxInfo(int stationId) throws Exception;


    StationInfoLxDto getHxContainerInfo( StationLxPositionHxDto stationLxPositionHxDto,String container) throws Exception;

    /**
     * 查询站台编号
     */
    List<String> getStationNoByContainerNo(String containerNo) throws Exception;
    /**
     * 推送数据
     */
    void sendHxToClient(int stationId) throws Exception;
}
