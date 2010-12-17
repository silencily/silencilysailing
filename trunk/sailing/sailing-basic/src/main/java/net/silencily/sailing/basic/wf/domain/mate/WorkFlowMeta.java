package net.silencily.sailing.basic.wf.domain.mate;

import java.util.List;

public class WorkFlowMeta {

	// ����ID
	private String id;

	// ��������
	private String name;

	// ��������
	private String wftype;

	// ���̰汾 ����ĳ�EDITION
	private String bizId;

	private Action initialActions;

	private Action globalActions;

	private Action commonActions;

	private List steps;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getBizId() {
		return bizId;
	}

	public Action getCommonActions() {
		return commonActions;
	}

	public void setCommonActions(Action commonActions) {
		this.commonActions = commonActions;
	}

	public Action getGlobalActions() {
		return globalActions;
	}

	public void setGlobalActions(Action globalActions) {
		this.globalActions = globalActions;
	}

	public Action getInitialActions() {
		return initialActions;
	}

	public void setInitialActions(Action initialActions) {
		this.initialActions = initialActions;
	}

	public void setSteps(List steps) {
		this.steps = steps;
	}

	public List getSteps() {
		return steps;
	}

	public String getWftype() {
		return wftype;
	}

	public void setWftype(String wftype) {
		this.wftype = wftype;
	}

}
