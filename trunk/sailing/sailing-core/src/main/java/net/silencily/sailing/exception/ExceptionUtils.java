package net.silencily.sailing.exception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;

/**
 * 异常处理的<code>utility</code>类, 提供一些方便的方法
 * @author scott
 * @since 2006-4-14
 * @version $Id: ExceptionUtils.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public abstract class ExceptionUtils {
    public static final String DEFAULT_MESSAGE_SEPARATOR = System.getProperty("line.separator");
    /**
     * 检索<code>InvocationTargetException</code>, <code>UnexpectedException</code>
     * 中实际包含的异常信息, 如果参数不是上面的类型就返回原来的参数
     * @param ex 要检索的异常
     * @return 实际的异常信息
     */
    public static Throwable getActualThrowable(Throwable ex) {
        if (ex == null) {
            return ex;
        }
        Throwable e = null;
        /* 不再继续深入到UnexpectedException, 在大多数情况下这就是我们显示时需要的, 而它
         * 反映的真实的异常可以(只能)从 log 中体现出来
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
     * 从顶层开始检索所有嵌套的异常信息组成<code>String</code>, 信息之间由系统缺省的换行符分隔
     * @param ex 检索信息的异常
     * @return   异常信息
     */
    public static String getCauseMessage(Throwable ex) {
        return getCauseMessage(ex, DEFAULT_MESSAGE_SEPARATOR);
    }

    /**
     * 从顶层开始检索所有嵌套的异常信息组成<code>String</code>, 信息之间由参数<code>separator</copde>
     * 分隔
     * @param ex 检索信息的异常
     * @param separator 分隔信息的分隔符
     * @return   异常信息
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
            message = "错误使用" 
                + ExceptionUtils.class.getName()
                + "的getCauseMessage()方法,参数是null";
        } else if (StringUtils.isBlank(message)) {
            message = ex.getClass().getName();
        }

        return message;
    }
}
