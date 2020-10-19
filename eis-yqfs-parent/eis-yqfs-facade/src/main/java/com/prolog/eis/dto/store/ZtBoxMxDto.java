package com.prolog.eis.dto.store;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author panteng
 * @description: 在途料箱监控
 * @date 2020/5/8 20:10
 */
public class ZtBoxMxDto {

    @ApiModelProperty("站台编号")
    private int stationId;

    @ApiModelProperty("站台号")
    private String stationNo;

    @ApiModelProperty("料箱编号")
    private String containerNo;

    @ApiModelProperty("子容器编号")
    private String containerSubNo;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品数量")
    private Integer commodityNum;

    @ApiModelProperty("商品条码")
    private String goodsBarCode;

    @ApiModelProperty("在途状态（1：去往站台，2：等待播种，3：进入播种位，4：播种中，5：离开站台，6:入库中）")
    private int ztState;

    @ApiModelProperty("料箱任务类型 1 播种任务 2 盘点任务 3 退库任务 4 补货任务 5 入库换箱 6.合箱任务 7 空托盘入库任务")
    private int containerTaskType;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(Integer commodityNum) {
        this.commodityNum = commodityNum;
    }

    public String getGoodsBarCode() {
        return goodsBarCode;
    }

    public void setGoodsBarCode(String goodsBarCode) {
        this.goodsBarCode = goodsBarCode;
    }

    public int getZtState() {
        return ztState;
    }

    public void setZtState(int ztState) {
        this.ztState = ztState;
    }

    public int getContainerTaskType() {
        return containerTaskType;
    }

    public void setContainerTaskType(int containerTaskType) {
        this.containerTaskType = containerTaskType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }
}
