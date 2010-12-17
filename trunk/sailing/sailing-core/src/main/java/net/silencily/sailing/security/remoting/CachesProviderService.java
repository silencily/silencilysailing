/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.remoting;

import java.io.Serializable;

import net.silencily.sailing.security.acegi.intercept.web.cache.PermissionCache;
import net.silencily.sailing.security.acegi.providers.dao.FlushableUserCache;

//tongjq import com.qware.security.app.organization.manager.OrganizationManager;

/**
 * �ṩȨ��ϵͳ����Ҫ�Ļ���
 * @since 2006-2-28
 * @author ����
 * @version $Id: CachesProviderService.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface CachesProviderService extends Serializable {
	
	FlushableUserCache getUserCache();
	
//	tongjq	ResourceCache getResourceCache();
	
	PermissionCache getPermissionCache();
	
//	tongjq	OrganizationManager getOrganizationManager();
	
	void removeUserFromCache(String username);
	
	void flushUserCache();
	
	void flushResourceCache();
	
	void flushPermissionCache();
	
	void flushOrganizationCache();
	
	/**
	 * ˢ�����л���
	 *
	 */
	void flushAllCaches();
	
	
	
}
