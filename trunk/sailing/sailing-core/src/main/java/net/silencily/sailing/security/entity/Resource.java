package net.silencily.sailing.security.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * <class>Resource</class> 代表系统中一个资源, 可能是 web 层的一个 url 或 service 层的一个 class
 * 
 * @since 2006-01-19
 * @author qian
 * @author 王政
 * @version $Id: Resource.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @hibernate.class table="security_resource"
 */
public class Resource implements Serializable {
	
	//	~ Static fields/initializers =============================================
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 271577293669339602L;
	
	// 以下定义资源层数
	
	public static final String WEB_LAYER = "web";

	public static final String DOMAIN_LAYER = "domain";
	
	// 以下定义 web 层资源类型, 只有 menu 才会显示在主菜单中
	
	public static final String MENU_TYPE = "menu";
	
	public static final String BUTTON_TYPE = "button";
	
	//	~ Instance fields ========================================================
	
	private String id;
	
	/** 标识是 web 层还是 service 层资源 */
	private String layer = WEB_LAYER;
	
	/** 资源类型,  可能是 菜单, 或 按钮, 仅对 web 层有效 */
	private String type = MENU_TYPE;
	
	/** web 层的一个 url  */
	private String url;
	
	/** web 层 url 需要显示在菜单中的标题 */
	private String title;
		
	/** 如果采用 acl, 为 acl class */
	private String aclClass;
	
	/** 如果采用 acl, 为 aclObjectIdentity */
	private String aclObjectIdentity;
	
	private int orderField;
	
	/** 是否系统资源, 系统资源不予许做任何写操作 */
	private boolean systemResource;
	
    /** 是否允许在 url 后加入其他参数 */
    private boolean allowAppendUrl;
	
	private Integer version;
	
	private Application application;

	private Resource parent;
	
	/** 仅用于级联删除 */
	private Set permissions = new HashSet();
	
	//	~ Methods ================================================================
	
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
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "200"
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String property1) {
		this.url = property1;
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
	 * @return Returns the aclClass.
	 */
	public String getAclClass() {
		return aclClass;
	}

	/**
	 * @param aclClass The aclClass to set.
	 */
	public void setAclClass(String aclClass) {
		this.aclClass = aclClass;
	}

	/**
	 * @hibernate.property  type = "com.coheg.framework.hibernate3.CStringType" length = 200"
	 * @return Returns the aclObjectIdentity.
	 */
	public String getAclObjectIdentity() {
		return aclObjectIdentity;
	}

	/**
	 * @param aclObjectIdentity The aclObjectIdentity to set.
	 */
	public void setAclObjectIdentity(String aclObjectIdentity) {
		this.aclObjectIdentity = aclObjectIdentity;
	}

	/**
	 * @hibernate.property  type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 * @return Returns the layer.
	 */
	public String getLayer() {
		return layer;
	}

	/**
	 * @param layer The layer to set.
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}

	/**
	 * @hibernate.property  type = "com.coheg.framework.hibernate3.CStringType" column = "resource_type" length = "100"
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @hibernate.property  type = "com.coheg.framework.hibernate3.CStringType" length = "200"
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @hibernate.property
	 * @return Returns the systemResource.
	 */
	public boolean isSystemResource() {
		return systemResource;
	}

	/**
	 * @param systemResource The systemResource to set.
	 */
	public void setSystemResource(boolean systemResource) {
		this.systemResource = systemResource;
	}

	/**
	 * @hibernate.property column = "allow_appendurl"
	 * @return Returns the allowAppendUrl.
	 */
	public boolean isAllowAppendUrl() {
		return allowAppendUrl;
	}

	/**
	 * @param allowAppendUrl The allowAppendUrl to set.
	 */
	public void setAllowAppendUrl(boolean allowAppendUrl) {
		this.allowAppendUrl = allowAppendUrl;
	}
	
	/**
	 * @hibernate.many-to-one column="parent_id" foreign-key = "false"
	 */
	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
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
	 * @hibernate.key column="resource_id"
	 * @hibernate.one-to-many class="com.coheg.security.entity.Permission"
	 * @return Returns the permissions.
	 */
	public Set getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions The permissions to set.
	 */
	public void setPermissions(Set permissions) {
		this.permissions = permissions;
	}
	
	
	
}
