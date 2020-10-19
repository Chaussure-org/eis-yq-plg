package com.prolog.eis.model.inbound;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("empty_container_inbound_task")
public class EmptyInboundTask {

	@Id
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	@Column("id")
	@ApiModelProperty("id")
	private int id;
	
	@Column("container_no")
	@ApiModelProperty("容器编号")
	private String containerNo;
	
	@Column("create_time")
	@ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((containerNo == null) ? 0 : containerNo.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmptyInboundTask other = (EmptyInboundTask) obj;
		if (containerNo == null) {
			if (other.containerNo != null)
				return false;
		} else if (!containerNo.equals(other.containerNo))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EmptyInboundTask [id=" + id + ", containerNo=" + containerNo + ", createTime=" + createTime
				+ "]";
	}

	public EmptyInboundTask(int id, String containerNo, Date createTime) {
		super();
		this.id = id;
		this.containerNo = containerNo;
		this.createTime = createTime;
	}

	public EmptyInboundTask() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
