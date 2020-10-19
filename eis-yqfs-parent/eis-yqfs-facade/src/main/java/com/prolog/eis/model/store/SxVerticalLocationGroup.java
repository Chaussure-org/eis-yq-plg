package com.prolog.eis.model.store;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("sx_vertical_location_group")
public class SxVerticalLocationGroup {

	@Id
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("group_no")
	@ApiModelProperty("垂直货位组编号")
	private String groupNo;		//垂直货位组编号
	
	@Column("limit_weight")
	@ApiModelProperty("限重")
	private double limitWeight;		//限重

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the limitWeight
	 */
	public double getLimitWeight() {
		return limitWeight;
	}

	/**
	 * @param limitWeight the limitWeight to set
	 */
	public void setLimitWeight(double limitWeight) {
		this.limitWeight = limitWeight;
	}

	/**
	 * @return the groupNo
	 */
	public String getGroupNo() {
		return groupNo;
	}

	/**
	 * @param groupNo the groupNo to set
	 */
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	
	
	
}
