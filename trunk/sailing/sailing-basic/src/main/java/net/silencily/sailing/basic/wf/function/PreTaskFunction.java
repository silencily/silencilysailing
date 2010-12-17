package net.silencily.sailing.basic.wf.function;

import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.util.WfUtils;
import net.silencily.sailing.common.message.MessageInsert;
import net.silencily.sailing.security.SecurityContextInfo;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class PreTaskFunction implements FunctionProvider{

	public void execute(Map tvar, Map args, PropertySet ps) throws WorkflowException {
		WorkflowInfo wfInfo = (WorkflowInfo) tvar.get("dto");
		if (!WfUtils.isTogetherSign(wfInfo.getWorkflowName(), wfInfo.getCurrentStep().getId())) {
			MessageInsert.endMessage(wfInfo.getTaskId());
		} else {
			MessageInsert.endMessage(wfInfo.getTaskId(), SecurityContextInfo.getCurrentUser().getUserId());
		}	
	}
}


