package com.prolog.eis.model.apprf;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @title 入库单明细历史表
 * @author jinxf
 * @time 2020/4/23 11:53
 */
@Table(value = "INBOUND_TASK_MX_HIS")
public class AppInboundTaskMxHisModel {

    @Id
    @ApiModelProperty("Id")
    private Integer id;

    @ApiModelProperty("入库单汇总id")
    @Column("INBOUND_TASK_HZ_ID")
    private Integer inboundTaskHzId;

    @ApiModelProperty("子容器编号")
    @Column("CONTAINER_SUB_NO")
    private String containerSubNo;

    @ApiModelProperty("商品id")
    @Column("GOODS_ID")
    private Integer goodsId;

    @ApiModelProperty("商品数量")
    @Column("GOODS_NUM")
    private Integer goodsNum;

    @ApiModelProperty("有效期（YYYY-MM-DD HH:MM:SS）")
    @Column("EXPIRY_DATE")
    private Date expiryDate;

    @ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
    @Column("CREATE_TIME")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInboundTaskHzId() {
        return inboundTaskHzId;
    }

    public void setInboundTaskHzId(Integer inboundTaskHzId) {
        this.inboundTaskHzId = inboundTaskHzId;
    }

    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AppInboundTaskMxHisModel{" +
                "id=" + id +
                ", inboundTaskHzId=" + inboundTaskHzId +
                ", containerSubNo=" + containerSubNo +
                ", goodsId=" + goodsId +
                ", goodsNum=" + goodsNum +
                ", expiryDate=" + expiryDate +
                ", createTime=" + createTime +
                '}';
    }
}