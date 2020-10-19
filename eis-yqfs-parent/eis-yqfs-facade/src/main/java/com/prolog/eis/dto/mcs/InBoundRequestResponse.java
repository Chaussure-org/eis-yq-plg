package com.prolog.eis.dto.mcs;

public class InBoundRequestResponse {

	private String taskId;
	
	private String target;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "InBoundRequestResponse [taskId=" + taskId + ", target=" + target + "]";
	}
	
	
	
}
