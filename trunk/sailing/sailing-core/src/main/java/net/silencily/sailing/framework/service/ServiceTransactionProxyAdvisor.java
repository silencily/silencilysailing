package net.silencily.sailing.framework.service;

import java.lang.reflect.Method;
import java.util.Properties;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.core.GlobalParameters;
import net.silencily.sailing.framework.core.ServiceBase;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * <p>用于事务性<code>service</code>方法的事务属性配置, 缺省情况下所有实现了<code>ServiceBase</code>
 * 的业务组件都是事务性的, 并且事务行为是<code>EJB's REQUIRED</code>, 实际的事务行为由
 * <code>SerivceTransactionAttributeSource</code>决定</p>
 * <p>这个<code>Pointcut</code>负责挑选出<code>ServiceBase</code>组件, 并排除与操作接口
 * {@link net.silencily.sailing.framework.persistent.coheg.persistent.ServiceDaoSupport <code>ServiceDaoSupport</code>}
 * 相关的任何方法, 比如<code>getter/setter</code>
 * </p>
 * <p>这个类在使用前要设置以下几个属性<ul>
 * <li><code>TransactionAttributeSource</code>: 如何创建事务属性, 这个属性是可选的, 缺省
 * 情况下我们使用{@link com.coheg.framework.service.ServiceTransactionAttributeSource}. 如果
 * 有其他的选择可以通过<code>setTransactionAttributes</code>方法注入进来</li>
 * <li><code>PlatformTransactionManager</code>: 事务管理器, 这个属性是必须的, 通过属性
 * <code>platformTransactionManager</code>来设置</li></ul></p>
 * 
 * @author scott
 * @since 2006-3-10
 * @version $Id: ServiceTransactionProxyAdvisor.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 * @see com.coheg.framework.service.ServiceTransactionAttributeSource
 * @see net.silencily.sailing.framework.core.coheg.core.ServiceBase
 *
 */
public class ServiceTransactionProxyAdvisor extends StaticMethodMatcherPointcutAdvisor implements InitializingBean {
    private static Log logger = LogFactory.getLog(ServiceTransactionProxyAdvisor.class);
    
    public static final String DEFAULT_TRANSACTION_MANAGER_NAME = GlobalParameters.MODULE_NAME + "platformTransactionManager";
    
    /**
     * 用于创建事务属性配置的组件, 缺省按照每个事务方法都是<code>EJB's REQUIRED</code>事务性
     * 的原则处理, 同时支持在<code>spring's bean factory</code>配置文件中增加<code>description</code>
     * 元素来处理其它的事务配置
     */    
    private TransactionAttributeSource transactionAttributeSource =  new ServiceTransactionAttributeSource();

    private PlatformTransactionManager platformTransactionManager;
    
    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
        this.transactionAttributeSource = transactionAttributeSource;
    }
    
    /**
     * 实现同 {@link TransactionAspectSupport#setTransactionAttributes(Properties)}
     * @param transactionAttributes
     */
	public void setTransactionAttributes(Properties transactionAttributes) {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		tas.setProperties(transactionAttributes);
		this.transactionAttributeSource = tas;
	}

    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }
    
    /**
     * 仅仅对扩展了{@link ServiceBase}的服务类提供事务管理
     */
    public boolean matches(Method method, Class targetClass) {
        Class clazz = targetClass;
        if (clazz == null) {
            clazz = method.getDeclaringClass();
        }
        return ServiceBase.class.isAssignableFrom(clazz);
    }

    public void afterPropertiesSet() throws Exception {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionAttributeSource(this.transactionAttributeSource);
        interceptor.setTransactionManager(platformTransactionManager);
        setAdvice(interceptor);
        try {
            interceptor.afterPropertiesSet();
        } catch (Exception e) {
            String msg = "配置事务Advisor错误";

            if (StringUtils.isNotBlank(e.getMessage())) {
                msg += ":" + e.getMessage();
            }
            UnexpectedException ex = new UnexpectedException(msg, e);
            logger.error(msg, e);
            throw ex;
        }
    }
}
