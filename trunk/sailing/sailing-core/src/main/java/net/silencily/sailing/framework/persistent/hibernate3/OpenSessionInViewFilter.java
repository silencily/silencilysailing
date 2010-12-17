/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent.hibernate3;

import net.silencily.sailing.container.ServiceProvider;

import org.hibernate.SessionFactory;



/**
 * ���� {@link org.springframework.orm.hibernate3.support.OpenSessionInViewFilter#lookupSessionFactory()} ����,
 * �� {@link com.coheg.container.ServiceProvider} �л�ȡ SessionFactory
 * @see org.springframework.orm.hibernate3.support.OpenSessionInViewFilter  
 * @since 2006-6-26
 * @author ����
 * @version $Id: OpenSessionInViewFilter.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class OpenSessionInViewFilter extends org.springframework.orm.hibernate3.support.OpenSessionInViewFilter {
	
	/**
	 * 
	 * @see org.springframework.orm.hibernate3.support.OpenSessionInViewFilter#lookupSessionFactory()
	 */
	protected SessionFactory lookupSessionFactory() {
		if (logger.isDebugEnabled()) {
			logger.debug("Using SessionFactory '" + getSessionFactoryBeanName() + "' for OpenSessionInViewFilter");
		}
		return (SessionFactory) ServiceProvider.getService(getSessionFactoryBeanName());
	}
	
}
