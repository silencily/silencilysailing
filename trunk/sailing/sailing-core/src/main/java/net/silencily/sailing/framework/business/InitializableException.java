package net.silencily.sailing.framework.business;

import net.silencily.sailing.exception.BaseSystemException;
import net.silencily.sailing.exception.ExceptionUtils;

/**
 * 用于表示在初始化工作中发生的错误, 把从{@link Initializable#execute()}方法抛出的任何异常
 * 都包装成这个异常
 * @author zhangli
 * @version $Id: InitializableException.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 * @since 2007-6-4
 */
public class InitializableException extends BaseSystemException {
    
    public InitializableException(Initializable init, Throwable ex) {
        StringBuffer msg = new StringBuffer(init.getClass().getName())
            .append(':').append(ExceptionUtils.getCauseMessage(ex));
        setErrorInformation(msg.toString());
        super.initCause(ex);
    }
}
