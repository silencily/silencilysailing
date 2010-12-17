/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.exceptions;

import net.silencily.sailing.framework.transfer.TransferExceptionMessageTranslator;


/**
 * <class>TransferException</class> �ǵ��뵼���쳣�Ļ���, ���� RuntimeException �̳е�ԭ������������׳����쳣, �ͻ�Ҫ���������Ҫ�ύ !!!
 * ��Ȼż��Ϊ�������������, but ,,, customer is GOD ... :(
 * @since 2005-9-25
 * @author ����
 * @version $Id: TransferException.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 */
public class TransferException extends Exception {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 3453375231821928462L;

	/**
	 * the message
	 * @param message
	 */
	public TransferException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public TransferException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * �õ�ת������쳣��Ϣ
	 * @return �쳣��Ϣ
	 * @see TransferExceptionMessageTranslator#lookup(Class)
	 */
	public String getTranslatedMessage() {
		Class causeClass = getCause() == null ? Exception.class : getCause().getClass();
		return TransferExceptionMessageTranslator.lookup(causeClass);
	}
	
}
