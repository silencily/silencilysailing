/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project material
 */
package net.silencily.sailing.framework.persistent.hibernate3.entity;

import java.io.Serializable;

import net.silencily.sailing.framework.core.User;

/**
 * ���԰�װ�û��Ķ���, Ŀ���Ǹ����û����õ�������, ��Ҫ���õ�ʵ�������������µ����� : <p/><code>
 * private UserWrapper principal = new UserWrapper();
 * </code><p/> hbm ���������� <p/><code>
 * type = "user_wrapper"  �� type = "com.coheg.persistent.hibernate3.usertype.UserWrapperType"
 * </code><p/>
 * �������� {@link UserWrapperType} �������
 * @see UserWrapperType
 * @since 2006-7-23
 * @author java2enterprise
 * @version $Id: UserWrapper.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class UserWrapper implements Serializable {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -1386749014932828974L;
	
	/** �û��� */
	protected String username;
	
	/** ������ */
	protected String chineseName;

	public UserWrapper() {
	}

	public UserWrapper(String username, String chineseName) {
		this.username = username;
		this.chineseName = chineseName;
	}
	
	/**
	 * ʹ�� {@link User} ���������� user wrapper
	 * @see User#getUsername()
	 * @see User#getChineseName()
	 * @param user the user object
	 */
	public UserWrapper(User user) {
		this.username = user.getUsername();
		this.chineseName = user.getChineseName();
	}
	
	/**
	 * @return the chineseName
	 */
	public String getChineseName() {
		return chineseName;
	}

	/**
	 * @param chineseName the chineseName to set
	 */
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
