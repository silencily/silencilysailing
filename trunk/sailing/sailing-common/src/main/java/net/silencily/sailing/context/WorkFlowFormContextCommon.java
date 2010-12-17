package net.silencily.sailing.context;

import javax.servlet.http.HttpSession;

import net.silencily.sailing.security.SecurityContextInfo;



/**
 * ����˵��������Ĵ��ڣ���ȫ����Ϊҵ��ͨû�С�
 * @author wenjb
 * @version 1.0
 */
public class WorkFlowFormContextCommon extends BusinessContext {

	/**
	 * ��ȡ��ǰ��SESSION,˽�еķ���
	 */
	private static HttpSession getHttpSession() {
		return SecurityContextInfo.getSession();
	}

	/**
	 * ����˵�������ݴ���Ĳ�����������Ӧ��FieldStatus
	 */
	public static String getFieldStatus() {
		return (String) getHttpSession().getAttribute("fieldStatus");
	}
	
	/**
	 * ����˵������־��ǰ�ı��Ƿ��ǹ������ڲ��ı�
	 * 
	 */
	public static String getTag() {
		// ��ǰ��URL����TASKID����"1"�������ڹ������ڲ�,û�еĻ�����"2"
		String tag = null;
		try {
			tag = (String) getHttpSession().getAttribute("tag");
		} catch (Exception e) {
			tag = "2";
		}
		return tag;
	}
	
	/**
	 * ����˵������־��ǰ��URL�Ƿ��Ǵ��Ѱ������ǴӴ�����������
	 * 
	 */
	public static String getUrlKey() {
		String urlKey = "";
		try {
			urlKey = (String) getHttpSession().getAttribute("urlKey");
		} catch (Exception e) {
			urlKey = "";
		}
		return urlKey;
	}
}
