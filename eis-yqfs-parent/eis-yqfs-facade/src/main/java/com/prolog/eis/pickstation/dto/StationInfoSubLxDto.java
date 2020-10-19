package com.prolog.eis.pickstation.dto;

/**
 * @author SunPP and Rod Johnson
 * @date 2020/07/01 17:07
 */
public class StationInfoSubLxDto {
    private String goodsName;

    private String subContainerNo;
    private String barCode;
    //0:不是  1：是
    private int isCurrent;
    private int seedCount;
    private int orderId;
    private int actualNum;
    private int planNum;
    private int isFinish;
    private int subNum;

    public int getSubNum() {
        return subNum;
    }

    public void setSubNum(int subNum) {
        this.subNum = subNum;
    }

    //合箱货格,默认0
    private int isIntegrationLx = 0;

    public int getIsIntegrationLx() {
        return isIntegrationLx;
    }

    public void setIsIntegrationLx(int isIntegrationLx) {
        this.isIntegrationLx = isIntegrationLx;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSubContainerNo() {
        return subContainerNo;
    }

    public void setSubContainerNo(String subContainerNo) {
        this.subContainerNo = subContainerNo;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(int isCurrent) {
        this.isCurrent = isCurrent;
    }

    public int getSeedCount() {
        return seedCount;
    }

    public void setSeedCount(int seedCount) {
        this.seedCount = seedCount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
