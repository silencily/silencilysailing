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
 * @author ����
 * @version $Id: ImportProcessDataCallbackException.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 */
public class ImportProcessDataCallbackException extends TransferException {

	/** use serialVersionUID from JDK 1.0.2 for interoperability */
	private static final long serialVersionUID = 8764692879782569303L;
	
	/** �ڴ�������ʱ callback �����쳣�������� */
	private Integer[] errorRowNumbers = new Integer[0];
	
	/** �ڴ�������ʱ callback �����������쳣 */
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
	 * �õ����ڴ��쳣����ϸ����
	 * @return the description
	 */
	public String getParticularDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append(" �ڴ�������ʱ, �����������ִ��� : ");
		sb.append("<br>");
		
		Assert.isTrue(errorRowNumbers.length == errorRowExceptions.length);
		for (int i = 0; i < errorRowNumbers.length; i++) {
			sb.append(" &nbsp;&nbsp; �� ");
			sb.append(errorRowNumbers[i]);
			sb.append(" ���쳣��Ϣ : ");
			sb.append(errorRowExceptions[i] == null ? " " : TransferExceptionMessageTranslator.lookup(errorRowExceptions[i].getClass()));
			sb.append("<br>");
		}
		
		sb.append("�����������ѱ�ɾ��, Ϊ���ܼ�������, ϵͳ�ѽ���Щ���ڵ������г�ȥ. ");		
		return sb.toString();
	}
	
	
	
}
