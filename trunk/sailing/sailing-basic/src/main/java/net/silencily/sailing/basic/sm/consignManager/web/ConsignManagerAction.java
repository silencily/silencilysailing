package net.silencily.sailing.basic.sm.consignManager.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUserRole;
import net.silencily.sailing.basic.sm.permission.service.PermissionService;
import net.silencily.sailing.basic.sm.role.service.RoleService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.dbtime.DBTime;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.dao.DataIntegrityViolationException;

public class ConsignManagerAction extends DispatchActionPlus{
	
	
	/**
	 * �ɹ�����
	 */
	private static int countOk = 0;
	
	/**
	 * ʧ�ܼ���
	 */
	private static int countNg = 0;

	/**��ͬ  */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}

	/**
	 * 
	 * �������� ���ýӿڷ���
	 * @return
	 * 2007-11-23 ����07:13:11
	 * @version 1.0
	 * @author baihe
	 */
	private  static PermissionService perMissionService() {
		return (PermissionService) ServiceProvider
				.getService(PermissionService.SERVICE_NAME);
	}

	/**
	 * 
	 * �������� ���ýӿڷ���
	 * @return
	 * 2007-11-23 ����07:13:11
	 * @version 1.0
	 * @author baihe
	 */
	private  static RoleService roleService() {
		return (RoleService) ServiceProvider
				.getService(RoleService.SERVICE_NAME);
	}
	/**
	 * 
	 * �������� ɾ��ί�н�ɫ
	 * @param userInfo
	 * @param strId
	 * @param countOk
	 * @param countNg
	 * 2007-11-28 ����02:55:16
	 * @version 1.0
	 * @author wanghy
	 */
	private String delCommonRole(TblCmnUser userInfo,String strId){
		
		TblCmnUser tblCmnUserBean = (TblCmnUser)getService().load(TblCmnUser.class, strId);
		List userRoleLst = roleService().getUserRoleConsignedUser(userInfo, tblCmnUserBean);
		String strName = "";
		//ɾ���û���ί�еĽ�ɫ
		for(Iterator iter = userRoleLst.iterator();iter.hasNext();){
			
			TblCmnUserRole ur = (TblCmnUserRole)iter.next();

				//ί�н�ɫ�߼�ɾ����
				try{
					strName = tblCmnUserBean.getEmpName();
					ur.setTblCmnUser(ur.getTblCmnUserByConsignerId());
					ur.setTblCmnUserByConsignerId(null);
				
					getService().update(ur);
					countOk++;
				} catch (Exception ex){
					ex.printStackTrace();
					countNg++;
				}
			}
		return strName;
	}
	
	/**
	 * 
	 * �������� ɾ��ί��Ȩ��
	 * @param userInfo
	 * @param strId
	 * @param countOk
	 * @param countNg
	 * @return
	 * 2007-11-28 ����03:08:38
	 * @version 1.0
	 * @author wanghy
	 */
	private String delCommonPermission(TblCmnUser userInfo,String strId){	
		
		//ί�н�ɫȡ��
		TblCmnUser tblPermissionUserBean = (TblCmnUser)getService().load(TblCmnUser.class, strId);
		List userRoleLst = perMissionService().getUserPermissionConsignedUser(userInfo, tblPermissionUserBean);
		String strName = "";
		//ɾ���û���ί��Ȩ��
		for(Iterator iter = userRoleLst.iterator();iter.hasNext();){
			TblCmnUserPermission perm = (TblCmnUserPermission)iter.next();
				//ί�н�ɫ�߼�ɾ����
				try{
					strName = tblPermissionUserBean.getEmpName();
					perm.setTblCmnUserByUserId(perm.getTblCmnUserByConsignerId());
					perm.setTblCmnUserByConsignerId(null);
					getService().update(perm);
					countOk++;
				} catch (Exception ex){
					ex.printStackTrace();
					countNg++;
				}
		}
		return strName;
	}

	
	public ActionForward entry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		return mapping.findForward("entry");
	}
	
	/**
	 * 
	 * �������� ί���û���ʾ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-26 ����05:31:05
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		ConsignManagerForm theForm = (ConsignManagerForm)form;
		String userId = SecurityContextInfo.getCurrentUser().getUserId();
		
		Set tempSet = new HashSet();
		Set setPermission = new HashSet();
		Set setRole = new HashSet();
		TblCmnUser userInfo = (TblCmnUser)getService().load(TblCmnUser.class, userId);
		//Ȩ����Ϣ
		setPermission = userInfo.getConsigneUserPermissions();
		
		//��ɫ��Ϣ
		setRole = userInfo.getConsigneUserRoles();
		//��������
		//�û�����ί�н�ɫ
		for(Iterator iter = setRole.iterator(); iter.hasNext();) {
			TblCmnUserRole tblCmnUserRoleBean = (TblCmnUserRole)iter.next();
			TblCmnUser considerRoleBean = tblCmnUserRoleBean.getTblCmnUser();
			if ( null != considerRoleBean && "0".equals(tblCmnUserRoleBean.getDelFlg())){
					//ί���û���������.
					tempSet.add(considerRoleBean);
					
				}
		}
		
//		//�û�����ί��Ȩ��
//		for(Iterator iter = setPermission.iterator(); iter.hasNext();) {
//			TblCmnUserPermission tblCmnUserPermissionBean = (TblCmnUserPermission)iter.next();
//			TblCmnUser tblPermissUserBean = tblCmnUserPermissionBean.getTblCmnUserByUserId();
//			if ( null != tblPermissUserBean && "0".equals(tblCmnUserPermissionBean.getDelFlg())){
//					tempSet.add(tblPermissUserBean);
//				}
//		}
//		
		//ת��Ϊlist
		List tmpList = new ArrayList();
		for(Iterator iter = tempSet.iterator(); iter.hasNext();){
			TblCmnUser tblUserBean = (TblCmnUser)iter.next();
			tmpList.add(tblUserBean);
		}
		
		String arrOid = request.getParameter("oid2");
		String[] arr = {};
		if (null != arrOid && !"".equals(arrOid)){
			arr = arrOid.split(";");
			for (int i = 0 ; i < arr.length; i++){
				TblCmnUser addUser = (TblCmnUser)getService().load(TblCmnUser.class, arr[i]);
				tmpList.add(addUser);
			}
		}
		
		theForm.setList(tmpList);
		theForm.setUserBean(userInfo);
		theForm.setOid(userId);
		
		return mapping.findForward("list");
	}
	
	/**
	 * 
	 * �������� ί���û���ί��Ȩ��ȡ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-26 ����05:32:02
	 * @version 1.0
	 * @author wanghy
	 */
	public  ActionForward consignPermissionList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ConsignManagerForm theForm = (ConsignManagerForm)form;
		String OidCollection = theForm.getOid();
		String strOid[] = OidCollection.split(";");
		TblCmnUser userInfoConsigner = null;
		TblCmnUser userInfo = null;
		List permissionLst = new ArrayList();
		if (strOid.length > 1){
			
			//ί��Ȩ��ȡ��
			userInfoConsigner = (TblCmnUser)getService().load(TblCmnUser.class, strOid[1]);
			userInfo = (TblCmnUser)getService().load(TblCmnUser.class, strOid[0]);
			permissionLst = perMissionService().getUserPermissionConsignedUser(userInfoConsigner, userInfo);
			theForm.setList(permissionLst);
			theForm.setOid(OidCollection);			
		}else{
			userInfoConsigner = (TblCmnUser)getService().load(TblCmnUser.class, OidCollection);
		}
		theForm.setUserBean(userInfoConsigner);
		
		return mapping.findForward("consignPermissionList");
	}

	/**
	 * 
	 * �������� ί���û���ί�н�ɫ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-26 ����05:32:07
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward consignRoleList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)  {
		
		ConsignManagerForm theForm = (ConsignManagerForm)form;
		String consignId = theForm.getOid();
		String userId = SecurityContextInfo.getCurrentUser().getUserId();
		TblCmnUser userInfoConsigner = null;
		TblCmnUser userInfo = null;
		List permissionLst = new ArrayList();
		if (StringUtils.isNotBlank(consignId)){
			//ί�н�ɫȡ��
			userInfoConsigner = (TblCmnUser)getService().load(TblCmnUser.class, userId);
			userInfo = (TblCmnUser)getService().load(TblCmnUser.class, consignId);
			permissionLst = roleService().getUserRoleConsignedUser(userInfoConsigner, userInfo);
			theForm.setList(permissionLst);
			theForm.setOid(consignId);		
		}else {
			userInfoConsigner = (TblCmnUser)getService().load(TblCmnUser.class, userId);
		}
		theForm.setUserBean(userInfoConsigner);		
		return mapping.findForward("consignRoleList");
	}

	/**
	 * 
	 * �������� ɾ��ѡ����û���Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-28 ����02:09:26
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward deleteUsersAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ConsignManagerForm theForm = (ConsignManagerForm)form;		
		String oid1 = request.getParameter("oid1");
		List ids=Tools.split(oid1,"\\$");
		int len = ids.size();
		TblCmnUser userInfo = null;
		
		//String OidCollection = theForm.getOid();
		String userId = SecurityContextInfo.getCurrentUser().getUserId();
		
	//	String strOid[] = OidCollection.split(";");
		
//		if(strOid.length == 1){
//			//�û���Ϣȡ��
//			userInfo = (TblCmnUser)getService().load(TblCmnUser.class, OidCollection);
//			
//		}else {
//			//�û���Ϣȡ��
			userInfo = (TblCmnUser)getService().load(TblCmnUser.class, userId);
			
//		}
		//ɾ��ί���˵�����
		String empName = "";
		if (ids.size() == 1){
			String roleName = "";
			String permissionName = "";
			//ɾ����ɫ����
			roleName = delCommonRole(userInfo, oid1);
			
			//ɾ��ί��Ȩ��
			//permissionName = delCommonPermission(userInfo, oid1);
			if (!"".equals(roleName)) {
				empName = roleName;
			} else {
				empName = permissionName;
			} 
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I054_R_3", empName, String.valueOf(countOk), String.valueOf(countNg)));		
			countOk = 0;
			countNg = 0;
		}else{
			for(int i=0; i<len; i++){
				String roleName = "";
				String permissionName = "";
				//��ί����ID
				String strId = ids.get(i).toString();
				
				//ɾ����ɫ����
				roleName = delCommonRole(userInfo, strId);
				//ɾ��ί��Ȩ��
				permissionName = delCommonPermission(userInfo, strId);
				
				if (!"".equals(roleName)) {
					empName = roleName;
				} else {
					empName = permissionName;
				} 
				
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I054_R_3", empName, String.valueOf(countOk), String.valueOf(countNg)));		
				
				countOk = 0;
				countNg = 0;
			}
		}
		return list(mapping, form, request, response);
		}
	
	/**
	 * 
	 * �������� ɾ��ί�н�ɫ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-28 ����03:25:36
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward deleteRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String oid1 = request.getParameter("oid1");
		List ids=Tools.split(oid1,"\\$");
		int len = ids.size();
		int countOk = 0;
		int countNg = 0;
		TblCmnUserRole usrRoleBean = null;
		String empName = "";
		if (ids.size() == 1){
			
			//ɾ������
			try {
				usrRoleBean = (TblCmnUserRole)getService().load(TblCmnUserRole.class, oid1);
				empName = usrRoleBean.getTblCmnUser().getEmpName();

				//���󻥻�
				usrRoleBean.setTblCmnUser(usrRoleBean.getTblCmnUserByConsignerId());
				usrRoleBean.setTblCmnUserByConsignerId(null);
				getService().update(usrRoleBean);
				countOk++;
			} catch(Exception ex) {
				ex.printStackTrace();
				countNg++;
			}
		} else {
			for (int i=0; i<len; i++){
				
				//ɾ������				
				String strTemp = ids.get(i).toString();

				try {
					usrRoleBean = (TblCmnUserRole)getService().load(TblCmnUserRole.class, strTemp);
					empName = usrRoleBean.getTblCmnUser().getEmpName();
					//���󻥻�
					usrRoleBean.setTblCmnUser(usrRoleBean.getTblCmnUserByConsignerId());
					usrRoleBean.setTblCmnUserByConsignerId(null);
					getService().update(usrRoleBean);
					countOk++;
				} catch(Exception ex) {
					ex.printStackTrace();
					countNg++;
				}
			}
		}
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I054_R_3", empName, String.valueOf(countOk), String.valueOf(countNg)));		
		countOk = 0;
		countNg = 0;
		return consignRoleList(mapping, form, request, response);
	}

	public ActionForward deletePermission(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String oid1 = request.getParameter("oid1");
		List ids=Tools.split(oid1,"\\$");
		int len = ids.size();
		int countOk = 0;
		int countNg = 0;
		TblCmnUserPermission usrRoleBean = null;
		String empName = "";
		if (ids.size() == 1){
			
			//ɾ������
			try {
				usrRoleBean = (TblCmnUserPermission)getService().load(TblCmnUserPermission.class, oid1);
				empName = usrRoleBean.getTblCmnUserByUserId().getEmpName();
				//���󻥻�
				usrRoleBean.setTblCmnUserByUserId(usrRoleBean.getTblCmnUserByConsignerId());
				usrRoleBean.setTblCmnUserByConsignerId(null);
				getService().update(usrRoleBean);
				countOk++;
			} catch(Exception ex) {
				ex.printStackTrace();
				countNg++;
			}
		} else {
			for (int i=0; i<len; i++){
				
				//ɾ������				
				String strTemp = ids.get(i).toString();

				try {
					usrRoleBean = (TblCmnUserPermission)getService().load(TblCmnUserPermission.class, strTemp);
					empName = usrRoleBean.getTblCmnUserByUserId().getEmpName();
					//���󻥻�
					usrRoleBean.setTblCmnUserByUserId(usrRoleBean.getTblCmnUserByConsignerId());
					usrRoleBean.setTblCmnUserByConsignerId(null);
					getService().update(usrRoleBean);
					countOk++;
				} catch(Exception ex) {
					ex.printStackTrace();
					countNg++;
				}
			}
		}
		
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I054_R_3", empName, String.valueOf(countOk), String.valueOf(countNg)));		
		countOk = 0;
		countNg = 0;
		return consignPermissionList(mapping, form, request, response);
	}
	
	/**
	 * 
	 * �������� �޸�ί��Ȩ������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * 2007-11-29 ����11:34:29
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward permissionUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		ConsignManagerForm theForm = (ConsignManagerForm)form;
		String strOid = request.getParameter("oid2");
		theForm.setOid(strOid);
		TblCmnUserPermission userPermissionBean = (TblCmnUserPermission)getService().load(TblCmnUserPermission.class, strOid);
		theForm.setUserPermissionBean(userPermissionBean);
		return mapping.findForward("permissionUpdate");
	}
	
	/**
	 * 
	 * �������� Ȩ�����ͱ���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-23 ����06:39:50
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward permissionSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ConsignManagerForm theForm =(ConsignManagerForm)form;

		TblCmnUserPermission rolePermissionBean = theForm.getUserPermissionBean();
		try {
			getService().update(rolePermissionBean);
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I048_R_0"));
		}catch(Exception ex ){
			ex.printStackTrace();
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I049_R_0"));
		}
		theForm.setUserPermissionBean(rolePermissionBean);
		//return permissionShow(mapping, form, request, response);
		return null;
		}
	
	/**
	 * 
	 * �������� �޸�ί��Ȩ������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * 2007-11-29 ����11:34:29
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward roleUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		ConsignManagerForm theForm = (ConsignManagerForm)form;
		String strOid = request.getParameter("oid2");
		theForm.setOid(strOid);
		TblCmnUserRole userRoleBean = (TblCmnUserRole)getService().load(TblCmnUserRole.class, strOid);
		theForm.setUserRoleBean(userRoleBean);
		return mapping.findForward("roleUpdate");
	}
	
	/**
	 * 
	 * �������� Ȩ�����ͱ���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-23 ����06:39:50
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward roleSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ConsignManagerForm theForm =(ConsignManagerForm)form;
		TblCmnUserRole userRole = theForm.getUserRoleBean();
		try {
			getService().update(userRole);
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I048_R_0"));
		}catch(Exception ex ){
			ex.printStackTrace();
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I049_R_0"));
		}
		theForm.setUserRoleBean(userRole);
		return null;
		}
	
	public ActionForward addpermission(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
//		ConsignManagerForm theForm = (ConsignManagerForm)form;
//		//�û�id��ί���û�Ȩ��id
//		String OidCollection = theForm.getOid();
//		String strOid[] = OidCollection.split(";");
//		//ί���û���oid
//		String StrConsignId = strOid[0];
//		//���Ȩ�޵�oid����
//		String permissionOid = request.getParameter("oid1");
//		String permissionArr[] = permissionOid.split(";");
//		
//		if (strOid.length > 1) {
//			
//		}else {
//			
//		}
//		
//		TblCmnUser userInfoConsigner = null;
//		TblCmnUser userInfo = null;
//		List permissionLst = new ArrayList();
		
		return consignPermissionList(mapping, form, request, response);
	}

	/**
	 * 
	 * �������� ��ӽ�ɫ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-6 ����04:44:43
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward addRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		ConsignManagerForm theForm = (ConsignManagerForm)form;
		//�û�id��ί���û�Ȩ��id
		//String OidCollection = theForm.getOid();
		//String strOid[] = OidCollection.split(";");
		//��ί���û���oid
		String StrByConsignerId = theForm.getOid();
		//ί���û���oid
		String StrConsignerId = SecurityContextInfo.getCurrentUser().getUserId();
		
		//���ί���û��Ľ�ɫ��oid����
		String roleOid = request.getParameter("oid1");
		String roleArr[] = roleOid.split(";");
		int countNg = 0 ;
		int countOk = 0 ;
		//ί���߶���
		TblCmnUser tblCmnUser = (TblCmnUser)getService().load(TblCmnUser.class, StrConsignerId);
		//��ί���߶���
		TblCmnUser tblCmnUserByConsigner = (TblCmnUser)getService().load(TblCmnUser.class, StrByConsignerId);
//		//��ȡ��ǰʱ��
//		Calendar cal = Calendar.getInstance();
//		Date currentTime = cal.getTime();
//		//��ȡ30����ʱ��
//		cal.add(cal.DATE, 30);
//		Date validateTime = cal.getTime();
		int timeCount = roleArr.length;
		Date startTime = DBTime.getDBTime();
		Date endTime = DBTime.getDBTime();
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try{
			 startTime = df.parse(roleArr[timeCount-2]);
			 endTime = df.parse(roleArr[timeCount-1]);
		}catch(Exception ex){
			 ex.printStackTrace();
		}
		
		for (int i = 0; i < timeCount-2; i++) {
			TblCmnRole tblCmnRole = (TblCmnRole)getService().load(TblCmnRole.class, roleArr[i]);
			if("CMN_NORMAL_USER".equals(tblCmnRole.getRoleCd())){
				MessageUtils.addWarnMessage(request, "������ɫ�����Ա�ί��");
				continue;
			}
			if("systemRole".equals(tblCmnRole.getRoleCd())){
				MessageUtils.addWarnMessage(request, "������ɫ�����Ա�ί��");
				continue;
			}
			List userRoleList = roleService().getUserRole(tblCmnUser, tblCmnRole);
			for (Iterator iter = userRoleList.iterator();iter.hasNext();) {
				TblCmnUserRole userRoleBean = (TblCmnUserRole)iter.next();
				userRoleBean.setTblCmnUser(tblCmnUserByConsigner);
				userRoleBean.setTblCmnUserByConsignerId(tblCmnUser);
				userRoleBean.setBeginTime(startTime);
				userRoleBean.setInvalidTime(endTime);
				try{
					getService().update(userRoleBean);
					countOk ++;
				}catch(DataIntegrityViolationException ex) {
					countNg ++;
				}
				//һ����Լ�ж�
				List testList = roleService().getUserRole(tblCmnUserByConsigner, tblCmnRole);
				if (testList.size() > 1) {
					getService().delete(userRoleBean);	
				}
			}
		}
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I055_R_2",String.valueOf(countNg),String.valueOf(countOk)));		
		return consignRoleList(mapping, form, request, response);
	}
	

}
