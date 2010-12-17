/*
 * Copyright 2004-2005 wangz.
 * Project shufe_newsroom
 */
package net.silencily.sailing.security.acegi.intercept.web;


/**
 * ���Ա�ʾϵͳ�����е���ԴȨ�޶���, ����ÿһ�� {@link net.silencily.sailing.security.acegi.intercept.web.coheg.security.acegi.intercept.web.ResourceMapping} ����һ����Դ��������Ȩ
 * @since 2005-8-4
 * @author ����
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



