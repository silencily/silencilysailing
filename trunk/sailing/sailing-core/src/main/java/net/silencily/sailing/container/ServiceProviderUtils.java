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
 * <p><code>ServiceProvider</code>的帮助类, 对配置实现提供了更多的控制, 可以访问到实现的
 * <code>BeanFactory</code>, 根配置, 以及其他的一些配置方法</p>
 * <p>这个类是为了保持<code>ServiceProvider's API</code>的清洁, 并为架构配置提供支持,不
 * 是用于应用代码</p>
 * 
 * @author scott
 * @since 2006-2-27
 * @version $Id: ServiceProviderUtils.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 *
 */
public abstract class ServiceProviderUtils {

    /**
     * <p>检索指定名称的<code>BeanFactory</code>, 这个名称是定义在<code>BeanFactory</code>
     * 中的<code>ServiceInfo</code>的<code>name</code>属性
     * @param name 要检索的<code>BeanFactory</code>的名称
     * @return 定义<code>ServiceInfo</code>的工厂
     * @throws NullPointerException 如果参数是<code>null</code>或<code>empty</code>
     * @throws IllegalArgumentException 如果指定名称的工厂不存在
     */
    public static BeanFactory getBeanFactory(String name) {
        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("检索指定名称的BeanFactory时参数是空值");
        }
        
        ServiceInfo info = (ServiceInfo) CollectionUtils.find(
            ServiceProvider.getServiceProviderCore().allBeanFactories.keySet(), 
            new IdentifiedByNameServiceInfo(name));
        if (info == null) {
            throw new IllegalArgumentException("如果指定名称[" + name + "]的工厂不存在");
        }
        return (BeanFactory) ServiceProvider.getServiceProviderCore().allBeanFactories.get(info);
    }
    
    /**
     * 检索指定类型的服务, 返回的服务是按照配置层次由根到叶子的顺序组织的, 这个顺序保证的是配置
     * 的顺序, 在一个配置中的各个服务的顺序不保证是定义在配置文件中的顺序
     * @param clazz 要检索的服务类型
     * @return 指定类型的服务, 如果没有满足条件的服务, 返回<code>EMPTY_LIST</code>
     */
    public static List getServicesOfType(Class clazz) {
        BeansWithSpecifiedType typed = new BeansWithSpecifiedType(clazz);
        CollectionUtils.collect(
            ServiceProvider.getServiceProviderCore().allBeanFactories.values(), typed);
        
        return typed.allTypedBeans;
    }
    
    /**
     * 一个用于从配置名称检索配置信息<code>ServiceInfo</code>的类
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
     * 用于检索指定类型的<code>beans</code>
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
