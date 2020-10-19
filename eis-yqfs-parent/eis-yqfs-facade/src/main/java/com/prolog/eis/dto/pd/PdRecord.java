package com.prolog.eis.dto.pd;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/7/12 22:35
 */
public class PdRecord {

    @ApiModelProperty("商品编号")
    private String goodsNo;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品数量")
    private int originalCount;
    @ApiModelProperty("修改后的数量")
    private int modifyCount;
    @ApiModelProperty("修改的时间")
    private Date updateDate;
    @ApiModelProperty("盘点类型")
    private int pdType;
    @ApiModelProperty("差值")
    private int difCount;

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

    public int getOriginalCount() {
        return originalCount;
    }

    public void setOriginalCount(int originalCount) {
        this.originalCount = originalCount;
    }

    public int getModifyCount() {
        return modifyCount;
    }

    public void setModifyCount(int modifyCount) {
        this.modifyCount = modifyCount;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getDifCount() {
        return difCount;
    }

    public int getPdType() {
        return pdType;
    }

    public void setPdType(int pdType) {
        this.pdType = pdType;
    }

    public void setDifCount(int difCount) {
        this.difCount = difCount;
    }
}
