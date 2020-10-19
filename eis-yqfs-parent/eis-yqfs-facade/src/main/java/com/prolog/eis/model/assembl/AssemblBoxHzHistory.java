package com.prolog.eis.model.assembl;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 合箱计划汇总历史表
 * 
 * @author tg
 *
 */
@Table("assembl_box_hz_history")
public class AssemblBoxHzHistory {

	@Column("id")
	@ApiModelProperty("id")
	private Integer id;

	@Column("goods_id")
	@ApiModelProperty("商品id")
	private Integer goodsId;

	@Column("station_id")
	@ApiModelProperty("作业站台id")
	private Integer stationId;

	@Column("task_state")
	@ApiModelProperty("任务状态 0创建 1下发")
	private Integer taskState;

	@Column("create_time")
	@ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
	private Date createTime;

	@Column("release_time")
	@ApiModelProperty("下发时间（YYYY-MM-DD HH:MM:SS）")
	private Date releaseTime;

	@Column("all_is_out")
	@ApiModelProperty("是否全部出库")
	private boolean allIsOut;

	public boolean isAllIsOut() {
		return allIsOut;
	}

	public void setAllIsOut(boolean allIsOut) {
		this.allIsOut = allIsOut;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public Integer getTaskState() {
		return taskState;
	}

	public void setTaskState(Integer taskState) {
		this.taskState = taskState;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}
}
