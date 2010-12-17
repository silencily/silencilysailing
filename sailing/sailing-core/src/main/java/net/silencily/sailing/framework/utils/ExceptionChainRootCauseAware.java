/*
 * Copyright 2005-2010 the original author or autors
 *  
 *    http://www.coheg.com.cn
 *
 * Project { cohegFramwork }
 */
package net.silencily.sailing.framework.utils;

/**
 * <class>ExceptionChainRootCauseAware</class> 用于得到一个 Exception Chain 最顶级的 cause, 适用于 JDK1.4 以后版本
 * @see java.lang.Throwable#getCause()
 * @since 2005-8-5
 * @author 王政
 * @version $Id: ExceptionChainRootCauseAware.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public interface ExceptionChainRootCauseAware {
    
    /**
     * 得到一个 Exception Chain 最顶级的 cause, 用递归方法获取
     * @param t 源异常
     * @return The Root Cause Of A Exception Chain
     */
    Throwable getRootCause(Throwable t);
    
    /**
     * 清空上次运算结果, 可以下一次运算
     *
     */
    void clearResult();
    
}



