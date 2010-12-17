/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.intercept.web.cache;

import java.io.Serializable;
import java.util.List;

import net.silencily.sailing.security.FlushableCache;

//tongjq import com.coheg.framework.spring.ehcache.FlushableCache;
/**
 * @since 2006-2-13
 * @author ÍõÕþ
 * @version $Id: PermissionCache.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public interface PermissionCache extends FlushableCache, Serializable {
	
	/**
	 * 
	 * @param permissionFilter
	 * @return list fill with {@link PermissionHolder}
	 */
	List getPermissionsFromCache(PermissionFilter permissionFilter);
	
}
