package net.silencily.sailing.common.ui.selector.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class SelectorForm extends ActionForm{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private List userList = new ArrayList();
	private List deptList = new ArrayList();	
	private List codeList = new ArrayList();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public List getUserList() {
		return userList;
	}

	public void setUserList(List ls) {
		this.userList = ls;
	}
	
	public List getDeptList() {
		return deptList;
	}

	public void setDeptList(List ls) {
		this.deptList = ls;
	}
	
	public List getCodeList() {
		return codeList;
	}

	public void setCodeList(List ls) {
		this.codeList = ls;
	}
}
