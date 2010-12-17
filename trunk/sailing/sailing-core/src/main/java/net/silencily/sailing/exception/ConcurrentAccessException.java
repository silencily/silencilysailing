package net.silencily.sailing.exception;

/**
 * 相对独立的一种异常, 每个操作都可能发生这种异常, 可以使用缺省的异常信息. 也可以提供更详细的信息
 * 以更好地提示用户. 系统已经配置了错误信息, 如果没有指定更具体的信息就使用默认的错误信息
 * @author scott
 * @since 2006-4-9
 * @version $Id: ConcurrentAccessException.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
public class ConcurrentAccessException extends BaseBusinessException {
    public ConcurrentAccessException() {
        super();
    }
    
    public ConcurrentAccessException(String msg) {
        super(new Object[] {msg});
    }
}
