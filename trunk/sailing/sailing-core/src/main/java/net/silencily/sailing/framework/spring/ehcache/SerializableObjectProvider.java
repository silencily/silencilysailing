/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.spring.ehcache;

import java.io.Serializable;

import org.springframework.util.Assert;

/**
 * <class>SerializableObjectProvider</class> 的目的是为没有实现 {@link java.io.Serializable} 的 class 作一层封装以便放到 cache 中
 * @since 2005-11-4
 * @author 王政
 * @version $Id: SerializableObjectProvider.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class SerializableObjectProvider implements Serializable {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -4324391916079650895L;
	
	private Object sourceObject;
	
	public SerializableObjectProvider(Object sourceObject) {
		Assert.notNull(sourceObject, " sourceObject is required. ");
		this.sourceObject = sourceObject;
	}

	/**
	 * @return Returns the sourceObject.
	 */
	public Object getSourceObject() {
		return sourceObject;
	}
	
}
