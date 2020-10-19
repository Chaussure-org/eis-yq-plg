package com.prolog.eis.dto.pd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/6/15 9:29
 */
public class PdDwContainerSubDto {

    //子容器号
    private String containerSubNo;

    //业主名称
    private String ownerName;

    //商品名称
    private String goodsName;

    //商品数量
    private Integer goodsNum;

    //商品Id
    private int goodsId;

    //有效日期
    private Date expiryDate;

    //有效期
    private String expiryDateStr;

    //商品条码
    private List<String> barCode = new ArrayList<String>();


    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryDateStr() {
        return expiryDateStr;
    }

    public void setExpiryDateStr(String expiryDateStr) {
        this.expiryDateStr = expiryDateStr;
    }

    public List<String> getBarCode() {
        return barCode;
    }

    public void setBarCode(List<String> barCode) {
        this.barCode = barCode;
    }
}
