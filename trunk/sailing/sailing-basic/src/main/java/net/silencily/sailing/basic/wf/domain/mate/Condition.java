package net.silencily.sailing.basic.wf.domain.mate;

import java.util.ArrayList;
import java.util.List;

/**
 * ------------------ 
 * Author : yijinhua 
 * Date : 2007-11-13 
 * -------------------
 */

public class Condition {
	/*------------------------------------------------------
	 -type:String
	 -args:List
	 -------------------------------------------------------*/
	private String type = "class";

	private List args = null;

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
