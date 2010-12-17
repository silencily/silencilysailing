/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer;

import java.util.LinkedList;
import java.util.List;

import net.silencily.sailing.framework.transfer.exceptions.ImportCallbackException;
import net.silencily.sailing.framework.transfer.exceptions.TypeConversionErrorException;

/**
 * <class>TransferImportResult</class> ���ڴ洢�������ݺ�����б�����Ϣ
 * @since 2005-9-25
 * @author ����
 * @version $Id: TransferImportResult.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class TransferImportResult {
	
	/** �������ݺ����е���ɹ��ķ��ض���, fill with {@link TransferImportable} */
	private List transferImportables = new LinkedList();
	
	/** �������ݺ����г�������ת���쳣, fill with {@link TypeConversionErrorException} */
	private List importTypeConversionErrorExceptions = new LinkedList();
	
	/** �������ݺ��� {@link TransferImportCallback } �г��ֵ����������쳣, ������ ui ����ʾ, fill with {@link Exception} */
	private List importCallbackExceptions = new LinkedList();
	
	/** ҵ�����, �������洢��Ҫ��ҵ����Ϣ */
	private Object businessObject = new Object();
	
	private int importSuccessedCount;
	
	/**
	 * @param importSuccessedCount The importSuccessedCount to set.
	 */
	public void setImportSuccessedCount(int successImportCount) {
		this.importSuccessedCount = successImportCount;
	}

	/**
	 * 
	 * @return
	 */
	public int getImportSuccessedCount() {
		return importSuccessedCount;
	}
	
	/**
	 * @return Returns the transferImportables.
	 */
	public List getTransferImportables() {
		return transferImportables;
	}

	/**
	 * @param transferImportables The transferImportables to set.
	 */
	public void setTransferImportables(List importCallbackReturnObjects) {
		this.transferImportables = importCallbackReturnObjects;
	}
	
	
	/**
	 * @return Returns the importCallbackExceptions.
	 */
	public List getImportCallbackExceptions() {
		return importCallbackExceptions;
	}

	/**
	 * @param importCallbackExceptions The importCallbackExceptions to set.
	 */
	public void setImportCallbackExceptions(List importDataAccessExceptions) {
		this.importCallbackExceptions = importDataAccessExceptions;
	}

	/**
	 * @return Returns the importTypeConversionErrorExceptions.
	 */
	public List getImportTypeConversionErrorExceptions() {
		return importTypeConversionErrorExceptions;
	}

	/**
	 * @param importTypeConversionErrorExceptions The importTypeConversionErrorExceptions to set.
	 */
	public void setImportTypeConversionErrorExceptions(List importTypeConversionErrorExceptions) {
		this.importTypeConversionErrorExceptions = importTypeConversionErrorExceptions;
	}

	/**
	 * 
	 * @param importCallbackReturnObject
	 */
	public void addImportCallbackSuccessReturnObject(TransferImportable importCallbackSuccessReturnObject) {
		if (getTransferImportables() == null) {
			setTransferImportables(new LinkedList());
		}
		getTransferImportables().add(importCallbackSuccessReturnObject);
	}
	
	/**
	 * @param importTypeConversionErrorExceptions The importTypeConversionErrorExceptions to set.
	 */
	public void addImportTypeConversionErrorException(TypeConversionErrorException typeConversionErrorException) {
		if (getImportTypeConversionErrorExceptions() == null) {
			setImportTypeConversionErrorExceptions(new LinkedList());
		}
		getImportTypeConversionErrorExceptions().add(typeConversionErrorException);
	}
	
	
	/**
	 * @param importCallbackExceptions The importCallbackExceptions to set.
	 */
	public void addImportCallbackException(ImportCallbackException importCallbackException) {
		if (getImportCallbackExceptions() == null) {
			setImportCallbackExceptions(new LinkedList());
		}
		getImportCallbackExceptions().add(importCallbackException);
	}

	/**
	 * @return Returns the businessObject.
	 */
	public Object getBusinessObject() {
		return businessObject;
	}

	/**
	 * @param businessObject The businessObject to set.
	 */
	public void setBusinessObject(Object businessObject) {
		this.businessObject = businessObject;
	}
	
}
