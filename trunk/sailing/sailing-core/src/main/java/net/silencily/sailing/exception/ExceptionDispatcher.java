package net.silencily.sailing.exception;


/**
 * <p>�쳣����ܹ��Ĺ�����, ��ϵͳ�з����쳣ʱ���ȵ�ǡ�����쳣��������������쳣. �������Ϊ��֧
 * ���ض���������¶��쳣�������չ</p>
 * @author scott
 * @since 2006-4-1
 * @version $Id: ExceptionDispatcher.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public interface ExceptionDispatcher {
    
    /**
     * ָ��һ���쳣��������쳣��ص��쳣������
     * @param exp ����������쳣
     * @return    ����쳣��ص��쳣������
     */
    BaseExceptionHandler dipacher(Throwable exp);
}
