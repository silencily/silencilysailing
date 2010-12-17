package net.silencily.sailing.basic.wf.web;

import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;

public abstract class BisWorkflowFormSupport extends BaseFormPlus {
	
	private String nextStepSpecialObj;

	private String taskId;
	
	private String forwardUrl;

	private boolean forwardFlag;

	private String urlKey;
	
	protected int operType;

	protected String listStep;

	protected boolean registry;
	
	// ¶¯×÷Ãû³Æ
	private String actionName;

	protected ComboSupportList qworkflowStatusList;

	public BisWorkflowFormSupport() {
	}

	public String getListStep() {
		return listStep;
	}

	public void setListStep(String listStep) {
		this.listStep = listStep;
	}

	public boolean isRegistry() {
		return registry;
	}

	public void setRegistry(boolean registry) {
		this.registry = registry;
	}

	public ComboSupportList getQworkflowStatusList() {
		if (qworkflowStatusList == null)
			qworkflowStatusList = WorkflowViewHelper.getWorkflowStatusList();
		qworkflowStatusList
				.setSelectedValues(getConditionValues("workflowStatus"));
		return qworkflowStatusList;
	}

	public void forwardUrl(String url) {
		this.forwardFlag = true;
		this.forwardUrl = url;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}

	public boolean isHasForwardUrl() {
		return forwardFlag;
	}

	public String getUrlKey() {
		return "&urlKey=" + urlKey;
	}

	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}

	public String getTaskId() {
		if(StringUtils.isBlank(taskId)){
			return this.request.getParameter("taskId");
		}
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getNextStepSpecialObj() {
		return nextStepSpecialObj;
	}

	public void setNextStepSpecialObj(String nextStepSpecialObj) {
		this.nextStepSpecialObj = nextStepSpecialObj;
	}

	public int getOperType() {
		return operType;
	}

	public void setOperType(int operType) {
		this.operType = operType;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

}
