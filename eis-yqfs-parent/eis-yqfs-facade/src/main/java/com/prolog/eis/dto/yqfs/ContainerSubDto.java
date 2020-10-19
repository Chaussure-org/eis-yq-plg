package com.prolog.eis.dto.yqfs;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author panteng
 * @description:
 * @date 2020/4/23 10:26
 */
public class ContainerSubDto {

    @ApiModelProperty("子容器编号")
    private String containerSubNo;

    @ApiModelProperty("容器编号")
    private String containerNo;

    @ApiModelProperty("商品Id")
    private Integer commodityId;

    @ApiModelProperty("商品数量")
    private Integer commodityNum;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品条码")
    private String goodsBarCode;

    @ApiModelProperty("容器布局类型（1.整箱、2.日字、3.田字）")
    private int layoutType;

    @ApiModelProperty("是否需要补货（0不需要、1需要）")
    private boolean needState;

    @ApiModelProperty("商品编号")
    private String goodsNo;

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public boolean isNeedState() {
        return needState;
    }

    public void setNeedState(boolean needState) {
        this.needState = needState;
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

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public Integer getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(Integer commodityNum) {
        this.commodityNum = commodityNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsBarCode() {
        return goodsBarCode;
    }

    public void setGoodsBarCode(String goodsBarCode) {
        this.goodsBarCode = goodsBarCode;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }
}

