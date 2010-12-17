package net.silencily.sailing.exception;

import org.apache.commons.logging.Log;

/**
 * <p>�쳣����Ĳ��Խӿ�, �ṩͨ�õ��쳣������Ϊ. ����ӿڱ��ܹ������������쳣ʵ��, ���ṩһ�µ�
 * ��Ϊ</p>
 * 
 * @author scott
 * @since 2006-3-22
 * @version $Id: BaseExceptionHandler.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 * @see BaseAppException
 * @see BaseSystemException
 */
public interface BaseExceptionHandler {
    
    /**
     * ���������쳣, ���ÿɶ����쳣��Ϣ, �������쳣���ظ�������. �������û��ʵ��<code>ExceptionInfo</code>
     * �ӿ�, �����ķ���ֵ�ǶԲ�����ʵ����<code>ExceptionInfo</code>�İ�װ. 
     * @param ex        Ҫ������Ϣ���쳣��
     * @param clazz     �����쳣����
     * @param method    �����쳣�ķ���
     * @return �������쳣��Ϣ, ������<code>null</code>
     */
    ExceptionInfo handle(Exception ex, Class clazz, String method);

    /**
     * <code>log</code>����쳣������Ϣ
     * @param info Ҫ������Ϣ���쳣��, ��Ȼ����Ϊ<code>ExceptionInfo</code>, �����������
     *             ����<code>Exception</code>������
     */
    void log(Log logger, ExceptionInfo info);
}
