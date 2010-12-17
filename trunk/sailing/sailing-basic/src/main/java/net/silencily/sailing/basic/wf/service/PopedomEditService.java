package net.silencily.sailing.basic.wf.service;

import net.silencily.sailing.basic.wf.web.PopedomEditForm;
import net.silencily.sailing.framework.core.ServiceBase;

public interface PopedomEditService extends ServiceBase{

	public static String SERVICE_NAME = "wf.PopedomEditService";
	
	/**
	 * �������� ��ʼ��������������ҵ�����
	 * @param theForm
	 * @return
	 * 2008-06-10 18:26:33
	 * @version 1.0
	 * @author yangxl
	 */
	void save(PopedomEditForm theForm) throws Exception;
	/**
	 * 
	 * ��������  ɾ���ɱ༭����Ϣ
	 * @param oid
	 * @return
	 * 2008-06-10 21:26:31
	 * @version 1.0
	 * @author yangxl
	 */
	void delete(String oid);
	
}
