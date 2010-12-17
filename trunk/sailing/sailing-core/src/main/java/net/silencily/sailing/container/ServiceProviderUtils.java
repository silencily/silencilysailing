package net.silencily.sailing.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;

import net.silencily.sailing.framework.core.ServiceInfo;

/**
 * <p><code>ServiceProvider</code>�İ�����, ������ʵ���ṩ�˸���Ŀ���, ���Է��ʵ�ʵ�ֵ�
 * <code>BeanFactory</code>, ������, �Լ�������һЩ���÷���</p>
 * <p>�������Ϊ�˱���<code>ServiceProvider's API</code>�����, ��Ϊ�ܹ������ṩ֧��,��
 * ������Ӧ�ô���</p>
 * 
 * @author scott
 * @since 2006-2-27
 * @version $Id: ServiceProviderUtils.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 *
 */
public abstract class ServiceProviderUtils {

    /**
     * <p>����ָ�����Ƶ�<code>BeanFactory</code>, ��������Ƕ�����<code>BeanFactory</code>
     * �е�<code>ServiceInfo</code>��<code>name</code>����
     * @param name Ҫ������<code>BeanFactory</code>������
     * @return ����<code>ServiceInfo</code>�Ĺ���
     * @throws NullPointerException ���������<code>null</code>��<code>empty</code>
     * @throws IllegalArgumentException ���ָ�����ƵĹ���������
     */
    public static BeanFactory getBeanFactory(String name) {
        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("����ָ�����Ƶ�BeanFactoryʱ�����ǿ�ֵ");
        }
        
        ServiceInfo info = (ServiceInfo) CollectionUtils.find(
            ServiceProvider.getServiceProviderCore().allBeanFactories.keySet(), 
            new IdentifiedByNameServiceInfo(name));
        if (info == null) {
            throw new IllegalArgumentException("���ָ������[" + name + "]�Ĺ���������");
        }
        return (BeanFactory) ServiceProvider.getServiceProviderCore().allBeanFactories.get(info);
    }
    
    /**
     * ����ָ�����͵ķ���, ���صķ����ǰ������ò���ɸ���Ҷ�ӵ�˳����֯��, ���˳��֤��������
     * ��˳��, ��һ�������еĸ��������˳�򲻱�֤�Ƕ����������ļ��е�˳��
     * @param clazz Ҫ�����ķ�������
     * @return ָ�����͵ķ���, ���û�����������ķ���, ����<code>EMPTY_LIST</code>
     */
    public static List getServicesOfType(Class clazz) {
        BeansWithSpecifiedType typed = new BeansWithSpecifiedType(clazz);
        CollectionUtils.collect(
            ServiceProvider.getServiceProviderCore().allBeanFactories.values(), typed);
        
        return typed.allTypedBeans;
    }
    
    /**
     * һ�����ڴ��������Ƽ���������Ϣ<code>ServiceInfo</code>����
     */
    private static class IdentifiedByNameServiceInfo implements Predicate {
        private String factoryName;
        
        public IdentifiedByNameServiceInfo(String factoryName) {
            this.factoryName = factoryName;
        }
        
        public boolean evaluate(Object info) {
            return this.factoryName.equals(((ServiceInfo) info).getName());
        }
    }
    
    /**
     * ���ڼ���ָ�����͵�<code>beans</code>
     */
    private static class BeansWithSpecifiedType implements Transformer {
        private List allTypedBeans = new ArrayList();
        private Class specifiedType;
        
        public BeansWithSpecifiedType(Class specifiedType) {
            this.specifiedType = specifiedType;
        }
        
        public Object transform(Object obj) {
            ListableBeanFactory beanFactory = (ListableBeanFactory) obj;
            Map map = beanFactory.getBeansOfType(specifiedType);
            if (map.size() > 0) {
                allTypedBeans.addAll(map.values());
            }

            return obj;
        }
    }
}
