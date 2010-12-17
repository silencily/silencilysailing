package net.silencily.sailing.exception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;

/**
 * �쳣�����<code>utility</code>��, �ṩһЩ����ķ���
 * @author scott
 * @since 2006-4-14
 * @version $Id: ExceptionUtils.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public abstract class ExceptionUtils {
    public static final String DEFAULT_MESSAGE_SEPARATOR = System.getProperty("line.separator");
    /**
     * ����<code>InvocationTargetException</code>, <code>UnexpectedException</code>
     * ��ʵ�ʰ������쳣��Ϣ, �������������������;ͷ���ԭ���Ĳ���
     * @param ex Ҫ�������쳣
     * @return ʵ�ʵ��쳣��Ϣ
     */
    public static Throwable getActualThrowable(Throwable ex) {
        if (ex == null) {
            return ex;
        }
        Throwable e = null;
        /* ���ټ������뵽UnexpectedException, �ڴ��������������������ʾʱ��Ҫ��, ����
         * ��ӳ����ʵ���쳣����(ֻ��)�� log �����ֳ���
         */
        if (ex instanceof InvocationTargetException) {
            e = ((InvocationTargetException) ex).getTargetException();
//        } else if (ex instanceof UnexpectedException) {
//            e = ((UnexpectedException) ex).getActual();
        } else if (ex instanceof UndeclaredThrowableException) {
            e = ((UndeclaredThrowableException) ex).getUndeclaredThrowable();
        } else if (ex instanceof ServletException) {
            e = ((ServletException) ex).getRootCause();
        }
        
        if (e == null) {
            e = ex;
        } else if (e != ex){
            e = getActualThrowable(e);
        }

        return e;
    }
    
    /**
     * �Ӷ��㿪ʼ��������Ƕ�׵��쳣��Ϣ���<code>String</code>, ��Ϣ֮����ϵͳȱʡ�Ļ��з��ָ�
     * @param ex ������Ϣ���쳣
     * @return   �쳣��Ϣ
     */
    public static String getCauseMessage(Throwable ex) {
        return getCauseMessage(ex, DEFAULT_MESSAGE_SEPARATOR);
    }

    /**
     * �Ӷ��㿪ʼ��������Ƕ�׵��쳣��Ϣ���<code>String</code>, ��Ϣ֮���ɲ���<code>separator</copde>
     * �ָ�
     * @param ex ������Ϣ���쳣
     * @param separator �ָ���Ϣ�ķָ���
     * @return   �쳣��Ϣ
     */
    public static String getCauseMessage(Throwable ex, String separator) {
        Throwable root = ex, previous = null;
        List messages = new ArrayList(10);
        while (root != null && root != previous) {
            messages.add(root.getMessage());
            previous = root;
            root = root.getCause();
        }
        
        StringBuffer msg = new StringBuffer();
        for (int i = 0; i < messages.size(); i++) {
            String m = (String) messages.get(i);
            if (StringUtils.isNotBlank(m)) {
                if (msg.length() > 0) {
                    msg.append(separator);
                }
                msg.append(m);
            }
        }

        String message = msg.toString();
        
        if (ex == null) {
            message = "����ʹ��" 
                + ExceptionUtils.class.getName()
                + "��getCauseMessage()����,������null";
        } else if (StringUtils.isBlank(message)) {
            message = ex.getClass().getName();
        }

        return message;
    }
}
