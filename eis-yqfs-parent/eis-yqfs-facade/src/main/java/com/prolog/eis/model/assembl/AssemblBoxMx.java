package com.prolog.eis.model.assembl;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 合箱计划明细表(AssemblBoxMx)实体类
 *
 * @author panteng
 * @since 2020-05-03 16:57:02
 */
@Table("assembl_box_mx")
public class AssemblBoxMx implements Serializable {
    private static final long serialVersionUID = 635710484794817351L;


	public static  final int TASK_STATE_WKS = 0;//未开始
	public static  final int TASK_STATE_JXZ = 1;//进行中
	public static  final int TASK_STATE_YWC = 2;//已完成
	public static  final int TASK_STATE_ERROR = 3;//异常剔除

    @Id
    @Column("id")
    @ApiModelProperty("id")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;
    
    @Column("assembl_box_hz_id")
    @ApiModelProperty("合箱计划汇总id")
    private int assemblBoxHzId;
    
    @Column("container_no")
    @ApiModelProperty("容器编号")
    private String containerNo;
    
    @Column("task_state")
    @ApiModelProperty("任务状态 任务状态 0未开始 1进行中 2已完成")
    private int taskState;
    
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

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public int getTaskState() {
		return taskState;
	}

	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}