package com.prolog.eis.pickstation.dto;

import java.util.List;
import java.util.Objects;

/**
 * @Author
 * @Description 当前播种相关信息
 * @CreateTime 2020/6/13 17:22
 */
public class SendCurrentSeedDto {

    /**
     *
     */
    private int id;

    /**
     *  货格号
     */
    private String containerSubNo;

    /**
     *  料箱号
     */
    private String containerNo;

    /**
     *  商品名
     */
    private String goodsName;

    /**
     *  商品条码
     */
    private List<String> goodsBarCode;

    /**
     *  播种数量
     */
    private int count;

    /**
     *  订单箱号
     */
    private String orderBox;

    public SendCurrentSeedDto() {
        super();
    }

    public SendCurrentSeedDto(int id, String containerSubNo, String containerNo, String goodsName, List<String> goodsBarCode, int count, String orderBox) {
        this.id = id;
        this.containerSubNo = containerSubNo;
        this.containerNo = containerNo;
        this.goodsName = goodsName;
        this.goodsBarCode = goodsBarCode;
        this.count = count;
        this.orderBox = orderBox;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public List<String> getGoodsBarCode() {
        return goodsBarCode;
    }

    public void setGoodsBarCode(List<String> goodsBarCode) {
        this.goodsBarCode = goodsBarCode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getOrderBox() {
        return orderBox;
    }

    public void setOrderBox(String orderBox) {
        this.orderBox = orderBox;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendCurrentSeedDto that = (SendCurrentSeedDto) o;
        return id == that.id &&
                count == that.count &&
                Objects.equals(containerSubNo, that.containerSubNo) &&
                Objects.equals(containerNo, that.containerNo) &&
                Objects.equals(goodsName, that.goodsName) &&
                Objects.equals(goodsBarCode, that.goodsBarCode) &&
                Objects.equals(orderBox, that.orderBox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, containerSubNo, containerNo, goodsName, goodsBarCode, count, orderBox);
    }

    @Override
    public String toString() {
        return "SendCurrentSeedDto{" +
                "id=" + id +
                ", containerSubNo='" + containerSubNo + '\'' +
                ", containerNo='" + containerNo + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsBarCode=" + goodsBarCode +
                ", count=" + count +
                ", orderBox='" + orderBox + '\'' +
                '}';
    }
}
