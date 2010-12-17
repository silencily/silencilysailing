/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * ∑¿÷π JSP ∂¡»°ª∫¥Ê
 * @since 2005-11-30
 * @author Õı’˛
 * @version $Id: ForceResponseNoCacheFilter.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class ForceResponseNoCacheFilter extends OncePerRequestFilter {

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "-1");
		filterChain.doFilter(request, response);
	}

}
