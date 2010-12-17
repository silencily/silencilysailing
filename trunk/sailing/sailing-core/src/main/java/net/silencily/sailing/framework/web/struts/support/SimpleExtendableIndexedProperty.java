package net.silencily.sailing.framework.web.struts.support;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p><code>ExtendableIndexedProperty</code>�ļ�ʵ��, ����<code>PropertyUtilBean</code>
 * ����չ, ��<code>PropertyUtilBean</code>������Ϊ�˱���ͨ��������߻��淴��Ľ���ṩЧ��,
 * ���е��쳣����ʹ��<code>PropertyUtilsBean</code>, ֻ��һ���ڴ���<code>dto</code>ʱ�׳�
 * ���쳣</p>
 * @author scott
 * @since 2006-3-29
 * @version $Id: SimpleExtendableIndexedProperty.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 *
 */
public class SimpleExtendableIndexedProperty implements ExtendableIndexedProperty {
    private PropertyUtilsBean utils;
    
    private Log logger = LogFactory.getLog(PropertyUtilsBean.class);
    
    private boolean isInfo;
    
    public SimpleExtendableIndexedProperty(PropertyUtilsBean utils) {
        this.utils = utils;
        isInfo = logger.isInfoEnabled();
    }
    
    /* ������κβ���, �ڵ����������֮ǰ��Щ�����Ѿ��������. ���������һ��Ҫ������������ */
    public void extend(Object bean, String propName, int index) 
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        
        PropertyDescriptor propDesc = utils.getPropertyDescriptor(bean, propName);
        
        if (!(propDesc instanceof IndexedPropertyDescriptor)) {
            logger.warn("��չbean�������Ե�ֵʱ["
                + bean.getClass().getName()
                + "������["
                + propName
                + "]������������");
            return;
        }
        
        IndexedPropertyDescriptor indexedPropDesc = (IndexedPropertyDescriptor) propDesc;
        Object dtos = propDesc.getReadMethod().invoke(bean, null);
        if (dtos == null) {
            createFromNull(bean, index, indexedPropDesc);
        } else {
            int size = 0;
            if (dtos.getClass().isArray()) {
                size = ((Object[]) dtos).length;
            } else if (List.class.isAssignableFrom(dtos.getClass())) {
                size = ((List) dtos).size();
            }
            
            if (size <= index) {
                fillToIndex(bean, index, indexedPropDesc, dtos);
            } else if (dtos.getClass().isArray()) {
                Object obj = ((Object[]) dtos)[index];
                if (obj == null) {
                    ((Object[]) dtos)[index] = createDto(indexedPropDesc);
                }
            } else if (List.class.isAssignableFrom(dtos.getClass())) {
                Object obj = ((List) dtos).get(index);
                if (obj == null) {
                    ((List) dtos).set(index, createDto(indexedPropDesc));
                }
            }
        }
    }
    
    /**
     * ���ʹ�÷������ķ���<code>getXXX()</code>������<code>null</code>�ʹ�����СΪ<code>index</code>
     * �������<code>List</code>
     * @param bean  Ҫ��װ��<code>bean</code>
     * @param index ��װ�������<code>List</code>�Ĵ�С
     * @param propDesc Ҫ�������Ե�<code>PropertyDescriptor</code>
     */
    private void createFromNull(Object bean, int index, IndexedPropertyDescriptor propDesc) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        List list = new ArrayList(index + 1);
        for (int i = 0; i < index; i++) {
            list.add(null);
        }
        list.add(createDto(propDesc));
        
        Method writer = propDesc.getWriteMethod();
        Class paramType = writer.getParameterTypes()[0];
        
        if (List.class.isAssignableFrom(paramType)) {
            writer.invoke(bean, new Object[] {list});
        } else if (paramType.isArray()) {
            writer.invoke(
                bean, 
                new Object[] {list.toArray((Object[]) Array.newInstance(paramType.getComponentType(), list.size()))});
        } else {
            logger.warn("ִ��["
                + bean.getClass().getName()
                + "]��������"
                + propDesc.getName()
                + "��д����ʱ�����Ƿ�������["
                + paramType.getName()
                + "]");
        }
    }
    
    /**
     * �������Եĳ��Ȳ���, ʹ��<code>DTO</code>ʵ����䵽<code>index + 1</code>
     * @param bean      ����Ҫ�������Ե�<code>bean</code>
     * @param index     ��䵽�������
     * @param desc      Ҫ�������Ե�<code>IndexedPropertyDescription</code>
     */
    private void fillToIndex(Object bean, int index, IndexedPropertyDescriptor desc, Object dtos) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        Method writer = desc.getWriteMethod();
        Object[] params = null;
        
        if (dtos.getClass().isArray()) {
            int len = ((Object[]) dtos).length;
            Object[] cons = (Object[]) Array.newInstance(dtos.getClass().getComponentType(), index + 1);
            System.arraycopy(dtos, 0, cons, 0, len);
            for (int i = len; i < index; i++) {
                cons[i] = null;
            }
            cons[index] = createDto(desc);
            params = new Object[] {cons};
        } else if (List.class.isAssignableFrom(dtos.getClass())) {
            List dtoList = (List) dtos;
            for (int i = dtoList.size(); i < index; i++) {
                dtoList.add(null);
            }
            dtoList.add(createDto(desc));
            params = new Object[] {dtoList};
        }
        
        writer.invoke(bean, params);
    }
    
    private Object createDto(IndexedPropertyDescriptor desc) {
        Class clazz = desc.getIndexedReadMethod().getReturnType();
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            log("���ܴ�����[" + clazz.getName() + "]��ʵ��", e);
        }
        return obj;
    }

    private void log(String msg, Exception e) {
        if (isInfo) {
            logger.info(msg + ":" + e.getMessage() + "," + e);
        }
        
        IllegalStateException ex = new IllegalStateException(msg);
        ex.initCause(e);
        throw ex;
    }
}
