package net.silencily.sailing.basic.wf.domain.mate;
/**
 * ------------------ 
 * Author : yijinhua 
 * Date : 2007-11-13 
 * ------------------
 */

public class Permission {

	private String name;

	private Conditions conditions;

	public Conditions getConditions() {
		return conditions;
	}

	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
