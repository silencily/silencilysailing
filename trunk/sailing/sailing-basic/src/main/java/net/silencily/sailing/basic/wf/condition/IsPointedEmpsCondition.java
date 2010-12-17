package net.silencily.sailing.basic.wf.condition;

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;


public class IsPointedEmpsCondition implements Condition{

	public boolean passesCondition(Map tvar, Map args, PropertySet ps) throws WorkflowException {
		//2008-7-16 yangxl 具有取消操作的权限约束（注释）
		/*String emps = (String)args.get("pointed.emp");
		boolean flg = false;
		if (emps != null) {
			String empCd[] = emps.split(",");
			if (empCd != null && empCd.length > 0) {
				
				for (int i=0; i<empCd.length; i++) {
					if (i == 0) {
						flg = com.qware.security.SecurityContextInfo.getCurrentUser().hasRoleCd(empCd[0]);
					} else {
						String currEmpCd = SecurityContextInfo.getCurrentUser().getEmpCd();
						if (empCd[i].equals(currEmpCd)) {
							flg = true;
							break;
						}						
					}
				}				
			} 
		} */
		return true;
	}

}
