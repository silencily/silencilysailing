package net.silencily.sailing.basic.wf.domain.mate;

import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.basic.wf.constant.WorkflowOperation;
import net.silencily.sailing.basic.wf.domain.TblWfParticularInfo;


/**
 * ------------------ Author : yijinhua Date : 2007-11-13 ------------------
 */
public class Step {

	/*
	 * -id:String -name:String -permission:Permission -actions:List
	 * 
	 * +setUnFixed（）//非固定流程设定 +setTogetherSign()//会签设定 +setPassRound()//传阅设定
	 * +setSuspendable()//挂起设定 +setCancel()//取消设定 +setGoBack()//退回设定
	 * +setFetch()//取回
	 */

	private String id; // 步骤ID

	private String name; // 步骤名

	private Permission permission;// Action条件List

	private List actions; // ActionList

	// 设置共通角色
	private String getPermissionFirstRole() {
		String arg2Value = null;
		try {
			Conditions permissionCondtions = this.getPermission()
					.getConditions();
			List permissionCondtionList = permissionCondtions.getConditions();
			Condition condition = new Condition();
			if (permissionCondtionList.size() > 0) {
				condition = (Condition) permissionCondtionList.get(0);
			}
			List list = condition.getArgs();
			Arg arg2 = new Arg();
			if (list.size() > 1) {
				arg2 = (Arg) list.get(1);
			}
			arg2Value = arg2.getValue();
		} catch (Exception e) {
			return arg2Value;
		}
		return arg2Value;
	}

	// 取得ActionId
	private String getActionId() {
		String actionId = id;
		return actionId + this.getActions().size();
	}

	// 设置取回
	public void setFetch(String nextStepID, String role, String stepName,
			String role2) {
		try {
			// 给其他action负值。
			addRoleForRetake(role, role2);

			// ------------------Action------------------
			// 取回Action设置

			Action setFetchAction = new Action();
			//setFetchAction.setName("取回到" + stepName);
			setFetchAction.setName(stepName + "取回");
			setFetchAction.setId(getActionId());

			// 设置Conditions
			Conditions setFetchConditions = new Conditions();
			setFetchConditions.setType("AND");
			List setFetchConditionsList = new ArrayList();

			// 设置Action第一个条件
			Condition setFetchActionCondition1 = new Condition();
			setFetchActionCondition1.setType("class");
			List setFetchActionCondition1ArgList = new ArrayList();
			Arg setFetchActionCondition1Arg1 = new Arg();
			Arg setFetchActionCondition1Arg2 = new Arg();
			setFetchActionCondition1Arg1.setName("class.name");
			setFetchActionCondition1Arg1
					.setValue("com.qware.wf.condition.RoleCdCondition");
			String strRole = role;
			setFetchActionCondition1Arg2.setName("role.code");
			setFetchActionCondition1Arg2.setValue(strRole);
			setFetchActionCondition1ArgList.add(setFetchActionCondition1Arg1);
			setFetchActionCondition1ArgList.add(setFetchActionCondition1Arg2);
			setFetchActionCondition1.setArgs(setFetchActionCondition1ArgList);

			// 设置Action第二个条件
			Condition setFetchActionCondition2 = new Condition();
			setFetchActionCondition2.setType("class");
			List setFetchActionCondition2ArgList = new ArrayList();
			Arg setFetchActionCondition2Arg1 = new Arg();
			Arg setFetchActionCondition2Arg2 = new Arg();
			setFetchActionCondition2Arg1.setName("class.name");
			setFetchActionCondition2Arg1
					.setValue("com.qware.wf.condition.StatusCondition");
			setFetchActionCondition2Arg2.setName("status");
			setFetchActionCondition2Arg2.setValue("processing");
			setFetchActionCondition2ArgList.add(setFetchActionCondition2Arg1);
			setFetchActionCondition2ArgList.add(setFetchActionCondition2Arg2);
			setFetchActionCondition2.setArgs(setFetchActionCondition2ArgList);

			// 设置Action第三个条件
			Condition setFetchActionCondition3 = new Condition();
			setFetchActionCondition3.setType("class");
			List setFetchActionCondition3ArgList = new ArrayList();
			Arg setFetchActionCondition3Arg = new Arg();
			setFetchActionCondition3Arg.setName("class.name");
			setFetchActionCondition3Arg
					.setValue("com.qware.wf.condition.IsCanbeRetakeCondition");
			setFetchActionCondition3ArgList.add(setFetchActionCondition3Arg);
			setFetchActionCondition3.setArgs(setFetchActionCondition3ArgList);

			// 把Action的三个条件添加到ConditionList中
			setFetchConditionsList.add(setFetchActionCondition1);
			setFetchConditionsList.add(setFetchActionCondition2);
			setFetchConditionsList.add(setFetchActionCondition3);
			setFetchConditions.setConditions(setFetchConditionsList);

			// 设置Action结果
			Result setFetchResult = new Result();
			setFetchResult.setOldStatus("finish");
			setFetchResult.setStatus("retake");
			String strNextStepID = nextStepID;
			setFetchResult.setStep(strNextStepID);
			List setFetchResultList = new ArrayList();
			setFetchResultList.add(setFetchResult);
			setFetchAction.setPrefunction(this.getPreFunction());
			setFetchAction.setConditions(setFetchConditions);
			setFetchAction.setPostfunction(getRetakePostFunction());
			setFetchAction.setResults(setFetchResultList);

			this.getActions().add(setFetchAction);
		} catch (Exception e) {
			return;
		}
	}

	private Function getRetakePostFunction() {
		Function postFunction = new Function();
		String type = "class";
		Arg arg = new Arg();
		arg.setName("class.name");
		arg.setValue("com.qware.wf.function.RetakePostTaskFunction");
		List args = new ArrayList();
		args.add(arg);

		postFunction.setType(type);
		postFunction.setArgs(args);
		return postFunction;
	}

	// 设置退回节点
	public void setGoBack(TblWfParticularInfo wfin) {
		try {
			// 设置退回节点 Action
			Action setGoBackAction = new Action();
			setGoBackAction.setName("退回");
			setGoBackAction.setId(getActionId());

			// 设置退回节点 Action的Condtions
			Conditions setGoBackConditions = new Conditions();

			// 设置第二个Condition
			Condition setGoBackCondition2 = new Condition();
			setGoBackCondition2.setType("class");

			List setGoBackCondition2ArgList = new ArrayList();
			Arg setGoBackCondition2Arg1 = new Arg();
			Arg setGoBackCondition2Arg2 = new Arg();
			setGoBackCondition2Arg1.setName("class.name");
			setGoBackCondition2Arg1
					.setValue("com.qware.wf.condition.StatusCondition");
			setGoBackCondition2Arg2.setName("status");
			setGoBackCondition2Arg2.setValue("retake,untread,processing");
			setGoBackCondition2ArgList.add(setGoBackCondition2Arg1);
			setGoBackCondition2ArgList.add(setGoBackCondition2Arg2);

			setGoBackCondition2.setArgs(setGoBackCondition2ArgList);

			// 把Condition添加到Conditions中
			List setGoBackConditionsList = new ArrayList();
			// setGoBackConditionsList.add(setGoBackCondition1);
			setGoBackConditionsList.add(setGoBackCondition2);
			// setGoBackConditionsList.add(setGoBackCondition3);

			setGoBackConditions.setConditions(setGoBackConditionsList);

			// 设置结果
			Result setGoBackResult = new Result();
			List setGoBackResultList = new ArrayList();
			setGoBackResult.setOldStatus("finish");
			setGoBackResult.setStatus("untread");

			setGoBackResult.setStep(wfin == null ? "wfin is nothing" : wfin
					.getGobackStep());
			setGoBackResultList.add(setGoBackResult);

			// 设置Action
			setGoBackAction.setPrefunction(this.getPreFunction());
			setGoBackAction.setPostfunction(getPostFunction());
			setGoBackAction.setConditions(setGoBackConditions);
			setGoBackAction.setResults(setGoBackResultList);

			// 将Action添加到actions中

			this.getActions().add(setGoBackAction);
		} catch (Exception e) {
			return;
		}
	}

	private Function getPostFunction() {
		Function postFunction = new Function();
		String type = "class";
		Arg arg = new Arg();
		arg.setName("class.name");
		arg.setValue("com.qware.wf.function.PostTaskFunction");
		List args = new ArrayList();
		args.add(arg);
		Arg arg2 = new Arg();
		arg2.setName("untread");
		arg2.setValue("true");
		args.add(arg2);
		postFunction.setType(type);
		postFunction.setArgs(args);
		return postFunction;
	}

	// 设置取消节点
	public void setCancel(String nextStepID) {
		try {
			WorkFlowMeta wf = new WorkFlowMeta();
			// 设置取消节点Action
			Action setCancelAction = new Action();
			setCancelAction.setName("取消本流程");
			setCancelAction
					.setId(WorkflowOperation.KILLED);

			// 设置取消节点Action的Conditions
			Conditions setCancelConditions = new Conditions();
			setCancelConditions.setType("AND");

			// 设置第一个Condition
			Condition setCancelCondition1 = new Condition();
			setCancelCondition1.setType("class");

			Arg setCancelCondition1Arg1 = new Arg();
			Arg setCancelCondition1Arg2 = new Arg();
			setCancelCondition1Arg1.setName("class.name");
			setCancelCondition1Arg1
					.setValue("com.qware.wf.condition.AvailableStepsCondition");
			setCancelCondition1Arg2.setName("availableSteps");
			setCancelCondition1Arg2.setValue(getPermissionFirstRole());
			List setCancelCondition1ArgList = new ArrayList();
			setCancelCondition1ArgList.add(setCancelCondition1Arg1);
			setCancelCondition1ArgList.add(setCancelCondition1Arg2);
			setCancelCondition1.setArgs(setCancelCondition1ArgList);

			// 设置第二个Condition
			Condition setCancelCondition2 = new Condition();
			setCancelCondition2.setType("class");

			Arg setCancelCondition2Arg1 = new Arg();
			Arg setCancelCondition2Arg2 = new Arg();
			setCancelCondition2Arg1.setName("class.name");
			setCancelCondition2Arg1
					.setValue("com.qware.wf.condition.RoleCdCondition");
			setCancelCondition2Arg2.setName("role.code");
			setCancelCondition2Arg2.setValue("GR.PROFESSIONAL");
			List setCancelCondition2ArgList = new ArrayList();
			setCancelCondition2ArgList.add(setCancelCondition2Arg1);
			setCancelCondition2ArgList.add(setCancelCondition2Arg2);
			setCancelCondition2.setArgs(setCancelCondition2ArgList);

			// 把二个条件添加到Conditions中
			List setCancelConditionsList = new ArrayList();
			setCancelConditionsList.add(setCancelCondition1);
			setCancelConditionsList.add(setCancelCondition2);
			setCancelConditions.setConditions(setCancelConditionsList);

			// 设置结果
			Result setCancelResult = new Result();
			List setCancelResultList = new ArrayList();
			setCancelResult.setOldStatus("finish");
			setCancelResult.setStatus("killed");
			String stepId = nextStepID;
			setCancelResult.setStep("100");// stepId
			setCancelResult.setOwner("${inputOwner}");
			setCancelResultList.add(setCancelResult);
			// 设置Action
			setCancelAction.setConditions(setCancelConditions);
			setCancelAction.setResults(setCancelResultList);

			// 把Action添加到Actions中

			wf.setGlobalActions(setCancelAction);
		} catch (Exception e) {
			return;
		}
	}

	// 非固定流程
	public void setUnFixed() {
		try {
			// 非固定流程Action设定
			Action setUnFixedAction = new Action();
			setUnFixedAction.setName("非固定流程");
			setUnFixedAction.setId(id + "01");

			// Action中的ConditionsList
			List setUnFixedConditions = new ArrayList();

			// Action中的conditions并设置起类型
			Conditions csetUnFixedonditions = new Conditions();
			csetUnFixedonditions.setType("AND");

			// 非固定流程Action中的第一个Condition设置
			Condition setUnFixedCondition1 = new Condition();
			setUnFixedCondition1.setType("class");
			List setUnFixedCondition1ArgList = new ArrayList();
			Arg setUnFixedArg1 = new Arg();
			Arg setUnFixedArg2 = new Arg();
			setUnFixedArg1.setName("class.name");
			setUnFixedArg1
					.setValue("com.coheg.workflow.impl.condition.AllowedRolesCondition");
			setUnFixedArg2.setName("role.code");
			setUnFixedArg2.setValue(getPermissionFirstRole() + "");
			setUnFixedCondition1ArgList.add(setUnFixedArg1);
			setUnFixedCondition1ArgList.add(setUnFixedArg2);
			setUnFixedCondition1.setArgs(setUnFixedCondition1ArgList);

			// 非固定流程Action中的第二个Condition设置
			Condition setUnFixedCondition2 = new Condition();
			setUnFixedCondition2.setType("class");
			Arg setUnFixedArg3 = new Arg();
			Arg setUnFixedArg4 = new Arg();
			setUnFixedArg3.setName("class.name");
			setUnFixedArg3.setValue("com.qware.wf.condition.StatusCondition");
			setUnFixedArg4.setName("status");
			setUnFixedArg4.setValue("processing");
			List setUnFixedCondition2ArgList = new ArrayList();
			setUnFixedCondition2ArgList.add(setUnFixedArg3);
			setUnFixedCondition2ArgList.add(setUnFixedArg4);
			setUnFixedCondition2.setArgs(setUnFixedCondition2ArgList);

			// 非固定流程Action中的第三个Condition设置
			Condition setUnFixedCondition3 = new Condition();
			setUnFixedCondition3.setType("class");
			Arg setUnFixedArg5 = new Arg();
			setUnFixedArg5.setName("class.name");
			setUnFixedArg5.setValue("com.qware.wf.condition.IsMyTaskCondition");
			List setUnFixedCondition3ArgList = new ArrayList();
			setUnFixedCondition3ArgList.add(setUnFixedArg5);
			setUnFixedCondition3.setArgs(setUnFixedCondition3ArgList);

			// 把Condition添加到Action的ConditionsList中
			setUnFixedConditions.add(setUnFixedCondition1);
			setUnFixedConditions.add(setUnFixedCondition2);
			setUnFixedConditions.add(setUnFixedCondition3);

			csetUnFixedonditions.setConditions(setUnFixedConditions);

			// 设置Action结果
			List setUnFixedUnResultList = new ArrayList();
			Result setUnFixedUnResult = new Result();
			setUnFixedUnResult.setOldStatus("finish");
			setUnFixedUnResult.setStatus("processing");
			setUnFixedUnResult.setStep(id);
			setUnFixedUnResultList.add(setUnFixedUnResult);

			// 把条件和结果添加到Action中
			setUnFixedAction.setConditions(csetUnFixedonditions);
			setUnFixedAction.setResults(setUnFixedUnResultList);

			// 把非固定流程Action添加到actions中

			this.getActions().add(setUnFixedAction);
		} catch (Exception e) {
			return;
		}
	}

	private Function getSuspendPostTaskFunction() {
		Function postFunction = new Function();
		String type = "class";
		Arg arg = new Arg();
		arg.setName("class.name");
		arg.setValue("com.qware.wf.function.SuspendPostTaskFunction");
		List args = new ArrayList();
		args.add(arg);

		postFunction.setType(type);
		postFunction.setArgs(args);
		return postFunction;
	}

	private Function getActivePostTaskFunction() {
		Function postFunction = new Function();
		String type = "class";
		Arg arg = new Arg();
		arg.setName("class.name");
		arg.setValue("com.qware.wf.function.ActivePostTaskFunction");
		List args = new ArrayList();
		args.add(arg);

		postFunction.setType(type);
		postFunction.setArgs(args);
		return postFunction;
	}

	// 挂起设定
	/**
	 * 功能描述
	 * 
	 * @throws Exception
	 *             2007-11-19 下午03:12:04
	 * @version 1.0
	 * @author yijh
	 * @throws Exception
	 */

	public void setSuspendable(TblWfParticularInfo wfin) {
		try {
			// -------------挂起------------
			// 设置挂起Action
			Action setSuspendableAction = new Action();
			setSuspendableAction.setName("挂起");
			setSuspendableAction.setId(getActionId());

			// 设置挂起Action的Conditions
			Conditions setSuspendableConditions = new Conditions();
			setSuspendableConditions.setType("AND");
			List setSuspendableConditionsList = new ArrayList();

			// 设置第一个条件
			Condition setSuspendableCondition1 = new Condition();
			setSuspendableCondition1.setType("class");

			List setSuspendableArgList1 = new ArrayList();
			Arg setSuspendableArg1 = new Arg();
			Arg setSuspendableArg2 = new Arg();
			setSuspendableArg1.setName("class.name");
			setSuspendableArg1
					.setValue("com.qware.wf.condition.RoleCdCondition");
			setSuspendableArg2.setName("role.code");
			setSuspendableArg2.setValue(wfin == null ? "wfin is null" : wfin
					.getAllRoleCd());

			setSuspendableArgList1.add(setSuspendableArg1);
			setSuspendableArgList1.add(setSuspendableArg2);

			setSuspendableCondition1.setArgs(setSuspendableArgList1);

			// 设置第二个条件
			Condition setSuspendableCondition2 = new Condition();
			setSuspendableCondition2.setType("class");
			List setSuspendableArgList2 = new ArrayList();
			Arg setSuspendableArg3 = new Arg();
			Arg setSuspendableArg4 = new Arg();
			setSuspendableArg3.setName("class.name");
			setSuspendableArg3
					.setValue("com.qware.wf.condition.StatusCondition");
			setSuspendableArg4.setName("status");
			setSuspendableArg4.setValue("processing");
			setSuspendableArgList2.add(setSuspendableArg3);
			setSuspendableArgList2.add(setSuspendableArg4);
			setSuspendableCondition2.setArgs(setSuspendableArgList2);

			// 把两个条件添加到ConditionsList中
			setSuspendableConditionsList.add(setSuspendableCondition1);
			setSuspendableConditionsList.add(setSuspendableCondition2);

			// 设置挂起ActionConditions
			setSuspendableConditions
					.setConditions(setSuspendableConditionsList);
			setSuspendableAction.setConditions(setSuspendableConditions);

			// 设置ActionResults
			Result setSuspendableResult = new Result();
			setSuspendableResult.setOldStatus("finish");
			setSuspendableResult.setStatus("suspend");
			setSuspendableResult.setStep(id);
			List setSuspendableResultList = new ArrayList();
			setSuspendableResultList.add(setSuspendableResult);
			setSuspendableAction.setPrefunction(this.getPreFunction());
			setSuspendableAction.setPostfunction(getSuspendPostTaskFunction());
			setSuspendableAction.setResults(setSuspendableResultList);
			if (this.getActions() != null) {
				this.getActions().add(setSuspendableAction);
				// ------------激活----------------
				// 激活Action设置
				Action activeAction = new Action();
				activeAction.setName("激活");
				activeAction.setId(getActionId());

				Conditions activeConditions = new Conditions();
				activeConditions.setType("AND");

				// 设置第一个条件
				Condition activeCondition1 = new Condition();
				activeCondition1.setType("class");
				List activeConditionsArgList1 = new ArrayList();
				Arg activeConditionsArg1 = new Arg();
				Arg activeConditionsArg2 = new Arg();
				activeConditionsArg1.setName("class.name");
				activeConditionsArg1
						.setValue("com.qware.wf.condition.RoleCdCondition");
				activeConditionsArg2.setName("role.code");
				activeConditionsArg2.setValue(wfin == null ? "wfin is null"
						: wfin.getAllRoleCd());
				activeConditionsArgList1.add(activeConditionsArg1);
				activeConditionsArgList1.add(activeConditionsArg2);
				activeCondition1.setArgs(activeConditionsArgList1);

				// 设置第二个条件
				Condition activeCondition2 = new Condition();
				activeCondition2.setType("class");
				List activeConditionsArgList2 = new ArrayList();
				Arg activeConditionsArg3 = new Arg();
				Arg activeConditionsArg4 = new Arg();
				activeConditionsArg3.setName("class.name");
				activeConditionsArg3
						.setValue("com.qware.wf.condition.StatusCondition");
				activeConditionsArg4.setName("status");
				activeConditionsArg4.setValue("suspend");
				activeConditionsArgList2.add(activeConditionsArg3);
				activeConditionsArgList2.add(activeConditionsArg4);
				activeCondition2.setArgs(activeConditionsArgList2);

				// 把条件添加到列表中
				List activeConditionList = new ArrayList();
				activeConditionList.add(activeCondition1);
				activeConditionList.add(activeCondition2);
				activeConditions.setConditions(activeConditionList);

				// 设定Actiond的Conditions
				activeAction.setConditions(activeConditions);

				// 设置Results
				List resultList = new ArrayList();
				Result activeResult = new Result();
				activeResult.setOldStatus("finish");
				activeResult.setStatus("processing");
				activeResult.setStep(id);
				resultList.add(activeResult);
				activeAction.setPrefunction(this.getPreFunction());
				activeAction.setPostfunction(getActivePostTaskFunction());
				// 设置Action的Results
				activeAction.setResults(resultList);

				this.getActions().add(activeAction);
			}
		} catch (Exception e) {
			return;
		}

	}

	// 生成前处理
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

	// 会签设定,会签只在Action中设定,画面选定会签
	public void setTogetherSign() {

		Action setTogetherSignAction = null;

		if (this.getActions() != null && this.getActions().size() > 0) {
			setTogetherSignAction = (Action) this.getActions().get(0);
			Function post = setTogetherSignAction.getPostfunction();
			List results = null;
			if (setTogetherSignAction != null) {
				results = setTogetherSignAction.getResults();
			}
			Result result = null;
			if (results != null && results.size() > 0) {
				result = (Result) (results.get(0));
			}
			String nextStepID = null;
			if (result != null) {
				nextStepID = result.getStep();
			}
			if (setTogetherSignAction != null) {
				setTogetherSignAction.setResults(null);
				String actionId = setTogetherSignAction.getId();
				String actionName = setTogetherSignAction.getName();
				setTogetherSignAction = new Action();
				setTogetherSignAction.setId(actionId);
				setTogetherSignAction.setName(actionName);
			}

			this.getActions().remove(0);

			// 设置会签

			// Action中的Conditions
			Conditions setTogetherSignConditions = new Conditions();
			setTogetherSignConditions.setType("AND");

			// 设置第二个条件
			Condition setTogetherSignCondition2 = new Condition();
			setTogetherSignCondition2.setType("class");
			Arg setTogetherSignArg3 = new Arg();
			Arg setTogetherSignArg4 = new Arg();
			setTogetherSignArg3.setName("class.name");
			setTogetherSignArg3
					.setValue("com.qware.wf.condition.StatusCondition");
			setTogetherSignArg4.setName("status");
			setTogetherSignArg4.setValue("processing");
			List setTogetherSignCondition2ArgList = new ArrayList();
			setTogetherSignCondition2ArgList.add(setTogetherSignArg3);
			setTogetherSignCondition2ArgList.add(setTogetherSignArg4);
			setTogetherSignCondition2.setArgs(setTogetherSignCondition2ArgList);

			// 设置第三个条件
			Condition setTogetherSignCondition3 = new Condition();
			setTogetherSignCondition3.setType("class");
			List setTogetherSignCondition3ArgList = new ArrayList();
			Arg setTogetherSignCondition3Arg = new Arg();
			setTogetherSignCondition3Arg.setName("class.name");
			setTogetherSignCondition3Arg
					.setValue("com.qware.wf.condition.IsMyTaskCondition");
			setTogetherSignCondition3ArgList.add(setTogetherSignCondition3Arg);
			setTogetherSignCondition3.setArgs(setTogetherSignCondition3ArgList);

			List setTogetherSignConditionsList = new ArrayList();
			setTogetherSignConditionsList.add(setTogetherSignCondition2);
			setTogetherSignConditionsList.add(setTogetherSignCondition3);

			// 设置Conditions
			setTogetherSignConditions
					.setConditions(setTogetherSignConditionsList);
			if (setTogetherSignAction != null) {
				setTogetherSignAction.setConditions(setTogetherSignConditions);
			}
			// 设置结果Results

			// 设置有条件结果
			Result setTogetherSignResult = new Result();

			setTogetherSignResult.setOldStatus("finish");
			setTogetherSignResult.setStatus("processing");
			setTogetherSignResult.setStep(nextStepID);

			Conditions setTogetherSignResultConditions = new Conditions();

			Condition setTogetherSignResultCondition = new Condition();
			setTogetherSignResultCondition.setType("class");

			Arg setTogetherSignResultArg = new Arg();
			setTogetherSignResultArg.setName("class.name");

			setTogetherSignResultArg
					.setValue("com.qware.wf.condition.PassRoundApproverCondition");

			List setTogetherSignResultArgList = new ArrayList();
			setTogetherSignResultArgList.add(setTogetherSignResultArg);
			setTogetherSignResultCondition
					.setArgs(setTogetherSignResultArgList);
			List setTogetherSignResultConditionsList = new ArrayList();
			setTogetherSignResultConditionsList
					.add(setTogetherSignResultCondition);

			setTogetherSignResultConditions
					.setConditions(setTogetherSignResultConditionsList);

			setTogetherSignResult
					.setConditions(setTogetherSignResultConditions);

			Result setTogetherSignUnResult = new Result();
			setTogetherSignUnResult.setOldStatus("finish");
			setTogetherSignUnResult.setStatus("processing");
			setTogetherSignUnResult.setStep(id);

			List setTogetherSignResultList = new ArrayList();
			setTogetherSignResultList.add(setTogetherSignResult);
			setTogetherSignResultList.add(setTogetherSignUnResult);
			if (setTogetherSignAction != null) {
				setTogetherSignAction.setResults(setTogetherSignResultList);
			}
			
			
			// 设置后处理
			
			Function setTogetherSignFunction = new Function();
			setTogetherSignFunction.setType("class");

			Arg setTogetherSignFunctionArg = new Arg();
			setTogetherSignFunctionArg.setName("class.name");
			setTogetherSignFunctionArg
					.setValue("com.qware.wf.function.PassRoundPostTaskFunction");
			List setTogetherSignFunctionArgList = new ArrayList();
			setTogetherSignFunctionArgList.add(setTogetherSignFunctionArg);
			
//			 加发邮件

			if (post != null) {
				Arg mail = new Arg();
				List list = new ArrayList();
				list = post.getArgs();
				for (int i=0; i<list.size(); i++) {
					Arg arg = (Arg)list.get(i);
					if ("send.mail".equals(arg.getName())) {
						mail = arg;
						setTogetherSignFunctionArgList.add(mail);
					}
				}
			}
			
			setTogetherSignFunction.setArgs(setTogetherSignFunctionArgList);

			if (setTogetherSignAction != null) {
				setTogetherSignAction.setPostfunction(setTogetherSignFunction);
				setTogetherSignAction.setPrefunction(getPreFunction());
				this.getActions().add(setTogetherSignAction);
			}
		}
	}

	public String getStepRole() {
		if (this.getPermission() != null) {
			Conditions conditions = this.getPermission().getConditions();
			if (conditions != null) {
				List conditionList = conditions.getConditions();
				Condition condition = null;
				if (conditionList != null && conditionList.size() > 0) {
					condition = (Condition) conditionList.get(0);
				}
				List argList = null;
				if (condition != null) {
					argList = condition.getArgs();
				}
				if (argList != null) {
					for (int i = 0; i < argList.size(); i++) {
						Arg arg = (Arg) argList.get(i);
						if (arg == null) {
							continue;
						}
						if ("role.code".equals(arg.getName())) {
							String value = arg.getValue();
							return value;
						}
					}
				}
			}
		}
		return "nothing";
	}

	// 给该step的其他acntion加role, 给step的permissions加role
	public void addRoleForRetake(String role, String role2) {
		if (this.getPermission() != null) {
			Conditions conditions = this.getPermission().getConditions();
			if (conditions != null) {
				List conditionList = conditions.getConditions();
				Condition condition = null;

				if (conditionList != null && conditionList.size() > 0) {
					condition = (Condition) conditionList.get(0);
				}
				List argList = null;
				if (condition != null) {
					argList = condition.getArgs();
				}
				if (argList != null) {
					for (int i = 0; i < argList.size(); i++) {
						Arg arg = (Arg) argList.get(i);
						if (arg == null) {
							continue;
						} else if ("role.code".equals(arg.getName())) {
							String value = arg.getValue();
							arg.setValue(value + role);
						}
					}
					if (this.getActions() != null) {
						for (int i = 0; i < this.getActions().size(); i++) {
							Action action = (Action) this.getActions().get(i);
							if (action == null) {
								continue;
							}
							Conditions actionConditions = action
									.getConditions();
							if (actionConditions == null) {
								continue;
							}
							actionConditions.setType("AND");
							List actionCondList = actionConditions
									.getConditions();
							if (actionCondList == null) {
								continue;
							}
							Condition cond = new Condition();
							cond.setType("class");
							List argsList = new ArrayList();
							Arg arg1 = new Arg();
							Arg arg2 = new Arg();
							arg1.setName("class.name");
							arg1
									.setValue("com.qware.wf.condition.RoleCdCondition");
							arg2.setName("role.code");
							arg2.setValue(role2);
							argsList.add(arg1);
							argsList.add(arg2);
							cond.setArgs(argsList);
							actionCondList.add(cond);
						}
					}
				}
			}
		}
	}

	public void updateGoBacked() {
		try{
		for (int x = 0; x < this.getActions().size(); x++) {
			Action action = (Action) this.getActions().get(x);

			Conditions conditions1 = action.getConditions();

			List condList = conditions1.getConditions();

			for (int y = 0; y < condList.size(); y++) {
				Condition oldCond = (Condition) condList.get(y);

				List argsList = oldCond.getArgs();

				for (int z = 0; z < argsList.size(); z++) {
					Arg oldArg = (Arg) argsList.get(z);

					if ("processing".equals(oldArg.getValue())) {
						oldArg.setValue("retake,untread,processing");
						if (condList.size() > 1) {
							conditions1.setType("AND");
						}
					}
				}
			}
		}
		}catch(Exception e){
			return;
		}
	}

	public List getActions() {
		return actions;
	}

	public void setActions(List actions) {
		this.actions = actions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

}
