/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.meta;

import net.silencily.sailing.framework.transfer.TransferRow;

/**
 * @since 2005-11-21
 * @author ÍõÕþ
 * @version $Id: TransferMetaDataFactory.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public abstract class TransferMetaDataFactory {

	public static TransferMetaData getDefaultTransferMetaData() {
		DefaultTransferMetaData metaData = new DefaultTransferMetaData();
		metaData.setDateFormat(TransferRow.DEFAULT_DATE_FORMAT);
		metaData.setTxtSeparator(TransferRow.DEFAULT_TXT_SEPARATOR);
		return metaData;
	}
	
}
