/**
 * @author yangxl
 *
 */
package net.silencily.sailing.basic.wf.domain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.silencily.sailing.basic.wf.constant.WorkflowOperation;
import net.silencily.sailing.basic.wf.domain.mate.Action;
import net.silencily.sailing.basic.wf.domain.mate.Arg;
import net.silencily.sailing.basic.wf.domain.mate.Condition;
import net.silencily.sailing.basic.wf.domain.mate.Conditions;
import net.silencily.sailing.basic.wf.domain.mate.Function;
import net.silencily.sailing.basic.wf.domain.mate.Permission;
import net.silencily.sailing.basic.wf.domain.mate.Result;
import net.silencily.sailing.basic.wf.domain.mate.RetakeInfo;
import net.silencily.sailing.basic.wf.domain.mate.Step;
import net.silencily.sailing.basic.wf.domain.mate.WorkFlowMeta;
import net.silencily.sailing.basic.wf.service.WFService;
import net.silencily.sailing.container.ServiceProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public final class WorkFlowMetaOperator {

	private static final long serialVersionUID = 1L;

	// ����Ҫ���ص�ʵ��
	private static WorkFlowMetaOperator instance = new WorkFlowMetaOperator();

	// ��־����
	private static final Logger logger = Logger
			.getLogger(WorkFlowMetaOperator.class.getName());;

	// ˽�еĹ��캯��
	private WorkFlowMetaOperator() {
	}

	// ��ȡWorkFlowMetaOperator��ʵ��
	public static WorkFlowMetaOperator getInstance() {
		logger.debug("ȡ��WorkFlowMetaOperator�Ķ���");
		return instance;
	}

	/*
	 * +save(workFlowMeta:WorkFlowMeta) ���޸ĺ��WorkFlowMeta�����޸� Author : yangxl
	 * Date : 2007-11-21
	 */
	public synchronized void save(WorkFlowMeta wfm, String dirs) {// WorkFlowMeta
		// wfinfo
		// ͨ�������ȡxml����Ҫ�Ľڵ��Ԫ��
		Document document = new Document();

		DocType docType = new DocType("workflow",
				"-//OpenSymphony Group//DTD OSWorkflow 2.6//EN",
				"http://www.opensymphony.com/osworkflow/workflow_2_8.dtd");
		document.setDocType(docType);
		Element root = new Element("workflow");
		document.setRootElement(root);
		// --------�����ڵ����-------
		Step st = new Step();
		Action action = new Action();
		Permission permission = new Permission();
		Conditions conditions = new Conditions();
		Result result = new Result();
		Result result0 = new Result();// Init
		Arg arg = new Arg();
		Arg arg0 = new Arg();// Init
		Condition condition = new Condition();
		Function fun = new Function();

		// ---------------- ����initial-actions--------------------------------
		Element newInitActions = new Element("initial-actions");
		root.addContent(newInitActions);
		Element newInitAction = new Element("action");
		newInitActions.addContent(newInitAction);
		if (wfm.getInitialActions() != null
				&& !("").equals(wfm.getInitialActions())) {
			if (wfm.getInitialActions().getId() != null
					&& !("").equals(wfm.getInitialActions().getId())) {
				Attribute InitactionId = new Attribute("id", wfm
						.getInitialActions().getId());
				newInitAction.setAttribute(InitactionId);
			}
			if (wfm.getInitialActions().getName() != null
					&& !("").equals(wfm.getInitialActions().getName())) {
				Attribute InitactionName = new Attribute("name", wfm
						.getInitialActions().getName());
				newInitAction.setAttribute(InitactionName);
			}
			Element newInitPreFunction = new Element("pre-functions");
			newInitAction.addContent(newInitPreFunction);
			Element newInitFunction = new Element("function");
			if(wfm.getInitialActions().getPrefunction()!=null 
					&& !("").equals(wfm.getInitialActions().getPrefunction())){
				Attribute InitactionFunctionType = new Attribute("type","class");
				newInitFunction.setAttribute(InitactionFunctionType);
			}
			newInitPreFunction.addContent(newInitFunction);
			Element newInitArg = new Element("arg");
			newInitFunction.addContent(newInitArg);
			if (wfm.getInitialActions().getPrefunction().getArgs() != null
					&& !("").equals(wfm.getInitialActions().getPrefunction()
							.getArgs())) {
				for (int a = 0; a < wfm.getInitialActions().getPrefunction()
						.getArgs().size(); a++) {
					arg0 = (Arg) wfm.getInitialActions().getPrefunction()
							.getArgs().get(a);
					if (arg0 != null && !("").equals(arg0)) {
						if (arg0.getName() != null
								&& !("").equals(arg0.getName())) {
							Attribute InitArgname = new Attribute("name", arg0
									.getName());
							newInitArg.setAttribute(InitArgname);
						}
						if (arg0.getValue() != null
								&& !("").equals(arg0.getValue())) {

							newInitArg.addContent(arg0.getValue());
						}
					}
				}
			}
			Element newInitResults = new Element("results");

			Element newInitUnconditionalResult = new Element(
					"unconditional-result");

			if (wfm.getInitialActions().getResults() != null
					&& !("").equals(wfm.getInitialActions().getResults())) {
				if (wfm.getInitialActions().getResults().size() == 1) {
					newInitAction.addContent(newInitResults);
					newInitResults.addContent(newInitUnconditionalResult);
					
				} else {
					newInitAction.addContent(newInitResults);
					newInitResults.addContent(newInitUnconditionalResult);					
				}
				for (int b = 0; b < wfm.getInitialActions().getResults().size(); b++) {
					result0 = (Result) wfm.getInitialActions().getResults()
							.get(b);
					if (result0 != null && !("").equals(result0)) {
						if (result0.getOldStatus() != null
								&& !("").equals(result0.getOldStatus())) {
							Attribute InitResultsUnReOldStatus = new Attribute(
									"old-status", result0.getOldStatus());
							newInitUnconditionalResult
									.setAttribute(InitResultsUnReOldStatus);
						}
						if (result0.getStatus() != null
								&& !("").equals(result0.getStatus())) {
							Attribute InitResultsUnReStatus = new Attribute(
									"status", result0.getStatus());
							newInitUnconditionalResult
									.setAttribute(InitResultsUnReStatus);
						}
						if (result0.getStep() != null
								&& !("").equals(result0.getStep())) {
							Attribute InitResultsUnReStep = new Attribute(
									"step", result0.getStep());
							newInitUnconditionalResult
									.setAttribute(InitResultsUnReStep);
						}
						if (result0.getOwner() != null
								&& !("").equals(result0.getOwner())) {
							Attribute InitResultsUnReOwner = new Attribute(
									"owner", result0.getOwner());
							newInitUnconditionalResult
									.setAttribute(InitResultsUnReOwner);
						}
					}
				}
			}

		}

		// --------------------����global-actions----------------------------------
		Element newGlobalActions = new Element("global-actions");
		root.addContent(newGlobalActions);
		Element newGlobalAction = new Element("action");
		newGlobalActions.addContent(newGlobalAction);
		if (wfm.getGlobalActions() != null
				&& !("").equals(wfm.getGlobalActions())) {
			if (wfm.getGlobalActions().getId() != null
					&& !("").equals(wfm.getGlobalActions().getId())) {
				Attribute GlobalactionId = new Attribute("id", wfm
						.getGlobalActions().getId());
				newGlobalAction.setAttribute(GlobalactionId);
			}
			if (wfm.getGlobalActions().getName() != null
					&& !("").equals(wfm.getGlobalActions().getName())) {
				Attribute GlobalactionName = new Attribute("name", wfm
						.getGlobalActions().getName());
				newGlobalAction.setAttribute(GlobalactionName);
			}
			if (wfm.getGlobalActions().getConditions() != null
					&& !("").equals(wfm.getGlobalActions().getConditions())) {
				conditions = (Conditions) wfm.getGlobalActions()
						.getConditions();
				Element newRestrictTo = new Element("restrict-to");
				newGlobalAction.addContent(newRestrictTo);
				Element newConditions = new Element("conditions");
				newRestrictTo.addContent(newConditions);
				if (conditions.getConditions() != null
						&& conditions.getConditions().size() > 0) {
					for (int y = 0; y < conditions.getConditions().size(); y++) {
						if (conditions.getConditions().get(y) != null
								&& !("").equals(conditions.getConditions().get(
										y))) {
							condition = (Condition) conditions.getConditions()
									.get(y);
						}
						Element newCondition = new Element("condition");
						newConditions.addContent(newCondition);
						if (conditions.getType() != null
								&& !("").equals(conditions.getType())) {
							Attribute conditionsType = new Attribute("type",
									conditions.getType());
							newConditions.setAttribute(conditionsType);
						}
						if (condition.getType() != null
								&& !("").equals(condition.getType())) {
							Attribute conditionType = new Attribute("type",
									condition.getType());
							newCondition.setAttribute(conditionType);
						}
						if (condition.getArgs() != null
								&& condition.getArgs().size() > 0) {
							for (int u = 0; u < condition.getArgs().size(); u++) {
								if (condition.getArgs().get(u) != null
										&& !("").equals(condition.getArgs()
												.get(u))) {
									arg = (Arg) condition.getArgs().get(u);
								}
								Element newArg = new Element("arg");
								newCondition.addContent(newArg);
								if (arg.getName() != null
										&& !("").equals(arg.getName())) {
									Attribute argName1 = new Attribute("name",
											arg.getName());
									newArg.setAttribute(argName1);
								}
								if (arg.getValue() != null
										&& !("").equals(arg.getValue())) {
									newArg.addContent(arg.getValue());
								}
							}
						}
					}
				}
			}

			if (wfm.getGlobalActions().getResults() != null
					&& wfm.getGlobalActions().getResults().size() > 0) {
				for (int k = 0; k < wfm.getGlobalActions().getResults().size(); k++) {
					if (wfm.getGlobalActions().getResults().get(k) != null
							&& !("").equals(wfm.getGlobalActions().getResults()
									.get(k))) {
						result = (Result) wfm.getGlobalActions().getResults()
								.get(k);
					}
					Element newResults = new Element("results");
					newGlobalAction.addContent(newResults);

					Element newUnconditionalresult = new Element(
							"unconditional-result");
					newResults.addContent(newUnconditionalresult);
					if (result.getOldStatus() != null
							&& !("").equals(result.getOldStatus())) {
						Attribute oldStatus = new Attribute("old-status",
								result.getOldStatus());// ������Ϊold-status,ֵΪWorkFlowMeta��������ԡ�
						newUnconditionalresult.setAttribute(oldStatus);// ���մ�����������ӵ�newUnconditionalresultԪ�ء�
					}
					if (result.getStatus() != null
							&& !("").equals(result.getStatus())) {
						Attribute status = new Attribute("status", result
								.getStatus());
						newUnconditionalresult.setAttribute(status);
					}
					if (result.getStep() != null
							&& !("").equals(result.getStep())) {
						Attribute step = new Attribute("step", result.getStep());
						newUnconditionalresult.setAttribute(step);
					}
				}
			}
		}
		if (wfm != null && !("").equals(wfm) && wfm.getSteps() != null
				&& wfm.getSteps().size() > 0) {
			// --------------����steps--------------------------------------------
			Element newSteps = new Element("steps");
			root.addContent(newSteps);
			for (int i = 0; i < wfm.getSteps().size(); i++) {
				if (wfm.getSteps().get(i) != null
						&& !("").equals(wfm.getSteps().get(i))) {
					st = (Step) wfm.getSteps().get(i);
				}
				
				//---------------�ֶ����ɽ�������---------------------------------
				int num = wfm.getSteps().size()-1;
				if(num == i){
					Element newend = new Element("step");
					newSteps.addContent(newend);
					Attribute newendid = new Attribute("id",st.getId());
					Attribute newendname = new Attribute("name","����");
					newend.setAttribute(newendid);
					newend.setAttribute(newendname);
					Element newendactions = new Element("actions");
					newend.addContent(newendactions);
					Element newendaction = new Element("action");
					Attribute newendactionname = new Attribute("name","����");
					Attribute newendactionid = new Attribute("id",WorkflowOperation.TO_END);
					Attribute newendactionfinish = new Attribute("finish","TRUE");
					Attribute newendactionauto = new Attribute("auto","TRUE");
					newendaction.setAttribute(newendactionname);
					newendaction.setAttribute(newendactionid);
					newendaction.setAttribute(newendactionfinish);
					newendaction.setAttribute(newendactionauto);
					newendactions.addContent(newendaction);
					Element newendresults = new Element("results");
					newendaction.addContent(newendresults);
					Element newendresultsunconditional = new Element("unconditional-result");
					newendresults.addContent(newendresultsunconditional);
					Attribute newendresultsunconditionaloldstatus = new Attribute("old-status","finish");
					Attribute newendresultsunconditionalstatus = new Attribute("status","finish");
					newendresultsunconditional.setAttribute(newendresultsunconditionaloldstatus);
					newendresultsunconditional.setAttribute(newendresultsunconditionalstatus);
					break;
				}
				//-----------------------------------------------------------------------
				
				// ------------------����step--------------------------------------
				Element newStep = new Element("step");
				if (st.getId() != null && !("").equals(st.getId())) {
					Attribute stepId = new Attribute("id", st.getId());
					newStep.setAttribute(stepId);
				}
				if (st.getName() != null && !("").equals(st.getName())) {
					Attribute stepName = new Attribute("name", st.getName());
					newStep.setAttribute(stepName);
				}

				// --------------------����external-permissions-----------------
				if (st.getPermission() != null
						&& !("").equals(st.getPermission())) {
					permission = (Permission) st.getPermission();
					Element newExternalPermission = new Element(
							"external-permissions");
					newStep.addContent(newExternalPermission);
					Element newPermission = new Element("permission");
					newExternalPermission.addContent(newPermission);
					if (permission.getName() != null
							&& !("").equals(permission.getName())) {
						Attribute permissionName = new Attribute("name",
								permission.getName());
						newPermission.setAttribute(permissionName);
					}
					Element newRestrictTo = new Element("restrict-to");
					newPermission.addContent(newRestrictTo);
					Element newConditions = new Element("conditions");
					newRestrictTo.addContent(newConditions);
					if (permission.getConditions() != null
							&& !("").equals(permission.getConditions())) {
						if (permission.getConditions().getConditions() != null
								&& permission.getConditions().getConditions()
										.size() > 0) {
							for (int z = 0; z < permission.getConditions()
									.getConditions().size(); z++) {
								if (permission.getConditions().getConditions()
										.get(z) != null
										&& !("").equals(permission
												.getConditions()
												.getConditions().get(z))) {
									condition = (Condition) permission
											.getConditions().getConditions()
											.get(z);
								}
								Element newCondition = new Element("condition");
								newConditions.addContent(newCondition);
								if (permission.getConditions().getType() != null
										&& !("").equals(permission
												.getConditions().getType())) {
									Attribute conditionsType = new Attribute(
											"type", permission.getConditions()
													.getType());
									newConditions.setAttribute(conditionsType);
								}
								if (condition.getArgs() != null
										&& condition.getArgs().size() > 0) {
									if (condition.getType() != null
											&& !("")
													.equals(condition.getType())) {
										Attribute conditionType = new Attribute(
												"type", condition.getType());
										newCondition
												.setAttribute(conditionType);
									}
									for (int u = 0; u < condition.getArgs()
											.size(); u++) {
										if (condition.getArgs().get(u) != null
												&& !("").equals(condition
														.getArgs().get(u))) {
											arg = (Arg) condition.getArgs()
													.get(u);
										}
										Element newArg = new Element("arg");
										newCondition.addContent(newArg);
										if (arg.getName() != null
												&& !("").equals(arg.getName())) {
											Attribute argName1 = new Attribute(
													"name", arg.getName());
											newArg.setAttribute(argName1);
										}
										if (arg.getValue() != null
												&& !("").equals(arg.getValue())) {
											newArg.addContent(arg.getValue());
										}
									}
								}
							}
						}
					}
				}
				// ------------------����actions-----------------------------------
				newSteps.addContent(newStep);
				Element newActions = new Element("actions");
				newStep.addContent(newActions);

				// --------------------����Action------------------------------------
				if (st.getActions() != null && st.getActions().size() > 0) {
					for (int j = 0; j < st.getActions().size(); j++) {
						if (st.getActions().get(j) != null
								&& !("").equals(st.getActions().get(j))) {
							action = (Action) st.getActions().get(j);
						}
						Element newAction = new Element("action");
						newActions.addContent(newAction);
						if (action.getName() != null
								&& !("").equals(action.getName())) {
							Attribute actionName = new Attribute("name", action
									.getName());// ������Ϊ
							// name,ֵΪActionl�����name�����ԡ�
							newAction.setAttribute(actionName);// ��������ӵ�ActionԪ��
						}
						if (action.getId() != null
								&& !("").equals(action.getId())) {
							Attribute actionId = new Attribute("id", action
									.getId());// ������Ϊid,ֵΪAction�����id������
							newAction.setAttribute(actionId);// ͬ��
						}

						if (action.isFinish()) {
							Attribute actionfinish = new Attribute("finish",
									"TRUE");
							newAction.setAttribute(actionfinish);
						}
						if (action.isAuto()) {
							Attribute actionAuto = new Attribute("auto", "TRUE");
							newAction.setAttribute(actionAuto);
						}
						// ----------����restrict-to---------------------------
						if (action.getConditions() != null
								&& !("").equals(action.getConditions())) {
							conditions = (Conditions) action.getConditions();
							Element newRestrictTo = new Element("restrict-to");
							newAction.addContent(newRestrictTo);
							Element newConditions = new Element("conditions");
							newRestrictTo.addContent(newConditions);
							if (conditions.getConditions() != null
									&& conditions.getConditions().size() > 0) {
								for (int y = 0; y < conditions.getConditions()
										.size(); y++) {
									if (conditions.getConditions().get(y) != null
											&& !("").equals(conditions
													.getConditions().get(y))) {
										condition = (Condition) conditions
												.getConditions().get(y);
									}
									Element newCondition = new Element(
											"condition");
									newConditions.addContent(newCondition);
									if (conditions.getType() != null
											&& !("").equals(conditions
													.getType())) {
										Attribute conditionsType = new Attribute(
												"type", conditions.getType());
										newConditions
												.setAttribute(conditionsType);
									}
									if (condition.getType() != null
											&& !("")
													.equals(condition.getType())) {
										Attribute conditionType = new Attribute(
												"type", condition.getType());
										newCondition
												.setAttribute(conditionType);
									}
									if (condition.getArgs() != null
											&& condition.getArgs().size() > 0) {
										for (int u = 0; u < condition.getArgs()
												.size(); u++) {
											if (condition.getArgs().get(u) != null
													&& !("").equals(condition
															.getArgs().get(u))) {
												arg = (Arg) condition.getArgs()
														.get(u);
											}
											Element newArg = new Element("arg");
											newCondition.addContent(newArg);
											if (arg.getName() != null
													&& !("").equals(arg
															.getName())) {
												Attribute argName1 = new Attribute(
														"name", arg.getName());
												newArg.setAttribute(argName1);
											}
											if (arg.getValue() != null
													&& !("").equals(arg
															.getValue())) {
												newArg.addContent(arg
														.getValue());
											}
										}
									}
								}
							}
						}
						// ---------------------����pre-functions---------------------------
						if (action.getPrefunction() != null
								&& !("").equals(action.getPrefunction())) {
							Element newPreFun = new Element("pre-functions");
							newAction.addContent(newPreFun);
							Element newFun1 = new Element("function");
							newPreFun.addContent(newFun1);
							fun = action.getPrefunction();
							if (fun.getType() != null
									&& !("").equals(fun.getType())) {
								Attribute funtype1 = new Attribute("type", fun
										.getType());
								newFun1.setAttribute(funtype1);
							}
							if (fun.getArgs() != null
									&& fun.getArgs().size() > 0) {
								for (int q = 0; q < fun.getArgs().size(); q++) {
									if (fun.getArgs().get(q) != null
											&& !("").equals(fun.getArgs()
													.get(q))) {
										arg = (Arg) fun.getArgs().get(q);
									}
									Element newFunArg1 = new Element("arg");
									newFun1.addContent(newFunArg1);
									if (arg.getName() != null
											&& !("").equals(arg.getName())) {
										Attribute funArgName = new Attribute(
												"name", arg.getName());
										newFunArg1.setAttribute(funArgName);
									}
									if (arg.getValue() != null
											&& !("").equals(arg.getValue())) {
										newFunArg1.addContent(arg.getValue());
									}
								}
							}
						}
						// ---------------------����results----------------------------------------
						if (action.getResults() != null
								&& action.getResults().size() > 0) {
							Element newResults = new Element("results");
							newAction.addContent(newResults);
							for (int k = 0; k < action.getResults().size(); k++) {
								if (action.getResults().get(k) != null
										&& !("").equals(action.getResults()
												.get(k))) {
									result = (Result) action.getResults()
											.get(k);
								}
								try{
									if (result.getConditions().getConditions().size() > k
												&& result.getConditions().getConditions().get(k) != null
												&& !("").equals(result.getConditions().getConditions().get(k))) {
											Element newResult = new Element(
													"result");
											if (action.getResults().size() != 1) {
												newResults
														.addContent(newResult);
											}

											if (result.getOldStatus() != null
													&& !("").equals(result
															.getOldStatus())) {
												Attribute oldStatus = new Attribute(
														"old-status", result
																.getOldStatus());// ������Ϊold-status,ֵΪWorkFlowMeta��������ԡ�
												newResult
														.setAttribute(oldStatus);// ���մ�����������ӵ�newUnconditionalresultԪ�ء�
											}
											if (result.getStatus() != null
													&& !("").equals(result
															.getStatus())) {
												Attribute status = new Attribute(
														"status", result
																.getStatus());
												newResult.setAttribute(status);
											}
											if (result.getStep() != null
													&& !("").equals(result
															.getStep())) {
												Attribute step = new Attribute(
														"step", result
																.getStep());
												newResult.setAttribute(step);
											}

											Element newConditions = new Element(
													"conditions");
											newResult.addContent(newConditions);
											if (result.getConditions() != null
													&& result.getConditions()
															.getConditions()
															.size() > 0) {
												for (int h = 0; h < result
														.getConditions()
														.getConditions().size(); h++) {
													if (result.getConditions()
															.getConditions()
															.get(h) != null
															&& !("")
																	.equals(result
																			.getConditions()
																			.getConditions()
																			.get(
																					h))) {
														condition = (Condition) result
																.getConditions()
																.getConditions()
																.get(h);
													}
													Element newCondition = new Element(
															"condition");
													newConditions
															.addContent(newCondition);
													if (condition.getType() != null
															&& !("")
																	.equals(condition
																			.getType())) {
														Attribute conditinType = new Attribute(
																"type",
																condition
																		.getType());
														newCondition
																.setAttribute(conditinType);
													}
													if (condition.getArgs() != null
															&& condition
																	.getArgs()
																	.size() > 0) {
														for (int m = 0; m < condition
																.getArgs()
																.size(); m++) {
															if (condition
																	.getArgs()
																	.get(m) != null
																	&& !("")
																			.equals(condition
																					.getArgs()
																					.get(
																							m))) {
																arg = (Arg) condition
																		.getArgs()
																		.get(m);
															}
															Element newArg = new Element(
																	"arg");
															newCondition
																	.addContent(newArg);
															if (arg.getName() != null
																	&& !("")
																			.equals(arg
																					.getName())) {
																Attribute argName = new Attribute(
																		"name",
																		arg
																				.getName());
																newArg
																		.setAttribute(argName);
															}
															if (arg.getValue() != null
																	&& !("")
																			.equals(arg
																					.getValue())) {
																newArg
																		.addContent(arg
																				.getValue());
															}
														}
													}
												}
											}

									}
								}catch(Exception e) {
									Element newUnconditionalresult = new Element(
											"unconditional-result");

									newResults
											.addContent(newUnconditionalresult);

									if (result.getOldStatus() != null
											&& !("").equals(result
													.getOldStatus())) {
										Attribute oldStatus = new Attribute(
												"old-status", result
														.getOldStatus());// ������Ϊold-status,ֵΪWorkFlowMeta��������ԡ�
										newUnconditionalresult
												.setAttribute(oldStatus);// ���մ�����������ӵ�newUnconditionalresultԪ�ء�
									}
									if (result.getStatus() != null
											&& !("").equals(result.getStatus())) {
										Attribute status = new Attribute(
												"status", result.getStatus());
										newUnconditionalresult
												.setAttribute(status);
									}
									if (result.getStep() != null
											&& !("").equals(result.getStep())) {
										Attribute step = new Attribute("step",
												result.getStep());
										newUnconditionalresult
												.setAttribute(step);
									}
								}
							}
						}
						// ----------------------����post-functions----------------------------

						if (action.getPostfunction() != null
								&& !("").equals(action.getPostfunction())) {
							Element newPostFun = new Element("post-functions");
							newAction.addContent(newPostFun);
							Element newFun1 = new Element("function");
							newPostFun.addContent(newFun1);
							fun = action.getPostfunction();
							if (fun.getType() != null
									&& !("").equals(fun.getType())) {
								Attribute funtype1 = new Attribute("type", fun
										.getType());
								newFun1.setAttribute(funtype1);
							}
							if (fun.getArgs() != null
									&& fun.getArgs().size() > 0) {
								for (int q = 0; q < fun.getArgs().size(); q++) {
									if (fun.getArgs().get(q) != null
											&& !("").equals(fun.getArgs()
													.get(q))) {
										arg = (Arg) fun.getArgs().get(q);
									}
									Element newFunArg1 = new Element("arg");
									newFun1.addContent(newFunArg1);
									if (arg.getName() != null
											&& !("").equals(arg.getName())) {
										Attribute funArgName = new Attribute(
												"name", arg.getName());
										newFunArg1.setAttribute(funArgName);
									}
									if (arg.getValue() != null
											&& !("").equals(arg.getValue())) {
										newFunArg1.addContent(arg.getValue());
									}
								}

							}
						}

					}
				}
			}
		}
		// �ö�������xml
		String filename = "";
		String xmlName = wfm.getName().trim().toString().replace('.', '-');
		filename = dirs + "\\" + xmlName + "-workflow" + ".xml";
		System.out.println(filename);
		File file = new File(filename);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			Format format = Format.getPrettyFormat();
			format.setEncoding("gb2312");
			XMLOutputter xmlOut = new XMLOutputter(format);
			xmlOut.output(document, fw);

		} catch (IOException e) {			
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}

	}

	/*
	 * �õ�TblWfParticularInfo�������Ϣ Author : yangxl Date : 2007-11-21
	 */
	private TblWfParticularInfo seleteTblWfParticularInfo(
			TblWfOperationInfo wfinfo, Long stepId) {

		Set stepIdList = wfinfo.getTblWfParticularInfos();
		Iterator it = stepIdList.iterator();
		TblWfParticularInfo wfin = new TblWfParticularInfo();
		while (it.hasNext()) {
			TblWfParticularInfo wfinn = (TblWfParticularInfo) it.next();// �õ�TblWfParticularInfo����
			if (wfinn.getStepId().equals(stepId)) {
				wfin = wfinn;
				break;
			}
		}
		return wfin;
	}

	/*
	 * �õ�XML�����ļ�step�����Ϣ Author : yangxl Date : 2007-11-21
	 */
	public WorkFlowMeta populateBase(TblWfOperationInfo wfinfo, WorkFlowMeta wfm) {

		Set stepIdList = new HashSet();
		stepIdList = wfinfo.getTblWfParticularInfos();			
		Iterator it = stepIdList.iterator();
		while (it.hasNext()) {
			TblWfParticularInfo wfin = (TblWfParticularInfo) it.next();// �õ�TblWfParticularInfo����
			// ----------------------���빤����----------------------
			Action ac = new Action();
			ac.setId(WorkflowOperation.INIT);
			ac.setName("���빤����");
			Function fun = new Function();
			fun.setType("class");
			Arg arg = new Arg();
			List args = new ArrayList();
			args.add(arg);
			arg.setName("class.name");
			arg.setValue("com.opensymphony.workflow.util.Caller");
			Result result = new Result();
			result.setOldStatus("finish");
			result.setStatus("processing");
			result.setStep(wfin.getStepId().toString());
			result.setOwner("${caller}");
			List results = new ArrayList();
			results.add(result);
			fun.setArgs(args);
			ac.setResults(results);
			ac.setPrefunction(fun);

			wfm.setInitialActions(ac);
			wfm.setId(wfinfo.getId());
			wfm.setWftype(wfinfo.getWftype().toString());
			wfm.setName(wfinfo.getName());
			wfm.setBizId(wfinfo.getEdition().toString());
			break;
		}

		return wfm;
	}

	/*
	 * �õ�XML�����ļ���steps��Ϣ Author : yangxl Date : 2007-11-21
	 */
	public WorkFlowMeta populateSteps(TblWfOperationInfo wfinfo,
			WorkFlowMeta wfm) {

		// ����ִ��ȡ��������step
		StringBuffer availableSteps = new StringBuffer("");
		boolean flag = false;
		String userRole = "";
		String testStepId = "";
		StringBuffer messageSteps = new StringBuffer("");
		Set stepIdList = new HashSet();
		stepIdList = wfinfo.getTblWfParticularInfos();
		Iterator it = stepIdList.iterator();
		
		List steps = new ArrayList();
		String endId = WorkflowOperation.END;
		// �ű�ȡ�ص�step
		List fetchList = new ArrayList();
		//���˻ص�step
		String[] gobackStepIds = new String[]{};
		//��תָ���ڵ��step
		List pointStepList = new ArrayList();
		
		for (int i = 0; i < stepIdList.size(); i++) {

			TblWfParticularInfo wfin = (TblWfParticularInfo) it.next();// �õ�TblWfParticularInfo����

			String actionKey = wfin.getStepId().toString();
			Step step = new Step();
			Permission permission = new Permission();
			Conditions conditions = new Conditions();
			Condition condition1 = new Condition();
			Arg arg1 = new Arg();
			Arg arg2 = new Arg();
			
			// -------------Result result = new Result();1.10��
			List conditionsList = new ArrayList();
			List args = new ArrayList();
			List actions = new ArrayList();
			// -------------List results = new ArrayList();1.10��
			if (i == stepIdList.size() - 1) {
				endId = wfin.getStepId().toString();
			}
			step.setId(wfin.getStepId().toString());
			step.setName(wfin.getStepName());
			permission.setName(wfin.getStepName() + "����Ȩ��");// permission.setName
			
			condition1.setType("class");
			arg1.setName("class.name");
			arg1.setValue("com.qware.wf.condition.RoleCdCondition");
			arg2.setName("role.code");
			arg2.setValue(wfin.getAllRoleCd());
			args.add(arg1);
			args.add(arg2);
			conditionsList.add(condition1);
			String commitStep = wfin.getCommitStep();
			step.setPermission(permission);		
			
			/*if (("Y").equals(wfin.getCommit())) {// �ύ
				if(("Y").equals(wfin.getMessage())){//��Ϣ
					String stepId = wfin.getStepId().toString();
					messageSteps.append(stepId +=",");
					Arg arg3 = new Arg();
					arg3.setName("send.mail");
					arg3.setValue("true");
					args.add(arg3);
				}
			}*/
			
			if (StringUtils.isNotBlank(commitStep)){
				String[] commitSteps = commitStep.split(",");
				for (int z = 0; z < commitSteps.length; z++) {
					Action action = new Action();
					Function preFunction = getPreFunction();					
					action.setPrefunction(preFunction);	
					if(("Y").equals(wfin.getMessage())){//��Ϣ
						Arg arg3 = new Arg();
						arg3.setName("send.mail");
						arg3.setValue("true");
						action.setPostfunction(getPostFunction(arg3));
					}else{
						action.setPostfunction(getPostFunction(null));
					}
					//ָ�������� point.emp
					//ָ����ɫ point.role
					//ҵ��ָ�� point.business
					if ("point.emp".equals(wfin.getHelpman())) {
						
					} else if ("point.role".equals(wfin.getHelpman())) {
//						Map map = new HashMap();
//						map.put(commitSteps[z], wfin.getTblUserId());
//						pointStepList.add(map);
//						Arg arg = new Arg();
//						arg.setName("next.role.code");
//						arg.setValue("");
//						postFunction = getPostFunction(arg);
//						action.setPostfunction(postFunction);
					} else if ("point.business".equals(wfin.getHelpman())) {
						
					}

					Result result = new Result();
					List results = new ArrayList();
					action.setName("�ύ��"
							+ seleteTblWfParticularInfo(wfinfo,
									new Long(Long.parseLong(commitSteps[z])))
									.getStepName() + "����");
					result.setStep(commitSteps[z]);
					//add start liyan 2008-03-07
					if (i == stepIdList.size() - 1) {
						if(commitSteps[z].equals("99")){
							action.setName("�ύ���������");
						}
						//result.setStep("99");
						//�ύ����������yangxl 2008-06-17
						result.setStep(commitSteps[z]);
					}
					//add end liyan 2008-03-07
					action.setId(actionKey + z);
					Conditions conditions0 = new Conditions();
					Condition condition0 = new Condition();
					condition0.setType("class");
					Arg arg0 = new Arg();
					Arg arg11 = new Arg();
					arg0.setName("class.name");
					arg0.setValue("com.qware.wf.condition.StatusCondition");
					arg11.setName("status");
					//arg11.setValue("processing");
					arg11.setValue("retake,untread,processing");
					List args0 = new ArrayList();
					args0.add(arg0);
					args0.add(arg11);
					condition0.setArgs(args0);
					List conditionsList0 = new ArrayList();
					conditionsList0.add(condition0);
					conditions0.setConditions(conditionsList0);
					action.setConditions(conditions0);
					result.setOldStatus("finish");
					result.setStatus("processing");
					//result.setStep(commitSteps[z]);
					result.setOwner("Owner");
					if (i == (stepIdList.size() - 2)) {
						if(commitSteps[z].equals("99")){
							action.setName("�ύ���������");
						}
						//result.setStep("99");
						//�ύ����������yangxl 2008-06-17
						result.setStep(commitSteps[z]);
						//delete start liyan 2008-03-05
						//action.setFinish(true);
						//result.setStatus("finish");
						//delete end liyan 2008-03-05
					}
					results.add(result);
					action.setResults(results);
					actions.add(action);
				}
			}
				condition1.setArgs(args);
				conditions.setConditions(conditionsList);
				permission.setConditions(conditions);
				step.setActions(actions);
			
			if (("Y").equals(wfin.getFetch())) {// ȡ��
				RetakeInfo retakeInfo = new RetakeInfo();
				String[] CommitstepIds = wfin.getCommitStep().split(",");

				userRole = wfin.getAllRoleCd();// ��ɫ���
				testStepId = wfin.getStepId().toString();
				retakeInfo.setCommitStepids(CommitstepIds);
				retakeInfo.setRoles(wfin.getAllRoleCd());
				retakeInfo.setStepId(wfin.getStepId().toString());
				retakeInfo.setStepName(wfin.getStepName());
				retakeInfo.setWorkflowName(wfinfo.getName());
				fetchList.add(retakeInfo);
				// ������step�ύ��ȥ�������ܱ�ȡ�أ�ÿһ��action��Ҫ��condition
				for (int x = 0; x < actions.size(); x++) {
					Action action = (Action) actions.get(x);
					Conditions conditions1 = action.getConditions();
					List condList = conditions1.getConditions();
					for (int y = 0; y < condList.size(); y++) {
						Condition oldCond = (Condition) condList.get(y);
						List argsList = oldCond.getArgs();
						for (int z = 0; z < argsList.size(); z++) {
							Arg oldArg = (Arg) argsList.get(z);
							if ("processing".equals(oldArg.getValue())) {
								oldArg.setValue("retake,untread,processing");
								if(condList.size() > 1){
									conditions1.setType("AND");
								}
							}
						}
					}
				}
				flag = true;				

			// }else if(("Y").equals(wfin.getPassround())){//����

			}
			if (("Y").equals(wfin.getGoback())) {// �˻�
				step.setGoBack(wfin);
				// ������step�ύ��ȥ�������ܱ��˻أ�ÿһ��action��Ҫ��condition

				RetakeInfo retakeInfo = new RetakeInfo();
				if(wfin.getGobackStep() != null){
					gobackStepIds = wfin.getGobackStep().split(",");
				}
			}
			if (("Y").equals(wfin.getCancel())) {// ȡ��
				String stepId = wfin.getStepId().toString();
				availableSteps.append(stepId += ",");				
			}
			if (("Y").equals(wfin.getSuspend())) {// ����
				step.setSuspendable(wfin);
			}
			if (("Y").equals(wfin.getTogethersign())) {// ��ǩ
				step.setTogetherSign();
			}

			
			//����end
			if(StringUtils.isBlank(commitStep)){
				// ����
				Result result = new Result();
				step.setPermission(null);
				step.setName("����");
				actions =null;
				actions = new ArrayList();
				Action action0 = new Action();
				actions.add(action0);
				action0.setFinish(true);
				action0.setName("����");
				action0.setId(WorkflowOperation.TO_END);
				action0.setAuto(true);
				action0.setConditions(null);
				action0.setPrefunction(null);
				action0.setPostfunction(null);
				result.setStep(null);
				result.setStatus("finish");
				result.setOldStatus("finish");
				List resultList = new ArrayList();
				resultList.add(result);
				action0.setResults(resultList);
			}
			steps.add(step);
			
		}
		for (int i = 0; i < steps.size() - 1; i++) {
			Step step = (Step) steps.get(i);
            //�����ȡ�ص�step�����˻ز����Ļ�������һ�±��˻ص���step��
			if (null != gobackStepIds) {
				for (int x = 0; x < gobackStepIds.length; x++) {
					if (StringUtils.isBlank(step.getId())
							|| StringUtils.isBlank(gobackStepIds[x])) {
						continue;
					} else if (step.getId().trim().equals(
							gobackStepIds[x].trim())) {
						step.updateGoBacked();
					}
				}
			}
			//����ȡ��step
			for (int j = 0; j < fetchList.size(); j++) {
				RetakeInfo retakeInfo = (RetakeInfo) fetchList.get(j);
				String[] commitStepIds = retakeInfo.getCommitStepids();
				
				for (int z = 0; z < commitStepIds.length; z++) {
					if (StringUtils.isBlank(step.getId())
							|| StringUtils.isBlank(commitStepIds[z])) {
						continue;
					} else if (step.getId().trim().equals(
							commitStepIds[z].trim())) {

						step.setFetch(retakeInfo.getStepId(), retakeInfo
								.getRoles(), retakeInfo.getStepName(), step
								.getStepRole());
						// ����step������acntion��role, ��step��permissions��role
					}
				}
			}
		}

		wfm.setSteps(steps);
		
		// --------------------ȡ������-------------------------
		// ����ȡ���ڵ�Action
		Action setCancelAction = new Action();
		setCancelAction.setName("ȡ��������");
		setCancelAction.setId(WorkflowOperation.KILLED);

		// ����ȡ���ڵ�Action��Conditions
		Conditions setCancelConditions = new Conditions();
		setCancelConditions.setType("AND");

		// ���õ�һ��Condition
		Condition setCancelCondition1 = new Condition();
		setCancelCondition1.setType("class");

		Arg setCancelCondition1Arg1 = new Arg();
		Arg setCancelCondition1Arg2 = new Arg();
		setCancelCondition1Arg1.setName("class.name");
		setCancelCondition1Arg1
				.setValue("com.qware.wf.condition.AvailableStepsCondition");
		setCancelCondition1Arg2.setName("availableSteps");
		if (StringUtils.isNotBlank(availableSteps.toString())) {
			setCancelCondition1Arg2.setValue(availableSteps.toString());
		} else {
			setCancelCondition1Arg2.setValue("nothing");
		}

		List setCancelCondition1ArgList = new ArrayList();
		setCancelCondition1ArgList.add(setCancelCondition1Arg1);
		setCancelCondition1ArgList.add(setCancelCondition1Arg2);
		setCancelCondition1.setArgs(setCancelCondition1ArgList);

		// ���õڶ���Condition
		Condition setCancelCondition2 = new Condition();
		setCancelCondition2.setType("class");

		Arg setCancelCondition2Arg1 = new Arg();
		Arg setCancelCondition2Arg2 = new Arg();
		setCancelCondition2Arg1.setName("class.name");
		//add start liyan 2008-03-07
		setCancelCondition2Arg1
				.setValue("com.qware.wf.condition.IsPointedEmpsCondition");
		setCancelCondition2Arg2.setName("pointed.emp");
		String flowManager = "";
		if(wfinfo.getFlowManager()!=null ){
			flowManager = wfinfo.getFlowManager();
		}
		String[] fms = flowManager.split(",");
		if (fms != null && fms.length > 0) {
			setCancelCondition2Arg2.setValue("systemRole," + flowManager);
		} else {
			setCancelCondition2Arg2.setValue("systemRole");
		}
		
		//add end liyan 2008-03-07
		List setCancelCondition2ArgList = new ArrayList();
		setCancelCondition2ArgList.add(setCancelCondition2Arg1);
		setCancelCondition2ArgList.add(setCancelCondition2Arg2);
		setCancelCondition2.setArgs(setCancelCondition2ArgList);

		// �Ѷ���������ӵ�Conditions��
		List setCancelConditionsList = new ArrayList();
		setCancelConditionsList.add(setCancelCondition1);
		setCancelConditionsList.add(setCancelCondition2);
		setCancelConditions.setConditions(setCancelConditionsList);

		// ���ý��
		Result setCancelResult = new Result();
		List setCancelResultList = new ArrayList();
		setCancelResult.setOldStatus("finish");
		setCancelResult.setStatus("killed");
		
		setCancelResult.setStep(endId);		
		setCancelResultList.add(setCancelResult);
		// ����Action
		setCancelAction.setConditions(setCancelConditions);
		setCancelAction.setResults(setCancelResultList);

		wfm.setGlobalActions(setCancelAction);

		return wfm;
	}

	// ����ǰ����
	private Function getPreFunction() {
		Function preFunction = new Function();
		String type = "class";
		Arg arg = new Arg();
		arg.setName("class.name");
		arg.setValue("com.qware.wf.function.PreTaskFunction");
		List args = new ArrayList();
		args.add(arg);
		preFunction.setType(type);
		preFunction.setArgs(args);
		return preFunction;
	}

	// ���ɺ���
	private Function getPostFunction(Arg arg2) {
		Function postFunction = new Function();
		String type = "class";
		Arg arg = new Arg();
		arg.setName("class.name");
		arg.setValue("com.qware.wf.function.PostTaskFunction");
		List args = new ArrayList();
		args.add(arg);
		if (arg2 != null)
			args.add(arg2);
		postFunction.setType(type);
		postFunction.setArgs(args);
		return postFunction;
	}

	private String getNextStepRole(String workflowName, Long currentStepId) {
		Long nextStepId = new Long(currentStepId.longValue() + 1);
		TblWfOperationInfo twoi = new TblWfOperationInfo();
		TblWfOperationInfo twoi2 = new TblWfOperationInfo();
		twoi.setName(workflowName);
		List wfList = ((WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME)).findsearch(twoi);
		if (wfList != null && wfList.size() > 0) {
			twoi2 = (TblWfOperationInfo) wfList.get(0);
		}
		Set stepSet = new HashSet(); 
		stepSet = twoi2.getTblWfParticularInfos();
		
		Iterator it = stepSet.iterator();
		String roles = "";
		while (it.hasNext()) {
			TblWfParticularInfo step = (TblWfParticularInfo) it.next();
			if (nextStepId.equals(step.getStepId())) {
				roles = step.getAllRoleCd();
			}
		}
		return roles;
	}

	/*
	 * �õ�XML�����ļ�����Ϣ Author : yangxl Date : 2007-11-21
	 */
	public WorkFlowMeta getWorkFlowMeta(TblWfOperationInfo wfInfo) {
		WorkFlowMeta wfm = new WorkFlowMeta();
		wfm = populateBase(wfInfo, wfm);
		return populateSteps(wfInfo, wfm);

	}
}
