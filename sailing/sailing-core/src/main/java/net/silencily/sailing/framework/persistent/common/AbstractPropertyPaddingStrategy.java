package net.silencily.sailing.framework.persistent.common;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import net.silencily.sailing.framework.persistent.BaseDto;

import org.apache.commons.lang.StringUtils;

/**
 * 属性设置策略的缺省实现, 检索属性的读写方法. 支持<code>BaseDto</code>的子类的属性设置
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
                    throw new IllegalStateException("属性[" + desc.getName() + "]没有写方法");
                }
            }
        } catch (Exception e) {
            String msg = "读取/设置bean[" + bean.getClass().getName() + "]的属性"
                + desc.getName()
                + "]时参数错误";
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
     * 判断<code>bean</code>的属性是否需要设置
     * @param bean     执行设置属性的<code>bean</code>, 是<code>BaseDto</code>的子类
     * @param desc     要操作的属性
     * @param result   从<code>bean</code>读属性方法返回的结果
     * @return 这个属性是否需要设置, 如果返回<code>true</code>, 将调用<code>settingWith</code>
     *         方法
     */
    abstract protected boolean isShouldSet(Object bean, PropertyDescriptor desc, Object result);
    
    /**
     * 如果应该设置这个属性的值那么就使用这个属性设置, 就是<code>isShouldSet</code>方法返回
     * <code>true</code>的情况下, 使用这个值作为属性的值
     */
    abstract protected Object settingWith();
    
    /**
     * 这个名称的属性是想要设置的属性吗?
     * @param propertyName 要检查的属性名, 永远不是<code>null</code>或<code>empty</code>
     * @return 是否是要设置的属性
     */
    abstract protected boolean isDesirableProperty(String propertyName);
}
