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
 * Ϊʵ���˴�����ǽӿ�<code>ServiceBase</code>��ҵ���������ͨ�÷������, �ڲ���Ҫ���õ� ����½��������������Ĵ���,
 * �������Բ���ȱʡ��<code>DefaultTransactionAttribute</code> , �������Ե�������
 * <code>ServiceTransactionAttributeSource</code>�д���. Ϊ�˼�����, ����
 * <code>advisor</code>����������ͬһ��<code>BeanFactory</code>���Թ̶�����������:
 * {@link #DEFAULT_TRANSACTION_ADVIOSR_NAME
 * <code>DEFAULT_TRANSACTION_ADVIOSR_NAME</code>} , �����ķ������ͨ�������
 * <code>setInterceptorNames</code>����
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
			logger.error("��������������BeanFacotry");
			throw new BeanCreationException("������������BeanFactory��");
		}

		if (this.advisor != null) {
			this.advisors = new Advisor[] { this.advisor };
		}
		/* ����Ӧ�������� advisor, �������, ��־�ȱ������ö������������� */
		setApplyCommonInterceptorsFirst(false);
	}
}
