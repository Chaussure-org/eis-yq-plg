package com.prolog.eis.model.pd;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 盘点计划历史
 * @author tg
 *
 */
@Table("pd_task_his")
public class PdTaskHis {
	
	@Column("id")
	@ApiModelProperty("id")
	private int id;
	
	@Column("wms_pd_no")
	@ApiModelProperty("wms盘点单据编号")
	private String wmsPdNo;

	@Column("remark")
	@ApiModelProperty("备注")
	private String remark;

	@Column("pd_state")
	@ApiModelProperty("盘点状态 1:已创建 2:待盘点,3:盘点中,4:已完成,5上传完成")
	private int pdState;

	@Column("pd_type")
	@ApiModelProperty("盘点类型 1 eis内部盘点  2 wms下发的盘点计划")
	private int pdType;

	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;
	
	@Column("xiafa_time")
	@ApiModelProperty("下发时间")
	private Date xiafaTime;
	
	@Column("start_time")
	@ApiModelProperty("开始时间")
	private Date startTime;
	
	@Column("end_time")
	@ApiModelProperty("作业完成时间")
	private Date endTime;
	
	@Column("upload_time")
	@ApiModelProperty("上传时间")
	private Date uploadTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWmsPdNo() {
		return wmsPdNo;
	}

	public void setWmsPdNo(String wmsPdNo) {
		this.wmsPdNo = wmsPdNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getPdState() {
		return pdState;
	}

	public void setPdState(int pdState) {
		this.pdState = pdState;
	}

	public int getPdType() {
		return pdType;
	}

	public void setPdType(int pdType) {
		this.pdType = pdType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getXiafaTime() {
		return xiafaTime;
	}

	public void setXiafaTime(Date xiafaTime) {
		this.xiafaTime = xiafaTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	


}
