package net.silencily.sailing.framework.persistent.common;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.utils.DumpUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * <p>填充<i>执行像<code>insert</code>操作时</i><code>DTO</code>不足的属性, 这些属性常常是
 * <code>uuid</code>,<code>version</code>,<code>operatorId</code>等基础属性, 这些属
 * 性规定在<code>BaseDto</code>中, 所以这个操作是针对<code>BaseDto</code>执行的. 具体某个
 * 属性在什么情况下设置, 如何设置将委托给某个属性的<code>PaddingStrategy</code>来执行</p>
 * @author scott
 * @since 2006-4-13
 * @version $Id: PropertyPadding.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public class PropertyPadding {
    public static final String SERVICE_NAME = GlobalParameters.MODULE_NAME + ".propertyPadding";

    /**
     * 属性填充策略的<code>Set</code>, 元素是<code>PropertyPaddingStrategy</code>
     */
    private Set paddingStrategies;
    
    public void setPaddingStrategies(Set paddingStrategies) {
        this.paddingStrategies = paddingStrategies;
    }
    
    /**
     * 注册新的属性填充策略, 这些策略用于架构执行数据库的<code>insert</code>操作时填写缺省的
     * 属性
     * @param stratgy 属性填充策略
     */
    public synchronized void registry(PropertyPaddingStrategy stratgy) {
        if (this.paddingStrategies == null) {
            this.paddingStrategies = new HashSet();
        }
        this.paddingStrategies.add(stratgy);
    }
    
    public Set getPaddingStrategies() {
        if (this.paddingStrategies == null) {
            return Collections.EMPTY_SET;
        }
        
        return this.paddingStrategies;
    }
    
    public Object padding(Object dto) {
        if (dto != null) {
            PropertyDescriptor[] descs = DumpUtils.getPropertyDescriptorsForBean(dto);
            for (int i = 0; i < descs.length; i++) {
                PropertyPaddingStrategy strategy = (PropertyPaddingStrategy) CollectionUtils.find(
                    getPaddingStrategies(), new PropertyPredicate(dto.getClass(), descs[i].getName()));
                if (strategy != null) {
                    dto = strategy.padding(dto, descs[i]);
                }
            }
        }
        
        return dto;
    }
    
    private static class PropertyPredicate implements Predicate {
        private String name;
        private Class clazz;
        
        public PropertyPredicate(Class clazz, String name) {
            this.clazz = clazz;
            this.name = name;
        }
        
        public boolean evaluate(Object element) {
            PropertyPaddingStrategy strategy = (PropertyPaddingStrategy) element;
            return strategy.supportProperty(clazz, name);
        }
    }
}
