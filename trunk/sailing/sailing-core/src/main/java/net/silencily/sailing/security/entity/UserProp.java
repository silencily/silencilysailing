package net.silencily.sailing.security.entity;

import java.io.Serializable;

/**
 * 
 * @author qian
 * @hibernate.class table="security_user_prop"
 */
public class UserProp implements Serializable {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -899131453975563439L;

	private UserPropId id = new UserPropId();
	
	private String propValue;
	
	private Integer version;
	
	/**
	 * @hibernate.composite-id
	 */
	public UserPropId getId() {
		return id;
	}

	public void setId(UserPropId id) {
		this.id = id;
	}
	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 */
	public String getPropValue() {
		return propValue;
	}

	public void setPropValue(String propValue) {
		this.propValue = propValue;
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
