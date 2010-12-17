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

/**
 * @since 2006-2-13
 * @author ÍõÕþ
 * @version $Id: ResourceCache.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface ResourceCache extends FlushableCache, Serializable {
	
	
	/**
	 * 
	 * @param resourceFilter
	 * @return list fill with {@link ResourceHolder}
	 */
	List getResourcesFromCache(ResourceFilter resourceFilter);
	
}
