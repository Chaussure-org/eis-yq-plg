package com.prolog.eis.model.repair;


import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 入库补货记录表
 */
@Table("inbound_repair_info")
public class InboundRepairInfo {


    public static final int REPQAIR_STATUS = 1;//补货类型
    public static final int INBOUND_STATUS = 2;//入库类型
    @Id
    @Column("id")
    @ApiModelProperty("id")
    private Integer id;

    @Column("container_no")
    @ApiModelProperty("料箱号")
    private String containerNo;

    @Column("container_sub_no")
    @ApiModelProperty("子容器编号")
    private String containerSubNo;

    @Column("commodity_num")
    @ApiModelProperty("入库或补货数量")
    private Integer commodityNum;

    @Column("goods_id")
    @ApiModelProperty("商品条码")
    private Integer goodsId;

    @Column("inbound_statu")
    @ApiModelProperty("入库类型")
    private Integer inboundStatu;

    @Column("create_time")
    @ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
    private Date createTime;

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

    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }

    public Integer getCommodityNum() {
        return commodityNum;
    }

    public void setCommodityNum(Integer commodityNum) {
        this.commodityNum = commodityNum;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getInboundStatu() {
        return inboundStatu;
    }

    public void setInboundStatu(Integer inboundStatu) {
        this.inboundStatu = inboundStatu;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
