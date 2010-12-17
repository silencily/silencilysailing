/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.silencily.sailing.framework.transfer.exceptions.TypeConversionErrorException;


/**
 * @since 2005-9-25
 * @author ÍõÕş
 * @version $Id: DefaultTransferImportRow.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class DefaultTransferImportRow extends AbstractTransferRow implements TransferImportRow {
	
	private boolean last = false;
	
	private List importColumnContents = new ArrayList();
	
	/**
	 * Constructor
	 *
	 */
	public DefaultTransferImportRow() {
		this.dateFormat = TransferRow.DEFAULT_DATE_FORMAT;
	}
	
	
	/**
	 * @param importColumnContents The importColumnContents to set.
	 */
	public void setImportColumnContents(List columnContents) {
		this.importColumnContents = columnContents;
	}

	public String getString(int columnIndex) throws TypeConversionErrorException {
		Object value = null;
		try {
			value = importColumnContents.get(columnIndex - 1);
			if (value == null) {
				return null;
			}
			if (Number.class.isInstance(value)) {
				return getFormatedNumberValue(value, 0);
			}
			return value.toString().trim();
		} catch (IndexOutOfBoundsException e) {
			return null;
		} 
	}
	
	public Boolean getBoolean(int columnIndex) throws TypeConversionErrorException {
		Object value = null;
		try {
			value = importColumnContents.get(columnIndex - 1);
			if (value == null) {
				return Boolean.FALSE;
			}
			
			String stringValue = value.toString();
			if (stringValue.equalsIgnoreCase("true") 
					|| stringValue.equalsIgnoreCase("1") 
					|| stringValue.equalsIgnoreCase("ÊÇ") 
					|| stringValue.equalsIgnoreCase("y")
					|| stringValue.equalsIgnoreCase("yes")) {
				
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (IndexOutOfBoundsException e) {
			return null;
		} catch (NumberFormatException e) {
			throw new TypeConversionErrorException(" Type Conversion Error ", e, rowNumber, columnIndex, value, Integer.class);
		}
	}
	
	public Integer getInteger(int columnIndex) throws TypeConversionErrorException {
		Object value = null;
		try {
			value = importColumnContents.get(columnIndex - 1);
			if (value == null) {
				return null;
			}
			return new Integer(Double.valueOf(value.toString()).intValue());
		} catch (IndexOutOfBoundsException e) {
			return null;
		} catch (NumberFormatException e) {
			throw new TypeConversionErrorException(" Type Conversion Error ", e, rowNumber, columnIndex, value, Integer.class);
		}
	}
	
	public Long getLong(int columnIndex) throws TypeConversionErrorException {
		Object value = null;
		try {
			value = importColumnContents.get(columnIndex - 1);
			if (value == null) {
				return null;
			}
			return new Long(Double.valueOf(value.toString()).longValue());
		} catch (IndexOutOfBoundsException e) {
			return null;
		} catch (NumberFormatException e) {
			throw new TypeConversionErrorException(" Type Conversion Error ", e, rowNumber, columnIndex, value, Integer.class);
		}
	}
	

	public Float getFloat(int columnIndex) throws TypeConversionErrorException {
		Object value = null;
		try {
			value = importColumnContents.get(columnIndex - 1);
			if (value == null) {
				return null;
			}		
			return new Float(getFormatedNumberValue(value, 2));
		} catch (IndexOutOfBoundsException e) {
			return null;
		} catch (NumberFormatException e) {
			throw new TypeConversionErrorException(" Type Conversion Error ", e, rowNumber, columnIndex, value, Float.class);
		}
	}

	public BigDecimal getBigDecimal(int columnIndex) throws TypeConversionErrorException {
		Object value = null;
		try {
			value = importColumnContents.get(columnIndex - 1);
			if (value == null) {
				return null;
			}
			
			String formatedNumberValue = getFormatedNumberValue(value, 2);
			return new BigDecimal(formatedNumberValue);
		} catch (IndexOutOfBoundsException e) {
			return null;
		} catch (NumberFormatException e) {
			throw new TypeConversionErrorException(" Type Conversion Error ", e, rowNumber, columnIndex, value, BigDecimal.class);
		}
	}


	/**
	 * @param value
	 * @return
	 * @throws NumberFormatException
	 */
	public static String getFormatedNumberValue(Object value, int fractionDigit) throws NumberFormatException {
		double doubleValue = Double.valueOf(value.toString()).doubleValue();
		StringBuffer patternBuffer = new StringBuffer("###########################");
//		if (fractionDigit > 0) {
//			patternBuffer.append(",");
//			for (int i = 0; i < fractionDigit; i++) {
//				patternBuffer.append("#");
//			}
//		}
		
		NumberFormat decimalFormat = new DecimalFormat(patternBuffer.toString(), new DecimalFormatSymbols(Locale.CHINA));
		decimalFormat.setMaximumIntegerDigits(100);
		decimalFormat.setMaximumFractionDigits(fractionDigit);		
		String formatedValue = decimalFormat.format(doubleValue);
		return formatedValue;
	}
	
	
	
//	private String processMoney(String moneyValue) {
//		if (moneyValue.indexOf(USD_PREFIX) == 0 || moneyValue.indexOf(CNY_PREFIX) ==0) {
//			return moneyValue.substring(1);
//		} 
//		return moneyValue;
//	}
	
	
	public Date getDate(int columnIndex) throws TypeConversionErrorException {
		Object value = null;
		try {
			value = importColumnContents.get(columnIndex - 1);
			if (value == null) {
				return null;
			}		
			if (Date.class.isInstance(value)) {
				return (Date) value;
			}
			return getDateFormat().parse(value.toString());
		} catch (IndexOutOfBoundsException e) {
			return null;
		} catch (ParseException e) {
			throw new TypeConversionErrorException(" Type Conversion Error ", e, rowNumber, columnIndex, value, Date.class);
		}
	}


	/**
	 * @return Returns the last.
	 */
	public boolean isLast() {
		return last;
	}


	/**
	 * @param last The last to set.
	 */
	public void setLast(boolean last) {
		this.last = last;
	}






	
}
