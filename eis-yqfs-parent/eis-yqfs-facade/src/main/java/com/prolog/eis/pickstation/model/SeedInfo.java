package com.prolog.eis.pickstation.model;


import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("播种详情")
@Table("seed_info")
public class SeedInfo {

  @Column("id")
  @ApiModelProperty("id")
  @AutoKey(type = AutoKey.TYPE_IDENTITY)
  private Integer id;

  @ApiModelProperty("料箱号")
  @Column("container_no")
  private String containerNo;

  @ApiModelProperty("订单号")
  @Column("order_id")
  private int orderId;

  @ApiModelProperty("订单明细号")
  @Column("order_mx_id")
  private int orderMxId;

  @ApiModelProperty("订单箱号")
  @Column("order_box_no")
  private String orderBoxNo;

  @ApiModelProperty("站台号")
  @Column("station_id")
  private int stationId;

  @ApiModelProperty("播种数量")
  @Column("num")
  private int num;

  @ApiModelProperty("创建时间")
  @Column("create_time")
  private Date createTime;

  public SeedInfo(Integer id, String containerNo, int orderId, int orderMxId, String orderBoxNo, int stationId, int num, Date createTime) {
    this.id = id;
    this.containerNo = containerNo;
    this.orderId = orderId;
    this.orderMxId = orderMxId;
    this.orderBoxNo = orderBoxNo;
    this.stationId = stationId;
    this.num = num;
    this.createTime = createTime;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getContainerNo() {
    return containerNo;
  }

  public void setContainerNo(String containerNo) {
    this.containerNo = containerNo;
  }


  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }


  public int getOrderMxId() {
    return orderMxId;
  }

  public void setOrderMxId(int orderMxId) {
    this.orderMxId = orderMxId;
  }


  public String getOrderBoxNo() {
    return orderBoxNo;
  }

  public void setOrderBoxNo(String orderBoxNo) {
    this.orderBoxNo = orderBoxNo;
  }


  public int getStationId() {
    return stationId;
  }

  public void setStationId(int stationId) {
    this.stationId = stationId;
  }


  public int getNum() {
    return num;
  }

  public void setNum(int num) {
    this.num = num;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

}
