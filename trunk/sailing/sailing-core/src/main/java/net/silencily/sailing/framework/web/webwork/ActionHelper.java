package net.silencily.sailing.framework.web.webwork;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionContext;

/**
 * RequestHelper, Webwork 版本, 注意所有方法必须在 Webwork Action 中调用才有效
 * @since 2005-7-18
 * @author 王政
 * @version $Id: ActionHelper.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class ActionHelper {
    
	public static String getInvocationContextName() {
		return ActionContext.getContext().getActionInvocation().getInvocationContext().getName();
	}
	
	public static String getInvocationContextMethodName() {
		return ActionContext.getContext().getActionInvocation().getProxy().getConfig().getMethodName();
	}
    
    public static Map getParameterMap() {
        return ActionContext.getContext().getParameters();
    }
    
    public static String getParam(String paramName, String defaultValue) {
        Map paramMap = getParameterMap();
        Object paramValue = paramMap.get(paramName);
        if (paramValue == null) {
            return defaultValue;
        }
        if (paramValue.getClass() == String[].class) {
            if ( ((String[]) paramValue).length == 0) {
                return defaultValue;
            } else {
                String returnValue = ((String[]) paramValue)[0];
                return StringUtils.isEmpty(returnValue) ? defaultValue : returnValue;
            }
        } else {
            Assert.isInstanceOf(String.class, paramValue);
            String returnValue = (String) paramValue;
            return StringUtils.isEmpty(returnValue) ? defaultValue : returnValue;
        }
    }
    
    public static String[] getParams(String paramName, String[] defaultValue) {
        Map paramMap = getParameterMap();
        Object paramValue = paramMap.get(paramName);
        if (paramValue == null) {
            return defaultValue;
        }
        if (paramValue.getClass() == String[].class) {
            if ( ((String[]) paramValue).length == 0) {
                return defaultValue;
            } else {
                return (String[]) paramValue;
            }
        } else {
            Assert.isInstanceOf(String.class, paramValue);
            return new String[] { (String) paramValue };
        }
    }
    
    public static HttpSession getSession() {
        return getRequest().getSession();
    }
    
    public static HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }
    
    public static HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }
}
