package net.silencily.sailing.basic.sm.permission.web;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserPermission;
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
 * @version $Id: PermissionForm.java,v 1.1 2010/12/10 10:56:30 silencily Exp $
 * @since 2007-8-29
 */
public class PermissionForm extends BaseFormPlus {
	

	private List list;
	private TblCmnPermission bean;
	private String parentCode;
	private String roleName;
	private String roleId;
	private String oldParentCode;
	private ComboSupportList nodetypeComboList;
	private ComboSupportList urltypeComboList;
	private ComboSupportList systemPermissionComboList;
	private ComboSupportList readAccessLevelComboList = null;
	private ComboSupportList writeAccessLevelComboList = null;
	private ComboSupportList rwCtrlComboList = null;
	private String menuDataFlg;
	private String strCreate;
	private String permissionCd;
	private String url;
	private List perRole;
	private List perUser;
	private ComboSupportList statusComboList = null;
	private String fatherName;
	private String currentFatherName;
	private String pageUpDlg;

	/**
	 * ���{@link THrDept}��ʵ��, �Ա�<code>web tier framework</code>���Կ�ʼ
	 * ��װ����ҳ��Ĳ���, ҳ�����������Ҫ�޸ĵĲ��������ǰ����е����Զ�������ҳ���<code>hidden field</code> ��,
	 * ����<code>java</code>��������ͨ���е�һ�ַ���, ��Ȼ���������ķ���, �����Ը���һЩ,
	 * �������ǽ����������ִ����ȵ�����Ч�İ취
	 * 
	 * @return {@link THrDept}��ʵ��
	 */
	public TblCmnPermission getBean() {
		if (bean == null) {
			if (StringUtils.isBlank(getOid())) {
				bean = new TblCmnPermission();
			}
			else {
				bean = (TblCmnPermission)getCommonService().load(TblCmnPermission.class, getOid());
			}
		}
		return bean;
	}

	public void setBean(TblCmnPermission bean) {
		this.bean = bean;
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
		if (parentCode == null || "".equals(parentCode)) {
			parentCode = request.getParameter("parentCode");
		}
		if (parentCode == null || "".equals(parentCode)) {
			parentCode = PermissionAction.service().getInitRoot().getId();
		}

		return parentCode;
	}

	public void setParentCode(String parentId) {
		this.parentCode = parentId;
	}
	
	private CommonService getCommonService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
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

	public ComboSupportList getSystemPermissionComboList() {
		if(null == systemPermissionComboList)
		{
			systemPermissionComboList 
				= new  ComboSupportList(TblCmnPermission.getSystemPermissionMap());
		}		
		
		return systemPermissionComboList;
	}

	public void setSystemPermissionComboList(
			ComboSupportList systemPermissionComboList) {
		this.systemPermissionComboList = systemPermissionComboList;
	}

	public ComboSupportList getUrltypeComboList() {
		if(null == urltypeComboList)
		{
			urltypeComboList = new  ComboSupportList(TblCmnPermission.getUrltypeMap());
		}		
		
		return urltypeComboList;
	}

	public void setUrltypeComboList(ComboSupportList urltypeComboList) {
		this.urltypeComboList = urltypeComboList;
	}

	public String getMenuDataFlg() {
		return menuDataFlg;
	}

	public void setMenuDataFlg(String menuDataFlg) {
		this.menuDataFlg = menuDataFlg;
	}

	public String getStrCreate() {
		return strCreate;
	}

	public void setStrCreate(String strCreate) {
		this.strCreate = strCreate;
	}

	public String getOldParentCode() {
		return oldParentCode;
	}

	public void setOldParentCode(String oldParentCode) {
		this.oldParentCode = oldParentCode;
	}

	public String getPermissionCd() {
		return permissionCd;
	}

	public void setPermissionCd(String permissionCd) {
		this.permissionCd = permissionCd;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getCurrentFatherName() {
		return currentFatherName;
	}

	public void setCurrentFatherName(String currentFatherName) {
		this.currentFatherName = currentFatherName;
	}

	public String getPageUpDlg() {
		return pageUpDlg;
	}

	public void setPageUpDlg(String pageUpDlg) {
		this.pageUpDlg = pageUpDlg;
	}
	
	/**
	 * 
	 * �������� ��ȡֻ�����ݷ��ʼ��������б������Դ
	 * @return
	 * 2007-11-30 ����01:34:23
	 * @version 1.0
	 * @author baihe
	 */
	public ComboSupportList getReadAccessLevelComboList() {
		if(null == readAccessLevelComboList)
		{
			readAccessLevelComboList = new  ComboSupportList(TblCmnUserPermission.getReadAccessLevelMap());
		}
		return readAccessLevelComboList;
	}

	public void setReadAccessLevelComboList(ComboSupportList readAccessLevelComboList) {
		this.readAccessLevelComboList = readAccessLevelComboList;
	}
	
	/**
	 * 
	 * �������� ��ȡ�ɱ༭���ݷ��ʼ��������б������Դ
	 * @return
	 * 2007-11-30 ����01:35:18
	 * @version 1.0
	 * @author baihe
	 */
	public ComboSupportList getWriteAccessLevelComboList() {
		if(null == writeAccessLevelComboList)
		{
			writeAccessLevelComboList = new  ComboSupportList(TblCmnUserPermission.getWriteAccessLevelMap());
		}
		return writeAccessLevelComboList;
	}

	public void setWriteAccessLevelComboList(ComboSupportList writeAccessLevelComboList) {
		this.writeAccessLevelComboList = writeAccessLevelComboList;
	}
	
	/**
	 * 
	 * �������� ��ȡ��д���������б������Դ
	 * @return
	 * 2007-11-30 ����01:36:29
	 * @version 1.0
	 * @author baihe
	 */
	public ComboSupportList getRwCtrlComboList() {
		if(null == rwCtrlComboList)
		{
			rwCtrlComboList = new  ComboSupportList(TblCmnUserPermission.getRwCtrlMap());
		}
		return rwCtrlComboList;
	}

	public void setRwCtrlComboList(ComboSupportList rwCtrlComboList) {
		this.rwCtrlComboList = rwCtrlComboList;
	}

	public List getPerRole() {
		return perRole;
	}

	public void setPerRole(List perRole) {
		this.perRole = perRole;
	}

	public List getPerUser() {
		return perUser;
	}

	public void setPerUser(List perUser) {
		this.perUser = perUser;
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
