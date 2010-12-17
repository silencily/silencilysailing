/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.intercept.web.cache;

/**
 * @since 2006-2-13
 * @author ����
 * @version $Id: PermissionFilter.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public interface PermissionFilter {
	
	boolean accept(PermissionHolder permissionHolder);
	
}
