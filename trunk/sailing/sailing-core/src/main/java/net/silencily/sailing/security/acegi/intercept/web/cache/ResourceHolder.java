/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.intercept.web.cache;

import java.io.Serializable;

/**
 * @since 2006-2-13
 * @author 王政
 * @version $Id: ResourceHolder.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class ResourceHolder implements Serializable {
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 4398024442270329501L;

	/**
	 * These fields used by both {@link net.silencily.sailing.security.acegi.intercept.web.coheg.security.acegi.intercept.web.ResourceMappingProviderImpl}
	 * and {@link com.coheg.security.acegi.strutsmenu.MenuItemProviderImpl}
	 */
	
    private String resourceId;
    
    private String resourceName;
    
    private String parentName;
    
    private String resourcePath;
    
	/**
	 * These fields only used by {@link net.silencily.sailing.security.acegi.intercept.web.coheg.security.acegi.intercept.web.ResourceMappingProviderImpl}
	 */
    
    private boolean expression;
    
	/**
	 * These fields only used by {@link com.coheg.security.acegi.strutsmenu.MenuItemProviderImpl}
	 */
    
    private String applicationId;
    
    private String applicationServer;
    
    private String applicationContextPath;
    
    private String layer;
    
    private String type;
    
    private String title;
       
    // 是否允许在 url 后加入其他参数
    private boolean allowAppendUrl;
    
    /**
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

	public ResourceHolder() {            
    }



    /**
     * @return Returns the parentName.
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * @param parentName The parentName to set.
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * @return Returns the resourceId.
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * @param resourceId The resourceId to set.
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * @return Returns the resourceName.
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * @param resourceName The resourceName to set.
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * @return Returns the resourcePath.
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * @param resourcePath The resourcePath to set.
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

	/**
	 * @return Returns the expression.
	 */
	public boolean isExpression() {
		return expression;
	}

	/**
	 * @param expression The expression to set.
	 */
	public void setExpression(boolean expression) {
		this.expression = expression;
	}

	/**
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
	 * @return Returns the applicationId.
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId The applicationId to set.
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
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
	 * @return Returns the applicationContextPath.
	 */
	public String getApplicationContextPath() {
		return applicationContextPath;
	}

	/**
	 * @param applicationContextPath The applicationContextPath to set.
	 */
	public void setApplicationContextPath(String applicationContextPath) {
		this.applicationContextPath = applicationContextPath;
	}

	/**
	 * @return Returns the applicationServer.
	 */
	public String getApplicationServer() {
		return applicationServer;
	}

	/**
	 * @param applicationServer The applicationServer to set.
	 */
	public void setApplicationServer(String applicationServer) {
		this.applicationServer = applicationServer;
	}
	
}
