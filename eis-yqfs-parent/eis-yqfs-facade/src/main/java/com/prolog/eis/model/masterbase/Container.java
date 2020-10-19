package com.prolog.eis.model.masterbase;


import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("container")
public class Container {

	public final static String CONTAINER_NO_SATRT = "6";

	@Column("container_no")
	@ApiModelProperty("容器编号")
	private String containerNo;
	@Column("layout_type")
	@ApiModelProperty("容器布局类型（1.整箱、2.日字、3.田字）")
	private int layoutType;


	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public int getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(int layoutType) {
		this.layoutType = layoutType;
	}
}
