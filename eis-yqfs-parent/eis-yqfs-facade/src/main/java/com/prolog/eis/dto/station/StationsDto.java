package com.prolog.eis.dto.station;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/7/19 9:53
 */
public class StationsDto {

    @ApiModelProperty("站台id")
    private int stationId;

    @ApiModelProperty("站台编号")
    private String stationNo;

    @ApiModelProperty("站台类型")
    private int stationType;

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public int getStationType() {
        return stationType;
    }

    public void setStationType(int stationType) {
        this.stationType = stationType;
    }
}
