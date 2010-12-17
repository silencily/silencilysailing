/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project security
 */
package net.silencily.sailing.security.acegi.intercept.web;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.silencily.sailing.security.SecurityContextInfo;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.ConfigAttributeEditor;
import org.acegisecurity.intercept.web.AbstractFilterInvocationDefinitionSource;
import org.acegisecurity.intercept.web.FilterInvocationDefinitionMap;
import org.acegisecurity.intercept.web.FilterInvocationDefinitionSource;
import org.acegisecurity.intercept.web.PathBasedFilterInvocationDefinitionMap;
import org.acegisecurity.intercept.web.RegExpBasedFilterInvocationDefinitionMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;


/**
 * <class>RdbmsBasedFilterInvocationDefinitionSource</class> 是基于数据库的权限存储实现, 它支持两种风格的配置
 * 注意, 由于 hack 了 {@link org.acegisecurity.intercept.web.PathBasedFilterInvocationDefinitionMap#lookupAttributes(java.lang.String)} 的方法,
 * 目前已知的适用范围是 acegi 1.0-RC2 和 acegi 1.0.0 final !!!
 * @see net.silencily.sailing.security.acegi.intercept.web.coheg.security.acegi.intercept.web.ConfigableFilterInvocationDefinition
 * @see org.acegisecurity.intercept.web.RegExpBasedFilterInvocationDefinitionMap
 * @see org.acegisecurity.intercept.web.PathBasedFilterInvocationDefinitionMap
 * @since 2006-1-19
 * @author 王政
 * @version $Id: RdbmsBasedFilterInvocationDefinitionSource.java,v 1.1 2010/12/10 10:54:24 silencily Exp $
 */
public class RdbmsBasedFilterInvocationDefinitionSource extends AbstractFilterInvocationDefinitionSource 
	implements ConfigableFilterInvocationDefinition, InitializingBean {
	
	
    //~ Static fields/initializers =============================================

    private static final Log logger = LogFactory.getLog(RdbmsBasedFilterInvocationDefinitionSource.class);
	
	//	~ Instance fields ========================================================
	
    private String resourceExpression = PERL5_KEY;
    
    private boolean convertUrlToLowercaseBeforeComparison = false;
	    
    private ResourceMappingProvider resourceMappingProvider;
    
    //  ~ Methods ================================================================
    
	/**
	 * 
	 * @see org.acegisecurity.intercept.web.AbstractFilterInvocationDefinitionSource#lookupAttributes(java.lang.String)
	 */
	public ConfigAttributeDefinition lookupAttributes(String url) {
//		FilterInvocationDefinitionSource actualSource = populateFilterInvocationDefinitionSource();

		String cleanURL = SecurityContextInfo.getCurrentPageUrl();
		System.out.println("权限Check用URL:" + cleanURL);
/* tongjq
		if (RegExpBasedFilterInvocationDefinitionMap.class.isInstance(actualSource)) {
			System.out.println("CLASS -- " + RdbmsBasedFilterInvocationDefinitionSource.class + " End (68)");
			return ((RegExpBasedFilterInvocationDefinitionMap) actualSource).lookupAttributes(cleanURL);
		} else if (PathBasedFilterInvocationDefinitionMap.class.isInstance(actualSource)) {
			//	FIX Acegi 1.0.0 final look up bug 
//			return ((PathBasedFilterInvocationDefinitionMap) actualSource).lookupAttributes(url);

			System.out.println("CLASS -- " + RdbmsBasedFilterInvocationDefinitionSource.class + " End (73)");
			return innerLookupAttributes((PathBasedFilterInvocationDefinitionMap) actualSource, cleanURL);
		}
tongjq*/
        ConfigAttributeEditor configAttribEd = new ConfigAttributeEditor();
        configAttribEd.setAsText(cleanURL);

        return (ConfigAttributeDefinition) configAttribEd.getValue();
		
//		throw new IllegalStateException("wrong type of " + actualSource + ", it should be " + RegExpBasedFilterInvocationDefinitionMap.class 
//				+ " or " + PathBasedFilterInvocationDefinitionMap.class);		
	}
		

	private ConfigAttributeDefinition innerLookupAttributes(PathBasedFilterInvocationDefinitionMap map, String url) {
		if (convertUrlToLowercaseBeforeComparison) {
			url = url.toLowerCase();
			if (logger.isDebugEnabled()) {
				logger.debug("Converted URL to lowercase, from: '" + url + "'; to: '" + url + "'");
			}
		}
		
		try {
			Field requestMapField = map.getClass().getDeclaredField("requestMap");
			Field pathMatcherField = map.getClass().getDeclaredField("pathMatcher");
			
			requestMapField.setAccessible(true);
			pathMatcherField.setAccessible(true);
			
			List requestMap = (List) requestMapField.get(map);
			PathMatcher pathMatcher = (PathMatcher) pathMatcherField.get(map);
			
			return HackPathBasedFilterInvocationDefinitionMap.innerLookupAttributes(requestMap, pathMatcher, url);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Error during innerLookupAttributes :" + e);
			}
		}
		
		return null;
	}	
	
	
	private static class HackPathBasedFilterInvocationDefinitionMap extends PathBasedFilterInvocationDefinitionMap {
		
		public static ConfigAttributeDefinition innerLookupAttributes(List requestMap, PathMatcher pathMatcher, String url) {
			Iterator iter = requestMap.iterator();
			
		    while (iter.hasNext()) {
		        EntryHolder entryHolder = (EntryHolder) iter.next();
		       
		        boolean matched = pathMatcher.match(entryHolder.getAntPath(), url);
		        
		        //TODO 处理 Struts Dispatch Action 的 bug, /xx/xx.do?step=xx 被用户恶意改为 /xx/xx.do?xx=xx&step=xx 时可绕过安全验证
		        
		        if (logger.isDebugEnabled()) {
		            logger.debug("Candidate is: '" + url + "'; pattern is " + entryHolder.getAntPath() + "; matched="
		                + matched);
		        }

		        if (matched) {
		            return entryHolder.getConfigAttributeDefinition();
//		        }else{
//			        if(url.equalsIgnoreCase("http://localhost:8080/publicresource/common/systemcode.do?step=frame&systemModuleName=hr")){
//			        	return entryHolder.getConfigAttributeDefinition();
//		            }
		        }
		    }
		    
		    return null;
		}
		
	}
	
	public static String replace(String url) {
        StringBuffer newUrl = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(url, "?");
        while (tokenizer.hasMoreTokens()) {
        	newUrl.append(tokenizer.nextToken());
        	if (tokenizer.hasMoreTokens()) {
        		newUrl.append("\\\\?");
        	}
        }
        return newUrl.toString();
	}
	
	public static void main(String[] args) {
		String str = "/aasdf/sfds.do?asf=asf";
		System.out.println(replace(str));
	}

	/**
	 * 
	 * @see org.acegisecurity.intercept.ObjectDefinitionSource#getConfigAttributeDefinitions()
	 */
	public Iterator getConfigAttributeDefinitions() {
		FilterInvocationDefinitionSource actualSource = populateFilterInvocationDefinitionSource();

		if (RegExpBasedFilterInvocationDefinitionMap.class.isInstance(actualSource)) {
			return ((RegExpBasedFilterInvocationDefinitionMap) actualSource).getConfigAttributeDefinitions();
		} else if (PathBasedFilterInvocationDefinitionMap.class.isInstance(actualSource)) {			
			return ((PathBasedFilterInvocationDefinitionMap) actualSource).getConfigAttributeDefinitions();
		}
		
		throw new IllegalStateException("wrong type of " + actualSource + ", it should be " + RegExpBasedFilterInvocationDefinitionMap.class 
				+ " or " + PathBasedFilterInvocationDefinitionMap.class);	
	}
	
	
    private FilterInvocationDefinitionSource populateFilterInvocationDefinitionSource() {            	
    	FilterInvocationDefinitionMap definitionSource = null;
    	
    	if (PERL5_KEY.equals(getResourceExpression())) {
    		definitionSource = new RegExpBasedFilterInvocationDefinitionMap();
    	} else if (ANT_PATH_KEY.equals(getResourceExpression())) {
    		definitionSource = new PathBasedFilterInvocationDefinitionMap();
    	} else {
    		throw new IllegalArgumentException("wrong resourceExpression value");
    	}
        
        definitionSource.setConvertUrlToLowercaseBeforeComparison(isConvertUrlToLowercaseBeforeComparison());
        /*tongjq
        ResourceMapping[] mappings = getResourceMappingProvider().getResourceMappings();
        if (mappings == null || mappings.length ==0) {
            return (FilterInvocationDefinitionSource) definitionSource;
        }
        
        for (int i = 0; i < mappings.length; i++) {
            ResourceMapping mapping = mappings[i];
            String[] recipents = mapping.getRecipients();
            
            if (recipents == null || recipents.length == 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Notice, the resource : " + mapping.getResourcePath() + " hasn't no recipents, it will access by any one ! ");
                }
                continue;
            }
            
            StringBuffer valueBuffer = new StringBuffer();
            for (int j = 0; j < recipents.length; j++) {
                valueBuffer.append(recipents[j]);
                if (j < recipents.length - 1) {
                    valueBuffer.append(STAND_DELIM_CHARACTER);
                }
            }
            String value = valueBuffer.toString();                    
            addSecureUrl(definitionSource, mapping.getResourcePath(), value);
         }
         tongjq*/
//        addSecureUrl(definitionSource, SecurityContextInfo.getCurrentPageUrl(), SecurityContextInfo.getCurrentPageUrl());
        return (FilterInvocationDefinitionSource )definitionSource;
    }
	
    
    /**
     * @param source
     * @param name
     * @param value
     * @throws IllegalArgumentException
     */
    private synchronized void addSecureUrl(FilterInvocationDefinitionMap source, String name, String value) 
        throws IllegalArgumentException {
        
        // Convert value to series of security configuration attributes
        ConfigAttributeEditor configAttribEd = new ConfigAttributeEditor();
        configAttribEd.setAsText(value);

        ConfigAttributeDefinition attr = (ConfigAttributeDefinition) configAttribEd.getValue();

        // Register the regular expression and its attribute
        source.addSecureUrl(name, attr);
    }
    
    
	/**
	 * @return Returns the convertUrlToLowercaseBeforeComparison.
	 */
	public boolean isConvertUrlToLowercaseBeforeComparison() {
		return convertUrlToLowercaseBeforeComparison;
	}

	/**
	 * @param convertUrlToLowercaseBeforeComparison The convertUrlToLowercaseBeforeComparison to set.
	 */
	public void setConvertUrlToLowercaseBeforeComparison(boolean convertUrlToLowercaseBeforeComparison) {
		this.convertUrlToLowercaseBeforeComparison = convertUrlToLowercaseBeforeComparison;
	}

	/**
	 * @return Returns the resourceExpression.
	 */
	public String getResourceExpression() {
		return resourceExpression;
	}

	/**
	 * @param resourceExpression The resourceExpression to set.
	 */
	public void setResourceExpression(String resourceExpression) {
		this.resourceExpression = resourceExpression;
	}

	/**
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(resourceMappingProvider, " resourceMappingProvider is required. ");
	}

	/**
	 * @param resourceMappingProvider The resourceMappingProvider to set.
	 */
	public void setResourceMappingProvider(ResourceMappingProvider resourceMappingProvider) {
		this.resourceMappingProvider = resourceMappingProvider;
	}

	/**
	 * @return Returns the resourceMappingProvider.
	 */
	public ResourceMappingProvider getResourceMappingProvider() {
		return resourceMappingProvider;
	}

}