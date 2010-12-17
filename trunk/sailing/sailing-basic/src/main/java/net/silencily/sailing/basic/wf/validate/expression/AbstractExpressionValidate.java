package net.silencily.sailing.basic.wf.validate.expression;

import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;

public abstract class AbstractExpressionValidate implements ExpressionValidate {

	private Map params;
	
	public boolean validate(WorkflowInfo info) {
		boolean ret = false;
		ret = this.doValidate(info);
		return ret;
	}

	protected abstract boolean doValidate(WorkflowInfo info);

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}
}
