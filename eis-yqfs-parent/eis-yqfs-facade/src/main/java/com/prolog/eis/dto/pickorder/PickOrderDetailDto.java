package com.prolog.eis.dto.pickorder;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/7/19 11:23
 */
public class PickOrderDetailDto {

    @ApiModelProperty("料箱号")
    private String containerNo;

    @ApiModelProperty("箱库id")
    private Integer xkStoreId;

    @ApiModelProperty("在途库存id")
    private Integer ztStoreId;

    @ApiModelProperty("订单明细id")
    private int orderMxId;

    @ApiModelProperty("是否完成")
    private int isFinish;

    @ApiModelProperty("计划数量")
    private int bindingNum;

    @ApiModelProperty("实际数量")
    private int actualNum;

    @ApiModelProperty("商品id")
    private int commodityId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("条码")
    private String goodsBarCode;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Integer getXkStoreId() {
        return xkStoreId;
    }

    public void setXkStoreId(Integer xkStoreId) {
        this.xkStoreId = xkStoreId;
    }

    public Integer getZtStoreId() {
        return ztStoreId;
    }

    public void setZtStoreId(Integer ztStoreId) {
        this.ztStoreId = ztStoreId;
    }

    public int getOrderMxId() {
        return orderMxId;
    }

    public void setOrderMxId(int orderMxId) {
        this.orderMxId = orderMxId;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public int getBindingNum() {
        return bindingNum;
    }

    public void setBindingNum(int bindingNum) {
        this.bindingNum = bindingNum;
    }

    public int getActualNum() {
        return actualNum;
    }

    public void setActualNum(int actualNum) {
        this.actualNum = actualNum;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
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
}
