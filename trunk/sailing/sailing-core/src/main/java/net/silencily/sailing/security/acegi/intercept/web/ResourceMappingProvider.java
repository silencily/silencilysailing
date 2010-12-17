/*
 * Copyright 2004-2005 wangz.
 * Project shufe_newsroom
 */
package net.silencily.sailing.security.acegi.intercept.web;


/**
 * 用以表示系统中所有的资源权限定义, 其中每一个 {@link net.silencily.sailing.security.acegi.intercept.web.coheg.security.acegi.intercept.web.ResourceMapping} 代表一个资源的所有授权
 * @since 2005-8-4
 * @author 王政
 * @version $Id: ResourceMappingProvider.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public interface ResourceMappingProvider {
    
	String RESOURCE_PATH_PREFIX = "/";
	
    /**
     * Get Resource Mapping
     * @return resource mapping
     */
    ResourceMapping[] getResourceMappings();
    
}



