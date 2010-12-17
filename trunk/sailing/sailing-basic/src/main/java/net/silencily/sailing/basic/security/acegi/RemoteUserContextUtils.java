/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.basic.security.acegi;

import net.silencily.sailing.basic.security.acegi.userdetails.hibernate.HibernateAuthenticationDao;
import net.silencily.sailing.security.acegi.userdetails.ExtensionalUserDetails;
import net.silencily.sailing.security.entity.User;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.cas.CasAuthenticationToken;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;



/**
 * <class>RemoteUserContextUtils</class> 是权限系统与业务系统最重要的接口, 其主要功能是提供当前登录用户的常用接口
 * @since 2005-9-7
 * @author 王政
 * @version $Id: RemoteUserContextUtils.java,v 1.1 2010/12/10 10:56:47 silencily Exp $
 */
public abstract class RemoteUserContextUtils {
	
	private static final Log logger = LogFactory.getLog(RemoteUserContextUtils.class);
	
	/**
	 * 得到当前登录用户
	 * @return 当前登录用户
	 * @see SecurityContextHolder
	 */
	public static ExtensionalUserDetails getRemoteUser() {
		System.out.println("CLASS -- " + RemoteUserContextUtils.class + " | (Line 46) | "+ "Method --> getRemoteUser() | TimeMillis: " + System.currentTimeMillis());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication == null) {
			System.out.println("CLASS -- " + RemoteUserContextUtils.class + " End (50)");
//tongjq			throw new BaseRuntimeException(" authentication is null , make sure the filter has been configed ! ");
		}
		
		Object principal = authentication.getPrincipal();
		if (principal == null) {
			System.out.println("CLASS -- " + RemoteUserContextUtils.class + " End (56)");
			throw new UsernameNotFoundException(" The user doesn't exists ! ");
		}
		
		if (ExtensionalUserDetails.class.isInstance(principal)) {
			System.out.println("CLASS -- " + RemoteUserContextUtils.class + " End (61)");
			// use Local Authentication
			return (ExtensionalUserDetails) principal;
		} else if (principal.equals(ExtensionalUserDetails.ANONYMOUS_USERNAME)){
			System.out.println("CLASS -- " + RemoteUserContextUtils.class + " End (65)");
			return getAnonymousUser();
		} else if (CasAuthenticationToken.class.isInstance(authentication)) {
			System.out.println("CLASS -- " + RemoteUserContextUtils.class + " End (68)");
			// use Yale Central Authentication Service (CAS) Single Sign On
			UserDetails userDetails = ((CasAuthenticationToken) authentication).getUserDetails();
			Assert.isInstanceOf(ExtensionalUserDetails.class, userDetails, getErrorMessage(userDetails.getClass()));
			return (ExtensionalUserDetails) userDetails;
		}
		
		String errorMessage = getErrorMessage(principal.getClass());
		if (logger.isInfoEnabled()) {
			logger.info(errorMessage);
		}
		System.out.println("CLASS -- " + RemoteUserContextUtils.class + " End (79)");
		throw new IllegalStateException(errorMessage);
	}

	/**
	 * @param detailClass
	 * @return
	 */
	private static String getErrorMessage(Class detailClass) {
		StringBuffer sb = new StringBuffer();
		sb.append(" UserDetails Should be ");
		sb.append(UserDetails.class);
		sb.append(" , but it is ");
		sb.append(detailClass);
		sb.append(" now, please make sure the custom dao ");
		sb.append(HibernateAuthenticationDao.class);
		sb.append(" has been used. ");
		return sb.toString();
	}
	
	/**
	 * 得到当前登录用户的所有角色
	 * @return all roles of remote user
	 * @throws UsernameNotFoundException if user hasn't roles
	 */
	public static String[] getRemoteUserRoles() throws UsernameNotFoundException {
		UserDetails userDetails = getRemoteUser();
		GrantedAuthority[] authorities = userDetails.getAuthorities();
		if (authorities == null) {
			throw new UsernameNotFoundException("User has no GrantedAuthority");
		}
		
		String[] roles = new String[authorities.length];
		
		for (int i = 0; i < authorities.length; i++) {
			roles[i] = authorities[i].getAuthority();
		}
		
		return roles;
	}
	
	
	/**
     * Returns a boolean indicating whether the authenticated user is included
     * in the specified logical "role".  Roles and role membership can be
     * defined using deployment descriptors.  If the user has not been
     * authenticated, the method returns <code>false</code>.
     * 
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
     * @param role	a <code>String</code> specifying the name of the role
     * @return		a <code>boolean</code> indicating whether
     *			the remote user belongs to a given role;
     *			<code>false</code> if the user has not been 
     *			authenticated
	 */
	public static boolean isUserInRole(String role) {		
		String[] userRoles = getRemoteUserRoles();
		
		for (int i = 0; i < userRoles.length; i++) {
			if (ObjectUtils.nullSafeEquals(userRoles, role)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 用户是否可用, 也就是指 request 中用户是否已经登陆
	 * @return whether remote user avaliable
	 */
	public static boolean isRemoteUserAvaliable() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		return authentication != null;
	}
	
	/**
	 * 得到匿名用户
	 * @return the anonymous user
	 */
	public static ExtensionalUserDetails getAnonymousUser() {
		User user = new User();
		user.setUsername(ExtensionalUserDetails.ANONYMOUS_USERNAME);
		user.setChineseName("匿名用户");
		user.setEnabled(true);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setAuthorities(new GrantedAuthority[] {new GrantedAuthorityImpl(ExtensionalUserDetails.ANONYMOUS_USER_ROLENAME)});
		return user;
	}
	
	/**
	 * 设置一个 mock user 到 Context
	 * @param user the user
	 * @param roles the user roles
	 */
	public static void setMockRemoteUser2Context(ExtensionalUserDetails user, String[] roles) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, "111111");
		GrantedAuthority[] authorities = new GrantedAuthority[roles.length];
		for (int i = 0; i < roles.length; i++) {
			authorities[i] = new GrantedAuthorityImpl(roles[i]);
		}
		user.setAuthorities(authorities);
		SecurityContext securityContext = new SecurityContextImpl();
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
	}
	
	
	public static void main(String[] args) {
		User user = new User();
		user.setUsername("admin");
		String[] roles = new String[] {"role1", "role2", "role3"};
		setMockRemoteUser2Context(user, roles);
		ExtensionalUserDetails details = getRemoteUser();
		Assert.isTrue(user.getUsername().equals(details.getUsername()));
		Assert.isTrue(getRemoteUserRoles().length == roles.length);
	}
	
}
