package com.prolog.eis.model.apprf;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @title 出库单汇总历史
 * @author jinxf
 * @time 2020/4/23 11:59l
 */
@Table(value = "OUTBOUND_TASK_HZ_HISTORY")
public class AppOutboundTaskHzHistoryModel {
    @Id
    @ApiModelProperty("Id")
    private Integer id;

    @ApiModelProperty("拣选单Id")
    @Column("PICKING_ORDER_ID")
    private Integer pickingOrderId;

    @ApiModelProperty("出库单编号")
    @Column("OUTBOUND_CODE")
    private String outboundCode;

    @ApiModelProperty("时效（YYYY-MM-DD HH:MM:SS）")
    @Column("expect_time")
    private Date expectTime;

    @ApiModelProperty("优先级")
    @Column("PRIORITY")
    private Integer priority;

    @ApiModelProperty("拣选开始时间")
    @Column("PICK_DATE_TIME")
    private Date pickDateTime;

    @ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
    @Column("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty("最后操作时间（YYYY-MM-DD HH:MM:SS）")
    @Column("LASTDATE_TIME")
    private Date lastDateTime;

    @ApiModelProperty("订单箱编号")
    @Column("ORDER_BOX_NO")
    private String orderBoxNo;

    @ApiModelProperty("站台id")
    @Column("STATION_ID")
    private Integer stationId;

    @ApiModelProperty("是否加入订单池（0：否，1：是）")
    @Column("IS_ADD_POOL")
    private Integer isAddPool;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPickingOrderId() {
        return pickingOrderId;
    }

    public void setPickingOrderId(Integer pickingOrderId) {
        this.pickingOrderId = pickingOrderId;
    }

    public String getOutboundCode() {
        return outboundCode;
    }

    public void setOutboundCode(String outboundCode) {
        this.outboundCode = outboundCode == null ? null : outboundCode.trim();
    }

    public Date getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getPickDateTime() {
        return pickDateTime;
    }

    public void setPickDateTime(Date pickDateTime) {
        this.pickDateTime = pickDateTime;
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

    public String getOrderBoxNo() {
        return orderBoxNo;
    }

    public void setOrderBoxNo(String orderBoxNo) {
        this.orderBoxNo = orderBoxNo == null ? null : orderBoxNo.trim();
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Integer getIsAddPool() {
        return isAddPool;
    }

    public void setIsAddPool(Integer isAddPool) {
        this.isAddPool = isAddPool;
    }
}