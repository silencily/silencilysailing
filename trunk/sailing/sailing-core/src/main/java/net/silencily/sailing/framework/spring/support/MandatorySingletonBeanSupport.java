/*
 * Copyright 2005-2010 the original author or autors
 *  
 *    http://www.coheg.com.cn
 *
 * Project { cohegFramework }
 */
package net.silencily.sailing.framework.spring.support;

import org.springframework.beans.factory.FactoryBean;


/**
 * <class>MandatorySingletonBeanSupport</class> use to madatory a spring bean's singlton attribute to "true",
 * subclass require the funciton can simply inherit it
 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
 * @see org.springframework.beans.factory.support.DefaultListableBeanFactory#preInstantiateSingletons()
 * @since 2005-8-7
 * @author ÍõÕþ
 * @version $Id: MandatorySingletonBeanSupport.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public abstract class MandatorySingletonBeanSupport implements FactoryBean {

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return this;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return getClass();
	}

	/**
	 * Allways return true to mandatory the singleton attribute to "true"
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

}
