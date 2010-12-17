package net.silencily.sailing.exception;


/**
 * 系统中所有<b>业务异常</b>的超类, 业务异常是设计开发过程中可以预料到的错误情况. 处理这类异常是
 * 开发过程的一部分. 架构异常处理机制为这类异常查找恰当的错误代码, 并解决配置在代码之外的错误信息
 * @author scott
 * @since 2006-4-1
 * @version $Id: BaseBusinessException.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public abstract class BaseBusinessException extends BaseSystemException {
    public BaseBusinessException() {
        super();
    }
    
    public BaseBusinessException(String key, Object[] params) {
        super(key, params);
    }
    
    public BaseBusinessException(Object[] params) {
        super(params);
    }
}
