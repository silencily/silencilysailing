/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 * Project cardAssistant
 */
package net.silencily.sailing.framework.web.webwork.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.webwork.util.WebWorkTypeConverter;
import com.opensymphony.xwork.util.TypeConversionException;

/**
 * <class>DateConterter</class> 是 {@link java.util.Date} 的转换器
 * 
 * @since 2005-9-7
 * @author 王政
 * @version $Id: DateConverter.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 */
public class DateConverter extends WebWorkTypeConverter {

	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	public static final Date DEFAULT_VALUE = null;

	private String datePattern;

	private Date defaultValue;

	public DateConverter() {
		this.datePattern = DEFAULT_DATE_PATTERN;
		this.defaultValue = DEFAULT_VALUE;
	}

	/**
	 * @return Returns the datePattern.
	 */
	public String getDatePattern() {
		return datePattern;
	}

	/**
	 * @param datePattern  The datePattern to set.
	 */
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	/**
	 * @return Returns the defaultValue.
	 */
	public Date getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue  The defaultValue to set.
	 */
	public void setDefaultValue(Date defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @see com.opensymphony.webwork.util.WebWorkTypeConverter#convertFromString(java.util.Map, java.lang.String[], java.lang.Class)
	 */
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (toClass != Date.class) {
			throw new TypeConversionException(new UnsupportedOperationException(getClass() + " only support java.util.Date Type!"));
		}

		if (values == null || values.length == 0) {
			return defaultValue;
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			sb.append(values[i]);
		}
		String dateValue = sb.toString().trim();
		
		if (StringUtils.isBlank(dateValue)) {
			return defaultValue;
		}
		
		if (dateValue.length() != datePattern.length()) {
			throw new TypeConversionException("Can't convert " + dateValue + " to java.util.Date in usring format : " + datePattern);
		}
		
		try {
			DateFormat df = new SimpleDateFormat(datePattern);
			df.setLenient(false);
			return df.parse(dateValue);
		} catch (Exception e) {
			throw new TypeConversionException("Can't convert " + dateValue + " to java.util.Date in usring format : " + datePattern);			
		}
	}

	/**
	 * @see com.opensymphony.webwork.util.WebWorkTypeConverter#convertToString(java.util.Map, java.lang.Object)
	 */
	public String convertToString(Map context, Object o) {
		if (o == null) {
			return null;
		}
		
		if (! (o instanceof Date)) {
			throw new TypeConversionException(new UnsupportedOperationException(getClass() + " only support java.util.Date Type!"));
		}
		return new SimpleDateFormat(datePattern).format((Date) o);
	}

}
