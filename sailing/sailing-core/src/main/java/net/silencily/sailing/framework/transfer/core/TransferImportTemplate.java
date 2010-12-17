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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.silencily.sailing.framework.transfer.TransferImportResult;
import net.silencily.sailing.framework.transfer.TransferImportRow;
import net.silencily.sailing.framework.transfer.TransferImportable;
import net.silencily.sailing.framework.transfer.callbacks.TransferImportCallback;
import net.silencily.sailing.framework.transfer.callbacks.TransferImportProcessDataCallback;
import net.silencily.sailing.framework.transfer.exceptions.ImportCallbackException;
import net.silencily.sailing.framework.transfer.exceptions.ImportProcessDataCallbackException;
import net.silencily.sailing.framework.transfer.exceptions.TransferException;
import net.silencily.sailing.framework.transfer.exceptions.TypeConversionErrorException;
import net.silencily.sailing.framework.transfer.meta.FileType;
import net.silencily.sailing.framework.transfer.meta.ImportMetaData;
import net.silencily.sailing.framework.transfer.strategy.ProcessTransferRowContext;
import net.silencily.sailing.framework.transfer.strategy.ProcessTransferRowStrategy;
import net.silencily.sailing.framework.transfer.support.TransferImportAccessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <class>TransferImportTemplate</class> 是 {@link com.coheg.framework.transfer.core.TransferImportOperations} 的默认实现
 * @see com.coheg.framework.transfer.core.TransferImportOperations
 * @since 2005-9-25
 * @author 王政
 * @version $Id: TransferImportTemplate.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 */
public class TransferImportTemplate extends TransferImportAccessor implements
		TransferImportOperations {
	
	private static Log logger = LogFactory.getLog(TransferImportTemplate.class);
	
	public TransferImportTemplate() {
		
	}
	
	public TransferImportTemplate(ImportMetaData importMetaData) {
		setImportMetaData(importMetaData);
		afterPropertiesSet();
	}
	
	/**
	 * @see com.coheg.framework.transfer.core.TransferImportOperations#importData(java.io.InputStream, com.coheg.framework.transfer.meta.FileType, Date, com.coheg.framework.transfer.callbacks.TransferImportCallback)
	 * @see TransferImportResult
	 */
	public TransferImportResult importData(final InputStream in, final FileType fileType, final Date importDate, TransferImportCallback importCallback) 
		throws TransferException {
		
		ProcessTransferRowStrategy strategy = ProcessTransferRowContext.getStrategy(fileType);
		List importRows = strategy.populateTransferRows(in, getImportMetaData());		
		TransferImportResult result = new TransferImportResult();
		int successImportCount = 0;
		int rowIndex = 1;
		
		for (Iterator iter = importRows.iterator(); iter.hasNext(); ) {
			try {
				TransferImportable transferImportable = importCallback.doImport((TransferImportRow) iter.next());
				result.addImportCallbackSuccessReturnObject(transferImportable);
				if (transferImportable.isImportSuccessed()) {
					successImportCount++;
				}		
			} catch (TypeConversionErrorException e) {				
				result.addImportTypeConversionErrorException(e);
			} catch (Exception e) {
				ImportCallbackException importCallbackException = new ImportCallbackException(e.getMessage(), e, rowIndex);
				result.addImportCallbackException(importCallbackException);
			}
			rowIndex ++;
		}
		
		result.setImportSuccessedCount(successImportCount);
		return result;
	}

	/**
	 * 
	 * @throws ImportProcessDataCallbackException 
	 * @see com.coheg.framework.transfer.core.TransferImportOperations#processDataAfterImported(Serializable[], com.coheg.framework.transfer.callbacks.TransferImportProcessDataCallback)
	 */
	public void processDataAfterImported(Serializable[] ids, TransferImportProcessDataCallback processDataCallback) 
		throws ImportProcessDataCallbackException {
		
		if (ids == null || ids.length == 0) {
			return;
		}
		
		List errorRowNumbers = new LinkedList();
		List errorRowExceptions = new LinkedList();
		
		for (int i = 0; i < ids.length; i++) {			
			try {
				processDataCallback.processData(ids[i]);
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug(" 在处理数据时发生异常, 请检查程序 ", e);
				}
				errorRowNumbers.add(new Integer(i + 1));
				errorRowExceptions.add(e);
			}
			
		}
		
		if (! errorRowNumbers.isEmpty()) {
			throw new ImportProcessDataCallbackException("处理数据时发生异常", null, (Integer[]) errorRowNumbers.toArray(new Integer[0]), (Exception[]) errorRowExceptions.toArray(new Exception[0]));
		}
	}
	
}
