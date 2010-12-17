/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer;

import java.math.BigDecimal;
import java.util.Date;



/**
 * @since 2005-9-25
 * @author ÍõÕþ
 * @version $Id: TransferExportRow.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public interface TransferExportRow extends TransferRow {
	
	void setString(int columnIndex, String stringValue);
	
	void setBoolean(int columnIndex, Boolean booleanValue);
	
	void setInteger(int columnIndex, Integer integerValue);
	
	void setLong(int columnIndex, Long longValue);
	
	void setFloat(int columnIndex, Float floatValue);
	
	void setBigDecimal(int columnIndex, BigDecimal bigDecimalValue);
	
	void setDate(int columnIndex, Date dateValue);
	
	String getFormatedString(int columnIndex);
	
	Object get(int columnIndex);
	
}
