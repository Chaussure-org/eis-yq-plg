package com.prolog.eis.model.masterbase;


import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("lotinfo")
public class Lotinfo {
    @Id
    @ApiModelProperty("id")
    @Column("id")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;

    @Column("owner_id")
    @ApiModelProperty("业主id")
    private int ownerId;

    @Column("goods_id")
    @ApiModelProperty("商品id")
    private int goodsId;

    @Column("lot")
    @ApiModelProperty("批号")
    private String lot;

    @Column("lot_num")
    @ApiModelProperty("批次号")
    private String lotNum;

    @Column("production_date")
    @ApiModelProperty("生产日期")
    private Date productionDate;

    @Column("expiry_date")
    @ApiModelProperty("有效日期")
    private Date expiryDate;

    @Column("status")
    @ApiModelProperty("批次状态（1：正常，2：异常）")
    private int status;

    @Column("remark")
    @ApiModelProperty("备注")
    private String remark;


    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;


    @Column("update_time")
    @ApiModelProperty("修改时间")
    private Date updateYime;

    public Lotinfo() {
    }

    public Lotinfo(int id, int ownerId, int goodsId, String lot, String lotNum, Date productionDate, Date expiryDate, int status, String remark, Date createTime, Date updateYime) {
        this.id = id;
        this.ownerId = ownerId;
        this.goodsId = goodsId;
        this.lot = lot;
        this.lotNum = lotNum;
        this.productionDate = productionDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.remark = remark;
        this.createTime = createTime;
        this.updateYime = updateYime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateYime() {
        return updateYime;
    }

    public void setUpdateYime(Date updateYime) {
        this.updateYime = updateYime;
    }
}
