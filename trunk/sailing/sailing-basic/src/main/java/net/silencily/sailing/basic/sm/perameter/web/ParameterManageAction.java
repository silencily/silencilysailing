package net.silencily.sailing.basic.sm.perameter.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnSysParameter;
import net.silencily.sailing.basic.sm.perameter.service.ParameterManageService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class ParameterManageAction extends DispatchActionPlus{
	
	public static final String FORWARD_ENTRY="entry";
	
	public static final String FORWARD_LIST="list";
	
	public static final String FORWARD_INFO="info";
	
	public static final String CMN_PARAMETER="cmn_parameter";
	
	/**
	 * �������� ���ù��ýӿ�
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
	
	/**
	 * 
	 * �������� ���ýӿڷ���
	 * @return
	 * 2007-11-30 ����10:23:33
	 * @version 1.0
	 * @author baihe
	 */
	public static ParameterManageService service() {
		return (ParameterManageService) ServiceProvider
				.getService(ParameterManageService.SERVICE_NAME);
	}
	
	/**
	 * 
	 * �������� ��¼
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-30 ����07:42:38
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward entry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward(FORWARD_ENTRY);
	}
	
	/**
	 * 
	 * �������� ϵͳ�����б�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-30 ����07:07:32
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String empCd = SecurityContextInfo.getCurrentUser().getEmpCd();
		List createdTime=new ArrayList();
		List temp=new ArrayList(0);
		createdTime.add("createdTime");
		request.setAttribute("viewBean",getService().fetchAll(TblCmnSysParameter.class,empCd,CMN_PARAMETER, createdTime, temp));
		return mapping.findForward(FORWARD_LIST);
	}
	
	/**
	 * 
	 * �������� ϵͳ������ϸ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-30 ����08:03:22
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward info(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			ParameterManageForm theForm=(ParameterManageForm)form;
			request.setAttribute(mapping.getAttribute(), theForm);
			return mapping.findForward(FORWARD_INFO);
	}
	
	/**
	 * 
	 * �������� ɾ���б���Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-3 ����11:27:35
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ParameterManageForm theForm = (ParameterManageForm)form;
		TblCmnSysParameter bean = theForm.getSysParameter();
		String sign = bean.getParameterSign();
//		getService().deleteLogic(bean);
		getService().delete(bean);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I036_R_1",sign ));
		return list(mapping, form, request, response);
	}
	
	/**
	 * 
	 * �������� �޸Ĳ�����Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-3 ����06:58:33
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ParameterManageForm theForm = (ParameterManageForm)form;
		TblCmnSysParameter bean=theForm.getSysParameter();
		if(StringUtils.isBlank(bean.getId())){
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I002_R_0"));
		}else{
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I003_R_0"));			
		}
		getService().saveOrUpdate(bean);
		return info(mapping, form, request, response);
	}
}
