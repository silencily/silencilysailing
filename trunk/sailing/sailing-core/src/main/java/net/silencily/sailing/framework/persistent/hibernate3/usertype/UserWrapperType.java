/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project material
 */
package net.silencily.sailing.framework.persistent.hibernate3.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.silencily.sailing.framework.persistent.hibernate3.entity.UserWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.springframework.util.Assert;

/**
 * ������� {@link UserWrapper} �� hibernate ����
 * @see UserWrapper
 * @since 2006-7-23
 * @author java2enterprise
 * @version $Id: UserWrapperType.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class UserWrapperType extends AbstractEntityWrapperType {
	
	private static transient Log logger = LogFactory.getLog(UserWrapperType.class);
	
	private static List callbackChains = new ArrayList();
	
	/**
	 * ���ڴ��� UserWrapper �Ļص��ӿ�
	 *
	 */
	public static interface UserWrapperCallbackHandler {		
		
		/**
		 * �� {@link UserWrapperType} load �� UserWrapper ��Ĵ�����
		 * @param userWrapperLoaded userWrapper loaded
		 * @throws Throwable ��������κ��쳣
		 */
		void afterNullSafeGet(UserWrapper userWrapperLoaded) throws Throwable;
	}
	
	/**
	 * Ϊ UserWrapperType ע��һ���ص��ӿ�
	 * @param handler the handler
	 */
	public static void registerCallbackHandler(UserWrapperCallbackHandler handler) {
		if (logger.isInfoEnabled()) {
			logger.info(" Ϊ " + UserWrapperType.class + " ע��ص��ӿ� " + handler);
		}
		callbackChains.add(handler);
	}
	
	/**
	 * Ϊ UserWrapperType  ע��һ���ص��ӿ�
	 * @param handler the handler
	 */
	public static void deRegisterCallbackHandler(UserWrapperCallbackHandler handler) {
		if (logger.isInfoEnabled()) {
			logger.info(" Ϊ " + UserWrapperType.class + " ע���ص��ӿ� " + handler);
		}
		callbackChains.remove(handler);
	}
	
	public Class returnedClass() {
		return UserWrapper.class;
	}
	
	public String[] getPropertyNames() {
		return new String[] {"username"};
	}

	public Type[] getPropertyTypes() {
		return new Type[] {Hibernate.STRING};
	}

	public Object getPropertyValue(Object component, int property) throws HibernateException {
		Assert.isInstanceOf(returnedClass(), component, " ������������ ");
		UserWrapper userWrapper = (UserWrapper) component;
		return userWrapper.getUsername();
	}
	
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) 
		throws HibernateException, SQLException {		
		String username = (String) Hibernate.STRING.nullSafeGet(rs, names);
		if (username == null) {
			return null;
		}
		UserWrapper userWrapper = new UserWrapper();
		userWrapper.setUsername(username);
		for (Iterator iter = callbackChains.iterator(); iter.hasNext(); ) {
			UserWrapperCallbackHandler callbackHandler = (UserWrapperCallbackHandler) iter.next();
			try {
				callbackHandler.afterNullSafeGet(userWrapper);
			} catch (Throwable t) {
				if (logger.isDebugEnabled()) {
					logger.debug(" ִ�лص��ӿ� " + callbackHandler + " ʱ���� : " + t);
				}
			}
		}
		return userWrapper;
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) 
		throws HibernateException, SQLException {
		String name = null;
		if (value != null) {
			Assert.isInstanceOf(returnedClass(), value, " ������������ ");
			name = ((UserWrapper) value).getUsername();
		}
		Hibernate.STRING.nullSafeSet(st, name, index);	
	}

}
