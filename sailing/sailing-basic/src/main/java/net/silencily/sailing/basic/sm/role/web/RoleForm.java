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
 * 组织机构维护<code>form</code>, 注意其中的{@link #getBean()}是核心所在, 利用这个模式
 * 我们可以极大地简化从一个请求组装<code>domain object</code>的过程
 * 
 * @author gaojing
 * @version $Id: RoleForm.java,v 1.1 2010/12/10 10:56:42 silencily Exp $
 * @since 2007-8-29
 */
public class RoleForm extends BaseFormPlus {
	
	private static Map menuRoleMap = new HashMap();
	static{
		menuRoleMap.put("0", "目录");
		menuRoleMap.put("1", "角色");
	}
	/**
	 * 委托角色标记
	 */
	private String consignFlg;
	
	private String typevalue = "0";
	/**
	 * LIST信息
	 */
	private List list;
	
	/**
	 * 角色
	 */
	private List rolList;
	
	/**
	 * 组织
	 */
	private List orgList;

	/**
	 * 角色数据
	 */
	private TblCmnRole roleBean;
	
	/**
	 *  数据访问级别
	 */
	private ComboSupportList readAccessLevelComboList = null;
	
	private ComboSupportList urltypeComboList;
	
	/**
	 *写级别
	 */
	private ComboSupportList writeAccessLevelComboList = null;
	
	/**
	 * 读写控制
	 */
	private ComboSupportList rwCtrlComboList = null;
	
	private ComboSupportList nodetypeComboList;
	
	/**
	 * 角色组织数据
	 */
	private TblCmnRoleOrg orgBean;
	
	private TblCmnRolePermission cmnRolePermission;
	
	/**
	 * 角色权限信息
	 */
	private TblCmnPermission cmnPermission;
	/**
	 * 角色类型 
	 */
	private ComboSupportList statusComboList = null;
	
	/**
	 * 系统角色
	 */
	private ComboSupportList systemRoleComboList = null;
	
	/**
	 * 类型
	 */
	private ComboSupportList menuRoleComboList = null;
	
	/**
	 * 父节点名称
	 */
	private String fatherName;
	
	/**
	 * 父节点名称(查询用)
	 */
	private String searchFatherName;
	
	/**
	 * 显示名称
	 */
	private String displayName;

	/**
	 * 同层序号
	 */
	private String displayOrder;
	
	/**
	 * 角色标识(角色）
	 */
	private String roleCd;
	
	/**
	 * 系统权限(角色）
	 */
	private String systemRole;
	
	/**
	 * 父节点
	 */
	private String parentCode;
	
	/**
	 * 原来的父节点
	 */
	private String  oldParentCode;
	
	private String  strFlg;
	
	/**
	 * 组织/角色区分
	 */
	private String orgRoleFlg;
	
	/**
	 * 新建识别.
	 */
	private String strCreate;
	
	/**
	 * 
	 * 功能描述 共通service
	 * @return
	 * 2007-12-7 下午01:40:10
	 * @version 1.0
	 * @author wanghy
	 */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
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
	 * 获得{@link THrDept}的实例, 以便<code>web tier framework</code>可以开始
	 * 组装来自页面的参数, 页面仅仅处理需要修改的参数而不是把所有的属性都体现在页面的<code>hidden field</code> 中,
	 * 这是<code>java</code>社区最普通流行的一种方案, 当然还有其它的方案, 但是稍复杂一些,
	 * 所以我们仅仅采用这种粗粒度但简单有效的办法
	 * 
	 * @return {@link THrDept}的实例
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
					//更改父节点
					if(!fatherid.equals(orgBean.getFather().getId()))
						orgBean.setFather((TblCmnRoleOrg)getService().load(TblCmnRoleOrg.class, fatherid));
				}
				else
				{
					//新增父节点
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
					//更改父节点
					if(!fatherid.equals(roleBean.getFather().getId()))
						roleBean.setFather((TblCmnRoleOrg)getService().load(TblCmnRoleOrg.class, fatherid));
				}
				else
				{
					//新增父节点
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
