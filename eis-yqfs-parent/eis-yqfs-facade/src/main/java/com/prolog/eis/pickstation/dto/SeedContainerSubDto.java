package com.prolog.eis.pickstation.dto;

/**
 * @author SunPP and Rod Johnson
 * @date 2020/07/01 18:06
 */
public class SeedContainerSubDto {

    private String containerSubNo;
    private String containerNo;
    private String commodityId;
    private String commodityNo;
    private String goodsName;
    private String barCode;
    private int bindingNum;
    private int layoutType;
    private int orderId;
    private int picking;
    private int actualNum;
    private int isFinish;
    private int subNum;

    public int getSubNum() {
        return subNum;
    }

    public void setSubNum(int subNum) {
        this.subNum = subNum;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getBindingNum() {
        return bindingNum;
    }

    public void setBindingNum(int bindingNum) {
        this.bindingNum = bindingNum;
    }



    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPicking() {
        return picking;
    }

    public void setPicking(int picking) {
        this.picking = picking;
    }

    public int getActualNum() {
        return actualNum;
    }

    public void setActualNum(int actualNum) {
        this.actualNum = actualNum;
    }
}
