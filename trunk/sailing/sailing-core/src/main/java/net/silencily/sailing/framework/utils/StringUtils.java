/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.utils;

import java.io.UnsupportedEncodingException;

import org.springframework.util.Assert;

/**
 * @since 2005-9-23
 * @author 王政
 * @version $Id: StringUtils.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public abstract class StringUtils {
	
	/**
	 * 将一个字符串转换编码, 转换后将返回一个新的字符串, 不会改变 参数字符串
	 * @see java.nio.charset.Charset
	 * @see java.lang.String#getBytes(java.lang.String)
	 * @see java.lang.String#String(byte[], java.lang.String)
	 * @param str the string
	 * @param srcCharsetName 原字符集
	 * @param destCharsetName 目的字符集
	 * @return 经过编码转换后的字符集
	 * @throws UnsupportedEncodingException If the named charset is not supported
	 */
	public static String transformCharsetIfNecessary(String str, String srcCharsetName, String destCharsetName) 
		throws UnsupportedEncodingException {
		
		Assert.notNull(srcCharsetName, " srcCharsetName is required. ");
		Assert.notNull(destCharsetName, " destCharsetName is requried. ");
		
		if (str == null) {
			return null;
		}
		
		if (srcCharsetName.equals(destCharsetName)) {
			return str;
		}
		
		return new String(str.getBytes(srcCharsetName), destCharsetName);
	}
	
	
}
