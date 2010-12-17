package net.silencily.sailing.framework.persistent.common;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import net.silencily.sailing.framework.persistent.BaseDto;

import org.apache.commons.lang.StringUtils;

/**
 * �������ò��Ե�ȱʡʵ��, �������ԵĶ�д����. ֧��<code>BaseDto</code>���������������
 * @author scott
 * @since 2006-4-13
 * @version $Id: AbstractPropertyPaddingStrategy.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public abstract class AbstractPropertyPaddingStrategy implements PropertyPaddingStrategy {
    protected Object[] NON_PARAMETER = new Object[0];

    public Object padding(Object bean, PropertyDescriptor desc) {
        Method reader = desc.getReadMethod();
        try {
            if (isShouldSet(bean, desc, reader.invoke(bean, NON_PARAMETER))) {
                Method writer = desc.getWriteMethod();
                if (writer != null) {
                    writer.invoke(bean, new Object[] {settingWith()});
                } else {
                    throw new IllegalStateException("����[" + desc.getName() + "]û��д����");
                }
            }
        } catch (Exception e) {
            String msg = "��ȡ/����bean[" + bean.getClass().getName() + "]������"
                + desc.getName()
                + "]ʱ��������";
            if (e.getMessage() != null) {
                msg += "," + e.getMessage();
            }

            IllegalStateException ex = new IllegalStateException(msg);
            if (!(e instanceof IllegalStateException)) {
                ex.initCause(e);
            }
            
            throw ex;
        }

        return bean;
    }

    public boolean supportProperty(Class clazz, String name) {
        return BaseDto.class.isAssignableFrom(clazz) && StringUtils.isNotBlank(name) && isDesirableProperty(name);
    }

    /**
     * �ж�<code>bean</code>�������Ƿ���Ҫ����
     * @param bean     ִ���������Ե�<code>bean</code>, ��<code>BaseDto</code>������
     * @param desc     Ҫ����������
     * @param result   ��<code>bean</code>�����Է������صĽ��
     * @return ��������Ƿ���Ҫ����, �������<code>true</code>, ������<code>settingWith</code>
     *         ����
     */
    abstract protected boolean isShouldSet(Object bean, PropertyDescriptor desc, Object result);
    
    /**
     * ���Ӧ������������Ե�ֵ��ô��ʹ�������������, ����<code>isShouldSet</code>��������
     * <code>true</code>�������, ʹ�����ֵ��Ϊ���Ե�ֵ
     */
    abstract protected Object settingWith();
    
    /**
     * ������Ƶ���������Ҫ���õ�������?
     * @param propertyName Ҫ����������, ��Զ����<code>null</code>��<code>empty</code>
     * @return �Ƿ���Ҫ���õ�����
     */
    abstract protected boolean isDesirableProperty(String propertyName);
}
