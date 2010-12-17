/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.intercept.web.cache;

import java.io.Serializable;

/**
 * @since 2006-2-13
 * @author ÍõÕþ
 * @version $Id: PermissionHolder.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class PermissionHolder implements Serializable {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 1123909438971882875L;

	private String recipent;
	
	private String resourceId;

	public PermissionHolder() {
	}
	
	
	/**
	 * @param recipent
	 * @param resourceId
	 */
	public PermissionHolder(String recipent, String resourceId) {
		this.recipent = recipent;
		this.resourceId = resourceId;
	}
	
	
	/**
	 * @return Returns the recipent.
	 */
	public String getRecipent() {
		return recipent;
	}
	/**
	 * @param recipent The recipent to set.
	 */
	public void setRecipent(String recipent) {
		this.recipent = recipent;
	}
	/**
	 * @return Returns the resourceId.
	 */
	public String getResourceId() {
		return resourceId;
	}
	/**
	 * @param resourceId The resourceId to set.
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
}
