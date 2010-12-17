package net.silencily.sailing.exception;


/**
 * <p>异常处理架构的工厂类, 当系统中发生异常时调度到恰当的异常处理器处理这个异常. 这个类是为了支
 * 持特定需求情况下对异常处理的扩展</p>
 * @author scott
 * @since 2006-4-1
 * @version $Id: ExceptionDispatcher.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public interface ExceptionDispatcher {
    
    /**
     * 指定一个异常检索这个异常相关的异常处理器
     * @param exp 期望处理的异常
     * @return    这个异常相关的异常处理器
     */
    BaseExceptionHandler dipacher(Throwable exp);
}
