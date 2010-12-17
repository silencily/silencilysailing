/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.loader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 初始化 {@link net.silencily.sailing.security.loader.coheg.security.client.loader.SecurityContextLoader}, 适用于 Servlet2.4 及以上容器,
 * Servlet2.2/2.3 请使用 {@link net.silencily.sailing.security.loader.coheg.security.client.loader.SecurityContextLoaderServlet} 
 * @see net.silencily.sailing.security.loader.coheg.security.client.loader.SecurityContextLoaderServlet
 * @see org.springframework.web.context.ContextLoaderListener
 * @since 2006-6-30
 * @author 王政
 * @version $Id: SecurityContextLoaderListener.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class SecurityContextLoaderListener implements ServletContextListener {
		
	public void contextInitialized(ServletContextEvent event) {		
		SecurityContextLoader.initServletContext(event.getServletContext());
	}

	public void contextDestroyed(ServletContextEvent event) {
		SecurityContextLoader.destroyServletContext();
	}


}
