package net.silencily.sailing.basic.security.acegi.userdetails;

import java.io.Serializable;

import net.silencily.sailing.security.model.CurrentUser;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;



public class ExtensionalUserDetails implements Serializable, UserDetails {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 4154414006735196812L;
	
	
	// ~ Instance fields ========================================================
	
	// for acegi 
	
	/** 用户标示 */
//	private String id;
	
	private String username;
	
	private String password;

	private GrantedAuthority[] authorities;

	private boolean accountNonExpired = true;

	private boolean accountNonLocked = true;

	private boolean credentialsNonExpired = true;

	private boolean enabled = true;
	
	// for project's custom required
//	private DefaultCurrentUser defaultCurrentUser = null;
    private CurrentUser currentUser = null;

	
	// ~ Constructors
	// ===========================================================
	
	/**
	 * Default constructor, only for hibernate use
	 */
	public ExtensionalUserDetails() {
		// throw new IllegalArgumentException("Cannot use default constructor");
	}
	
	
	// ~ Methods
	public String getUsername() {
		return username;
	}

	public void setUsername(String property1) {
		this.username = property1;
	}
	
	/**
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String property1) {
		this.password = property1;
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
	 * 功能描述
	 * @return
	 * 2007-11-27 上午11:16:14
	 * @version 1.0
	 * @author tongjq
	 */
	public CurrentUser getCurrentUser() {
		return currentUser;
	}


	/**
	 * 功能描述
	 * @param defaultCurrentUser
	 * 2007-11-27 上午11:19:32
	 * @version 1.0
	 * @author tongjq
	 */
	public void setCurrentUser(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}
	
}
