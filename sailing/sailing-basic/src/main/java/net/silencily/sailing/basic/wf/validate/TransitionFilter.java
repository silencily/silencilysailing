package net.silencily.sailing.basic.wf.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.entry.WorkflowAction;
import net.silencily.sailing.basic.wf.validate.impl.AbstractTransitionValidate;

public class TransitionFilter {
	public static List filterActions(PageContext pageContext, WorkflowInfo info) {
		Map params = new HashMap();
		params.put("pageContext", pageContext);
		List actions = info.getCurrentStep().getAvailableActions();
		TransitionValidate tv = new AbstractTransitionValidate();
		tv.setParameters(params);
		List removeIndex = new ArrayList();
		for (int i=0; i<actions.size(); i++) {
			WorkflowAction action = (WorkflowAction)actions.get(i);
			if (!tv.validate(info, action.getNextStep().getId())) {
				removeIndex.add(String.valueOf(i));
			}
		}
		for (int i=removeIndex.size() - 1; i>=0; i--) {
			actions.remove(Integer.parseInt((String)removeIndex.get(i)));
		}
		
		return actions;
	}
	
}
