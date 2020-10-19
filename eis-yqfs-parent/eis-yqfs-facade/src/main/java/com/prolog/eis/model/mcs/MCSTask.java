package com.prolog.eis.model.mcs;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("mcs_task")
public class MCSTask {

	@Id
	@ApiModelProperty("主键")
	private int id;		//主键
	
	/**
	 * 规则HHmmss+四位流水码(1-9999)
	 */
	@Column("task_id")
	private String taskId;
	/**
	 * 任务类型：1：入库:2：出库、3.跨层 4.MCS链条机前移
	 */
	@Column("type")
	private int type;
	/**
	 * 母托盘编号
	 */
	@Column("stock_id")
	private String stockId;
	/**
	 * 请求位置:原坐标
	 */
	@Column("source")
	private String source;
	/**
	 * 目的位置：目的坐标
	 */
	@Column("target")
	private String target;
	/**
	 * 入库重量
	 */
	@Column("weight")
	private String weight;
	/**
	 * 任务优先级,0-99,0优先级最大
	 */
	@Column("priority")
	private int priority;
	
	/**
	 * 任务状态(1.完成、2.失败)
	 */
	@Column("task_state")
	private int taskState;
	
	/**
	 * 发送次数
	 */
	@Column("send_count")
	private int sendCount;	
	
	@Column("err_msg")
	private String errMsg;
	
	@Column("create_time")
	private Date createTime;
	
	@Column("eis_type")
	private int eisType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getTaskState() {
		return taskState;
	}

	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public int getEisType() {
		return eisType;
	}

	public void setEisType(int eisType) {
		this.eisType = eisType;
	}

	public MCSTask(int id, String taskId, int type, String stockId, String source, String target, String weight,
			int priority, int taskState, int sendCount, String errMsg, Date createTime, int eisType) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.type = type;
		this.stockId = stockId;
		this.source = source;
		this.target = target;
		this.weight = weight;
		this.priority = priority;
		this.taskState = taskState;
		this.sendCount = sendCount;
		this.errMsg = errMsg;
		this.createTime = createTime;
		this.eisType = eisType;
	}

	public MCSTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MCSTask [id=" + id + ", taskId=" + taskId + ", type=" + type + ", stockId=" + stockId + ", source="
				+ source + ", target=" + target + ", weight=" + weight + ", priority=" + priority + ", taskState="
				+ taskState + ", sendCount=" + sendCount + ", errMsg=" + errMsg + ", createTime=" + createTime
				+ ", eisType=" + eisType + "]";
	}
	
	
	
	
}
