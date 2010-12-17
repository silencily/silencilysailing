package net.silencily.sailing.security;

import java.util.Map;

import javax.servlet.http.HttpSession;

import net.silencily.sailing.security.model.CurrentUser;


/**
 * 安全上下文
 * 
 * @author yushn
 * @version 1.0
 */
public class SecurityContextInfo {
	private static ThreadLocal currentUser = new ThreadLocal();

	private static ThreadLocal currentPageUrl = new ThreadLocal();

	private static ThreadLocal mainTableClassName = new ThreadLocal();

	private static ThreadLocal singleSignOnUrl = new ThreadLocal();

	// 存放当前请求的session
	private static ThreadLocal session = new ThreadLocal();
	
	/**
	 * 清空当前安全上下文 功能描述 2007-11-26 下午07:47:49
	 * 
	 * @version 1.0
	 * @author yushn
	 */
	public static void clear() {
		currentUser.set(null);
	}

	/**
	 * 获取当前登录用户 功能描述
	 * 
	 * @return 2007-11-20 下午09:13:38
	 * @version 1.0
	 * @author yushn
	 */
	public static CurrentUser getCurrentUser()
	{
		// if (currentUser.get() == null) {
		// //TODO:从acige安全上下文中获取当前用户,当前用户信息在登录时构造
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
	 * 获取当前请求页面的url 功能描述
	 * 
	 * @return 2007-11-28 下午07:17:32
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
	 * 从SESSION获取当前请求的URL对应的数据记录的数据分级信息 功能描述
	 * 
	 * @return 2007-12-25 下午04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static Map getRwCtrlTypeMap() {

		return (Map) getSession().getAttribute("rwCtrlTypeMap");
	}

	/**
	 * 往SESSION中设置当前请求的URL对应的数据记录的数据分级信息 功能描述
	 * 
	 * @return 2007-12-25 下午04:55:32
	 * @version 1.0
	 * @author wenjb
	 */
	public static void setRwCtrlTypeMap(Map rwCtrlType) {
		getSession().removeAttribute("rwCtrlTypeMap");
		getSession().setAttribute("rwCtrlTypeMap", rwCtrlType);
	}

	/**
	 * 获取当前请session 功能描述
	 * 
	 * @return 2008-1-3 下午04:55:32
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