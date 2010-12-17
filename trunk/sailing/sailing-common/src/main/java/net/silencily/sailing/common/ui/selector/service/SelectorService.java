package net.silencily.sailing.common.ui.selector.service;

import java.util.List;

/**
 * ����ѡ��������
 * @author liuz
 *
 */
public interface SelectorService {
	
	String SERVICE_NAME = "common.ui.selectorService";

	/**�г����������û�
	 * �����<code>currentId</code>�ǵ�ǰ����ID
	 * Ĭ�ϵĸ��ڵ�Ϊ����=<code>0</code>�����ָ�����currentId,����ʾ������������в�ε��Ӳ���.
	 */
	List listDeptTree(String currentId);
	
	/**�г����������û�
	 * currentId ͬ��
	 */
	List listUserTree(String currentId);
	
	/**
	 * ָ��ĳ�������µ��û�list
	 * @param orgId
	 * @param Level �ڵ���
	 * @return
	 */
	List listUserByOrgId(String orgId,int Level);
	
	/** ��ʽ��ʾָ������<code>parentId</code>����������ڵ�
	 *  ע����������ʽ��һ���ԣ����ԣ����parentId�½ڵ���Ŀ����̫��
	 *  Ĭ��Ϊ���ڵ�0
	 * @param parentId
	 * @return
	 */
	List listAllCodeByParentId(String parentId);
	
	/** ��ʽ��ʾָ������<code>codeId</code>�¸�����´���ڵ�
	 *  �����治ͬ�������ǻ�ʽ���ԣ�����Ľڵ���Ŀ��������
	 *  TODO ����Ӧ�÷��ر���ڵ���ӽڵ����ݣ�Ŀǰֻ�����ӽڵ�
	 *  ע��Ĭ�ϵ�code=0
	 * @param parentId
	 * @return
	 */
	List listCodeByParentId(String codeId);
}
