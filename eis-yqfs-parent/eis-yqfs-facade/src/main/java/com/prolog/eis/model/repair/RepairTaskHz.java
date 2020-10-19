package com.prolog.eis.model.repair;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 补货汇总(RepairTaskHz)实体类
 *
 * @author panteng
 * @since 2020-04-24 16:11:41
 */
@Table("repair_task_hz")
public class RepairTaskHz implements Serializable {
    private static final long serialVersionUID = 558021820182527361L;

    public static final int REPAIRTASK_ING = 1;
    public static final int REPAIRTASK_COMPLETE = 2;
    @Id
    @ApiModelProperty("id")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private Integer id;


    @ApiModelProperty("容器号")
    @Column("container_no")
    private String containerNo;


    @Column("operator")
    @ApiModelProperty("操作人")
    private String operator;

    @Column("repair_status")
    @ApiModelProperty("状态（1：进行中，2：已完成）")
    private Integer repairStatus;

    @Column("create_time")
    @ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
    private Date createTime;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

}