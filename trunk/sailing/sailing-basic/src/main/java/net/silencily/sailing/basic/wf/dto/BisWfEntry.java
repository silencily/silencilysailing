package net.silencily.sailing.basic.wf.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.entry.WorkflowAction;
import net.silencily.sailing.basic.wf.entry.WorkflowStep;
import net.silencily.sailing.basic.wf.util.BisWfServiceLocator;
import net.silencily.sailing.basic.wf.util.WfUtils;
import net.silencily.sailing.basic.wf.util.WorkflowUtils;
import net.silencily.sailing.hibernate3.EntityPlus;

import org.apache.commons.lang.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.Session;


public abstract class BisWfEntry extends EntityPlus implements WorkflowInfo,
		org.hibernate.classic.Lifecycle {
	// ~ WorkflowInfo's fields --
	
	protected static final String INTERVAL = ", ";

	private String workflowStatus = WorkflowInfo.STATUS_SCRATCH;

	private Map data = new HashMap();

	private List historySteps;

	private WorkflowStep currentStep;

	private String actionId;

	private List initialActions = new ArrayList();

	private String taskId;

	private String initSplitActionId;

	// private String currentStepName = new String("\u672A\u77E5\u73AF\u8282");

	private String currentStepName;

	public void setCurrentStepName(String currentStepName) {
		this.currentStepName = currentStepName;
	}

	public String getCurrentStepName() {
		if(EntityPlus.isReflectCopyEntity == super.getReflectCopyEntityProperties()){
			return null;
		}
		if (this.currentStep != null
				&& StringUtils.isNotBlank(currentStep.getName())) {
			currentStepName = currentStep.getName();
		} else if (StringUtils.isNotBlank(this.currentStepName)) {
			return this.currentStepName;
		} else {
			try {
				BisWfServiceLocator.getWorkflowService().loadWorkflowInfo(this,
						taskId);
				if (this.getCurrentStep() != null) {
					currentStepName = this.getCurrentStep().getName();
				} else {
					return "";
				}
			} catch (Exception e) {
				//实体bean加载时会抛出异常，返回一个空的字符串yangxl-08-6-12
				e.printStackTrace();
				return "";
			}
		}
		return currentStepName;
	}

	public String getNextStepName() {
		if (StringUtils.isNotBlank(initSplitActionId)) {
			String[] s = initSplitActionId.split(",");
			if (s != null && s.length > 1) {
				return s[1];
			}
		}
		return "";
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskId() {

		return taskId;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	//private boolean done = false;
	
	public WorkflowStep getCurrentStep() {
		if(EntityPlus.isReflectCopyEntity == super.getReflectCopyEntityProperties()){
			return null;
		}
		if (currentStep == null || StringUtils.isBlank(currentStep.getId())) {
		
		try {
			
				BisWfServiceLocator.getWorkflowService().loadWorkflowInfo(this,
						taskId);
				
			} catch (Exception e) {
				e.printStackTrace();
				return new WorkflowStep();
			}
		
		}
		if (currentStep == null) {
			return new WorkflowStep();
		} else {
			//if (done == false) {
				String pointType = BisWfServiceLocator.getWorkflowService()
						.getPointType(this.getWorkflowName(),
								currentStep.getId());
				currentStep.setPointType(pointType);
				String specialObject = BisWfServiceLocator.getWorkflowService()
						.getSpecialObject(this.getWorkflowName(),
								currentStep.getId());
				currentStep.setSpecialObject(specialObject);
				String roles = BisWfServiceLocator.getWorkflowService()
						.getRoles(this.getWorkflowName(), currentStep.getId());
				currentStep.setRoles(roles);
				List actions = currentStep.getAvailableActions();
				for (int i = 0; i < actions.size(); i++) {
					WorkflowAction wfAction = (WorkflowAction) actions.get(i);
					WorkflowStep nextStep = wfAction.getNextStep();
					if (nextStep != null) {
						String nextPointType = BisWfServiceLocator
								.getWorkflowService().getPointType(
										this.getWorkflowName(),
										nextStep.getId());
						nextStep.setPointType(nextPointType);

						String nextSpecialObject = BisWfServiceLocator
								.getWorkflowService().getSpecialObject(
										this.getWorkflowName(),
										nextStep.getId());
						nextStep.setSpecialObject(nextSpecialObject);

						String nextRoles = BisWfServiceLocator
								.getWorkflowService().getRoles(
										this.getWorkflowName(),
										nextStep.getId());
						nextStep.setRoles(nextRoles);
					}

				//}
				//done = true;
			}

		}
		return currentStep;
	}

	public void setCurrentStep(WorkflowStep currentStep) {
		this.currentStep = currentStep;
	}

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}

	public List getHistorySteps() {
		if(EntityPlus.isReflectCopyEntity == super.getReflectCopyEntityProperties()){
			return null;
		}
		if (this.historySteps == null) {
			try {
				BisWfServiceLocator.getWorkflowService().loadWorkflowInfo(this,
						taskId);
			} catch (Exception e) {
				return new ArrayList();
			}
		}
		return historySteps;
	}

	public void setHistorySteps(List historySteps) {
		this.historySteps = historySteps;
	}

	public List getInitialActions() {
		return initialActions;
	}

	public void setInitialActions(List initialActions) {
		this.initialActions = initialActions;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public String getWorkflowStatusName() {
		return WorkflowUtils.getWorkflowStatusName(getWorkflowStatus());
	}

	/**
	 * @see WorkflowUtils#isWorkflowCanBeInitialized(WorkflowInfo)
	 */
	public boolean isWorkflowCanBeInitialized() {
		return this.getWorkflowStatus() == null
				|| this.getWorkflowStatus().equals("scratch")
				|| WorkflowUtils
						.getWorkflowStatusName(this.getWorkflowStatus())
						.equals("\u672A\u77E5\u72B6\u6001");
	}

	public String getInitSplitActionId() {
		if (StringUtils.isNotBlank(initSplitActionId)) {
			String[] s = initSplitActionId.split(",");
			if (s != null && s.length > 0) {
				return s[0];
			}
		}
		return "";
	}

	public void setInitSplitActionId(String initSplitActionId) {
		this.initSplitActionId = initSplitActionId;
	}

	private List signerList = null;

	public List getSignerList() {
		if (signerList == null) {
			List list = new ArrayList();
			if (this.getEmpCds() != null && this.getEmpCds().size() > 0) {
				for (int i = 0; i < this.getEmpCds().size(); i++) {
					String empCd = (String) this.getEmpCds().get(i);
					FlowTaskInfo fti = new FlowTaskInfo();
					fti.setProcessId(empCd);
					list.add(fti);
				}
			}
			signerList = list;
		}
		return signerList;
	}

	public void setSignerList(List signerList) {
		this.signerList = signerList;
	}

	public boolean isRoleSign() {
		if (signerList != null && signerList.size() > 0) {
			FlowTaskInfo fti = (FlowTaskInfo) signerList.get(0);
			String roleSignFlag = fti.getRoleSignFlag();
			if (signerList.size() == 1 && "N".equals(roleSignFlag)) {
				if (!StringUtils.isBlank(fti.getRoleSignRole())) {
					return true;
				}
			}
		}
		return false;
	}

	private String processId;

	public String getProcessId() {
		return this.processId;
	}

	public void setProcessId(String empCd) {
		this.processId = empCd;
	}

	private List EmpCds = new ArrayList();

	public List getEmpCds() {
		return EmpCds;
	}

	public String getEmpCds(int index) {
		return (String) EmpCds.get(index);
	}

	public void setEmpCds(List empCds) {
		EmpCds = empCds;
	}

	public void setEmpCds(int index, String empcd) {
		EmpCds.add(empcd);
	}

	public boolean isSuperFlow() throws Exception {
		return new WfUtils().hasSubFlow(this);
	}

	public boolean isSubFlow() throws Exception {
		return new WfUtils().hasSupFlow(this);
	}

	public boolean onDelete(Session session) throws CallbackException {
		return false;
	}

	public void onLoad(Session session, Serializable serializable) {
//		 try {
//			if (WorkflowUtils.isInstanceOfWorkflowAndStatus(this)) {
//				javax.servlet.http.HttpSession httpSession = com.qware.security.SecurityContextInfo
//						.getSession();
//				ClonedObject clonedObj = ObjectOperateTools.clone(this);
//				Map map = (Map) httpSession.getAttribute("onLoadMap");
//				if (map == null)
//					map = new HashMap();
//				map.put(this, clonedObj);
//				httpSession.setAttribute("onLoadMap", map);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e.getMessage());
//		}
	}

	public boolean onSave(Session session) throws CallbackException {
		return false;
	}

	public boolean onUpdate(Session session) throws CallbackException {
//		boolean result = false;
//		 String record = "";
//		try {
//			if (WorkflowUtils.isInstanceOfWorkflowAndStatus(this)) {
//				javax.servlet.http.HttpSession httpSession = com.qware.security.SecurityContextInfo
//						.getSession();
//				Map map = (Map) httpSession.getAttribute("onLoadMap");
//				if (map == null)
//					throw new RuntimeException("the onLoadMap is null");
//				ClonedObject oldBean = (ClonedObject) map.get(this);
//				if (oldBean != null) {
//					record = ChangeRecordTools.getRecords(oldBean, this);
//				}
//				if (StringUtils.isNotBlank(record)) {
//					WorkflowInfo info = (WorkflowInfo) this;
//					Map recordMap = new HashMap();
//					String pk = BisWfServiceLocator.getWorkflowService()
//							.getPkForStep(info);
//					recordMap.put(pk, record);
//					httpSession.setAttribute("records", recordMap);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
		return false;
	}
	
	protected static Object isNull(Object obj){
		return obj == null ? "" : obj;
	}
}
