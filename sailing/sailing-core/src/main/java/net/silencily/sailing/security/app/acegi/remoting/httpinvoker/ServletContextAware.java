/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.app.acegi.remoting.httpinvoker;

/**
 * @since 2006-7-31
 * @author java2enterprise
 * @version $Id: ServletContextAware.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 */
public interface ServletContextAware {
	
	String getScheme();
	
	Integer getServerPort();
	
}
