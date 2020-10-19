package com.prolog.eis.service.masterbase;



import com.prolog.eis.dto.hxdispatch.StationLxPositionHxDto;
import com.prolog.eis.dto.yqfs.ContainerSubInfoDto;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.pickstation.dto.HxContainerInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ContainerSubService {
    /**
     * 查50条无用容器
     * @return
     */
    List<ContainerSub> findContainerSubInfo();


    List<ContainerSub> fiadAll();

    /**
     * 查有效容器信息
     * @return
     */
    List<ContainerSubInfoDto> findValidContainerSubInfo();

    /**
     * 更新容器信息
     */
    void updateContainerSubInfo(List<ContainerSub> containerSubInfos) throws Exception;

    /**
     * 更新货格
     * @param containerSub
     * @throws Exception
     */
    void update(ContainerSub containerSub) throws Exception;


    List<String> findContainerSubNo(int goodsId, String containerNo);
    ContainerSub findById(String containSubNo);

    /**
     * 根据容器生成货格
     */
    void createContainerSubNo() throws Exception;


    /**
     * 设置货格商品状态
     * @param disable
     * @param conatinerSubNo
     * @throws Exception
     */
    void setDisable(boolean disable,String ...conatinerSubNo) throws Exception;


    List<StationLxPositionHxDto> getHxContainerInfo(int goodsId, String containerNo);

    /**
     * 根据料箱编号更新料箱货格信息
     * @param containerNo
     * @throws Exception
     */
    void updateContainerSubByContainer(String containerNo) throws Exception;

    List<HxContainerInfoDto> findHxContainerSub(String containerNo);

    List<Integer> findByMap(Map map);
}
