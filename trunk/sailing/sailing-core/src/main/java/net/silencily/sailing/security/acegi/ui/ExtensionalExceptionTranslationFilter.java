/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.InsufficientAuthenticationException;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.ui.ExceptionTranslationFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

/**
 * 扩展 {@link org.acegisecurity.ui.ExceptionTranslationFilter} 以实现一些自定义设置
 * @since 2006-1-23
 * @author 王政
 * @version $Id: ExtensionalExceptionTranslationFilter.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class ExtensionalExceptionTranslationFilter extends ExceptionTranslationFilter {
	
	private static final Log logger = LogFactory.getLog(ExtensionalExceptionTranslationFilter.class);
	
    private boolean forbiddenAnyAnonymousVisit = false;
    
    private List ignoreForbiddenFileFormats = new LinkedList();
    
    /**
	 * @return Returns the forbiddenAnyAnonymousVisit.
	 */
	public boolean isForbiddenAnyAnonymousVisit() {
		return forbiddenAnyAnonymousVisit;
	}

	/**
	 * 设定是否任何资源都不允许匿名访问, 注意如果设置 为 true, /login.jsp, /logoff.jsp 一定不能使用此 Filter, 否则会死循环!
	 * @param forbiddenAnyAnonymousVisit The forbiddenAnyAnonymousVisit to set.
	 */
	public void setForbiddenAnyAnonymousVisit(boolean forbiddenAnyAnonymousVisit) {
		this.forbiddenAnyAnonymousVisit = forbiddenAnyAnonymousVisit;
	}
	

	/**
	 * 设置忽略的文件格式
	 * @param ignoreForbiddenFileFormats The ignoreForbiddenFileFormats to set.
	 */
	public void setIgnoreForbiddenFileFormats(List ignoreForbiddenFileFormats) {
		this.ignoreForbiddenFileFormats = ignoreForbiddenFileFormats;
	}

	/**
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {    	   	
		if (!(request instanceof HttpServletRequest)) {
		    throw new ServletException("HttpServletRequest required");
		}
		
		if (!(response instanceof HttpServletResponse)) {
		    throw new ServletException("HttpServletResponse required");
		}
		
		/** 任何匿名访问都将定位到登陆页面, 注意如果 {@link #isForbiddenAnyAnonymousVisit()} 为 true, /login.jsp 一定不能使用此 Filter, 否则会死循环!  */ 
		if (isForbiddenAnyAnonymousVisit() && ! ignore((HttpServletRequest) request)) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null || getAuthenticationTrustResolver().isAnonymous(authentication)) {
			    if (logger.isDebugEnabled()) {
			        logger.debug("Access is denied (user is anonymous); redirecting to authentication entry point");
			    }
			    sendStartAuthentication(request, response, chain, new InsufficientAuthenticationException( "Full authentication is required to access this resource"));
			    return;
			}
		}
		super.doFilter(request, response, chain);
    }
    
    
    private boolean ignore(HttpServletRequest request) {
    	if (CollectionUtils.isEmpty(ignoreForbiddenFileFormats)) {
    		return false;
    	}
    	
    	String uri = request.getRequestURI();
    	
    	//to process jsessionid 问题
    	if (uri.indexOf(";jsessionid=") > -1) {
    		uri = uri.substring(0, uri.indexOf(";jsessionid="));
    	}
    	
    	for (Iterator iter = ignoreForbiddenFileFormats.iterator(); iter.hasNext(); ) {
    		String format = iter.next().toString();
    		if (uri.endsWith(format)) {
    			return true;
    		}
    	}
    
    	return false;
    }
    
}