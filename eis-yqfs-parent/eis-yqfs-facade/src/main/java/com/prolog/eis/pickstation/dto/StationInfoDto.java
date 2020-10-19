package com.prolog.eis.pickstation.dto;

import java.util.List;

/**
 * @author SunPP and Rod Johnson
 * @date 2020/07/01 17:01
 */
public class StationInfoDto {

    private int stationId;

    private List<StationInfoLxPositionDto> lxPositionList;

    private List<StationInfoDdxPositionDto> ddxPositionList;

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public List<StationInfoLxPositionDto> getLxPositionList() {
        return lxPositionList;
    }

    public void setLxPositionList(List<StationInfoLxPositionDto> lxPositionList) {
        this.lxPositionList = lxPositionList;
    }

    public List<StationInfoDdxPositionDto> getDdxPositionList() {
        return ddxPositionList;
    }

    public void setDdxPositionList(List<StationInfoDdxPositionDto> ddxPositionList) {
        this.ddxPositionList = ddxPositionList;
    }


}
