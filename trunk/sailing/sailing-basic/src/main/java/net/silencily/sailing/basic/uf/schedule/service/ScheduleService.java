package net.silencily.sailing.basic.uf.schedule.service;

import java.util.List;

import net.silencily.sailing.basic.uf.domain.TblUfSchedule;
import net.silencily.sailing.common.message.domain.TblUfMessage;
import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.framework.web.view.ComboSupportList;


/**
 * @author zhangwenqi
 *
 */
public interface ScheduleService extends ServiceBase {
	public static String SERVICE_NAME = "uf.ScheduleService";
	
	/**
	 * 
	 * �������� ���ݵ�ǰʱ����ѯ����ȡ���б�
	 * @param ��ǰʱ��currentDay
	 * @return list
	 * 2007-12-26 ����02:40:55
	 * @version 1.0
	 * @author yuqian
	 */
	List list(String currentDay);
	
	
	TblUfSchedule load(String oid);
	
	
    void save(Object obj);


    /**
     * 
     * �������� �õ���ǰ���ڵ��ַ���
     * @return
     * 2007-12-29 ����03:43:24
     * @version 1.0
     * @author yuqian
     */
	String getCurrentDay();


	/**
	 * 
	 * �������� �õ���ǰ�µı���¼�б�
	 * @param currentDay
	 * @return
	 * 2007-12-29 ����04:02:09
	 * @version 1.0
	 * @author yuqian
	 */
	List getMemo(String currentDay);

	/**
	 * 
	 * �������� �õ�����ʱ���б�
	 * @return
	 * 2008-1-3 ����04:21:35
	 * @version 1.0
	 * @author yuqian
	 */
	ComboSupportList getAlseList();

	/**
	 * 
	 * �������� �õ�ʱ�����б�
	 * @return
	 * 2008-1-3 ����04:21:38
	 * @version 1.0
	 * @author yuqian
	 */
	ComboSupportList getReptList();

	/**
	 * 
	 * �������� ���뷢����Ϣ
	 * @param bean
	 * 2008-1-7 ����09:51:17
	 * @version 1.0
	 * @author yuqian
	 * @throws Exception 
	 */
	TblUfMessage saveMseeage(TblUfSchedule bean) throws Exception;
	
	/**
	 * 
	 * �������� �޸ķ�����Ϣ
	 * @param bean
	 * 2008-1-7 ����02:24:39
	 * @version 1.0
	 * @author yuqian
	 */
	void updataMessage(TblUfSchedule bean);
	/**
	 * ���شӽ��쿪ʼ������List
	 * @param currentDay
	 * @return
	 */
	List listNowAfter(String currentDay);
}