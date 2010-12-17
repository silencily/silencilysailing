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
 * <p><code>ExtendableIndexedProperty</code>的简单实现, 用于<code>PropertyUtilBean</code>
 * 的扩展, 对<code>PropertyUtilBean</code>的引用为了避免通过反射或者缓存反射的结果提供效率,
 * 所有的异常处理使用<code>PropertyUtilsBean</code>, 只有一处在创建<code>dto</code>时抛出
 * 的异常</p>
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
    
    /* 不检查任何参数, 在调用这个方法之前这些参数已经设置完成. 但这个方法一定要用于索引参数 */
    public void extend(Object bean, String propName, int index) 
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        
        PropertyDescriptor propDesc = utils.getPropertyDescriptor(bean, propName);
        
        if (!(propDesc instanceof IndexedPropertyDescriptor)) {
            logger.warn("扩展bean索引属性的值时["
                + bean.getClass().getName()
                + "的属性["
                + propName
                + "]不是索引属性");
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
     * 如果使用非索引的方法<code>getXXX()</code>返回了<code>null</code>就创建大小为<code>index</code>
     * 的数组或<code>List</code>
     * @param bean  要组装的<code>bean</code>
     * @param index 组装后数组或<code>List</code>的大小
     * @param propDesc 要处理属性的<code>PropertyDescriptor</code>
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
            logger.warn("执行["
                + bean.getClass().getName()
                + "]索引属性"
                + propDesc.getName()
                + "的写方法时参数是非索引的["
                + paramType.getName()
                + "]");
        }
    }
    
    /**
     * 索引属性的长度不足, 使用<code>DTO</code>实例填充到<code>index + 1</code>
     * @param bean      包含要操作属性的<code>bean</code>
     * @param index     填充到这个长度
     * @param desc      要操作属性的<code>IndexedPropertyDescription</code>
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
            log("不能创建类[" + clazz.getName() + "]的实例", e);
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
