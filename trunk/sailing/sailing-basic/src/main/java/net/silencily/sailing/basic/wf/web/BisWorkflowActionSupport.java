package net.silencily.sailing.basic.wf.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.wf.util.WorkflowUtils;
import net.silencily.sailing.common.context.WorkFlowFormContext;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class BisWorkflowActionSupport extends DispatchActionPlus {

	public BisWorkflowActionSupport() {
	}

	protected abstract String getFullActionPath();

	protected abstract void initializeWorkflow(
			BisWorkflowFormSupport bisworkflowformsupport)throws Exception;

	protected abstract void doTransition(
			BisWorkflowFormSupport bisworkflowformsupport)throws Exception;

//	protected abstract void populateTodoList(
//			BisWorkflowFormSupport bisworkflowformsupport);

	public ActionForward initializeWorkflow(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BisWorkflowFormSupport bisWorkflowFormSupport = (BisWorkflowFormSupport) form;
		initializeWorkflow(bisWorkflowFormSupport);
		MessageUtils
				.addMessage(request,
						"\u7533\u8BF7\u5DF2\u7ECF\u63D0\u4EA4, \u8BF7\u7B49\u5F85\u5BA1\u6279");
		ActionForward forward;
		if (bisWorkflowFormSupport.isHasForwardUrl()) {
			forward = new ActionForward(bisWorkflowFormSupport.getForwardUrl());
		} else {
			forward = new ActionForward(getFullActionPath()
					+ "?step=list&listStep="
					+ bisWorkflowFormSupport.getListStep()
					+ "&paginater.page=0");
		}
		return forward;
	}

	public ActionForward doTransition(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		BisWorkflowFormSupport bisWorkflowFormSupport = (BisWorkflowFormSupport) form;
		doTransition(bisWorkflowFormSupport);
		//将表单在流内部的标志清除
		WorkFlowFormContext.setTag(null);
		MessageUtils.addMessage(request, "\u64CD\u4F5C\u6210\u529F");
		ActionForward forward;
		if (bisWorkflowFormSupport.isHasForwardUrl()) {
			forward = new ActionForward(bisWorkflowFormSupport.getForwardUrl());
		} else {
			String url = WorkflowUtils.findWfUrl("entry") + bisWorkflowFormSupport.getUrlKey();
			forward = new ActionForward(url);
		}		
		return forward;
	}

//	public ActionForward getTodoList(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		BisWorkflowFormSupport bisWorkflowFormSupport = (BisWorkflowFormSupport) form;
//		populateTodoList(bisWorkflowFormSupport);
//		return mapping.findForward("list");
//	}

	public ActionForward workflowInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("workflowInfo");
	}

	public static final String FORWARD_LIST = "list";

	public static final String FORWARD_WORKFLOW_INFO = "workflowInfo";
}
