package net.silencily.sailing.container;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEvent;

import net.silencily.sailing.exception.BaseExceptionHandler;
import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.resource.CodedMessageSource;

public class ServiceProvider {
    private static ServiceProviderCore core;
    
    static {
        core = new ServiceProviderCore();
        String excludedPattern = GlobalParameters.CONFIGURATION_LOCATION + "/**/test/**/*";
        core.load(
            GlobalParameters.ROOT_CONFIGURATION_NAME, 
            GlobalParameters.CONFIGURATION_PATTERN, 
            excludedPattern);
    }
    
    /**
     * 检索指定名称的服务, 名称必须遵守<code>配置名称.服务名称</code>的规范, 这个名称就是配置
     * 在配置文件中的<code>id</code>属性.
     * @param name 要检索的服务名称
     * @return 指定名称的服务, 不返回<code>null</code>
     * @throws IllegalArgumentException 如果指定名称的服务不存在
     */
    public static Object getService(String name) {
        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("检索服务时服务名是空[" + name + "]");
        }
        
        Object bean = null;
        for (Iterator it = core.leafConfigurations.iterator(); bean == null && it.hasNext(); ) {
            BeanFactory beanFactory = (BeanFactory) it.next();
            /* 为了效率, 没有调用 hasService(String) 方法 */
            if (beanFactory.containsBean(name)) {
                bean = beanFactory.getBean(name);
            }
        }
        
        if (bean == null) {
            throw new IllegalArgumentException("没有定义名称[" + name + "]的服务");
        }
        
        return bean;
    }
    
    /**
     * 查询是否有指定名称的服务
     * @param name 要查询的服务名
     * @return 如果定义了这个名称的服务返回<code>true</code>, 否则<code>false</code>
     * @throws NullPointerException 如果名称是<code>empty</code>或<code>null</code>
     */
    public static boolean hasService(String name) {
        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("检索服务时服务名是空[" + name + "]");
        }
        
        boolean hasSpecifiedService = false;
        for (Iterator it = core.leafConfigurations.iterator(); !hasSpecifiedService && it.hasNext(); ) {
            BeanFactory beanFactory = (BeanFactory) it.next();
            hasSpecifiedService = beanFactory.containsBean(name);
        }
        
        return hasSpecifiedService;
    }
    /**
     * <p>广播事件, 以便使感兴趣的组件能够接收到通知, 根配置中必须注册名称是{@link GlobalParameters#APPLICATION_EVENT_MULTICASTER <code>APPLICATION_EVENT_MULTICASTER</code>}
     * 的<code>ApplicationEventMulticaster</code>组件</p>
     * 
     * @param event 要广播的事件, 是<code>ApplicationEvent</code>的子类
     * @throws NullPointerException 如果参数<code>event</code>是<code>null</code>
     */
    public static void multicaster(ApplicationEvent event) {
        if (event == null) {
            throw new NullPointerException("广播事件时作为事件的参数是null");
        }
        
        core.multicaster(event);
    }
    
    /**
     * 检索客户化信息服务, 从这个服务可以检索到预先配置的信息. 这个服务是架构配置的, 如果这个方
     * 法抛出异常就是配置问题
     * @return 客户化信息服务
     */
    public static CodedMessageSource getCodedMessageSource() {
        return (CodedMessageSource) core.getBeanFromRootConfiguration(GlobalParameters.APPLICATION_CODED_MESSAGE);
    }
    
    public static BaseExceptionHandler getExceptionHandler() {
        return (BaseExceptionHandler) core.getBeanFromRootConfiguration(GlobalParameters.APPLICATION_EXCEPTION_HANDLER);
    }

    /**
     * 用于架构中<code>utility</code>类的方法, 不用于应用代码
     * @return 系统核心的配置管理类
     */
    static ServiceProviderCore getServiceProviderCore() {
        return core;
    }
}
