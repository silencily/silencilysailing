package net.silencily.sailing.basic.wf.function;

import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.common.context.WorkFlowFormContext;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class SendMail implements FunctionProvider{

	public void execute(Map tvar, Map args, PropertySet ps) throws WorkflowException {
		WorkflowInfo wfInfo = (WorkflowInfo)tvar.get("dto");
		WorkFlowFormContext.workFlowSendMessage(wfInfo.getWorkflowName(), wfInfo.getCurrentStep().getName(), wfInfo.getWorkflowEditPath(wfInfo.getId()));
	}
}
