package net.silencily.sailing.framework.service;

import net.silencily.sailing.framework.core.ServiceBase;

import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.TargetSourceCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>
 * 为实现了创建标记接口<code>ServiceBase</code>的业务组件创建通用服务代理, 在不需要配置的 情况下仅仅创建事务管理的代理,
 * 事务属性采用缺省的<code>DefaultTransactionAttribute</code> , 事务属性的设置在
 * <code>ServiceTransactionAttributeSource</code>中处理. 为了简化配置, 事务
 * <code>advisor</code>必须配置在同一个<code>BeanFactory</code>中以固定的名称命名:
 * {@link #DEFAULT_TRANSACTION_ADVIOSR_NAME
 * <code>DEFAULT_TRANSACTION_ADVIOSR_NAME</code>} , 其它的服务可以通过超类的
 * <code>setInterceptorNames</code>设置
 * </p>
 * 
 * @author scott
 * @since 2006-3-2
 * @version $Id: ServiceAutoProxyCreator.java,v 1.1 2009/05/05 08:32:04
 *          zhaozhiguo Exp $
 * 
 */
public class ServiceAutoProxyCreator extends AbstractAutoProxyCreator implements
		InitializingBean {

	private Advisor advisor;

	private Advisor[] advisors;

	private TargetSourceCreator customTargetSourceCreator;

	public void setAdvisor(Advisor advisor) {
		this.advisor = advisor;
	}

	public void setAdvisors(Advisor[] advisors) {
		this.advisors = advisors;
	}

	public void setCustomTargetSourceCreator(
			TargetSourceCreator customTargetSourceCreator) {
		this.customTargetSourceCreator = customTargetSourceCreator;
	}

	public TargetSourceCreator getCustomTargetSourceCreator() {
		return this.customTargetSourceCreator;
	}

	protected Object[] getAdvicesAndAdvisorsForBean(Class beanClass,
			String beanName, TargetSource customTargetSource)
			throws BeansException {

		if (ServiceBase.class.isAssignableFrom(beanClass)) {
			return this.advisors;
		}
		return DO_NOT_PROXY;
	}

	/* super class's customTargetSources didn't expose to subclass */
	protected TargetSource getCustomTargetSource(Class beanClass,
			String beanName) {
		if (this.customTargetSourceCreator != null) {
			TargetSource ts = customTargetSourceCreator.getTargetSource(
					beanClass, beanName);
			if (ts != null) {
				// Found a matching TargetSource.
				if (logger.isDebugEnabled()) {
					logger.debug("TargetSourceCreator [" + ts
							+ " found custom TargetSource for bean with name '"
							+ beanName + "'");
				}
				return ts;
			}
		}
		return null;
	}

	public void afterPropertiesSet() throws Exception {
		if (getBeanFactory() == null) {
			logger.error("这个类必须配置在BeanFacotry");
			throw new BeanCreationException("这个类必须用于BeanFactory中");
		}

		if (this.advisor != null) {
			this.advisors = new Advisor[] { this.advisor };
		}
		/* 首先应用其他的 advisor, 对于审计, 日志等必须配置独立的事务属性 */
		setApplyCommonInterceptorsFirst(false);
	}
}
