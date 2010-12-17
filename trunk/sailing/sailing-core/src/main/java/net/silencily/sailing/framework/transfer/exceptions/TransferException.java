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
 * <class>TransferException</class> 是导入导出异常的基类, 不从 RuntimeException 继承的原因是如果程序抛出此异常, 客户要求事务必需要提交 !!!
 * 虽然偶认为这样做不大合理, but ,,, customer is GOD ... :(
 * @since 2005-9-25
 * @author 王政
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
	 * 得到转译过的异常信息
	 * @return 异常信息
	 * @see TransferExceptionMessageTranslator#lookup(Class)
	 */
	public String getTranslatedMessage() {
		Class causeClass = getCause() == null ? Exception.class : getCause().getClass();
		return TransferExceptionMessageTranslator.lookup(causeClass);
	}
	
}
