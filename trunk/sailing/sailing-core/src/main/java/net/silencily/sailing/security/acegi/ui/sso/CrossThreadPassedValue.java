/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui.sso;


/**
 * ��־�ӿ�, ������ {@link SingleSignOnService#syncOperationBeforeSSOLoginRunnerStart(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.acegisecurity.Authentication)}}
 * �� {@link SingleSignOnService#loginSuccess(CrossThreadPassedValue)}} ֮����̴߳��ݶ���
 * @see CustomAuthenticationProcessingFilter
 * @see SingleSignOnService
 * @since 2006-7-13
 * @author java2enterprise
 * @version $Id: CrossThreadPassedValue.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface CrossThreadPassedValue {

}
