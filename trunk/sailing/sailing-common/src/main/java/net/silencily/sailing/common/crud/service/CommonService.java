package net.silencily.sailing.common.crud.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.struts.BaseFormPlus;

import org.hibernate.criterion.DetachedCriteria;


/**
 * @author zhaoyifei
 *
 */
public interface CommonService extends ServiceBase{

	String SERVICE_NAME = "common.CommonCURD";
	
	/**
	 * 
	 * �������� �������ݿ����ݣ�����Ϊ�½��ĳ־û�����
	 * @param e �־û�����
	 * Oct 31, 2007 10:33:10 AM
	 */
	public void add(Object e);
	
	/**
	 * 
	 * �������� �޸����ݿ����ݡ�
	 * @param e �־û�����
	 * Oct 31, 2007 10:34:12 AM
	 */
	public void update(Object e);
	
	/**
	 * 
	 * ��������	����ɾ�����ݿ�����
	 * @param e �־û�����
	 * Oct 31, 2007 10:52:01 AM
	 */
	public void delete(Object e);
	
	/**
	 * 
	 * �������� �߼�ɾ�����ݿ����ݣ��޸ĳ־û������delFlg�ֶ�Ϊ"1"
	 * @param e �־û�����
	 * Oct 31, 2007 10:52:36 AM
	 */
	public void deleteLogic(Object e);
	/**
	 * 
	 * �������� ������޸����ݿ�����
	 * @param e �־û�����
	 * Oct 31, 2007 10:53:33 AM
	 */
	public void saveOrUpdate(Object e);
	
	/**
	 * 
	 * �������� ��ѯ���ݣ������Զ�����
	 * @param c ���ݿ���������
	 * @param user ��ѯ�û�
	 * @return List ���ط������������ݿ�����
	 * Oct 31, 2007 10:53:54 AM
	 */
	public List getList(Class c,String user);
	
	/**
	 * 
	 * �������� ��ѯ���ݣ������Զ�����
	 * @param c ���ݿ���������
	 * @return List ���ط������������ݿ�����
	 * Oct 31, 2007 10:56:28 AM
	 */
	public List getList(Class c);
	/**
	 * 
	 * ��������  ��ѯ���ݣ������Զ�����
	 * @param c  ���ݿ���������
	 * @param user ��ѯ�û�
	 * @param pageId ҳ��id
	 * @param orderAsc ���� ����
	 * @param orderDesc ���� ����
	 * @return ViewBean ������ʾ�б�Ķ���
	 * Oct 31, 2007 10:57:11 AM
	 */
	public ViewBean fetchAll(Class c,String user,String pageId,List orderAsc,List orderDesc);
	
	/**
	 * 
	 * ��������  ��ѯ���ݣ������Զ�����
	 * @param c ������������
	 * @param user ��ѯ�û�
	 * @param pageId ҳ��id
	 * @return ViewBean ������ʾ�б�Ķ���
	 * Oct 31, 2007 10:59:11 AM
	 */
	public ViewBean fetchAll(Class c,String user,String pageId);
	
	/**
	 * 
	 * ��������  ��ѯ���ݣ������Զ�����
	 * @param c ���ݿ���������
	 * @param pageId ҳ��id
	 * @return ViewBean ������ʾ�б�Ķ���
	 * Oct 31, 2007 10:59:33 AM
	 */
	public ViewBean fetchAll(Class c,String pageId);
	
	/**
	 * 
	 * �������� �������ݿ��е�һ������
	 * @param c ���ݿ���������
	 * @param id ���ݵ�id
	 * @return ���ݿ�����
	 * Oct 31, 2007 11:00:00 AM
	 */
	public Object load(Class c,String id);
	/**
	 * 
	 * �������� �������ݿ��е�һ������
	 * @param c ���ݿ���������
	 * @param id ���ݵ�id
	 * @return ���ݿ�����
	 * Oct 31, 2007 11:00:00 AM
	 */
	public Object writeLoad(Class c,String id);
	
	/**
	 * 
	 * �������� ����������ѯ������������Ҫ����Ψһ����֤
	 * @param dc ��ѯ����
	 * @return ��ѯ��Ŀ
	 * Oct 31, 2007 11:01:04 AM
	 */
	public int getSize(DetachedCriteria dc);
	
	/**
	 * 
	 * �������� �����߼�ɾ������
	 * @param c ���ݿ���������
	 * @param oids id����
	 * @return ɾ������
	 * Oct 31, 2007 6:10:35 PM
	 */
	public int deleteAllLogic(Class c,List oids);
	
	/**
	 * 
	 * �������� ��������ɾ������
	 * @param c ���ݿ���������
	 * @param oids id����
	 * @return ɾ������
	 * Oct 31, 2007 6:10:35 PM
	 */
	public int deleteAll(Class c,List oids);
	
	/**
	 * 
	 * �������� �������������Ӽ�
	 * @param s ����
	 * @param bfp form
	 * @return ���������Ӽ�
	 * Nov 6, 2007 10:13:16 AM
	 */
	public Set getSubSet(Set s,BaseFormPlus bfp);
	
	
	/**
	 * 
	 * �������� �����߼�ɾ������
	 * @param c ���ݼ���
	 * @return ɾ������
	 * Oct 31, 2007 6:10:35 PM
	 */
	public int deleteAllLogic(Collection c);
	
	/**
	 * 
	 * �������� ��������ɾ������
	 * @param c ���ݼ���
	 * @return ɾ������
	 * Oct 31, 2007 6:10:35 PM
	 */
	public int deleteAll(Collection c);
	
	/**
	 * ע�� �˷������Ƽ�ʹ��!!!��ʹ��listSetToVB����˷���
	 * �������� ���մ�����������viewbean
	 * @param c ���ݿ���������
	 * @param pageId ҳ��id
	 * @param data Ҫ������viewbean�е�����list
	 * @return ViewBean ������ʾ�б�Ķ���
	 * 2007-11-26 ����11:34:09
	 * @version 1.0
	 * @author lihe
	 */
	public ViewBean setVBByList(String pageId,List data);
    
	/**
	 *
	 * �������� ���մ�����������viewbean
	 * @param c ���ݿ���������
	 * @param pageId ҳ��id
	 * @param data Ҫ������viewbean�е�����list
	 * @return ViewBean ������ʾ�б�Ķ���
	 * 2008-02-15 ����11:34:09
	 * @version 1.0
	 * @author tongjq
	 */
	public ViewBean listSetToVB(String pageId,List data);
    /**
     *
     * �������� ����������ȡ����
     * @param dc ����
     * @return List ��������
     * 2008-01-05 13:09:11
     * @version 1.0
     * @author tongjq
     */
    public List findByCriteria(DetachedCriteria dc);
}
