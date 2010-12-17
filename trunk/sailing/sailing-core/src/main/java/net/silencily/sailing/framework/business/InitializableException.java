package net.silencily.sailing.framework.business;

import net.silencily.sailing.exception.BaseSystemException;
import net.silencily.sailing.exception.ExceptionUtils;

/**
 * ���ڱ�ʾ�ڳ�ʼ�������з����Ĵ���, �Ѵ�{@link Initializable#execute()}�����׳����κ��쳣
 * ����װ������쳣
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
