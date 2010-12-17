package net.silencily.sailing.security.entity;

import java.io.Serializable;
import java.util.LinkedHashSet;

/**
 * @author qian
 * @hibernate.class table="security_code_category"
 */
public class CodeCategory implements Serializable {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -1996241810255113492L;

	private String value;
	
	private String name;
	
	private String description;

	private Integer version;
	
	/**
	 * @link aggregation
	 * @associates com.coheg.security.entity.Code
	 * @directed directed
	 * @supplierCardinality 0..*
	 */
	private java.util.Set codes = new LinkedHashSet();
	
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
	 * @hibernate.id unsaved-value="null" generator-class="assigned" length = "32"
	 */
	public String getValue() {
		return value;
	}

	public void setValue(String property1) {
		this.value = property1;
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
	 * @hibernate.set lazy="true" order-by = "orderField"
	 * @hibernate.key column="code_category_id"
	 * @hibernate.one-to-many class="com.coheg.security.entity.Code"
	 */
	public java.util.Set getCodes() {
		return codes;
	}

	public void setCodes(java.util.Set codes) {
		this.codes = codes;
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
