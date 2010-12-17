/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.context;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.security.SecurityContextInfo;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * ��� {@link SecurityContextHolder} �� filter, ʹ����� filter ��ԭ���� tomcat �̳߳�û����� SecurityContext,
 * ע��� filter ����������а�ȫ filter �ĵ�һλ
 * @since 2006-11-11
 * @author java2enterprise
 * @version $Id: ExpirerFilter.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class ExpirerFilter extends OncePerRequestFilter {

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
        throws ServletException, IOException {

        if (null == SecurityContextInfo.getCurrentUser()){
            response.setContentType("text/html;charset=GBK");
            response.getWriter().println("��������ʱ��û�в������Ự�Ѿ����ڡ������µ�¼��<br>");
            response.getWriter().println("<a href='" + request.getContextPath() + "' target='_top'>���ص�¼ҳ��</a>");
//            response.sendRedirect(request.getContextPath());
            return;
        }
        
        filterChain.doFilter(request, response);    
    }

}
