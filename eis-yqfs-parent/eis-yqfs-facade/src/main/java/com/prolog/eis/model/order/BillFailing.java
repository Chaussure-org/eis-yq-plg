package com.prolog.eis.model.order;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/8/5 19:28
 */

@Table("bill_failing")
@ApiModel("合单失败明细")
public class BillFailing {

    @Id
    @AutoKey(type=AutoKey.TYPE_IDENTITY)
    @Column("id")
    private int id;

    @Column("bill_no")
    @ApiModelProperty("清单号")
    private String billNo;

    @Column("wave_no")
    @ApiModelProperty("波次")
    private String waveNo;

    @Column("dealer_id")
    @ApiModelProperty("经销商id")
    private int dealerId;

    @Column("goods_no")
    @ApiModelProperty("商品编号")
    private String goodsNo;

    @Column("goods_name")
    @ApiModelProperty("商品名称")
    private String goodsName;

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

    public int getDealerId() {
        return dealerId;
    }

    public void setDealerId(int dealerId) {
        this.dealerId = dealerId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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
