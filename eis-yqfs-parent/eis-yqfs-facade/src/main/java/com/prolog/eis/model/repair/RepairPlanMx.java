package com.prolog.eis.model.repair;

import java.util.Date;
import java.io.Serializable;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * 补货计划明细(RepairPlanMx)实体类
 *
 * @author panteng
 * @since 2020-04-25 11:02:08
 */
@Table("repair_plan_mx")
public class RepairPlanMx implements Serializable {
    private static final long serialVersionUID = 878871730442044234L;

    public static final int REPAIR_STATUS_ING=1;
    public static final int REPAIR_STATUS_XF=2;
    public static final int REPAIR_STATUS_FINISH=3;

    @Id
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    @ApiModelProperty("id")
    private Integer id;
    
    @Column("repair_plan_id")
    @ApiModelProperty("补货计划id")
    private Integer repairPlanId;
    
    @Column("container_no")
    @ApiModelProperty("容器编号")
    private String containerNo;
    
    @Column("repair_status")
    @ApiModelProperty("状态1 未开始 ,  2 已下发 ,  3 补货完成")
    private Integer repairStatus;
    
    @Column("create_time")
    @ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
    private Date createTime;
    
    @Column("container_sub_no")
    @ApiModelProperty("子容器编号")
    private String containerSubNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRepairPlanId() {
        return repairPlanId;
    }

    public void setRepairPlanId(Integer repairPlanId) {
        this.repairPlanId = repairPlanId;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
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

	public String getContainerSubNo() {
		return containerSubNo;
	}

	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
	}

}