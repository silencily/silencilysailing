/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.providers.dao;
import net.silencily.sailing.security.remoting.CachesProviderService;

import org.acegisecurity.providers.dao.DaoAuthenticationProvider;
import org.springframework.util.Assert;


/**
 * @since 2006-2-28
 * @author ÍõÕþ
 * @version $Id: ReDefineUserCacheDaoAuthenticationProvider.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public class ReDefineUserCacheDaoAuthenticationProvider extends DaoAuthenticationProvider {

	private CachesProviderService cachesProviderService;

	/**
	 * @param cachesProviderService The cachesProviderService to set.
	 */
	public void setCachesProviderService(CachesProviderService cachesProviderService) {
		this.cachesProviderService = cachesProviderService;
		super.setUserCache(cachesProviderService.getUserCache());
	}

	/**
	 * @see org.acegisecurity.providers.dao.DaoAuthenticationProvider#doAfterPropertiesSet()
	 */
	protected void doAfterPropertiesSet() throws Exception {
		super.doAfterPropertiesSet();
        Assert.notNull(cachesProviderService, " cachesProviderService is required. ");
	}	
	
	
}
