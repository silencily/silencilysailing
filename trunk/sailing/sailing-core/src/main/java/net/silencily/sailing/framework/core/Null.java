package net.silencily.sailing.framework.core;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ����һ��<code>null</code>��ֵ, �������ڳ־û���Ҫ����<code>domain object</code>����
 * ����ʱ������<code>null</code>������, ��ô���<code>null</code>��Ӧ�ú��Ի��ǵ�ȷҪ����
 * ������<code>null</code>��������? �ڼܹ���ͨ���Ĵ��������һ��������<code>null</code>,
 * �ͺ���, �����<code>Null</code>����ȷ��ʹ�����ֵ��<code>null</code>������
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
    
    /** ���ַ���, <code>String</code>���� */
    public final static Null NULL_STRING = new Null("<null>");
    
    /** ����������, <code>java.util.Date</code>���� */
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
