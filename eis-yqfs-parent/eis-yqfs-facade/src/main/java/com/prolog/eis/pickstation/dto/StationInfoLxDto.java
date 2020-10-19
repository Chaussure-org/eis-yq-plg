package com.prolog.eis.pickstation.dto;

import java.util.List;

/**
 * @author SunPP and Rod Johnson
 * @date 2020/07/01 17:06
 */
public class StationInfoLxDto {

    private int layoutType;

    List<StationInfoSubLxDto> subLxList;

    private String containerNo;

    private String containerDirection;

    private String positionNo;

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

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }
    public List<StationInfoSubLxDto> getSubLxList() {
        return subLxList;
    }

    public void setSubLxList(List<StationInfoSubLxDto> subLxList) {
        this.subLxList = subLxList;
    }
}
