package com.prolog.eis.dto.hxdispatch;

/**
 * 合箱亮灯DTO
 */
public class StationLxPositionHxDto {

    private String containerSubNo;

    private int layoutType;

    private String positionNo;

    private String containerDirection;

    private int stationId;

    public StationLxPositionHxDto() {
    }

    public StationLxPositionHxDto(String containerSubNo, int layoutType, String positionNo, String containerDirection, int stationId) {
        this.containerSubNo = containerSubNo;
        this.layoutType = layoutType;
        this.positionNo = positionNo;
        this.containerDirection = containerDirection;
        this.stationId = stationId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getContainerDirection() {
        return containerDirection;
    }

    public void setContainerDirection(String containerDirection) {
        this.containerDirection = containerDirection;
    }
}
