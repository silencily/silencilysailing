/*
 * Copyright 2005-2010 the original author or autors
 *  
 *    http://www.coheg.com.cn
 *
 * Project { cohegFramwork }
 */
package net.silencily.sailing.framework.utils;

/**
 * <class>ExceptionChainRootCauseAware</class> ���ڵõ�һ�� Exception Chain ����� cause, ������ JDK1.4 �Ժ�汾
 * @see java.lang.Throwable#getCause()
 * @since 2005-8-5
 * @author ����
 * @version $Id: ExceptionChainRootCauseAware.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public interface ExceptionChainRootCauseAware {
    
    /**
     * �õ�һ�� Exception Chain ����� cause, �õݹ鷽����ȡ
     * @param t Դ�쳣
     * @return The Root Cause Of A Exception Chain
     */
    Throwable getRootCause(Throwable t);
    
    /**
     * ����ϴ�������, ������һ������
     *
     */
    void clearResult();
    
}



