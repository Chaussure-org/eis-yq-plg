package com.prolog.eis.dto.mcs;

public class InBoundRequest {

	/**
	 * 规则HHmmss+四位流水码(1-9999)
	 */
	private String taskId;
	/**
	 * 任务类型：1：入库:2：出库 3 跨层
	 */
	private int type;
	/**
	 * 母托盘编号
	 */
	private String stockId;
	/**
	 * 请求位置:原坐标
	 */
	private String source;
	/**
	 * 目的位置：目的坐标
	 */
	private String target;
	/**
	 * 入库重量
	 */
	private String weight;
	/**
	 * 任务优先级,0-99,0优先级最大
	 */
	private int priority;
	/**
	 *【任务状态：1：任务开始；2：任务完成】
	 */
	private int status;
	
	/**
	 * 子托盘编号
	 */
	private String stockIdSub;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	/**
	 * @return the stockIdSub
	 */
	public String getStockIdSub() {
		return stockIdSub;
	}

	/**
	 * @param stockIdSub the stockIdSub to set
	 */
	public void setStockIdSub(String stockIdSub) {
		this.stockIdSub = stockIdSub;
	}

	@Override
	public String toString() {
		return "InBoundRequest [taskId=" + taskId + ", type=" + type + ", stockId=" + stockId + ", source=" + source
				+ ", target=" + target + ", weight=" + weight + ", priority=" + priority + ", status=" + status
				+ ", stockIdSub=" + stockIdSub + "]";
	}
	
}
