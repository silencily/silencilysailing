/**
 * Name: IWorkflowService.java
 * Author: Bis liyan
 */
package net.silencily.sailing.basic.wf.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.dto.WfSearch;
import net.silencily.sailing.basic.wf.entry.WorkflowRelation;
import net.silencily.sailing.basic.wf.entry.WorkflowStep;
import net.silencily.sailing.security.model.CurrentUser;


/**
 * The service interface of workflow 工作流服务接口
 * 
 * @author Bis liyan
 */
public interface IWorkflowService {

	public String getPreSpecialObject(WorkflowInfo info);
	/**
	 * 获得step上定义的所有role;用","号分隔
	 */
	public String getRoles(String workflowName, String stepId);

	/**
	 * 获得step上的特殊对象
	 */
	public String getSpecialObject(String workflowName, String stepId);

	/**
	 * 获得Nextstep上的特殊对象
	 */
	public String getNextStepSpecObj(String workflowName, String currId);

	/**
	 * 获得PointType,指定待办方法标识
	 */
	public String getPointType(String workflowName, String stepId);

	public List getRolesOfStepByActionId(WorkflowInfo wfInfo) throws Exception;

	public List getUsersOfStepByActionId(WorkflowInfo wfInfo) throws Exception;

	/**
	 * 初始化子流
	 */
	public void initializeSubWorkflow(WorkflowRelation wr,
			WorkflowInfo subWfInfo) throws Exception;

	/**
	 * 给父流解锁
	 */
	public void unlockSuperWorkflow(WorkflowRelation wr) throws Exception;

	/**
	 * 得到step上被指定的user
	 */
	public Set getPointedUserOfStep(String taskId);

	public WorkflowStep getNextStep(WorkflowInfo workflowInfo);

	/**
	 * 得到step上的可以指定的user
	 */
	public List getUsersOfStep(String workflowName, String stepId);

	/**
	 * 获得流程实例的个数
	 */
	public int getFlowEntryCount(WfSearch wfsearch) throws Exception;

	/**
	 * 获得流程实例
	 */
	public List findFlowEntry(WfSearch wfsearch, int pageNo, int pageSize)
			throws Exception;

	/**
	 * 获得当前用户的待办任务个数
	 */
	public int getWaitTaskCount(CurrentUser user, WfSearch wfsearch)
			throws Exception;

	/**
	 * 获得当前用户的待办任务
	 */
	public List findWaitTask(CurrentUser user, WfSearch wfsearch, int pageNo,
			int pageSize) throws Exception;

	/**
	 * 获得当前用户的已办任务个数
	 */
	public int getOverTaskCount(CurrentUser user, WfSearch wfsearch)
			throws Exception;

	/**
	 * 获得当前用户的已办任务
	 */
	public List findOverTask(CurrentUser user, WfSearch wfsearch, int pageNo,
			int pageSize) throws Exception;

	public List findConsignTask(CurrentUser user, WfSearch wfsearch,
			int pageNo, int pageSize) throws Exception;

	public List findConsignedTask(CurrentUser user, WfSearch wfsearch,
			int pageNo, int pageSize) throws Exception;

	public void loadWorkflowInfo(WorkflowInfo workflowInfo, String taskId)
			throws Exception;

	public void doTransition(WorkflowInfo workflowInfo, Map map)
			throws Exception;
	
	public void doTransition(WorkflowInfo workflowInfo)
	throws Exception;

	public void initializeWorkflow(WorkflowInfo workflowInfo) throws Exception;

	public void initializeWorkflow(WorkflowInfo workflowInfo,Map map) throws Exception;
	
	public void termainateWorkflow(WorkflowInfo workflowInfo, String value) throws Exception;
	
	public void terminateWorkflow(WorkflowInfo workflowInfo) throws Exception;

	public String getCurrentStepId(WorkflowInfo workflowInfo);

	/**
	 * Find current user's passround task 获得当前用户的传阅任务
	 * 
	 * @param user
	 *            current's user
	 * @param wfsearch
	 *            search's condition
	 * @param pageNo
	 *            page's no
	 * @param pageSize
	 *            one page' size
	 * @throws Exception
	 * @return passround task's list
	 */
	public List findPassRoundTask(CurrentUser user, WfSearch wfsearch,
			int pageNo, int pageSize) throws Exception;

	/**
	 * get passround task's count 获得当前用户的传阅任务个数
	 * 
	 * @param user
	 *            current's user
	 * @param wfsearch
	 *            search's condition
	 * @throws Exception
	 * @return passround task's count
	 */
	public int getPassRoundTaskCount(CurrentUser user, WfSearch wfsearch)
			throws Exception;

	/**
	 * clear one passround's task 清除当前用户的传阅任务
	 * 
	 * @param user
	 *            current's user
	 * @param taskId
	 *            task's id
	 * @throws Exception
	 */
	public void clearPassRoundTask(CurrentUser user, String taskId)
			throws Exception;

	/**
	 * send passround's task 发送传阅任务
	 * 
	 * @param workflowInfo 
	 *            instance of workflow
	 * @param stepId
	 *            want passround step's id
	 * @throws Exception
	 */
	public void sendPassRoundTask(WorkflowInfo workflowInfo, String stepId) throws Exception;
	/**
	 * 得到记录变更历史数据的主键
	 * 
	 * @param workflowInfo 
	 *            instance of workflow
	 * @return pk	
	 */
	public String getPkForStep(WorkflowInfo workflowInfo);
	/**
	 * 得到流程发起者
	 * 
	 * @param workflowInfo
	 * @return empCd
	 */
	public String findWorkflowSponsor(WorkflowInfo info) throws Exception;
	
	public void sendTaskWhenScratch(WorkflowInfo info) throws Exception;
		
}
