/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.core.support;

import net.silencily.sailing.framework.transfer.core.TransferImportTemplate;
import net.silencily.sailing.framework.transfer.meta.ImportMetaData;

import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

/**
 * Convenient super class for Transfer import data access objects.
 * Requires a ImportMetaData to be set, providing a
 * TransferImportTemplate based on it to subclasses.
 * 
 * @since 2005-9-26
 * @author ÍõÕþ
 * @version $Id: TransferImportDaoSupport.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 */
public abstract class TransferImportDaoSupport extends DaoSupport {
	
	private ImportMetaData importMetaData;
	
	private TransferImportTemplate transferImportTemplate;
	
	/**
	 * @see org.springframework.dao.support.DaoSupport#checkDaoConfig()
	 */
	protected final void checkDaoConfig() throws IllegalArgumentException {
		Assert.notNull(getImportMetaData(), " importMetaData or transferImportTemplate is required. ");
	}

	/**
	 * @return Returns the transferImportTemplate.
	 */
	public final TransferImportTemplate getTransferImportTemplate() {
		return transferImportTemplate;
	}

	/**
	 * @return Returns the importMetaData.
	 */
	public final ImportMetaData getImportMetaData() {
		return importMetaData == null ? getTransferImportTemplate().getImportMetaData() : importMetaData;
	}

	/**
	 * @param importMetaData The importMetaData to set.
	 */
	public final void setImportMetaData(ImportMetaData importMetaData) {
		this.importMetaData = importMetaData;
		this.transferImportTemplate = createTransferImportTemplate(importMetaData);
	}
	
	/**
	 * Create a TransferImportTemplate for the given ImportMetaData.
	 * Only invoked if populating the TransferDAO with a ImportMetaData reference!
	 * <p>Can be overridden in subclasses to provide a TransferImportTemplate instance
	 * with different configuration, or a custom TransferImportTemplate subclass.
	 * @see #setImportMetaData(ImportMetaData)
	 * @param importMetaData the ImportMetaData to create a TransferImportTemplate for
	 * @return the new TransferImportTemplate instance
	 */
	protected TransferImportTemplate createTransferImportTemplate(ImportMetaData importMetaData) {
		return new TransferImportTemplate(importMetaData);
	}
	
}
