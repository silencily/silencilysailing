package net.silencily.sailing.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * 一个方便<code>log</code>的类, 用于使<code>log</code>信息更具有可读性
 * 
 * @author scott
 * @since 2005-12-28
 * @version $Id: DebugUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public abstract class DebugUtils {
    
    /**
     * 分行显示<code>Collection</code>的内容, 其中的元素应该为<code>String</code>类型
     * , 否则将调用它的<code>toString</code>方法转换成<code>String</code>
     * 
     * @param list 要显示的<code>Collection</code>
     * @return 具有系统平台默认的换行符的字符串
     */
    public static String convertMultiLines(Collection list) {
        StringBuffer sb = new StringBuffer();
        String separator = System.getProperty("line.separator");
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            sb.append(separator).append(it.next());
        }
        
        return sb.toString();
    }
    
    /**
     * 分行显示数组内容
     * @param objs 要显示的数组
     * @return 具有系统平台默认的换行符的字符串
     */
    public static String convertMultiLines(Object[] objs) {
        return convertMultiLines(Arrays.asList(objs));
    }
}
