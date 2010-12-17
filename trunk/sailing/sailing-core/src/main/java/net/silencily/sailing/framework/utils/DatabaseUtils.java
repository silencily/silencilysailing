/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * @since 2005-9-24
 * @author 王政
 * @version $Id: DatabaseUtils.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public abstract class DatabaseUtils {
	
	public static String DEFAULT_CONFIG_PATH = "jdbc.properties";
	
	public static String DEFAULT_CHARSET_KEY_NAME = "dababase.charset";
	
	private static String configPath = DEFAULT_CONFIG_PATH;
	
	private static String charsetKeyName = DEFAULT_CHARSET_KEY_NAME;
	
	private static Properties properties = null;
	
	/**
	 * @return Returns the configPath.
	 */
	public static String getConfigPath() {
		return configPath;
	}

	/**
	 * @param configPath The configPath to set.
	 */
	public static void setConfigPath(String configPath) {
		DatabaseUtils.configPath = configPath;
	}
	
	/**
	 * @return Returns the charsetKeyName.
	 */
	public static String getCharsetKeyName() {
		return charsetKeyName;
	}

	/**
	 * @param charsetKeyName The charsetKeyName to set.
	 */
	public static void setCharsetKeyName(String keyName) {
		DatabaseUtils.charsetKeyName = keyName;
	}
	
	public static String getPropertyValue(String keyName) {
		return getProperties().getProperty(keyName);
	}
	
	/**
	 * 得到数据库 charset
	 * @return the charset name
	 */
	public static String getDatabaseCharset() {
		Properties properties = getProperties();
		String charset = properties.getProperty(getCharsetKeyName());
		Assert.notNull(charset, " can't get charset from " + getConfigPath() + " in using key name " + getCharsetKeyName());
		return charset;
	}
	
	/**
	 * 重新读取 properties 文件
	 *
	 */
	public static void reloadProperties() {
		URL url = Thread.currentThread().getContextClassLoader().getResource(getConfigPath());
		Assert.notNull(url,  getConfigPath() + " missing  ");
		
		properties = new Properties();
		try {
			properties.load(url.openStream());
		} catch (IOException e) {
			throw new RuntimeException(" can't get properteis ", e);
		}
	}
	
	/**
	 * 将从数据库中取得的字符串转换为 "GBK" 编码, 并做 trim 处理
	 * @param str the string
	 * @return the string after processed
	 */
	public static String transformString2GBKAndTrim(String str) {		
		try {
			return  net.silencily.sailing.framework.utils.StringUtils.transformCharsetIfNecessary(StringUtils.trim(str), getDatabaseCharset(), "GBK");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String transformString2ISOIfNecessarilyAndTrim(String str) {
		try {
			return  net.silencily.sailing.framework.utils.StringUtils.transformCharsetIfNecessary(StringUtils.trim(str), "GBK", getDatabaseCharset());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private synchronized static Properties getProperties () {
		if (properties == null) {
			reloadProperties();
		}
		return properties;
	}
	

	public static void main(String[] args) {
		System.out.println(getDatabaseCharset());
	}
	
	

	
	
	
	
	
}
