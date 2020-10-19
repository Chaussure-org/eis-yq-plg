package com.prolog.eis.model.inbound;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("empty_container_inbound_task_his")
public class EmptyInboundTaskHis {

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

	public EmptyInboundTaskHis() {
	}

	public EmptyInboundTaskHis(int id, String containerNo, Date createTime) {
		this.id = id;
		this.containerNo = containerNo;
		this.createTime = createTime;
	}

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
}
