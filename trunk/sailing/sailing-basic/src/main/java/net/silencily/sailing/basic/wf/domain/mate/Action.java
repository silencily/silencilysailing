package net.silencily.sailing.basic.wf.domain.mate;

import java.util.ArrayList;
import java.util.List;

/**
 * ------------------ 
 * Author : yijinhua 
 * Date : 2007-11-13 
 * -------------------
 */
public class Action {

	/*
	 * -id:String 
	 * -name:String 
	 * -auto:boolean 
	 * -finish:boolean 
	 * -prefunction:Function
	 * -postunction:Function 
	 * -conditions:Conditions
	 * -results:List
	 */

	private String id;

	private String name;

	private boolean auto;

	private boolean finish;

	private Function prefunction;

	private Function postfunction;

	private Conditions conditions;

	private List results = new ArrayList();

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public Conditions getConditions() {		
		return conditions;
	}

	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Function getPostfunction() {
		return postfunction;
	}

	public void setPostfunction(Function postfunction) {
		this.postfunction = postfunction;
	}

	public Function getPrefunction() {
		return prefunction;
	}

	public void setPrefunction(Function prefunction) {
		this.prefunction = prefunction;
	}

	public List getResults() {
		return results;
	}

	public void setResults(List results) {
		this.results = results;
	}

	
	
}
