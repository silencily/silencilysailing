package net.silencily.sailing.framework.service.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import net.silencily.sailing.container.ConfigurationException;
import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.framework.core.ServiceInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.TargetSourceCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * 用于创建系统中常用服务的抽象实现, 像<code>HibernateTemplate</code>, <code>JdbcTemplate</code>
 * , <code>AuthenticationService</code>, <code>RequestContext</code>, 这个类用于
 * {@link ServiceAutoProxyCreator}的属性, 可以配置在<code>BeanFactory</code>或
 * <code>ApplicationContext</code>中
 * @author zhangli
 * @version $Id: AbstractMethodImplementationTargetSourceCreator.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 * @since 2007-5-5
 */
public class AbstractMethodImplementationTargetSourceCreator implements BeanFactoryAware, TargetSourceCreator {
    
    private Log logger = LogFactory.getLog(AbstractMethodImplementationTargetSourceCreator.class);
    
    private Map methodNames = new HashMap();

    private Map benaFactories = new HashMap(100);
    
    public void setMethodNames(Map methodNames) {
        this.methodNames = methodNames;
    }

    public TargetSource getTargetSource(Class beanClass, String beanName) {
        int pos = beanName.lastIndexOf('.');
        /* 不代理不遵守约定的服务 */
        if (pos == -1) {
            logger.warn("bean name [" + beanName + "]没有使用约定的前缀,无法执行TargetSource代理");
            return null;
        }
        String prefix = beanName.substring(0, pos);
        final BeanFactory factory = (BeanFactory) this.benaFactories.get(prefix);
        if (factory == null) {
            logger.warn("根据ServiceInfo'name[" + prefix + "]没有找到BeanFactory,不能基于TargetSource代理" + beanName);
            return null;
        }
        if ((beanClass.getModifiers() & Modifier.ABSTRACT) == 0 || !ServiceBase.class.isAssignableFrom(beanClass)) {
            return null;
        }
        if (!ConfigurableListableBeanFactory.class.isInstance(factory)) {
            return null;
        }
        ConfigurableListableBeanFactory listableFactory = (ConfigurableListableBeanFactory) factory;
        BeanDefinition bd = listableFactory.getBeanDefinition(beanName);
        if (bd.isAbstract()) {
            return null;
        }
        if (!RootBeanDefinition.class.isInstance(bd)) {
            return null;
        }
        final RootBeanDefinition rbd = (RootBeanDefinition) bd;
        if (!rbd.getMethodOverrides().isEmpty()) {
            return null;
        }
        TargetSource ts = new TargetSource() {

            public Object getTarget() throws Exception {
                return new CglibProxyCreator(factory, rbd).instantiate();
            }

            public Class getTargetClass() {
                return rbd.getBeanClass();
            }

            public boolean isStatic() {
                return true;
            }

            public void releaseTarget(Object target) throws Exception {
            }
        };
        return ts;
    }

    private class CglibProxyCreator {
        private RootBeanDefinition beanDefinition;
        private BeanFactory beanFactory;
        
        public CglibProxyCreator(BeanFactory beanFactory, RootBeanDefinition beanDefinition) {
            this.beanDefinition = beanDefinition;
            this.beanFactory = beanFactory;
        }
        
        public Object instantiate() {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanDefinition.getBeanClass());
            enhancer.setCallbackFilter(new CallbackFilterImpl());
            enhancer.setCallbacks(new Callback[] {NoOp.INSTANCE, new AbstractMethodImlementator()});
            return enhancer.create();
        }

        /* this class comes from CglibSubclassingInstantiationStratgy */
        private class CglibIdentitySupport {

            protected RootBeanDefinition getBeanDefinition() {
                return beanDefinition;
            }
            
            public int hashCode() {
                return beanDefinition.hashCode();
            }
            
            public boolean equals(Object other) {
                return (other.getClass() == getClass()) &&
                        ((CglibIdentitySupport) other).getBeanDefinition() == beanDefinition;
            }
        }
        
        private class AbstractMethodImlementator extends CglibIdentitySupport implements MethodInterceptor {

            public Object intercept(Object obj, Method method, Object[] args, MethodProxy mp) throws Throwable {
                String name = method.getName();
                return beanFactory.getBean((String) methodNames.get(name));
            }
        }
        
        private class CallbackFilterImpl extends CglibIdentitySupport implements CallbackFilter {

            public int accept(Method method) {
                return (method.getModifiers() & Modifier.ABSTRACT) != 0 && methodNames.containsKey(method.getName()) ? 1 : 0;
            }
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        Map map = ((ListableBeanFactory) beanFactory).getBeansOfType(ServiceInfo.class);
        if (map.size() != 1) {
            throw new ConfigurationException("BeanFactory " + beanFactory + "包含多个/没有包含ServiceInfo");
        }
        ServiceInfo serviceInfo = (ServiceInfo) map.values().iterator().next();
        this.benaFactories.put(serviceInfo.getName(), beanFactory);
    }
}
