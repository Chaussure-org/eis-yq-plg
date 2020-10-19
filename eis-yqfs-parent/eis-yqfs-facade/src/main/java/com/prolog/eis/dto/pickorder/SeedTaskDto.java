package com.prolog.eis.dto.pickorder;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/8/31 16:14
 */
public class SeedTaskDto {

    private Integer orderId;

    private Integer orderBoxNo;

    private String containerNo;

    private String carStatus;

    private String lineStatus;

    private String seedStatus;

    private String goodsName;

    private String goodsNo;

    private String specification;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderBoxNo() {
        return orderBoxNo;
    }

    public void setOrderBoxNo(Integer orderBoxNo) {
        this.orderBoxNo = orderBoxNo;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getLineStatus() {
        return lineStatus;
    }

    public void setLineStatus(String lineStatus) {
        this.lineStatus = lineStatus;
    }

    public String getSeedStatus() {
        return seedStatus;
    }

    public void setSeedStatus(String seedStatus) {
        this.seedStatus = seedStatus;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    @Override
    public String toString() {
        return "SeedTaskDto{" +
                "orderId=" + orderId +
                ", orderBoxNo=" + orderBoxNo +
                ", containerNo='" + containerNo + '\'' +
                ", carStatus='" + carStatus + '\'' +
                ", lineStatus='" + lineStatus + '\'' +
                ", seedStatus='" + seedStatus + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsNo='" + goodsNo + '\'' +
                ", specification='" + specification + '\'' +
                '}';
    }
}
