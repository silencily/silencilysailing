package net.silencily.sailing.framework.persistent.common;

import java.beans.PropertyDescriptor;

/**
 * ���־û�һ��<code>DTO</code>ʱ, ����������ù涨���Ƶ�����ֵ. ����<code>PropertyPadding</code>
 * ��. ���統����һ���µ�<code>DTO</code>ʱ, ���ǿ��Բ�����<code>id</code>, ��������ӿڵ�
 * ʵ�������<code>id</code>ֵ���趨. ������ʵ��ͨ����������<code>insert</code>����
 * @author scott
 * @since 2006-4-13
 * @version $Id: PropertyPaddingStrategy.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public interface PropertyPaddingStrategy {
    
    /**
     * ����һ��<code>bean</code>����Ҫ���õ����Ե�����, ������Ե�����
     * @param bean Ҫ��������ֵ��<code>bean</code>
     * @param desc Ҫ��������ֵ����������
     * @return ��������ֵ���<code>bean</code>
     */
    Object padding(Object bean, PropertyDescriptor desc);
    
    /**
     * �����֧�ֵ���������, ����һ��<code>bean' class</code>�������������������ʱ����<code>
     * true</code>, ��ô�ܹ���ί��<code>padding</code>������������. ���ռܹ�<code>DTO</code>
     * �Ĺ涨, <code>id</code>, <code>version</code>, <code>operatorId</code>, <code>
     * createdTime</code>, <code>lastModifiedTime</code>������������Ŀ��, �����⼸������
     * ʵ�ֲ�Ӧ���ж�<code>clazz</code>����
     * @param clazz �������Ե�<code>bean class</code>
     * @param name  ��������
     * @return      �Ƿ����������
     */
    boolean supportProperty(Class clazz, String name);
}
