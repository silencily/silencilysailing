/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.loader;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 初始化 {@link net.silencily.sailing.security.loader.coheg.security.client.loader.SecurityContextLoader}, 适用于 Servlet2.2/2/3 容器, 具体原因同
 * {@link org.springframework.web.context.ContextLoaderServlet}
 * @see net.silencily.sailing.security.loader.coheg.security.client.loader.SecurityContextLoaderListener
 * @see org.springframework.web.context.ContextLoaderServlet
 * @since 2006-6-30
 * @author 王政
 * @version $Id: SecurityContextLoaderServlet.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class SecurityContextLoaderServlet extends HttpServlet {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -6100270577658872047L;

	
	/**
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		SecurityContextLoader.initServletContext(getServletContext());
	}

	/**
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		SecurityContextLoader.destroyServletContext();
	}


	/**
	 * This should never even be called since no mapping to this servlet should
	 * ever be created in web.xml. That's why a correctly invoked Servlet 2.3
	 * listener is much more appropriate for initialization work ;-)
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		getServletContext().log("Attempt to call service method on ContextLoaderServlet as [" + request.getRequestURI() + "] was ignored");
		response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	public String getServletInfo() {
		return "ContextLoaderServlet for Servlet API 2.2/2.3 " + "(deprecated in favor of ContextLoaderListener for Servlet API 2.4)";
	}
	
}
