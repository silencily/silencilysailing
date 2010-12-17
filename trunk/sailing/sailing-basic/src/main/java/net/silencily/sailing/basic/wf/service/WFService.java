package net.silencily.sailing.basic.wf.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.basic.wf.domain.TblWfParticularInfo;
import net.silencily.sailing.framework.core.ServiceBase;


public interface WFService extends ServiceBase {
	
	public static final String SERVICE_NAME = "wf.wFService";


	//WFOPERATION��Ϣ�������
	/**
	 * 
	 * �������� ͨ����������Ϣ���PK�����ҹ������ľ���ʵ��,�м�����ϵ��
	 * @param id
	 * @return
	 * 2007-12-5 ����10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public TblWfOperationInfo findWfOperInfoById(String id);
	
	public TblWfOperationInfo findWfOperInfoByName(String name);
	
	/**
	 * 
	 * �������� �����ݿ��е������еĹ�����ʵ����
	 * @param 
	 * @return
	 * 2007-12-5 ����10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public List findAllWfOper();
	
	/**
	 * 
	 * �������� �洢һ��������ʵ��,Ŀǰֻ�洢��������Ϣ��������XML�ļ�����
	 * @param wfinfo
	 * @return
	 * 2007-12-5 ����10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void saveWfOperInfo(TblWfOperationInfo wfinfo);
	
	/**
	 * 
	 * ��������  ɾ��������ʵ����Ϣ��ֻ���޸Ĺ�������Ϣ���ɾ����־�ֶ�
	 * @param id
	 * @return
	 * 2007-12-5 ����10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void deleteWfOperInfo(String id);
	
	/**
	 * 
	 * ��������  �����޸ĵĹ�����ʵ����Ϣ��
	 * @param wfInfo
	 * @return
	 * 2007-12-5 ����10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void updateWfOperInfo(TblWfOperationInfo wfInfo);
	
	//�Ծ���������Ϣ��Ĳ���	
	
	/**
	 * 
	 * �������� �洢һ�����趨����Ϣ,ͬʱ�洢��Ȩ����Ϣ���м�����ϵ��
	 * @param wfinfo
	 * @return
	 * 2007-12-5 ����10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void saveWfParInfo(TblWfParticularInfo wfinfo);
	
	/**
	 * 
	 * ��������  ɾ��������ʵ����Ϣ��ֻ���޸Ĳ��趨����Ϣ���ɾ����־�ֶ�
	 * @param wfinfo
	 * @return
	 * 2007-12-5 ����10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void deleteWfParInfo(String id);
	
	/**
	 * 
	 * ��������  ���¾������̶�����Ϣ�����趨����Ϣ����
	 * @param wfInfo
	 * @return
	 * 2007-12-5 ����10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void updateWfParInfo(TblWfParticularInfo wfInfo);

	/**
	 * 
	 * ��������  ���ݱ������õ���Ȩ�ֶε��б�
	 * @param str
	 * @return
	 * 2007-12-5 ����10:26:31
	 * @version 1.0
	 * @author wenjb
	 */	
	//public List readWFForm(Class str);
	
	/**
	 * 
	 * ��������  ���ݱ������õ���ɫ���б�
	 * @return
	 * 2007-12-14 ����17:50:31
	 * @version 1.0
	 * @author yangxl
	 */
	public List findRole();
	
	/**
	 * 
	 * ��������  ���ݱ�ʶ���õ���ɫ��
	 * @param roleCd
	 * @return
	 * 2008-02-21 ����11:50:31
	 * @version 1.0
	 * @author yangxl
	 */
	public TblCmnRole findroleCdRole(String roleCd);
	
	/**
	 * 
	 * ��������  ����id���õ���ɫ��
	 * @param id
	 * @return
	 * 2008-02-21 ����12:50:31
	 * @version 1.0
	 * @author yangxl
	 */
	public TblCmnRole findidRole(String id);
	/**
	 * 
	 * ��������  ����id �õ�������Ϣ����б�
	 * @param id
	 * @return
	 * 2007-12-20 ����11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public TblWfParticularInfo findWfParticularInfoById(String id);
	
	/**
	 * 
	 * ��������  ���ϲ�ѯ
	 * @param wfInfo
	 * @return
	 * 2007-12-20 ����11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public List findsearch(TblWfOperationInfo wfInfo);
	
	/**
	 * 
	 * ��������  ɾ��
	 * @param id
	 * @return
	 * 2007-12-20 ����11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public void deleteWfFormInfo(String id);
	
	/**
	 * 
	 * ��������  �༭�汾��
	 * @param id
	 * @return
	 * 2008-1-17 ����11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public TblWfOperationInfo findWfOperInfoByEdition(String name);
	
	/**
	 * 
	 * ��������  �ж�step���Ʋ����ظ�
	 * @param stepName,tblWfOperationInfo
	 * @return
	 * 2008-1-24 ����11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public boolean findbooleanOpinstepName(String stepName,TblWfOperationInfo tblWfOperationInfo); 
	
	/**
	 * ��¼Ӧ���������ض������µ�״̬
	 * @param appBean Ӧ��ʵ��bean
	 * @param beanId  Ӧ��ʵ��bean��id
	 * @param stepId
	 * @throws Exception 
	 */
	public void recAppDataStatus(WorkflowInfo appBean,String stepId) throws Exception;
	/**
	 * �ָ�Ӧ���������ض������µ�״̬
	 * @param appBean
	 * @param beanId
	 * @param stepid
	 */
	public void resetAppDataStatus(WorkflowInfo appBean,String stepId);
	
	/**
	 * 
	 * ��������  ͨ������Ա��ʶ��ù���Ա��Ϣ
	 * @param empCd
	 * @return
	 * 2008-3-06 ����11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public TblCmnUser findempCdUser(String empCd);
	
	/**
	 * �����������������
	 * @param beanId
	 */
	public void cleanAppDataStatus(String beanId);
	
	/**
	 * 
	 * �������� ������п�������
	 * @param
	 * @return
	 * 2008-3-14 ������13:38:31
	 * @version 1.0
	 * @author yangxl
	 */
	public List findWfOperInfo();
	
	/**
	 * 
	 * �������� ������п�������
	 * @param ooid,stid
	 * @return
	 * 2008-3-20 ������13:38:31
	 * @version 1.0
	 * @author yangxl
	 */
	public String findTblWfParticularInfoName(String ooid,String stid);
	
	/**
	 * 
	 * �������� �������������Ϣ
	 * @return
	 * 2008-5-29 ������13:38:31
	 * @version 1.0
	 * @author yangxl
	 */
	public List findWfOperInfoAll();

	public void saveHistoryEntryData(String entryStepId,String stepChangeContent,String masterOid) throws Exception;
	
	public String searchHistoryEntryData(String entryStepId) throws Exception;
	
	public String searchStepFieldComment(Class entry,String entryField) throws Exception;
}
