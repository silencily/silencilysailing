/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent.hibernate3.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.silencily.sailing.framework.persistent.hibernate3.entity.DepartmentWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.springframework.util.Assert;

/**
 * 负责解析 {@link DepartmentWrapper} 的 hibernate 类型
 * @since 2006-7-23
 * @author java2enterprise
 * @version $Id: DepartmentWrapperType.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class DepartmentWrapperType extends AbstractEntityWrapperType {
	
	private static transient Log logger = LogFactory.getLog(DepartmentWrapperType.class);
	
	private static List callbackChains = new ArrayList();
	
	/**
	 * 用于处理 DepartmentWrapper 的回调接口
	 */
	public static interface DepartmentWrapperCallbackHandler {
		
		/**
		 * 在 {@link DepartmentWrapperType} load 到 DepartmentWrapper 后的处理方法
		 * @param departmentLoaded department loaded
		 * @throws Throwable if any exception happens
		 */
		void afterNullSafeGet(DepartmentWrapper departmentLoaded) throws Throwable;		
	}
	
	/**
	 * 为 DepartmentWrapperType 注册一个回调接口
	 * @param handler the handler
	 */
	public static void registerCallbackHandler(DepartmentWrapperCallbackHandler handler) {
		if (logger.isInfoEnabled()) {
			logger.info(" 为 " + DepartmentWrapperType.class + " 注册回调接口 " + handler);
		}
		callbackChains.add(handler);
	}
	
	/**
	 * 为 DepartmentWrapperType  注销一个回调接口
	 * @param handler the handler
	 */
	public static void deRegisterCallbackHandler(DepartmentWrapperCallbackHandler handler) {
		if (logger.isInfoEnabled()) {
			logger.info(" 为 " + DepartmentWrapperType.class + " 注销回调接口 " + handler);
		}
		callbackChains.remove(handler);
	}
	
	public Class returnedClass() {
		return DepartmentWrapper.class;
	}

	/**
	 * @see org.hibernate.usertype.CompositeUserType#getPropertyNames()
	 */
	public String[] getPropertyNames() {
		return new String[] {"departmentId"};
	}

	/**
	 * @see org.hibernate.usertype.CompositeUserType#getPropertyTypes()
	 */
	public Type[] getPropertyTypes() {
		return new Type[] {Hibernate.STRING};
	}

	/**
	 * @see org.hibernate.usertype.CompositeUserType#getPropertyValue(java.lang.Object, int)
	 */
	public Object getPropertyValue(Object component, int property) throws HibernateException {
		Assert.isInstanceOf(returnedClass(), component, " 类型声明错误 ");
		DepartmentWrapper departmentWrapper = (DepartmentWrapper) component;
		return departmentWrapper.getDepartmentId();
	}

	/**
	 * @see org.hibernate.usertype.CompositeUserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], org.hibernate.engine.SessionImplementor, java.lang.Object)
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) 
		throws HibernateException, SQLException {

		String departmentId = (String) Hibernate.STRING.nullSafeGet(rs, names);
		if (departmentId == null) {
			return null;
		}
		DepartmentWrapper departmentWrapper = new DepartmentWrapper();
		departmentWrapper.setDepartmentId(departmentId);
		for (Iterator iter = callbackChains.iterator(); iter.hasNext(); ) {
			DepartmentWrapperCallbackHandler callbackHandler = (DepartmentWrapperCallbackHandler) iter.next();
			try {
				callbackHandler.afterNullSafeGet(departmentWrapper);
			} catch (Throwable t) {
				if (logger.isDebugEnabled()) {
					logger.debug(" 执行回调接口 " + callbackHandler + " 时错误 : " + t);
				}
			}
		}
		return departmentWrapper;
	}

	/**
	 * @see org.hibernate.usertype.CompositeUserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int, org.hibernate.engine.SessionImplementor)
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) 
		throws HibernateException, SQLException {
		
		String name = null;
		if (value != null) {
			Assert.isInstanceOf(returnedClass(), value, " 类型声明错误 ");
			name = ((DepartmentWrapper) value).getDepartmentId();
		}
		Hibernate.STRING.nullSafeSet(st, name, index);
	}


}
