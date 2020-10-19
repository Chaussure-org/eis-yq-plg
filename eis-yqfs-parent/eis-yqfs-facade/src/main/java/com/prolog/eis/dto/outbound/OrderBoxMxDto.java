package com.prolog.eis.dto.outbound;


public class OrderBoxMxDto {

    private String orderNo;//出库清单

    private String goodsName;

    private String goodsNo;

    private int count;//商品数量

    private String orderBoxNo; // 订单箱号

    public OrderBoxMxDto(String orderNo, String goodsName, String goodsNo, int count, String orderBoxNo) {
        this.orderNo = orderNo;
        this.goodsName = goodsName;
        this.goodsNo = goodsNo;
        this.count = count;
        this.orderBoxNo = orderBoxNo;
    }

    public String getOrderBoxNo() {
        return orderBoxNo;
    }

    public void setOrderBoxNo(String orderBoxNo) {
        this.orderBoxNo = orderBoxNo;
    }

    public OrderBoxMxDto() {
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
