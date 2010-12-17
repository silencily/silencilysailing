package net.silencily.sailing.basic.sm.permission.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnPermission;
import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * ��֯��������, ���еķ���������<code>null</code>
 * 
 * @author gaojing
 * @version $Id: PermissionService.java,v 1.1 2010/12/10 10:56:48 silencily Exp $
 * @since 2007-8-29
 */
public interface PermissionService extends ServiceBase {
	

	String SERVICE_NAME = "sm.permissionService";
	
	/**
	 * ��ȡ��ʼ���ڵ�
	 * @return
	 */
	public TblCmnPermission getInitRoot();


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
	 * ��ȡ���ڵ�
	 * @return
	 */
	TblCmnPermission getRoot();
	
	/**
	 * �г�ָ���ڵ���һ������������Ϣ
	 * 
	 * @param config �г�����ڵ��µ�����
	 * @return ָ���ڵ���һ������������Ϣ, Ԫ��������{@link TblCmnPermission}
	 */
	List getChildren(String oid);
	/**
	 * �г�ָ���ڵ���һ��ָ�����͵ĺ��ӽڵ�
	 * @param oid
	 * @param nodetype	���ӽڵ�����
	 * @return
	 */
	List getChildren(String oid,String[] nodetypes);
	/**
	 * ����ҳ����������Ӹ��ڵ㿪ʼ��ѯ���ڵ㼰�������ӽڵ��µļ�¼
	 * ֧�ַ�ҳ
	 * ��������
	 * @param foid
	 * @return
	 * 2007-12-6 ����04:45:44
	 * @version 1.0
	 * @author yushn
	 */
	List queryStartWithFatherId(String foid ,String fname);
	/**
	 * 
	 * �������� ȡ��ɫȨ�޹���
	 * @param bean
	 * @return
	 * 2007-12-5 ����06:15:35
	 * @version 1.0
	 * @author wanghy
	 */
	List getRolePermision(TblCmnPermission bean);

	/**
	 * 
	 * �������� ȡ�û�Ȩ�޹���
	 * @param bean
	 * @return
	 * 2007-12-5 ����06:16:33
	 * @version 1.0
	 * @author wanghy
	 */
	List getUserPermision(TblCmnPermission bean);
	/**
	 * 
	 * �������� ί��Ȩ��ȡ��
	 * @param bean
	 * @return
	 * 2007-11-28 ����05:40:07
	 * @version 1.0
	 * @author wanghy
	 */
	public List getUserPermissionConsignedUser(TblCmnUser consignerBean, TblCmnUser userBean);

	/**
	 * 
	 * �������� �ж���
	 * @param role
	 * @param strFlg
	 * @return
	 * 2007-12-5 ����06:06:27
	 * @version 1.0
	 * @author wanghy
	 */
	public List onlyOneFlg(String permissionCd, String fatherId);
	
	/**
	 * �ж���ͬurl�Ĺ���Ȩ���Ƿ����
	 * @param url
	 * @return
	 */
	public boolean existUrl(String url);


	public List getPerRole(TblCmnPermission bean);


	public List getPerUser(TblCmnPermission bean);


	public List getRoleUser(TblCmnRole role);

}
