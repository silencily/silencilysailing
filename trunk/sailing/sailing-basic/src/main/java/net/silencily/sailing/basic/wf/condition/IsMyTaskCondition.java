package net.silencily.sailing.basic.wf.condition;
import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.util.WfUtils;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;

public class IsMyTaskCondition implements Condition{

	public boolean passesCondition(Map tvar, Map arg, PropertySet ps) throws WorkflowException {
		boolean returnValue = false;
		WfUtils util = new WfUtils();		
		try{
			WorkflowInfo wfInfo = (WorkflowInfo)tvar.get("dto");
			returnValue = util.isMyTask(wfInfo);
		}catch(Exception e){
			e.printStackTrace();
			throw new WorkflowException("isMyTaskCondition exception.");
		}
		return returnValue;
	}

}
