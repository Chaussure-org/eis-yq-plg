package com.prolog.eis.model.order;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("bill_mx")
@ApiModel("台账明细")
public class BillMx {

  @Id
  @Column("id")
  private int id;

  @Column("bill_hz_id")
  @ApiModelProperty("清单汇总id")
  private int billHzId;
  
  @Column("goods_id")
  @ApiModelProperty("商品id")
  private int goodsId;
  
  @Column("plan_num")
  @ApiModelProperty("计划数量")
  private int planNum;
  
  @Column("create_time")
  @ApiModelProperty("创建时间")
  private Date createTime;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public int getBillHzId() {
    return billHzId;
  }

  public void setBillHzId(int billHzId) {
    this.billHzId = billHzId;
  }


  public int getGoodsId() {
    return goodsId;
  }

  public void setGoodsId(int goodsId) {
    this.goodsId = goodsId;
  }


  public int getPlanNum() {
    return planNum;
  }

  public void setPlanNum(int planNum) {
    this.planNum = planNum;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

}
