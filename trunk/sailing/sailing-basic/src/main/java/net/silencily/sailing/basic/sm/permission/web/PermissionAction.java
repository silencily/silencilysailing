package net.silencily.sailing.basic.sm.permission.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.permission.service.PermissionService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.domain.tree.FlatTreeUtils;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.DispatchActionPlus;
import net.silencily.sailing.utils.MessageInfo;
import net.silencily.sailing.utils.MessageUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * ��֯����(DeptAction)
 * @author gaojing
 * @version $Id: PermissionAction.java,v 1.1 2010/12/10 10:56:30 silencily Exp $
 * @since 2007-8-29
 */
public class PermissionAction extends DispatchActionPlus {

	public static PermissionService service() {
		return (PermissionService) ServiceProvider.getService(PermissionService.SERVICE_NAME);
	}

	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
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
	 * 2007-12-4 ����02:22:48
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward entry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PermissionForm theForm = (PermissionForm)form;
		TblCmnPermission bean = (TblCmnPermission)service().getRoot();
		theForm.setBean(bean);
		return mapping.findForward("entry");
	}
	
	/**
	 * 
	 * �������� ������������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-4 ����02:22:48
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PermissionForm theForm = (PermissionForm)form;
		TblCmnPermission bean = (TblCmnPermission)service().getRoot();
		theForm.setBean(bean);
		return mapping.findForward("selEntry");
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
	 * 2007-12-4 ����02:22:44
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PermissionForm theForm =(PermissionForm)form;
		String parentCode = request.getParameter("parentCode");
		String search = request.getParameter("search");
		if (parentCode==null || StringUtils.isBlank(parentCode)) {
			parentCode = service().getInitRoot().getId();
		}
		theForm.setCurrentFatherName(((TblCmnPermission)getService().load(TblCmnPermission.class, parentCode)).getDisplayname());
		List list = null;
		List tempLst = new ArrayList();
		if (!"s".equals(search)){
			list = service().getChildren(parentCode);
			theForm.setPageUpDlg("searchTag");
		} else {
			String fatherName = request.getParameter("fatherName");
			list = service().queryStartWithFatherId(parentCode,fatherName);
			theForm.setPageUpDlg(null);
		}
		for(Iterator iter= list.iterator();iter.hasNext();){
		TblCmnPermission tblPmBean = (TblCmnPermission)iter.next();	
			if (null != tblPmBean && "0".equals(tblPmBean.getDelFlg())) {
				tempLst.add(tblPmBean);
			}
		}
		theForm.setList(tempLst);
		return mapping.findForward("list");	
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
	 * 2007-12-5 ����12:22:28
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PermissionForm theForm =(PermissionForm)form;
		String Oid = theForm.getOid();
		String parentCode = theForm.getParentCode();
		String strArr[] ={};
		String strArrCode[] = {};
		if(null != Oid){
			strArr = Oid.split(";");
		}
		if (null != parentCode){
			strArrCode =  parentCode.split(";");
		}
		//����ڵ�ȡ��.
		if (strArr.length > 1) {
			theForm.setOid(strArr[0]);
//			menuDataFlg = strArr[1];
		} 
		theForm.setMenuDataFlg(theForm.getBean().getNodetype());
		//���ڵ�ȡ��
		if (strArrCode.length > 1) {
			theForm.setParentCode(strArrCode[0]);
			theForm.setMenuDataFlg(strArrCode[1]);
			theForm.setStrCreate(strArrCode[2]);
			theForm.setOid("");
		} 
		String creat = theForm.getStrCreate();
		if ("c".equals(creat) || StringUtils.isBlank(Oid)){
			//�½�
			TblCmnPermission father = (TblCmnPermission)getService().load(TblCmnPermission.class, theForm.getParentCode());
			if (StringUtils.isBlank(creat)){
				theForm.setMenuDataFlg("1");
			}
			TblCmnPermission  children = new TblCmnPermission();
			children.setFather(father);
			theForm.setBean(children);
			theForm.setParentCode(father.getId());
			theForm.getBean().setUrltype("1");
			theForm.getBean().setSystemPermission("0");
		} else {	
			if (!"root".equals(theForm.getOid())){
				TblCmnPermission roleBean = theForm.getBean();
				theForm.setParentCode(roleBean.getFather().getId());
			} 
		}
		if("0".equals(theForm.getMenuDataFlg())){
			if("c".equals(creat)){
				MessageUtils.addMessage(request, "��ǰΪĿ¼�½�״̬");
			}else{
				MessageUtils.addMessage(request, "��ǰΪĿ¼�༭״̬");
			}
		}else if("1".equals(theForm.getMenuDataFlg())){
			if("c".equals(creat)){
				MessageUtils.addMessage(request, "��ǰΪ����Ȩ���½�״̬");
			}else{
				MessageUtils.addMessage(request, "��ǰΪ����Ȩ�ޱ༭״̬");
			}
		}else{
			if("c".equals(creat)){
				MessageUtils.addMessage(request, "��ǰΪ������Ȩ���½�״̬");
			}else{
				MessageUtils.addMessage(request, "��ǰΪ������Ȩ�ޱ༭״̬");
			}
		}
		return mapping.findForward("edit");	
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
	 * 2007-12-4 ����02:22:55
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward tree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		PermissionForm theForm = (PermissionForm)form;
		String parentCode = theForm.getParentCode();
		if (parentCode==null || StringUtils.isBlank(parentCode)) {
			parentCode = service().getInitRoot().getId();
		}
		List tempLst = new ArrayList();
		String nodetype = request.getParameter("nodetype");
		List list = null;
		String[] nodetypes = null;
		if(StringUtils.isNotBlank(nodetype))
		{
			nodetypes = nodetype.split(";");
			list = service().getChildren(parentCode,nodetypes);
		}
		else
		{
			list = service().getChildren(parentCode);
		}
		
		for(Iterator iter = list.iterator();iter.hasNext();){
			TblCmnPermission tblPmBean = (TblCmnPermission)iter.next();
			if(null != tblPmBean ){
				if(null != nodetypes)
					tblPmBean.setNodeTypes(nodetypes);
				tempLst.add(tblPmBean);
			}
		}
		response.setContentType("text/plain; charset=GBK");
		response.getWriter().print(FlatTreeUtils.serialize(tempLst, false));
		return null;
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
	 * 2007-12-4 ����02:22:55
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selectTree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		PermissionForm theForm = (PermissionForm)form;
		String parentCode = theForm.getParentCode();
		if (parentCode==null || StringUtils.isBlank(parentCode)) {
			parentCode = service().getInitRoot().getId();
		}
		List tempLst = new ArrayList();
		List list = service().getChildren(parentCode);
		
		for(Iterator iter = list.iterator();iter.hasNext();){
			TblCmnPermission tblPmBean = (TblCmnPermission)iter.next();
			if(null != tblPmBean ){
				//tblPmBean.setNodetype((Integer.parseInt(tblPmBean.getNodetype()) + 1)+"");
				tempLst.add(tblPmBean);
			}
		}
		response.setContentType("text/plain; charset=GBK");
		response.getWriter().print(FlatTreeUtils.serialize(tempLst, false));
		return null;
	}
	
	/**
	 * 
	 * �������� ��ý�ɫ�����û�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-4 ����02:22:55
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward achieveRoleUser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String roleId = request.getParameter("roleId");
		PermissionForm theForm =(PermissionForm)form;
		TblCmnRole role = new TblCmnRole();
		if(StringUtils.isNotBlank(roleId)){
			 role = (TblCmnRole) getService().load(
					TblCmnRole.class, roleId);
		}
		List list = null;
		if (null != role.getId()) {
			list = service().getRoleUser(role);
		} else {
			list = new ArrayList(0);
		}
		theForm.setList(list);
		theForm.setRoleName(role.getName());
		theForm.setRoleId(role.getId());
		return mapping.findForward("perAchieveRoleUser");
	}
	
	/**
	 * 
	 * �������� Ȩ����ϸ��Ϣ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-5 ����12:22:28
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward permissionInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PermissionForm theForm =(PermissionForm)form;
		String parentCode = theForm.getParentCode();
		theForm.setOid(parentCode);
		TblCmnPermission bean = (TblCmnPermission)getService().load(TblCmnPermission.class, theForm.getParentCode());
		theForm.setMenuDataFlg(bean.getNodetype());
		theForm.setBean(bean);
		return mapping.findForward("permissionInfo");	
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
	 * 2007-12-5 ����03:46:52
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward permissionSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		PermissionForm theForm = (PermissionForm)form;
		TblCmnPermission child = theForm.getBean();
		String currentId = "";
		//ȡ�õ�ǰ�ڵ��ID��theForm.getParentCode()�б������Ҫת�ƽڵ��ID��
		if(child != null){
			currentId = child.getId();
			
			//��֤��ͬurl�Ĺ���Ȩ���Ƿ����
			if("1".equals(child.getNodetype())&& StringUtils.isNotBlank(child.getUrl()) && StringUtils.isBlank(child.getId()))
			{
				
				if(service().existUrl(child.getUrl()))
				{
					MessageUtils.addErrorMessage(request, "��ͬ�����ӵĹ���Ȩ���Ѵ��ڣ�����������");	
					return mapping.findForward("edit");					
				}
			}
		}
		//����ѵ�ǰ�ڵ��Ƶ��Լ����ӽڵ��еĴ���������õ�ǰ�ڵ��ID��Ҫת�Ƶ��Ľڵ�ID��Ƚϣ������ǰ�ڵ���
		//Ҫת�ƽڵ�ĸ������Ͳ�����ת�Ʋ�����ҳ����ʾ������Ϣ��
		if (service().isSubNode(currentId,theForm.getParentCode() )){
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I060_R_0"));
			return mapping.findForward("edit");
		}
			
		List onlyLst = null;
		if ("2".equals(theForm.getMenuDataFlg())){
			onlyLst = service().onlyOneFlg(child.getPermissionCd(),theForm.getParentCode());
		}
		String sameItem="";
		if(null != onlyLst)
		{	
			Iterator it = onlyLst.iterator();
			while(it.hasNext()){
				if(((TblCmnPermission)it.next()).getId().equals(child.getId())){
					sameItem = "ok";
					break;
				}
			}
			if (0 != onlyLst.size() && !sameItem.equals("ok"))
			{	
				MessageUtils.addErrorMessage(request, "������Ȩ�ޱ�ʶ�Ѵ��ڣ�����������");	
				return mapping.findForward("edit");
			}
		
		}
		TblCmnPermission father = (TblCmnPermission)getService().load(TblCmnPermission.class, theForm.getParentCode());
		child.setFather(father);
		if ("c".equals(theForm.getStrCreate())){
			
			//�½�һ��Ŀ¼����
			try {
				getService().add(child);
				theForm.setOid(child.getId());
				theForm.setStrCreate("");
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I038_R_0"));
			}catch (Exception ex){
				ex.printStackTrace();
				MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I039_R_0"));
			}
		} else {
			//TblCmnRoleOrg org = roleForm.getOrgBean();
			
			try {
				//����һ��Ŀ¼����
				getService().update(child);
				MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I040_R_0"));

			} catch (Exception ex){
				ex.printStackTrace();
				MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I041_R_0"));
			}
			
		}

		return mapping.findForward("edit");
	}
	
	public ActionForward removePermission(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String oid1 = request.getParameter("oid1");
		try{
			TblCmnPermission userPermission = (TblCmnPermission) getService().load(
					TblCmnPermission.class, oid1);
			List childLst = service().getChildren(oid1);
			if (null != childLst && 0 != childLst.size()){
				MessageUtils.addWarnMessage(request, MessageInfo.factory().getMessage("SM_I052_R_0"));				
			} else {
				List rolePLst = service().getRolePermision(userPermission);
				List userPLst = service().getUserPermision(userPermission);
				if ((null == userPLst || 0 == userPLst.size()) && 
						 (null == rolePLst || 0 == rolePLst.size())) {
//					getService().deleteLogic(userPermission);
					getService().delete(userPermission);
					MessageUtils.addMessage(request, MessageInfo.factory().getMessage("SM_I044_R_0"));	
				} else {
					MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I053_R_0"));	
				}
			}
		}catch(Exception e){
			MessageUtils.addErrorMessage(request, MessageInfo.factory().getMessage("SM_I046_R_0"));	
			e.printStackTrace();
			return mapping.findForward("list");
		}
			return list(mapping, form, request, response);
	}
	
	/**
	 * 
	 * �������� Ȩ�޹�����ɫ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-5 ����12:22:28
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward permissionRole(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PermissionForm theForm =(PermissionForm)form;
		String Oid = theForm.getOid();
		String strArr[] ={};
		if(null != Oid && Oid.indexOf(";")>-1){
			strArr = Oid.split(";");
			if (strArr.length > 1) {
				theForm.setOid(strArr[0]);
			} 
		}
		List list = null;
		TblCmnPermission bean = theForm.getBean();
		if (null != bean.getId()) {
			list = service().getPerRole(bean);
		} else {
			list = new ArrayList(0);
		}

		theForm.setPerRole(list);
		return mapping.findForward("permissionRole");
	}
	
	/**
	 * 
	 * �������� Ȩ�޹����û�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 2007-12-5 ����12:22:28
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward permissionUser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PermissionForm theForm =(PermissionForm)form;
		String Oid = theForm.getOid();
		String strArr[] ={};
		if(null != Oid && Oid.indexOf(";")>-1){
			strArr = Oid.split(";");
			if (strArr.length > 1) {
				theForm.setOid(strArr[0]);
			} 
		}
		List list = null;
		TblCmnPermission bean = theForm.getBean();
		if (null != bean.getId()) {
			list = service().getPerUser(bean);
		} else {
			list = new ArrayList(0);
		}

		theForm.setPerUser(list);
		return mapping.findForward("permissionUser");
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
	 * 2007-12-5 ����07:27:45
	 * @version 1.0
	 * @author wanghy
	 */
	public ActionForward selectMenu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PermissionForm theForm = (PermissionForm)form;
		TblCmnPermission bean = (TblCmnPermission)service().getRoot();
		theForm.setBean(bean);
		return mapping.findForward("selectMenu");
	}

	
}
