/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.userdetails;

import java.util.Set;

import net.silencily.sailing.security.entity.Organization;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;


/**
 * 系统中一个用户, 为了得到用户扩展信息, 对 {@link org.acegisecurity.userdetails.UserDetails} 做了扩展
 * @since 2006-1-19
 * @author 王政
 * @version $Id: ExtensionalUserDetails.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public interface ExtensionalUserDetails extends UserDetails {
	
	//	~ Static fields/initializers =============================================
	
	/** 匿名用户的用户名 */
	String ANONYMOUS_USERNAME = "anonymous";
	
	/** 匿名用户拥有的角色名 */
	String ANONYMOUS_USER_ROLENAME = "ROLE_ANONYMOUS";
	
	
	//	~ Methods ================================================================
	
	/**
	 * 得到用户逻辑 Id, 业务系统实体中应该关联此属性
	 * @return the user id
	 */
	String getId();
	
	/**
	 * 得到用户组织机构
	 * @return the organization
	 */
	Organization getOrganization();
	
	/**
	 * 得到用户中文姓名
	 * @return the chinese name
	 */
	String getChineseName();
	
	/**
	 * 得到用户最高级别的角色 Level
	 * @see net.silencily.sailing.security.entity.coheg.security.entity.Role#LEVEL_ARRAY
	 * @return the top role level of user
	 */
	int getTopRoleLevel();
	
	/**
	 * 得到用户扩展属性集, elements fill with {@link net.silencily.sailing.security.entity.coheg.security.entity.UserProp}
	 * @return the user properties
	 */
	Set getUserProps();
	
	void setAuthorities(GrantedAuthority[] authorities);
}
