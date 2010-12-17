/**
 * Name: WorkflowUtils.java
 * Author: Bis liyan
 */
package net.silencily.sailing.basic.wf.util;

import java.util.HashMap;
import java.util.Map;

import net.silencily.sailing.basic.wf.WorkflowInfo;

import org.springframework.util.Assert;


/**
 * One util for some workflow's operation 对于工作流一些操作的工具类
 * 
 * @author Bis liyan
 */
public abstract class WorkflowUtils {

	public WorkflowUtils() {
	}

	/**
	 * Get one Chinese name of workflow's status by English name
	 * 得到一个工作流状态的中文名称，通过英文名称
	 * 
	 * @param workflowStatus
	 *            It's a English name of workflow's status
	 * @return workflowStatusName
	 */
	public static String getWorkflowStatusName(String workflowStatus) {
		String workflowStatusName = (String) WORKFLOW_STATUS_MAP
				.get(workflowStatus);
		return workflowStatusName != null ? workflowStatusName
				: "\u672A\u77E5\u72B6\u6001";
	}

	/**
	 * Judge one workflowInfo is can be initialize 判断一个workflowInfo是否可以初始化
	 * 
	 * @param workflowInfo
	 *            instance of workflow
	 * @return ture or false
	 */
	public static boolean isWorkflowCanBeInitialized(WorkflowInfo workflowInfo) {
		Assert.notNull(workflowInfo, "workflowInfo required");
		return workflowInfo.getWorkflowStatus() == null
				|| workflowInfo.getWorkflowStatus().equals("scratch");
	}

	/**
	 * delete start 2008-02-18 11:14 liyan public static WorkflowStep
	 * getHistoryStepByStepId( WorkflowInfo workflowInfo, String stepId) {
	 * Assert.notNull(stepId, "stepId required"); Map historyStepMap =
	 * getHistoryStepMap(workflowInfo); return (WorkflowStep)
	 * historyStepMap.get(stepId); }
	 * 
	 * public static Map getHistoryStepMap(WorkflowInfo workflowInfo) {
	 * Assert.notNull(workflowInfo, "workflowInfo required"); List list =
	 * workflowInfo.getHistorySteps(); if (CollectionUtils.isEmpty(list)) return
	 * Collections.EMPTY_MAP; Map map = new LinkedHashMap(list.size());
	 * WorkflowStep step; for (Iterator iter = list.iterator(); iter.hasNext();
	 * map.put(step .getId(), step)) step = (WorkflowStep) iter.next();
	 * 
	 * return map; }
	 * 
	 * public static List getTodoList(List ids, DetachedCriteria
	 * orignalCriteria) { org.hibernate.criterion.Criterion criterion =
	 * CriterionUtils .splitIdsConditionIfNecessary(ids);
	 * orignalCriteria.add(criterion); return
	 * DaoHelper.getHibernateTemplate().findByCriteria(orignalCriteria); }
	 * delete end 2008-02-18 11:14 liyan
	 */
	private static final Map WORKFLOW_STATUS_MAP = new HashMap() {

		private static final long serialVersionUID = -1841814371685403017L;

		{
			put("scratch", "\u8349\u7A3F");
			put("processing", "\u5904\u7406\u4E2D");
			put("finish", "\u5DF2\u5B8C\u6210");
			put("suspend", "\u6302\u8D77");
			put("killed", "\u88AB\u53D6\u6D88");
			put("archives", "\u5DF2\u5F52\u6863");
			put("untread", "\u88AB\u9000\u56DE");
			put("retake", "\u91CD\u65B0\u7F16\u5236");
			put("lockup", "\u8F6C\u5165\u5B50\u6D41\u7A0B");
		}
	};

	/**
	 * Find an url by key 返回一个url通过key
	 * 
	 * @param key
	 * @return url
	 */
	public static String findWfUrl(String key) {
		return (String) WORKFLOW_URL_MAP.get(key);
	}

	private static final Map WORKFLOW_URL_MAP = new HashMap() {

		private static final long serialVersionUID = -4400217288202726448L;

		{
			put("entry", "/wf/personWfSearchAction.do?step=entry");
			put("waitList", "/wf/personWfSearchAction.do?step=waitList");
			put("alreadyList", "/wf/personWfSearchAction.do?step=alreadyList");
			put("recieveList", "/wf/personWfSearchAction.do?step=recieveList");
			put("entrustList", "/wf/personWfSearchAction.do?step=entrustList");
			put("allList", "/wf/personWfSearchAction.do?step=allList");
			put("searchList", "/wf/personWfSearchAction.do?step=searchList");
			put("system", "/sm/wfManageAction.do?step=entry");
			// add start 2008-02-20 13:55 liyan
			put("passroundList",
					"/wf/personWfSearchAction.do?step=passroundList");
			// add end 2008-02-20 13:55 liyan
		}
	};

	/**
	 * 判断一个对象是否是流程对象，并且其状态不是草稿，已完成，取消
	 * 
	 * @param anObject
	 * @return flg
	 */
	public static boolean isInstanceOfWorkflowAndStatus(Object anObject) {
		boolean flg = false;
		if (WorkflowInfo.class.isAssignableFrom(anObject.getClass())) {
			WorkflowInfo info = (WorkflowInfo) anObject;

			if (WorkflowInfo.STATUS_SCRATCH.equals(info.getWorkflowStatus())) {
				flg = false;
			} else if (WorkflowInfo.STATUS_FINISH.equals(info
					.getWorkflowStatus())) {
				flg = false;
			} else if (WorkflowInfo.STATUS_KILLED.equals(info
					.getWorkflowStatus())) {
				flg = false;
			} else {
				flg = true;
			}

		}
		return flg;
	}
}
