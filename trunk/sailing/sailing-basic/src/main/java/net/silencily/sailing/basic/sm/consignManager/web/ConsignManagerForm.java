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
	 * ��ͨ��
	 */
	private List list;
	
	/**
	 * ��ɫ����
	 */
	private TblCmnRole roleBean;
	
	/**
	 * ��ί�ж���IDS
	 */
	private String consignId;
	
	/**
	 * �û�bean
	 */
	private TblCmnUser userBean;
	
	/**
	 * ί���û���Ȩ��
	 */
	TblCmnUserPermission userPermissionBean;
	
	/**
	 * ί���û��Ľ�ɫ
	 */
	TblCmnUserRole userRoleBean;
	
	/**
	 * ���ڵ�
	 */
	private String parentCode;


	/**��ͬ  */
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
