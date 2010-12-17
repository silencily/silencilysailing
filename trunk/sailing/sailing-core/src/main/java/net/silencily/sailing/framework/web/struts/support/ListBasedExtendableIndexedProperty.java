package net.silencily.sailing.framework.web.struts.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.persistent.BaseDto;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;

/**
 * <p>����<code>List</code>����д����չ����ʵ��, ��<code>java bean specification</code>��
 * ����Ϊ<code>List</code>����������, ���������Ե�ʹ�������ǿ���������������ͬ�ȶԴ���. ����
 * ��<code>List</code>�����ر���</p>
 * <p>ע�����ʵ����<code>fully hibernate-oriented</code>��, ������������ʵ��Ӧ��û������.��
 * Ȼû�в��Թ�. ���Գ���ʱ�����<code>log</code></p>
 * 
 * @author Scott Captain
 * @since 2006-8-25
 * @version $Id: ListBasedExtendableIndexedProperty.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 *
 */
public class ListBasedExtendableIndexedProperty implements ExtendableIndexedProperty {
    private Log logger = LogFactory.getLog(ListBasedExtendableIndexedProperty.class);

    public void extend(Object bean, String propName, int index) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List list = (List) getReadMethod(bean, propName).invoke(bean, null);
        if (list == null) {
            list = new ArrayList();
        } else if (!Hibernate.isInitialized(list)) {
            Hibernate.initialize(list);
            /* 
             * ����ɾ���������ݿ� load ����������, �Դ�ҳ���ύ������Ϊ׼���´���, ��Ϊֻ������
             * �������ֳ�ҳ��ɾ���ı仯. ���Ҫ��ÿ��ҳ���ύʱ��������һ�������� dto. 
             */
            list.clear();
        }

        if (list.size() <= index || list.get(index) == null) {
            Method method = getIndexedReadMethod(bean, propName);
            if (method == null) {
                return;
            }

            Class clazz = method.getReturnType();
            for (int i = list.size(); i < index + 1; i++) {
                list.add(null);
            }
            Object o = null;
            try {
                o = clazz.newInstance();
                setOwnerReferences(bean, o);
                list.set(index, o);
            } catch (Exception e) {
                throw new UnexpectedException("����ͨ��ȱʡ������ʵ����[" + clazz.getClass().getName() + "]", e);
            }
        }
    }

    public Method getReadMethod(Object bean, String name) {
        if (bean == null) {
            return null;
        }
        name = getGetterOrSetterMethodName(name, "get");
        Method method = null;
        try {
            method = bean.getClass().getMethod(name, null);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug(bean.getClass().getName() + "û�ж���" + name + "����");
            }
//            throw new UnexpectedException("û��[" + bean.getClass().getName() + "]��[" + name + "]����", e);
        }
        
        return method;
    }

    public Method getIndexedReadMethod(Object bean, String name) {
        if (bean == null) {
            return null;
        }
        name = getGetterOrSetterMethodName(name, "get");
        Method method = null;
        try {
            method = bean.getClass().getMethod(name, new Class[] {int.class});
            if (method.getReturnType() == void.class) {
                method = null;
            }
        } catch (Exception e) {
            /* ��Ҫ�׳��쳣ʹ����֪�����﷢�������� */
            throw new UnexpectedException("[" + bean.getClass().getName() + "]û�ж���[" + name + "(int)]����", e);
        }
        
        return method;
    }
    
    protected String getGetterOrSetterMethodName(String name, String prefix) {
        char c = Character.toUpperCase(name.charAt(0));
        if (name.length() > 1) {
            name = Character.toString(c) + name.substring(1);
        } else {
            name = Character.toString(c);
        }
        return prefix + name;
    }

    /**
     * ������͵�<code>Hibernate</code>ʽ��<code>parent-child</code>��ϵ����, ����װ
     * һ��ʵ��ļ������Եĸ���Ԫ��ʱ, ������ЩԪ�ض����ʵ�������, ִ�����������ʵ���Ԫ
     * ��Ҫ����<ul>
     * <li>�����������Ե�ʵ��������<code>BaseDto</code>, ���ǲ���<code>parent</code></li>
     * <li>����Ԫ��������<code>BaseDto</code></li>
     * <li>Ԫ�ض����˶�ʵ��(<code>parent</code>)������, ��������������ʵ���������ҵ�һ����ĸСд</li>
     * </ul>
     * @param parent ����<code>child</code>���ϵ�<code>BaseDto</code>
     * @param child  �����е�Ԫ��, <code>one-to-many</code>��ϵ�еĶ��
     */
    protected void setOwnerReferences(Object parent, Object child) {
        if (BaseDto.class.isAssignableFrom(parent.getClass()) && BaseDto.class.isAssignableFrom(child.getClass())) {
            String propName = ClassUtils.getShortClassName(parent.getClass());
            String methodName = getGetterOrSetterMethodName(propName, "set");
            Method method = null;
            try {
                method = child.getClass().getMethod(methodName, new Class[] {parent.getClass()});
                if (method != null) {
                    method.invoke(child, new Object[] {parent});
                }
            } catch (Exception e) {
                /* �޷���� parent-child ����, todo log */
                if (logger.isDebugEnabled()) {
                    logger.debug(parent.getClass().getName() 
                        + "��List�����е�Ԫ��" 
                        + child.getClass().getName() 
                        + "û�ж���" 
                        + methodName 
                        + "����," + e);
                }
            }
        }
    }

}
