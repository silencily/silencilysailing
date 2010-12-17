/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <class>TransferRow</class> 是导入导出过程中的一行数据
 * @since 2005-9-28
 * @author 王政
 * @version $Id: TransferRow.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public interface TransferRow {

	/** 默认的日期格式 */
	DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	/** 默认的文本属性值之间的间隔符 */
	String DEFAULT_TXT_SEPARATOR = "|";
	
	
	/**
	 * 得到当前数据是第几行
	 * @return the rowNumber
	 */
	int getRowNumber();
	
	/**
	 * 设置当前数据是第几行
	 * @param rowNumber the rowNumber you want to set
	 */
	void setRowNumber(int rowNumber);
	
	/**
	 * 得到当前行的列数
	 * @return the columnCounts
	 */
	int getColumnCounts();
	
	/**
	 * 设置当前行的列数
	 * @param columnCounts the columnCounts
	 */
	void setColumnCounts(int columnCounts);
	
	
	/**
	 * 得到日期格式, 用于在日期和字符串之间进行转换
	 * @return the DateFormat
	 * @see #setDateFormat(DateFormat)
	 * @see #getDate(int)
	 * @see TransferExportRow#setDate(int, Date)
	 */
	DateFormat getDateFormat();
	
	/**
	 * 设置日期格式, 用于在日期和字符串之间进行转换
	 * @see #getDateFormat()
	 * @see #getDate(int)
	 * @see TransferExportRow#setDate(int, Date)
	 * @param dateFormat the dataFormat you want to set
	 */
	void setDateFormat(DateFormat dateFormat);
	
	
}
