package net.silencily.sailing.security.entity;

import java.io.Serializable;

/**
 * 
 * @author qian
 * @author ÍõÕþ
 * @hibernate.class table="security_application"
 */
public class Application implements Serializable {
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -798393246286859984L;

	private String id;

	private String name;
	
	private int status;
	
	private String server;
	
	private String contextPath;
	
	private String description;

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
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 */
	public String getName() {
		return name;
	}

	public void setName(String property1) {
		this.name = property1;
	}
	/**
	 * @hibernate.property  type = "com.coheg.framework.hibernate3.CStringType"  length = "100"
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String property1) {
		this.description = property1;
	}
	/**
	 * @hibernate.property
	 */
	public int getStatus() {
		return status;
	}

	public void setStatus(int property1) {
		this.status = property1;
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
	 * @hibernate.property column = "context_path" type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 * @return Returns the contextPath.
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * @param contextPath The contextPath to set.
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * @hibernate.property type = "com.coheg.framework.hibernate3.CStringType" length = "100"
	 * @return Returns the server.
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param server The server to set.
	 */
	public void setServer(String serverPath) {
		this.server = serverPath;
	}
}
