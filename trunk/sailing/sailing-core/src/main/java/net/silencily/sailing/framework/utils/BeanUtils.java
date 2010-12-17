/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.utils;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * @since 2005-9-13
 * @author 王政
 * @version $Id: BeanUtils.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public abstract class BeanUtils {
	
	private static final Log logger = LogFactory.getLog(BeanUtils.class);
	
	/**
	 * 拷贝属性, 忽略源对象中的空字符串和空白字符串
	 * @param src the srcBean
	 * @return the dest bean
	 */
	public static void copyPropertyIgnoreBlankString(Object dest, Object src) {
		Assert.notNull(src, " src object is required. ");
		Assert.notNull(dest, " dest object is required. ");
		Class clazz = src.getClass();
		
		try {			
			PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(clazz);
			
			for (int i = 0; i < descriptors.length; i++) {
				PropertyDescriptor descriptor = descriptors[i];
				Object value = PropertyUtils.getProperty(src, descriptor.getName());
				
				if (value == null || "class".equals(descriptor.getName())) {
					continue;
				}
								
				if (String.class.isInstance(value) && StringUtils.isBlank((String) value)) {
					continue;
				}
				
				PropertyUtils.setProperty(dest, descriptor.getName(), value);
			}				
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug(" Error during copy properties ", e);
			}
			throw new RuntimeException(" Error during copy properties ", e);
		}
	}
	
	/**
	 * 将一个 bean 中的所有属性指设置为 null
	 * @param bean the bean
	 */
	public static void clearBeanProperties(Object bean) {
		Assert.notNull(bean, " bean is required. ");
		try {			
			PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean.getClass());	
			for (int i = 0; i < descriptors.length; i++) {		
				PropertyDescriptor descriptor = descriptors[i];
				Object value = PropertyUtils.getProperty(bean, descriptor.getName());
				
				if (value == null || "class".equals(descriptor.getName())) {
					continue;
				}
				
				PropertyUtils.setProperty(bean, descriptor.getName(), null);
			}				
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug(" Error during clearBeanProperties", e);
			}
			throw new RuntimeException(" Error during clearBeanProperties ", e);
		}
	}
	
	
	public static void convertStringPropertyEncoding(Object bean, String srcEncoding, String destEncoding) {
		Assert.notNull(bean, " bean is required. ");
		try {			
			PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean.getClass());	
			for (int i = 0; i < descriptors.length; i++) {		
				PropertyDescriptor descriptor = descriptors[i];
				Object value = PropertyUtils.getProperty(bean, descriptor.getName());
				
				if (value == null || ! String.class.isInstance(value)) {
					continue;
				}
				
				String convertedValue = net.silencily.sailing.framework.utils.StringUtils.transformCharsetIfNecessary((String) value, srcEncoding, destEncoding);
				PropertyUtils.setProperty(bean, descriptor.getName(), convertedValue);
			}				
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug(" Error during clearBeanProperties", e);
			}
			throw new RuntimeException(" Error during clearBeanProperties ", e);
		}
	}
	
}
