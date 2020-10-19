package com.prolog.eis.model.inbound;

import java.io.Serializable;
import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 入库单汇总(InboundTask)实体类
 *
 * @author panteng
 * @since 2020-04-23 14:34:18
 */
@Table("inbound_task")
public class InboundTask implements Serializable {
    private static final long serialVersionUID = -23517920106451836L;
    @Column("id")
    @ApiModelProperty("id")
    @Id
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
    private Integer id;
    
    @Column("container_no")
    @ApiModelProperty("容器编号")
    private String containerNo;
    
    @Column("operator")
    @ApiModelProperty("操作人")
    private String operator;
    
    @Column("inbound_status")
    @ApiModelProperty("状态（1：进行中，2：已完成）")
    private int inbound_status;
    
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

	public int getInbound_status() {
		return inbound_status;
	}

	public void setInbound_status(int inbound_status) {
		this.inbound_status = inbound_status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "InboundTask [id=" + id + ", containerNo=" + containerNo + ", operator=" + operator
				+ ", inbound_status=" + inbound_status + ", createTime=" + createTime + "]";
	}

	public InboundTask(Integer id, String containerNo, String operator, int inbound_status, Date createTime) {
		super();
		this.id = id;
		this.containerNo = containerNo;
		this.operator = operator;
		this.inbound_status = inbound_status;
		this.createTime = createTime;
	}

	public InboundTask() {
		super();
		// TODO Auto-generated constructor stub
	}
    



}