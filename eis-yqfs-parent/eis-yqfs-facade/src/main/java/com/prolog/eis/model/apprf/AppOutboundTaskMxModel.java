package com.prolog.eis.model.apprf;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table(value = "OUTBOUND_TASK_MX")
public class AppOutboundTaskMxModel {
    @Id
    @ApiModelProperty("Id")
    private Integer id;

    @ApiModelProperty("入库单汇总id")
    @Column("OUTBOUND_TASK_HZ_ID")
    private Integer outboundTaskHzId;

    @ApiModelProperty("商品id")
    @Column("GOODS_ID")
    private Integer goodsId;

    @ApiModelProperty("批次id")
    @Column("LOT_ID")
    private Integer lotId;

    @ApiModelProperty("计划数量")
    @Column("PLAN_NUM")
    private Integer planNum;

    @ApiModelProperty("实际数量")
    @Column("ACTUAL_NUM")
    private Integer actualNum;

    @ApiModelProperty("是否完成：（0：否，1：是）")
    @Column("IS_COMPLETE")
    private Integer isComplete;

    @ApiModelProperty("是否断拣：（0：否，1：是）")
    @Column("IS_NOT_PICK")
    private Integer isNotPick;

    @ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
    @Column("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty("修改时间（YYYY-MM-DD HH:MM:SS）")
    @Column("UPDATE_TIME")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOutboundTaskHzId() {
        return outboundTaskHzId;
    }

    public void setOutboundTaskHzId(Integer outboundTaskHzId) {
        this.outboundTaskHzId = outboundTaskHzId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getLotId() {
        return lotId;
    }

    public void setLotId(Integer lotId) {
        this.lotId = lotId;
    }

    public Integer getPlanNum() {
        return planNum;
    }

    public void setPlanNum(Integer planNum) {
        this.planNum = planNum;
    }

    public Integer getActualNum() {
        return actualNum;
    }

    public void setActualNum(Integer actualNum) {
        this.actualNum = actualNum;
    }

    public Integer getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Integer isComplete) {
        this.isComplete = isComplete;
    }

    public Integer getIsNotPick() {
        return isNotPick;
    }

    public void setIsNotPick(Integer isNotPick) {
        this.isNotPick = isNotPick;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}