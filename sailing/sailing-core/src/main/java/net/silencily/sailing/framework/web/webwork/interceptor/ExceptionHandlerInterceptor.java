/*
 * Copyright 2004-2005 wangz.
 * Project shufe_newsroom
 */
package net.silencily.sailing.framework.web.webwork.interceptor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ObjectUtils;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.TextProvider;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * 异常处理, 用于处理所有 action 异常, 将流转到 "globalException" 对应的页面 /common/webwork/exceptionHandler.jsp
 * @since 2005-7-27
 * @author 王政
 * @version $Id: ExceptionHandlerInterceptor.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class ExceptionHandlerInterceptor implements Interceptor {

    public static final String EXCEPTION_ERROR_MESSAGE_SUFFIX = ".errorMessage";

	private static Log logger = LogFactory.getLog(ExceptionHandlerInterceptor.class);

    public static final String EXCEPTION_ATTRIBUTE = "com_coheg_webwork_exception";
    
    public static final String GLOBAL_ERROR = "globalException";
      
    public String intercept(ActionInvocation invocation) throws Exception {
        try {
            return invocation.invoke();
        } catch (Exception e) {
        	e.printStackTrace();
            if (logger.isErrorEnabled()) {
                logger.error("Error in webwork action : " + invocation.getAction().getClass(), e);
            }
            
            return doException(invocation, e);
        }       
    }

	public String doException(ActionInvocation invocation, Exception e) {
		Action action = (Action) invocation.getAction();
		
		if ( action instanceof TextProvider ) {
		    TextProvider textProvider = (TextProvider) action;
//                ExceptionChainRootCauseAware aware = new ExceptionChainRootCauseAwareSupport();
//                Throwable cause = aware.getRootCause(e);
		    
		    String displayText = getDisplayText(e, textProvider, null);                
		    setExceptionMessage(displayText);
		} else {                
		    setExceptionMessage(e.getMessage());
		}
		            
		return GLOBAL_ERROR;
	}

    /**
     * TODO 可以加上对不同类型 Exception 的不同处理
     * @param e
     * @param textProvider
     * @return
     */
    public static String getDisplayText(Throwable e, TextProvider textProvider, List args) {
        Class exceptionClazz = e.getClass();
        // Il8N Properties File should involve property like this:
        // DataAccessException.errorMessage = DataAccessError        
        String displayText = getDisplayTextInRecursion(exceptionClazz, textProvider, args);
        displayText = ObjectUtils.nullSafeEquals(displayText, "java.lang.Throwable.errorMessage") ? e.getMessage() : displayText;
        return displayText;
    }
    
    private static String getDisplayTextInRecursion(Class clazz, TextProvider textProvider,  List args) {
        String textKey = clazz.getName() + EXCEPTION_ERROR_MESSAGE_SUFFIX;     
        String displayText = textProvider.getText(textKey, args);
        while (ObjectUtils.nullSafeEquals(textKey, displayText)) {
            Class superClazz = clazz.getSuperclass();
            if (superClazz != Throwable.class) {
                displayText = getDisplayTextInRecursion(superClazz, textProvider, args);
            } else {
                displayText= "java.lang.Throwable.errorMessage";
                break;
            }
        }
        return displayText;
    }
    
    private static void setExceptionMessage(String exceptionMessage) {
        ServletActionContext.getRequest().setAttribute(EXCEPTION_ATTRIBUTE, exceptionMessage);
    }

    public void destroy() {
    }

    public void init() {
    }
    
    /**
     * Other strategy, use validationAware display errors
     * @author Administrator
     *
     */
//    private static class ExceptionHandlerListener implements PreResultListener {
//        private ActionInvocation invocation;
//
//        private boolean beforeResult = true;
//
//        private String result = Action.ERROR;
//
//        public ExceptionHandlerListener(ActionInvocation invocation) {
//            this.invocation = invocation;
//            invocation.addPreResultListener(this);
//        }
//
//        String invoke() throws Exception {
//            try {
//                result = invocation.invoke();
//            } catch (Exception e) {
//                if (beforeResult) {
//
//                    logger.warn("There was an error executing the Action", e);
//                    populateErrors(invocation, e);
//                    return Action.ERROR;
//                } else {
//                    logger.error("There was after error executing the result.", e);
//                    // it doesn't really matter what we return, since the result
//                    // has already been mapped
//                    throw new Exception(e);
//                    /*
//                     * if we are getting an exception after results, things may
//                     * be too bad to handle. Perhaps we should map to a global
//                     * result page
//                     */
//                }
//            }
//            return result;
//        }
//
//        public void beforeResult(ActionInvocation invocation, String resultCode) {
//            beforeResult = false;
//            result = resultCode;
//        }
//
//        private void populateErrors(ActionInvocation invocation, Exception e) {
//            Action action = invocation.getAction();
//
//            if (action instanceof ValidationAware) {
//                ValidationAware validationAware = (ValidationAware) action;
//                String message;
//                if (action instanceof TextProvider) {
//                    // example key -->exception.save.ServiceException = there is
//                    // a problem with your service dude"
//                    String key = "exception." + invocation.getInvocationContext().getName() + "." + e.getClass().getName();
//                    message = ((TextProvider) action).getText(key);
//                    if (message.equals(key)) {
//                        message = e.getMessage(); 
//                        // assume the message on the exception is correct/already looked up
//                    }
//                } else {
//                    message = "Problem invoking:"
//                            + invocation.getInvocationContext().getName()
//                            + " . cause:" + e.getMessage();
//                }
//                validationAware.addActionError(message);
//            }
//        }
//
//    }



}
