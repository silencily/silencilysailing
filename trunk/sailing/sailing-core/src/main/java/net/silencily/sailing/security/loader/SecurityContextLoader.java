/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.loader;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @since 2006-6-30
 * @author ÍõÕþ
 * @version $Id: SecurityContextLoader.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public abstract class SecurityContextLoader {
	
	private static transient Log logger = LogFactory.getLog(SecurityContextLoader.class);
	
	protected static ApplicationContext applicationContext;
	
	protected static ServletContext servletContext;
	
	/**
	 * @return Returns the applicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		if (applicationContext != null) {
			return applicationContext;
		}
		if (servletContext == null) {
			throw new RuntimeException("No servletContext found: no " 
			+ SecurityContextLoaderListener.class + " or " + SecurityContextLoaderServlet.class + " registered? ");
		}
		
		return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

	public static void initServletContext(ServletContext servletContext) {
		if (logger.isInfoEnabled()) {
			logger.info(SecurityContextLoader.class + " initialing... ");
		}
		SecurityContextLoader.servletContext = servletContext;
		if (logger.isInfoEnabled()) {
			logger.info(SecurityContextLoader.class + " initialized ");
		}
	}
	
	public static void destroyServletContext() {
		if (logger.isInfoEnabled()) {
			logger.info(SecurityContextLoader.class + " destroying... ");
		}
		servletContext = null;
		if (logger.isInfoEnabled()) {
			logger.info(SecurityContextLoader.class + " destroyed ");
		}
	}
	
	public static void initApplicationContext(ApplicationContext applicationContext) {
		SecurityContextLoader.applicationContext = applicationContext;
	}
}
