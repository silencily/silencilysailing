/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project material
 */
package net.silencily.sailing.framework.persistent.hibernate3.usertype;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.util.EqualsHelper;

/**
 * 实体封装类型解析器的抽象实现
 * @since 2006-7-23
 * @author java2enterprise
 * @version $Id: AbstractEntityWrapperType.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public abstract class AbstractEntityWrapperType implements CompositeUserType {

	public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {
		return cached;
	}

	public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException {
		return (Serializable) value;
	}

	public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
		throw new UnsupportedOperationException("Immutable!");
	}

	public Object replace(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException {
		return original;
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return EqualsHelper.equals(x, y);
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	public boolean isMutable() {
		return false;
	}



}
