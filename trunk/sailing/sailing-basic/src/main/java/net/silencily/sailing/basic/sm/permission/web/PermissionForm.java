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
 * 组织机构维护<code>form</code>, 注意其中的{@link #getBean()}是核心所在, 利用这个模式
 * 我们可以极大地简化从一个请求组装<code>domain object</code>的过程
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
	 * 获得{@link THrDept}的实例, 以便<code>web tier framework</code>可以开始
	 * 组装来自页面的参数, 页面仅仅处理需要修改的参数而不是把所有的属性都体现在页面的<code>hidden field</code> 中,
	 * 这是<code>java</code>社区最普通流行的一种方案, 当然还有其它的方案, 但是稍复杂一些,
	 * 所以我们仅仅采用这种粗粒度但简单有效的办法
	 * 
	 * @return {@link THrDept}的实例
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
	 * 在调用这个方法时, <code>web tier framework</code>可能还没有开始组装{@link #parentCode}
	 * 这个属性, 也就是说从{@link #getParentCode()}不能返回请求参数"parentCode"的值, 我们主动从
	 * <code>request</code>中获取这个值, 以便方法{@link #getBean()}可以正确执行
	 * </p>
	 * <p>
	 * 尽管不是每一个功能都需要这个参数, 但是统一地处理可以简化编程
	 * </p>
	 * 
	 * @return 请求参数<code>parentCode</code>的值
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
	 * 功能描述 获取只读数据访问级别下拉列表框数据源
	 * @return
	 * 2007-11-30 下午01:34:23
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
	 * 功能描述 获取可编辑数据访问级别下拉列表框数据源
	 * @return
	 * 2007-11-30 下午01:35:18
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
	 * 功能描述 获取读写控制下拉列表框数据源
	 * @return
	 * 2007-11-30 下午01:36:29
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
