package net.silencily.sailing.basic.sm.dept.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnDept;
import net.silencily.sailing.framework.core.ServiceBase;



/**
 * ��֯��������, ���еķ���������<code>null</code>
 * 
 * @author gaojing
 * @version $Id: TblSmDeptService.java,v 1.1 2010/12/10 10:56:45 silencily Exp $
 * @since 2007-8-29
 */
public interface TblSmDeptService extends ServiceBase {

	String SERVICE_NAME = "sm.deptService";

	/**
	 * �г�ָ���ڵ���һ������������Ϣ, ����������<code>null</code>, ����������·������� ������ڵ���һ�������нڵ�
	 * 
	 * @param config �г�����ڵ��µ�����, �����<code>null</code>��ʾ�г����ø��ڵ�
	 * @return ָ���ڵ���һ������������Ϣ, Ԫ��������{@link TblCmnDept}
	 */
	List list(TblCmnDept config, String radio);

	/**
	 * ����򴴽�һ��������Ϣ
	 * 
	 * @param config Ҫ����򴴽���������Ϣ
	 
	 void saveOrUpdate(TblCmnDept config);
     */
	/**
	 * ɾ��ָ����������Ϣ, �������ڵ������ӽڵ�������ɾ��
	 * 
	 * @param config Ҫɾ���Ľڵ�
	 void delete(TblCmnDept config);

	 */
	/**
	 * ����ָ������Ľڵ�
	 * 
	 * @param id Ҫ���صĽڵ����
	 * @return �ڵ���Ϣ
	 
	TblCmnDept load(String id);
	*/
	/**
	 * ��ָ���Ľڵ��´����µĽڵ���Ϣ
	 * 
	 * @param parentId ָ���ڵ��<code>code</code>
	 * @return �´����Ľڵ���Ϣ
	 */
	TblCmnDept newInstance(String parentId);

	/** �����Լ����޲�list()����,��ʾ���еĽڵ� 
	List list();*/

	/** ��ȡ��id */
	public String getParentId(String id);
}
