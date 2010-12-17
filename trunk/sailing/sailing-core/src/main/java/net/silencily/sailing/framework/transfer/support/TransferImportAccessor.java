/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.support;

import net.silencily.sailing.framework.transfer.meta.ImportMetaData;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @since 2005-9-25
 * @author ÍõÕþ
 * @version $Id: TransferImportAccessor.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public abstract class TransferImportAccessor implements InitializingBean {
	
	private ImportMetaData importMetaData;
	
	/**
	 * @return Returns the importMetaData.
	 */
	public final ImportMetaData getImportMetaData() {
		return importMetaData;
	}

	/**
	 * @param importMetaData The importMetaData to set.
	 */
	public final void setImportMetaData(ImportMetaData tableMetaData) {
		this.importMetaData = tableMetaData;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {
		Assert.notNull(getImportMetaData(), " importMetaData must be specified ");
	}
	
	
	
	
}
