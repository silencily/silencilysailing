package net.silencily.sailing.basic.wf.validate;

import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;

public interface TransitionValidate {
	
	boolean validate(WorkflowInfo info, String nextStepId);
	
	void setParameters(Map params);
}
