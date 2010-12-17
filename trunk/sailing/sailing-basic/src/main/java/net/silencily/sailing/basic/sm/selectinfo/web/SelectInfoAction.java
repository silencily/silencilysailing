package net.silencily.sailing.basic.sm.selectinfo.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserMember;
import net.silencily.sailing.basic.sm.user.service.UserManageService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.DispatchActionPlus;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;



public class SelectInfoAction extends DispatchActionPlus {

	
	public static final String FORWARD_ENTRY = "selectEntry";
	public static final String FORWARD_INFO = "selectInfo";

	/**
	 * 调用共通服务
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
	
	public static UserManageService service() {
		return (UserManageService) ServiceProvider
				.getService(UserManageService.SERVICE_NAME);
	}

	public ActionForward selectEntry(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {

		return mapping.findForward("selectEntry");
	}
	
	
	public ActionForward selectDataEntry(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {

		return mapping.findForward("selectDataEntry");
	}
	public ActionForward selectInfo(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		
		SelectInfoForm theForm=(SelectInfoForm)form;
		String checkType = request.getParameter("checkType");
		if (checkType != null && !"".equals(checkType)) {
			theForm.setCheckType(checkType);
		} else {
			theForm.setCheckType("radio");
		}
		theForm.setInfoList(getService().getList(TblCmnUser.class)); 
		return mapping.findForward("selectInfo");
	}
	
	public ActionForward selectDataInfo(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		
		SelectInfoForm theForm=(SelectInfoForm)form;
		String checkType = request.getParameter("checkType");
		if (checkType != null && !"".equals(checkType)) {
			theForm.setCheckType(checkType);
		} else {
			theForm.setCheckType("radio");
		}
		theForm.setInfoList(getService().getList(TblCmnUserMember.class)); 
		return mapping.findForward("selectDataInfo");
	}
}

