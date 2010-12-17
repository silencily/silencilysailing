/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.silencily.sailing.security.acegi.ui.sso.CrossThreadPassedValue;
import net.silencily.sailing.security.acegi.ui.sso.SSOLogoutRunner;
import net.silencily.sailing.security.acegi.ui.sso.SingleSignOnService;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.ui.rememberme.RememberMeServices;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * @since 2006-3-23
 * @author 王政
 * @version $Id: LogoffServlet.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class LogoffServlet extends HttpServlet {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 4895340403482761615L;

	private transient static Log logger = LogFactory.getLog(LogoffServlet.class);
	
	public static final String REMEMBER_ME_SERVICES = "security.rememberMeServices";
	
	public static final String SINGLE_SIGN_ON_SERVCIE = "security.singleSignOnService";
	
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		/*
		SingleSignOnService singleSignOnService = null;
		try {
			singleSignOnService = (SingleSignOnService) getBean(req, SINGLE_SIGN_ON_SERVCIE);
		} catch (Throwable t) {
			if (logger.isDebugEnabled()) {
				logger.debug(" 不能获取 " + SingleSignOnService.class + ", 忽略单点登录");
			}
		}		
		if (singleSignOnService != null) {		
			ssoLogout(req, resp, singleSignOnService);
		}
		*/
		RememberMeServices rememberMeServices = (RememberMeServices) getBean(req, REMEMBER_ME_SERVICES);
		rememberMeServices.loginFail(req, resp);
		
		HttpSession session = req.getSession(false);
		
		if (session != null) {
			if (logger.isDebugEnabled()) {
				logger.debug(session.getId() + " Logoff");
			}
			session.invalidate();
		}
		
		SecurityContextHolder.clearContext();
		resp.sendRedirect(req.getContextPath() + "/coheg/login.jsp?logoff=true");		
	}

	public static void ssoLogout(HttpServletRequest req, HttpServletResponse resp, SingleSignOnService singleSignOnService) {
		try {
			CrossThreadPassedValue passedValue = singleSignOnService.syncOperationBeforeSSOLogoutRunnerStart(req, resp);
			Thread ssoLogoutThread = new Thread(new SSOLogoutRunner(singleSignOnService, passedValue));
			ssoLogoutThread.start();
		} catch (Throwable t) {
			if (logger.isDebugEnabled()) {
				logger.debug(" 单点登出时失败 : " + t);
			}
		}
	}
	
	private Object getBean(HttpServletRequest request, String beanName) {
		ServletContext context = request.getSession().getServletContext();
		return WebApplicationContextUtils.getRequiredWebApplicationContext(context).getBean(beanName);
	} 
	
	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	

}
