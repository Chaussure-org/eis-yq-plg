package com.prolog.eis.model.order;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wangkang
 * @Description 订单箱明细
 * @CreateTime 2020/7/4 11:44
 */
@Table("order_box_mx")
public class OrderBoxMx {

    @Id
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    @Column("id")
    private int id;

    @Column("order_box_no")
    @ApiModelProperty("订单箱号")
    private String orderBoxNo;

    @Column("container_no")
    @ApiModelProperty("料箱号")
    private String containerNo;

    @Column("container_sub_no")
    @ApiModelProperty("货格号")
    private String containerSubNo;

    @Column("commodity_id")
    @ApiModelProperty("商品号")
    private int commodityId;

    @Column("commodity_num")
    @ApiModelProperty("商品数量")
    private int commodityNum;

    @Column("order_mx_id")
    @ApiModelProperty("订单明细id")
    private int orderMxId;

    public int getOrderMxId() {
        return orderMxId;
    }

    public void setOrderMxId(int orderMxId) {
        this.orderMxId = orderMxId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderBoxNo() {
        return orderBoxNo;
    }

    public void setOrderBoxNo(String orderBoxNo) {
        this.orderBoxNo = orderBoxNo;
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

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public int getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(int commodityNum) {
        this.commodityNum = commodityNum;
    }
}
