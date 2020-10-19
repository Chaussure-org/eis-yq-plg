package com.prolog.eis.model.assembl;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 合箱计划明细历史表
 * @author tg
 *
 */
@Table("assembl_box_mx_history")
public class AssemblBoxMxHistory {
	
    @Column("id")
    @ApiModelProperty("id")
    private Integer id;
    
    @Column("assembl_box_hz_id")
    @ApiModelProperty("合箱计划汇总id")
    private Integer assemblBoxHzId;
    
    @Column("container_no")
    @ApiModelProperty("容器编号")
    private String containerNo;
    
    @Column("task_state")
    @ApiModelProperty("任务状态 0创建 1已出库 2合箱开始 3合箱完成")
    private Integer taskState;
    
    @Column("create_time")
    @ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
    private Date createTime;

	@Column("outed")
	@ApiModelProperty("是否已出库")
	private boolean outed;

	public boolean isOuted() {
		return outed;
	}

	public void setOuted(boolean outed) {
		this.outed = outed;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAssemblBoxHzId() {
		return assemblBoxHzId;
	}

	public void setAssemblBoxHzId(Integer assemblBoxHzId) {
		this.assemblBoxHzId = assemblBoxHzId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
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
    
    

}
