/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.remoting;

import net.silencily.sailing.security.acegi.intercept.web.cache.PermissionCache;
import net.silencily.sailing.security.acegi.providers.dao.FlushableUserCache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @since 2006-2-28
 * @author ÍõÕþ
 * @version $Id: CachesProviderServiceImpl.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class CachesProviderServiceImpl implements CachesProviderService, InitializingBean {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 2556641861699332297L;

	private FlushableUserCache userCache;
	
//tongjq	private ResourceCache resourceCache;
	
	private PermissionCache permissionCache;
	
//	tongjq	private OrganizationManager organizationManager;
	

	/**
	 * @return Returns the permissionCache.
	 */
	public PermissionCache getPermissionCache() {
		return permissionCache;
	}


	/**
	 * @param permissionCache The permissionCache to set.
	 */
	public void setPermissionCache(PermissionCache permissionCache) {
		this.permissionCache = permissionCache;
	}


	/**
	 * @return Returns the resourceCache.
	 */
//	tongjq	public ResourceCache getResourceCache() {
//	tongjq		return resourceCache;
//	tongjq	}


	/**
	 * @param resourceCache The resourceCache to set.
	 */
//	tongjq	public void setResourceCache(ResourceCache resourceCache) {
//	tongjq		this.resourceCache = resourceCache;
//	tongjq	}


	/**
	 * @return Returns the userCache.
	 */
	public FlushableUserCache getUserCache() {
		return userCache;
	}


	/**
	 * @param userCache The userCache to set.
	 */
	public void setUserCache(FlushableUserCache userCache) {
		this.userCache = userCache;
	}
	
	/**
	 * @return Returns the organizationManager.
	 */
//	tongjq	public OrganizationManager getOrganizationManager() {
//	tongjq		return organizationManager;
//	tongjq	}


	/**
	 * @param organizationManager The organizationManager to set.
	 */
//	tongjq	public void setOrganizationManager(OrganizationManager organizationManager) {
//	tongjq		this.organizationManager = organizationManager;
//	tongjq	}


	public void removeUserFromCache(String username) {
		userCache.removeUserFromCache(username);
	}


	public void flushResourceCache() {
//tongjq		resourceCache.flushCache();
	}


	public void flushPermissionCache() {
		permissionCache.flushCache();
	}
	
	public void flushOrganizationCache() {
//	tongjq		organizationManager.flushCache();
	}
	
	public void flushAllCaches() {
		userCache.flushCache();
//tongjq		resourceCache.flushCache();
		permissionCache.flushCache();
//tongjq		organizationManager.flushCache();
	}
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(userCache, " userCache is required. ");
		Assert.notNull(userCache, " resourceCache is required. ");
		Assert.notNull(userCache, " permissionCache is required. ");
//tongjq		Assert.notNull(organizationManager, " organizationManager is required. ");
	}


	public void flushUserCache() {
		userCache.flushCache();
	}





}
