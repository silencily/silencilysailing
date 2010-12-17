/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.security.acegi.providers.dao;

import net.silencily.sailing.security.FlushableCache;

import org.acegisecurity.providers.dao.UserCache;


/**
 * @since 2005-11-4
 * @author ����
 * @version $Id: FlushableUserCache.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public interface FlushableUserCache extends UserCache, FlushableCache {

}
