package net.silencily.sailing.security.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @since 2006-01-19
 * @author 王政
 * @version $Id: Role.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @hibernate.class table="security_role"
 *
 */
public class Role implements Serializable {
	
	//	~ Static fields/initializers =============================================
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -335077729668310784L;
	
	/** 定义分级授权级别, 数字越小说明级别越高, 目前支持 5 级 */
	public static final int[] LEVEL_ARRAY = {1, 2, 3, 4, 5};	
	
	private static final Map ROLE_LEVEL_MAP = new LinkedHashMap() {
		private static final long serialVersionUID = -8978289341498775797L;
		{
			put(new Integer(LEVEL_ARRAY[0]), " 第一级 ");
			put(new Integer(LEVEL_ARRAY[1]), " 第二级 ");
			put(new Integer(LEVEL_ARRAY[2]), " 第三级 ");
			put(new Integer(LEVEL_ARRAY[3]), " 第四级 ");
			put(new Integer(LEVEL_ARRAY[4]), " 第五级 ");
		}
	};
	
	//	 ~ Instance fields ========================================================
	
	private String id;
	
	private String name;
	
	/** 是否系统角色, 系统角色不能被删除 */
	private boolean systemRole = true;
	
	/**
	 * 用户分级授权，即用户机构的级别
	 */
	private int level;

	private String description;
	
	private Integer version;
	
	private Application application;
	
	private Set users = new HashSet();
	
	/**
	 * @link aggregation
	 * @associates com.coheg.security.entity.Permission
	 * @directed directed
	 * @supplierCardinality 0..*
	 */
	private Set permissions = new HashSet();
	
	
	//	~ Methods ================================================================
	
	/**
	 * 得到角色分级授权 Map
	 */
	public static Map getRoleLevelMap() {
		return ROLE_LEVEL_MAP;
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
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String property1) {
		this.description = property1;
	}
	
	/**
	 * @hibernate.property column = "role_level"
	 */
	public int getLevel() {
		return level;
	}

	public void setLevel(int property1) {
		this.level = property1;
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
	 * @return Returns the systemRole.
	 */
	public boolean isSystemRole() {
		return systemRole;
	}

	/**
	 * @param systemRole The systemRole to set.
	 */
	public void setSystemRole(boolean systemRole) {
		this.systemRole = systemRole;
	}
	
	/**
	 * @hibernate.many-to-one column="application_id"
	 */
	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
	
	
	/**
	 * @hibernate.set lazy="true" table="security_permission" cascade = "all" order-by = "id" inverse = "false"
	 * @hibernate.key column="role_id"
	 * @hibernate.one-to-many class="com.coheg.security.entity.Permission"
	 */
	public java.util.Set getPermissions() {
		return permissions;
	}

	public void setPermissions(java.util.Set permissions) {
		this.permissions = permissions;
	}
	
	public void addPermission(Permission permission) {
		if (this.permissions == null) {
			this.permissions = new LinkedHashSet();
		}
		permission.setRole(this);
		this.permissions.add(permission);
	}

	/**
	 * @hibernate.set lazy="true" table="security_user_role" cascade = "none" inverse = "false"
	 * @hibernate.key column="role_id"
	 * @hibernate.many-to-many column="user_id" class="com.coheg.security.entity.User" 
	 * @return Returns the users.
	 */
	public Set getUsers() {
		if (users == null) {
			users = new HashSet();
		}
		return users;
	}

	/**
	 * @param users The users to set.
	 */
	public void setUsers(Set users) {
		this.users = users;
	}
	

}
