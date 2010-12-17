package net.silencily.sailing.basic.wf.domain.mate;
/**
 * ------------------ 
 * Author : yijinhua 
 * Date : 2007-11-13 
 * ------------------
 */

public class Result {
	/*
	 * -oldStatus:String 
	 * -status:String 
	 * -step:String 
	 * -conditions:List
	 */
	private String oldStatus;

	private String status;

	private String step;

	private Conditions conditions;
	
	private String owner;
	
	

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getOldStatus() {
		return oldStatus;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getStep() {
		return step;
	}

	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}

	public Conditions getConditions() {
		return conditions;
	}
}
