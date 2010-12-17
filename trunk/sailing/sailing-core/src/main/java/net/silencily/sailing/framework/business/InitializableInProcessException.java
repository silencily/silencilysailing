package net.silencily.sailing.framework.business;

import net.silencily.sailing.exception.BaseSystemException;

/**
 * 正在执行{@link Initializable}的初始化过程中对这个服务的方法执行了调用时抛出这个异常
 * @author zhangli
 * @version $Id: InitializableInProcessException.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 * @since 2007-6-4
 */
public class InitializableInProcessException extends BaseSystemException {
    
    public InitializableInProcessException(Initializable init) {
        super(new String[] {init.getClass().getName()});
    }
}
