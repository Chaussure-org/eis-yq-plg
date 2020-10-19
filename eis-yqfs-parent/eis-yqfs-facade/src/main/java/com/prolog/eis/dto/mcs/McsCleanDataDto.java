package com.prolog.eis.dto.mcs;

public class McsCleanDataDto {

	private String taskId;
	
	private String stockId;
	
	private String clearCrood;

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the stockId
	 */
	public String getStockId() {
		return stockId;
	}

	/**
	 * @param stockId the stockId to set
	 */
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	/**
	 * @return the clearCrood
	 */
	public String getClearCrood() {
		return clearCrood;
	}

	/**
	 * @param clearCrood the clearCrood to set
	 */
	public void setClearCrood(String clearCrood) {
		this.clearCrood = clearCrood;
	}

	public McsCleanDataDto(String taskId, String stockId, String clearCrood) {
		super();
		this.taskId = taskId;
		this.stockId = stockId;
		this.clearCrood = clearCrood;
	}

	public McsCleanDataDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "McsCleanDataDto [taskId=" + taskId + ", stockId=" + stockId + ", clearCrood=" + clearCrood + "]";
	}
	
	
	
}
