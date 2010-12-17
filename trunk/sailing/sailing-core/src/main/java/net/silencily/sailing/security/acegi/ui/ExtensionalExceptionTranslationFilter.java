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
 * ��չ {@link org.acegisecurity.ui.ExceptionTranslationFilter} ��ʵ��һЩ�Զ�������
 * @since 2006-1-23
 * @author ����
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
	 * �趨�Ƿ��κ���Դ����������������, ע��������� Ϊ true, /login.jsp, /logoff.jsp һ������ʹ�ô� Filter, �������ѭ��!
	 * @param forbiddenAnyAnonymousVisit The forbiddenAnyAnonymousVisit to set.
	 */
	public void setForbiddenAnyAnonymousVisit(boolean forbiddenAnyAnonymousVisit) {
		this.forbiddenAnyAnonymousVisit = forbiddenAnyAnonymousVisit;
	}
	

	/**
	 * ���ú��Ե��ļ���ʽ
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
		
		/** �κ��������ʶ�����λ����½ҳ��, ע����� {@link #isForbiddenAnyAnonymousVisit()} Ϊ true, /login.jsp һ������ʹ�ô� Filter, �������ѭ��!  */ 
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
    	
    	//to process jsessionid ����
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