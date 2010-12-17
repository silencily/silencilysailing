package net.silencily.sailing.basic.wf.condition;

import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.util.WfUtils;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;


public class PassRoundApproverCondition implements Condition{

	public boolean passesCondition(Map tvar, Map args, PropertySet ps) throws WorkflowException {
		WorkflowInfo wfInfo = (WorkflowInfo)tvar.get("dto");
		WfUtils utils = new WfUtils();
		boolean flag = false;
		try {
			flag = utils.isLaster(wfInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkflowException();
		}
		
		return flag;
		
	}
}
