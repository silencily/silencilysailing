package net.silencily.sailing.security.entity;

import java.io.Serializable;

/**
 * @since 2006-01-19
 * @version $Id: UserPropId.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * the compisiteId of {@link UserPropId}
 */
public class UserPropId implements Serializable {
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -3693688013327822806L;
	
	private User user;
	
	private String propName;
	/**
	 * @hibernate.key-property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 */
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	/**
	 * @hibernate.key-many-to-one class="com.coheg.security.entity.User" column="user_id"/>
	 * @return Returns the user.
	 */
	public  User getUser() {
		return user;
	}
	/**
	 * @param user The user to set.
	 */
	public  void setUser(User user) {
		this.user = user;
	}
	
	
}
