package com.prolog.eis.model.repair;

import java.util.Date;
import java.io.Serializable;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * 补货明细表(RepairTaskMx)实体类
 *
 * @author panteng
 * @since 2020-04-24 16:16:29
 */
@Table("repair_task_mx")
public class RepairTaskMx implements Serializable {
    private static final long serialVersionUID = -40727693238865977L;

    public static final int MXSTATUS_ING=1;
    public static final int MXSTATUS_COMPLETE=2;
    @Id
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    @ApiModelProperty("id")
    private Integer id;
    
    @Column("repair_task_hz_id")
    @ApiModelProperty("入库单汇总id")
    private Integer repairTaskHzId;

    @Column("container_no")
    @ApiModelProperty("子容器编号")
    private String containerNo;
    
    @Column("goods_id")
    @ApiModelProperty("商品id")
    private Integer goodsId;



    @Column("container_sub_no")
    @ApiModelProperty("子容器编号")
    private String containerSubNo;

    @Column("repair_status")
    @ApiModelProperty("状态")
    private Integer repairStatus;

    @Column("repair_count")
    @ApiModelProperty("补了的数量")
    private Integer repairCount;
    
    @Column("create_time")
    @ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
    private Date createTime;
    
    @Column("update_time")
    @ApiModelProperty("修改时间（YYYY-MM-DD HH:MM:SS）")
    private Date updateTime;

    public Integer getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(Integer repairCount) {
        this.repairCount = repairCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRepairTaskHzId() {
        return repairTaskHzId;
    }

    public void setRepairTaskHzId(Integer repairTaskHzId) {
        this.repairTaskHzId = repairTaskHzId;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }

    public Integer getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(Integer repairStatus) {
        this.repairStatus = repairStatus;
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