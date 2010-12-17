package net.silencily.sailing.basic.wf.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.wf.WorkflowConstants;
import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.WorkflowService;
import net.silencily.sailing.basic.wf.constant.WfUnionCodeConstants;
import net.silencily.sailing.basic.wf.constant.WorkflowOperation;
import net.silencily.sailing.basic.wf.db.WfDBConnection;
import net.silencily.sailing.basic.wf.entry.WorkflowAction;
import net.silencily.sailing.basic.wf.entry.WorkflowStep;
import net.silencily.sailing.basic.wf.util.ChangeRecordTools;
import net.silencily.sailing.basic.wf.util.WfUtils;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.security.SecurityContextInfo;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;
import com.opensymphony.workflow.AbstractWorkflow;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.WorkflowContext;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;
import com.opensymphony.workflow.loader.ConditionsDescriptor;
import com.opensymphony.workflow.loader.PermissionDescriptor;
import com.opensymphony.workflow.loader.ResultDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowStore;

public abstract class OsWorkflowService implements WorkflowService,
		WorkflowConstants {
	private static class SpringWorkflow extends AbstractWorkflow implements
			WorkflowContext {

		public String getCaller() {
			return SecurityContextInfo.getCurrentUser()
					.getEmpCd();
		}

		public void setRollbackOnly() {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}

		protected boolean transitionWorkflow(WorkflowEntry entry,
				List currentSteps, WorkflowStore store, WorkflowDescriptor wf,
				ActionDescriptor action, Map transientVars, Map inputs,
				PropertySet ps) throws WorkflowException {
			boolean ret = super.transitionWorkflow(entry, currentSteps, store,
					wf, action, transientVars, inputs, ps);
			List cur = store.findCurrentSteps(entry.getId());
			List his = store.findHistorySteps(entry.getId());
			WorkflowStep fromStep = createWorkflowStep(his, wf);
			WorkflowStep toStep = createWorkflowStep(cur, wf);
			Map map = new HashMap();
			map.put("dto", transientVars.get("dto"));
			map.put("current.user", map.get("current.user"));
			map.put("dto.id", transientVars.get("dto.id"));
			if (transientVars.get("data") != null) {
				Map m = (Map) transientVars.get("data");
				map.putAll(m);
			}
			WorkflowAction wa = new WorkflowAction();
			wa.setId(String.valueOf(action.getId()));
			wa.setName(action.getName());
			wa.setMetadata(action.getMetaAttributes());
			Map params = new HashMap();
			params.put("from.step", fromStep);
			params.put("to.step", toStep);
			params.put("action", wa);
			params.put("data", map);
			WorkflowTransitionEvent event = new WorkflowTransitionEvent(params);
			ServiceProvider.multicaster(event);
			return ret;
		}

		private WorkflowStep createWorkflowStep(List list, WorkflowDescriptor wf) {
			if (CollectionUtils.isEmpty(list))
				return null;
			Step step = (Step) list.get(0);
			if (step == null)
				return null;
			StepDescriptor desc = wf.getStep(step.getStepId());
			if (desc == null)
				return null;
			WorkflowStep s = new WorkflowStep();
            //历史步骤信息
            s.setEntryStepId(step.getId());
			s.setName(desc.getName());
			s.setId(String.valueOf(desc.getId()));
			s.setMetadata(desc.getMetaAttributes());
			s.setStartTime(step.getStartDate());
			s.setUsername(step.getCaller());
			s.setFinishTime(step.getFinishDate());
			Map m = wf.getMetaAttributes();
			if (m != null && m.containsKey("workflow.name"))
				s.setDescription((String) m.get("workflow.name"));
			return s;
		}

		protected boolean passesCondition(ConditionDescriptor conditionDesc,
				Map transientVars, PropertySet ps, int currentStepId)
				throws WorkflowException {
			Condition c = null;
			boolean passed;
			try {
				if (!cachedConditions.containsKey(conditionDesc)) {
					if (OsWorkflowService.logger.isDebugEnabled())
						OsWorkflowService.logger
								.debug("\u6CA1\u6709\u5728\u7F13\u5B58\u4E2D\u627E\u5230\u6307\u5B9A\u6761\u4EF6"
										+ conditionDesc.asXML());
					String type = conditionDesc.getType();
					Map args = new HashMap(conditionDesc.getArgs());
					java.util.Map.Entry mapEntry;
					for (Iterator iterator = args.entrySet().iterator(); iterator
							.hasNext(); mapEntry.setValue(getConfiguration()
							.getVariableResolver().translateVariables(
									(String) mapEntry.getValue(),
									transientVars, ps)))
						mapEntry = (java.util.Map.Entry) iterator.next();

					Condition condition = getResolver()
							.getCondition(type, args);
					if (condition == null) {
						context.setRollbackOnly();
						throw new WorkflowException("Could not load condition");
					}
					cachedConditions.put(conditionDesc, condition);
				}
				c = (Condition) cachedConditions.get(conditionDesc);

				passed = c.passesCondition(transientVars, conditionDesc
						.getArgs(), ps);
				if (conditionDesc.isNegate())
					passed = !passed;
			} catch (Exception e) {
				context.setRollbackOnly();
				if (e instanceof WorkflowException)
					throw (WorkflowException) e;
				else
					throw new WorkflowException(
							"Unknown exception encountered when checking condition "
									+ c, e);
			}
			return passed;
		}

		public boolean evalue(StepDescriptor stepDescriptor, Map params,
				List pricipals) {
			return false;
		}

		private static final long serialVersionUID = 5034299819439421855L;

		Map cachedConditions;

		public SpringWorkflow() {
			cachedConditions = Collections.synchronizedMap(new HashMap());
			super.context = this;
		}
	}

	public OsWorkflowService() {
		defaultOpinion = WorkflowConstants.DEFAULT_OPINION;
		abstractWorkflow = new SpringWorkflow();
	}

	public void setDefaultOpinion(String defaultOpinion) {
		this.defaultOpinion = defaultOpinion;
	}

	public List getTodoList(String workflowName) {
		return getTodoList(workflowName, null);
	}

	public List getTodoList(String workflowName, String stepNo) {
		long s1 = System.currentTimeMillis();
		verifyWorkflowName(workflowName);
		WorkflowEntryWithOid entries[] = null;
		if (StringUtils.isBlank(stepNo))
			entries = listAllActivatedWorkflowEntry(workflowName);
		else
			entries = listSpecifiedStepWorkflowEntry(workflowName, stepNo);
		if (logger.isDebugEnabled())
			logger
					.debug("\u6267\u884C\u68C0\u7D22\u5DE5\u4F5C\u6D41\u5B9E\u4F53["
							+ (System.currentTimeMillis() - s1) + "ms");
		Map map = new HashMap(2);
		map.put("current.role.code", getRolesOfUser());
		map.put("current.user", SecurityContextInfo
				.getCurrentUser());
		Set ret = new LinkedHashSet(entries.length);
		for (int i = 0; i < entries.length; i++) {
			map.put("dto.id", entries[i].getOid());
			SimpleStepWithSavedValues step = entries[i].getCurrentStep();
			if (step == null)
				continue;
			map.put("data", step.getOriginalKeyAndValue());
			if (abstractWorkflow
					.getSecurityPermissions(entries[i].getId(), map).size() > 0)
				ret.add(entries[i].getOid());
		}

		List list = new ArrayList(ret.size());
		list.addAll(ret);
		if (logger.isDebugEnabled())
			logger.debug("\u6267\u884CgetTodoList["
					+ (System.currentTimeMillis() - s1) + "ms");
		return list;
	}

	public String getCurrentStepId(WorkflowInfo info) {
		verifyWorkflowInfo(
				info,
				"\u52A0\u8F7D\u4E1A\u52A1\u5B9E\u4F53\u76F8\u5173\u7684\u5DE5\u4F5C\u6D41\u4FE1\u606F\u65F6");
		WorkflowEntryWithOid ww = listSpecifiedOidWorkflowEntry(info.getId());
		WorkflowDescriptor wd = abstractWorkflow.getWorkflowDescriptor(info
				.getWorkflowName());
		if (wd == null)
			throw new IllegalStateException(
					"\u6CA1\u6709\u52A0\u8F7D\u5230\u540D\u79F0\u662F["
							+ info.getWorkflowName()
							+ "]\u7684\u5DE5\u4F5C\u6D41\u5B9A\u4E49");
		StepDescriptor cad = null;
		int currentStep = 0;
		if (ww != null && ww.getCurrentStep() != null
				&& ww.getCurrentStep().getId() != 0L
				&& (cad = wd.getStep(ww.getCurrentStep().getStepId())) != null) {
			currentStep = ww.getCurrentStep().getStepId();
		}
		return currentStep == 0 ? ("\u672A\u77E5\u73AF\u8282") : String
				.valueOf(currentStep);
	}

	public void loadWorkflowInfoWithOutAction(WorkflowInfo info) {
		verifyWorkflowInfo(
				info,
				"\u52A0\u8F7D\u4E1A\u52A1\u5B9E\u4F53\u76F8\u5173\u7684\u5DE5\u4F5C\u6D41\u4FE1\u606F\u65F6");
		WorkflowEntryWithOid ww = listSpecifiedOidWorkflowEntry(info.getId());
		WorkflowDescriptor wd = abstractWorkflow.getWorkflowDescriptor(info
				.getWorkflowName());
		if (wd == null)
			throw new IllegalStateException(
					"\u6CA1\u6709\u52A0\u8F7D\u5230\u540D\u79F0\u662F["
							+ info.getWorkflowName()
							+ "]\u7684\u5DE5\u4F5C\u6D41\u5B9A\u4E49");
		Map map = populateMap(info);
		StepDescriptor cad = null;
		if (ww != null && ww.getCurrentStep() != null
				&& ww.getCurrentStep().getId() != 0L
				&& (cad = wd.getStep(ww.getCurrentStep().getStepId())) != null) {
			WorkflowStep step = new WorkflowStep();
			
            //步骤信息
            step.setEntryStepId(ww.getCurrentStep().getId());
            
			step.setStartTime(ww.getCurrentStep().getStartDate());
			step.setId(String.valueOf(ww.getCurrentStep().getStepId()));
			
			step.setName(cad.getName());
			step.setMetadata(((Map) (cad.getMetaAttributes() != null ? cad
					.getMetaAttributes() : ((Map) (new HashMap(10))))));
			step.getMetadata().put("step.entry.id",
					String.valueOf(ww.getCurrentStep().getId()));
			step.setHasPermission(Boolean.FALSE);
			step.setPrincipal(Collections.EMPTY_LIST);
			String wn = info.getWorkflowName();
			if (wd.getMetaAttributes() != null
					&& wd.getMetaAttributes().containsKey("workflow.name"))
				wn = (String) wd.getMetaAttributes().get("workflow.name");
			step.setDescription(wn);

			// if (abstractWorkflow.getSecurityPermissions(ww.getId(),
			// map).size() > 0) {
			step.setHasPermission(Boolean.TRUE);
			int actionIds[] = abstractWorkflow.getAvailableActions(ww.getId(),
					map);
			List actions = new ArrayList(actionIds.length);
			for (int i = 0; i < actionIds.length; i++) {
				if (WorkflowOperation.KILLED.equals("" + actionIds[i])) {
					ActionDescriptor ad = wd.getAction(actionIds[i]);
					WorkflowAction wa = new WorkflowAction();
					wa.setId(String.valueOf(actionIds[i]));
					wa.setName(ad.getName());
					wa.setMetadata(ad.getMetaAttributes());
					List results = ad.getConditionalResults();
					int nextStepId = 0;
					if (results != null && results.size() > 0) {
						ResultDescriptor result = (ResultDescriptor) results
								.get(0);
						nextStepId = result.getStep();
					} else {
						nextStepId = ad.getUnconditionalResult().getStep();
					}
					if (nextStepId == -1) {
						WorkflowStep nextStep = new WorkflowStep();
						nextStep.setId(step.getId());
						nextStep.setName(step.getName());
						wa.setNextStep(nextStep);
					} else if (nextStepId != 0) {
						StepDescriptor st = wd.getStep(nextStepId);
						if (st == null)
							throw new NullPointerException(
									"\u5DE5\u4F5C\u6D41\u914D\u7F6E\u9519\u8BEF:\u5DE5\u4F5C\u6D41"
											+ info.getWorkflowName()
											+ "\u6CA1\u6709\u5B9A\u4E49id="
											+ nextStepId + "\u7684\u6B65\u9AA4");
						WorkflowStep nextStep = new WorkflowStep();
						nextStep.setId(String.valueOf(nextStepId));
						nextStep.setName(st.getName());
						nextStep.setMetadata(st.getMetaAttributes());
						wa.setNextStep(nextStep);
					}
					actions.add(wa);
					break;
				}
			}

			step.setAvailableActions(actions);
			// }

			info.setCurrentStep(step);
		} else {
			info.setCurrentStep(null);
			if (WorkflowInfo.STATUS_FINISH.equals(info.getWorkflowStatus())) {
				info.setCurrentStepName(info.getWorkflowStatusName());
			}
		}
		List historySteps = ww == null ? Collections.EMPTY_LIST : ww
				.getHistorySteps();
		List workflowSteps = new ArrayList(historySteps.size());
		for (int i = 0; i < historySteps.size(); i++) {
			SimpleStepWithSavedValues hs = (SimpleStepWithSavedValues) historySteps
					.get(i);
			StepDescriptor hsd = wd.getStep(hs.getStepId());
			if (hsd == null)
				continue;
			WorkflowStep s = new WorkflowStep();
			
            //历史步骤信息
            s.setEntryStepId(hs.getId());
            
			s.setId(String.valueOf(hs.getStepId()));
			s.setName(hsd.getName());
			s.setStartTime(hs.getStartDate());
			s.setFinishTime(hs.getFinishDate());
			s.setUsername(hs.getCaller());
			s.setData(hs.getOriginalKeyAndValue());
			s.setOpinion((String) hs.getOriginalKeyAndValue().get("opinion"));
			Map meta = hsd.getMetaAttributes();
			s.setMetadata(meta != null ? meta : Collections.EMPTY_MAP);
			s.setActionId(String.valueOf(hs.getActionId()));
			ActionDescriptor ad = wd.getAction(hs.getActionId());
			if (ad != null) {
				WorkflowAction wa = new WorkflowAction();
				wa.setId(s.getActionId());
				wa.setName(ad.getName());
				s.setAction(wa);
			}
			workflowSteps.add(s);
		}
		this.setTerminateInfo(info, workflowSteps);
		this.setKilledInfo(info, workflowSteps);
		info.setHistorySteps(workflowSteps);

	}	
	
	public String getStepName(String name, int x) {
		String ret = "";
		WorkflowDescriptor wd = abstractWorkflow.getWorkflowDescriptor(name);
		if (wd != null) {
			StepDescriptor step = wd.getStep(x);		
			if (step != null) {
				ret = step.getName();
			}
		}
		return ret;
	}

	public void loadWorkflowInfo(WorkflowInfo info) {
		verifyWorkflowInfo(
				info,
				"\u52A0\u8F7D\u4E1A\u52A1\u5B9E\u4F53\u76F8\u5173\u7684\u5DE5\u4F5C\u6D41\u4FE1\u606F\u65F6");
		info.setInitialActions(fetchInitialActions(info));
		WorkflowEntryWithOid ww = listSpecifiedOidWorkflowEntry(info.getId());
		WorkflowDescriptor wd = abstractWorkflow.getWorkflowDescriptor(info
				.getWorkflowName());
		if (wd == null)
			throw new IllegalStateException(
					"\u6CA1\u6709\u52A0\u8F7D\u5230\u540D\u79F0\u662F["
							+ info.getWorkflowName()
							+ "]\u7684\u5DE5\u4F5C\u6D41\u5B9A\u4E49");
		Map map = populateMap(info);
		StepDescriptor cad = null;
		if (ww != null && ww.getCurrentStep() != null
				&& ww.getCurrentStep().getId() != 0L
				&& (cad = wd.getStep(ww.getCurrentStep().getStepId())) != null) {
			Map data = ww.getCurrentStep().getOriginalKeyAndValue();
			map.put("data", data);
			info.setData(data);
			WorkflowStep step = new WorkflowStep();
			info.setCurrentStep(step);
			
            //历史步骤信息
            step.setEntryStepId(ww.getCurrentStep().getId());
			
			step.setStartTime(ww.getCurrentStep().getStartDate());
			step.setId(String.valueOf(ww.getCurrentStep().getStepId()));
			
			step.setName(cad.getName());
			step.setMetadata(((Map) (cad.getMetaAttributes() != null ? cad
					.getMetaAttributes() : ((Map) (new HashMap(10))))));
			step.getMetadata().put("step.entry.id",
					String.valueOf(ww.getCurrentStep().getId()));
			step.setHasPermission(Boolean.FALSE);
			step.setPrincipal(Collections.EMPTY_LIST);
			String wn = info.getWorkflowName();
			if (wd.getMetaAttributes() != null
					&& wd.getMetaAttributes().containsKey("workflow.name"))
				wn = (String) wd.getMetaAttributes().get("workflow.name");
			step.setDescription(wn);
			if (abstractWorkflow.getSecurityPermissions(ww.getId(), map).size() > 0) {
				if (!WorkflowInfo.STATUS_LOCKUP
						.equals(info.getWorkflowStatus())) {
					step.setHasPermission(Boolean.TRUE);
					int actionIds[] = abstractWorkflow.getAvailableActions(ww
							.getId(), map);
					List actions = new ArrayList(actionIds.length);
					for (int i = 0; i < actionIds.length; i++) {
						ActionDescriptor ad = wd.getAction(actionIds[i]);
						WorkflowAction wa = new WorkflowAction();
						wa.setId(String.valueOf(actionIds[i]));
						wa.setName(ad.getName());
						wa.setMetadata(ad.getMetaAttributes());
						List results = ad.getConditionalResults();
						int nextStepId = 0;
						if (results != null && results.size() > 0) {
							ResultDescriptor result = (ResultDescriptor) results
									.get(0);
							nextStepId = result.getStep();
						} else {
							nextStepId = ad.getUnconditionalResult().getStep();
						}
						if (nextStepId == -1) {
							WorkflowStep nextStep = new WorkflowStep();
							nextStep.setId(step.getId());
							nextStep.setName(step.getName());
							wa.setNextStep(nextStep);
						} else if (nextStepId != 0) {
							StepDescriptor st = wd.getStep(nextStepId);
							if (st == null)
								throw new NullPointerException(
										"\u5DE5\u4F5C\u6D41\u914D\u7F6E\u9519\u8BEF:\u5DE5\u4F5C\u6D41"
												+ info.getWorkflowName()
												+ "\u6CA1\u6709\u5B9A\u4E49id="
												+ nextStepId
												+ "\u7684\u6B65\u9AA4");
							WorkflowStep nextStep = new WorkflowStep();
							nextStep.setId(String.valueOf(nextStepId));
							nextStep.setName(st.getName());
							nextStep.setMetadata(st.getMetaAttributes());
							wa.setNextStep(nextStep);
						}
						if (this.isRetakefilter(ad.getName(), 
												getLastStepId(ww == null 
														? Collections.EMPTY_LIST : ww.getHistorySteps()), 
												nextStepId)) {
							actions.add(wa);
						}
						
					}

					step.setAvailableActions(actions);
				}

			}

		} else {
			info.setCurrentStep(null);
			if (WorkflowInfo.STATUS_FINISH.equals(info.getWorkflowStatus())) {
				info.setCurrentStepName(info.getWorkflowStatusName());
			}
		}
		List historySteps = ww == null ? Collections.EMPTY_LIST : ww
				.getHistorySteps();
		List workflowSteps = new ArrayList(historySteps.size());
		for (int i = 0; i < historySteps.size(); i++) {
			SimpleStepWithSavedValues hs = (SimpleStepWithSavedValues) historySteps
					.get(i);
			StepDescriptor hsd = wd.getStep(hs.getStepId());
			if (hsd == null)
				continue;
			WorkflowStep s = new WorkflowStep();
			s.setId(String.valueOf(hs.getStepId()));
			
            //历史步骤信息
            s.setEntryStepId(hs.getId());
            
			s.setName(hsd.getName());
			s.setStartTime(hs.getStartDate());
			s.setFinishTime(hs.getFinishDate());
			s.setUsername(hs.getCaller());
			Map date = hs.getOriginalKeyAndValue();
			try {
				date.put(WorkflowConstants.KEY_STEP_CHANGE_CONTENT,
						ChangeRecordTools.getRecord(info.getId() + "."
								+ hs.getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			s.setData(date);
			s.setOpinion((String) hs.getOriginalKeyAndValue().get("opinion"));
			Map meta = hsd.getMetaAttributes();
			s.setMetadata(meta != null ? meta : Collections.EMPTY_MAP);
			s.setActionId(String.valueOf(hs.getActionId()));
			ActionDescriptor ad = wd.getAction(hs.getActionId());
			if (ad != null) {
				WorkflowAction wa = new WorkflowAction();
				wa.setId(s.getActionId());
				wa.setName(ad.getName());
				s.setAction(wa);
			}
			workflowSteps.add(s);
		}
		this.setTerminateInfo(info, workflowSteps);
		this.setKilledInfo(info, workflowSteps);
		info.setHistorySteps(workflowSteps);
	}
	private void setTerminateInfo(WorkflowInfo info, List workflowSteps) {
		if (WorkflowInfo.STATUS_FINISH.equals(info.getWorkflowStatus())) {
			PropertySet ps = getPropertySetFromDtoId(info.getId());
			if("true".equals(ps.getString(info.getId()))) {
				WorkflowStep lastStep = (WorkflowStep)workflowSteps.get(workflowSteps.size() - 1);
				WorkflowAction action = new WorkflowAction();
				lastStep.setAction(action);
				action.setName(ps.getString(info.getId() + getPropertySetFromDtoId(info.getId())
				.getLong("workflow.id")));
				lastStep.setOpinion(ps.getString(info.getId() + ".opinion"));
			}
		}		
	}
	private void setKilledInfo(WorkflowInfo info, List workflowSteps) {
		if (WorkflowInfo.STATUS_KILLED.equals(info.getWorkflowStatus())) {
			WorkflowStep lastStep = (WorkflowStep)workflowSteps.get(workflowSteps.size() - 1);
			WorkflowAction action = new WorkflowAction();
			lastStep.setAction(action);
			action.setName("取消本流程");			
			PropertySet ps = getPropertySetFromDtoId(info.getId());
			lastStep.setOpinion(ps.getString(info.getId() + ".opinion"));
		}
	}
	private boolean isRetakefilter(String name, int last, int next) {
		boolean ret = false;
		if (name.indexOf(WfUnionCodeConstants.RETAKE) != -1) {
			ret = (last == next);
		} else {
			ret = true;
		}
		return ret;		
	}
	private int getLastStepId(List list) {
		int ret = 0;
		if (list != null && !list.isEmpty()) {
			SimpleStepWithSavedValues hs = (SimpleStepWithSavedValues) list
					.get(list.size() - 1);
			ret = hs.getStepId();
		}
		return ret;
	}

	public void doTransition(WorkflowInfo info, Map inputs) {
		verifyWorkflowInfo(info,
				"\u63D0\u4EA4\u5DE5\u4F5C\u6D41\u5230\u4E0B\u4E00\u6B65\u65F6");
		int aid = 0;
		try {
			aid = Integer.parseInt(info.getActionId());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"\u8981\u63D0\u4EA4\u7684action id\u4E0D\u662F\u6570\u5B57");
		}
		long wfId = getPropertySetFromDtoId(info.getId())
				.getLong("workflow.id");
		if (wfId == 0L)
			throw new IllegalStateException("\u4E1A\u52A1\u5B9E\u4F53[" + info
					+ "]\u8FD8\u6CA1\u6709\u5173\u8054\u5DE5\u4F5C\u6D41");
		Map map = populateMap(info);
		if (!CollectionUtils.isEmpty(inputs))
			map.putAll(inputs);
		List currentSteps = abstractWorkflow.getCurrentSteps(wfId);
		Assert
				.isTrue(
						currentSteps.size() != 0,
						"\u63D0\u4EA4\u4E1A\u52A1\u5BF9\u8C61\u65F6\u6CA1\u6709\u5F53\u524D\u6B65\u9AA4");
		Step step = (Step) currentSteps.get(0);
		map.put("data", fetchData(info.getId(), step.getId()));
		try {
			abstractWorkflow.doAction(wfId, aid, map);
			settingCurrentValues(info, wfId);
		} catch (Exception e) {
			throw new UnexpectedException(
					"\u4E0D\u80FD\u6267\u884C\u5DE5\u4F5C\u6D41\u52A8\u4F5C,\u5F53\u524D\u6B65\u9AA4config'id="
							+ step.getStepId()
							+ ",step'id="
							+ step.getId()
							+ "," + e.getMessage(), e);
		}
	}

	public void initializeWorkflow(WorkflowInfo info) {
		verifyWorkflowInfo(info, "\u542F\u52A8\u5DE5\u4F5C\u6D41\u65F6");
		Map map = populateMap(info);
		try {
			int aid = 0;
			try {
				aid = Integer.parseInt(info.getActionId());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						"\u8981\u63D0\u4EA4\u7684action id\u4E0D\u662F\u6570\u5B57");
			}
			long wfId = abstractWorkflow.initialize(info.getWorkflowName(),
					aid, map);
			if (logger.isDebugEnabled())
				logger
						.debug("\u521D\u59CB\u5316\u5DE5\u4F5C\u6D41\u5B8C\u6210, workflow id=["
								+ wfId
								+ "],\u4E1A\u52A1\u5B9E\u4F53id=["
								+ info.getId() + "]");
			abstractWorkflow.getPropertySet(wfId).setString("dto.id",
					info.getId());
			settingCurrentValues(info, wfId);
		} catch (Exception e) {
			throw new UnexpectedException(
					"\u4E0D\u80FD\u542F\u52A8\u5DE5\u4F5C\u6D41,"
							+ e.getMessage(), e);
		}
	}

	public void clear(WorkflowInfo info) {
		verifyWorkflowInfo(info,
				"\u6E05\u9664\u5DE5\u4F5C\u6D41\u4FE1\u606F\u65F6");
		long wfId = getPropertySetFromDtoId(info.getId())
				.getLong("workflow.id");
		if (wfId != 0L) {
			try {
				abstractWorkflow.changeEntryState(wfId, 3);
			} catch (WorkflowException e) {
				abstractWorkflow.setRollbackOnly();
				throw new UnexpectedException(
						"\u5220\u9664\u5DE5\u4F5C\u6D41\u4FE1\u606F[" + info
								+ "]\u9519\u8BEF," + e.getMessage(), e);
			}
			removeWorkflowEntry(wfId);
			removeAllEntryInPropertySet(abstractWorkflow.getPropertySet(wfId));
			removeAllEntryInPropertySet(getPropertySetFromDtoId(info.getId()));
		}
	}

	public void activate(WorkflowInfo info) {
		verifyWorkflowInfo(info, " activate \u5DE5\u4F5C\u6D41\u65F6 ");
		long wfId = getPropertySetFromDtoId(info.getId())
				.getLong("workflow.id");
		if (wfId != 0L)
			try {
				if (abstractWorkflow.canModifyEntryState(wfId, 1)) {
					info.setWorkflowStatus("processing");
					abstractWorkflow.changeEntryState(wfId, 1);
				}
			} catch (WorkflowException e) {
				abstractWorkflow.setRollbackOnly();
				throw new UnexpectedException(
						"activate \u5DE5\u4F5C\u6D41\u4FE1\u606F[" + info
								+ "]\u9519\u8BEF," + e.getMessage(), e);
			}

	}

	public void suspend(WorkflowInfo info) {
		verifyWorkflowInfo(info, " suspend \u5DE5\u4F5C\u6D41\u65F6 ");
		long wfId = getPropertySetFromDtoId(info.getId())
				.getLong("workflow.id");
		if (wfId != 0L)
			try {
				if (abstractWorkflow.canModifyEntryState(wfId, 2)) {
					info.setWorkflowStatus("suspend");
					abstractWorkflow.changeEntryState(wfId, 2);
				}
			} catch (WorkflowException e) {
				abstractWorkflow.setRollbackOnly();
				throw new UnexpectedException(
						"suspend \u5DE5\u4F5C\u6D41\u4FE1\u606F[" + info
								+ "]\u9519\u8BEF," + e.getMessage(), e);
			}

	}
	public void terminate(WorkflowInfo info, String value, String opinion) {		
		terminate(info);
		this.saveTerminateInfo(info, getPropertySetFromDtoId(info.getId())
				.getLong("workflow.id"), value, opinion);
	}
	public void terminate(WorkflowInfo info) {
		verifyWorkflowInfo(info, " finish \u5DE5\u4F5C\u6D41\u65F6 ");
		WfUtils util = new WfUtils();
		long wfId = getPropertySetFromDtoId(info.getId())
				.getLong("workflow.id");
		if (wfId != 0L) {
			try {
				if (abstractWorkflow.canModifyEntryState(wfId, 4)) {
					info.setWorkflowStatus("finish");
					abstractWorkflow.changeEntryState(wfId, 4);

					util.setTransactionConn(WfDBConnection.getConnection());
					util.postFlowTaskInfo(info,
							SecurityContextInfo
									.getCurrentUser());
				}
			} catch (Exception e) {
				abstractWorkflow.setRollbackOnly();
				throw new UnexpectedException(
						"finish \u5DE5\u4F5C\u6D41\u4FE1\u606F[" + info
								+ "]\u9519\u8BEF," + e.getMessage(), e);
			} finally {
				if (util.getTransactionConn() != null) {
					try {
						util.getTransactionConn().close();
					} catch (SQLException e) {

					}
				}
			}
		}
	}
	private void saveKillInfo(WorkflowInfo info, long wfId, String opinion) {
		PropertySet ps = getPropertySetFromDtoId(info.getId());		
		ps.setString(info.getId() + ".opinion", opinion == null ? "" : opinion);
	}
	public void killWorkflow(WorkflowInfo info, String opinion) {
		this.killWorkflow(info);
		this.saveKillInfo(info, getPropertySetFromDtoId(info.getId())
				.getLong("workflow.id"), opinion);
	}
	public void killWorkflow(WorkflowInfo info) {
		verifyWorkflowInfo(info, " kill \u5DE5\u4F5C\u6D41\u65F6 ");
		long wfId = getPropertySetFromDtoId(info.getId())
				.getLong("workflow.id");
		if (wfId != 0L)
			try {
				if (abstractWorkflow.canModifyEntryState(wfId, 3)) {
					abstractWorkflow.changeEntryState(wfId, 3);
					this.settingCurrentValues(info, wfId);
					info.setWorkflowStatus("killed");
					WfUtils.removeKilledFlowTask(info);
				}
			} catch (Exception e) {
				abstractWorkflow.setRollbackOnly();
				throw new UnexpectedException(
						"kill \u5DE5\u4F5C\u6D41\u4FE1\u606F[" + info
								+ "]\u9519\u8BEF," + e.getMessage(), e);
			}
	}

	private void verifyWorkflowName(String workflowName) {
		Assert.notNull(workflowName,
				"\u6CA1\u6709\u6307\u5B9A\u5DE5\u4F5C\u6D41\u540D\u79F0");
		if (ArrayUtils.indexOf(abstractWorkflow.getWorkflowNames(),
				workflowName) < 0)
			throw new IllegalArgumentException("\u6307\u5B9A\u540D\u79F0["
					+ workflowName
					+ "]\u7684\u5DE5\u4F5C\u6D41\u4E0D\u5B58\u5728");
		else
			return;
	}

	protected String[] getRolesOfUser() {
		List roles = ContextInfo.getCurrentUser().getRoles();
		return (String[]) roles.toArray(new String[0]);
	}

	protected abstract WorkflowEntryWithOid[] listAllActivatedWorkflowEntry(
			String s);

	protected abstract WorkflowEntryWithOid[] listSpecifiedStepWorkflowEntry(
			String s, String s1);

	protected abstract void removeWorkflowEntry(long l);

	protected abstract String listOidFromWorkflowEntryId(long l);

	protected abstract Map getDataFromPropertySet(String s);

	protected abstract List listHistoryStepByOid(String s);

	protected abstract WorkflowEntryWithOid listSpecifiedOidWorkflowEntry(
			String s);

	private void verifyWorkflowInfo(WorkflowInfo info, String title) {
		if (info == null || StringUtils.isBlank(info.getId())) {
			throw new NullPointerException(title
					+ ",dto\u6216dto's id\u662Fnull");
		} else {
			verifyWorkflowName(info.getWorkflowName());
			return;
		}
	}

	protected PropertySet getPropertySetFromDtoId(String id) {
		Map map = new HashMap(1);
		map.put("globalKey", "oid_" + id);
		return PropertySetManager.getInstance("jdbc", map);
	}

	private void removeAllEntryInPropertySet(PropertySet ps) {
		Collection c = ps.getKeys();
		for (Iterator it = c.iterator(); it.hasNext(); ps.remove((String) it
				.next()))
			;
	}
	private void saveTerminateInfo(WorkflowInfo info, long wfId, String value, String opinion) {
		PropertySet ps = getPropertySetFromDtoId(info.getId());
		ps.setString(info.getId() + wfId, value == null ? "" : value);
		ps.setString(info.getId(), "true");
		ps.setString(info.getId() + ".opinion", opinion == null ? "" : opinion);
	}
	private void settingCurrentValues(WorkflowInfo info, long wfId) throws Exception {
		PropertySet ps = getPropertySetFromDtoId(info.getId());
		String status = null;
		ps.setLong("workflow.id", wfId);
		List list = abstractWorkflow.getCurrentSteps(wfId);
		if (list.size() > 0) {
			Step currentStep = (Step) list.get(0);
			status = currentStep.getStatus();
			info.setWorkflowStatus(status);
			ps.setString("workflow.status", status);
			if (logger.isDebugEnabled())
				logger
						.debug("\u4FDD\u5B58\u5DE5\u4F5C\u6D41\u72B6\u6001WorkflowInfo.getWorkflowStatus(),key=[workflow.status],\u503C["
								+ status + "]");
			String prefix = String.valueOf(currentStep.getId()) + ".";
			Map data = info.getData();
			if (data != null) {
				Iterator it = data.keySet().iterator();
				do {
					if (!it.hasNext())
						break;
					String key = (String) it.next();
					if (isNotEmpty(data.get(key))) {
						ps.setAsActualType(prefix + key, data.get(key));
						if (logger.isDebugEnabled())
							logger
									.debug("\u4FDD\u5B58\u4E1A\u52A1\u6570\u636E\u5230\u5DE5\u4F5C\u6D41,\u5F53\u524D\u6B65\u9AA4["
											+ prefix
											+ "key=["
											+ key
											+ "],value=[" + data.get(key) + "]");
					}
				} while (true);
			}
		}

		List historySteps = abstractWorkflow.getHistorySteps(wfId);
		if (historySteps.size() > 0) {
			Step s = (Step) historySteps.get(0);
			if (WorkflowEntry.COMPLETED == abstractWorkflow.getEntryState(wfId)) {
				if (historySteps.size() > 1) {
					s = (Step) historySteps.get(1);
				}
			}

			String opinion = null;
			if (info.getCurrentStep() != null
					&& !StringUtils.isBlank(info.getCurrentStep().getId()))
				opinion = info.getCurrentStep().getOpinion();
			ps.setString("opinion." + s.getId(),
					StringUtils.isBlank(opinion) ? defaultOpinion : opinion);
			Map data = null;
			if (info.getCurrentStep() != null
					&& (data = info.getCurrentStep().getData()) != null
					&& !StringUtils.isBlank(info.getCurrentStep().getId())) {
				String prefix = String.valueOf(s.getId()) + ".";
				Iterator it = data.entrySet().iterator();
				do {
					if (!it.hasNext())
						break;
					java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
					String key = String.valueOf(entry.getKey());
					Object val = entry.getValue();
					if (isNotEmpty(val))
						ps.setAsActualType(prefix + key, val);
				} while (true);
			}
			if (status == null) {
				info.setWorkflowStatus(s.getStatus());
				ps.setString("workflow.status", s.getStatus());
			}
		}
	}

	private Map fetchData(String oid, long stepId) {
		Map data = getDataFromPropertySet(oid);
		Map ret = new HashMap(data.size());
		String key;
		Object value;
		for (Iterator it = data.entrySet().iterator(); it.hasNext(); ret.put(
				key, value)) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			key = (String) entry.getKey();
			value = entry.getValue();
			key = key.substring(String.valueOf(stepId).length() + 1);
		}

		return ret;
	}

	public String getPkForStep(WorkflowInfo info) {
		StringBuffer pk = new StringBuffer(info.getId() + ".");
		long wfId = getPropertySetFromDtoId(info.getId())
				.getLong("workflow.id");
		if (wfId != 0L) {
			List list = abstractWorkflow.getCurrentSteps(wfId);
			if (list.size() > 0) {
				Step step = (Step) list.get(0);
				pk.append(String.valueOf(step.getId()));
			}
		}
		return pk.toString();
	}

	public List fetchInitialActions(WorkflowInfo info) {
		WorkflowDescriptor wd = null;
		try {
			wd = abstractWorkflow.getConfiguration().getWorkflow(
					info.getWorkflowName());
		} catch (FactoryException e) {
			throw new UnexpectedException(
					"\u4E0D\u80FD\u83B7\u5F97\u5DE5\u4F5C\u6D41["
							+ info.getWorkflowName()
							+ "]\u7684\u521D\u59CB\u5316\u6B65\u9AA4", e);
		}
		List ads = wd.getInitialActions();
		List ret = new ArrayList(ads.size());
		Map map = populateMap(info);
		for (int i = 0; i < ads.size(); i++) {
			ActionDescriptor ad = (ActionDescriptor) ads.get(i);
			if (!abstractWorkflow.canInitialize(info.getWorkflowName(), ad
					.getId(), map))
				continue;
			WorkflowAction wa = new WorkflowAction();
			wa.setId(String.valueOf(ad.getId()));
			wa.setName(ad.getName());
			wa.setMetadata(ad.getMetaAttributes());
			if (wa.getMetadata() == null || wa.getMetadata().size() == 0)
				wa.setMetadata(new HashMap(1));
			wa.getMetadata().putAll(wd.getMetaAttributes());
			ret.add(wa);
		}

		return ret;
	}

	private Map populateMap(WorkflowInfo info) {
		Map map = new HashMap(5);
		map.put("dto", info);
		map.put("dto.id", info.getId());
		map.put("current.role.code", getRolesOfUser());
		map.put("current.user", SecurityContextInfo
				.getCurrentUser());
		return map;
	}

	public WorkflowStep getCurrentStep(WorkflowInfo info) {
		Assert.notNull(info, " workflowInfo required. ");
		Assert.notNull(info.getId(), "workflowInfo.getId() required");
		long wfId = getPropertySetFromDtoId(info.getId())
				.getLong("workflow.id");
		if (wfId != 0L) {
			List list = abstractWorkflow.getCurrentSteps(wfId);
			if (list.size() > 0) {
				Step step = (Step) list.get(0);
				StepDescriptor sd = abstractWorkflow.getWorkflowDescriptor(
						info.getWorkflowName()).getStep(step.getStepId());
				WorkflowStep s = new WorkflowStep();
	            //历史步骤信息
	            s.setEntryStepId(step.getId());
	            
				s.setId(String.valueOf(step.getStepId()));
				s.setName(sd.getName());
				s.setStartTime(step.getStartDate());
				List roles = sd.getPermissions();
				int i = 0;
				do {
					if (roles == null || i >= roles.size())
						break;
					PermissionDescriptor pd = (PermissionDescriptor) roles
							.get(i);
					if (pd == null)
						break;
					ConditionsDescriptor condsDesc = pd.getRestriction()
							.getConditionsDescriptor();
					if (condsDesc == null || condsDesc.getConditions() == null
							|| condsDesc.getConditions().size() == 0)
						break;
					for (int j = 0; j < condsDesc.getConditions().size(); j++) {
						if (!(com.opensymphony.workflow.loader.ConditionDescriptor.class)
								.isInstance(condsDesc.getConditions().get(j)))
							continue;
						ConditionDescriptor cd = (ConditionDescriptor) condsDesc
								.getConditions().get(j);
						if (cd != null || cd.getArgs() != null)
							s.getMetadata().putAll(cd.getArgs());
					}

					i++;
				} while (true);
				s.getMetadata().putAll(sd.getMetaAttributes());
				s.getMetadata().put("step.entry.id",
						String.valueOf(step.getId()));
				return s;
			}
		}
		return null;
	}

	public void fillHistorySteps(WorkflowInfo info) {
		if (info == null)
			throw new NullPointerException(
					"\u68C0\u7D22\u5386\u53F2\u6B65\u9AA4\u65F6\u53C2\u6570WorkflowInfo\u662Fnull");
		if (StringUtils.isBlank(info.getId())
				|| StringUtils.isBlank(info.getWorkflowName()))
			throw new IllegalArgumentException(
					"\u68C0\u7D22\u5386\u53F2\u6B65\u9AA4\u65F6\u53C2\u6570WorkflowInfo\u7684id\u6216workflowName\u662Fnull");
		WorkflowDescriptor wd = abstractWorkflow.getWorkflowDescriptor(info
				.getWorkflowName());
		if (wd == null)
			throw new IllegalArgumentException(
					"\u68C0\u7D22\u5386\u53F2\u6B65\u9AA4\u9519\u8BEF,\u6CA1\u6709\u5DE5\u4F5C\u6D41\u540D\u79F0["
							+ info.getWorkflowName() + "]");
		List workflowSteps = listHistoryStepByOid(info.getId());
		for (int i = 0; i < workflowSteps.size(); i++) {
			SimpleStepWithSavedValues hs = (SimpleStepWithSavedValues) workflowSteps
					.get(i);
			StepDescriptor hsd = wd.getStep(hs.getStepId());
			if (hsd != null) {
				WorkflowStep s = new WorkflowStep();
	            //历史步骤信息
	            s.setEntryStepId(hs.getId());
	            
				s.setId(String.valueOf(hs.getStepId()));
				s.setName(hsd.getName());
				s.setStartTime(hs.getStartDate());
				s.setFinishTime(hs.getFinishDate());
				s.setUsername(hs.getCaller());
				s.setData(hs.getOriginalKeyAndValue());
				s.setOpinion((String) hs.getOriginalKeyAndValue()
						.get("opinion"));
				Map meta = hsd.getMetaAttributes();
				s.setMetadata(meta != null ? meta : Collections.EMPTY_MAP);
				workflowSteps.add(s);
			}
		}

		info.setHistorySteps(workflowSteps);
	}

	public boolean exists(String workflowName) {
		if (StringUtils.isBlank(workflowName)) {
			throw new NullPointerException(
					"\u53C2\u6570workflowName\u662F\u7A7A\u503C");
		} else {
			String names[] = abstractWorkflow.getWorkflowNames();
			Arrays.sort(names);
			return Arrays.binarySearch(names, workflowName) > -1;
		}
	}

	private boolean isNotEmpty(Object value) {
		boolean notEmpty = value != null;
		if (notEmpty && (value instanceof String))
			notEmpty = StringUtils.isNotBlank((String) value);
		return notEmpty;
	}

	protected static Log logger;

	private String defaultOpinion;

	protected SpringWorkflow abstractWorkflow;

	static {
		logger = LogFactory.getLog(OsWorkflowService.class);
	}	
}
