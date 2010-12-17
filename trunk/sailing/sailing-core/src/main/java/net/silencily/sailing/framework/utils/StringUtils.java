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
 * @author ����
 * @version $Id: StringUtils.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public abstract class StringUtils {
	
	/**
	 * ��һ���ַ���ת������, ת���󽫷���һ���µ��ַ���, ����ı� �����ַ���
	 * @see java.nio.charset.Charset
	 * @see java.lang.String#getBytes(java.lang.String)
	 * @see java.lang.String#String(byte[], java.lang.String)
	 * @param str the string
	 * @param srcCharsetName ԭ�ַ���
	 * @param destCharsetName Ŀ���ַ���
	 * @return ��������ת������ַ���
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
