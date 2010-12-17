/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;


/**
 * @since 2006-7-13
 * @author java2enterprise
 * @version $Id: SingleSignOnService.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface SingleSignOnService {
	
	/**
	 * 在调用 {@link #loginSuccess(Authentication)} 前进行的同步操作, 此操作是不可阻塞的, 例如一个 set session 的操作
     * @param request that contained the valid authentication request
     * @param response to change, cancel or modify the remember-me token
     * @param successfulAuthentication representing the successfully authenticated principal
	 * @return sysc operation result
	 */
	CrossThreadPassedValue syncOperationBeforeSSOLoginRunnerStart(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication);
	
	/**
	 * 单点登录成功后进行的操作, 可能是写数据库或者调用 webservcie 进行广播
	 * @param value return value of {@link #syncOperationBeforeSSOLoginRunnerStart(HttpServletRequest, HttpServletResponse, Authentication)}
	 */
	void loginSuccess(CrossThreadPassedValue value);
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	CrossThreadPassedValue syncOperationBeforeSSOLogoutRunnerStart(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 
	 * @param passedValue
	 */
	void logout(CrossThreadPassedValue passedValue);
}
