package com.prolog.eis.model.repair;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 补货汇总历史
 * @author tg
 *
 */
@Table("repair_task_hz_his")
public class RepairTaskHzHis {
	
	@Id
	@Column("id")
	@ApiModelProperty("id")
	private Integer id;
    
    @Column("container_no")
    @ApiModelProperty("容器编号")
    private String containerNo;
    
    @Column("operator")
    @ApiModelProperty("操作人")
    private String operator;
    
    @Column("inbound_status")
    @ApiModelProperty("状态（1：进行中，2：已完成）")
    private Integer inboundStatus;
    
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
