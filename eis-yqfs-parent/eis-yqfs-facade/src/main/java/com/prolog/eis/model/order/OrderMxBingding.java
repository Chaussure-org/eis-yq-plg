package com.prolog.eis.model.order;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/7/25 12:06
 */

@Table("order_mx_bingding")
public class OrderMxBingding {

    @Column("order_mx_id")
    @ApiModelProperty("订单明细Id")
    private int orderMxId;


    @Column("container_no")
    @ApiModelProperty("容器编号")
    private String containerNo;

    @Column("container_sub_no")
    @ApiModelProperty("容器编号")
    private String containerSubNo;

    public int getOrderMxId() {
        return orderMxId;
    }

    public void setOrderMxId(int orderMxId) {
        this.orderMxId = orderMxId;
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
}
