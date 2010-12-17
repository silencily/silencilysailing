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
 * <p>基于<code>List</code>的填写可扩展属性实现, 在<code>java bean specification</code>中
 * 不认为<code>List</code>是索引属性, 而这类属性的使用在我们开发中与索引属性同等对待的. 所以
 * 对<code>List</code>属性特别处理</p>
 * <p>注意这个实现是<code>fully hibernate-oriented</code>的, 但用于其它的实现应该没有问题.当
 * 然没有测试过. 调试程序时打开这个<code>log</code></p>
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
             * 我们删除掉从数据库 load 出来的数据, 以从页面提交的数据为准重新创建, 因为只有这样
             * 才能体现出页面删除的变化. 这就要求每次页面提交时的数据是一个完整的 dto. 
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
                throw new UnexpectedException("不能通过缺省构造器实例化[" + clazz.getClass().getName() + "]", e);
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
                logger.debug(bean.getClass().getName() + "没有定义" + name + "方法");
            }
//            throw new UnexpectedException("没有[" + bean.getClass().getName() + "]的[" + name + "]方法", e);
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
            /* 需要抛出异常使我们知道哪里发生了问题 */
            throw new UnexpectedException("[" + bean.getClass().getName() + "]没有定义[" + name + "(int)]方法", e);
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
     * 解决典型的<code>Hibernate</code>式的<code>parent-child</code>关系引用, 当组装
     * 一个实体的集合属性的各个元素时, 设置这些元素对这个实体的引用, 执行这个操作的实体和元
     * 素要满足<ul>
     * <li>包含集合属性的实体类型是<code>BaseDto</code>, 就是参数<code>parent</code></li>
     * <li>集合元素类型是<code>BaseDto</code></li>
     * <li>元素定义了对实体(<code>parent</code>)的引用, 而且属性名称是实体类名称且第一个字母小写</li>
     * </ul>
     * @param parent 包含<code>child</code>集合的<code>BaseDto</code>
     * @param child  集合中的元素, <code>one-to-many</code>关系中的多端
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
                /* 无法解决 parent-child 引用, todo log */
                if (logger.isDebugEnabled()) {
                    logger.debug(parent.getClass().getName() 
                        + "的List属性中的元素" 
                        + child.getClass().getName() 
                        + "没有定义" 
                        + methodName 
                        + "方法," + e);
                }
            }
        }
    }

}
