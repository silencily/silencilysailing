/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.report.jasper;

import java.util.HashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.Assert;

/**
 * 思路来自于 com.opensymphony.webwork.views.jasperreports.OgnlValueStackShadowMap
 * @since 2006-9-28
 * @author java2enterprise
 * @version $Id: JavaBeanShadowMap.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 */
public class JavaBeanShadowMap extends HashMap {

	//	~ Static fields/initializers =============================================
	
	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 2541540608310671165L;
	
	//	~ Instance fields ========================================================
	
	private Object javaBean;

	//	~ Constructors ===========================================================
	
	public JavaBeanShadowMap(Object javaBean) {
		Assert.notNull(javaBean, "javaBean required.");
		this.javaBean = javaBean;
	}
	
	//  ~ Methods ================================================================
	
	/**
     * Implementation of containsKey(), overriding HashMap implementation.
     *
     * @param key - The key to check in HashMap and if not found to check on valueStack.
     * @return <tt>true</tt>, if conatins key, <tt>false</tt> otherwise.
	 * @see java.util.HashMap#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		boolean hasKey = super.containsKey(key);
		
		if (!hasKey && String.class.isInstance(key)) {
			try {
				if (PropertyUtils.getProperty(javaBean, (String) key) != null) {
					hasKey = true;
				}
			} catch (Exception ignore) {
			} 
		}
		
		return hasKey;
	}

	/**
     * Implementation of get(), overriding HashMap implementation.
     *
     * @param key - The key to get in HashMap and if not found there from the javaBean.
     * @return value - The object from HashMap or if null, from the javaBean.
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	public Object get(Object key) {
		Object value = super.get(key);
		
		if (value == null && String.class.isInstance(key)) {
			try {
				value = PropertyUtils.getProperty(javaBean, (String) key);
			} catch (Exception ignore) {
			} 
		}
		
		return value;
	}	
	
	
}
