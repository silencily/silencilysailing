package net.silencily.sailing.basic.sm.role.service;

import java.util.Collection;
import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnRoleOrg;
import net.silencily.sailing.basic.sm.domain.TblCmnRolePermission;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.role.web.RoleForm;
import net.silencily.sailing.framework.core.ServiceBase;

/**
 * 组织机构管理, 所有的方法不返回<code>null</code>
 * 
 * @author gaojing
 * @version $Id: RoleService.java,v 1.1 2010/12/10 10:56:45 silencily Exp $
 * @since 2007-8-29
 */
public interface RoleService extends ServiceBase {

	
	 String SERVICE_NAME = "sm.roleService";
	 
	 
	 /**
	  * 角色:权限-用户信息取得
	  * 
	  * @param config
	  * @param radio
	  * @return
	  */
	List list(TblCmnRole config,String strFlg);
	
	/**
	 * 
	 * 功能描述
	 * @param fatherid
	 * @param subid
	 * @return
	 * 2007-12-10 下午08:21:46
	 * @version 1.0
	 * @author wanghy
	 */
	boolean isSubNode(String fatherid ,String subid);
	

	/**
	 * 
	 * 功能描述 取用户角色信息
	 * @param tblCmnUser
	 * @param tblCmnRole
	 * @return
	 * 2007-12-6 下午06:15:32
	 * @version 1.0
	 * @author wanghy
	 */
	List getUserRole(TblCmnUser tblCmnUser, TblCmnRole tblCmnRole);
	
	/**
	 * 
	 * 功能描述 取得角色
	 * @param tblCmnUser
	 * @return
	 * 2007-12-6 下午07:20:22
	 * @version 1.0
	 * @author wanghy
	 */
	List getRole(TblCmnUser tblCmnUser);
	
	 /**
	  * 组织结构取得
	  * @param oid
	  * @return
	  */
	 List getChildren(String oid, RoleForm form);	

	 /**
	  * 组织结构取得
	  * @param oid
	  * @return
	  */
	 List getOrgMenu(String oid,RoleForm form);
	 

	/**
	 * 获取初始根节点
	 * @return
	 */
	public TblCmnRoleOrg getInitRoot();


	/**
	 * 获取初始根节点
	 * @return
	 */
	public TblCmnRoleOrg getRoot();	
	
	
	/**
	 * 
	 * 功能描述 委托角色取得
	 * @param bean
	 * @return
	 * 2007-11-28 下午05:40:07
	 * @version 1.0
	 * @author wanghy
	 */
	public List getUserRoleConsignedUser(TblCmnUser consignerBean, TblCmnUser userBean);
	
	/**
	 * 目录查询，根据页面的条件，从父节点开始查询父节点及其所有子节点下的角色目录记录
	 * 支持分页
	 * 功能描述
	 * @param foid
	 * @return
	 * 2007-12-6 下午04:45:44
	 * @version 1.0
	 * @author yushn
	 */
	public List queryOrgStartWithFatherId(String foid);
	/**
	 * 角色查询，根据页面的条件，从父节点开始查询父节点及其所有子节点下的角色记录
	 * 支持分页
	 * 功能描述
	 * @param foid
	 * @return
	 * 2007-12-6 下午04:45:44
	 * @version 1.0
	 * @author yushn
	 */
	public List queryRoleStartWithFatherId(String foid,String fname);
	
	public List queryRoleStartWithFatherId(String foid);
	/**
	 * 根据角色标识获取角色列表
	 * @param c
	 * @return
	 */
	public List findRolesByRoleCds(Collection c);

	public List getRoleOrgChildren();

	public List getRoleChildren();
	
	/**
	 * 将指定目录下的所有功能权限加给角色
	 * @param foderid	权限目录id
	 * @param role		角色对象
	 * @param rwCtrl	
	 * @param readAccessLevel
	 * @param writeAccessLevel
	 * @return
	 */
	public int addFolerPermissions(String foderid,
			TblCmnRole role,String rwCtrl,String readAccessLevel,String writeAccessLevel);
	/**
	 * 判断传入的角色集合中是否有角色属于指定的角色目录
	 * @param orgid
	 * @param roles
	 * @return
	 */
	public boolean isAnyRoleInRoleOrg(String orgid,Collection roles);
	
	/**
	 * 给角色增加功能权限相关的数据项权限。
	 * @param rp
	 * @return
	 */
	public int addFiledPermissionsForFunPermission(TblCmnRolePermission rp);

	String saveOrg(TblCmnRoleOrg children, RoleForm roleForm, String fatherid);
	
}
