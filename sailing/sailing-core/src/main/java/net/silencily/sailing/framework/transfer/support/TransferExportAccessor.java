/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.support;

import net.silencily.sailing.framework.transfer.meta.TransferMetaData;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @since 2005-9-28
 * @author ÍõÕþ
 * @version $Id: TransferExportAccessor.java,v 1.1 2010/12/10 10:54:25 silencily Exp $
 */
public abstract class TransferExportAccessor implements InitializingBean {
	
	private TransferMetaData transferMetaData;
	
	
	/**
	 * @return Returns the transferMetaData.
	 */
	public final TransferMetaData getTransferMetaData() {
		return transferMetaData;
	}

	/**
	 * @param transferMetaData The transferMetaData to set.
	 */
	public final void setTransferMetaData(TransferMetaData transferMetaData) {
		this.transferMetaData = transferMetaData;
	}



	public void afterPropertiesSet() {
		Assert.notNull(getTransferMetaData(), " transferMetaData must be specified. ");
	}

}
