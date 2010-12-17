package net.silencily.sailing.basic.wf.domain.mate;

import java.util.List;

/**
 * ------------------ 
 * Author : yijinhua 
 * Date : 2007-11-13 
 * -------------------
 */

public class Function {
	/*
	 * -type:String 
	 * -args:List
	 */
	private String type = "class";

	private List args;

	public List getArgs() {
		return args;
	}

	public void setArgs(List args) {
		this.args = args;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
