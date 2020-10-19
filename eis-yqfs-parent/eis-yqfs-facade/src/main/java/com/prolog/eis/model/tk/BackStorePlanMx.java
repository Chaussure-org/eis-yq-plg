package com.prolog.eis.model.tk;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 退库计划明细
 * @author tg
 *
 */
@Table("back_store_plan_mx")
public class BackStorePlanMx {
	
	@Id
	@Column("id")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("id")
	private int id; 
	
	@Column("back_store_plan_id")
	@ApiModelProperty("退库计划主键")
	private int backStorePlanId;
	
	@Column("box_no")
	@ApiModelProperty("料箱号")
	private String boxNo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBackStorePlanId() {
		return backStorePlanId;
	}

	public void setBackStorePlanId(int backStorePlanId) {
		this.backStorePlanId = backStorePlanId;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}
	
	

}
