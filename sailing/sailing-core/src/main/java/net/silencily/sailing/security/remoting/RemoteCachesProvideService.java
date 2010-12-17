/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.remoting;

import java.io.Serializable;


/**
 * @since 2006-6-9
 * @author ÍõÕþ
 * @version $Id: RemoteCachesProvideService.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface RemoteCachesProvideService extends Serializable {
	
	void removeUserFromCache(String username);
	
	void flushUserCache();
	
	void flushResourceCache();
	
	void flushPermissionCache();
	
	void flushOrganizationCache();
	
	void flushAllCaches();
	
}
