package net.silencily.sailing.basic.sm.dept.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnDeptPareRec;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * ��֯��������, ���еķ���������<code>null</code>
 * 
 * @author gaojing
 * @version $Id: TblSmDeptPareRecService.java,v 1.1 2010/12/10 10:56:45 silencily Exp $
 * @since 2007-8-29
 */
public interface TblSmDeptPareRecService extends ServiceBase {

	String SERVICE_NAME = "sm.deptParentService";

	/**
	 * ����򴴽�һ��������Ϣ
	 * 
	 * @param config Ҫ����򴴽���������Ϣ
	 
	void saveOrUpdate(TblHrDeptPareRec config);
	*/
	/** ������ʾ״̬������� */
//	List list();

	/**
	 * ����ָ������Ľڵ�
	 * 
	 * @param id Ҫ���صĽڵ����
	 * @return �ڵ���Ϣ
	 */
//	TblHrDeptPareRec load(String id);

	/**
	 * ��ָ���Ľڵ��´����µĽڵ���Ϣ
	 * 
	 * @param parentId ָ���ڵ��<code>code</code>
	 * @return �´����Ľڵ���Ϣ
	 */
	TblCmnDeptPareRec newInstance(String parentCode);

}
