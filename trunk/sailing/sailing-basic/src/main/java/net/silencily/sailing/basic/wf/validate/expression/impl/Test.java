package net.silencily.sailing.basic.wf.validate.expression.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.entry.WorkflowStep;
import net.silencily.sailing.basic.wf.validate.expression.ClassExpressionValidate;

public class Test extends ClassExpressionValidate implements WorkflowInfo{

	public Set getSet() {
		Set set = new HashSet();
		set.add(new Double(1));
		set.add(new Double(2));
		set.add(new Double(3));
		set.add(new Double(4));
		set.add(new Double(5));
		return set;
	}
	public List getList() {
		List list = new ArrayList();
		list.add(new Double(10));
		list.add(new Double(11));
		list.add(new Double(12));
		list.add(new Double(13));
		list.add(new Double(14));
		return list;
	}
	public String getString() {
		return "1";
	}
	public int getInt() {
		return 1;
	}
	
	public String getActionId() {		
		return null;
	}

	public WorkflowStep getCurrentStep() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCurrentStepName() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getEmpCds() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getHistorySteps() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getInitialActions() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInitSplitActionId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNextStepName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProcessId() {
		// TODO Auto-generated method stub
		return null;
	}

	public List getSignerList() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTaskId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWorkflowEditPath(String oid) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWorkflowName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWorkflowStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWorkflowStatusName() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRoleSign() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSubFlow() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSuperFlow() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isWorkflowCanBeInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setActionId(String s) {
		// TODO Auto-generated method stub
		
	}

	public void setCurrentStep(WorkflowStep workflowstep) {
		// TODO Auto-generated method stub
		
	}

	public void setCurrentStepName(String currentStepName) {
		// TODO Auto-generated method stub
		
	}

	public void setEmpCds(List empCds) {
		// TODO Auto-generated method stub
		
	}

	public void setHistorySteps(List list) {
		// TODO Auto-generated method stub
		
	}

	public void setId(String s) {
		// TODO Auto-generated method stub
		
	}

	public void setInitialActions(List list) {
		// TODO Auto-generated method stub
		
	}

	public void setInitSplitActionId(String id) {
		// TODO Auto-generated method stub
		
	}

	public void setProcessId(String empCd) {
		// TODO Auto-generated method stub
		
	}

	public void setSignerList(List signerList) {
		// TODO Auto-generated method stub
		
	}

	public void setTaskId(String taskId) {
		// TODO Auto-generated method stub
		
	}

	public void setWorkflowStatus(String s) {
		// TODO Auto-generated method stub
		
	}

	public Map getData() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setData(Map map) {
		// TODO Auto-generated method stub
		
	}

	protected boolean doValidate(WorkflowInfo info) {
		return false;
	}
	public String getTaskName(String title) {
		// TODO Auto-generated method stub
		return null;
	}

}
