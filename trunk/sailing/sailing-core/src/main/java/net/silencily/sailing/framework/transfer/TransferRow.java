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
 * <class>TransferRow</class> �ǵ��뵼�������е�һ������
 * @since 2005-9-28
 * @author ����
 * @version $Id: TransferRow.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public interface TransferRow {

	/** Ĭ�ϵ����ڸ�ʽ */
	DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	/** Ĭ�ϵ��ı�����ֵ֮��ļ���� */
	String DEFAULT_TXT_SEPARATOR = "|";
	
	
	/**
	 * �õ���ǰ�����ǵڼ���
	 * @return the rowNumber
	 */
	int getRowNumber();
	
	/**
	 * ���õ�ǰ�����ǵڼ���
	 * @param rowNumber the rowNumber you want to set
	 */
	void setRowNumber(int rowNumber);
	
	/**
	 * �õ���ǰ�е�����
	 * @return the columnCounts
	 */
	int getColumnCounts();
	
	/**
	 * ���õ�ǰ�е�����
	 * @param columnCounts the columnCounts
	 */
	void setColumnCounts(int columnCounts);
	
	
	/**
	 * �õ����ڸ�ʽ, ���������ں��ַ���֮�����ת��
	 * @return the DateFormat
	 * @see #setDateFormat(DateFormat)
	 * @see #getDate(int)
	 * @see TransferExportRow#setDate(int, Date)
	 */
	DateFormat getDateFormat();
	
	/**
	 * �������ڸ�ʽ, ���������ں��ַ���֮�����ת��
	 * @see #getDateFormat()
	 * @see #getDate(int)
	 * @see TransferExportRow#setDate(int, Date)
	 * @param dateFormat the dataFormat you want to set
	 */
	void setDateFormat(DateFormat dateFormat);
	
	
}
