package net.silencily.sailing.security.model;

/**
 * ���ݷ�������
 * @author yushn
 * @version 1.0
 */
public interface DataAccessType {
	/**
	 * ϵͳ��:û�з�Χ����
	 */
	int SYSTEM = 0;
	/**
	 * ���ż�:ֻ�ܷ������Լ�ͬ���ŵ��û�����������
	 */
	int DEPT = 1;
	/**
	 * ���˼���ֻ�ܷ����Լ�����������
	 */
	int SELF = 2;
	/**
	 * ��ֹ�����ɷ���
	 */
	int FORBID = 3;
}
