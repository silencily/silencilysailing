/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.remoting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.silencily.sailing.security.app.acegi.remoting.httpinvoker.MultiApplicationHttpInvokerProxyFactoryBean;
import net.silencily.sailing.security.app.acegi.remoting.httpinvoker.MultiApplicationHttpInvokerProxyFactoryBean.ApplicationNameCachesProviderServiceMapAware;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;




/**
 * @see MultiApplicationHttpInvokerProxyFactoryBean
 * @since 2006-6-9
 * @author 王政
 * @version $Id: RemoteCachesProviderSerivceImpl.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class RemoteCachesProviderSerivceImpl implements RemoteCachesProvideService, InitializingBean {
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -592460491273684940L;

	private static transient Log logger = LogFactory.getLog(RemoteCachesProviderSerivceImpl.class);
	
	private List remoteCachesProviderServices;
	
	private ApplicationNameCachesProviderServiceMapAware applicationNameCachesProviderServiceMapAware;
	
	
	/**
	 * @param applicationNameCachesProviderServiceMapAware the applicationNameCachesProviderServiceMapAware to set
	 */
	public void setApplicationNameCachesProviderServiceMapAware(ApplicationNameCachesProviderServiceMapAware applicationNameCachesProviderServiceMapAware) {
		this.applicationNameCachesProviderServiceMapAware = applicationNameCachesProviderServiceMapAware;
	}

	public void removeUserFromCache(String username) {
		for (int i = 0; i < remoteCachesProviderServices.size(); i++) {
			try {
				CachesProviderService service = (CachesProviderService) remoteCachesProviderServices.get(i);
				service.removeUserFromCache(username);	
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error(" WebService 请求出错, 请确认客户端应用已正确配置 : " + e);
				}
			}
		}
	}
	
	public void flushUserCache() {
		for (int i = 0; i < remoteCachesProviderServices.size(); i++) {
			try {
				CachesProviderService service = (CachesProviderService) remoteCachesProviderServices.get(i);
				service.getUserCache().flushCache();
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error(" WebService 请求出错, 请确认客户端应用已正确配置 : " + e.getCause());
				}
			}
		}
	}

	public void flushResourceCache() {
		for (int i = 0; i < remoteCachesProviderServices.size(); i++) {			
			try {
				CachesProviderService service = (CachesProviderService) remoteCachesProviderServices.get(i);
				service.flushResourceCache();
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error(" WebService 请求出错, 请确认客户端应用已正确配置 : " + e.getCause());
				}
			}
		}
	}

	public void flushPermissionCache() {
		for (int i = 0; i < remoteCachesProviderServices.size(); i++) {			
			try {
				CachesProviderService service = (CachesProviderService) remoteCachesProviderServices.get(i);
				service.flushPermissionCache();
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error(" WebService 请求出错, 请确认客户端应用已正确配置 : " + e.getCause());
				}
			}
		}
	}
	
	public void flushOrganizationCache() {
		for (int i = 0; i < remoteCachesProviderServices.size(); i++) {			
			try {
				CachesProviderService service = (CachesProviderService) remoteCachesProviderServices.get(i);
				service.flushOrganizationCache();
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error(" WebService 请求出错, 请确认客户端应用已正确配置 : " + e.getCause());
				}
			}
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(applicationNameCachesProviderServiceMapAware, " applicationNameCachesProviderServiceMapAware required. ");
		Map applicationNameCachesProviderServiceMap = applicationNameCachesProviderServiceMapAware.getApplicationNameCachesProviderServiceMap();
		Assert.notNull(applicationNameCachesProviderServiceMap);
		remoteCachesProviderServices = new ArrayList(applicationNameCachesProviderServiceMap.size());
		for (Iterator iter = applicationNameCachesProviderServiceMap.entrySet().iterator(); iter.hasNext(); ) {
			Map.Entry entry = (Entry) iter.next();
			Assert.isInstanceOf(String.class, entry.getKey());
			Assert.isInstanceOf(CachesProviderService.class, entry.getValue());
			remoteCachesProviderServices.add(entry.getValue());
		}
	}

	public void flushAllCaches() {
		flushUserCache();
		flushResourceCache();
		flushPermissionCache();
		flushOrganizationCache();
	}	


}
