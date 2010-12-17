package net.silencily.sailing.basic.wf.condition;

import java.util.Map;

import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;

public class RoleCdCondition implements Condition {

	public boolean passesCondition(Map tvar, Map args, PropertySet ps)
			throws WorkflowException {
		return check(args);
	}

	private synchronized boolean check(Map args) {
		CurrentUser user = SecurityContextInfo.getCurrentUser();
		String roleCd = (String) args.get("role.code");
		String[] roleCdArray = new String[] {};
		roleCdArray = roleCd.split(",");
		for (int i = 0; i < roleCdArray.length; i++) {
			if (user.hasRoleCd(roleCdArray[i]))
				return true;
		}
		return false;
	}

}
