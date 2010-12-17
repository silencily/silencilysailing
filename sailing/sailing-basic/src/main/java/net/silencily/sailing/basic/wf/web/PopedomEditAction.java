package net.silencily.sailing.basic.wf.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.wf.domain.TblWfEditInfo;
import net.silencily.sailing.basic.wf.service.PopedomEditService;
import net.silencily.sailing.common.context.MessageTypeContext;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.context.OperType;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PopedomEditAction extends  BisWorkflowActionSupport{
	
	protected String getFullActionPath() {
		
		return "/wf/PopedomEditAction.do";
	}

	
	public static final String FORWARD_LIST = "list";
	
	public static final String FORWARD_INFO = "info";
	
	public static final String FORWARD_ENTRY = "entry";
	
	public static final String MOCK_TABEL = "wf_popedomEdit";
	
	/**
	 * 
	 * 功能描述 获得公共Service
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2008-06-10 18:47:39
	 * @version 1.0
	 * @author yangxl
	 */
	private CommonService getCommonService() {
		
		return (CommonService) ServiceProvider.getService(CommonService.SERVICE_NAME);
	}

	public static PopedomEditService getService() {
		return (PopedomEditService) ServiceProvider
				.getService(PopedomEditService.SERVICE_NAME);
	}

	/**
	 * 
	 * 功能描述 进入
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2008-06-10 18:47:39
	 * @version 1.0
	 * @author yangxl
	 */
	public ActionForward entry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward(FORWARD_ENTRY);
	}
	
	/**
	 * 
	 * 功能描述 列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2008-05-14 18:47:39
	 * @version 1.0
	 * @author yangxl
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CurrentUser ucn = SecurityContextInfo.getCurrentUser();
		request.setAttribute("viewBean", getCommonService().fetchAll(
				TblWfEditInfo.class, ucn.getEmpCd(), MOCK_TABEL));
		return mapping.findForward(FORWARD_LIST);
	}
	
	
	/**
	 * 
	 * 功能描述 删除
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2008-06-10 18:47:39
	 * @version 1.0
	 * @author yangxl
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//获得要删除合同的oid
		String oid = request.getParameter("oid");
		
		// 当oid为空时，直接返回
		if(StringUtils.isBlank(oid)){
			return list(mapping, form, request, response);
		}

		try {
			// 删除该合同
			getService().delete(oid);
				
		} catch (Exception e) {
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("CM_I046_C_0"));
			return list(mapping, form, request, response);
		}
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("CM_I047_C_0"));	
		return list(mapping, form, request, response);
	}
	
	/**
	 * 
	 * 功能描述 详细
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2008-06-10 18:47:39
	 * @version 1.0
	 * @author yangxl
	 */
	public ActionForward info(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		PopedomEditForm theForm = (PopedomEditForm)form;
		  
		int rwCtrlType = theForm.getBean().getRwCtrlType();
		int operType = MessageTypeContext.messageType(request, rwCtrlType);
		switch(operType){
			case OperType.ADD:
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"CM_I048_C_0"));
				break;
			case OperType.EDIT:
				theForm.setBean((TblWfEditInfo) getCommonService().load(
						TblWfEditInfo.class, theForm.getOid()));
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"CM_I050_C_0"));
				break;
			case OperType.VIEW:
				theForm.setBean((TblWfEditInfo) getCommonService().load(
						TblWfEditInfo.class, theForm.getOid()));
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage("CM_I049_C_0"));
				break;
		}
	
		return mapping.findForward(FORWARD_INFO);
		
	}
	
	/**
	 * 
	 * 功能描述 保存
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2008-05-14 18:47:39
	 * @version 1.0
	 * @author yangxl
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		PopedomEditForm theForm = (PopedomEditForm)form;
		String oid = theForm.getBean().getId();
		//调用INFO处理
		theForm.setCallinfo("T");
		//清空message
		MessageUtils.clearMessages(request);

		try {
			getService().save(theForm);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("CM_I051_C_0"));
			return mapping.findForward(FORWARD_INFO);
		}
		if (StringUtils.isBlank(oid)) {
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("CM_I005_R_0"));
		} else {
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("CM_I004_R_0"));
		}
		theForm.setOid(theForm.getBean().getId());
		return info(mapping, form, request, response);
	}
	
	protected void doTransition(BisWorkflowFormSupport bisworkflowformsupport) throws Exception {
		// TODO Auto-generated method stub
		
	}

	protected void initializeWorkflow(BisWorkflowFormSupport bisworkflowformsupport) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
