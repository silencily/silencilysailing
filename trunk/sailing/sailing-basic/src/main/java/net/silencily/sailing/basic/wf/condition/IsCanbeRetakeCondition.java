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
			// ����ȡ�ص���������
			// 1.û����ǩ��
			// 2.�����ύ������
			// 3.�鿴curr_step_id���Ƿ���ȡ�ز���
			// 4.curr_step������Щ������ȡ�ء�
			
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
