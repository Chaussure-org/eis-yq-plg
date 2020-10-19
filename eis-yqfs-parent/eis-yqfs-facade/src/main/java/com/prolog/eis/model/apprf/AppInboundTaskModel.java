package com.prolog.eis.model.apprf;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @title 入库单汇总表
 * @author jinxf
 * @time 2020/4/23 11:49
 */

@Table(value = "INBOUND_TASK")
public class AppInboundTaskModel {
    @Id
    @ApiModelProperty("Id")
    private Integer id;

    @ApiModelProperty("容器编号")
    @Column("CONTAINER_NO")
    private String containerNo;

    @ApiModelProperty("操作人")
    @Column("OPERATOR")
    private String operator;

    @ApiModelProperty("状态（1：进行中，2：已完成）")
    @Column("INBOUND_STATUS")
    private Integer inboundStatus;

    @ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
    @Column("CREATE_TIME")
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getInboundStatus() {
        return inboundStatus;
    }

    public void setInboundStatus(Integer inboundStatus) {
        this.inboundStatus = inboundStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}