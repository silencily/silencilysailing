package net.silencily.sailing.common.dict.service;

import java.util.List;

import net.silencily.sailing.common.dict.domain.CommonBasicCode;
import net.silencily.sailing.framework.core.ServiceBase;




/**
 * @author zhaoyifei
 *
 */
public interface CommonBasicCodeService extends ServiceBase{

	static final String SERVICE_NAME = "common.CommonBasicCodeService";
	
	List list(CommonBasicCode config);

	public String getNameByCode(String code);
	/**
	 * ����򴴽�һ��������Ϣ
	 * @param config Ҫ����򴴽���������Ϣ
	 */
	void saveOrUpdate(CommonBasicCode config);
	
	/**
	 * ɾ��ָ����������Ϣ, �������ڵ������ӽڵ�������ɾ��
	 * @param config Ҫɾ���Ľڵ�
	 */
	void delete(CommonBasicCode config);
	
	/**
	 * ����ָ������Ľڵ�
	 * @param id Ҫ���صĽڵ����
	 * @return �ڵ���Ϣ
	 */
	CommonBasicCode load(String id);
	
	/**
	 * ����ָ������Ľڵ�
	 * @param id Ҫ���صĽڵ����
	 * @return �ڵ���Ϣ
	 */
	CommonBasicCode get(String id);
	
	/**
	 * ��ָ���Ľڵ��´����µĽڵ���Ϣ
	 * @param parentId ָ���ڵ��<code>code</code>
	 * @return �´����Ľڵ���Ϣ
	 */
	CommonBasicCode newInstance(String parentId);
	
	/**
	 * �������� ��ȡָ���������Ϣ
	 * @param basicCd Ҫ��ѯ�Ĵ���
	 * @return List
	 */
	public List getListByCode(String basicCd);
}
