package com.prolog.eis.model.apprf;

import com.prolog.eis.model.base.BaseModel;
import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * @title 料箱容器表
 * @author jinxf
  * @time 2020/4/23 10:58
  */
@Table(value = "CONTAINER_INFO")
public class AppEmptyBoxInstockOrderModel extends BaseModel {
	@Id
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("容器编号")
	@Column("CONTAINER_NO")
	private String containerNo;

	@Column("LAYOUT_TYPE")
	@ApiModelProperty("容器布局类型（1.整箱、2.日字、3.田字）")
	private Integer layoutType;

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public Integer getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(Integer layoutType) {
		this.layoutType = layoutType;
	}
}
