package net.silencily.sailing.framework.utils;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * @since 2005-9-7
 * @author 王政
 * @version $Id: RequestHelper.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class RequestHelper {

	/**
	 * 从 reqeust 获取参数, 如果为空, 返回默认值
	 * @param request HttpServletRequest
	 * @param paramName 参数名称
	 * @param defaultValue 默认值
	 * @return 返回值
	 */
	public static String getParam(ServletRequest request, String paramName, String defaultValue) {
		String paramValue = request.getParameter(paramName);
		return StringUtils.isEmpty(paramValue) ? defaultValue : paramValue;
	}
	
}
