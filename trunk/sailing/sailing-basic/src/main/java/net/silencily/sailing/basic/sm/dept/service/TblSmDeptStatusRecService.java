package net.silencily.sailing.basic.sm.dept.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnDeptStatusRec;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * ��֯��������, ���еķ���������<code>null</code>
 * 
 * @author gaojing
 * @version $Id:TblHrDeptStatusRecService.java,v 1.2 2007/07/06 07:49:09 scott
 * Exp $
 * @since 2007-8-29
 */
public interface TblSmDeptStatusRecService extends ServiceBase {

	String SERVICE_NAME = "sm.deptStatusService";

	/**
	 * ����򴴽�һ��������Ϣ
	 * 
	 * @param config Ҫ����򴴽���������Ϣ
	 */
//	void saveOrUpdate(TblHrDeptStatusRec config);

	/** ������ʾ״̬������� */
//	List list();

	/**
	 * ����ָ������Ľڵ�
	 * 
	 * @param id Ҫ���صĽڵ����
	 * @return �ڵ���Ϣ
	 */
//	TblHrDeptStatusRec load(String id);

	/**
	 * ��ָ���Ľڵ��´����µĽڵ���Ϣ
	 * 
	 * @param parentId ָ���ڵ��<code>code</code>
	 * @return �´����Ľڵ���Ϣ
	 */
	TblCmnDeptStatusRec newInstance(String parentCode);
}
