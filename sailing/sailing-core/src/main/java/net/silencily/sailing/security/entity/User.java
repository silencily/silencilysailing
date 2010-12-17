package net.silencily.sailing.security.entity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;

import net.silencily.sailing.security.acegi.userdetails.ExtensionalUserDetails;

import org.acegisecurity.GrantedAuthority;
import org.apache.commons.lang.StringUtils;


/**
 * @since 2006-01-19
 * @author qian
 * @author 王政
 * @version $Id: User.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @hibernate.class table="security_user"
 */
public class User implements Serializable, ExtensionalUserDetails {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 4154414006735196812L;
	
	
	// ~ Instance fields ========================================================
	
	// for acegi 
	
	/** 用户标示 */
	private String id;
	
	private String username;
	
	private String password;

	private GrantedAuthority[] authorities;

	private boolean accountNonExpired = true;

	private boolean accountNonLocked = true;

	private boolean credentialsNonExpired = true;

	private boolean enabled = true;
	

	// for project's custom required
	
	private Organization organization;
	
	/** 中文名 */
	private String chineseName;
	
	private Integer version;

	/**
	 * @link aggregation
	 * @associates com.coheg.security.entity.UserProp
	 * @directed directed
	 * @supplierCardinality 0..*
	 */
	java.util.Set userProps = new LinkedHashSet();
	
	/**
	 * @link aggregation
	 * @associates com.coheg.security.entity.Role
	 * @directed directed
	 * @supplierCardinality 0..*
	 */
	java.util.Set roles = new LinkedHashSet();

	// for ui
	
	boolean selected = false;
	
	// ~ Constructors
	// ===========================================================
	
	/**
	 * Default constructor, only for hibernate use
	 */
	public User() {
		// throw new IllegalArgumentException("Cannot use default constructor");
	}
	
	
	// ~ Methods
	// ================================================================
	
	/**
	 * 根据用户动态属性名称取得动态属性值
	 */
	public String getPropValue(String propName) {
		if (userProps == null) {
			return null;
		}
		
		for (Iterator iter = userProps.iterator(); iter.hasNext(); ) {
			UserProp userProp = (UserProp) iter.next();
			if (StringUtils.equals(userProp.getId().getPropName(), propName)) {
				return userProp.getPropValue();
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @see net.silencily.sailing.basic.security.acegi.userdetails.coheg.security.acegi.userdetails.ExtensionalUserDetails#getTopRoleLevel()
	 */
	public int getTopRoleLevel() {
		int topRoleLevel = Integer.MAX_VALUE;
		
		for (Iterator iter = getRoles().iterator(); iter.hasNext(); ) {
			Role role = (Role) iter.next();
			if (role.getLevel() < topRoleLevel) {
				topRoleLevel = role.getLevel();
			}
		}
		
		return topRoleLevel;
	}
	
	
	/**
	 * @hibernate.id unsaved-value="null" generator-class="uuid.hex" length = "32"
	 * 
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @hibernate.property unique = "true" type = "com.coheg.framework.hibernate3.CStringType" length = "50"
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
	 * @hibernate.version unsaved-value="null"
	 */
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}


	/**
	 * @hibernate.property 
	 * @return Returns the accountNonExpired.
	 */
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}


	/**
	 * @param accountNonExpired The accountNonExpired to set.
	 */
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}


	/**
	 * @hibernate.property 
	 * @return Returns the accountNonLocked.
	 */
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}


	/**
	 * @param accountNonLocked The accountNonLocked to set.
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}


	/**
	 * @return Returns the authorities.
	 */
	public GrantedAuthority[] getAuthorities() {
		return authorities;
	}


	/**
	 * @param authorities The authorities to set.
	 */
	public void setAuthorities(GrantedAuthority[] authorities) {
		this.authorities = authorities;
	}


	/**
	 * @hibernate.property column="credentialsNonExpired"
	 * @return Returns the credentialsNonExpired.
	 */
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}


	/**
	 * @param credentialsNonExpired The credentialsNonExpired to set.
	 */
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}


	/**
	 * @hibernate.property 
	 * @return Returns the enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}


	/**
	 * @param enabled The enabled to set.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	/**
	 * @hibernate.property  column = "chinese_name" type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 * @return Returns the chineseName.
	 */
	public String getChineseName() {
		return chineseName;
	}


	/**
	 * @param chineseName The chineseName to set.
	 */
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	
	/**
	 * @hibernate.set lazy="true" table="security_user_role" cascade = "save-update" inverse = "false"
	 * @hibernate.key column="user_id"
	 * @hibernate.many-to-many column="role_id" class="com.coheg.security.entity.Role" 
	 */
	public java.util.Set getRoles() {
		if (this.roles == null) {
			this.roles = new LinkedHashSet();
		}		
		return roles;
	}

	public void setRoles(java.util.Set roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		if (!role.getUsers().contains(this)) {
			role.getUsers().add(this);			
		}
		if (!getRoles().contains(role)) {
			getRoles().add(role);			
		}
	}

	/**
	 * @see net.silencily.sailing.basic.security.acegi.userdetails.coheg.security.acegi.userdetails.ExtensionalUserDetails#getUserProps()
	 * @hibernate.set lazy = "true" table = "security_user_prop" cascade = "all" inverse = "false"
	 * @hibernate.key column = "user_id"
	 * @hibernate.one-to-many class = "com.coheg.security.entity.UserProp"
	 */
	@SuppressWarnings("rawtypes")
	public java.util.Set getUserProps() {
		return userProps;
	}

	public void setUserProps(java.util.Set userProps) {
		this.userProps = userProps;
	}
	
	public void addUserProps(UserProp userProp) {
		if (this.userProps == null) {
			this.userProps = new LinkedHashSet();
		}
		userProp.getId().setUser(this);
		this.userProps.add(userProp);
	}


	/**
	 * @hibernate.many-to-one column="organizationId"
	 * @return Returns the organization.
	 */
	public Organization getOrganization() {
		return organization;
	}


	/**
	 * @param organization The organization to set.
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}


	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}


	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}



	
}
