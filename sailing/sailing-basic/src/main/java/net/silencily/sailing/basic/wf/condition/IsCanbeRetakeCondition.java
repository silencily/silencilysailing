package net.silencily.sailing.basic.wf.condition;

import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.util.WfUtils;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;

public class IsCanbeRetakeCondition implements Condition {
	
	public boolean passesCondition(Map tvar, Map args, PropertySet ps)
			throws WorkflowException {
		boolean returnValue = false;
		WorkflowInfo wfInfo = (WorkflowInfo) tvar.get("dto");
		WfUtils util = new WfUtils();
		
		try {
			// 可以取回的两个条件
			// 1.没有人签过
			// 2.是我提交的任务
			// 3.查看curr_step_id上是否有取回操作
			// 4.curr_step能在那些步骤上取回。
			
			if (!util.isSomeOneSign(wfInfo) && util.isMyTaskOfCommited(wfInfo)
					&& util.isHasRetakeByStepId(wfInfo)) {	
				returnValue = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

}
