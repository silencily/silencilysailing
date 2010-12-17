package net.silencily.sailing.exception;

import org.apache.commons.logging.Log;

/**
 * <p>异常处理的策略接口, 提供通用的异常处理行为. 这个接口被架构的两个基础异常实现, 以提供一致的
 * 行为</p>
 * 
 * @author scott
 * @since 2006-3-22
 * @version $Id: BaseExceptionHandler.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 * @see BaseAppException
 * @see BaseSystemException
 */
public interface BaseExceptionHandler {
    
    /**
     * 处理发生的异常, 设置可读的异常信息, 处理后的异常返回给调用者. 如果参数没有实现<code>ExceptionInfo</code>
     * 接口, 处理后的返回值是对参数的实现了<code>ExceptionInfo</code>的包装. 
     * @param ex        要生成信息的异常类
     * @param clazz     发生异常的类
     * @param method    发生异常的方法
     * @return 处理后的异常信息, 不返回<code>null</code>
     */
    ExceptionInfo handle(Exception ex, Class clazz, String method);

    /**
     * <code>log</code>这个异常及其信息
     * @param info 要生成信息的异常类, 虽然造型为<code>ExceptionInfo</code>, 但这个参数必
     *             须是<code>Exception</code>的子类
     */
    void log(Log logger, ExceptionInfo info);
}
