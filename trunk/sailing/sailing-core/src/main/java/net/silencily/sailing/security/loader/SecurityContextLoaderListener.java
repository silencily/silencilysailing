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
 * ��ʼ�� {@link net.silencily.sailing.security.loader.coheg.security.client.loader.SecurityContextLoader}, ������ Servlet2.4 ����������,
 * Servlet2.2/2.3 ��ʹ�� {@link net.silencily.sailing.security.loader.coheg.security.client.loader.SecurityContextLoaderServlet} 
 * @see net.silencily.sailing.security.loader.coheg.security.client.loader.SecurityContextLoaderServlet
 * @see org.springframework.web.context.ContextLoaderListener
 * @since 2006-6-30
 * @author ����
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
