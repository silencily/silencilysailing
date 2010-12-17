package net.silencily.sailing.exception;

/**
 * ��Զ�����һ���쳣, ÿ�����������ܷ��������쳣, ����ʹ��ȱʡ���쳣��Ϣ. Ҳ�����ṩ����ϸ����Ϣ
 * �Ը��õ���ʾ�û�. ϵͳ�Ѿ������˴�����Ϣ, ���û��ָ�����������Ϣ��ʹ��Ĭ�ϵĴ�����Ϣ
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
