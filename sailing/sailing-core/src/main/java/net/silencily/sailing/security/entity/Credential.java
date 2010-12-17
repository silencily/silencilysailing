package net.silencily.sailing.security.entity;

import java.io.Serializable;

/**
 * 信用凭证，即遗留系统的用户帐号和密码
 * @hibernate.class table="security_credential"
 */

public class Credential implements Serializable {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -3574460893678143632L;

	private String password;

	private String username;

	private String appUserId;

	private String id;
	
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
	 * @hibernate.property
	 */
	public String getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(String property1) {
		this.appUserId = property1;
	}
	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 */
	public String getUsername() {
		return username;
	}

	public void setUsername(String property1) {
		this.username = property1;
	}
	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String property1) {
		this.password = property1;
	}
	
	/**
	 * @hibernate.version  unsaved-value="null"
	 */
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
