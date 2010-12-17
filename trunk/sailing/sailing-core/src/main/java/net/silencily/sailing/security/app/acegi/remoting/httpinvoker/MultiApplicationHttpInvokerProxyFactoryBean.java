/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.app.acegi.remoting.httpinvoker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.silencily.sailing.security.app.appliction.dao.MultiApplicationHttpInvokerDao;
import net.silencily.sailing.security.entity.Application;
import net.silencily.sailing.security.remoting.CachesProviderService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.httpinvoker.HttpInvokerClientInterceptor;
import org.springframework.util.Assert;

//import com.coheg.security.app.application.dao.ApplicationDao;

//import com.coheg.security.remoting.RemoteCachesProviderSerivceImpl;

/**
 * 根据应用配置动态构造 webservice 客户端的 factory bean, 返回类型是 {@link ApplicationNameCachesProviderServiceMapAware}
 * @see RemoteCachesProviderSerivceImpl
 * @see ApplicationDao
 * @since 2006-7-31
 * @author java2enterprise
 * @version $Id: MultiApplicationHttpInvokerProxyFactoryBean.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public class MultiApplicationHttpInvokerProxyFactoryBean implements FactoryBean, InitializingBean {
	
	private static transient Log logger = LogFactory.getLog(MultiApplicationHttpInvokerProxyFactoryBean.class);
	
	private ServletContextAware servletContextAware;
	
	private MultiApplicationHttpInvokerDao multiApplicationHttpInvokerDao;
	
	private String authenticationServerContext;
	
	private String remotingCachesProviderServiceUrl;
	
	private ApplicationNameCachesProviderServiceMapAware aware;
	
	/**
	 * @param multiApplicationHttpInvokerDao the multiApplicationHttpInvokerDao to set
	 */
	public void setMultiApplicationHttpInvokerDao(MultiApplicationHttpInvokerDao multiApplicationHttpInvokerDao) {
		this.multiApplicationHttpInvokerDao = multiApplicationHttpInvokerDao;
	}

	/**
	 * @param authenticationServerContext the authenticationServerContext to set
	 */
	public void setAuthenticationServerContext(String authenticationServerContext) {
		this.authenticationServerContext = authenticationServerContext;
	}

	/**
	 * @param servletContextAware the servletContextAware to set
	 */
	public void setServletContextAware(ServletContextAware servletContextAware) {
		this.servletContextAware = servletContextAware;
	}
	
	/**
	 * @param remotingCachesProviderServiceUrl the remotingCachesProviderServiceUrl to set
	 */
	public void setRemotingCachesProviderServiceUrl(String remotingCachesProviderServiceUrl) {
		this.remotingCachesProviderServiceUrl = remotingCachesProviderServiceUrl;
	}
	
	/**
	 * the applicationNameCachesProviderServiceMap, key : applicationName, value : concreate class of {@link CachesProviderService}
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return aware;
	}

	public Class getObjectType() {
		return ApplicationNameCachesProviderServiceMapAware.class;
	}

	public boolean isSingleton() {
		return true;
	}

	private void init() {
		List list = multiApplicationHttpInvokerDao.findAll();
		Map applicationNameCachesProviderServiceMap = new HashMap();
		
		for (Iterator iter = list.iterator(); iter.hasNext(); ) {
			Application application = (Application) iter.next();
			String contextPath = application.getContextPath();
			
			// 不加入 security 本身, 同时过滤掉重名的 contextPath
			if (StringUtils.equals(contextPath, authenticationServerContext)
				|| applicationNameCachesProviderServiceMap.containsKey(contextPath)) {
				continue;
			}
			
			StringBuffer serviceUrl = new StringBuffer();
			serviceUrl.append(servletContextAware.getScheme());
			serviceUrl.append("://localhost:");
			serviceUrl.append(servletContextAware.getServerPort());
			serviceUrl.append(contextPath);
			serviceUrl.append(remotingCachesProviderServiceUrl);
			
			HttpInvokerClientInterceptor interceptor = new HttpInvokerClientInterceptor();
			interceptor.setServiceUrl(serviceUrl.toString());
			interceptor.setServiceInterface(CachesProviderService.class);			
			Object cachesProviderService = ProxyFactory.getProxy(CachesProviderService.class, interceptor);
			applicationNameCachesProviderServiceMap.put(contextPath, cachesProviderService);
		}
		
		aware = new ApplicationNameCachesProviderServiceMapAware(applicationNameCachesProviderServiceMap);
		if (logger.isInfoEnabled()) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(" 初始化 remote caches provider service 完成 : ");
			for (Iterator iter = applicationNameCachesProviderServiceMap.entrySet().iterator(); iter.hasNext(); ) {
				Map.Entry entry = (Entry) iter.next();
				buffer.append(" AppName [");
				buffer.append(entry.getKey());
				buffer.append("] service [");
				buffer.append(entry.getValue());
				buffer.append("] ");
			}
			logger.info(buffer.toString());			
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(servletContextAware, " servletContextAware required. ");
		Assert.notNull(multiApplicationHttpInvokerDao, " multiApplicationHttpInvokerDao required. ");
		Assert.notNull(authenticationServerContext, " authenticationServerContext required. ");		
		Assert.notNull(remotingCachesProviderServiceUrl, " remotingCachesProviderServiceUrl required. ");	
		init();
	}
	
	/**
	 * 再封装一层的目的是防止 webwork 自动注入 map 类型
	 *
	 */
	public static class ApplicationNameCachesProviderServiceMapAware {
		
		private Map applicationNameCachesProviderServiceMap;
		
		public ApplicationNameCachesProviderServiceMapAware(Map applicationNameCachesProviderServiceMap) {
			this.applicationNameCachesProviderServiceMap = applicationNameCachesProviderServiceMap;
		}

		/**
		 * @return the applicationNameCachesProviderServiceMap
		 */
		public Map getApplicationNameCachesProviderServiceMap() {
			return applicationNameCachesProviderServiceMap;
		}

	}
	
}
