package com.prolog.eis.model.assembl;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 合箱计划操作表
 * @author tg
 *
 */
@Table("assembl_box_operate")
public class AssemblBoxOperate {
	
	@Id
	@Column("id")
	@ApiModelProperty("id")	
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("assembl_box_hz_id")
	@ApiModelProperty("合箱计划汇总id")
	private int assemblBoxHzId;
	
	@Column("source_sub_container_no")
	@ApiModelProperty("原子容器编号")
	private String sourceSubContainerNo;
	
	@Column("target_sub_container_no")
	@ApiModelProperty("目子标容器编号")
	private String targetSubContainerNo;
	
	@Column("goods_num")
	@ApiModelProperty("商品数量")
	private int goodsNum;
	
	@Column("create_time")
	@ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
	private Date createTime;
	@Column("operator_staff")
	@ApiModelProperty("操作人")
	private String operatorStaff;

	@Column("station_no")
	@ApiModelProperty("站台编号")
	private String stationNo;

	public String getStationNo() {
		return stationNo;
	}

	public void setStationNo(String stationNo) {
		this.stationNo = stationNo;
	}

	public String getOperatorStaff() {
		return operatorStaff;
	}

	public void setOperatorStaff(String operatorStaff) {
		this.operatorStaff = operatorStaff;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAssemblBoxHzId() {
		return assemblBoxHzId;
	}

	public void setAssemblBoxHzId(int assemblBoxHzId) {
		this.assemblBoxHzId = assemblBoxHzId;
	}

	public String getSourceSubContainerNo() {
		return sourceSubContainerNo;
	}

	public void setSourceSubContainerNo(String sourceSubContainerNo) {
		this.sourceSubContainerNo = sourceSubContainerNo;
	}

	public String getTargetSubContainerNo() {
		return targetSubContainerNo;
	}

	public void setTargetSubContainerNo(String targetSubContainerNo) {
		this.targetSubContainerNo = targetSubContainerNo;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	

}
