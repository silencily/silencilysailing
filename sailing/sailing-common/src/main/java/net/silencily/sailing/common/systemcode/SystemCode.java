/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.systemcode;

import java.io.Serializable;

import net.silencily.sailing.common.domain.tree.FlatTreeNode;


/**
 * 系统代码, Use Composite Pattern
 * @since 2006-9-19
 * @author java2enterprise
 * @version $Id: SystemCode.java,v 1.1 2010/12/10 10:54:20 silencily Exp $
 */
public class SystemCode implements Serializable, FlatTreeNode {
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 1764715495228322683L;
	
	/** 根节点 */
	public static final String TREE_NODE_ROOT_ID = "0";
	
	/** 编码, 全局唯一 */
	private String code;
	
	/** 名称 */
	private String name;
	
	/** 描述 */
	private String description;
	
	/** 父编码 */
	private String parentCode;
	
	/** 排序号 */
	private Integer sequenceNo;
	
	// attributes for FlatTreeNode
	private boolean hasChildren;
	
	/**
	 * @return the sequenceNo
	 */
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	/**
	 * @param sequenceNo the sequenceNo to set
	 */
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
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
	 * @return the parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode the parentCode to set
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
	//~ methods for FlatTreeNode
	
	public String getCaptain() {
		return getName();
	}

	public Object getData() {
		return this;
	}

	public String getIdentity() {
		return getCode();
	}

	public String getImageType() {
		if (TREE_NODE_ROOT_ID.equals(getCode())) {
			return "0";
		}
		if (isHasChildren()) {
			return "1";
		}
		return "2";
	}

	public boolean isCanbeSelected() {
		return true;
	}

	/**
	 * @return the hasChildren
	 */
	public boolean isHasChildren() {
		return hasChildren;
	}

	/**
	 * @param hasChildren the hasChildren to set
	 */
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public boolean isLeaf() {
		return false;
	}
	
	
	
}
