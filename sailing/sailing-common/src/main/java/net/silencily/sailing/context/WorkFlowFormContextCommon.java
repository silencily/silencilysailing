package net.silencily.sailing.context;

import javax.servlet.http.HttpSession;

import net.silencily.sailing.security.SecurityContextInfo;



/**
 * 功能说明：此类的存在，完全是因为业务共通没有。
 * @author wenjb
 * @version 1.0
 */
public class WorkFlowFormContextCommon extends BusinessContext {

	/**
	 * 获取当前的SESSION,私有的方法
	 */
	private static HttpSession getHttpSession() {
		return SecurityContextInfo.getSession();
	}

	/**
	 * 功能说明：根据传入的参数，设置相应的FieldStatus
	 */
	public static String getFieldStatus() {
		return (String) getHttpSession().getAttribute("fieldStatus");
	}
	
	/**
	 * 功能说明：标志当前的表单是否是工作流内部的表单
	 * 
	 */
	public static String getTag() {
		// 当前的URL包含TASKID返回"1"，表明在工作流内部,没有的话返回"2"
		String tag = null;
		try {
			tag = (String) getHttpSession().getAttribute("tag");
		} catch (Exception e) {
			tag = "2";
		}
		return tag;
	}
	
	/**
	 * 功能说明：标志当前的URL是否是从已办任务还是从待办任务来的
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
