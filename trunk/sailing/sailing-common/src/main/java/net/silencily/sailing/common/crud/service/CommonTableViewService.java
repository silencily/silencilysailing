package net.silencily.sailing.common.crud.service;

import java.util.List;

import net.silencily.sailing.common.crud.domain.CommonTableScreen;
import net.silencily.sailing.common.crud.domain.CommonTableView;
import net.silencily.sailing.framework.core.ServiceBase;


/**
 * @author zhaoyifei
 *
 */
public interface CommonTableViewService extends ServiceBase{

	String SERVICE_NAME = "common.ViewRows";
	/**
	 * 
	 * �������� �õ��û����Ƶ���ʾ��
	 * @param userId �û�id
	 * @param c �������־û�������
	 * @return �����б�
	 * 2007-8-22����10:51:14
	 */
	public List getRows(String userId,String pageId);
	
	/**
	 * 
	 * �������� �õ��û����Ƶ���������
	 * @param userId �û�id
	 * @param c �������־û�������
	 * @param asc ����ʽ asc��desc
	 * @return �����б�
	 * 2007-8-22����10:51:14
	 */
	public List getRows(String userId,String pageId,String asc);
	/**
	 * 
	 * �������� �õ��û����Ƶ���������
	 * @param userId �û�id
	 * @param c �������־û�������
	 * @param asc �Ƿ���������
	 * @return �����б�
	 * 2007-8-22����10:51:14
	 */
	public List getRows(String userId, String pageId, boolean asc);
	
	/**
	 * 
	 * �������� ���п�����ʾ������
	 * @param c �������־û�������
	 * @return �����б�
	 * 2007-8-22����10:54:05
	 */
	public List getRows(String pageId);
	
    /**
     * 
     * �������� �õ�Ĭ�ϵ���������
     * @param c �������־û�������
     * @return �����б�
     * 2007-8-22����10:51:14
     */
    public List getDefOrders(String pageId);
	/**
	 * 
	 * �������� �õ��е������ֵ�
	 * @param c
	 * @param rowName
	 * @return
	 * 2007-8-22����11:06:12
	 */
	public String getRowsDict(String pageId,String rowName);
	
	/**
	 * 
	 * �������� ����
	 * @param l
	 * Oct 10, 2007 11:11:15 AM
	 */
	public void saveRows(String[] ids,String userId,String pageId);
	
	/**
	 * 
	 * �������� ����
	 * @param l
	 * Oct 10, 2007 11:11:15 AM
	 */
	public void saveRows(String[] ids,String userId,String pageId,String asc);
	
	/**
	 * 
	 * �������� ��ѯ������Ի���������
	 * @param pageId ҳ��id
	 * @param user �û�
	 * @return <code>"'aa','bb','dd'"</code>
	 * Nov 28, 2007 4:57:48 PM
	 * @version 1.0
	 * @author zhaoyf
	 */
	public String getSearchTags(String pageId,String user);
	
	/**
	 * 
	 * �������� ����
	 * @param l
	 * Oct 10, 2007 11:11:15 AM
	 */
	public void saveRows(String[] ids, String userId, String pageId,boolean asc);
	
	/**
	 * 
	 * �������� �Ƿ���������
	 * @return
	 * Jan 9, 2008 1:27:23 PM
	 * @version 1.0
	 * @author zhaoyf
	 */
	public boolean isSetIsNull();
	
//	------------------------������ʾ����---------------------
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
	 * �������� ͨ�� tableName ��ѯ������Ϣ��
	 * @param tableName
	 * @return
	 * 2008-01-28 ����10:26:31
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableView findCtViewByName(String tableName);
	
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
	 * @param tableName
	 * @return
	 * 2008-01-28 ����10:26:38
	 * @version 1.0
	 * @author yangxl
	 */
	public void deleteCtView(String tableName);
	
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
	
	/**
	 * 
	 * �������� ͨ�� id ��ѯҳ����Ϣ
	 * @param id
	 * @return
	 * 2008-01-28 ����10:26:31
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableScreen findCtScreenById(String id);
	/**
	 * 
	 * �������� ͨ�� name ��ѯҳ����Ϣ
	 * @param name
	 * @return
	 * 2008-01-28 ����10:26:31
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableScreen findCtScreenByName(String name);
	
	/**
	 * 
	 * �������� ����ҳ����Ϣ 
	 * @param 
	 * @return
	 * 2008-01-28 ����10:26:32
	 * @version 1.0
	 * @author yangxl
	 */
	public List findAllCtScreen();
	
	/**
	 * 
	 * �������� �洢ҳ����Ϣ 
	 * @param ctscreen
	 * @return
	 * 2008-01-28 ����10:26:35
	 * @version 1.0
	 * @author yangxl
	 */
	public void saveCtScreen(String id,String screenName,String tableName);

	/**
	 * 
	 * �������� ɾ��ҳ����Ϣ 
	 * @param id
	 * @return
	 * 2008-01-28 ����10:26:38
	 * @version 1.0
	 * @author yangxl
	 */
	public void deleteCtScreen(String id);
	
	/**
	 * 
	 * ��������  ���ϲ�ѯ
	 * @param ctscreen
	 * @return
	 * 2008-01-28 ����11:08:40
	 * @version 1.0
	 * @author yangxl
	 */
	public List searchs(CommonTableScreen ctscreen);
	
	/**
	 * 
	 * ��������  ͨ��tableName��ѯ������ͬ����
	 * @param tableName
	 * @return
	 * 2008-01-28 ����11:08:40
	 * @version 1.0
	 * @author yangxl
	 */
	public CommonTableScreen findCtTableByName(String tableName);
	/**
	 * tableview by baihe
	 * @param tableViews
	 * @param bean
	 */
	public void save(List tableViews, CommonTableScreen bean);
	public List getTableViews(String tableName);
}
