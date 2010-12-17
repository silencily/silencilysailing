package net.silencily.sailing.security.entity;

import java.io.Serializable;

/**
 * @since 2006-01-19
 * @author qian
 * @author 王政
 * @version $Id: Permission.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @hibernate.class table="security_permission"
 */
public class Permission implements Serializable {
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -3627446237251370568L;

	private String id;
	/**
	 * 允许对资源进行的操作，例如：修改、删除、查询等
	 */
	private int aclMask;

	private Integer version;
	
	private Resource resource;
	
	private Role role;
	
	public Permission() {
		
	}
	/**
	 * @hibernate.id unsaved-value="null" generator-class="uuid.hex" length = "32"
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @hibernate.many-to-one column="resource_id" class = "com.coheg.security.entity.Resource"
	 */
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	/**
	 * @hibernate.property
	 */
	public int getAclMask() {
		return aclMask;
	}

	public void setAclMask(int property1) {
		this.aclMask = property1;
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
	 * @hibernate.many-to-one column="role_id" class = "com.coheg.security.entity.Role"
	 * @return Returns the role.
	 */
	public Role getRole() {
		return role;
	}
	
	/**
	 * @param role The role to set.
	 */
	public void setRole(Role role) {
		this.role = role;
	}
}
