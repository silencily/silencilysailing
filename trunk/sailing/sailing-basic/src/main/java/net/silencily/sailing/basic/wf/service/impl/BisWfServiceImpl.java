package net.silencily.sailing.basic.wf.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.sm.user.service.UserManageService;
import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.WorkflowService;
import net.silencily.sailing.basic.wf.constant.WorkflowOperation;
import net.silencily.sailing.basic.wf.db.WfDBConnection;
import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.basic.wf.domain.TblWfParticularInfo;
import net.silencily.sailing.basic.wf.dto.BisWfEntry;
import net.silencily.sailing.basic.wf.dto.ClonedObject;
import net.silencily.sailing.basic.wf.dto.WfEntry;
import net.silencily.sailing.basic.wf.dto.WfSearch;
import net.silencily.sailing.basic.wf.entry.WorkflowRelation;
import net.silencily.sailing.basic.wf.entry.WorkflowStep;
import net.silencily.sailing.basic.wf.service.IWorkflowService;
import net.silencily.sailing.basic.wf.service.WFService;
import net.silencily.sailing.basic.wf.util.BeanCompareUtils;
import net.silencily.sailing.basic.wf.util.Description;
import net.silencily.sailing.basic.wf.util.ObjectOperateTools;
import net.silencily.sailing.basic.wf.util.PassRoundUtils;
import net.silencily.sailing.basic.wf.util.WfCommonTools;
import net.silencily.sailing.basic.wf.util.WfUtils;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.persistent.Entity;
import net.silencily.sailing.hibernate3.CodeWrapperPlus;
import net.silencily.sailing.hibernate3.EntityPlus;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.opensymphony.workflow.WorkflowException;

public class BisWfServiceImpl implements IWorkflowService {
	public void sendTaskWhenScratch(WorkflowInfo info) throws Exception {
		if (info == null || !WorkflowInfo.STATUS_SCRATCH.equals(info.getWorkflowStatus()) || "".equals(info.getId()) || null == info.getId()) {
			throw new Exception("The bean cannot sent task .");
		}
		new WfUtils().addTaskWithoutWorkflow(info);		
	}

	public void termainateWorkflow(WorkflowInfo workflowInfo, String value) throws Exception {
		this.wfservice.terminate(workflowInfo, value, workflowInfo.getCurrentStep().getOpinion());		
	}

	public void unlockSuperWorkflow(WorkflowRelation wr) throws Exception {
		WfUtils util = new WfUtils();
		try {
			util.setTransactionConn(WfDBConnection.getConnection());
			util.getTransactionConn().setAutoCommit(false);

			WorkflowInfo superWfInfo = util.getSuperWfInfo(wr);
			doTransition(superWfInfo, Collections.EMPTY_MAP);
			util.endSubWorkflow(wr);

			util.getTransactionConn().commit();
		} catch (Exception e) {
			try {
				util.getTransactionConn().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new WorkflowException("unlock super workflow exception");
			}
			e.printStackTrace();
			throw new WorkflowException("unlock super workflow exception");
		} finally {
			try {
				util.getTransactionConn().setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new WorkflowException();
			}
			if (util.getTransactionConn() != null) {
				try {
					util.getTransactionConn().close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new WorkflowException();
				}
			}
		}

	}

	public void initializeSubWorkflow(WorkflowRelation wr,
			WorkflowInfo subWfInfo) throws Exception {
		WfUtils util = new WfUtils();
		try {
			util.setTransactionConn(WfDBConnection.getConnection());
			util.getTransactionConn().setAutoCommit(false);
			
			if (!subWfInfo.isWorkflowCanBeInitialized()) {
				throw new UnsupportedOperationException("对不起, 此纪录不具备初始化工作流条件");
			}

			subWfInfo.setActionId(WorkflowOperation.INIT);

			wfservice.initializeWorkflow(subWfInfo);
			
			((CommonService) ServiceProvider
					.getService(CommonService.SERVICE_NAME))
					.saveOrUpdate(subWfInfo);

			util.doRelation(wr);
			util.getTransactionConn().commit();
		} catch (Exception e) {
			try {
				util.getTransactionConn().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new WorkflowException();
			}
			e.printStackTrace();
			throw new WorkflowException("start sub workflow exception");
		} finally {
			try {
				util.getTransactionConn().setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new WorkflowException();
			}
			if (util.getTransactionConn() != null) {
				try {
					util.getTransactionConn().close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new WorkflowException();
				}
			}
		}

	}

	public Set getPointedUserOfStep(String taskId) {
		
		Set resultSet = new HashSet();
		try {
			resultSet = WfUtils.getPointedUserOfStep(taskId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public List getUsersOfStep(String workflowName, String stepId) {
		return getAboutEmp(workflowName, stepId, null);
	}

	private List getAboutEmp(String workflowName, String stepId, String flag) {
		if (StringUtils.isBlank(workflowName) || StringUtils.isBlank(stepId)) {
			return new ArrayList();
		}
		String roles = "";
		try {
			roles = this.getRoles(workflowName, stepId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] roleIds = new String[] {};
		List roleIdList = new ArrayList();
		if (StringUtils.isNotBlank(roles)) {
			roleIds = roles.split(",");
			for (int i = 0; i < roleIds.length; i++) {
				roleIdList.add(roleIds[i]);
			}
		}
		if ("role".equals(flag)) {
			return roleIdList;
		} else {
			// TblCmnUser.class
			List resultList = ((UserManageService) ServiceProvider
					.getService(UserManageService.SERVICE_NAME))
					.getUsersByRoleCds(roleIdList);
			return resultList;
		}
	}

	public BisWfServiceImpl() {
	}

	public void loadWorkflowInfo(WorkflowInfo workflowInfo, String taskId)
			throws Exception {
		if (workflowInfo != null) {
			if (StringUtils.isBlank(taskId) && StringUtils.isBlank(workflowInfo.getInitSplitActionId())) {
				this.getWfservice().loadWorkflowInfoWithOutAction(workflowInfo);
			} else {
				workflowInfo.setTaskId(taskId);
				this.getWfservice().loadWorkflowInfo(workflowInfo);
			}
		}		
	}

	public void doTransition(WorkflowInfo workflowInfo, Map map)
			throws Exception {
	
		//得到初始事物状态
		boolean tag = WfDBConnection.getConnection().getAutoCommit();	
//		
		//设置步骤比较MAP
//		if(map == null || map.isEmpty()){
//			map = new HashMap(0);
//		}
//		setCompareStepMap(map,workflowInfo);
//		
		String preStep = workflowInfo.getCurrentStep().getId();
//		StringBuffer changeBuf = new StringBuffer(); 
//		String stepChangeContent = "";
//		ClonedObject oldMainBean = (ClonedObject)map.get("oldMainBean");
//		WorkflowInfo newMainBean = (WorkflowInfo)map.get("newMainBean");
//		boolean flg = false;
//		if (oldMainBean != null && newMainBean != null) {			
//			changeBuf.append(MainBeanChangeRecord.getChangeRecord(oldMainBean, newMainBean));
//			flg = true;
//		}
//		Map subListMap = (Map)map.get("subListMap");
//		if (subListMap != null) {
//			Set set = subListMap.keySet();
//			Iterator it = set.iterator();
//			while (it.hasNext()) {
//				List oldList = (List)it.next();
//				List newList = (List)subListMap.get(oldList);
//				String subRecord = SubListChangeRecord.getChangeRecord(oldList, newList);
//				changeBuf.append(flg ? "<br>" + subRecord : subRecord );
//				flg = true;
//			}		
//		}		
//		
//		if (flg) {
//			stepChangeContent = changeBuf.toString();
//			if (stepChangeContent.length() > 4000) {
//				stepChangeContent = stepChangeContent.substring(0, 3999);
//			}
//			Map data = new HashMap();
//			data.put(WorkflowConstants.KEY_STEP_CHANGE_CONTENT, stepChangeContent);
//			map.put(WorkflowConstants.KEY_STEP_CHANGE_CONTENT, stepChangeContent);
//			//历史步骤信息修正
//			workflowInfo.getCurrentStep().setData(map); 
//		}
		
		//测试代码
		//setCompareStepMap(map,workflowInfo);
		
		//比较两个实体BEAN
		String stepChangeContent = "";
		String key = workflowInfo.getId();
		Description description = new Description();
		if(SecurityContextInfo.sessionManagerFactory("workFlowHistoryInfo") != null){
			Entity oldTemp = (Entity)((Map) SecurityContextInfo.sessionManagerFactory("workFlowHistoryInfo")).get(key);
			//将传过来的MAP进行处理，转化成STRING数组
			String[] subObject = (String [])((Map) SecurityContextInfo.sessionManagerFactory("workFlowHistoryInfo")).get("subStr");
			String[] uncompare = (String [])((Map) SecurityContextInfo.sessionManagerFactory("workFlowHistoryInfo")).get("uncompare");
			String[] displayField = (String [])((Map) SecurityContextInfo.sessionManagerFactory("workFlowHistoryInfo")).get("displayField");
			description.append(BeanCompareUtils.compare(oldTemp,workflowInfo,subObject,uncompare,displayField,true));
		}
		stepChangeContent = description.toString();
		
		String opinion = null;
		if (StringUtils.isNotBlank(workflowInfo.getCurrentStep().getOpinion())) {
			opinion = workflowInfo.getCurrentStep().getOpinion();
		}
		
		this.getWfservice().loadWorkflowInfo(workflowInfo);
		workflowInfo.getCurrentStep().setOpinion(opinion);
		
		
		if(StringUtils.isNotBlank(stepChangeContent)){
			//保存历史步骤信息
			WFService serviceTemp = (WFService)ServiceProvider.getService(WFService.SERVICE_NAME);
			serviceTemp.saveHistoryEntryData(String.valueOf(workflowInfo.getCurrentStep().getEntryStepId()), stepChangeContent, workflowInfo.getId());	
		}
		//清除SESSION信息
		SecurityContextInfo.getSession().removeAttribute("workFlowHistoryInfo");
		SecurityContextInfo.getSession().removeAttribute("loadByTag");
	
		
		if (WorkflowOperation.KILLED.equals(workflowInfo.getActionId())) {
			WfUtils util = new WfUtils();
           
			if (util.hasSubFlow(workflowInfo)) {
				
				WorkflowRelation wr = new WorkflowRelation();
				wr.setSuperOid(workflowInfo.getId());
				wr.setSuperClass(WfUtils.getUnProxyClassName(workflowInfo.getClass()));
				WorkflowInfo subFlow = util.getSubWfInfo(wr);
				wfservice.killWorkflow(subFlow);
			} else if (util.hasSupFlow(workflowInfo)) {
				
				WorkflowRelation wr = new WorkflowRelation();
				wr.setSubOid(workflowInfo.getId());
				wr.setSubClass(WfUtils.getUnProxyClassName(workflowInfo.getClass()));				
				unlockSuperWorkflow(wr);
			} 			
			wfservice.killWorkflow(workflowInfo, workflowInfo.getCurrentStep().getOpinion());				
			
		} else if (WorkflowOperation.SUSPEND.equals(workflowInfo.getActionId())) {
			wfservice.suspend(workflowInfo);
		} else if (WorkflowOperation.ACTIVATE
				.equals(workflowInfo.getActionId())) {
			wfservice.activate(workflowInfo);
		} else {
			wfservice.doTransition(workflowInfo, map);
			if (WorkflowInfo.STATUS_FINISH.equals(workflowInfo.getWorkflowStatus())) {
				WfUtils util = new WfUtils();
				util.setFinishData(workflowInfo);
			}
			
		}
		this.getWfservice().loadWorkflowInfo(workflowInfo);
		
		if(	WorkflowInfo.STATUS_UNTREAD.equals(workflowInfo.getWorkflowStatus())
			||WorkflowInfo.STATUS_RETAKE.equals(workflowInfo.getWorkflowStatus()))
		{
			//如果是退回或取回操作要恢复曾经为null的值 yushn 2008-03-10
			WFService service = (WFService)ServiceProvider.getService(WFService.SERVICE_NAME);
			service.resetAppDataStatus(workflowInfo, workflowInfo.getCurrentStep().getId());
		}
		else if(WorkflowInfo.STATUS_FINISH.equals(workflowInfo.getWorkflowStatus())
				||WorkflowInfo.STATUS_KILLED.equals(workflowInfo.getWorkflowStatus()))
		{
			//流程结束清理临时数据
			WFService service = (WFService)ServiceProvider.getService(WFService.SERVICE_NAME);
			service.cleanAppDataStatus(workflowInfo.getId());
		}
		else 
		{
			//记录跳转前应用对象的状态 yushn 2008-03-10
			WFService service = (WFService)ServiceProvider.getService(WFService.SERVICE_NAME);
			service.recAppDataStatus(workflowInfo, preStep);
		}
	
		WfDBConnection.getConnection().setAutoCommit(tag);
//		//工作流事物测试
//		System.out.print("工作流事物测试");
//		throw new RuntimeException("工作流事物测试，嘿嘿，发生异常了哈");//谁这么哈~
//		//工作流事物测试
		//得到保存的SESSION中
		
	}

	public void initializeWorkflow(WorkflowInfo workflowInfo) throws Exception {
		if (!workflowInfo.isWorkflowCanBeInitialized()) {
			throw new UnsupportedOperationException("对不起, 此纪录不具备初始化工作流条件");
		}
		WfUtils.removeTaskBeforeWorkflowInit(workflowInfo);
		workflowInfo.setActionId(WorkflowOperation.INIT);

		wfservice.initializeWorkflow(workflowInfo);
		
		WfCommonTools.sendFirstTask(workflowInfo);
		Assert.isTrue(StringUtils.isNotBlank(workflowInfo.getInitSplitActionId()), "init split is null exception");
		workflowInfo.setActionId(workflowInfo.getInitSplitActionId());
		workflowInfo.setInitSplitActionId(null);
		doTransition(workflowInfo, Collections.EMPTY_MAP);		
	}

	public void initializeWorkflow(WorkflowInfo workflowInfo,Map map) throws Exception {
		if (!workflowInfo.isWorkflowCanBeInitialized()) {
			throw new UnsupportedOperationException("对不起, 此纪录不具备初始化工作流条件");
		}
		WfUtils.removeTaskBeforeWorkflowInit(workflowInfo);
		workflowInfo.setActionId(WorkflowOperation.INIT);

		wfservice.initializeWorkflow(workflowInfo);
		
		WfCommonTools.sendFirstTask(workflowInfo);
		Assert.isTrue(StringUtils.isNotBlank(workflowInfo.getInitSplitActionId()), "init split is null exception");
		workflowInfo.setActionId(workflowInfo.getInitSplitActionId());
		workflowInfo.setInitSplitActionId(null);
		doTransition(workflowInfo, map);		
	}
	

	private class WorkflowServiceProxy implements InvocationHandler {

		private Object wfService;
		
		private String[] loadMethods = new String[]{"load"};
		
		protected WorkflowService getWorkflowService(WorkflowService service) {
			wfService = service;
			return (WorkflowService)Proxy.newProxyInstance(service.getClass().getClassLoader(), new Class[] {WorkflowService.class}, this);
		}
		
		private WorkflowStep getCurrentStep(WorkflowInfo info) throws Throwable {
			Field field = BisWfEntry.class.getDeclaredField("currentStep");
			field.setAccessible(true);
			WorkflowStep step = null;
			step = (WorkflowStep)field.get(info);
			if (step == null)
				step = new WorkflowStep();
			return step;			
		}		 
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object ret = null;			
			if (loadMatch(method.getName()) && 
					args != null && args.length > 0 && WorkflowInfo.class.isAssignableFrom(args[0].getClass())) {
				WorkflowInfo info = (WorkflowInfo)args[0];
				WorkflowStep step = this.getCurrentStep(info);
				String opinion = step.getOpinion();
				ret = method.invoke(wfService, args);
				this.getCurrentStep(info).setOpinion(opinion);				
			} else {
				ret = method.invoke(wfService, args);
			}
			return ret;
		}
		
		private boolean loadMatch(String name) {
			boolean ret = false;
			for (int i=0; i<loadMethods.length; i++) {
				if (name.startsWith(loadMethods[i])) {
					ret = true;
					break;
				}
			}
			return ret;
		}		
	}
	public WorkflowService getWfservice() {
		//WorkflowServiceProxy proxy = new WorkflowServiceProxy();
		//return proxy.getWorkflowService(wfservice);
		return this.wfservice;
	}

	public void setWfservice(WorkflowService wfservice) {
		this.wfservice = wfservice;
	}

	private WorkflowService wfservice;

	public static final String SERVICE_NAME = "bisworkflow.BisWfService";

	public WorkflowStep getNextStep(WorkflowInfo workflowInfo) {
		WorkflowStep step = null;
		WfUtils util = new WfUtils();
		//获得actionId的初始值yangxl 2008-6-20
		String actions = workflowInfo.getActionId();
		try {
			if (WorkflowInfo.STATUS_SCRATCH.equals(workflowInfo
					.getWorkflowStatus())) {
				this.getWfservice().loadWorkflowInfo(workflowInfo);
				workflowInfo.setActionId(workflowInfo.getInitSplitActionId());
				List list = util.getWfInitSplit(workflowInfo.getWorkflowName());
				for (int i=0; i<list.size(); i++) {
					WorkflowStep wfstep = (WorkflowStep)list.get(i);
					if (workflowInfo.getActionId().equals(wfstep.getActionId())) {
						step = wfstep;
					}
				}
				if(step == null){
					//重新给actionId负值yangxl2008-6-20
					workflowInfo.setActionId(actions);
					step=  stepYn(workflowInfo);
				}
			}else {
				/*String opinion = workflowInfo.getCurrentStep().getOpinion();
				this.getWfservice().loadWorkflowInfo(workflowInfo);
				step = util.getNextStep(workflowInfo);
				workflowInfo.getCurrentStep().setOpinion(opinion);*/
				step = stepYn(workflowInfo);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return step;

	}
	//如果step为null或是不是草稿状态的时候会调用这个方法yangxl2008-6-20
	private  WorkflowStep stepYn(WorkflowInfo workflowInfo){
		
		WorkflowStep step = null;
		WfUtils util = new WfUtils();
		try {
			String opinion = workflowInfo.getCurrentStep().getOpinion();
			this.getWfservice().loadWorkflowInfo(workflowInfo);
			step = util.getNextStep(workflowInfo);
			workflowInfo.getCurrentStep().setOpinion(opinion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return step;
	}

	public List getUsersOfStepByActionId(WorkflowInfo wfInfo) throws Exception {
		if (!WorkflowInfo.STATUS_SCRATCH.equals(wfInfo.getWorkflowStatus())
				&& StringUtils.isNotBlank(wfInfo.getId())
				&& StringUtils.isNotBlank(wfInfo.getActionId())) {
			WorkflowStep step = this.getNextStep(wfInfo);
			return this.getUsersOfStep(wfInfo.getWorkflowName(), step.getId());
		}
		return Collections.EMPTY_LIST;
	}

	public List getRolesOfStepByActionId(WorkflowInfo wfInfo) throws Exception {

		return null;
	}

	public String getPointType(String workflowName, String stepId) {
		if (StringUtils.isBlank(workflowName) || StringUtils.isBlank(stepId)) {
			return "";
		}
		TblWfOperationInfo twoi = new TblWfOperationInfo();
		TblWfOperationInfo twoi2 = new TblWfOperationInfo();
	     //modifiey by gejianbao;
		CodeWrapperPlus cwp=new CodeWrapperPlus();
		cwp.setCode("0");
		twoi.setName(workflowName);
		twoi.setStatus(cwp);
		List wfList = ((WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME)).findsearch(twoi);
		if (wfList != null && wfList.size() > 0) {
			twoi2 = (TblWfOperationInfo) wfList.get(0);
		}
		
		Set stepSet = twoi2.getTblWfParticularInfos();
		Iterator it = stepSet.iterator();		
		while (it.hasNext()) {
			TblWfParticularInfo step = (TblWfParticularInfo) it.next();
			if (stepId.equals(String.valueOf(step.getStepId()))) {
				return step.getHelpman();
			}
		}
		return "";
	}

	public String getSpecialObject(String workflowName, String stepId) {
		if (StringUtils.isBlank(workflowName) || StringUtils.isBlank(stepId)) {
			return "";
		}
		TblWfOperationInfo twoi = new TblWfOperationInfo();
		TblWfOperationInfo twoi2 = new TblWfOperationInfo();
	     //modifiey by gejianbao;
		CodeWrapperPlus cwp=new CodeWrapperPlus();
		cwp.setCode("0");
		twoi.setName(workflowName);
		twoi.setStatus(cwp);
		List wfList = ((WFService) ServiceProvider
				.getService(WFService.SERVICE_NAME)).findsearch(twoi);
		if (wfList != null && wfList.size() > 0) {
			twoi2 = (TblWfOperationInfo) wfList.get(0);
		}
		Set stepSet = twoi2.getTblWfParticularInfos();
		Iterator it = stepSet.iterator();

		while (it.hasNext()) {
			TblWfParticularInfo step = (TblWfParticularInfo) it.next();
			if (stepId.equals(String.valueOf(step.getStepId()))) {
				return step.getPointStep();
			}
		}
		return "";
	}

	public String getRoles(String workflowName, String stepId) {
		String roles = "";
		try {
			roles = WfUtils.getRoles(workflowName, stepId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roles;
	}
	
	public String getNextStepSpecObj(String workflowName, String currId) {
		WfUtils util = new WfUtils();
		return this.getSpecialObject(workflowName, util.getPassRoundNextStepId(
				workflowName, currId));
	}

	public List findTask(CurrentUser user) throws Exception {
		return null;
	}
	
	public List findConsignedTask(CurrentUser user, WfSearch wfsearch, int pageNo, int pageSize) throws Exception {
		return null;
	}

	public List findConsignTask(CurrentUser user, WfSearch wfsearch, int pageNo, int pageSize) throws Exception {
		return null;
	}
	
	private List convertNullStep(List wfEntryList) {
		if (wfEntryList != null && wfEntryList.size() > 0) {
			for (int i=0; i<wfEntryList.size(); i++) {
				WfEntry wfEntry = (WfEntry)wfEntryList.get(i);
				if (wfEntry.getCurrentStepName() == null || "".equals(wfEntry.getCurrentStepName())) {
					wfEntry.setCurrentStepName(this.wfservice.getStepName(wfEntry.getWfName(), 1));
				}
				
				wfEntry.setUrl(Tools.getWFSecurityURL(wfEntry.getWfName(), wfEntry.getCurrentStepName(), wfEntry.getUrl()));
			}
		}
		return wfEntryList;
	}
	public List findFlowEntry(WfSearch wfsearch, int pageNo, int pageSize) throws Exception {
		return new WfUtils().findFlowEntry(wfsearch, pageNo, pageSize);
	}

	public List findOverTask(CurrentUser user, WfSearch wfsearch, int pageNo, int pageSize) throws Exception {
		return new WfUtils().findOverTask(user, wfsearch, pageNo, pageSize);
	}

	public List findWaitTask(CurrentUser user, WfSearch wfsearch, int pageNo, int pageSize) throws Exception {
		return convertNullStep(new WfUtils().findWaitTask(user, wfsearch, pageNo, pageSize));
	}

	public int getFlowEntryCount(WfSearch wfsearch) throws Exception {
		return new WfUtils().getEntryCount(wfsearch);
	}

	public int getOverTaskCount(CurrentUser user, WfSearch wfsearch) throws Exception {
		return new WfUtils().getOverTaskCount(user, wfsearch);
	}

	public int getWaitTaskCount(CurrentUser user, WfSearch wfsearch) throws Exception {
		return new WfUtils().getWaitTaskCount(user, wfsearch);
	}

	public String getCurrentStepId(WorkflowInfo workflowInfo) {
		return wfservice.getCurrentStepId(workflowInfo);
	}

	public void terminateWorkflow(WorkflowInfo workflowInfo) throws Exception {
		wfservice.terminate(workflowInfo);
	}

	public void clearPassRoundTask(CurrentUser user, String taskId) throws Exception {
		PassRoundUtils.clearPassRoundTask(user, taskId);
		
	}

	public List findPassRoundTask(CurrentUser user, WfSearch wfsearch, int pageNo, int pageSize) throws Exception {
		return PassRoundUtils.findPassRoundTask(user, wfsearch, pageNo, pageSize);
	}

	public int getPassRoundTaskCount(CurrentUser user, WfSearch wfsearch) throws Exception {
		return PassRoundUtils.getPassRoundTaskCount(user, wfsearch);
	}
	
	public void sendPassRoundTask(WorkflowInfo workflowInfo, String stepId) throws Exception {
		List signerList = PassRoundUtils.getSignerListByStepId(stepId, workflowInfo);
		PassRoundUtils.sendPassRoundTask(workflowInfo, signerList);		
	}

	public String getPkForStep(WorkflowInfo workflowInfo){
		return wfservice.getPkForStep(workflowInfo);
	}

	public String findWorkflowSponsor(WorkflowInfo info) throws Exception {
		return WfCommonTools.findWorkflowSponsor(info);
	}

	public String getPreSpecialObject(WorkflowInfo info) {
		List list = info.getHistorySteps();
		WorkflowStep step = (WorkflowStep)list.get(list.size() - 1);
		return this.getSpecialObject(info.getWorkflowName(), step.getId());
	}
	
	private void setCompareStepMap(Map map,WorkflowInfo now) throws Exception{
		String key = now.getId();
		Entity oldTemp = (Entity)((Map) SecurityContextInfo.sessionManagerFactory("workFlowHistoryInfo")).get(key);
		//主体BEAN数据
		ClonedObject old  = ObjectOperateTools.falseClone(oldTemp);
		//对现有的实体BEAN也进行一次COPY,根据拷贝的对象进行比较
    	//设置反射拷贝标志
    	((EntityPlus)now).setReflectCopyEntityProperties(EntityPlus.isReflectCopyEntity);
    	Object nowTemp = oldTemp.getClass().newInstance();
        BeanUtils.copyProperties(nowTemp, now);
    	//设置反射拷贝标志
        ((EntityPlus)now).setReflectCopyEntityProperties(EntityPlus.isNotReflectCopyEntity);
		
		
		map.put("oldMainBean",old);
		map.put("newMainBean", nowTemp);
		//从表实体BEAN数据的初始化
		Map subListMap = new HashMap(0);
		map.put("subListMap", subListMap);
		//subListMap初始化规则
		//key值为旧的对象
		//value为对应的新的对象
		List allChildProperties = (List) map.get("history");
		if(null != allChildProperties){
			for(int i = 0 ; i < allChildProperties.size() ; i++ ){
				String property = (String) allChildProperties.get(i);
				Object subOld = getPropertyValue(oldTemp,property);
				Object subNew = getPropertyValue(nowTemp,property);
				subListMap.put(chanagePropertyType(subOld,0), chanagePropertyType(subNew,1));
			}		
		}
	}
	
	private Object getPropertyValue(Object src , String name){
		if (src == null){
			return null;
		}
		int indexOfINDEXED_DELIM = -1;
		indexOfINDEXED_DELIM = name.indexOf('.');
        if(indexOfINDEXED_DELIM >= 0){
        	String nameTemp = new String("");
        	String nameForNext =  new String("");
        	nameTemp = name.substring(0, indexOfINDEXED_DELIM);
        	nameForNext = name.substring(indexOfINDEXED_DELIM+1);
        	return getPropertyValue(Tools.getProperty(src, nameTemp),nameForNext);
        }
        else{
        	return Tools.getProperty(src, name);
        }
	}
	
	//类型转型
	private Object chanagePropertyType(Object src,int status) throws Exception{
		List temp = new ArrayList(0);
		//Set转型
		if(src!=null && Set.class.isAssignableFrom(src.getClass())){
			temp = ObjectOperateTools.toList((Set)src);
		}
		//非SET转型和LIST转型
		if(src!=null && !Set.class.isAssignableFrom(src.getClass())&&!List.class.isAssignableFrom(src.getClass())){
			temp.add(src);
		}else{
			if(List.class.isAssignableFrom(src.getClass())){
				temp = (List) src;
			}
		}
		//旧对象数组需要转型
		if(status == 1){
			return temp;
		}else{
			List oldTemp = ObjectOperateTools.falseCloneList(temp);
			return oldTemp;
		}
		//对象转型
//		if(Entity.class.isAssignableFrom(src.getClass())){
//			ClonedObject Temp  = ObjectOperateTools.falseClone((Entity)src);
//			return Temp;
//		}
	}	
	
	private String[] dealSubBeanCompareMap(Map map){
		List allChildProperties = (List) map.get("history");
		if(null != allChildProperties){
			final int size = allChildProperties.size();
			String temp[] = (String[])allChildProperties.toArray(new String[size]);			
			return 	temp;
		}else{
			return null;
		}
	}

	public void doTransition(WorkflowInfo workflowInfo) throws Exception {
		this.doTransition(workflowInfo, Collections.EMPTY_MAP);
		
	}
}