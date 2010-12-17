package net.silencily.sailing.exception;

/**
 * <p>ÿ���쳣��Ӧ�ð�������Ϣ, �������ڿͻ��˵Ĵ�����ʾ��Ϣ. �ܹ���������������쳣��ʵ�������
 * �ӿ�, ���ṩһ�µ��쳣������Ϊ</p>
 * 
 * @author scott
 * @since 2006-3-22
 * @version $Id: ExceptionInfo.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 * @see BaseAppException
 * @see BaseSystemException
 */
public interface ExceptionInfo {
    
    /**
     * ���û������<code>MessageFormat</code>�滻�Ĳ���, �����������
     */
    Object[] NULL_PARAM = new Object[0];

    /**
     * ����������Ĵ�����Ϣ, �����Ϣ���ڿͻ��˵���ʾ��<code>log</code>, ��Ӧ�����ڿ������ж�
     * ������. 
     * @return ������쳣��Ϣ, ������<code>empty</code>��<code>null</code>
     */
    String getErrorInformation();
    
    /**
     * <p>�������õ�������ʾ��Ϣ�ķ���, Ӧ������������²�Ҫʹ���������, �ͻ�������Ӧ����������Դ��
     * ����, <b>ʵ��ע��:</b>�������ָ����<code>empty</code>��<code>null</code>�ͺ�����
     * ����������ֵ</p>
     * <p><code>BaseExceptionHandler</code>�������������ͻ�����Ϣ</p>
     * @param msg �ṩ<code>log</code>����ʾ����Ϣ
     */
    void setErrorInformation(String msg);
    
    /**
     * ���������쳣�����滻�Ĳ���ֵ
     * @return ���û�в����滻����{@link #NULL_PARAM <code>NULL_PARAM</code>}
     */
    Object[] getParams();
    
    /**
     * ����һ���쳣��Ϣ��Ӧ��<code>key</code>, ���<code>key</code>����������Ӧ���ⲿ����Ϣ
     * @return ���ֱ����������Ϣ(ͨ��������)��û��<code>key</code>����<code>null</code>
     */
    String getKey();
    
    /**
     * �������<code>ExceptionInfo</code>��ʾ���쳣
     * @return �������ʵ��ʱ���쳣, ������<code>null</code>
     */
    Throwable getThrowable();
}
