/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent.hibernate3.entity;

import java.io.Serializable;

/**
 * 代码封装对象, similar with {@link UserWrapper}
 * @since 2006-7-23
 * @author java2enterprise
 * @version $Id: CodeWrapper.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class CodeWrapper implements Serializable {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 6131568064883185039L;
	
	/** 代码 code */
	protected String code;
	
	/** 代码 name */
	protected String name;
	
	/** 代码描述 */
	protected String description;
	 
    public CodeWrapper() {
        super();
    }
    
    public CodeWrapper(String code, String name) {
        this.code = code;
        this.name = name;
    }

	public CodeWrapper(String code, String name, String description) {
		this.code = code;
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
