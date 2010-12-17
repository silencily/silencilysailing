/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer;

import java.text.DateFormat;


/**
 * @since 2005-9-28
 * @author ÍõÕþ
 * @version $Id: AbstractTransferRow.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public abstract class AbstractTransferRow {

	protected int rowNumber;
	
	protected int columnCounts;
	
	protected DateFormat dateFormat = TransferRow.DEFAULT_DATE_FORMAT;

	/**
	 * @return Returns the columnCounts.
	 */
	public int getColumnCounts() {
		return columnCounts;
	}

	/**
	 * @param columnCounts The columnCounts to set.
	 */
	public void setColumnCounts(int columnCounts) {
		this.columnCounts = columnCounts;
	}

	/**
	 * @return Returns the dateFormat.
	 */
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat The dateFormat to set.
	 */
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @return Returns the rowNumber.
	 */
	public int getRowNumber() {
		return rowNumber;
	}

	/**
	 * @param rowNumber The rowNumber to set.
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	
	
	
	
}
