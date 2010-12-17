package net.silencily.sailing.basic.wf.validate.expression;

import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;

public interface ExpressionValidate {

	boolean validate(WorkflowInfo info);
	
	void setParams(Map params);
}
