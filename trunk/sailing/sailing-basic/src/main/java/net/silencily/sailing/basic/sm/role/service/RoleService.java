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
 * ��֯��������, ���еķ���������<code>null</code>
 * 
 * @author gaojing
 * @version $Id: RoleService.java,v 1.1 2010/12/10 10:56:45 silencily Exp $
 * @since 2007-8-29
 */
public interface RoleService extends ServiceBase {

	
	 String SERVICE_NAME = "sm.roleService";
	 
	 
	 /**
	  * ��ɫ:Ȩ��-�û���Ϣȡ��
	  * 
	  * @param config
	  * @param radio
	  * @return
	  */
	List list(TblCmnRole config,String strFlg);
	
	/**
	 * 
	 * ��������
	 * @param fatherid
	 * @param subid
	 * @return
	 * 2007-12-10 ����08:21:46
	 * @version 1.0
	 * @author wanghy
	 */
	boolean isSubNode(String fatherid ,String subid);
	

	/**
	 * 
	 * �������� ȡ�û���ɫ��Ϣ
	 * @param tblCmnUser
	 * @param tblCmnRole
	 * @return
	 * 2007-12-6 ����06:15:32
	 * @version 1.0
	 * @author wanghy
	 */
	List getUserRole(TblCmnUser tblCmnUser, TblCmnRole tblCmnRole);
	
	/**
	 * 
	 * �������� ȡ�ý�ɫ
	 * @param tblCmnUser
	 * @return
	 * 2007-12-6 ����07:20:22
	 * @version 1.0
	 * @author wanghy
	 */
	List getRole(TblCmnUser tblCmnUser);
	
	 /**
	  * ��֯�ṹȡ��
	  * @param oid
	  * @return
	  */
	 List getChildren(String oid, RoleForm form);	

	 /**
	  * ��֯�ṹȡ��
	  * @param oid
	  * @return
	  */
	 List getOrgMenu(String oid,RoleForm form);
	 

	/**
	 * ��ȡ��ʼ���ڵ�
	 * @return
	 */
	public TblCmnRoleOrg getInitRoot();


	/**
	 * ��ȡ��ʼ���ڵ�
	 * @return
	 */
	public TblCmnRoleOrg getRoot();	
	
	
	/**
	 * 
	 * �������� ί�н�ɫȡ��
	 * @param bean
	 * @return
	 * 2007-11-28 ����05:40:07
	 * @version 1.0
	 * @author wanghy
	 */
	public List getUserRoleConsignedUser(TblCmnUser consignerBean, TblCmnUser userBean);
	
	/**
	 * Ŀ¼��ѯ������ҳ����������Ӹ��ڵ㿪ʼ��ѯ���ڵ㼰�������ӽڵ��µĽ�ɫĿ¼��¼
	 * ֧�ַ�ҳ
	 * ��������
	 * @param foid
	 * @return
	 * 2007-12-6 ����04:45:44
	 * @version 1.0
	 * @author yushn
	 */
	public List queryOrgStartWithFatherId(String foid);
	/**
	 * ��ɫ��ѯ������ҳ����������Ӹ��ڵ㿪ʼ��ѯ���ڵ㼰�������ӽڵ��µĽ�ɫ��¼
	 * ֧�ַ�ҳ
	 * ��������
	 * @param foid
	 * @return
	 * 2007-12-6 ����04:45:44
	 * @version 1.0
	 * @author yushn
	 */
	public List queryRoleStartWithFatherId(String foid,String fname);
	
	public List queryRoleStartWithFatherId(String foid);
	/**
	 * ���ݽ�ɫ��ʶ��ȡ��ɫ�б�
	 * @param c
	 * @return
	 */
	public List findRolesByRoleCds(Collection c);

	public List getRoleOrgChildren();

	public List getRoleChildren();
	
	/**
	 * ��ָ��Ŀ¼�µ����й���Ȩ�޼Ӹ���ɫ
	 * @param foderid	Ȩ��Ŀ¼id
	 * @param role		��ɫ����
	 * @param rwCtrl	
	 * @param readAccessLevel
	 * @param writeAccessLevel
	 * @return
	 */
	public int addFolerPermissions(String foderid,
			TblCmnRole role,String rwCtrl,String readAccessLevel,String writeAccessLevel);
	/**
	 * �жϴ���Ľ�ɫ�������Ƿ��н�ɫ����ָ���Ľ�ɫĿ¼
	 * @param orgid
	 * @param roles
	 * @return
	 */
	public boolean isAnyRoleInRoleOrg(String orgid,Collection roles);
	
	/**
	 * ����ɫ���ӹ���Ȩ����ص�������Ȩ�ޡ�
	 * @param rp
	 * @return
	 */
	public int addFiledPermissionsForFunPermission(TblCmnRolePermission rp);

	String saveOrg(TblCmnRoleOrg children, RoleForm roleForm, String fatherid);
	
}
