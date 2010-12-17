package net.silencily.sailing.framework.exception.persistent;

import net.silencily.sailing.framework.exception.SystemException;

/**
 * ���е����ݿ���ص��쳣ʵ������ӿ�
 * @author zhangli
 * @version $Id: DatabaseException.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 * @since 2007-4-23
 */
public interface DatabaseException extends SystemException {
    
    /**
     * ���������쳣�����ݿ�<code>sql</code>���
     * @return �����쳣�����ݿ�<code>sql</code>���, ���������е��쳣�������ֵ
     */
    String getSql();
    
    /**
     * ���������쳣�����ݿ����
     * @return �쳣��ص����ݿ����, Ŀǰ����ʹ�õ����ݿ��Ʒ���������ֵ
     */
    String getSqlCode();
}
