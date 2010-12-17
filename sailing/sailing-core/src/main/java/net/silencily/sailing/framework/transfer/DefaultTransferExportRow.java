/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @since 2005-9-25
 * @author ÍõÕþ
 * @version $Id: DefaultTransferExportRow.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class DefaultTransferExportRow extends AbstractTransferRow implements TransferExportRow {
	
	private List exportColumnContents = new ArrayList();
	
	/**
	 * @see com.coheg.framework.transfer.AbstractTransferRow#setColumnCounts(int)
	 */
	public void setColumnCounts(int columnCounts) {
		super.setColumnCounts(columnCounts);
		if (exportColumnContents.size() < columnCounts) {
			for (int i = 0; i < columnCounts; i++) {
				exportColumnContents.add(null);
			}
		}
		
	}

	public String getFormatedString(int columnIndex) {
		Object o = exportColumnContents.get(columnIndex);
		if (o == null) {
			return "";
		}
		
		Class clazz = o.getClass();
		if (clazz == String.class) {
			return (String) o;
		} else if (clazz == Date.class) {
			return valueOfDate((Date) o);
		}

		return String.valueOf(o);
	}
	
	public Object get(int columnIndex) {
		return exportColumnContents.get(columnIndex - 1);
	}
	
	public void setString(int columnIndex, String stringValue)  {
		innerSet(columnIndex, stringValue);
	}
	
	public void setBoolean(int columnIndex, Boolean booleanValue) {
		innerSet(columnIndex, booleanValue);
	}
	
	public void setInteger(int columnIndex, Integer integerValue)  {
		innerSet(columnIndex, integerValue);
	}
	
	public void setLong(int columnIndex, Long longValue)  {
		innerSet(columnIndex, longValue);
	}
	
	public void setFloat(int columnIndex, Float floatValue)  {
		innerSet(columnIndex, floatValue);
	}

	public void setBigDecimal(int columnIndex, BigDecimal bigDecimalValue)  {
		innerSet(columnIndex, bigDecimalValue);
	}

	public void setDate(int columnIndex, Date dateValue)  {
		innerSet(columnIndex, dateValue);
	}
	
	private void innerSet(int columnIndex, Object value) {
		if (getColumnCounts() == 0) {
			throw new IllegalStateException(" please set columnCounts before set other args ");
		}
		exportColumnContents.set(columnIndex - 1, value);
	}
	
	private String valueOfDate(Date date) {
		return date == null ? null : getDateFormat().format(date);
	}






}
