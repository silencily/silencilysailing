package net.silencily.sailing.common.basiccode.service;

import java.util.List;

import net.silencily.sailing.framework.core.ServiceBase;

public interface CommonBasicCodeInitService extends ServiceBase {

	static final String SERVICE_NAME = "common.CommonBasicCodeInitService";
	/**
	 * ����ָ������Ľڵ�
	 * 
	 * @param id
	 *            Ҫ���صĽڵ����
	 * @return �ڵ���Ϣ
	 */
	public List load(String id);

	/**
	 * ��ʼ��ѡ�е���ϵͳ
	 * 
	 * @param subID
	 *            ��ϵͳid
	 * @return ������
	 */
	public String reset(String subID);

	/**
	 * ����ID���Ҽ�¼
	 * 
	 * @param id
	 *            Ҫ�ҵ�ID
	 * @return �ҵ��ļ�¼
	 */
	public List find(String id);

}
