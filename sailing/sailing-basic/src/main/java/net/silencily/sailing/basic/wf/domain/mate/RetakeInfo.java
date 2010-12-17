package net.silencily.sailing.basic.wf.domain.mate;

public class RetakeInfo {
	private String workflowName;
	private String StepName;
	private String stepId;
	private String roles;
	private String[] commitStepids;
	public String[] getCommitStepids() {
		return commitStepids;
	}
	public void setCommitStepids(String[] commitStepids) {
		this.commitStepids = commitStepids;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public String getStepId() {
		return stepId;
	}
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	public String getStepName() {
		return StepName;
	}
	public void setStepName(String stepName) {
		StepName = stepName;
	}
	public String getWorkflowName() {
		return workflowName;
	}
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
}
