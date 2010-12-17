package net.silencily.sailing.basic.wf.dto;

public class FlowTaskInfo implements java.io.Serializable{
	private String id;
	private String processed = "N";
	private String processId;
	private String processUsername;
	private String processRole;
	private String commitUsername;
	private String commitRole;
	private String commitId;
	private String processDept;
	private String commitDept;
	private String oid;
	private String workflowName;
	private String currStepId;
	private String currStepName;
	private String toStepId;
	private String toStepName;
	private String roleSignFlag = "N";
	private String roleSignRole;
	private String roleSignDept;
	private String consignFlag = "N";
	private String consignedFlag = "N";
	private String consigner;
	private String consigneder;
	private String consignerRole;
	private String consignederRole;
	public String getCommitDept() {
		return commitDept;
	}
	public void setCommitDept(String commitDept) {
		this.commitDept = commitDept;
	}
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	public String getCommitRole() {
		return commitRole;
	}
	public void setCommitRole(String commitRole) {
		this.commitRole = commitRole;
	}
	public String getCommitUsername() {
		return commitUsername;
	}
	public void setCommitUsername(String commitUsername) {
		this.commitUsername = commitUsername;
	}
	public String getConsigneder() {
		return consigneder;
	}
	public void setConsigneder(String consigneder) {
		this.consigneder = consigneder;
	}
	public String getConsignederRole() {
		return consignederRole;
	}
	public void setConsignederRole(String consignederRole) {
		this.consignederRole = consignederRole;
	}
	public String getConsignedFlag() {
		return consignedFlag;
	}
	public void setConsignedFlag(String consignedFlag) {
		this.consignedFlag = consignedFlag;
	}
	public String getConsigner() {
		return consigner;
	}
	public void setConsigner(String consigner) {
		this.consigner = consigner;
	}
	public String getConsignerRole() {
		return consignerRole;
	}
	public void setConsignerRole(String consignerRole) {
		this.consignerRole = consignerRole;
	}
	public String getConsignFlag() {
		return consignFlag;
	}
	public void setConsignFlag(String consignFlag) {
		this.consignFlag = consignFlag;
	}
	public String getCurrStepId() {
		return currStepId;
	}
	public void setCurrStepId(String currStepId) {
		this.currStepId = currStepId;
	}
	public String getCurrStepName() {
		return currStepName;
	}
	public void setCurrStepName(String currStepName) {
		this.currStepName = currStepName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getProcessDept() {
		return processDept;
	}
	public void setProcessDept(String processDept) {
		this.processDept = processDept;
	}
	public String getProcessed() {
		return processed;
	}
	public void setProcessed(String processed) {
		this.processed = processed;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getProcessRole() {
		return processRole;
	}
	public void setProcessRole(String processRole) {
		this.processRole = processRole;
	}
	public String getProcessUsername() {
		return processUsername;
	}
	public void setProcessUsername(String processUsername) {
		this.processUsername = processUsername;
	}
	public String getRoleSignDept() {
		return roleSignDept;
	}
	public void setRoleSignDept(String roleSignDept) {
		this.roleSignDept = roleSignDept;
	}
	
	public String getRoleSignFlag() {
		return roleSignFlag;
	}
	public void setRoleSignFlag(String roleSignFlag) {
		this.roleSignFlag = roleSignFlag;
	}
	public String getRoleSignRole() {
		return roleSignRole;
	}
	public void setRoleSignRole(String roleSignRole) {
		this.roleSignRole = roleSignRole;
	}
	public String getToStepId() {
		return toStepId;
	}
	public void setToStepId(String toStepId) {
		this.toStepId = toStepId;
	}
	public String getToStepName() {
		return toStepName;
	}
	public void setToStepName(String toStepName) {
		this.toStepName = toStepName;
	}
	public String getWorkflowName() {
		return workflowName;
	}
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
}
