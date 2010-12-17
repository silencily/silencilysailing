/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * <class>Organization</class> 代表组织机构, 是一个树状结构
 * @since 2006-3-15
 * @author 王政
 * @version $Id: Organization.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @hibernate.class table = "security_organization"
 */
public class Organization implements Serializable {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -3803054385931695368L;
	
	private String id;
	
	private String name;
	
	private Organization parent;
	
	private Set children = new HashSet();
	
	private int childrenCount = 0;
	
	/**介绍*/
	//private String introduce;	
	/**部门主页*/
	//private String homepage;
	/**部门电话*/
	//private String tel;
	/**部门地址*/
	//private String address;
	/**备注*/
	//private String remark;

	/**
	 * @hibernate.id generator-class = "assigned" length = "50"
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100" column = "org_name"
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @hibernate.many-to-one class = "com.coheg.security.entity.Organization" column = "parent_id" index = "idx_organization_parent_id"
	 * @return Returns the parent.
	 */
	public Organization getParent() {
		return parent;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(Organization parent) {
		this.parent = parent;
	}

	/**
	 * @hibernate.property formula = " ( select count(*) from security_organization c where c.parent_id = id )  "
	 * @return the childrenCount
	 */
	public int getChildrenCount() {
		return childrenCount;
	}

	/**
	 * @param childrenCount the childrenCount to set
	 */
	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}
	
	/**
	 * @hibernate.set table = "security_organization" cascade = "all-delete-orphan" inverse = "true" order-by = "org_name"
	 * @hibernate.key column = "parent_id"
	 * @hibernate.one-to-many class = "com.coheg.security.entity.Organization"
	 * @return children
	 */
	public Set getChildren() {
		if (children == null) {
			children = new HashSet();
		}
		return children;
	}
	public void setChildren(Set children) {
		this.children = children;
	}
}
