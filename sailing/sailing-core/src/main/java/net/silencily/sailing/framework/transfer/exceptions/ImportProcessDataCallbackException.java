/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.exceptions;

import net.silencily.sailing.framework.transfer.TransferExceptionMessageTranslator;

import org.springframework.util.Assert;


/**
 * @since 2005-9-28
 * @author 王政
 * @version $Id: ImportProcessDataCallbackException.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 */
public class ImportProcessDataCallbackException extends TransferException {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 8764692879782569303L;
	
	/** 在处理数据时 callback 发生异常的所有行 */
	private Integer[] errorRowNumbers = new Integer[0];
	
	/** 在处理数据时 callback 发生的所有异常 */
	private Exception[] errorRowExceptions = new Exception[0];
	
	public ImportProcessDataCallbackException(String message, Throwable cause, Integer[] errorNumbers, Exception[] errorRowExceptions) {
		super(message, cause);
		Assert.notNull(errorNumbers, " errorRowNumbers must be specified ");
		Assert.notNull(errorRowExceptions, " errorRowExceptions must be specified ");
		this.errorRowNumbers = errorNumbers;
		this.errorRowExceptions = errorRowExceptions;
	}

	/**
	 * @return Returns the errorRowNumbers.
	 */
	public Integer[] getErrorRowNumbers() {
		return errorRowNumbers;
	}
	
	/**
	 * @return Returns the errorRowExceptions.
	 */
	public Exception[] getErrorRowExceptions() {
		return errorRowExceptions;
	}

	/**
	 * 得到关于此异常的详细描述
	 * @return the description
	 */
	public String getParticularDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append(" 在处理数据时, 以下行数出现错误 : ");
		sb.append("<br>");
		
		Assert.isTrue(errorRowNumbers.length == errorRowExceptions.length);
		for (int i = 0; i < errorRowNumbers.length; i++) {
			sb.append(" &nbsp;&nbsp; 第 ");
			sb.append(errorRowNumbers[i]);
			sb.append(" 行异常信息 : ");
			sb.append(errorRowExceptions[i] == null ? " " : TransferExceptionMessageTranslator.lookup(errorRowExceptions[i].getClass()));
			sb.append("<br>");
		}
		
		sb.append("可能是数据已被删除, 为了能继续操作, 系统已将这些行在导入结果中除去. ");		
		return sb.toString();
	}
	
	
	
}
