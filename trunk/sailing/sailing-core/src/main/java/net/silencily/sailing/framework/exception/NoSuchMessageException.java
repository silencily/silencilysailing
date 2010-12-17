package net.silencily.sailing.framework.exception;

/**
 * ��ʹ��ָ����<code>code</code>����������Ϣʱ, û���ҵ���Ӧ����Ϣ�׳�����쳣
 * 
 * @author scott
 * @since 2006-1-9
 * @version $Id: NoSuchMessageException.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 *
 */
@SuppressWarnings("serial")
public class NoSuchMessageException extends RuntimeException {
    private String code;
    
    public NoSuchMessageException(String code) {
        super("����[" + code + "]û�ж�����Ϣ");
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
}
