package net.silencily.sailing.basic.wf.domain.mate;

import java.util.List;
/**
 * ------------------ 
 * Author : yijinhua 
 * Date : 2007-11-13 
 * ------------------
 */
public class Conditions {

	private String type;

	private List conditions;

	public List getConditions() {
		return conditions;
	}

	public void setConditions(List conditions) {
		this.conditions = conditions;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
