/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.dom4j.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @since 2006-8-1
 * @author java2enterprise
 * @version $Id: XmlMenuFilter.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class XmlMenuFilter implements Filter, InitializingBean {

	public static final String XML_MENU_KEY = "_acegi_xml_menu_support_";
	
	private UserMenuAware userMenuAware;
		
	/**
	 * @param userMenuAware The userMenuAware to set.
	 */
	public void setUserMenuAware(UserMenuAware userMenuAware) {
		this.userMenuAware = userMenuAware;
	}


	public void afterPropertiesSet() throws Exception {
		Assert.notNull(userMenuAware, " userMenuAware is required. ");
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// dothing 
	}

	public void doFilter(
		ServletRequest request, 
		ServletResponse response, 
		FilterChain chain) 
		throws IOException, ServletException {
		
        if (!(request instanceof HttpServletRequest)) {
            throw new ServletException("Can only process HttpServletRequest");
        }

        if (!(response instanceof HttpServletResponse)) {
            throw new ServletException("Can only process HttpServletResponse");
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assert.notNull(authentication, " ¶Ô²»Æð, ÄúÉÐÎ´µÇÂ½! ");
        Document document = userMenuAware.buildXmlMenu((HttpServletRequest) request, authentication);
		request.setAttribute(XML_MENU_KEY, document.asXML());
		
		chain.doFilter(request, response);
	}

	public void destroy() {
		// dothing 
	}

}
