package net.silencily.sailing.security.entity;

import java.io.Serializable;

/**
 * @version $Id: Code.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * @hibernate.class table="security_code"
 */
public class Code implements Serializable {
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -4324032988534834480L;

	private String id;
	
	private String name;
	
	private String strValue;

	private int intValue;
	
	private String description;

	private int orderField;

	private CodeCategory codeCategory;
	
	private Integer version;
	
	public Code() {
		
	}
	
	/**
	 * @param codeCategory
	 * @param strValue
	 * @param name
	 * @param orderField
	 */
	public Code(CodeCategory codeCategory, String strValue, String name, int orderField) {
		this.codeCategory = codeCategory;
		this.strValue = strValue;
		this.name = name;
		this.orderField = orderField;
	}

	/**
	 * @hibernate.id unsaved-value="null" generator-class="uuid.hex" length = "32"
	 */
	public String getId() {
		return id;
	}

	public void setId(String property1) {
		this.id = property1;
	}
	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 */
	public String getName() {
		return name;
	}

	public void setName(String property1) {
		this.name = property1;
	}
	/**
	 * @hibernate.property
	 */
	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int property1) {
		this.intValue = property1;
	}
	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 */
	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String property1) {
		this.strValue = property1;
	}
	/**
	 * @hibernate.property
	 */
	public int getOrderField() {
		return orderField;
	}

	public void setOrderField(int property1) {
		this.orderField = property1;
	}
	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String property1) {
		this.description = property1;
	}
	/**
	 * @hibernate.many-to-one column="code_category_id"
	 */
	public CodeCategory getCodeCategory() {
		return codeCategory;
	}

	public void setCodeCategory(CodeCategory codeCategory) {
		this.codeCategory = codeCategory;
	}
	/**
	 * @hibernate.version unsaved-value="null"
	 */
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
