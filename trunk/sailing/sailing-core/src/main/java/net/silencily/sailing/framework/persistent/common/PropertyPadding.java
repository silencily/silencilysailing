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
 * <p>���<i>ִ����<code>insert</code>����ʱ</i><code>DTO</code>���������, ��Щ���Գ�����
 * <code>uuid</code>,<code>version</code>,<code>operatorId</code>�Ȼ�������, ��Щ��
 * �Թ涨��<code>BaseDto</code>��, ����������������<code>BaseDto</code>ִ�е�. ����ĳ��
 * ������ʲô���������, ������ý�ί�и�ĳ�����Ե�<code>PaddingStrategy</code>��ִ��</p>
 * @author scott
 * @since 2006-4-13
 * @version $Id: PropertyPadding.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 *
 */
public class PropertyPadding {
    public static final String SERVICE_NAME = GlobalParameters.MODULE_NAME + ".propertyPadding";

    /**
     * ���������Ե�<code>Set</code>, Ԫ����<code>PropertyPaddingStrategy</code>
     */
    private Set paddingStrategies;
    
    public void setPaddingStrategies(Set paddingStrategies) {
        this.paddingStrategies = paddingStrategies;
    }
    
    /**
     * ע���µ�����������, ��Щ�������ڼܹ�ִ�����ݿ��<code>insert</code>����ʱ��дȱʡ��
     * ����
     * @param stratgy ����������
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
