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
	 * �ڵ��� {@link #loginSuccess(Authentication)} ǰ���е�ͬ������, �˲����ǲ���������, ����һ�� set session �Ĳ���
     * @param request that contained the valid authentication request
     * @param response to change, cancel or modify the remember-me token
     * @param successfulAuthentication representing the successfully authenticated principal
	 * @return sysc operation result
	 */
	CrossThreadPassedValue syncOperationBeforeSSOLoginRunnerStart(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication);
	
	/**
	 * �����¼�ɹ�����еĲ���, ������д���ݿ���ߵ��� webservcie ���й㲥
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
