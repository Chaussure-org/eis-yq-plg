package com.prolog.eis.dto.yqfs;

import io.swagger.annotations.ApiModelProperty;

public class ContainerSubInfoDto {

    @ApiModelProperty("子容器编号")
    private String containerSubNo;

    @ApiModelProperty("容器编号")
    private String containerNo;

    @ApiModelProperty("商品Id")
    private Integer commodityId;

    @ApiModelProperty("商品数量")
    private Integer commodityNum;

    @ApiModelProperty("有效日期")
    private String expiryDate;

    @ApiModelProperty("入库任务明细")
    private Integer inBoundTaskDetailId;

    @ApiModelProperty("入库计划数量")
    private Integer inBoundPlanNum;
    @ApiModelProperty("货位id")
    private Integer storeLocationId;

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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getInBoundTaskDetailId() {
        return inBoundTaskDetailId;
    }

    public void setInBoundTaskDetailId(Integer inBoundTaskDetailId) {
        this.inBoundTaskDetailId = inBoundTaskDetailId;
    }

    public Integer getInBoundPlanNum() {
        return inBoundPlanNum;
    }

    public void setInBoundPlanNum(Integer inBoundPlanNum) {
        this.inBoundPlanNum = inBoundPlanNum;
    }

    public Integer getStoreLocationId() {
        return storeLocationId;
    }

    public void setStoreLocationId(Integer storeLocationId) {
        this.storeLocationId = storeLocationId;
    }
}

