package net.silencily.sailing.framework.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 一组断言的方法, 方便执行方法对参数的验证
 * @author zhangli
 * @version $Id: AssertUtils.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 * @since 2007-4-22
 */
public abstract class AssertUtils {
    
    public static final String NULL_VALUE_MESSAGE = "参数是空值"; 
    
    public static void notNull(Object value) {
        notNull(value, null);
    }
    
    /**
     * 用于判断方法调用时参数是否为<code>null</code>, 对于<code>String</code>类型的包括<cpde>empty</code>,
     * 如果参数是空抛出<code>NullPointerExpcetion</code>
     * @param value     要判断的参数
     * @param message   当参数是<code>null</code>,<code>empty</code>时的异常信息
     */
    public static void notNull(Object value, String message) {
        if (value == null || (value.getClass() == String.class && StringUtils.isBlank(value.toString()))) {
            if (StringUtils.isBlank(message)) {
                message = NULL_VALUE_MESSAGE;
            }
            throw new NullPointerException(message);
        }
    }
}
