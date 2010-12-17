/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.basic.security.acegi.context;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.silencily.sailing.basic.security.acegi.RemoteUserContextUtils;
import net.silencily.sailing.security.acegi.userdetails.ExtensionalUserDetails;

import org.acegisecurity.GrantedAuthority;


/**
 * <class>WebServiceOutSecurityFilter</class> 为 webservice 的访问提供一个入口, 她将向 Context 里放一个 anonymous user
 * @since 2006-6-12
 * @author 王政
 * @version $Id: WebServiceOutSecurityFilter.java,v 1.1 2010/12/10 10:56:45 silencily Exp $
 */
public class WebServiceOutSecurityFilter implements Filter {

	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(
		ServletRequest requst, 
		ServletResponse response,
		FilterChain chain) 
		throws IOException, ServletException {
		
		ExtensionalUserDetails details = RemoteUserContextUtils.getAnonymousUser();
		GrantedAuthority[] authorities = details.getAuthorities();
		String[] roles = new String[authorities.length];
		for (int i = 0; i < authorities.length; i++) {
			roles[i] = authorities[i].getAuthority();
		}
		
		RemoteUserContextUtils.setMockRemoteUser2Context(details, roles);
		chain.doFilter(requst, response);
	}

	public void destroy() {
	}

}
