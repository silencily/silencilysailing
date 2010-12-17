/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.basic.security.acegi.ui.webapp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.silencily.sailing.basic.security.acegi.userdetails.ExtensionalUserDetails;
import net.silencily.sailing.basic.security.acegi.userdetails.hibernate.HibernateAuthenticationDao;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.dict.domain.CommonBasicCode;
import net.silencily.sailing.common.dict.service.BasicCodeService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.security.SecurityContextInfo;
import net.silencily.sailing.security.acegi.ui.sso.SingleSignOnService;
import net.silencily.sailing.security.acegi.userdetails.DisabledUserException;
import net.silencily.sailing.security.acegi.userdetails.RepeatFailureException;
import net.silencily.sailing.security.model.CurrentUser;
import net.silencily.sailing.security.model.DefaultCurrentUser;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.event.authentication.InteractiveAuthenticationSuccessEvent;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;


/**
 * 扩展 AuthenticationProcessingFilter, 实现 cookie 及 数据库 的单点登录功能
 * @since 2006-7-8
 * @author 王政
 * @version $Id: CustomAuthenticationProcessingFilter.java,v 1.1 2010/12/10 10:56:48 silencily Exp $
 */
public class CustomAuthenticationProcessingFilter extends AuthenticationProcessingFilter {
	
	private static transient Log logger = LogFactory.getLog(CustomAuthenticationProcessingFilter.class);
	
	private SingleSignOnService singleSignOnService;
	
	/**
	 * @param singleSignOnService the singleSignOnService to set
	 */
	public void setSingleSignOnService(SingleSignOnService singleSignOnService) {
		this.singleSignOnService = singleSignOnService;
		if (logger.isInfoEnabled()) {
			logger.info(" Set  SingleSignOnService " + singleSignOnService.getClass() + " for  " + CustomAuthenticationProcessingFilter.class);
		}
	}


	/**
	 * @see org.acegisecurity.ui.AbstractProcessingFilter#successfulAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.acegisecurity.Authentication)
	 */
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success: " + authResult.toString());
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        if (logger.isDebugEnabled()) {
            logger.debug("Updated SecurityContextHolder to contain the following Authentication: '" + authResult + "'");
        }

        String targetUrl = obtainFullRequestUrl(request);
        
        if (isAlwaysUseDefaultTargetUrl()) {
            targetUrl = null;
        }

        if (StringUtils.isBlank(targetUrl)) {
            targetUrl = request.getContextPath() + getDefaultTargetUrl();
        }
        
//        String serviceCallback = InternetSupportAuthenticationProcessingFilterEntryPoint.obtainServiceCallback(request);
//        if (StringUtils.isNotBlank(serviceCallback)) {
//        	targetUrl += "&url=" + URLEncoder.encode(serviceCallback, "UTF-8");
//        }

//        serviceCallback = URLEncoder.encode(serviceCallback, "UTF-8");
//        targetUrl = InternetSupportAuthenticationProcessingFilterEntryPoint.appendServiceCallback(targetUrl, serviceCallback);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Redirecting to target URL from HTTP Session (or default): " + targetUrl);
        }

        onSuccessfulAuthentication(request, response, authResult);

        getRememberMeServices().loginSuccess(request, response, authResult);
        
        // implement sso
        /*
        if (singleSignOnService != null) {
        	try {
        		CrossThreadPassedValue passedValue = singleSignOnService.syncOperationBeforeSSOLoginRunnerStart(request, response, authResult);
        		Thread ssoLoginThread = new Thread(new SSOLoginRunner(singleSignOnService, passedValue));
        		ssoLoginThread.start();
        	} catch (Throwable t) {
        		if (logger.isErrorEnabled()) {
        			logger.error(" 单点登录时发生异常 : " + t);
        		}
        	}
        }*/
        
        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }
        
        
        
        //将当前用户的信息放置到SESSION当中 BEGIN
        HttpSession session = request.getSession(true);
        CurrentUser cu = null;
        if (SecurityContextHolder.getContext() != null) {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
        		ExtensionalUserDetails user = (ExtensionalUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (user != null) {
                    cu = user.getCurrentUser();
                }
            }
        }
        session.setAttribute("acigeCurrentUser", cu);
        SecurityContextInfo.setCurrentUser(cu);
        //将当前用户的信息放置到SESSION当中 END 
        
        //初始化基础编码
        Map<String, String> map = new HashMap<String, String>();
        BasicCodeService service = (BasicCodeService) ServiceProvider.getService(BasicCodeService.SERVICE_NAME);
        ComboSupportList csl = service.getComboListAll();
        for (Object o : csl) {
            CommonBasicCode c = (CommonBasicCode) o;
            map.put(c.getCode(), c.getName());
        }
        SecurityContextInfo.getSession().setAttribute("initBaseCode", map);

        // 登录失败次数清零
        DefaultCurrentUser dcu = (DefaultCurrentUser)SecurityContextInfo.getCurrentUser();				
        if (dcu != null) {
            TblCmnUser tcu = (TblCmnUser)getService().load(TblCmnUser.class, dcu.getUserId());
            tcu.setFailedTimes("0");
            getService().update(tcu);
        }
        
        response.sendRedirect(response.encodeRedirectURL(targetUrl));
	}

	
	/**
	 * @see org.acegisecurity.ui.AbstractProcessingFilter#unsuccessfulAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.acegisecurity.AuthenticationException)
	 */
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        // 如果是验证失败，则登录失败次数+1
        String permissionTimes = "";
        if (failed.getClass() == BadCredentialsException.class) {
            String empCd = request.getParameter("j_username");
            if (!StringUtils.isBlank(empCd)) {
                DetachedCriteria criteria = DetachedCriteria.forClass(TblCmnUser.class);
                criteria.add(Restrictions.eq("delFlg","0"));
//               criteria.createAlias("emp","emp");
                criteria.add(Restrictions.eq("empCd", empCd));
//                criteria.add(Restrictions.eq("delFlg", "0"));
                List list = getService().findByCriteria(criteria);
                if (!list.isEmpty()) {
                    TblCmnUser tcu = (TblCmnUser) list.get(0);
                    if (!StringUtils.isBlank(tcu.getFailedTimes())) {
                        tcu.setFailedTimes(Integer.toString(Integer.parseInt(tcu.getFailedTimes()) + 1));
                    } else {
                        tcu.setFailedTimes("1");
                    }
                    permissionTimes = Integer.toString(HibernateAuthenticationDao.ATTMEPT_LIMIT - Integer.parseInt(tcu.getFailedTimes()));
                    getService().update(tcu);
                }
            }
        }

        SecurityContextHolder.getContext().setAuthentication(null);

        if (logger.isDebugEnabled()) {
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
        }

        String failureUrl = getExceptionMappings().getProperty(failed.getClass().getName(), getAuthenticationFailureUrl());
//        String serviceCallback = InternetSupportAuthenticationProcessingFilterEntryPoint.obtainServiceCallback(request);
//        serviceCallback = URLEncoder.encode(serviceCallback, "UTF-8");
//        failureUrl = InternetSupportAuthenticationProcessingFilterEntryPoint.appendServiceCallback(failureUrl, serviceCallback);
        if (failed.getClass() == UsernameNotFoundException.class) {
            failureUrl = failureUrl + "&type=1";
        } else if (failed.getClass() == RepeatFailureException.class) {
            failureUrl = failureUrl + "&type=2";
        } else if (failed.getClass() == AuthenticationServiceException.class) {
            failureUrl = failureUrl + "&type=3";
        } else if (failed.getClass() == DisabledUserException.class) {
            failureUrl = failureUrl + "&type=4";
        } else {
            failureUrl = failureUrl + "&type=5&times=" + permissionTimes;
        }
            
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication request failed: " + failed.toString());
        }

        try {
            request.getSession().setAttribute(ACEGI_SECURITY_LAST_EXCEPTION_KEY, failed);
        } catch (Exception ignored) {}

        onUnsuccessfulAuthentication(request, response, failed);

        getRememberMeServices().loginFail(request, response);

        sendRedirect(request, response, failureUrl);
	}

    /**
    *功能描述：调用共同接口方法
    */
    private CommonService getService() {
        return (CommonService) ServiceProvider.getService(CommonService.SERVICE_NAME);
    }

}