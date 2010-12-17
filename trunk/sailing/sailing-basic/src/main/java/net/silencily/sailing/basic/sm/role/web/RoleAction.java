package net.silencily.sailing.basic.sm.role.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnRoleOrg;
import net.silencily.sailing.basic.sm.domain.TblCmnRolePermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserRole;
import net.silencily.sailing.basic.sm.role.service.RoleService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.domain.tree.FlatTreeUtils;
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


public class RoleAction extends DispatchActionPlus {
	

	/**
	 * 
	 * �������� ��ͨ��service
	 * @return
	 * 2007-12-7 ����01:03:55
	 * @version 1.0
	 * @author wanghy
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}

	

	/**
	 *  
	 * �������� ��ͨ��ɫĿ¼ȡ��.
	 * @param parentCode
	 * @param theForm
	 * @throws Exception
	 * 2007-12-7 ����01:04:56
	 * @version 1.0
	 * @author wanghy
	 */
	private void commonGetMenuRole(String parentCode, RoleForm theForm) throws Exception {

		List roleli = new ArrayList();
		List roleList = new ArrayList();
		//��ɫȡ��
		roleli = service().getChildren(parentCode,theForm);
		for(Iterator iter= roleli.iterator();iter.hasNext();){
			Object temp = iter.next();
			if (null != temp && temp instanceof TblCmnRole ) {
				TblCmnRole roleTemp = (TblCmnRole)temp;
				if ("0".equals(roleTemp.getDelFlg())) {
					roleList.add(roleTemp);
				}
			}
		}
		theForm.setRolList(roleList);
/*		
		//��֯���ֹ���
		List orgli = new ArrayList();
		List orgList = new ArrayList();

		//��֯�ṹȡ��
		orgli = service().getOrgMenu(parentCode,theForm);
		
		for(Iterator iter= orgli.iterator();iter.hasNext();){
			Object temp = iter.next();
			if (null != temp && temp instanceof TblCmnRoleOrg ) {
				TblCmnRoleOrg orgTemp = (TblCmnRoleOrg)temp;
				if ("0".equals(orgTemp.getDelFlg())){
					orgList.add(orgTemp);
				}
			}
		}
		theForm.setOrgList(orgList);
//		theForm.getPaginater().setCount(orgList.size()+roleList.size());
 */
	}
	
	
	/**
	 * 
	 * �������� ��ɫ��service
	 * @return
	 * 2007-12-7 ����01:03:46
	 * @version 1.0
	 * @author wanghy
	 */
	public static RoleService service() {
		return (RoleService) ServiceProvider.getService(RoleService.SERVICE_NAME);
	}
	
	/**
	 * 
	 * ��������  ��ͬ��ȡ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-30 ����02:48:00
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward tree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		RoleForm theForm = (RoleForm)form;
		String parentCode = theForm.getParentCode();
		
		if (parentCode==null || StringUtils.isBlank(parentCode)) {
			parentCode = service().getInitRoot().getId();
		}
		List newList = new ArrayList();
		List list  = null;
//		String treeFlg = request.getParameter("treeFlg");
//		if (("0").equals(treeFlg)){
//			//��ɫ����֯�ṹȡ��
//			list = service().getChildren(parentCode,theForm);
//			for(Iterator iter= list.iterator();iter.hasNext();){
//				Object temp = iter.next();
//				if (null != temp) {
//					if(temp instanceof TblCmnRoleOrg ){
//						TblCmnRoleOrg orgTemp = (TblCmnRoleOrg)temp;
//						orgTemp.setTableFlg("0");
//					}
//					newList.add(temp);
//				}
//			}			
//		} else if ("1".equals(treeFlg)) {
			
			//��֯�ṹȡ��
			list = service().getOrgMenu(parentCode,theForm);
			
			String tf = request.getParameter("treeFlg");
			if(StringUtils.isBlank(tf))
				tf = "0";
			for(Iterator iter= list.iterator();iter.hasNext();){
				Object temp = iter.next();
				if (null != temp && temp instanceof TblCmnRoleOrg) {
					TblCmnRoleOrg orgTemp = (TblCmnRoleOrg)temp;
					if ("0".equals(orgTemp.getDelFlg())){
						orgTemp.setTableFlg(tf);
						newList.add(temp);
					}
				}
			}			
//		}
		response.setContentType("text/plain; charset=GBK");
		response.getWriter().print(FlatTreeUtils.serialize(newList, false));
		return null;
	}
	
	/**
	 * 
	 * �������� ��ɫ�б��ʾ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:08:43
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RoleForm theForm =(RoleForm)form;
		String parentCode = request.getParameter("parentCode");
		String flg = request.getParameter("flg");
		if (parentCode==null || StringUtils.isBlank(parentCode)) {
			parentCode = service().getInitRoot().getId();
		}
		parentCode = parentCode.split(";")[0];
		TblCmnRoleOrg roleOrg =(TblCmnRoleOrg)getService().load(TblCmnRoleOrg.class, parentCode);
		theForm.setFatherName(roleOrg.getDisplayName());
		theForm.setTypevalue("1");
		if("org".equals(flg)){
			List orgli = new ArrayList();
			List orgList = new ArrayList();
			orgli = service().getOrgMenu(parentCode,theForm);
			for(Iterator iter= orgli.iterator();iter.hasNext();){
				Object temp = iter.next();
				if (null != temp && temp instanceof TblCmnRoleOrg ) {
					TblCmnRoleOrg orgTemp = (TblCmnRoleOrg)temp;
					if ("0".equals(orgTemp.getDelFlg())){
						orgList.add(orgTemp);
					}
				}
			}
			theForm.setOrgList(orgList);
			return mapping.findForward("directoryList");
		}else{
			List roleli = new ArrayList();
			List roleList = new ArrayList();
			roleli = service().getChildren(parentCode,theForm);
			for(Iterator iter= roleli.iterator();iter.hasNext();){
				Object temp = iter.next();
				if (null != temp && temp instanceof TblCmnRole ) {
					TblCmnRole roleTemp = (TblCmnRole)temp;
					if ("0".equals(roleTemp.getDelFlg())) {
						roleList.add(roleTemp);
					}
				}
			}
			theForm.setRolList(roleList);
			return mapping.findForward("list");
		}
	}
	
	/**
	 * 
	 * �������� ��ɫѡ���б�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:08:43
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selectRoleInfoList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RoleForm theForm =(RoleForm)form;
		String parentCode = request.getParameter("parentCode");
		
		if (parentCode==null || StringUtils.isBlank(parentCode)) {
			parentCode = service().getInitRoot().getId();
		}

		commonGetMenuRole(parentCode,theForm);
		return mapping.findForward("selectRoleInfoList");
	}
	
	/**
	 * 
	 * �������� ��ɫ�༭����.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:00:49
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward roleSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm  roleForm = (RoleForm)form;
		//String formParentCodeArr[] = roleForm.getParentCode().split(";");
		//TblCmnRoleOrg father = (TblCmnRoleOrg)getService().load(TblCmnRoleOrg.class, formParentCodeArr[0]);
		//roleForm.setFatherName(father.getDisplayName());
		TblCmnRole children = roleForm.getRoleBean();
		//children.setFather(father);		
		if ("c".equals(roleForm.getStrCreate())){
			//�½�һ����ɫ����
			try {
				getService().add(children);
				roleForm.setOid(children.getId());
				roleForm.setStrCreate("");
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I038_R_0"));
			}catch(DataIntegrityViolationException e){
				MessageUtils.addErrorMessage(request, "��ɫ��ʶ�Ѵ��ڣ�����������");
			}
		}else {
			//TblCmnRole tblCmnBean = roleForm.getRoleBean();
			try {
				getService().update(children);
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I040_R_0"));
			}catch(Exception ex){
				ex.printStackTrace();
				MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I041_R_0"));
			}
		}
		return mapping.findForward("detail");
	}
	
	/**
	 * 
	 * �������� ��ɫĿ¼�༭����.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:01:01
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward roleMenuSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm  roleForm = (RoleForm)form;
		String strMessage = "";
		TblCmnRoleOrg children = roleForm.getOrgBean();
		String fatherid = request.getParameter("temp.orgBean.father.id");
		try{
			strMessage = service().saveOrg(children,roleForm,fatherid);
			MessageUtils.addMessage(request, strMessage);
		}catch(Exception ex){
			strMessage = ex.getMessage();
			MessageUtils.addErrorMessage(request, strMessage);	
		}
		return mapping.findForward("detail");
	}
	
	/**
	 * 
	 * �������� ɾ����ɫ��Ŀ¼.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:01:14
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward delMethod(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		RoleForm  roleForm = (RoleForm)form;
		
		String strOid = request.getParameter("oid");
		String strFlg = request.getParameter("strFlg");
		String strMessage = "";
		
		if ("2".equals(strFlg)){
			//��ɫɾ������
			if ("".equals(strOid) || "root".equals(strOid)){
				MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I042_R_0"));
				
			} else {
				try {
					List roleUserLst = null;
					List rolePermissionLst = null;
					TblCmnRole roleBean = (TblCmnRole)getService().load(TblCmnRole.class, strOid);
					if("1".equals(roleBean.getSystemRole())){
						MessageUtils.addWarnMessage(request, "ϵͳ��ɫ����ɾ����");
						return roleSearch(mapping, form,request, response);
					}
					if (!roleBean.getTblCmnUserRoles().isEmpty() || !roleBean.getTblCmnRolePermissions().isEmpty()){
						MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I043_R_0"));
					} else {
						getService().delete(roleBean);
						MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I044_R_0"));
					}
				}catch(Exception ex ) {
					ex.printStackTrace();
					MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I043_R_0"));					
				}
			}
		} else {
			if ("".equals(roleForm.getOid()) || "root".equals(roleForm.getOid())){
				MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I042_R_0"));
			} else {
				try {
					TblCmnRoleOrg orgBean = (TblCmnRoleOrg)getService().load(TblCmnRoleOrg.class, strOid);
					Set tblCmnRoleLst = orgBean.getTblCmnRoles();
					Set tblOrgBeanLst  = orgBean.getChildren();
					if ((null != tblCmnRoleLst && 0 != tblCmnRoleLst.size())
							|| (null != tblOrgBeanLst && 0 != tblOrgBeanLst.size())){
						MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I043_R_0"));
					} else {
						String parentCode = request.getParameter("parentCode");
						getService().delete(orgBean);
						MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I044_R_0"));
						List orgli = new ArrayList();
						List orgList = new ArrayList();
						orgli = service().getOrgMenu(parentCode,roleForm);
						for(Iterator iter= orgli.iterator();iter.hasNext();){
							Object temp = iter.next();
							if (null != temp && temp instanceof TblCmnRoleOrg ) {
								TblCmnRoleOrg orgTemp = (TblCmnRoleOrg)temp;
								if ("0".equals(orgTemp.getDelFlg())){
									orgList.add(orgTemp);
								}
							}
						}
						roleForm.setOrgList(orgList);
					}
				} catch(Exception ex ) {
					ex.printStackTrace();
					MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I045_R_0"));
				} 
			}
			return roleOrgSearch(mapping, form,request, response);
		}
		return roleSearch(mapping, form,request, response);
		//���¼���
//		String parentCode = request.getParameter("parentCode");
//		commonGetMenuRole(parentCode,roleForm);
//		return mapping.findForward("list");
	
	}

	

	/**
	 * 
	 * �������� ��ɫӵ�е�Ȩ�ޱ�ʾ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:01:24
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward permissionShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		RoleForm  roleForm = (RoleForm)form;
		if(StringUtils.isBlank(roleForm.getOid()) || StringUtils.equals(";role", roleForm.getOid())){
			return mapping.findForward("error");
		}
		String strOid = roleForm.getOid();
		String strArr[] = strOid.split(";");
		if( strArr.length>1 && strArr[1].equals("org")){
			return mapping.findForward("error");
		}
		String orgFlg ="";
		if (strArr.length > 1) {
			strOid = strArr[0];
			roleForm.setOid(strOid);
			orgFlg = strArr[1];
			roleForm.setOrgRoleFlg(orgFlg);
		}


		if ((!"org".equals(orgFlg) && !"".equals(orgFlg)) ||!"".equals(strOid)) {
			if (!"root".equals(strOid)){
				
				TblCmnRole roleBean = roleForm.getRoleBean();
				List list = service().list(roleBean,"permission");
				roleForm.setRoleBean(roleBean);
				roleForm.setList(list);
			} else {
				TblCmnRole roleBean = roleForm.getRoleBean();
				roleForm.setRoleBean(roleBean);
			}
		}
		return mapping.findForward("permissionShow");
		
	}
	
	/**
	 * 
	 * �������� ��ɫӵ�е��û���ʾ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:01:31
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward userShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		RoleForm  roleForm = (RoleForm)form;
		if(StringUtils.isBlank(roleForm.getOid()) || StringUtils.equals(";role", roleForm.getOid())){
			return mapping.findForward("error");
		}
		String strOid = roleForm.getOid();
		String strArr[] = strOid.split(";");
		if( strArr.length>1 && strArr[1].equals("org")){
			return mapping.findForward("error");
		}
		String orgFlg ="";
		if (strArr.length > 1) {
			strOid = strArr[0];
			roleForm.setOid(strOid);
			orgFlg = strArr[1];
			roleForm.setOrgRoleFlg(orgFlg);
		}else {
			;
		}
		
		if ((!"org".equals(orgFlg) && !"".equals(orgFlg)) ||!"".equals(strOid)) {
			if (!"root".equals(strOid)){
				TblCmnRole roleBean = roleForm.getRoleBean();
				List list = service().list(roleBean,"user");
				roleForm.setRoleBean(roleBean);
				roleForm.setList(list);
			} else{
				TblCmnRole roleBean = roleForm.getRoleBean();
				roleForm.setRoleBean(roleBean);
				
			}
		}
		return mapping.findForward("userShow");
	}
	
	/**
	 * 
	 * �������� ��ɫ�������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:10:51
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward entry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		RoleForm theForm = (RoleForm)form;
		TblCmnRoleOrg roleOrg = service().getRoot();
		theForm.setOrgBean(roleOrg);

		return mapping.findForward("entry");
	}
	
	/**
	 * 
	 * �������� ��ɫĿ¼�������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:10:51
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward directoryEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		RoleForm theForm = (RoleForm)form;
		TblCmnRoleOrg roleOrg = service().getRoot();
		theForm.setOrgBean(roleOrg);

		return mapping.findForward("directoryEntry");
	}
	
	/**
	 * 
	 * �������� ��ɫ����ѡ�񴰿�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:10:51
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selectRoleEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		RoleForm theForm = (RoleForm)form;
		TblCmnRoleOrg roleOrg = service().getRoot();
		theForm.setOrgBean(roleOrg);
		return mapping.findForward("selectRoleEntry");
	}

	/**
	 * 
	 * �������� ѡ��Ŀ¼��������.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:01:40
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selectMenu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm theForm = (RoleForm)form;
		TblCmnRoleOrg roleOrg = service().getRoot();
		theForm.setOrgBean(roleOrg);
		return mapping.findForward("selectMenu");
	}
	
	/**
	 * 
	 * �������� ��ɫȨ�ޱ༭.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����01:01:57
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward userPermissionDetailed(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
			RoleForm theForm = (RoleForm) form;
			String oid2=request.getParameter("oid2");
			theForm.setOid(oid2);
			theForm.setCmnRolePermission((TblCmnRolePermission)getService().load(TblCmnRolePermission.class,oid2));
			return mapping.findForward("userPermissionDetailed");
	}
	
	/**
	 * 
	 * ��������  ɾ��Ȩ�޷���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-23 ����05:54:10
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward removePermission(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String oid1 = request.getParameter("oid1");
		try{
			TblCmnRolePermission userPermission = (TblCmnRolePermission) getService().load(
					TblCmnRolePermission.class, oid1);
//			getService().deleteLogic(userPermission);
			getService().delete(userPermission);
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I044_R_0"));
		}catch(Exception e){
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I046_R_0"));
			e.printStackTrace();
			return mapping.findForward("userShow");
		}
			return permissionShow(mapping, form, request, response);
	}

	/**
	 * 
	 * �������� ��ɫ�û���Ϣɾ������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-26 ����09:55:09
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward removeUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String oid1 = request.getParameter("oid1");
		String message="";
		try{
			TblCmnUserRole userRole = (TblCmnUserRole) getService().load(
					TblCmnUserRole.class, oid1);
//			getService().deleteLogic(userRole);
			getService().delete(userRole);
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I044_R_0"));
		}catch(Exception e){
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I046_R_0"));
			e.printStackTrace();
			return mapping.findForward("userShow");
		}
			MessageUtils.addMessage(request, message);
			return userShow(mapping, form, request, response);
	}

	/**
	 * 
	 * ��������  ����ɾ��Ȩ����Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-23 ����05:53:51
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward deletePermissionsAll(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	String oid1 = request.getParameter("oids");
	List ids=Tools.split(oid1,"\\$");
	int len = ids.size();
	int okcount = 0;
	int errCount = 0;
	for(int i=0; i<len; i++){
		TblCmnRolePermission rolePermissionBean = (TblCmnRolePermission) getService().load(TblCmnRolePermission.class, ids.get(i).toString());
			try {
//				getService().deleteLogic(rolePermissionBean);
				getService().delete(rolePermissionBean);
				okcount++;
			}catch(Exception ex) {
				errCount++;
				ex.printStackTrace();
				continue;
			}
			
		}
	MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I047_R_2", String.valueOf(okcount), String.valueOf(errCount)));
	return permissionShow(mapping, form, request, response);
	
	}

	/**
	 * 
	 * �������� ����ɾ���û���Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-26 ����09:55:42
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward deleteUsersAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String oid1 = request.getParameter("oids");
		List ids=Tools.split(oid1,"\\$");
		int len = ids.size();
		int okcount = 0;
		int errCount = 0;
		for(int i=0; i<len; i++){
			TblCmnUserRole roleUser = (TblCmnUserRole) getService().load(TblCmnUserRole.class, ids.get(i).toString());
				try {
//					getService().deleteLogic(roleUser);
					getService().delete(roleUser);
					okcount++;
				}catch(Exception ex) {
					errCount++;
					ex.printStackTrace();
					continue;
				}
				
			}
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I047_R_2", String.valueOf(okcount), String.valueOf(errCount)));		
		return userShow(mapping, form, request, response);
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
		RoleForm theForm =(RoleForm)form;
		TblCmnRolePermission rolePermissionBean = theForm.getCmnRolePermission();
		try {
			getService().update(rolePermissionBean);
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I048_R_0"));
		}catch(Exception ex ){
			ex.printStackTrace();
			MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I049_R_0"));
		}
		theForm.setCmnRolePermission(rolePermissionBean);
		return null;
	}
	
	/**
	 * ��ѯ��ĳ�ڵ㼰�ӽڵ��µ�����Ŀ¼
	 * ��������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-30 ����02:04:15
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward roleOrgSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		RoleForm  roleForm = (RoleForm)form;
		String parentCode = request.getParameter("parentCode");
		List orglist = new ArrayList();
		if (parentCode==null || StringUtils.isBlank(parentCode)) {
			parentCode = service().getInitRoot().getId();
		}
		orglist = service().queryOrgStartWithFatherId(parentCode);
		//�б�dataȡ��
		//commonGetMenuRole(tempId,roleForm);
		roleForm.setOrgList(orglist);
		roleForm.setTypevalue("1");
		return mapping.findForward("roleOrgSearch");
	}
	/**
	 * ��ѯ��ĳ�ڵ㼰�ӽڵ��µ����н�ɫ
	 * ��������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-30 ����02:04:15
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward roleSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		RoleForm  roleForm = (RoleForm)form;
		String parentCode = request.getParameter("parentCode");
		String selectInfo = request.getParameter("selectInfo");
		String fname = request.getParameter("searchFatherName");
		List rolelist = new ArrayList();
		if (parentCode==null || StringUtils.isBlank(parentCode)) {
			parentCode = service().getInitRoot().getId();
		}
			rolelist =  service().queryRoleStartWithFatherId(parentCode,fname);
		//�б�dataȡ��
		//commonGetMenuRole(tempId,roleForm);
		roleForm.setRolList(rolelist);
		roleForm.setTypevalue("1");
		if(StringUtils.isNotBlank(selectInfo)){
			return mapping.findForward("selectInfoForMulu");
		}else{
			return mapping.findForward("list");
		}
		
	}
	
	/**
	 * ��ѯ��ĳ�ڵ㼰�ӽڵ��µ����н�ɫ(��ɫ����ѡ��ҳ��)
	 * ��������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-30 ����02:04:15
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selectRoleSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm  roleForm = (RoleForm)form;
		String parentCode = request.getParameter("parentCode");
		String fname = request.getParameter("searchFatherName");
		if (parentCode==null || StringUtils.isBlank(parentCode)) {
			parentCode = service().getInitRoot().getId();
		}
		List rolelist = service().queryRoleStartWithFatherId(parentCode,fname);
		//�б�dataȡ��
		//commonGetMenuRole(tempId,roleForm);
		roleForm.setRolList(rolelist);
		return mapping.findForward("selectRoleInfoList");
	}
	
	/**
	 * 
	 * �������� ��ϸ����ȡ��.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-11-30 ����03:51:15
	 * @version 1.0
	 * @author wanghy
	 */
	public  ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm theForm =(RoleForm)form;
		TblCmnRole roleChildren = new TblCmnRole();
		TblCmnRoleOrg  children = new TblCmnRoleOrg();
		String oid = theForm.getOid();
		if(StringUtils.isBlank(oid)){
			oid="";
		}
		String strArr[] = oid.split(";");
		if (strArr.length > 1) {
			theForm.setOid(strArr[0]);
			theForm.setOrgRoleFlg(strArr[1]);
		} 
		String parentCode = request.getParameter("parentCode");
		String parentCodeArr[] = parentCode.split(";");
		String formParentCodeArr[] = theForm.getParentCode().split(";");
		TblCmnRoleOrg father = (TblCmnRoleOrg)getService().load(TblCmnRoleOrg.class, formParentCodeArr[0]);
		theForm.setFatherName(father.getDisplayName());
		if((StringUtils.isBlank(strArr[0]) || parentCodeArr.length > 2) && "org".equals(parentCodeArr[1])){
			theForm.setOrgRoleFlg("org");
			theForm.setStrCreate("c");
			theForm.setOrgBean(children);
			MessageUtils.addMessage(request, "��ǰΪ�½�Ŀ¼״̬");
			return mapping.findForward("detail");
		}
		if((StringUtils.isBlank(strArr[0]) || parentCodeArr.length > 2) && "role".equals(parentCodeArr[1])){
		//if((parentCodeArr.length > 1 && "role".equals(parentCodeArr[1])) || StringUtils.isBlank(oid)){
			theForm.setOrgRoleFlg("role");
			theForm.setStrCreate("c");
			roleChildren.setSystemRole("0");
			theForm.setRoleBean(roleChildren);
			MessageUtils.addMessage(request, "��ǰΪ�½���ɫ״̬");
			return mapping.findForward("detail");
		}
		if(StringUtils.isNotBlank(strArr[0]) && "org".equals(theForm.getOrgRoleFlg())){
			MessageUtils.addMessage(request, "��ǰΪĿ¼�༭״̬");
		}
		if(StringUtils.isNotBlank(strArr[0]) && "role".equals(theForm.getOrgRoleFlg())){
			if("systemRole".equals(theForm.getRoleBean().getRoleCd()) || "CMN_NORMAL_USER".equals(theForm.getRoleBean().getRoleCd()))
			theForm.setConsignFlg("baseOrSystem");
			MessageUtils.addMessage(request, "��ǰΪ��ɫ�༭״̬");
		}
		return mapping.findForward("detail");
	}	
	
	/**
	 * 
	 * �������� ��ɫѡ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-4 ����09:41:34
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selectInfo(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		RoleForm  roleForm = (RoleForm)form;
		String parentCode = roleForm.getParentCode();
		String mulu = request.getParameter("mulu");
		if(StringUtils.isNotBlank(mulu)){
			List roleli = new ArrayList();
			List roleList = new ArrayList();
			roleli = service().getChildren(parentCode,roleForm);
			for(Iterator iter= roleli.iterator();iter.hasNext();){
				Object temp = iter.next();
				if (null != temp && temp instanceof TblCmnRole ) {
					TblCmnRole roleTemp = (TblCmnRole)temp;
					if ("0".equals(roleTemp.getDelFlg())) {
						roleList.add(roleTemp);
					}
				}
			}
			roleForm.setRolList(roleList);
			return mapping.findForward("selectInfoForMulu");
		}else{
			if (!"root".equals(parentCode)){
				roleForm.setOid(parentCode);
				TblCmnRole roleBean = roleForm.getRoleBean();
				List list = service().list(roleBean,"permission");
				roleForm.setList(list);
			} 
			return mapping.findForward("selectInfo");
		}
	}

	/**
	 * 
	 * �������� ��ɫѡ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-4 ����09:41:34
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selectInfo2(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		RoleForm  roleForm = (RoleForm)form;
		String parentCode = roleForm.getParentCode();
		String mulu = request.getParameter("mulu");
		if(StringUtils.isNotBlank(mulu)){
			List roleli = new ArrayList();
			List roleList = new ArrayList();
			roleli = service().getChildren(parentCode,roleForm);
			for(Iterator iter= roleli.iterator();iter.hasNext();){
				Object temp = iter.next();
				if (null != temp && temp instanceof TblCmnRole ) {
					TblCmnRole roleTemp = (TblCmnRole)temp;
					if ("0".equals(roleTemp.getDelFlg())) {
						roleList.add(roleTemp);
					}
				}
			}
			roleForm.setRolList(roleList);
			return mapping.findForward("selectInfoForMulu");
		}else{
			if (!"root".equals(parentCode)){
				roleForm.setOid(parentCode);
				TblCmnRole roleBean = roleForm.getRoleBean();
				List list = service().list(roleBean,"permission");
				roleForm.setList(list);
			} 
			return mapping.findForward("selectInfo");
		}
	}
	
	/**
	 * 
	 * �������� ��ɫѡ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-4 ����09:41:34
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selectForRole(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		RoleForm  roleForm = (RoleForm)form;
		String parentCode = request.getParameter("parentCode");
		roleForm.setParentCode(parentCode);
			List roleli = new ArrayList();
			List roleList = new ArrayList();
			roleli = service().getChildren(parentCode,roleForm);
			for(Iterator iter= roleli.iterator();iter.hasNext();){
				Object temp = iter.next();
				if (null != temp && temp instanceof TblCmnRole ) {
					TblCmnRole roleTemp = (TblCmnRole)temp;
					if ("0".equals(roleTemp.getDelFlg())) {
						roleList.add(roleTemp);
					}
				}
			}
			roleForm.setRolList(roleList);
			return mapping.findForward("selectInfoFrame");
	}
	
	/**
	 * 
	 * �������� ��ɫѡ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-4 ����10:03:18
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selectEntry(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
			RoleForm theForm = (RoleForm)form;
			String userOid = request.getParameter("oid");
			String consignFlg = request.getParameter("flg");
			theForm.setConsignFlg(consignFlg);
			String userId = SecurityContextInfo.getCurrentUser().getUserId();
			if (StringUtils.isBlank(userOid)) {
				TblCmnRoleOrg roleOrg = service().getRoot();
				theForm.setOrgBean(roleOrg);				
			} else {
				if (StringUtils.isNotBlank(userOid)) {
			 		TblCmnUser userBean =(TblCmnUser)getService().load(TblCmnUser.class,userId);
			 		List menuLst = service().getRole(userBean);
			 		if (menuLst.size() == 0){
						TblCmnRoleOrg roleOrg = new TblCmnRoleOrg();
						roleOrg.setId("root");
						roleOrg.setDisplayName("���û��Ѿ�û�н�ɫ���䣮");
						theForm.setOrgBean(roleOrg);
			 		}else {
						TblCmnRoleOrg roleOrg = new TblCmnRoleOrg();
						roleOrg.setId("root");
						roleOrg.setDisplayName(userBean.getEmpName().concat(" �Ľ�ɫ�б�"));
						theForm.setOrgBean(roleOrg);
			 		}
					//theForm.setOid(userOid);
				} 
			}
			return mapping.findForward("selectEntry");
		}
	/**
	 * 
	 * �������� ��ɫѡ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-4 ����10:03:18
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selectEntry2(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
			RoleForm theForm = (RoleForm)form;
			String userOid = request.getParameter("oid");
			String consignFlg = request.getParameter("flg");
			theForm.setConsignFlg(consignFlg);
			String userId = SecurityContextInfo.getCurrentUser().getUserId();
			if (StringUtils.isBlank(userOid)) {
				TblCmnRoleOrg roleOrg = service().getRoot();
				theForm.setOrgBean(roleOrg);				
			} else {
				if (StringUtils.isNotBlank(userOid)) {
			 		TblCmnUser userBean =(TblCmnUser)getService().load(TblCmnUser.class,userId);
			 		List menuLst = service().getRole(userBean);
			 		if (menuLst.size() == 0){
						TblCmnRoleOrg roleOrg = new TblCmnRoleOrg();
						roleOrg.setId("root");
						roleOrg.setDisplayName("���û��Ѿ�û�н�ɫ���䣮");
						theForm.setOrgBean(roleOrg);
			 		}else {
						TblCmnRoleOrg roleOrg = new TblCmnRoleOrg();
						roleOrg.setId("root");
						roleOrg.setDisplayName(userBean.getEmpName().concat(" �Ľ�ɫ�б�"));
						theForm.setOrgBean(roleOrg);
			 		}
					//theForm.setOid(userOid);
				} 
			}
			return mapping.findForward("selectEntry2");
		}
	/**
	 * 
	 * �������� 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-4 ����11:30:53
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selTree(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) 
			throws Exception {
		
	 		String oid = request.getParameter("oid");
			RoleForm theForm = (RoleForm)form;
			String parentCode = theForm.getParentCode();
		 	List newList = new ArrayList();
		 	String oidArr[] = {};
		 	if (null != oid){
		 		oidArr = oid.split(";"); 
		 	}
		 	String flg = request.getParameter("flg");
		 	boolean isConsign = false;
		 	if(StringUtils.isNotBlank(flg))
		 	{
		 		if(flg.equals("consign"))
		 			isConsign = true;
		 	}
		 	//��ȡ��ǰ�û��Ľ�ɫ���ϣ���ί�н�ɫʱ�����Ľ�ɫѡ����Ҫ�õ���
		 	java.util.Collection roles = SecurityContextInfo.getCurrentUser().getRoles().keySet();
		 	
		 	if (oidArr.length > 1 && !"".equals(oidArr[0]) && !"".equals(oidArr[1])){
//		 	if(null != oid && 0 != oid.length()){
		 		TblCmnUser userBean =(TblCmnUser)getService().load(TblCmnUser.class, oidArr[1]);
		 		List menuLst = service().getRole(userBean);
		 		for (Iterator iter = menuLst.iterator();iter.hasNext();){
		 			TblCmnUserRole bean = (TblCmnUserRole)iter.next();
		 			TblCmnRole roleTemp = bean.getTblCmnRole();
		 			if ("0".equals(roleTemp.getDelFlg())){
		 				newList.add(roleTemp);
		 			}
		 			
		 		}
		 		
		 	} else {
				if (parentCode==null || StringUtils.isBlank(parentCode)) {
					parentCode = service().getInitRoot().getId();
				}
				List list  = null;
				//��ɫȡ��
				list = service().getChildren(parentCode,theForm);
				for(Iterator iter= list.iterator();iter.hasNext();){
					TblCmnRole temp = (TblCmnRole)iter.next();
					if (null != temp) {
//						if(temp instanceof TblCmnRoleOrg ){
//							TblCmnRoleOrg orgTemp = (TblCmnRoleOrg)temp;
//							orgTemp.setTableFlg("0");
//						}
						if(isConsign)//ί�н�ɫ
						{
							if(roles.contains(temp.getId()))
								newList.add(temp);
						}
						else
						{
							newList.add(temp);
						}
					}
				}
				//��֯�ṹȡ��
				list = service().getOrgMenu(parentCode,theForm);
				
				for(Iterator iter= list.iterator();iter.hasNext();){
					Object temp = iter.next();
					//if (null != temp && temp instanceof TblCmnRoleOrg) {
					TblCmnRoleOrg orgTemp = (TblCmnRoleOrg)temp;
					//if ("0".equals(orgTemp.getDelFlg())){
					orgTemp.setTableFlg("0");
					if(isConsign)//ί�н�ɫ
					{
						if(service().isAnyRoleInRoleOrg(orgTemp.getId(), roles))
						{
							newList.add(temp);
						}
					}
					else
					{
						newList.add(temp);
					}
					//}
					//}
				}				
		 	}	

			response.setContentType("text/plain; charset=GBK");
			response.getWriter().print(FlatTreeUtils.serialize(newList, false));
			return null;
	}
	
	/**
	 * 
	 * �������� ����Ȩ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����09:13:46
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward addPermission(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		RoleForm  roleForm = (RoleForm)form;
		String strOid = roleForm.getOid();
		String strArr[] = strOid.split(";");
		String orgFlg ="";
		if (strArr.length > 1) {
			strOid = strArr[0];
			roleForm.setOid(strOid);
			orgFlg = strArr[1];
			roleForm.setOrgRoleFlg(orgFlg);
		}
		String permissionOid = request.getParameter("permissionIds");
		TblCmnRole roleBean = (TblCmnRole)getService().load(TblCmnRole.class, roleForm.getOid());
		String permissionArr[] = permissionOid.split(";");
		//int countNg = 0 ;
		//int countOk = 0 ;
		for (int i = 3; i < permissionArr.length; i++){
			//���ؽڵ�
			TblCmnPermission permission = (TblCmnPermission) getService()
			.load(TblCmnPermission.class, permissionArr[i]);
			//�����Ŀ¼����Ҫ��Ŀ¼�µ�����Ȩ�޸����ɫ
			//�����Ȩ�ޣ���ô��Ȩ�޸����ɫ
			if("0".equals(permission.getNodetype()))//Ŀ¼
			{
				int count = service().addFolerPermissions(permission.getId(),
						roleBean,permissionArr[0].toString(),permissionArr[1].toString(),permissionArr[2].toString());
				MessageUtils.addMessage(request, "Ŀ¼["+permission.getPermissionRoute()+"]�µ�"+count+"��Ȩ��ȫ�����룡");
			}
			else//Ȩ��
			{
				TblCmnRolePermission rolePermisionBean = new TblCmnRolePermission();
				TblCmnPermission permissionBean = (TblCmnPermission)getService().load(TblCmnPermission.class, permissionArr[i]);
				rolePermisionBean.setTblCmnRole(roleBean);
				rolePermisionBean.setTblCmnPermission(permissionBean);
				rolePermisionBean.setRwCtrl(permissionArr[0].toString());
				rolePermisionBean.setReadAccessLevel(permissionArr[1].toString());
				rolePermisionBean.setWriteAccessLevel(permissionArr[2].toString());
				
				/*������Ȩ����Ҫ�������룬��Ӧ��Ĭ�ϼ��룬���ע�͵�һ�´���
				 * if("1".equals(permission.getNodetype()))//����Ȩ��
				{
					//�������е�������Ȩ��
					int count = service().addFiledPermissionsForFunPermission(rolePermisionBean);
					if(count>0)
						MessageUtils.addMessage(request, "����Ȩ��["+permission.getPermissionRoute()+"]��"+count+"��������Ȩ�޼���ɹ�!");
				}
				*/
				try {
					getService().add(rolePermisionBean);
					//countOk ++;
					MessageUtils.addMessage(request, "Ȩ��["+permission.getPermissionRoute()+"]����ɹ�!");

				}catch(DataIntegrityViolationException e){
					MessageUtils.addErrorMessage(request, "Ȩ��["+permission.getPermissionRoute()+"]�Ѵ���!");
					//countNg ++;
				}


			}
		}
		//MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I050_R_2", String.valueOf(countNg), String.valueOf(countOk)));		
		return permissionShow(mapping, form, request, response);
	}
	

	/**
	 * 
	 * �������� �����û�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-7 ����10:01:28
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward addUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		RoleForm  roleForm = (RoleForm)form;
		String strOid = roleForm.getOid();
		String strArr[] = strOid.split(";");
		int countNg = 0 ;
		int countOk = 0 ;
		String orgFlg ="";
		if (strArr.length > 1) {
			strOid = strArr[0];
			roleForm.setOid(strOid);
			orgFlg = strArr[1];
			roleForm.setOrgRoleFlg(orgFlg);
		}
		String userOid = request.getParameter("oid2");
		TblCmnRole roleBean = (TblCmnRole)getService().load(TblCmnRole.class, roleForm.getOid());
		
		String userArr[] = userOid.split(";");
		for (int i = 0; i < userArr.length; i++){
			TblCmnUserRole userRoleBean = new TblCmnUserRole();
			TblCmnUser userBean = (TblCmnUser)getService().load(TblCmnUser.class, userArr[i]);
			userRoleBean.setTblCmnRole(roleBean);
			userRoleBean.setTblCmnUser(userBean);
			try {
				getService().add(userRoleBean);
				countOk ++;
			}catch(DataIntegrityViolationException e){
				countNg ++;
			}
		}
		MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I064_R_2", String.valueOf(countNg), String.valueOf(countOk)));		
		return userShow(mapping, form, request, response);
	}
	///////////////////////////////////////////////////////////////
	/**
	 * �������� ��ɫ����ѡ�񵯳�����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @time 2009-4-22 ����19:52:34
	 * @version 1.0
	 * @author gejb
	 */
	public ActionForward selectMultipleRoleEntry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm theForm = (RoleForm)form;
		String strFlg=request.getParameter("strFlg");
		theForm.setStrFlg(strFlg);
		TblCmnRoleOrg roleOrg = service().getRoot();
		theForm.setOrgBean(roleOrg);
		return mapping.findForward("selectMultipleRoleEntry");
	}
	
	
	/**
	 * 
	 * �������� ��ɫ����ѡ���б�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @time 2009-4-22 ����19:52:34
	 * @version 1.0
	 * @author gejb
	 */
	public ActionForward selectMultipleRoleList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RoleForm theForm =(RoleForm)form;
		String parentCode = request.getParameter("parentCode");
		
		if (parentCode==null || StringUtils.isBlank(parentCode)) {
			parentCode = service().getInitRoot().getId();
		}

		commonGetMenuRole(parentCode,theForm);
		return mapping.findForward("selectMultipleRoleList");
	}
	
	/**
	 * ��ѯ��ĳ�ڵ㼰�ӽڵ��µ����н�ɫ(��ɫ��������ѡ��ҳ��)
	 * ��������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2009-4-23 ����02:30:15
	 * @version 1.0
	 * @author gejb
	 */
	public ActionForward selectMultipleRoleSearch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RoleForm  roleForm = (RoleForm)form;
		String parentCode = request.getParameter("parentCode");
		String fname = request.getParameter("searchFatherName");
		if (parentCode==null || StringUtils.isBlank(parentCode)) {
			parentCode = service().getInitRoot().getId();
		}
		List rolelist = service().queryRoleStartWithFatherId(parentCode,fname);
		//�б�dataȡ��
		//commonGetMenuRole(tempId,roleForm);
		roleForm.setRolList(rolelist);
		return mapping.findForward("selectMultipleRoleList");
	}
}

