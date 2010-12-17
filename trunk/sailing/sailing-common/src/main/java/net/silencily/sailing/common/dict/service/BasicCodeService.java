package net.silencily.sailing.common.dict.service;

import java.util.Map;

import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.framework.web.view.ComboSupportList;


/**
 * @author zhaoyf
 *	
 */
public interface BasicCodeService extends ServiceBase{

	static final String SERVICE_NAME = "common.CommonBasicCodeService";
	/**
	 * 
	 */
	public static final String DICT_SYSTEM_ROOT="systemroot";
	public static final String DICT_HR_ROOT="hrroot";
	
	
	public static final String DICT_HR_MODULE="";
	
	
	public static final String DICT_HR_TYPE_NATION="";
	
	
	/**
	 * 
	 * @param sid ���ڵ�id
	 * @return ����ϵͳ�Ļ��������б��ṩ������������״�ṹ�õ��ַ�����
	 * ����ʱ�䣺����02:13:52
	 * �����ˣ�zhaoyf
	 * ���ƣ�getBasicCodeTree
	 */
	public String getBasicCodeTree(String sid);
	
	/**
	 * 
	 * @param root ���ڵ�id
	 * @return �����뺺�ֶ�Ӧ��map
	 * ����ʱ�䣺����02:19:17
	 * �����ˣ�zhaoyf
	 * ���ƣ�getDictListByRoot
	 */
	public Map getDictListByRoot(String root);
	
	/**
	 * 
	 * @param id - �ֵ�����ID���������ϵͳ�Ļ�������ά�����IDһ�£�
	 * @param sid - ��ϵͳID
	 * @param bid - ��������ID
	 * @return
	 * ����ʱ�䣺����02:21:31
	 * �����ˣ�zhaoyf
	 * ���ƣ�getBasicCodeName
	 */
	public String getBasicCodeName(String sid,String bid,String id);
	
	/**
	 * 
	 * @param sid - ��ϵͳID
	 * @param bid - ��������ID
	 * @return �����뺺�ֶ�Ӧ��map
	 * ����ʱ�䣺����02:28:16
	 * �����ˣ�zhaoyf
	 * ���ƣ�getBasicCodeList
	 */
	public Map getBasicCodeList(String sid,String bid);
	
	public ComboSupportList getComboListAll();
	/**
	 * 
	 * �������� �õ��������б�
	 * @param sid - ��ϵͳID
	 * @param bid - ��������ID
	 * @return ComboSupportList
	 * 2007-9-4����01:26:41
	 */
	public ComboSupportList getComboList(String sid,String bid);
	
	
	/**
	 * 
	 * �������� ��code�õ�name
	 * @param code ����
	 * @return ������
	 * Oct 5, 2007 1:26:46 PM
	 */
	public String getNameByCode(String code);
}
