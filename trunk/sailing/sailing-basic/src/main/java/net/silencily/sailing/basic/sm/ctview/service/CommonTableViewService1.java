package net.silencily.sailing.basic.sm.ctview.service;

import java.util.List;

import net.silencily.sailing.common.crud.domain.CommonTableView;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * 
 * @author yangxl
 * @version 1.0
 */
public interface CommonTableViewService1 extends ServiceBase{

	String SERVICE_NAME = "common.ViewRows";
	
	/**
	 * 
	 * �������� ͨ�� id ��ѯ������Ϣ��
	 * @param id
	 * @return
	 * 2008-01-28 ����10:26:31
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableView findCtViewById(String id);
	/**
	 * 
	 * �������� ͨ�� name ��ѯ������Ϣ��
	 * @param name
	 * @return
	 * 2008-01-28 ����10:26:31
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableView findCtViewByName(String name);
	
	/**
	 * 
	 * �������� ���ж�����Ϣ 
	 * @param 
	 * @return
	 * 2008-01-28 ����10:26:32
	 * @version 1.0
	 * @author yangxl
	 */
	public List findAllCtView();
	
	/**
	 * 
	 * �������� �洢������Ϣ 
	 * @param ctview
	 * @return
	 * 2008-01-28 ����10:26:35
	 * @version 1.0
	 * @author yangxl
	 */
	public void saveCtView(CommonTableView ctview);

	/**
	 * 
	 * �������� ɾ��������Ϣ 
	 * @param id
	 * @return
	 * 2008-01-28 ����10:26:38
	 * @version 1.0
	 * @author yangxl
	 */
	public void deleteCtView(String id);
	
	/**
	 * 
	 * ��������  ���ϲ�ѯ
	 * @param ctview
	 * @return
	 * 2008-01-28 ����11:08:40
	 * @version 1.0
	 * @author yangxl
	 */
	public List search(CommonTableView ctview);
}
