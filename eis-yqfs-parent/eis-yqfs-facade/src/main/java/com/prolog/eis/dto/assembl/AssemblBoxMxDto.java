package com.prolog.eis.dto.assembl;

import java.util.Date;

/**
 * 合箱明细查询dto
 */
public class AssemblBoxMxDto {
    //合箱汇总id
   private int assemblBoxHzId;

   private String containerNo;

   private String containerSubNo;

   private String goodsNo;

   private String goodsName;

   private int layer;

   private int x;

   private int y;

   private int commodityNum;

   private Date expiryDate;

   private Date inboundTime;

   private int referenceNum;
   private int taskType;

    public AssemblBoxMxDto() {
    }

    public AssemblBoxMxDto(int assemblBoxHzId, String containerNo, String containerSubNo, String goodsNo, String goodsName, int layer, int x, int y, int commodityNum, Date expiryDate, Date inboundTime, int referenceNum, int taskType) {
        this.assemblBoxHzId = assemblBoxHzId;
        this.containerNo = containerNo;
        this.containerSubNo = containerSubNo;
        this.goodsNo = goodsNo;
        this.goodsName = goodsName;
        this.layer = layer;
        this.x = x;
        this.y = y;
        this.commodityNum = commodityNum;
        this.expiryDate = expiryDate;
        this.inboundTime = inboundTime;
        this.referenceNum = referenceNum;
        this.taskType = taskType;
    }

    public int getAssemblBoxHzId() {
        return assemblBoxHzId;
    }

    public void setAssemblBoxHzId(int assemblBoxHzId) {
        this.assemblBoxHzId = assemblBoxHzId;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(int commodityNum) {
        this.commodityNum = commodityNum;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getInboundTime() {
        return inboundTime;
    }

    public void setInboundTime(Date inboundTime) {
        this.inboundTime = inboundTime;
    }

    public int getReferenceNum() {
        return referenceNum;
    }

    public void setReferenceNum(int referenceNum) {
        this.referenceNum = referenceNum;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }
}
