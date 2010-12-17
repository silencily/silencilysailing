/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.core;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;


import net.silencily.sailing.framework.transfer.TransferImportResult;
import net.silencily.sailing.framework.transfer.callbacks.TransferImportCallback;
import net.silencily.sailing.framework.transfer.callbacks.TransferImportProcessDataCallback;
import net.silencily.sailing.framework.transfer.exceptions.ImportProcessDataCallbackException;
import net.silencily.sailing.framework.transfer.exceptions.TransferException;
import net.silencily.sailing.framework.transfer.meta.FileType;
import net.silencily.sailing.framework.transfer.meta.ImportMetaData;

/**
 * <class>TransferImportOperations</class> �ṩ�˵���������Ҫ�Ĳ���, ��Ĭ��ʵ���� {@link com.coheg.framework.transfer.core.TransferImportTemplate}
 * @see com.coheg.framework.transfer.core.TransferImportTemplate
 * @since 2005-9-25
 * @author ����
 * @version $Id: TransferImportOperations.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 */
public interface TransferImportOperations {
	
	/**
	 * ��������, ע�� inputStream �� fileType �Ķ�Ӧ��
	 * @param inputStream the inputStream of file
	 * @param fileType the fileType
	 * @param importDate ��������, ������ҳ��
	 * @param importCallback the importCallback, ������Ҫʵ�ִ˷���
	 * @return �������ݽ��, ���л�洢һЩ��Ҫ����Ϣ
	 * @throws TransferException if any exception happens
	 * @see TransferImportResult 
	 */
	TransferImportResult importData(final InputStream inputStream, final FileType fileType, final Date importDate, TransferImportCallback importCallback) throws TransferException;
	
	/**
	 * �����Ѿ����뵽���е�����, һ��������ֲ��� :��ɾ���������ݺ�ȷ������.  ע��, �ڴ˷����в��� originalImportResult ���ᱻ�޸�,
	 * Ŀ���ǽ����µĴ����������� ui ��, ����Ĳ������ǻὫ {@link TransferImportResult#importCallbackSuccessReturnObjects} �е��Ѵ������ɾ��
	 * @param ids �� ui ��ȡ�õ�ʵ�� id 
	 * @param processDataCallback the processDataCallback, ������Ҫʵ�ִ˷���
	 * @throws ImportProcessDataCallbackException ��� callback �г����κ��쳣
	 * @see TransferImportResult#importCallbackSuccessReturnObjects
	 * @see TransferImportProcessDataCallback
	 */
	void processDataAfterImported(final Serializable[] ids, TransferImportProcessDataCallback processDataCallback)
		throws ImportProcessDataCallbackException;
	

	/**
	 * �õ�����������Ҫ��Ԫ��Ϣ
	 * @return the ImportMetaData
	 */
	ImportMetaData getImportMetaData();
}
