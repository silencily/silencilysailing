/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.ui.rememberme;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.providers.rememberme.RememberMeAuthenticationToken;
import org.acegisecurity.ui.AuthenticationDetailsSource;
import org.acegisecurity.ui.AuthenticationDetailsSourceImpl;
import org.acegisecurity.ui.rememberme.RememberMeServices;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * <class>SameDomainTokenBasedRememberMeService</class> 用于实现同一 Domain 下的 cookie 共享, 以此为基础实现 SSO
 * @since 2006-2-27
 * @author 王政
 * @version $Id: SameDomainTokenBasedRememberMeService.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class SameDomainTokenBasedRememberMeService implements RememberMeServices, InitializingBean {
	
    //~ Static fields/initializers =============================================

    public static final String ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE_KEY = "ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE";
    public static final String DEFAULT_PARAMETER = "_acegi_security_remember_me";
    public static final String DEFAULT_TOKEN_VALIDITY_SECONDS_PARAMETER = "_acegi_security_remember_me_token_validity_seconds";
    public static final long DEFAULT_TOKEN_VALIDITY_SECONDS = 1209600; // 14 days
    
    protected static final Log logger = LogFactory.getLog(SameDomainTokenBasedRememberMeService.class);

    //~ Instance fields ========================================================
    
    private AuthenticationDetailsSource authenticationDetailsSource = new AuthenticationDetailsSourceImpl();
    private UserDetailsService userDetailsService;
    private String key;
    private String parameter = DEFAULT_PARAMETER;
    private String tokenValiditySecondsPameter = DEFAULT_TOKEN_VALIDITY_SECONDS_PARAMETER;
    
    /** cookie 过期时是否删除, 如果用户选择 cookie 生命周期为浏览器进程, 值必须为 false 以保证各应用之间的单点登陆 ! */
    private boolean deleteCookieIfExpired = false;
    
    //~ Methods ================================================================

    public void setUserDetailsService(UserDetailsService authenticationDao) {
        this.userDetailsService = authenticationDao;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

	public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
    
    /**
	 * @param tokenValiditySecondsPameter The tokenValiditySecondsPameter to set.
	 */
	public void setTokenValiditySecondsPameter(String tokenValiditySecondsPameter) {
		this.tokenValiditySecondsPameter = tokenValiditySecondsPameter;
	}
    
    /**
	 * @param deleteCookieIfExpired The deleteCookieIfExpired to set.
	 */
	public void setDeleteCookieIfExpired(boolean deleteCookieIfExpired) {
		this.deleteCookieIfExpired = deleteCookieIfExpired;
	}

	public void afterPropertiesSet() throws Exception {
        Assert.hasLength(key);
        Assert.hasLength(parameter);
        Assert.notNull(userDetailsService);
	}

	
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
    	Assert.isInstanceOf(NeverRemainCookiesSavedReqeustAwareWrapper.class, request, " 请使用 " + NeverRemainCookiesSavedReqeustAwareWrapper.class + "以实现单点登录!");
    	Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        for (int i = 0; i < cookies.length; i++) {
            if (ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE_KEY.equals(cookies[i].getName())) {
            	
                String cookieValue = cookies[i].getValue();

                if (Base64.isArrayByteBase64(cookieValue.getBytes())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Remember-me cookie detected");
                    }

                    // Decode token from Base64
                    // format of token is:  
                    //     username + ":" + expiryTime + ":" + Md5Hex(username + ":" + expiryTime + ":" + password + ":" + key)
                    String cookieAsPlainText = new String(Base64.decodeBase64(cookieValue.getBytes()));
                    String[] cookieTokens = StringUtils.delimitedListToStringArray(cookieAsPlainText, ":");

                    if (cookieTokens.length == 3) {
                        long tokenExpiryTime;

                        try {
                            tokenExpiryTime = new Long(cookieTokens[1])
                                .longValue();
                        } catch (NumberFormatException nfe) {
                            cancelCookie(request, response,
                                "Cookie token[1] did not contain a valid number (contained '"
                                + cookieTokens[1] + "')");

                            return null;
                        }

                        // Check it has not expired
                        if (deleteCookieIfExpired) {
                            if (tokenExpiryTime < System.currentTimeMillis()) {
                                cancelCookie(request, response,
                                    "Cookie token[1] has expired (expired on '"
                                    + new Date(tokenExpiryTime)
                                    + "'; current time is '" + new Date() + "')");

                                return null;
                            }
                        }
   

                        // Check the user exists
                        // Defer lookup until after expiry time checked, to possibly avoid expensive lookup
                        UserDetails userDetails;

                        try {
                            userDetails = this.userDetailsService.loadUserByUsername(cookieTokens[0]);
                        } catch (UsernameNotFoundException notFound) {
                            cancelCookie(request, response,
                                "Cookie token[0] contained username '"
                                + cookieTokens[0] + "' but was not found");

                            return null;
                        }

                        // Immediately reject if the user is not allowed to login
                        if (!userDetails.isAccountNonExpired()
                            || !userDetails.isCredentialsNonExpired()
                            || !userDetails.isEnabled()) {
                            cancelCookie(request, response,
                                "Cookie token[0] contained username '"
                                + cookieTokens[0]
                                + "' but account has expired, credentials have expired, or user is disabled");

                            return null;
                        }

                        // Check signature of token matches remaining details
                        // Must do this after user lookup, as we need the DAO-derived password
                        // If efficiency was a major issue, just add in a UserCache implementation,
                        // but recall this method is usually only called one per HttpSession
                        // (as if the token is valid, it will cause SecurityContextHolder population, whilst
                        // if invalid, will cause the cookie to be cancelled)
                        String expectedTokenSignature = DigestUtils.md5Hex(userDetails
                                .getUsername() + ":" + tokenExpiryTime + ":"
                                + userDetails.getPassword() + ":" + this.key);

                        if (!expectedTokenSignature.equals(cookieTokens[2])) {
                            cancelCookie(request, response,
                                "Cookie token[2] contained signature '"
                                + cookieTokens[2] + "' but expected '"
                                + expectedTokenSignature + "'");

                            return null;
                        }
                        // By this stage we have a valid token
                        if (logger.isDebugEnabled()) {
                            logger.debug("Remember-me cookie accepted");
                        }

                        /** set details for {@link SessionRegistryUtils#obtainSessionIdFromAuthentication} */
                        RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(this.key, userDetails, userDetails.getAuthorities());
                        /** create a new session if dont't exist, for {@link WebAuthenticationDetails} */
                        request.getSession(true);
                        auth.setDetails(authenticationDetailsSource.buildDetails((HttpServletRequest) request));                        
                        return auth;
                    } else {
                        cancelCookie(request, response,
                            "Cookie token did not contain 3 tokens; decoded value was '"
                            + cookieAsPlainText + "'");

                        return null;
                    }
                } else {
                    cancelCookie(request, response,
                        "Cookie token was not Base64 encoded; value was '"
                        + cookieValue + "'");

                    return null;
                }
            }
        }

        return null;
    }

    public void loginFail(HttpServletRequest request,
        HttpServletResponse response) {
    	
        cancelCookie(request, response,
            "Interactive authentication attempt was unsuccessful");
    }

    public void loginSuccess(HttpServletRequest request,
        HttpServletResponse response, Authentication successfulAuthentication) {
    	
        // Exit if the principal hasn't asked to be remembered
        if (!ServletRequestUtils.getBooleanParameter(request, parameter, false)) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "Did not send remember-me cookie (principal did not set parameter '"
                    + this.parameter + "')");
            }

            return;
        }

        // Determine username and password, ensuring empty strings
        Assert.notNull(successfulAuthentication.getPrincipal());
        Assert.notNull(successfulAuthentication.getCredentials());

        String username;
        String password;

        if (successfulAuthentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) successfulAuthentication.getPrincipal())
                .getUsername();
            password = ((UserDetails) successfulAuthentication.getPrincipal())
                .getPassword();
        } else {
            username = successfulAuthentication.getPrincipal().toString();
            password = successfulAuthentication.getCredentials().toString();
        }

        Assert.hasLength(username);
        Assert.hasLength(password);
        
        // 从 ui 获取 cookie 生命周期
        long tokenValiditySeconds = ServletRequestUtils.getLongParameter(request, tokenValiditySecondsPameter, DEFAULT_TOKEN_VALIDITY_SECONDS);
        
        long expiryTime = tokenValiditySeconds < 0 ? -1 : System.currentTimeMillis() + (tokenValiditySeconds * 1000);

        // construct token to put in cookie; format is:
        //     username + ":" + expiryTime + ":" + Md5Hex(username + ":" + expiryTime + ":" + password + ":" + key)
        String signatureValue = new String(DigestUtils.md5Hex(username + ":"
                    + expiryTime + ":" + password + ":" + key));
        String tokenValue = username + ":" + expiryTime + ":" + signatureValue;
        String tokenValueBase64 = new String(Base64.encodeBase64(tokenValue.getBytes()));
        
        Cookie cookie = makeValidCookie((int) tokenValiditySeconds, tokenValueBase64);
        response.addCookie(cookie);
  
        if (logger.isDebugEnabled()) {
            logger.debug("Added remember-me cookie for user '" + username
                + "', expiry: '" + new Date(expiryTime) + "'");
        }
    }

    protected Cookie makeCancelCookie() {
    	return makeSameDomainCookie(0, null);
    }
        
    protected Cookie makeValidCookie(int maxAge, String tokenValueBase64) {
    	return makeSameDomainCookie(maxAge, tokenValueBase64);
    }
    
    protected Cookie makeSameDomainCookie(int maxAge, String value) {    	
    	Cookie cookie = new Cookie(ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE_KEY, value);
    	cookie.setMaxAge(maxAge);
    	// 使用 "/" 使同一 Domain 下的 webapp 都能读到
    	cookie.setPath("/");
    	return cookie;
    }
        

    private void cancelCookie(HttpServletRequest request,
        HttpServletResponse response, String reasonForLog) {
        if (logger.isErrorEnabled()) {
            logger.error("Cancelling cookie for reason: " + reasonForLog);
        }
       
        response.addCookie(makeCancelCookie());
    }
}
