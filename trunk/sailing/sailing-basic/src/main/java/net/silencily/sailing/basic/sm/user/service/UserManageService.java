package net.silencily.sailing.basic.sm.user.service;

import java.sql.SQLException;
import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.sm.domain.TblCmnUserPermission;
import net.silencily.sailing.framework.core.ServiceBase;
/**
 * 
 * 
 * @author baihe
 * @version 1.0
 */
public interface UserManageService extends ServiceBase {
	/**
	 * 
	 */
	public static final String SERVICE_NAME = "sm.userManageService";
	
	/**
	 * 
	 * 功能描述 物理批量激活状态
	 * @param str
	 * @return
	 * 2007-11-23 下午07:57:21
	 * @version 1.0
	 * @author baihe
	 */
	public List batchjihuo(List str);
	
	/**
	 * 
	 * 功能描述 物理批量禁用状态
	 * @param str
	 * @return
	 * 2007-11-23 下午07:57:37
	 * @version 1.0
	 * @author baihe
	 */
	public List batchjinyong(List str);

	/**
	 * 
	 * 功能描述 返回权限列表信息
	 * @param bean
	 * @return
	 * 2007-11-23 下午07:58:19
	 * @version 1.0
	 * @author baihe
	 */
	public List getPermissions(TblCmnUser bean);
	
	/**
	 * 
	 * 功能描述 返回数据权限列表信息
	 * @param bean
	 * @return
	 * 2007-11-23 下午07:58:19
	 * @version 1.0
	 * @author baihe
	 */
	public List getDataPermission(TblCmnUser bean);
	
	/**
	 * 
	 * 功能描述 返回角色列表信息
	 * @param bean
	 * @return
	 * 2007-11-23 下午07:58:38
	 * @version 1.0
	 * @author baihe
	 */
	public List getRole(TblCmnUser bean);
	
	/**
	 * 
	 * 功能描述 返回角色与权限关联列表信息
	 * @param bean
	 * @return
	 * 2007-11-26 下午05:33:38
	 * @version 1.0
	 * @author baihe
	 */
	public List getRolePermissions(TblCmnRole bean);
	
	/**
	 * 
	 * 功能描述	返回角色与用户关联列表信息
	 * @param bean
	 * @return
	 * 2007-11-28 下午05:28:33
	 * @version 1.0
	 * @author baihe
	 */
	public List getRoleUser(TblCmnRole bean);
	
	/**
	 * 
	 * 功能描述 逻辑删除用户及与用户关联的权限、角色、消息配置
	 * @param bean
	 * 2007-11-29 下午01:33:14
	 * @version 1.0
	 * @author baihe
	 */
	public void deleteUser(TblCmnUser bean);
	
	public List getUsersByRolesWithAutoCondtionAndPaginater(List roles);
	
	public List getUsersByRoleCds(List roleCds);
	
	public List getUsersByRoles(List roles);

	public List getUsersByRolesWithAutoCondtionAndPaginater(List roles, String deptId);

//	public void deletePermission(TblCmnUserPermission userPermission);

//	public void deleteRole(TblCmnUserRole userRole);
	
	public boolean validateUserInfo(String empCd,String psd);
	
	public TblCmnUser getUser(String empCd);

	public List getUserListForConsign(String currentUserId); 
	
	public int addFolerPermissions(String foderid,
			TblCmnUser user,String rwCtrl,String readAccessLevel,String writeAccessLevel);
	
	public int addFiledPermissionsForFunPermission(TblCmnUserPermission up);

	public TblCmnRole getBaseRole();
	
	public TblCmnUser newInstance();
	
	public List getSubDept(String deptID) throws SQLException ;

}
