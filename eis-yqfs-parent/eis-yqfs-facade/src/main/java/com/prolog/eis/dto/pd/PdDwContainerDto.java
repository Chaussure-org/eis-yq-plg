package com.prolog.eis.dto.pd;

import java.util.List;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/6/15 9:29
 */
public class PdDwContainerDto {

    //容器号
    private String containerNo;

    //容器布局类型（1.整箱、2.日字、3.田字）
    private int layoutType;

    //当前面
    private String side;

    //亮灯子容器
    private String containerSubNo;

    //站台点位
    private String positionNo;

    //当前所有子容器子容器
    List<String> containerSubNos;

    //子容器
    private List<PdDwContainerSubDto> containerSubDto;

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

    public List<PdDwContainerSubDto> getContainerSubDto() {
        return containerSubDto;
    }

    public void setContainerSubDto(List<PdDwContainerSubDto> containerSubDto) {
        this.containerSubDto = containerSubDto;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public List<String> getContainerSubNos() {
        return containerSubNos;
    }

    public void setContainerSubNos(List<String> containerSubNos) {
        this.containerSubNos = containerSubNos;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }
}
