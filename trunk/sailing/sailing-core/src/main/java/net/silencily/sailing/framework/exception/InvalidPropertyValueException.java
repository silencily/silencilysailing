package net.silencily.sailing.framework.exception;

import net.silencily.sailing.exception.BaseBusinessException;

/**
 * ����쳣���ڱ���һ��<tt>ҵ��ʵ��</tt>����������Ч��, ��������֤������ǰ��<code>form</code>
 * �е�����ʧ��ʱ���쳣
 * @@InvalidPropertyValueException: ����:����{0}��ֵ��{1}, {2}
 * @author scott
 * @since 2006-3-30
 * @version $Id: InvalidPropertyValueException.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 *
 */
public class InvalidPropertyValueException extends BaseBusinessException {
    
    /**
     * ������֤ʧ���쳣
     * @param propertyName ������������������, ����:<tt>����</tt>
     * @param value        ��������ֵ, ����:<tt>������������������������������������������������</tt>
     * @param cause        ��������ԭ��, ����:<tt>�й��˲�Ӧ������ô��������,������?</tt>
     */
    public InvalidPropertyValueException(String propertyName, String value, String cause) {
        super(new Object[] {propertyName, value, cause});
    }
}
