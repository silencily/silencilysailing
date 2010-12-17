/*
 * Copyright 2004-2005 wangz.
 * Project shufe_newsroom
 */
package net.silencily.sailing.security.acegi.intercept.web;

import java.util.LinkedList;
import java.util.List;

import net.silencily.sailing.security.acegi.intercept.web.cache.PermissionCache;
import net.silencily.sailing.security.remoting.CachesProviderService;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * @since 2005-8-4
 * @author 王政
 * @version $Id: ResourceMappingProviderImpl.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class ResourceMappingProviderImpl implements ResourceMappingProvider, InitializingBean {
    
    
    //~ Instance fields ========================================================
    
    private CachesProviderService cachesProviderService;
    
     
    //~ Methods ================================================================
    
    /**
     * @see ResourceMappingProvider#getResourceMappings()
     */
    public ResourceMapping[] getResourceMappings() {
		List resourceMappings = new LinkedList();
        /* tongjq
		List allResource = getResourceCache().getResourcesFromCache(new ResourceFilter() {
			public boolean accept(ResourceHolder resourceHolder) {
				return StringUtils.isNotBlank(resourceHolder.getResourcePath());
			}
        });
        
        List recipentsResourceMapping = getPermissionCache().getPermissionsFromCache(new PermissionFilter() {
			public boolean accept(PermissionHolder permissionHolder) {
				return true;
			}
        });
        
        Map resourceRecipentsMap = new LinkedHashMap();
        
        for (Iterator iter = recipentsResourceMapping.iterator(); iter.hasNext(); ) {
        	PermissionHolder mappingHolder = (PermissionHolder) iter.next();
        	List recipents = (List) resourceRecipentsMap.get(mappingHolder.getResourceId());
        	if (recipents == null) {
        		recipents = new LinkedList();
        	}
        	if (! recipents.contains(mappingHolder.getRecipent())) {
        		recipents.add(mappingHolder.getRecipent());
        	}
        	resourceRecipentsMap.put(mappingHolder.getResourceId(), recipents);
        }
        
        for (Iterator iter = allResource.iterator(); iter.hasNext(); ) {          
            ResourceHolder holder = (ResourceHolder) iter.next();            
            String resourcePath = holder.getResourcePath();
            boolean expression = holder.isExpression();            
            
            if (StringUtils.isBlank(resourcePath)) {
                continue;
            }
            
            // 如果是模糊匹配, 加上 "**" 标志
            //FIXME
            resourcePath = expression ? resourcePath + "*" : resourcePath;
            
            // 如果 url 前没有 "/", 自动加上
            if (resourcePath.indexOf(RESOURCE_PATH_PREFIX) != 0) {
            	resourcePath = RESOURCE_PATH_PREFIX + resourcePath;
            }
                
            ResourceMapping mapping = new ResourceMapping();                       
            mapping.setResourcePath(resourcePath);
            
            String resourceId = holder.getResourceId();
            List recipentsByResource = (List) resourceRecipentsMap.get(resourceId);
            if (recipentsByResource == null) {
            	recipentsByResource = new LinkedList();
            }
            
            String[] recipents = (String[]) recipentsByResource.toArray(new String[0]);
            mapping.setRecipients(recipents);
            resourceMappings.add(mapping);
        }
              
        if (allResource.isEmpty()) {
            return new ResourceMapping[] {new ResourceMapping("/dontuseme", new String[] {"dontuseme"})};
        }
        tongjq */
        return (ResourceMapping[]) resourceMappings.toArray(new ResourceMapping[0]);
    }

	/**
	 * @return Returns the permissionCache.
	 */
	public PermissionCache getPermissionCache() {
		return cachesProviderService.getPermissionCache();
	}


	/**
	 * @return Returns the resourceCache.
	 */
//tongjq	public ResourceCache getResourceCache() {
//tongjq		return cachesProviderService.getResourceCache();
//tongjq	}


	public void afterPropertiesSet() throws Exception {
		Assert.notNull(cachesProviderService, " cachesProviderService is required. ");
	}

	/**
	 * @param cachesProviderService The cachesProviderService to set.
	 */
	public void setCachesProviderService(CachesProviderService cachesProviderService) {
		this.cachesProviderService = cachesProviderService;
	}
}



