package net.silencily.sailing.framework.codename;

import net.silencily.sailing.exception.BaseBusinessException;

/**
 * �������µ�<code>CodeName</code>ʱ��֤��Ч��
 * @author zhangli
 * @since 2007-3-22
 * @version $Id: InvalidCodeNameException.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 */
public class InvalidCodeNameException extends BaseBusinessException {
    
    /** �������޸�û����д<code>code</code>��<code>name</code> */
    private static String KEY_NULL = CodeName.class.getName() + ".null";
    
    /** �������޸�<code>CodeName</code>ʱ<code>code</code>��<code>name</code>����Խ�� */
    private static String KEY_EXCEED = CodeName.class.getName() + ".exceed";
    
    /**
     * ����<code>code</code>��<code>name</code>�ǿ�ֵʱ�Ĵ���
     */
    public InvalidCodeNameException() {
        super(KEY_NULL, new Object[] {});
    }
    
    public InvalidCodeNameException(String propertyName, int limitation) {
        super(KEY_EXCEED, new Integer[] {new Integer(limitation)});
    }
}
