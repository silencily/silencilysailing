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
 * 负责解析 {@link UserWrapper} 的 hibernate 类型
 * @see UserWrapper
 * @since 2006-7-23
 * @author java2enterprise
 * @version $Id: UserWrapperType.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class UserWrapperType extends AbstractEntityWrapperType {
	
	private static transient Log logger = LogFactory.getLog(UserWrapperType.class);
	
	private static List callbackChains = new ArrayList();
	
	/**
	 * 用于处理 UserWrapper 的回调接口
	 *
	 */
	public static interface UserWrapperCallbackHandler {		
		
		/**
		 * 在 {@link UserWrapperType} load 到 UserWrapper 后的处理方法
		 * @param userWrapperLoaded userWrapper loaded
		 * @throws Throwable 如果发生任何异常
		 */
		void afterNullSafeGet(UserWrapper userWrapperLoaded) throws Throwable;
	}
	
	/**
	 * 为 UserWrapperType 注册一个回调接口
	 * @param handler the handler
	 */
	public static void registerCallbackHandler(UserWrapperCallbackHandler handler) {
		if (logger.isInfoEnabled()) {
			logger.info(" 为 " + UserWrapperType.class + " 注册回调接口 " + handler);
		}
		callbackChains.add(handler);
	}
	
	/**
	 * 为 UserWrapperType  注销一个回调接口
	 * @param handler the handler
	 */
	public static void deRegisterCallbackHandler(UserWrapperCallbackHandler handler) {
		if (logger.isInfoEnabled()) {
			logger.info(" 为 " + UserWrapperType.class + " 注销回调接口 " + handler);
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
		Assert.isInstanceOf(returnedClass(), component, " 类型声明错误 ");
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
					logger.debug(" 执行回调接口 " + callbackHandler + " 时错误 : " + t);
				}
			}
		}
		return userWrapper;
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) 
		throws HibernateException, SQLException {
		String name = null;
		if (value != null) {
			Assert.isInstanceOf(returnedClass(), value, " 类型声明错误 ");
			name = ((UserWrapper) value).getUsername();
		}
		Hibernate.STRING.nullSafeSet(st, name, index);	
	}

}
