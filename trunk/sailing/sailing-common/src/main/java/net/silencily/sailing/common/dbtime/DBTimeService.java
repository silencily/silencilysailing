package net.silencily.sailing.common.dbtime;


/**
 * 
 * @author wenjb
 * @version 1.0
 */
public interface DBTimeService {
	
	public static final String SERVICE_NAME = "common.DBTimeService";
	
	/**
	 * 
	 * �������� ��ȡ��ǰ���ݿ��ʱ��
	 * 
	 * @param URL
	 * @return ����õ�List����ֱ�����õ�Tree��list�� 2007-11-28 ����09:46:14
	 * @version 1.0
	 * @author lihe
	 * @throws Exception 
	 */
	public String getDBTime();
	
}
