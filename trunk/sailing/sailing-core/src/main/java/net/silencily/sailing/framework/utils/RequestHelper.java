package net.silencily.sailing.framework.utils;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * @since 2005-9-7
 * @author ����
 * @version $Id: RequestHelper.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class RequestHelper {

	/**
	 * �� reqeust ��ȡ����, ���Ϊ��, ����Ĭ��ֵ
	 * @param request HttpServletRequest
	 * @param paramName ��������
	 * @param defaultValue Ĭ��ֵ
	 * @return ����ֵ
	 */
	public static String getParam(ServletRequest request, String paramName, String defaultValue) {
		String paramValue = request.getParameter(paramName);
		return StringUtils.isEmpty(paramValue) ? defaultValue : paramValue;
	}
	
}
