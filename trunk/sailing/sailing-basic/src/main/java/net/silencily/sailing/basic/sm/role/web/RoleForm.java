package net.silencily.sailing.basic.sm.role.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnRoleOrg;
import net.silencily.sailing.basic.sm.domain.TblCmnRolePermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;

/**
 * ��֯����ά��<code>form</code>, ע�����е�{@link #getBean()}�Ǻ�������, �������ģʽ
 * ���ǿ��Լ���ؼ򻯴�һ��������װ<code>domain object</code>�Ĺ���
 * 
 * @author gaojing
 * @version $Id: RoleForm.java,v 1.1 2010/12/10 10:56:42 silencily Exp $
 * @since 2007-8-29
 */
public class RoleForm extends BaseFormPlus {
	
	private static Map menuRoleMap = new HashMap();
	static{
		menuRoleMap.put("0", "Ŀ¼");
		menuRoleMap.put("1", "��ɫ");
	}
	/**
	 * ί�н�ɫ���
	 */
	private String consignFlg;
	
	private String typevalue = "0";
	/**
	 * LIST��Ϣ
	 */
	private List list;
	
	/**
	 * ��ɫ
	 */
	private List rolList;
	
	/**
	 * ��֯
	 */
	private List orgList;

	/**
	 * ��ɫ����
	 */
	private TblCmnRole roleBean;
	
	/**
	 *  ���ݷ��ʼ���
	 */
	private ComboSupportList readAccessLevelComboList = null;
	
	private ComboSupportList urltypeComboList;
	
	/**
	 *д����
	 */
	private ComboSupportList writeAccessLevelComboList = null;
	
	/**
	 * ��д����
	 */
	private ComboSupportList rwCtrlComboList = null;
	
	private ComboSupportList nodetypeComboList;
	
	/**
	 * ��ɫ��֯����
	 */
	private TblCmnRoleOrg orgBean;
	
	private TblCmnRolePermission cmnRolePermission;
	
	/**
	 * ��ɫȨ����Ϣ
	 */
	private TblCmnPermission cmnPermission;
	/**
	 * ��ɫ���� 
	 */
	private ComboSupportList statusComboList = null;
	
	/**
	 * ϵͳ��ɫ
	 */
	private ComboSupportList systemRoleComboList = null;
	
	/**
	 * ����
	 */
	private ComboSupportList menuRoleComboList = null;
	
	/**
	 * ���ڵ�����
	 */
	private String fatherName;
	
	/**
	 * ���ڵ�����(��ѯ��)
	 */
	private String searchFatherName;
	
	/**
	 * ��ʾ����
	 */
	private String displayName;

	/**
	 * ͬ�����
	 */
	private String displayOrder;
	
	/**
	 * ��ɫ��ʶ(��ɫ��
	 */
	private String roleCd;
	
	/**
	 * ϵͳȨ��(��ɫ��
	 */
	private String systemRole;
	
	/**
	 * ���ڵ�
	 */
	private String parentCode;
	
	/**
	 * ԭ���ĸ��ڵ�
	 */
	private String  oldParentCode;
	
	private String  strFlg;
	
	/**
	 * ��֯/��ɫ����
	 */
	private String orgRoleFlg;
	
	/**
	 * �½�ʶ��.
	 */
	private String strCreate;
	
	/**
	 * 
	 * �������� ��ͨservice
	 * @return
	 * 2007-12-7 ����01:40:10
	 * @version 1.0
	 * @author wanghy
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}

	/**
	 * <p>
	 * �ڵ����������ʱ, <code>web tier framework</code>���ܻ�û�п�ʼ��װ{@link #parentCode}
	 * �������, Ҳ����˵��{@link #getParentCode()}���ܷ����������"parentCode"��ֵ, ����������
	 * <code>request</code>�л�ȡ���ֵ, �Ա㷽��{@link #getBean()}������ȷִ��
	 * </p>
	 * <p>
	 * ���ܲ���ÿһ�����ܶ���Ҫ�������, ����ͳһ�ش�����Լ򻯱��
	 * </p>
	 * 
	 * @return �������<code>parentCode</code>��ֵ
	 */
	public String getParentCode() {
		if (parentCode == null ||"".equals(parentCode)) {
			parentCode = request.getParameter("parentCode");
		}
		if (parentCode == null || "".equals(parentCode)) {
			parentCode = RoleAction.service().getInitRoot().getId();
		}
		return parentCode;
	}

	public void setParentCode(String parentId) {
		this.parentCode = parentId;
	}


	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
	
	/**
	 * ���{@link THrDept}��ʵ��, �Ա�<code>web tier framework</code>���Կ�ʼ
	 * ��װ����ҳ��Ĳ���, ҳ�����������Ҫ�޸ĵĲ��������ǰ����е����Զ�������ҳ���<code>hidden field</code> ��,
	 * ����<code>java</code>��������ͨ���е�һ�ַ���, ��Ȼ���������ķ���, �����Ը���һЩ,
	 * �������ǽ����������ִ����ȵ�����Ч�İ취
	 * 
	 * @return {@link THrDept}��ʵ��
	 */	
	public TblCmnRoleOrg getOrgBean() {
		if (orgBean == null) {
			if (StringUtils.isBlank(getOid())) {
				orgBean = new TblCmnRoleOrg();
			}
			else {
				String[] tokens = getOid().split(";");
				orgBean = (TblCmnRoleOrg)getService().load(TblCmnRoleOrg.class, tokens[0]);
			}
			String fatherid = request.getParameter("temp.orgBean.father.id");
			if(StringUtils.isNotBlank(fatherid)){
				fatherid = fatherid.split(";")[0];
				if(null != orgBean.getFather())
				{
					//���ĸ��ڵ�
					if(!fatherid.equals(orgBean.getFather().getId()))
						orgBean.setFather((TblCmnRoleOrg)getService().load(TblCmnRoleOrg.class, fatherid));
				}
				else
				{
					//�������ڵ�
					orgBean.setFather((TblCmnRoleOrg)getService().load(TblCmnRoleOrg.class, fatherid));
				}
			}
		}
		return orgBean;
	}

	public void setOrgBean(TblCmnRoleOrg orgBean) {
		this.orgBean = orgBean;
	}

	public TblCmnRole getRoleBean() {
		if (roleBean == null) {
			if (StringUtils.isBlank(getOid())) {
				roleBean = new TblCmnRole();
			}
			else {
				String[] tokens = getOid().split(";");
				roleBean = (TblCmnRole)getService().load(TblCmnRole.class, tokens[0]);
			}
			String fatherid = request.getParameter("temp.roleBean.father.id");
			if(StringUtils.isNotBlank(fatherid)){
				fatherid = fatherid.split(";")[0];
				if(null != roleBean.getFather())
				{
					//���ĸ��ڵ�
					if(!fatherid.equals(roleBean.getFather().getId()))
						roleBean.setFather((TblCmnRoleOrg)getService().load(TblCmnRoleOrg.class, fatherid));
				}
				else
				{
					//�������ڵ�
					roleBean.setFather((TblCmnRoleOrg)getService().load(TblCmnRoleOrg.class, fatherid));
				}
			}

		}
		return roleBean;
	}


	public void setRoleBean(TblCmnRole roleBean) {
		this.roleBean = roleBean;
	}

	public String getStrCreate() {
		return strCreate;
	}

	public void setStrCreate(String strCreate) {
		this.strCreate = strCreate;
	}

	public String getStrFlg() {
		return strFlg;
	}

	public void setStrFlg(String strFlg) {
		this.strFlg = strFlg;
	}

	public ComboSupportList getStatusComboList() {
		if(null == statusComboList)
		{
			statusComboList = new  ComboSupportList(TblCmnUser.getStatusMap());
		}		
		return statusComboList;
	}
	
	public void setStatusComboList(ComboSupportList statusComboList) {
		this.statusComboList = statusComboList;
	}

	public TblCmnPermission getCmnPermission() {
		return cmnPermission;
	}

	public void setCmnPermission(TblCmnPermission cmnPermission) {
		this.cmnPermission = cmnPermission;
	}



	public TblCmnRolePermission getCmnRolePermission() {
		if (cmnRolePermission == null) {
			if (StringUtils.isBlank(getOid())) {
				cmnRolePermission = new TblCmnRolePermission();
			}
			else {
				cmnRolePermission = (TblCmnRolePermission)getService().load(TblCmnRolePermission.class, getOid());
			}
		}
		return cmnRolePermission;
	}

	public void setCmnRolePermission(TblCmnRolePermission cmnRolePermission) {
		this.cmnRolePermission = cmnRolePermission;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getRoleCd() {
		return roleCd;
	}

	public void setRoleCd(String roleCd) {
		this.roleCd = roleCd;
	}

	public String getSystemRole() {
		return systemRole;
	}

	public void setSystemRole(String systemRole) {
		this.systemRole = systemRole;
	}

	public List getOrgList() {
		return orgList;
	}

	public void setOrgList(List orgList) {
		this.orgList = orgList;
	}

	public List getRolList() {
		return rolList;
	}

	public void setRolList(List rolList) {
		this.rolList = rolList;
	}

	public String getOrgRoleFlg() {
		if(null == orgRoleFlg)
		{
			String flg = request.getParameter("flg");
			if("role".equals(flg))
			{
				orgRoleFlg="role";
			}
			else
			{
				orgRoleFlg="org";
			}
			
		}
		return orgRoleFlg;
	}

	public void setOrgRoleFlg(String orgRoleFlg) {
		this.orgRoleFlg = orgRoleFlg;
	}

	public ComboSupportList getReadAccessLevelComboList() {
		if(null == readAccessLevelComboList)
		{
			readAccessLevelComboList = new  ComboSupportList(TblCmnRolePermission.getReadAccessLevelMap());
		}		
		
		return readAccessLevelComboList;
	}

	public void setReadAccessLevelComboList(
			ComboSupportList readAccessLevelComboList) {
		this.readAccessLevelComboList = readAccessLevelComboList;
	}

	public ComboSupportList getRwCtrlComboList() {
		if(null == rwCtrlComboList)
		{
			rwCtrlComboList = new  ComboSupportList(TblCmnRolePermission.getRwCtrlMap());
		}		
		return rwCtrlComboList;
	}

	public void setRwCtrlComboList(ComboSupportList rwCtrlComboList) {
		this.rwCtrlComboList = rwCtrlComboList;
	}

	public ComboSupportList getWriteAccessLevelComboList() {
		if(null == writeAccessLevelComboList)
		{
			writeAccessLevelComboList = new  ComboSupportList(TblCmnRolePermission.getWriteAccessLevelMap());
		}		
		return writeAccessLevelComboList;
	}

	public void setWriteAccessLevelComboList(
			ComboSupportList writeAccessLevelComboList) {
		this.writeAccessLevelComboList = writeAccessLevelComboList;
	}

	public ComboSupportList getSystemRoleComboList() {
		systemRoleComboList = new  ComboSupportList(TblCmnRole.getSystemRoleMap());
		return systemRoleComboList;
	}

	public void setSystemRoleComboList(ComboSupportList systemRoleComboList) {
		this.systemRoleComboList = systemRoleComboList;
	}

	public String getOldParentCode() {
		return oldParentCode;
	}

	public void setOldParentCode(String oldParentCode) {
		this.oldParentCode = oldParentCode;
	}

	public ComboSupportList getMenuRoleComboList() {
		menuRoleComboList = new  ComboSupportList(getMenuRoleMap());
		return menuRoleComboList;
	}

	public void setMenuRoleComboList(ComboSupportList menuRoleComboList) {
		this.menuRoleComboList = menuRoleComboList;
	}

	public static Map getMenuRoleMap() {
		return menuRoleMap;
	}

	public static void setMenuRoleMap(Map menuRoleMap) {
		RoleForm.menuRoleMap = menuRoleMap;
	}

	public String getTypevalue() {
		return typevalue;
	}

	public void setTypevalue(String typevalue) {
		this.typevalue = typevalue;
	}

	public ComboSupportList getUrltypeComboList() {
		if(null == urltypeComboList)
		{
			urltypeComboList = new  ComboSupportList(TblCmnPermission.getUrltypeMap());
		}		
		
		return urltypeComboList;
	}
	
	public ComboSupportList getNodetypeComboList() {
		if(null == nodetypeComboList)
		{
			nodetypeComboList = new  ComboSupportList(TblCmnPermission.getNodeTypeMap());
		}		
		
		
		return nodetypeComboList;
	}

	public void setNodetypeComboList(ComboSupportList nodetypeComboList) {
		this.nodetypeComboList = nodetypeComboList;
	}

	public void setUrltypeComboList(ComboSupportList urltypeComboList) {
		this.urltypeComboList = urltypeComboList;
	}

	public String getSearchFatherName() {
		return searchFatherName;
	}

	public void setSearchFatherName(String searchFatherName) {
		this.searchFatherName = searchFatherName;
	}

	public String getConsignFlg() {
		return consignFlg;
	}

	public void setConsignFlg(String consignFlg) {
		this.consignFlg = consignFlg;
	}
}
