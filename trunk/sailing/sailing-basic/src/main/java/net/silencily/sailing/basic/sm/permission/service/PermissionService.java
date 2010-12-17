package net.silencily.sailing.basic.sm.permission.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * 组织机构管理, 所有的方法不返回<code>null</code>
 * 
 * @author gaojing
 * @version $Id: PermissionService.java,v 1.1 2010/12/10 10:56:48 silencily Exp $
 * @since 2007-8-29
 */
public interface PermissionService extends ServiceBase {
	

	String SERVICE_NAME = "sm.permissionService";
	
	/**
	 * 获取初始根节点
	 * @return
	 */
	public TblCmnPermission getInitRoot();


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
	 * 获取根节点
	 * @return
	 */
	TblCmnPermission getRoot();
	
	/**
	 * 列出指定节点下一级所有配置信息
	 * 
	 * @param config 列出这个节点下的配置
	 * @return 指定节点下一级所有配置信息, 元素类型是{@link TblCmnPermission}
	 */
	List getChildren(String oid);
	/**
	 * 列出指定节点下一级指定类型的孩子节点
	 * @param oid
	 * @param nodetype	孩子节点类型
	 * @return
	 */
	List getChildren(String oid,String[] nodetypes);
	/**
	 * 根据页面的条件，从父节点开始查询父节点及其所有子节点下的记录
	 * 支持分页
	 * 功能描述
	 * @param foid
	 * @return
	 * 2007-12-6 下午04:45:44
	 * @version 1.0
	 * @author yushn
	 */
	List queryStartWithFatherId(String foid ,String fname);
	/**
	 * 
	 * 功能描述 取角色权限关联
	 * @param bean
	 * @return
	 * 2007-12-5 下午06:15:35
	 * @version 1.0
	 * @author wanghy
	 */
	List getRolePermision(TblCmnPermission bean);

	/**
	 * 
	 * 功能描述 取用户权限关联
	 * @param bean
	 * @return
	 * 2007-12-5 下午06:16:33
	 * @version 1.0
	 * @author wanghy
	 */
	List getUserPermision(TblCmnPermission bean);
	/**
	 * 
	 * 功能描述 委托权限取得
	 * @param bean
	 * @return
	 * 2007-11-28 下午05:40:07
	 * @version 1.0
	 * @author wanghy
	 */
	public List getUserPermissionConsignedUser(TblCmnUser consignerBean, TblCmnUser userBean);

	/**
	 * 
	 * 功能描述 判定用
	 * @param role
	 * @param strFlg
	 * @return
	 * 2007-12-5 下午06:06:27
	 * @version 1.0
	 * @author wanghy
	 */
	public List onlyOneFlg(String permissionCd, String fatherId);
	
	/**
	 * 判断相同url的功能权限是否存在
	 * @param url
	 * @return
	 */
	public boolean existUrl(String url);


	public List getPerRole(TblCmnPermission bean);


	public List getPerUser(TblCmnPermission bean);


	public List getRoleUser(TblCmnRole role);

}
