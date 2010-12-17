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
 * @author ����
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
	 * �õ����͵���������������ҳ������ʾ
	 * @return ��������
	 */
	public String getChineseTypeName() {
		if (toClass == null) {
			return null;
		}
		
		if (toClass == Boolean.class) {
			return "��־λ";
		}
		
		if (toClass == Integer.class) {
			return "����";
		}
		
		if (toClass == Long.class) {
			return "������";
		}
		
		if (toClass == Float.class) {
			return "������";
		}
		
		if (toClass == BigDecimal.class) {
			return "���";
		}
		
		if (toClass == Date.class || toClass == java.util.Date.class) {
			return "����";
		}
		
		//TODO add other type if required
		
		return "δ֪����";
		
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
