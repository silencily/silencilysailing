/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project framework
 */
package net.silencily.sailing.framework.persistent.hibernate3.entity;

import java.io.Serializable;

import net.silencily.sailing.framework.core.User;

/**
 * 组织机构封装对象, similar with {@link UserWrapper}
 * @since 2006-7-23
 * @author java2enterprise
 * @version $Id: DepartmentWrapper.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public class DepartmentWrapper implements Serializable {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 3655061048834122595L;
	
	/** 部门 Id */
	protected String departmentId;
	
	/** 部门名称 */
	protected String departmentName;
	
	public DepartmentWrapper() {
	}

	public DepartmentWrapper(String departmentId, String departmentName) {
		this.departmentId = departmentId;
		this.departmentName = departmentName;
	}
	
	/**
	 * 使用 {@link User} 构造 department wrapper 对象
	 * @see User#getOrganizationId()
	 * @see User#getOrganizationName()
	 * @param user the user object
	 */
	public DepartmentWrapper(User user) {
		this.departmentId = user.getOrganizationId();
		this.departmentName = user.getOrganizationName();
	}
	
	/**
	 * @return the departmentId
	 */
	public String getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	
}
