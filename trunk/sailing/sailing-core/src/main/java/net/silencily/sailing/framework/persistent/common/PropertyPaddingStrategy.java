package net.silencily.sailing.framework.persistent.common;

import java.beans.PropertyDescriptor;

/**
 * 当持久化一个<code>DTO</code>时, 决定如何设置规定名称的属性值. 用于<code>PropertyPadding</code>
 * 类. 比如当创建一个新的<code>DTO</code>时, 我们可以不设置<code>id</code>, 而由这个接口的
 * 实现来完成<code>id</code>值的设定. 这个类的实现通常仅仅用于<code>insert</code>操作
 * @author scott
 * @since 2006-4-13
 * @version $Id: PropertyPaddingStrategy.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public interface PropertyPaddingStrategy {
    
    /**
     * 给定一个<code>bean</code>及需要设置的属性的描述, 完成属性的设置
     * @param bean 要设置属性值的<code>bean</code>
     * @param desc 要设置属性值的属性描述
     * @return 设置属性值后的<code>bean</code>
     */
    Object padding(Object bean, PropertyDescriptor desc);
    
    /**
     * 这个类支持的属性名称, 当用一个<code>bean' class</code>和属性名调用这个方法时返回<code>
     * true</code>, 那么架构将委托<code>padding</code>方法设置属性. 按照架构<code>DTO</code>
     * 的规定, <code>id</code>, <code>version</code>, <code>operatorId</code>, <code>
     * createdTime</code>, <code>lastModifiedTime</code>不能用于其它目的, 对于这几个属性
     * 实现不应该判断<code>clazz</code>参数
     * @param clazz 定义属性的<code>bean class</code>
     * @param name  属性名称
     * @return      是否处理这个属性
     */
    boolean supportProperty(Class clazz, String name);
}
