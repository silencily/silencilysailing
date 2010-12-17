package net.silencily.sailing.basic.wf.entry;

public class WorkflowRelation {
	private String id;

	private String superOid;

	private String superClass;

	private String subOid;

	private String subClass;

	private String unlockId;

	private String status;
	
	private String stepId;//父子流记录父流id
	
	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	

	public String getUnlockId() {
		return unlockId;
	}

	public void setUnlockId(String unlockId) {
		this.unlockId = unlockId;
	}

	public String getSubClass() {
		return subClass;
	}

	public void setSubClass(String subClass) {
		this.subClass = subClass;
	}

	public String getSubOid() {
		return subOid;
	}

	public void setSubOid(String subOid) {
		this.subOid = subOid;
	}

	public String getSuperClass() {
		return superClass;
	}

	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}

	public String getSuperOid() {
		return superOid;
	}

	public void setSuperOid(String superOid) {
		this.superOid = superOid;
	}
}
