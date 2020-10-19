package com.prolog.eis.model.order;

import java.util.Date;
import java.io.Serializable;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * 出库单明细表(OrderMx)实体类
 *
 * @author panteng
 * @since 2020-04-19 16:03:14
 */
@Table("order_mx")
public class OrderMx implements Serializable {
    private static final long serialVersionUID = 792823807765663882L;
    @Id
    @Column("id")
    @ApiModelProperty("id")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;
    
    @Column("order_hz_id")
    @ApiModelProperty("出库单汇总id")
    private int orderHzId;
    
    @Column("goods_id")
    @ApiModelProperty("商品id")
    private int goodsId;
    
//    @Column("lot_id")
//    @ApiModelProperty("批次id")
//    private Integer lotId;
    
    @Column("plan_num")
    @ApiModelProperty("计划数量")
    private int planNum;
    
    @Column("actual_num")
    @ApiModelProperty("实际数量")
    private int actualNum;
    @Column("out_num")
    @ApiModelProperty("出库数量")
    private int outNum;
    
    @Column("is_complete")
    @ApiModelProperty("是否完成：（0：否，1：是）")
    private int isComplete;
    
    @Column("is_not_pick")
    @ApiModelProperty("是否短拣：（0：否，1：是）")
    private int isNotPick;
    
    @Column("create_time")
    @ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
    private Date createTime;
    
    @Column("update_time")
    @ApiModelProperty("修改时间（YYYY-MM-DD HH:MM:SS）")
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderHzId() {
        return orderHzId;
    }

    public void setOrderHzId(int orderHzId) {
        this.orderHzId = orderHzId;
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

    public int getActualNum() {
        return actualNum;
    }

    public void setActualNum(int actualNum) {
        this.actualNum = actualNum;
    }

    public int getOutNum() {
        return outNum;
    }

    public void setOutNum(int outNum) {
        this.outNum = outNum;
    }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public int getIsNotPick() {
        return isNotPick;
    }

    public void setIsNotPick(int isNotPick) {
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