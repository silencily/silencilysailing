/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.sso.logoff;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @since 2006-5-8
 * @author ÍõÕþ
 * @version $Id: WebAppLogoffServlet.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public class WebAppLogoffServlet extends HttpServlet {

	//** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 6345844655530595349L;
	
	private transient static Log logger = LogFactory.getLog(WebAppLogoffServlet.class);

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("User [" + session.getId() + " ] Logoff");
			}
			session.invalidate();
		}
		SecurityContextHolder.clearContext();
	}
	
	
}



