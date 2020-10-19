package com.prolog.eis.model.order;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wangkang
 * @Description 订单箱汇总
 * @CreateTime 2020/7/4 11:42
 */
@Table("order_box_hz")
public class OrderBoxHz {

    @Id
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    @Column("id")
    private int id;

    @Column("order_box_no")
    @ApiModelProperty("订单箱号")
    private String orderBoxNo;

    @Column("order_hz_id")
    @ApiModelProperty("订单号")
    private int orderHzId;

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

    public int getOrderHzId() {
        return orderHzId;
    }

    public void setOrderHzId(int orderHzId) {
        this.orderHzId = orderHzId;
    }
}
