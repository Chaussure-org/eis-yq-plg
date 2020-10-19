package com.prolog.eis.pickstation.dto;

/**
 * @author SunPP and Rod Johnson
 * @date 2020/07/01 17:13
 */
public class StationInfoDdxPositionDto {

    private String DdxNo;

    private int isCurrent;

    private int orderId;

    //当前订单框 计划播种的数量
    private int planNum;
    //当前订单框实际播种的数量
    private int actualNum;

    //标识当前订单是销售还是移库
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String positionNo;

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public int getPlanNum() {
        return planNum;
    }

    public void setPlanNum(int planNum) {
        this.planNum = planNum;
    }

    public int getActualNum() {
        return actualNum;
    }

    public void setActualNum(int actualNum) {
        this.actualNum = actualNum;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(int isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getDdxNo() {
        return DdxNo;
    }

    public void setDdxNo(String ddxNo) {
        DdxNo = ddxNo;
    }

}
