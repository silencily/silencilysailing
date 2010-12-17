package net.silencily.sailing.basic.sm.consignManager.web;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUserRole;
import net.silencily.sailing.basic.sm.role.web.RoleAction;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.apache.commons.lang.StringUtils;


public class ConsignManagerForm extends BaseFormPlus{

	/**
	 * 共通用
	 */
	private List list;
	
	/**
	 * 角色数据
	 */
	private TblCmnRole roleBean;
	
	/**
	 * 被委托对象IDS
	 */
	private String consignId;
	
	/**
	 * 用户bean
	 */
	private TblCmnUser userBean;
	
	/**
	 * 委托用户的权限
	 */
	TblCmnUserPermission userPermissionBean;
	
	/**
	 * 委托用户的角色
	 */
	TblCmnUserRole userRoleBean;
	
	/**
	 * 父节点
	 */
	private String parentCode;


	/**共同  */
	private CommonService getService() {
		return (CommonService)ServiceProvider.getService(CommonService.SERVICE_NAME);
	}
	
	public List getList() {
		return list;
	}


	public void setList(List list) {
		this.list = list;
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
		if (parentCode == null) {
			parentCode = request.getParameter("parentCode");
		}
		if (parentCode == null) {
			parentCode = RoleAction.service().getInitRoot().getId();
		}
		return parentCode;
	}

	public void setParentCode(String parentId) {
		this.parentCode = parentId;
	}


	public TblCmnUser getUserBean() {
		return userBean;
	}


	public void setUserBean(TblCmnUser userBean) {
		this.userBean = userBean;
	}


	public String getConsignId() {
		return consignId;
	}


	public void setConsignId(String consignId) {
		this.consignId = consignId;
	}


	public TblCmnUserPermission getUserPermissionBean() {
		if (userPermissionBean == null) {
			if (StringUtils.isBlank(getOid())) {
				userPermissionBean = new TblCmnUserPermission();
			}
			else {
				userPermissionBean = (TblCmnUserPermission)getService().load(TblCmnUserPermission.class, getOid());
			}
		}
		return userPermissionBean;
	}


	public void setUserPermissionBean(TblCmnUserPermission userPermissionBean) {
		this.userPermissionBean = userPermissionBean;
	}

	public TblCmnRole getRoleBean() {
		if (roleBean == null) {
			if (StringUtils.isBlank(getOid())) {
				roleBean = new TblCmnRole();
			}
			else {
				roleBean = (TblCmnRole)getService().load(TblCmnRole.class, getOid());
			}
		}
		return roleBean;
	}


	public void setRoleBean(TblCmnRole roleBean) {
		this.roleBean = roleBean;
	}

	public TblCmnUserRole getUserRoleBean() {
		
		if (userRoleBean == null) {
			if (StringUtils.isBlank(getOid())) {
				userRoleBean = new TblCmnUserRole();
			}
			else {
				userRoleBean = (TblCmnUserRole)getService().load(TblCmnUserRole.class, getOid());
			}
		}
		return userRoleBean;
	}


	public void setUserRoleBean(TblCmnUserRole userRoleBean) {
		this.userRoleBean = userRoleBean;
	}

	
}
