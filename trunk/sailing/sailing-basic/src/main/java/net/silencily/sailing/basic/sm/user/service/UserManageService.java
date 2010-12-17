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
	 * �������� ������������״̬
	 * @param str
	 * @return
	 * 2007-11-23 ����07:57:21
	 * @version 1.0
	 * @author baihe
	 */
	public List batchjihuo(List str);
	
	/**
	 * 
	 * �������� ������������״̬
	 * @param str
	 * @return
	 * 2007-11-23 ����07:57:37
	 * @version 1.0
	 * @author baihe
	 */
	public List batchjinyong(List str);

	/**
	 * 
	 * �������� ����Ȩ���б���Ϣ
	 * @param bean
	 * @return
	 * 2007-11-23 ����07:58:19
	 * @version 1.0
	 * @author baihe
	 */
	public List getPermissions(TblCmnUser bean);
	
	/**
	 * 
	 * �������� ��������Ȩ���б���Ϣ
	 * @param bean
	 * @return
	 * 2007-11-23 ����07:58:19
	 * @version 1.0
	 * @author baihe
	 */
	public List getDataPermission(TblCmnUser bean);
	
	/**
	 * 
	 * �������� ���ؽ�ɫ�б���Ϣ
	 * @param bean
	 * @return
	 * 2007-11-23 ����07:58:38
	 * @version 1.0
	 * @author baihe
	 */
	public List getRole(TblCmnUser bean);
	
	/**
	 * 
	 * �������� ���ؽ�ɫ��Ȩ�޹����б���Ϣ
	 * @param bean
	 * @return
	 * 2007-11-26 ����05:33:38
	 * @version 1.0
	 * @author baihe
	 */
	public List getRolePermissions(TblCmnRole bean);
	
	/**
	 * 
	 * ��������	���ؽ�ɫ���û������б���Ϣ
	 * @param bean
	 * @return
	 * 2007-11-28 ����05:28:33
	 * @version 1.0
	 * @author baihe
	 */
	public List getRoleUser(TblCmnRole bean);
	
	/**
	 * 
	 * �������� �߼�ɾ���û������û�������Ȩ�ޡ���ɫ����Ϣ����
	 * @param bean
	 * 2007-11-29 ����01:33:14
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
