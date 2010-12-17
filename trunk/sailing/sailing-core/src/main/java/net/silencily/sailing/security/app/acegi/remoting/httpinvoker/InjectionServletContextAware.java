/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.app.acegi.remoting.httpinvoker;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @since 2006-7-31
 * @author java2enterprise
 * @version $Id: InjectionServletContextAware.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public class InjectionServletContextAware implements ServletContextAware, InitializingBean {

	private String scheme;
	
	private Integer serverPort;

	/**
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	/**
	 * @return the serverPort
	 */
	public Integer getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(scheme, " scheme required. ");	
		Assert.notNull(serverPort, " serverPort required. ");	
	}
	
}
