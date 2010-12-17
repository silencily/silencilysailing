package net.silencily.sailing.autokey.service;

import net.silencily.sailing.framework.core.ServiceBase;



/**
 * 
 * @author zhaoyf
 * @version 1.0
 */
public interface AutoKeyService extends ServiceBase {

	public final String SERVICE_NAME = "common.autokey";
	/**
	 * 
	 * �������� �õ����
	 * @param key ��ŵ�key
	 * @return sn
	 * Dec 6, 2007 11:15:15 AM
	 * @version 1.0
	 * @author zhaoyf
	 */
	public String autoSn(String key);
	/**
	 * 
	 * �������� �õ����
	 * @param key ��ŵ�key
	 * @param style �����ʽ
	 * @return sn
	 * Dec 6, 2007 11:15:15 AM
	 * @version 1.0
	 * @author zhaoyf
	 */
	public String fautoSn(String key,String style);
	/**
	 * 
	 * �������� �ع���ţ�ֻ�ع�һ��
	 * @param key ���key
	 * @return sn���ع�֮���sn
	 * Dec 6, 2007 11:15:53 AM
	 * @version 1.0
	 * @author zhaoyf
	 */
	public String rollbackSn(String key);
	/**
	 * 
	 * �������� ����sn
	 * @param key ��ŵ�key
	 * @param sn ���ֵ
	 * @return sn ���ú�ı��
	 * Dec 6, 2007 11:17:33 AM
	 * @version 1.0
	 * @author zhaoyf
	 */
	public String setSn(String key,int sn);
}
