package com.prolog.eis.dto.outbound;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/8/9 17:19
 */
public class YkBalingBox {

    //清单号"
    private String billNo;

    //商品名称
    private String goodsName;

    //商品编号
    private String goodsNo;

    //商品数量
    private int commodityNum;

    //订单箱号
    private String orderBoxNo;

    //打包箱号
    private String balingBoxNo;

    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
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

    public int getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(int commodityNum) {
        this.commodityNum = commodityNum;
    }

    public String getOrderBoxNo() {
        return orderBoxNo;
    }

    public void setOrderBoxNo(String orderBoxNo) {
        this.orderBoxNo = orderBoxNo;
    }

    public String getBalingBoxNo() {
        return balingBoxNo;
    }

    public void setBalingBoxNo(String balingBoxNo) {
        this.balingBoxNo = balingBoxNo;
    }
}
