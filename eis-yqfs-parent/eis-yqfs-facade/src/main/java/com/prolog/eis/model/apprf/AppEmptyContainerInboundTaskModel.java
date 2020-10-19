package com.prolog.eis.model.apprf;

import com.prolog.eis.model.base.BaseModel;
import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @title 空箱入库表
 * @author jinxf
  * @time 2020/4/23 10:58
  */
@Table(value = "EMPTY_CONTAINER_INBOUND_TASK")
public class AppEmptyContainerInboundTaskModel extends BaseModel {

	@Id
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("Id")
	private int id;

	@ApiModelProperty("容器编号")
	@Column("CONTAINER_NO")
	private String containerNo;

	@ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
	@Column("CREATE_TIME")
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
	public String toString() {
		return "AppEmptyContainerInboundTaskModel{" +
				"id=" + id +
				", containerNo='" + containerNo + '\'' +
				", createTime=" + createTime +
				'}';
	}
}
