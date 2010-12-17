package net.silencily.sailing.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * һ������<code>log</code>����, ����ʹ<code>log</code>��Ϣ�����пɶ���
 * 
 * @author scott
 * @since 2005-12-28
 * @version $Id: DebugUtils.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public abstract class DebugUtils {
    
    /**
     * ������ʾ<code>Collection</code>������, ���е�Ԫ��Ӧ��Ϊ<code>String</code>����
     * , ���򽫵�������<code>toString</code>����ת����<code>String</code>
     * 
     * @param list Ҫ��ʾ��<code>Collection</code>
     * @return ����ϵͳƽ̨Ĭ�ϵĻ��з����ַ���
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
     * ������ʾ��������
     * @param objs Ҫ��ʾ������
     * @return ����ϵͳƽ̨Ĭ�ϵĻ��з����ַ���
     */
    public static String convertMultiLines(Object[] objs) {
        return convertMultiLines(Arrays.asList(objs));
    }
}
