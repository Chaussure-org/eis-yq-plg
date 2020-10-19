package com.prolog.eis.dto.pickorder;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/7/18 21:07
 */
public class PickOrderMxDto {

    @ApiModelProperty("商品id")
    private int commodityId;

    @ApiModelProperty("商品名称")
    private String commodityName;

    @ApiModelProperty("条码")
    private String barCode;

    @ApiModelProperty("计划数量")
    private int planNum;

    @ApiModelProperty("实际数量")
    private int actualNum;

    @ApiModelProperty("订单明细id")
    private int orderMxId;

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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

    public int getOrderMxId() {
        return orderMxId;
    }

    public void setOrderMxId(int orderMxId) {
        this.orderMxId = orderMxId;
    }
}
