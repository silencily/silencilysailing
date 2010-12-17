/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui.webapp;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.security.acegi.ui.servlet.LogoffServlet;
import net.silencily.sailing.security.acegi.ui.sso.SingleSignOnService;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.ui.rememberme.RememberMeServices;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilterEntryPoint;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * 支持跨 webapp 的 url 重定向
 * @since 2006-3-6
 * @author 王政
 * @version $Id: InternetSupportAuthenticationProcessingFilterEntryPoint.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class InternetSupportAuthenticationProcessingFilterEntryPoint extends AuthenticationProcessingFilterEntryPoint 
	implements InitializingBean {
	
	public static final String SERVICE_CALLBACK_URL_PARAM = "serviceCallBack";
	
	private boolean useInternetLoginUrl = true;

	private RememberMeServices rememberMeServices;
	
	private SingleSignOnService singleSignOnService;
	
	/**
	 * @see org.acegisecurity.ui.webapp.AuthenticationProcessingFilterEntryPoint#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		Assert.notNull(rememberMeServices, " rememberMeServices required ");
	}

	/**
	 * @param useInternetLoginUrl The useInternetLoginUrl to set.
	 */
	public void setUseInternetLoginUrl(boolean useInternetLoginUrl) {
		this.useInternetLoginUrl = useInternetLoginUrl;
	}
	
	/**
	 * @param rememberMeServices the rememberMeServices to set
	 */
	public void setRememberMeServices(RememberMeServices rememberMeServices) {
		this.rememberMeServices = rememberMeServices;
	}
	
	/**
	 * @param singleSignOnService the singleSignOnService to set
	 */
	public void setSingleSignOnService(SingleSignOnService singleSignOnService) {
		this.singleSignOnService = singleSignOnService;
	}

	/**
	 * @see org.acegisecurity.ui.webapp.AuthenticationProcessingFilterEntryPoint#commence(javax.servlet.ServletRequest, javax.servlet.ServletResponse, org.acegisecurity.AuthenticationException)
	 */
	public void commence(ServletRequest request, ServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if (useInternetLoginUrl) {			
			String orignalRequestUri = ((HttpServletRequest) request).getRequestURI();			
			String queryString = ((HttpServletRequest) request).getQueryString();
			if (queryString != null) {
				orignalRequestUri += "?" + queryString;
			}
			
			String serviceCallback = URLEncoder.encode(orignalRequestUri, "UTF-8");
			String rebuildLoginFormUrl = appendServiceCallback(getLoginFormUrl(), serviceCallback);	
			
			if (singleSignOnService != null) {
				LogoffServlet.ssoLogout((HttpServletRequest) request, (HttpServletResponse) response, singleSignOnService);
			}
			rememberMeServices.loginFail((HttpServletRequest) request, (HttpServletResponse) response);
			((HttpServletResponse) response).sendRedirect(rebuildLoginFormUrl);
		} else {
			super.commence(request, response, authException);
		}
	}

	
	public static String obtainServiceCallback(HttpServletRequest request) {
		return request.getParameter(SERVICE_CALLBACK_URL_PARAM);
	}
	
	public static String appendServiceCallback(String loginUrl, String serviceCallback) {
		String appendChar = null;
		if (loginUrl.indexOf("?") > -1) {
			appendChar = "&";
		} else {
			appendChar = "?";
		}
		String rebuildLoginFormUrl = new StringBuffer(loginUrl).append(appendChar).append(SERVICE_CALLBACK_URL_PARAM).append("=").append(serviceCallback).toString();
		return rebuildLoginFormUrl;
	}
	
	public static void main(String[] args) {
		try {
			String url = "/ddemis/flaw/flawManageAction.do?step=entry&shrink=false&scratch=true";
			String newUrl = URLEncoder.encode(url, "UTF-8");
			System.out.println(newUrl);
			newUrl = URLEncoder.encode(url,"GB2312");
			System.out.println(newUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
}
