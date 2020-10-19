package com.prolog.eis.dto.store;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author panteng
 * @description: 在途料箱监控
 * @date 2020/5/8 20:10
 */
public class ZtStationStatisDto {

    @ApiModelProperty("站台编号")
    private int stationId;

    @ApiModelProperty("料箱数量")
    private int boxs;

    @ApiModelProperty("是否索取：1、索取订单，2、不索取任务 3 索取盘点任务")
    private int isClaim;

    @ApiModelProperty("是否锁定：1、锁定，2、不锁定'")
    private int isLock;

    @ApiModelProperty("站台作业类型 0:空闲  1:拣选作业  2 盘点作业 3合箱作业")
    private int stationTaskType;

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getBoxs() {
        return boxs;
    }

    public void setBoxs(int boxs) {
        this.boxs = boxs;
    }

    public int getIsClaim() {
        return isClaim;
    }

    public void setIsClaim(int isClaim) {
        this.isClaim = isClaim;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public int getStationTaskType() {
        return stationTaskType;
    }

    public void setStationTaskType(int stationTaskType) {
        this.stationTaskType = stationTaskType;
    }
}
