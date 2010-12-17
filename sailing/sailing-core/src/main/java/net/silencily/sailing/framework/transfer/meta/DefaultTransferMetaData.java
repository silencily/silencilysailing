/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.meta;

import java.text.DateFormat;

import net.silencily.sailing.framework.transfer.TransferRow;

/**
 * @since 2005-9-28
 * @author ÍõÕþ
 * @version $Id: DefaultTransferMetaData.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public class DefaultTransferMetaData implements TransferMetaData {
	
	private DateFormat dateFormat = TransferRow.DEFAULT_DATE_FORMAT;
	
	private String txtSeparator = TransferRow.DEFAULT_TXT_SEPARATOR;
	
	/**
	 * @param dateFormat The dateFormat to set.
	 */
	public final void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * 
	 * @see com.coheg.framework.transfer.meta.ImportMetaData#getDateFormat()
	 */
	public final DateFormat getDateFormat() {
		return dateFormat;
	}
	

	/**
	 * @return Returns the txtSeparator.
	 */
	public final String getTxtSeparator() {
		return txtSeparator;
	}

	/**
	 * @param txtSeparator The txtSeparator to set.
	 */
	public final void setTxtSeparator(String txtSeparator) {
		this.txtSeparator = txtSeparator;
	}	


}
