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
 * The service interface of workflow ����������ӿ�
 * 
 * @author Bis liyan
 */
public interface IWorkflowService {

	public String getPreSpecialObject(WorkflowInfo info);
	/**
	 * ���step�϶��������role;��","�ŷָ�
	 */
	public String getRoles(String workflowName, String stepId);

	/**
	 * ���step�ϵ��������
	 */
	public String getSpecialObject(String workflowName, String stepId);

	/**
	 * ���Nextstep�ϵ��������
	 */
	public String getNextStepSpecObj(String workflowName, String currId);

	/**
	 * ���PointType,ָ�����췽����ʶ
	 */
	public String getPointType(String workflowName, String stepId);

	public List getRolesOfStepByActionId(WorkflowInfo wfInfo) throws Exception;

	public List getUsersOfStepByActionId(WorkflowInfo wfInfo) throws Exception;

	/**
	 * ��ʼ������
	 */
	public void initializeSubWorkflow(WorkflowRelation wr,
			WorkflowInfo subWfInfo) throws Exception;

	/**
	 * ����������
	 */
	public void unlockSuperWorkflow(WorkflowRelation wr) throws Exception;

	/**
	 * �õ�step�ϱ�ָ����user
	 */
	public Set getPointedUserOfStep(String taskId);

	public WorkflowStep getNextStep(WorkflowInfo workflowInfo);

	/**
	 * �õ�step�ϵĿ���ָ����user
	 */
	public List getUsersOfStep(String workflowName, String stepId);

	/**
	 * �������ʵ���ĸ���
	 */
	public int getFlowEntryCount(WfSearch wfsearch) throws Exception;

	/**
	 * �������ʵ��
	 */
	public List findFlowEntry(WfSearch wfsearch, int pageNo, int pageSize)
			throws Exception;

	/**
	 * ��õ�ǰ�û��Ĵ����������
	 */
	public int getWaitTaskCount(CurrentUser user, WfSearch wfsearch)
			throws Exception;

	/**
	 * ��õ�ǰ�û��Ĵ�������
	 */
	public List findWaitTask(CurrentUser user, WfSearch wfsearch, int pageNo,
			int pageSize) throws Exception;

	/**
	 * ��õ�ǰ�û����Ѱ��������
	 */
	public int getOverTaskCount(CurrentUser user, WfSearch wfsearch)
			throws Exception;

	/**
	 * ��õ�ǰ�û����Ѱ�����
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
	 * Find current user's passround task ��õ�ǰ�û��Ĵ�������
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
	 * get passround task's count ��õ�ǰ�û��Ĵ����������
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
	 * clear one passround's task �����ǰ�û��Ĵ�������
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
	 * send passround's task ���ʹ�������
	 * 
	 * @param workflowInfo 
	 *            instance of workflow
	 * @param stepId
	 *            want passround step's id
	 * @throws Exception
	 */
	public void sendPassRoundTask(WorkflowInfo workflowInfo, String stepId) throws Exception;
	/**
	 * �õ���¼�����ʷ���ݵ�����
	 * 
	 * @param workflowInfo 
	 *            instance of workflow
	 * @return pk	
	 */
	public String getPkForStep(WorkflowInfo workflowInfo);
	/**
	 * �õ����̷�����
	 * 
	 * @param workflowInfo
	 * @return empCd
	 */
	public String findWorkflowSponsor(WorkflowInfo info) throws Exception;
	
	public void sendTaskWhenScratch(WorkflowInfo info) throws Exception;
		
}
