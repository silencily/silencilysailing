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
 * ϵͳ��һ���û�, Ϊ�˵õ��û���չ��Ϣ, �� {@link org.acegisecurity.userdetails.UserDetails} ������չ
 * @since 2006-1-19
 * @author ����
 * @version $Id: ExtensionalUserDetails.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public interface ExtensionalUserDetails extends UserDetails {
	
	//	~ Static fields/initializers =============================================
	
	/** �����û����û��� */
	String ANONYMOUS_USERNAME = "anonymous";
	
	/** �����û�ӵ�еĽ�ɫ�� */
	String ANONYMOUS_USER_ROLENAME = "ROLE_ANONYMOUS";
	
	
	//	~ Methods ================================================================
	
	/**
	 * �õ��û��߼� Id, ҵ��ϵͳʵ����Ӧ�ù���������
	 * @return the user id
	 */
	String getId();
	
	/**
	 * �õ��û���֯����
	 * @return the organization
	 */
	Organization getOrganization();
	
	/**
	 * �õ��û���������
	 * @return the chinese name
	 */
	String getChineseName();
	
	/**
	 * �õ��û���߼���Ľ�ɫ Level
	 * @see net.silencily.sailing.security.entity.coheg.security.entity.Role#LEVEL_ARRAY
	 * @return the top role level of user
	 */
	int getTopRoleLevel();
	
	/**
	 * �õ��û���չ���Լ�, elements fill with {@link net.silencily.sailing.security.entity.coheg.security.entity.UserProp}
	 * @return the user properties
	 */
	Set getUserProps();
	
	void setAuthorities(GrantedAuthority[] authorities);
}
