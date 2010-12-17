package net.silencily.sailing.security;

import java.util.Map;

import javax.servlet.http.HttpSession;

import net.silencily.sailing.security.model.CurrentUser;


/**
 * ��ȫ������
 * 
 * @author yushn
 * @version 1.0
 */
public class SecurityContextInfo {
	private static ThreadLocal currentUser = new ThreadLocal();

	private static ThreadLocal currentPageUrl = new ThreadLocal();

	private static ThreadLocal mainTableClassName = new ThreadLocal();

	private static ThreadLocal singleSignOnUrl = new ThreadLocal();

	// ��ŵ�ǰ�����session
	private static ThreadLocal session = new ThreadLocal();
	
	/**
	 * ��յ�ǰ��ȫ������ �������� 2007-11-26 ����07:47:49
	 * 
	 * @version 1.0
	 * @author yushn
	 */
	public static void clear() {
		currentUser.set(null);
	}

	/**
	 * ��ȡ��ǰ��¼�û� ��������
	 * 
	 * @return 2007-11-20 ����09:13:38
	 * @version 1.0
	 * @author yushn
	 */
	public static CurrentUser getCurrentUser()
	{
		// if (currentUser.get() == null) {
		// //TODO:��acige��ȫ�������л�ȡ��ǰ�û�,��ǰ�û���Ϣ�ڵ�¼ʱ����
		// }
		// return (CurrentUser)currentUser.get();
		// return new MockCurrentUser();
		/*
		 * if (SecurityContextHolder.getContext() == null) { return null; } if
		 * (SecurityContextHolder.getContext().getAuthentication() == null) {
		 * return null; } ExtensionalUserDetails user =
		 * (ExtensionalUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 * CurrentUser cu = null; if (user != null) { cu =
		 * user.getCurrentUser(); }
		 */	
		CurrentUser cu = null;	
		if(getSession()!= null){
			cu = (CurrentUser) getSession().getAttribute("acigeCurrentUser");
		}else{
			cu = (CurrentUser) SecurityContextInfo.currentUser.get();
		}
        return cu;
    }

	/**
	 * ��ȡ��ǰ����ҳ���url ��������
	 * 
	 * @return 2007-11-28 ����07:17:32
	 * @version 1.0
	 * @author yushn
	 */
	public static String getCurrentPageUrl() {
		if (null != currentPageUrl.get()){
			return currentPageUrl.get().toString();
		}else{
			return "";
		}
	}

	public static void setCurrentPageUrl(String currentPageUrl) {
		SecurityContextInfo.currentPageUrl.set(currentPageUrl);
	}

	public static String getMainTableClassName() {
		
		if (null != mainTableClassName.get()){
			return mainTableClassName.get().toString();
		}else{
			return "";
		}
	}

	public static void setMainTableClassName(String mainTableClassName) {
		SecurityContextInfo.mainTableClassName.set(mainTableClassName);
	}

	public static String getSingleSignOnUrl() {
		return (singleSignOnUrl.get() != null) ? singleSignOnUrl.get()
				.toString() : "";
	}

	public static void setSingleSignOnUrl(String singleSignOnUrl) {
		SecurityContextInfo.singleSignOnUrl.set(singleSignOnUrl);
	}

	/**
	 * ��SESSION��ȡ��ǰ�����URL��Ӧ�����ݼ�¼�����ݷּ���Ϣ ��������
	 * 
	 * @return 2007-12-25 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static Map getRwCtrlTypeMap() {

		return (Map) getSession().getAttribute("rwCtrlTypeMap");
	}

	/**
	 * ��SESSION�����õ�ǰ�����URL��Ӧ�����ݼ�¼�����ݷּ���Ϣ ��������
	 * 
	 * @return 2007-12-25 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static void setRwCtrlTypeMap(Map rwCtrlType) {
		getSession().removeAttribute("rwCtrlTypeMap");
		getSession().setAttribute("rwCtrlTypeMap", rwCtrlType);
	}

	/**
	 * ��ȡ��ǰ��session ��������
	 * 
	 * @return 2008-1-3 ����04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static HttpSession getSession() {
		return (HttpSession) SecurityContextInfo.session.get();
	}

	public static void setSession(HttpSession session) {
		SecurityContextInfo.session.set(session);
	}

	public static void setCurrentUser(CurrentUser currentUser) {
		SecurityContextInfo.currentUser.set(currentUser);
	}
	public static Object sessionManagerFactory(String key) {
		if(SecurityContextInfo.getSession()==null){
			return null;
		}else{
			return SecurityContextInfo.getSession().getAttribute(key);
		}
	}
}