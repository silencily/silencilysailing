package net.silencily.sailing.framework.core;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 表现一个<code>null</code>的值, 典型用于持久化层要根据<code>domain object</code>检索
 * 数据时其中有<code>null</code>的属性, 那么这个<code>null</code>是应该忽略还是的确要检索
 * 属性是<code>null</code>的数据呢? 在架构中通常的处理是如果一个属性是<code>null</code>,
 * 就忽略, 如果是<code>Null</code>就明确地使用这个值是<code>null</code>的属性
 * @author scott
 * @since 2006-3-27
 * @version $Id: Null.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 *
 */
@SuppressWarnings("serial")
public final class Null implements Serializable {
    private Object value;
    private static Map registries = new HashMap(20);
    
    private Null(Object value) {
        this.value = value;
        Class clazz = value.getClass();
        registries.put(clazz, this);
    }

    public static Null valueOf(Class clazz) {
        return (Null) registries.get(clazz);
    }
    
    public static Class typeOf(Null n) {
        return n.value.getClass();
    }
    
    /** 空字符串, <code>String</code>类型 */
    public final static Null NULL_STRING = new Null("<null>");
    
    /** 空日期类型, <code>java.util.Date</code>类型 */
    public final static Null NULL_DATE = new Null(new Date(0));
    
    public int hasCode() {
        return value.hashCode();
    }
    
    public boolean equals(Object o) {
        boolean ret = false;
        if (o instanceof Null) {
            ret = this.value == ((Null) o).value;
        }
        return ret;
    }
}
