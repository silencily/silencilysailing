/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.exceptions;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @since 2005-9-25
 * @author 王政
 * @version $Id: TypeConversionErrorException.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 */
public class TypeConversionErrorException extends TransferException {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = -3547373131195413891L;

	private int rowNumber;
	
	private int columnNumber;
	
	private Object srcObject;
	
	private Class toClass;
	
	/**
	 * 
	 * @param message
	 * @param cause
	 * @param rowNumber
	 * @param columnNumber
	 * @param srcObject
	 * @param toClass
	 */
	public TypeConversionErrorException(String message, Throwable cause, int rowNumber, int columnNumber, Object srcObject, Class toClass) {
		super(message, cause);
		this.rowNumber = rowNumber;
		this.columnNumber = columnNumber;
		this.srcObject = srcObject;
		this.toClass = toClass;
	}
	
	/**
	 * 得到类型的中文名称用于在页面上显示
	 * @return 中文名称
	 */
	public String getChineseTypeName() {
		if (toClass == null) {
			return null;
		}
		
		if (toClass == Boolean.class) {
			return "标志位";
		}
		
		if (toClass == Integer.class) {
			return "整型";
		}
		
		if (toClass == Long.class) {
			return "长整形";
		}
		
		if (toClass == Float.class) {
			return "浮点型";
		}
		
		if (toClass == BigDecimal.class) {
			return "金额";
		}
		
		if (toClass == Date.class || toClass == java.util.Date.class) {
			return "日期";
		}
		
		//TODO add other type if required
		
		return "未知类型";
		
	}

	/**
	 * @return Returns the columnNumber.
	 */
	public int getColumnNumber() {
		return columnNumber;
	}


	/**
	 * @return Returns the rowNumber.
	 */
	public int getRowNumber() {
		return rowNumber;
	}


	/**
	 * @return Returns the srcObject.
	 */
	public Object getSrcObject() {
		return srcObject;
	}


	/**
	 * @return Returns the toClass.
	 */
	public Class getToClass() {
		return toClass;
	}
}
