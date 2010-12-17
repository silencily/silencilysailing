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
 * 清除 {@link SecurityContextHolder} 的 filter, 使用这个 filter 的原因是 tomcat 线程池没有清空 SecurityContext,
 * 注意此 filter 必须放在所有安全 filter 的第一位
 * @since 2006-11-11
 * @author java2enterprise
 * @version $Id: ExpirerFilter.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class ExpirerFilter extends OncePerRequestFilter {

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
        throws ServletException, IOException {

        if (null == SecurityContextInfo.getCurrentUser()){
            response.setContentType("text/html;charset=GBK");
            response.getWriter().println("由于您长时间没有操作，会话已经过期。请重新登录。<br>");
            response.getWriter().println("<a href='" + request.getContextPath() + "' target='_top'>返回登录页面</a>");
//            response.sendRedirect(request.getContextPath());
            return;
        }
        
        filterChain.doFilter(request, response);    
    }

}
