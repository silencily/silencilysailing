/**
 * 
 */
package net.silencily.sailing.basic.wf.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.util.BisWfServiceLocator;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.struts.DispatchActionPlus;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author yushn
 *
 */
public class CommonAction extends DispatchActionPlus{
	/**
	 * 
	 * @param user
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static boolean canCommit(String user, WorkflowInfo bean)
	throws Exception {
		// 如果指定了字段，则与指定字段进行比较
	
//		String fieldName = bean.getSpecialObject();
//		if (StringUtils.isNotBlank(fieldName)) {
//			String p = (String) PropertyUtils
//					.getNestedProperty(bean, fieldName);
//			if (StringUtils.isNotBlank(p)) {
//				if (user.equals(p))
//					return true;
//				else
//					return false;
//			}
//		}
//		// 与指定的待办人比较
//		Set users1 = BisWfServiceLocator.getWorkflowService()
//				.getPointedUserOfStep(bean);
//		if (null != users1 && users1.size() > 0) {
//			if (users1.contains(user))
//				return true;
//			else
//				return false;
//		}
//		// 与当前步骤允许的人员比较
//		String stepid = BisWfServiceLocator.getWorkflowService()
//				.getCurrentStep(bean).getId();
//		List users2 = BisWfServiceLocator.getWorkflowService().getUsersOfStep(
//				bean.getWorkflowName(), stepid);
//		if (null != users2 && users2.size() > 0) {
//			if (users2.contains(user))
//				return true;
//			else
//				return false;
//		}
		return false;

}
	/**
	 * 获取下一步指定action的可待办人
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward querySigners(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String stepId = request.getParameter("stepId");
		WorkflowInfo bean = getBean(request);
		//List list = BisWfServiceLocator.getWorkflowService().getUsersOfStepByActionId(bean);
		String workflowName = bean.getWorkflowName();
		if (WorkflowInfo.STATUS_SCRATCH.equals(bean.getWorkflowStatus()) && StringUtils.isBlank(workflowName)){
			workflowName = request.getParameter("workflowName");
		}else {
			workflowName = bean.getWorkflowName();
		}
		List list = BisWfServiceLocator.getWorkflowService().getUsersOfStep(workflowName, stepId);
		request.setAttribute("signers", list);
		return mapping.findForward("querySigners");
	}
	/**
	 * AJAX方式获取下一步指定action的可待办人
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
//	public ActionForward getNextStepSigners(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception 
//	{
//		String actionId = request.getParameter("actionId");
//		WorkflowInfo bean = getBean(request);
//		bean.setActionId(actionId);
//		List list = BisWfServiceLocator.getWorkflowService().getUsersOfStepByActionId(bean);
//		response.setContentType("text/xml");
//		//response.setCharacterEncoding("GBK");
//		for(int i=0;i<list.size();i++)
//		{
//			if(i!=0)
//				response.getWriter().print("$");
//			String empcd = ((TblCmnUser)list.get(i)).getEmp().getEmpCd();
//			response.getWriter().print(empcd);
//		}
//		response.getWriter().close();
//		response.setHeader("Cache-Control", "no-cache");
//		response.setHeader("Pragma", "no-cache");
//		return null;
//	}
	public ActionForward canCurrentUserCommit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WorkflowInfo bean = getBean(request);
		// 加载工作流信息
		
		response.setContentType("text/xml");
		//response.setCharacterEncoding("GBK");
		String user = SecurityContextInfo.getCurrentUser().getEmpCd();
		if (canCommit(user, bean))
			response.getWriter().print("true");
		else
			response.getWriter().print("false");
		response.getWriter().close();
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		return null;
	}
	private static WorkflowInfo getBean(HttpServletRequest request) throws Exception
	{
		String oid = request.getParameter("oid");
		String className = request.getParameter("className");
		WorkflowInfo bean = null;
		if(StringUtils.isBlank(oid))
		{
			bean = (WorkflowInfo)Class.forName(className).newInstance();
		}
		else
			bean = (WorkflowInfo)getService().load(Class.forName(className), oid);
		return bean;
	}
	/**
	 * 获取共通服务
	 * @return
	 */
	private static CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}

}
