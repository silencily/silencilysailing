/*
 * Copyright 2004-2005 wangz.
 * Project shufe_newsroom
 */
package net.silencily.sailing.framework.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @since 2005-8-1
 * @author ÍõÕþ
 * @version $Id: ExceptionChainRootCauseAwareSupport.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class ExceptionChainRootCauseAwareSupport implements ExceptionChainRootCauseAware {
    
    private static Log logger = LogFactory.getLog(ExceptionChainRootCauseAwareSupport.class);
    
    private Throwable orignalException;
    
    private Throwable rootCause;
    
    private int count = 0;
    
    public ExceptionChainRootCauseAwareSupport() {
        if (logger.isDebugEnabled()) {
            logger.debug("Enter " + getClass());
        }
    }
    
    /**
     * 
     * @see com.coheg.framework.util.ExceptionChainRootCauseAware#getRootCause(java.lang.Throwable)
     */
    public Throwable getRootCause(Throwable t) {
        if (count == 0) {
            orignalException = t;
        }
        Throwable cause = t.getCause();        
        if (cause != null) {
            count++;
            rootCause = cause;
            cause = getRootCause(cause);
        }
        return count == 0 ? orignalException : rootCause;
    }
    
    /**
     * 
     * @see com.coheg.framework.util.ExceptionChainRootCauseAware#clearResult()
     */
    public void clearResult() {
        orignalException = null;
        rootCause = null;
        count = 0;
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        ExceptionChainRootCauseAwareSupport support = new ExceptionChainRootCauseAwareSupport();
        Exception e = new Exception("1", new RuntimeException("2", new IllegalArgumentException("3")));
        System.out.println(support.getRootCause(e).getClass());
        support.clearResult();
        Exception e2 = new Exception("1");
        System.out.println(support.getRootCause(e2).getClass());
    }

}



