package net.silencily.sailing.security.entity;

import java.io.Serializable;

/**
 * 帐号关联
 * @hibernate.class table="security_association"
 */

public class Association implements Serializable {
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 6393435656262252754L;

	private String id;
	
	/**
	 * @link aggregation
	 * @associates com.coheg.security.entity.Credential
	 * @directed directed
	 * @supplierCardinality 0..*
	 */
	private java.util.Set credentials = null;

	/**
	 * 统一用户管理系统的用户id
	 */

	private String userId;

	private Integer version;
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
	 * @hibernate.property  type = "com.coheg.framework.hibernate3.CStringType" length = "32"
	 */
	public String getUserId() {
		return userId;
	}

	public void setUserId(String property1) {
		this.userId = property1;
	}
	/**
	 * @hibernate.set lazy="true" 
	 * @hibernate.one-to-many class="com.coheg.security.entity.Credential"
	 * @hibernate.key column="association_id"
	 */
	public java.util.Set getCredentials() {
		return credentials;
	}

	public void setCredentials(java.util.Set credentials) {
		this.credentials = credentials;
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
