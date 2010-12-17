package net.silencily.sailing.basic.sm.user.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnMsgConfig;
import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserMember;
import net.silencily.sailing.basic.sm.domain.TblCmnUserPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUserRole;
import net.silencily.sailing.basic.sm.user.service.UserManageService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;
import net.silencily.sailing.utils.SecurityUtils;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * 
 * 
 * @author baihe
 * @version 1.0
 */
public class UserManageAction extends DispatchActionPlus {

	public static final String FORWARD_ENTRY = "entry";

	public static final String FORWARD_LIST = "list";

	public static final String FORWARD_INFO = "info";

	public static final String FORWARD_PERMISSION = "permission";

	public static final String FORWARD_ROLE = "role";

	public static final String FORWARD_MSG = "msg";

	public static final String FORWARD_PASSWORD = "password";

	public static final String CMN_USER = "cmn_user";

	public static final String FORWARD_USERPERDETAILED = "userPerDetailed";

	public static final String FORWARD_ACHIEVEROLEPERMISSION = "achieveRolePermission";

	public static final String FORWARD_SELECTINFO = "selectInfo";
	
	public static final String FORWARD_SELECTINFOFORCONSIGN = "selectInfoForConsign";

	public static final String FORWARD_ACHIEVEROLEUSER = "achieveRoleUser";
	
	public static final String FORWARD_ROLEPERUSERENTRY = "rolePerUserEntry";
	public static final String FORWARD_SELECTUSER = "selectUser";
	public static final String FORWARD_SELECCURUSER = "selectCurUser";
	

	/**
	 * 
	 * �������� ���ýӿڷ���
	 * 
	 * @return 2007-11-23 ����07:13:11
	 * @version 1.0
	 * @author baihe
	 */
	public static UserManageService service() {
		return (UserManageService) ServiceProvider
				.getService(UserManageService.SERVICE_NAME);
	}

	/**
	 * 
	 * �������� ��¼
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:13:25
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
	 * �������� �б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:13:39
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CurrentUser ucn = SecurityContextInfo.getCurrentUser();
		List createdTime = new ArrayList();
		List temp = new ArrayList(0);
		createdTime.add("createdTime");
		request.setAttribute("viewBean", getService().fetchAll(
				TblCmnUser.class, ucn.getEmpCd(), CMN_USER));
		return mapping.findForward(FORWARD_LIST);
	}

	/**
	 * 
	 * �������� ��ϸ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:17:13
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward info(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		if (theForm.getOid() == null || "".equals(theForm.getOid())) {
			TblCmnUser bean = new TblCmnUser();
			bean.setStatus("1");
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
					"SM_I001_P_0"));
			theForm.setBean(bean);
			request.setAttribute(mapping.getAttribute(), theForm);
			return mapping.findForward(FORWARD_INFO);
		}
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("HR_I042_P_0"));
		request.setAttribute(mapping.getAttribute(), theForm);
		return mapping.findForward(FORWARD_INFO);
	}

	/**
	 * 
	 * �������� Ȩ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:14:00
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward queryPermissions(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		List list = null;
		if (null != bean.getId()) {
			list = service().getPermissions(bean);
		} else {
			list = new ArrayList(0);
		}

		theForm.setPermissions(list);

		return mapping.findForward(FORWARD_PERMISSION);
	}
	
	/**
     * 
     * �������� ����Ȩ��
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @version 1.0
     * @author chenchen
     */
	public ActionForward dataPermission(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		List list = null;
		if (null != bean.getId()) {
			list = service().getDataPermission(bean);
		}
		String userId = request.getParameter("userId");
		if (null != request.getParameter("userId")){
			list = service().getDataPermission((TblCmnUser)getService().load(TblCmnUser.class, userId));
		}
		theForm.setDataPermission(list);
		if (null != request.getParameter("userId")){
		    MessageUtils.addMessage(request, "Ȩ�޼���ɹ�!");
		}
		return mapping.findForward("dataPermission");
	}

	/**
	 * 
	 * �������� ��ɫ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:14:19
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward queryRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		List list = null;
		if (null != bean.getId()) {
			list = service().getRole(bean);
		} else {
			list = new ArrayList(0);
		}
		theForm.setRole(list);

		return mapping.findForward(FORWARD_ROLE);
	}

	/**
	 * 
	 * �������� ��Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:15:06
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward msg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		TblCmnMsgConfig msg = new TblCmnMsgConfig();
		if (bean.getMsgConfigs().iterator().hasNext()) {
			msg = (TblCmnMsgConfig) bean.getMsgConfigs().iterator().next();
		} else {
			msg.setSmsService("0");
			msg.setImService("0");
			msg.setEmailService("0");
		}
		theForm.setCmnMsgConfig(msg);
		return mapping.findForward(FORWARD_MSG);

	}

	/**
	 * 
	 * �������� �����û��󱣴淽��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:19:25
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		//ȡ�û�����ɫ
		TblCmnRole baseRole = (TblCmnRole)service().getBaseRole();
		// ����Ĭ�Ͽ���
		bean.setPassword(SecurityUtils.passwordHex(null));
		bean.setFailedTimes("0");
		String message = "";
		try {
			getService().add(bean);
			theForm.setOid(bean.getId());
			message = MessageInfo.factory().getMessage("SM_I002_R_0");
		} catch (DataIntegrityViolationException e) {
			message = MessageInfo.factory().getMessage("SM_I023_R_0");
			MessageUtils.addWarnMessage(request, message);
			return mapping.findForward(FORWARD_INFO);
		}
		//��������ɫ���������û�
		TblCmnUserRole userRole = new TblCmnUserRole();
		userRole.setTblCmnUser(bean);
		userRole.setTblCmnRole(baseRole);
		getService().saveOrUpdate(userRole);
		MessageUtils.addMessage(request, message);
		return mapping.findForward(FORWARD_INFO);
	}

	/**
	 * 
	 * �������� �޸��û���ϸ�󱣴淽��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:20:02
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		String message = "";
		if("systemUser".equals(bean.getId())){
			MessageUtils.addErrorMessage(request, "���û�Ϊ�����û������ɱ��޸ģ�");
		}else{
			getService().saveOrUpdate(bean);
			message = MessageInfo.factory().getMessage("SM_I003_R_0");
			MessageUtils.addMessage(request, message);
		}
		return mapping.findForward(FORWARD_INFO);
		//return info(mapping, form, request, response);
	}

	/**
	 * 
	 * �������� �û�����������Ϣ���񷽷�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-27 ����01:42:48
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward msgSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		TblCmnMsgConfig msg = theForm.getCmnMsgConfig();
		bean.getId();
		msg.setTblCmnUser(bean);
		String message = "";
		try {
			getService().add(msg);
			message = MessageInfo.factory().getMessage("SM_I003_R_0");
		} catch (DataIntegrityViolationException e) {
			message = MessageInfo.factory().getMessage("SM_I023_R_0");
		}
		MessageUtils.addMessage(request, message);
		return msg(mapping, form, request, response);
	}

	/**
	 * 
	 * �������� �û��޸���Ϣ�������÷���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-27 ����01:45:19
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward msgUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnMsgConfig msg = theForm.getCmnMsgConfig();
		getService().update(msg);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I003_R_0"));
		return msg(mapping, form, request, response);
	}

	/**
	 * 
	 * �������� �������÷���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:15:20
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward initPsd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		bean.setPassword(SecurityUtils.passwordHex(null));
		bean.setFailedTimes("0");
		getService().update(bean);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
				"SM_I024_R_0"));
		return info(mapping, form, request, response);
	}

	/**
	 * 
	 * �������� ɾ���û��б���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:15:34
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		String empCd = bean.getEmpCd();
		if("systemUser".equals(bean.getId())){
			MessageUtils.addErrorMessage(request,"���û�Ϊ�����û���������ɾ����");
		}else{
			getService().delete(bean);
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
					"SM_I025_R_1", empCd));
		}
		return list(mapping, form, request, response);
	}

	/**
	 * 
	 * �������� ɾ��Ȩ���б���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:16:22
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward removePermission(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		String oid1 = request.getParameter("oid1");
		TblCmnUser bean = theForm.getBean();
		String empCd = bean.getEmpCd();
		TblCmnUserPermission userPermission = (TblCmnUserPermission) getService()
				.load(TblCmnUserPermission.class, oid1);
		String name = userPermission.getTblCmnPermission().getDisplayname();
//		getService().deleteLogic(userPermission);
		getService().delete(userPermission);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I026_R_2", empCd, name));
		return queryPermissions(mapping, form, request, response);
	}
	
	
	/**
	 * 
	 * �������� ɾ��Ȩ���б���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:16:22
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward removeDataPermission(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		String oid1 = request.getParameter("oid1");
		TblCmnUser bean = theForm.getBean();
		String empCd = bean.getEmpCd();
		TblCmnUserMember userMember = (TblCmnUserMember) getService()
				.load(TblCmnUserMember.class, oid1);
		String name = userMember.getTblCmnEntityMember().getName();
		getService().delete(userMember);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I026_R_2", empCd, name));
		return dataPermission(mapping, form, request, response);
	}

	/**
	 * 
	 * �������� ɾ����ɫ�б���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-26 ����02:05:26
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward removeRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		String empCd = bean.getEmpCd();
		String oid1 = request.getParameter("oid1");
		TblCmnUserRole userRole = (TblCmnUserRole) getService().load(
				TblCmnUserRole.class, oid1);
		String name = userRole.getTblCmnRole().getName();
//		getService().deleteLogic(userRole);
		getService().delete(userRole);
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I027_R_2", empCd, name));
		return queryRole(mapping, form, request, response);
	}
	
	/**
	 * 
	 * �������� ����ɾ���б���Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:24:58
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		String oid = request.getParameter("oids");
		if(oid.indexOf("systemUser")!=-1){ 
			MessageUtils.addErrorMessage(request,"��ɾ����¼���г����û��������Խ�������ɾ����");
		}else
		{
		List ids=Tools.split(oid,"\\$");
		for(int i=0,size=ids.size();i<size;i++){
			theForm.setBean((TblCmnUser) getService().load(TblCmnUser.class, ids.get(i).toString()));
					getService().delete(theForm.getBean());
				}
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("HR_I043_R_0"));
		}
		return list(mapping, theForm, request, response);
	}

	/**
	 * 
	 * �������� ����ɾ��Ȩ����Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:24:58
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward deletePermissionsAll(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		String empCd = bean.getEmpCd();
		String oid1 = request.getParameter("oids");
		List ids = Tools.split(oid1, "\\$");
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I034_R_1", empCd));
		for (int i = 0, size = ids.size(); i < size; i++) {
			TblCmnUserPermission userPermission=(TblCmnUserPermission) getService()
					.load(TblCmnUserPermission.class, ids.get(i).toString());
			String permissionName = userPermission.getTblCmnPermission().getDisplayname();
			MessageUtils.addMessage(request, permissionName);
//			getService().deleteLogic(userPermission);
			getService().delete(userPermission);
		}
		return queryPermissions(mapping, form, request, response);
	}

	/**
	 * 
	 * �������� ����ɾ����ɫ��Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-26 ����02:10:47
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward deleteRoleAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		String empCd = bean.getEmpCd();
		String oid1 = request.getParameter("oids");
		List ids = Tools.split(oid1, "\\$");
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I035_R_1", empCd));
		for (int i = 0, size = ids.size(); i < size; i++) {
			TblCmnUserRole userRole = (TblCmnUserRole) getService().load(
					TblCmnUserRole.class, ids.get(i).toString());
			String roleName = userRole.getTblCmnRole().getName();
			MessageUtils.addMessage(request, roleName);
//			getService().deleteLogic(userRole);
			getService().delete(userRole);
		}
		return queryRole(mapping, form, request, response);
	}

	/**
	 * 
	 * �������� ��������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:25:11
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward batchjihuo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		String oids = request.getParameter("oids");
		List str = Tools.split(oids, "\\$");
		str = service().batchjihuo(str);
		if (str.size() > 0)
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
					"SM_I028_R_0"));
		return list(mapping, theForm, request, response);
	}

	/**
	 * 
	 * �������� ��������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:25:27
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward batchjinyong(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		String oids = request.getParameter("oids");
		List str = Tools.split(oids, "\\$");
		str = service().batchjinyong(str);
		if (str.size() > 0)
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
					"SM_I029_R_0"));
		return list(mapping, theForm, request, response);
	}

	/**
	 * 
	 * �������� �û�Ȩ����ϸҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-23 ����07:25:37
	 * @version 1.0
	 * @author baihe
	 */
    public ActionForward dataPermissionDetailed(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UserManageForm theForm = (UserManageForm) form;
        String oid2 = request.getParameter("oid2");
        theForm.setOid(oid2);
        theForm.setCmnUserMember((TblCmnUserMember) getService().load(
                TblCmnUserMember.class, oid2));
        return mapping.findForward("dataPerDetailed");
    }
	
	/**
	 * �������� ��ɫ������Ϣ����ҳ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward rolePerUserEntry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String rowId = request.getParameter("rowId");
		String selectPage = request.getParameter("selectPage");
		UserManageForm theForm = (UserManageForm) form;
		if(StringUtils.isNotBlank(selectPage)){
			TblCmnRole role = (TblCmnRole) getService().load(TblCmnRole.class, rowId);
			theForm.setSearchTag(role.getName());
			theForm.setRoleId(role.getId());
			return mapping.findForward(FORWARD_ROLEPERUSERENTRY);
		}else{
			TblCmnUserRole userRole = (TblCmnUserRole) getService().load(TblCmnUserRole.class, rowId);
			TblCmnRole role = userRole.getTblCmnRole();
			theForm.setSearchTag(role.getName());
			theForm.setRoleId(role.getId());
			return mapping.findForward(FORWARD_ROLEPERUSERENTRY);
		}
	}

	/**
	 * 
	 * �������� ��ý�ɫ��Ȩ�޹�����Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-26 ����05:31:59
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward achieveRolePermission(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String roleId = request.getParameter("roleId");
		UserManageForm theForm = (UserManageForm) form;
		TblCmnRole role = (TblCmnRole) getService().load(TblCmnRole.class, roleId);
		List list = null;
		if (null != role.getId()) {
			list = service().getRolePermissions(role);

		} else {
			list = new ArrayList(0);
		}
		theForm.setRolePermission(list);
		theForm.setRoleId(roleId);
		return mapping.findForward(FORWARD_ACHIEVEROLEPERMISSION);
	}

	/**
	 * 
	 * �������� ��ý�ɫ���û�������Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-26 ����05:31:59
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward achieveRoleUser(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String roleId = request.getParameter("roleId");
		UserManageForm theForm = (UserManageForm) form;
		TblCmnRole role = (TblCmnRole) getService().load(
				TblCmnRole.class, roleId);
		List list = null;
		if (null != role.getId()) {
			list = service().getRoleUser(role);
		} else {
			list = new ArrayList(0);
		}
		theForm.setRoleUser(list);
		theForm.setRoleId(roleId);
		return mapping.findForward(FORWARD_ACHIEVEROLEUSER);
	}

	/**
	 * 
	 * �������� �޸��û�Ȩ����ϸ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-12-5 ����08:44:32
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward dataPermissionSave(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        UserManageForm theForm = (UserManageForm) form;
        TblCmnUserMember mbean = theForm.getCmnUserMember();
        getService().update(mbean);
        MessageUtils.addMessage(request, MessageInfo.factory().getMessage(
        "HR_I003_R_0"));
        return mapping.findForward("dataPerDetailed");
    }

	/**
	 * 
	 * �������� �û����Ȩ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-12-6 ����07:18:01
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward selectPermission(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		String strs = request.getParameter("strs");
		List ids = Tools.split(strs, "\\$");
		for (int i = 3, size = ids.size(); i < size; i++) {
			//���ؽڵ�
			TblCmnPermission permission = (TblCmnPermission) getService()
			.load(TblCmnPermission.class, ids.get(i).toString());
			//�����Ŀ¼����Ҫ��Ŀ¼�µ�����Ȩ�޸����û�
			//�����Ȩ�ޣ���ô��Ȩ�޸����û�
			if("0".equals(permission.getNodetype()))//Ŀ¼
			{
				int count = service().addFolerPermissions(permission.getId(),
						bean,ids.get(0).toString(),ids.get(1).toString(),ids.get(2).toString());
				MessageUtils.addMessage(request, "Ŀ¼["+permission.getPermissionRoute()+"]�µ�"+count+"��Ȩ��ȫ�����룡");
			}
			else//Ȩ��
			{
				TblCmnUserPermission userPermission = new TblCmnUserPermission();
				userPermission.setTblCmnUserByUserId(bean);
				userPermission.setTblCmnPermission(permission);
				userPermission.setRwCtrl(ids.get(0).toString());
				userPermission.setReadAccessLevel(ids.get(1).toString());
				userPermission.setWriteAccessLevel(ids.get(2).toString());
				String permissionName = permission.getDisplayname();
				
				/*������Ȩ����Ҫ�������룬��Ӧ��Ĭ�ϼ��룬���ע�͵�һ�´���
				if("1".equals(permission.getNodetype()))//����Ȩ��
				{
					//�������е�������Ȩ��
					int count = service().addFiledPermissionsForFunPermission(userPermission);
					if(count>0)
						MessageUtils.addMessage(request, "����Ȩ��["+permission.getPermissionRoute()+"]��"+count+"��������Ȩ�޼���ɹ�!");
				}
				*/
				try {
					getService().add(userPermission);
					MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I057_R_1", permissionName));

				}catch (DataIntegrityViolationException e) {
					MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I056_R_1", permissionName));
				}

			}
		} 
		return queryPermissions(mapping, form, request, response);

	}

	/**
	 * 
	 * �������� �û���ӽ�ɫ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-12-6 ����07:46:08
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward selectRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		String strs = request.getParameter("strs");
		List ids = Tools.split(strs, "\\$");
		for (int i = 0, size = ids.size(); i < size; i++) {
				TblCmnUserRole userRole = new TblCmnUserRole();
				userRole.setTblCmnUser(bean);
				TblCmnRole role = (TblCmnRole) getService().load(
						TblCmnRole.class, ids.get(i).toString());
				userRole.setTblCmnRole(role);
				String roleName = role.getName();
			try {
				getService().add(userRole);
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I059_R_1", roleName));
			}catch (DataIntegrityViolationException e) {
				MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I058_R_1", roleName));
			}
		} 
		return queryRole(mapping, form, request, response);

	}

	/**
	 * 
	 * �������� �û�ѡ���б�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-27 ����03:53:58
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward selectInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		List list = new ArrayList(0);
		try {
			list = getService().getList(TblCmnUser.class);
		} catch (Exception e) {
			return null;
		}
		theForm.setSelectUser(list);
		return mapping.findForward(FORWARD_SELECTINFO);
	}
	
	
	public ActionForward selectData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm)form;		
		String strs = request.getParameter("strs");
		String userArr[] = strs.split(";");
		TblCmnUser user = (TblCmnUser) getService().load(TblCmnUser.class, theForm.getOid());
		for (int i = 0; i < userArr.length; i++){
				TblCmnUserMember member = (TblCmnUserMember) getService()
				.load(TblCmnUserMember.class, userArr[i]);
//			TblCmnUserMember member = new TblCmnUserMember();
//			TblCmnEntityMember entityMember=(TblCmnEntityMember) getService().load(TblCmnEntityMember.class, userArr[i]);
				member.setTblCmnUser(user);
//				member.setTblCmnEntityMember(entityMember);
				getService().saveOrUpdate(member);
		}
		return dataPermission(mapping, form, request, response);
	}
	
	/**
	 * 
	 * �������� �û�ѡ���б�(���ί���û�)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-27 ����03:53:58
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward selectInfoForConsign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		String currentUserId = SecurityContextInfo.getCurrentUser().getUserId();
		List list = new ArrayList(0);
		try {
			list = service().getUserListForConsign(currentUserId);
		} catch (Exception e) {
			return null;
		}
		theForm.setSelectUser(list);
		return mapping.findForward(FORWARD_SELECTINFOFORCONSIGN);
	}
	
	/**
	 * 
	 * �������� ���ݽ�ɫѡ���û�
	 *  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-27 ����03:53:58
	 * @version 1.0
	 * @author baihe
	 */
	/*public ActionForward selectCurDutyper(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String  className = request.getParameter("className");
		String roleIds = "";
		if("TblAmWoHotmachticket".equals(className)){
			TblAmWoHotmachticket bean = new TblAmWoHotmachticket();
			roleIds = BisWfServiceLocator.getWorkflowService().getRoles(bean.getWorkflowName(), "1");
		}
		if("TblAmWoElectrictype1ticket".equals(className)){
			TblAmWoElectrictype1ticket bean = new TblAmWoElectrictype1ticket();
			roleIds = BisWfServiceLocator.getWorkflowService().getRoles(bean.getWorkflowName(), "1");
		}
		
		UserManageForm theForm = (UserManageForm) form;
		List ids = Tools.split(roleIds, ",");
		List user = service().getUsersByRoleCds(ids);
		theForm.setUser(user);
		return mapping.findForward(FORWARD_SELECCURUSER);
	}*/
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward selectUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String roleIds = request.getParameter("roleIds");
		UserManageForm theForm = (UserManageForm) form;
		List ids = Tools.split(roleIds, "\\$");
		List user = service().getUsersByRolesWithAutoCondtionAndPaginater(ids);
		theForm.setUser(user);
		return mapping.findForward(FORWARD_SELECTUSER);
	}
	
	/**
	 * 
	 * �������� ���ݽ�ɫ������ѡ���û�
	 *  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-11-27 ����03:53:58
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward selectUserIds(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String roleIds = request.getParameter("roleIds");
		String deptId = request.getParameter("deptId");
		UserManageForm theForm = (UserManageForm) form;
		List ids = Tools.split(roleIds, "\\$");
		List user = service().getUsersByRolesWithAutoCondtionAndPaginater(ids,deptId);
		theForm.setUser(user);
		return mapping.findForward(FORWARD_SELECTUSER);
	}

	/**
	 * 
	 * �������� ���������޸�ҳ��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-12-6 ����02:04:01
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward password(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CurrentUser ucn=SecurityContextInfo.getCurrentUser();
		UserManageForm theForm = (UserManageForm) form;
		theForm.setOid(ucn.getUserId());
		theForm.setUserName(ucn.getUserName());
		theForm.setEmpId(ucn.getEmpCd());
		TblCmnUser bean = theForm.getBean();
		return mapping.findForward(FORWARD_PASSWORD);
	}

	/**
	 * 
	 * �������� �����޸�����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             2007-12-6 ����03:51:12
	 * @version 1.0
	 * @author baihe
	 */
	public ActionForward updatePassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		CurrentUser ucn=SecurityContextInfo.getCurrentUser();
		UserManageForm theForm = (UserManageForm) form;
		TblCmnUser bean = theForm.getBean();
		theForm.setUserName(ucn.getUserName());
		theForm.setEmpId(ucn.getEmpCd());
		if (SecurityUtils.passwordHex(theForm.getOldPassword()).equals(
				bean.getPassword())) {
			bean.setPassword(SecurityUtils.passwordHex(theForm.getPassword()));
			getService().update(bean);
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I030_R_0"));
			return mapping.findForward(FORWARD_PASSWORD);

		} else {
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I031_R_0"));
			return mapping.findForward(FORWARD_PASSWORD);
		}
	}

	/**
	 * �������� ���ù��ýӿ�
	 */
	private CommonService getService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}
	
	
	public ActionForward selectDataEntry(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {

		return mapping.findForward("selectDataEntry");
	}
	
	
	public ActionForward selectDataInfo(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		
		UserManageForm theForm=(UserManageForm)form;
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
