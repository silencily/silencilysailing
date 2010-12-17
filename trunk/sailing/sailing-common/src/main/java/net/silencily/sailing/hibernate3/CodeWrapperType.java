/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.hibernate3; 

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.silencily.sailing.framework.persistent.hibernate3.entity.CodeWrapper;
import net.silencily.sailing.framework.persistent.hibernate3.usertype.AbstractEntityWrapperType;
import net.silencily.sailing.framework.persistent.hibernate3.usertype.UserWrapperType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;
import org.springframework.util.Assert;
/**
 * 负责解析 {@link CodeWrapper} 的 hibernate 类型
 * @since 2006-7-23
 * @author java2enterprise
 * @version $Id: CodeWrapperType.java,v 1.1 2010/12/10 10:54:14 silencily Exp $
 */
public class CodeWrapperType extends AbstractEntityWrapperType {
	
	private static transient Log logger = LogFactory.getLog(CodeWrapperType.class);
	
	//public static final String CODE_WRAPPER_CALLBACK_HANDLER_VERDOR_CLASS_NAME = "com.coheg.common.persistent.hibernate3.spi.ReformCodeWrapperCallbackHandler";
	
	private static CodeWrapperCallbackHanler codeWrapperCallbackHanler=new ReformCodeWrapperCallbackHandler();
		
	/**
	 * 用于处理 {@link CodeWrapper} 的回调接口, 一般是根据 code 得到 name, 需要使用者实现
	 *
	 */
	public static interface CodeWrapperCallbackHanler {
		
		/**
		 * 在 {@link CodeWrapper} load 到 CodeWrapper 后的处理方法
		 * @param codeWrapperLoaded codeWrapper loaded
		 * @throws Throwable 如果发生任何异常
		 */
		void afterNullSafeGet(CodeWrapper codeWrapperLoaded) throws Throwable;
		
	}
	
	/**
	 * 为 CodeWrapperType 注册一个回调接口
	 * @param handler the handler
	 */
	public static void registerCallbackHandler(CodeWrapperCallbackHanler handler) {
		//FIXME 暂时只取公用实现
//		if (CODE_WRAPPER_CALLBACK_HANDLER_VERDOR_CLASS_NAME.equals(handler.getClass().getName())) {
//			if (logger.isInfoEnabled()) {
//				logger.info(" 为 " + CodeWrapperType.class + " 注册回调接口 " + handler);
//			}
//			codeWrapperCallbackHanler = handler;
//		}
	}
	
	/**
	 * 为 CodeWrapperType  注销一个回调接口
	 * @param handler the handler
	 */
	public static void deRegisterCallbackHandler(CodeWrapperCallbackHanler handler) {
		if (logger.isInfoEnabled()) {
			logger.info(" 为 " + UserWrapperType.class + " 注销回调接口 " + handler);
		}
		codeWrapperCallbackHanler = null;
	}
		
	public Class returnedClass() {
		return CodeWrapper.class;
	}

	public String[] getPropertyNames() {
		return new String[] {"code"};
	}

	public Type[] getPropertyTypes() {
		return new Type[] {Hibernate.STRING};
	}

	public Object getPropertyValue(Object component, int property) throws HibernateException {
		Assert.isInstanceOf(returnedClass(), component, " 类型声明错误 ");
		CodeWrapper codeWrapper = (CodeWrapper) component;
		return codeWrapper.getCode();
	}

	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) 
		throws HibernateException, SQLException {
		
		String code = (String) Hibernate.STRING.nullSafeGet(rs, names);
		if (code == null) {
			return null;
		}
		CodeWrapper codeWrapper = new CodeWrapperPlus();
		codeWrapper.setCode(code);

		try {
			codeWrapperCallbackHanler.afterNullSafeGet(codeWrapper);
		} catch (Throwable t) {
			if (logger.isDebugEnabled()) {
				logger.debug(" 执行回调接口 " + codeWrapperCallbackHanler + " 时错误 : " + t);
			}
		}
	
		return codeWrapper;
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) 
		throws HibernateException, SQLException {
		
		String name = null;
		if (value != null) {
			Assert.isInstanceOf(returnedClass(), value, " 类型声明错误 ");
			name = ((CodeWrapper) value).getCode();
			CodeWrapperPlus cwp=(CodeWrapperPlus)value;
			if(cwp.isModify())
			try {
				codeWrapperCallbackHanler.afterNullSafeGet(cwp);
			} catch (Throwable t) {
				if (logger.isDebugEnabled()) {
					logger.debug(" 执行回调接口 " + codeWrapperCallbackHanler + " 时错误 : " + t);
				}
			}
		}
		Hibernate.STRING.nullSafeSet(st, name, index);
		
	}

	public Object deepCopy(Object value) throws HibernateException {
		// TODO Auto-generated method stub
		if(value==null)
			return null;
		if(CodeWrapperPlus.class.isAssignableFrom(value.getClass()))
			try {
				return ((CodeWrapperPlus)value).clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw new HibernateException(e);
			}
		return null;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		// TODO Auto-generated method stub
		if(x==null)
			return y==null;
		return x.equals(y);
	}


}
