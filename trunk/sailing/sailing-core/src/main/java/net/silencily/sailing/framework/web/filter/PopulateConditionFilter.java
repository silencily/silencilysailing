/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.framework.core.ContextInfo;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * �������, ʹ���з��� tomcat thredpool û������ϴ�ʹ�ù��� thread �еı���, ���ܰ�������װ
 * �߼��ŵ�����
 * @since 2006-10-14
 * @author scott
 * @version $Id: PopulateConditionFilter.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class PopulateConditionFilter extends OncePerRequestFilter {
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContextInfo.clear();
        filterChain.doFilter(request, response);
    }
}
