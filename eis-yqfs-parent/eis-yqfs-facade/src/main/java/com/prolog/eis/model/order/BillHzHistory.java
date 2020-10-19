package com.prolog.eis.model.order;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;


@Table("bill_hz_history")
@ApiModel("清单表历史")
public class BillHzHistory {

  @Id
  @Column("id")
  private int id;

  @Column("outbound_code")
  @ApiModelProperty("出库单编号")
  private String outboundCode;
  
  @Column("bill_no")
  @ApiModelProperty("清单编号")
  private String billNo;
  
  @Column("wave_no")
  @ApiModelProperty("销售单波次号")
  private String waveNo;
  
  @Column("order_Hz_id")
  @ApiModelProperty("订单汇总id")
  private int orderHzId;
  
  @Column("loan_no")
  @ApiModelProperty("借件单号")
  private String loanNo;
  
  @Column("loan_date_time")
  @ApiModelProperty("借件单日期")
  private Date loanDateTime;
  
  @Column("expect_time")
  @ApiModelProperty("时效（YYYY-MM-DD HH:MM:SS）")
  private Date expectTime;

  @Column("priority")
  @ApiModelProperty("优先级")
  private int priority;

  @Column("dealer_id")
  @ApiModelProperty("经销商id")
  private int dealerId;

  @Column("goods_factory")
  @ApiModelProperty("发货工厂")
  private String goodsFactory;

  @Column("transfer_library")
  @ApiModelProperty("中转库")
  private String transferLibrary;

  @Column("deliver_goods_mode")
  @ApiModelProperty("发货方式")
  private String deliverGoodsMode;

  @Column("shipping_point")
  @ApiModelProperty("发运点")
  private String shippingPoint;

  @Column("store_no")
  @ApiModelProperty("仓库号")
  private String storeNo;

  @Column("deliver_goods_type")
  @ApiModelProperty("发运品种")
  private String deliverGoodsType;

  @Column("dealer_address")
  @ApiModelProperty("客户地址")
  private String dealerAddress;

  @Column("weight")
  @ApiModelProperty("总重量")
  private double weight;

  @Column("net_weight")
  @ApiModelProperty("净重")
  private double netWeight;

  @Column("money")
  @ApiModelProperty("金额")
  private double money;

  @Column("confirm_time")
  @ApiModelProperty("确认时间")
  private Date confirmTime;

  @Column("type")
  @ApiModelProperty("订单类型")
  private String type;

  @Column("crtat_staff")
  @ApiModelProperty("创建人")
  private String crtatStaff;

  @Column("deal_time")
  @ApiModelProperty("建单时间")
  private Date dealTime;

  @Column("create_time")
  @ApiModelProperty("创建时间")
  private Date createTime;

  @Column("last_date_time")
  @ApiModelProperty("最后操作时间")
  private Date lastDateTime;

  @Column("order_create_time")
  @ApiModelProperty("建单时间")
  private Date orderCreateTime;

  @Column("store_place")
  @ApiModelProperty("场地编号")
  private String storePlace;

  @Column("place")
  @ApiModelProperty("场地")
  private String place;

  public String getStorePlace() {
    return storePlace;
  }

  public void setStorePlace(String storePlace) {
    this.storePlace = storePlace;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getOutboundCode() {
    return outboundCode;
  }

  public void setOutboundCode(String outboundCode) {
    this.outboundCode = outboundCode;
  }


  public String getBillNo() {
    return billNo;
  }

  public void setBillNo(String billNo) {
    this.billNo = billNo;
  }


  public String getWaveNo() {
    return waveNo;
  }

  public void setWaveNo(String waveNo) {
    this.waveNo = waveNo;
  }


  public int getOrderHzId() {
    return orderHzId;
  }

  public void setOrderHzId(int orderHzId) {
    this.orderHzId = orderHzId;
  }


  public String getLoanNo() {
    return loanNo;
  }

  public void setLoanNo(String loanNo) {
    this.loanNo = loanNo;
  }


  public Date getLoanDateTime() {
    return loanDateTime;
  }

  public void setLoanDateTime(Date loanDateTime) {
    this.loanDateTime = loanDateTime;
  }


  public Date getExpectTime() {
    return expectTime;
  }

  public void setExpectTime(Date expectTime) {
    this.expectTime = expectTime;
  }


  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }


  public int getDealerId() {
    return dealerId;
  }

  public void setDealerId(int dealerId) {
    this.dealerId = dealerId;
  }


  public String getGoodsFactory() {
    return goodsFactory;
  }

  public void setGoodsFactory(String goodsFactory) {
    this.goodsFactory = goodsFactory;
  }


  public String getTransferLibrary() {
    return transferLibrary;
  }

  public void setTransferLibrary(String transferLibrary) {
    this.transferLibrary = transferLibrary;
  }


  public String getDeliverGoodsMode() {
    return deliverGoodsMode;
  }

  public void setDeliverGoodsMode(String deliverGoodsMode) {
    this.deliverGoodsMode = deliverGoodsMode;
  }


  public String getShippingPoint() {
    return shippingPoint;
  }

  public void setShippingPoint(String shippingPoint) {
    this.shippingPoint = shippingPoint;
  }


  public String getStoreNo() {
    return storeNo;
  }

  public void setStoreNo(String storeNo) {
    this.storeNo = storeNo;
  }


  public String getDeliverGoodsType() {
    return deliverGoodsType;
  }

  public void setDeliverGoodsType(String deliverGoodsType) {
    this.deliverGoodsType = deliverGoodsType;
  }


  public String getDealerAddress() {
    return dealerAddress;
  }

  public void setDealerAddress(String dealerAddress) {
    this.dealerAddress = dealerAddress;
  }


  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }


  public double getNetWeight() {
    return netWeight;
  }

  public void setNetWeight(double netWeight) {
    this.netWeight = netWeight;
  }


  public double getMoney() {
    return money;
  }

  public void setMoney(double money) {
    this.money = money;
  }


  public Date getConfirmTime() {
    return confirmTime;
  }

  public void setConfirmTime(Date confirmTime) {
    this.confirmTime = confirmTime;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  public String getCrtatStaff() {
    return crtatStaff;
  }

  public void setCrtatStaff(String crtatStaff) {
    this.crtatStaff = crtatStaff;
  }


  public Date getDealTime() {
    return dealTime;
  }

  public void setDealTime(Date dealTime) {
    this.dealTime = dealTime;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


  public Date getLastDateTime() {
    return lastDateTime;
  }

  public void setLastDateTime(Date lastDateTime) {
    this.lastDateTime = lastDateTime;
  }


  public Date getOrderCreateTime() {
    return orderCreateTime;
  }

  public void setOrderCreateTime(Date orderCreateTime) {
    this.orderCreateTime = orderCreateTime;
  }

}
