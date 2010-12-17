/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import net.silencily.sailing.framework.transfer.exceptions.TypeConversionErrorException;


/**
 * <class>TransferImportRow</class> ���������ݹ����е�һ������
 * @since 2005-9-25
 * @author ����
 * @version $Id: TransferImportRow.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public interface TransferImportRow extends TransferRow {
	
	/** ��Ԫǰ׺ */
	String USD_PREFIX = "$";
	
	/** �����ǰ׺ */
	String CNY_PREFIX = "��";
	
	/**
	 * ��ǰ�����Ƿ�������һ��
	 * @return whether it is the last 
	 */
	boolean isLast();
	
	
	/**
	 * ��������ֵ�õ� String ֵ, ע�������ֵ�Ǵ� 1 ��ʼ, ����ͬ {@link java.sql.ResultSet#getString(int)}, 
	 * �������ֵ�����ļ��е������, ������ null
	 * 
	 * @see java.sql.ResultSet#getString(int)
	 * @param columnIndex the columnIndex
	 * @return the String value
	 * @throws TypeConversionErrorException �������ת�����ִ���, �벻Ҫ������쳣, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} �н�������Ӧ����
	 */
	String getString(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * ��������ֵ�õ� Boolean ֵ, ע�������ֵ�Ǵ� 1 ��ʼ, ����ͬ {@link java.sql.ResultSet#getBoolean(int)}, 
	 * �������ֵ�����ļ��е������, ������ null. <p>
	 * ���ô˷���, ˵������Ϊ����Ӧ����һ�� Boolean ���͵�ֵ, ϵͳ����ͼ���ļ��е� String ֵת��Ϊ Boolean ����, ���ת��ʧ��, �� throw {@link TypeConversionErrorException},
	 * �����㲻��Ҫ������쳣, ���ִ��쳣˵���ͻ��ϴ����ļ������Ͳ���ȷ, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * ������Ӧ�����������ʾ�� ui ��, ���������Ҫ���ü���
	 * 
	 * @see java.sql.ResultSet#getInt(int)
	 * @param columnIndex the columnIndex
	 * @return the Integer value
	 * @throws TypeConversionErrorException �������ת�����ִ���, �벻Ҫ������쳣, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} �н�������Ӧ����
	 */
	Boolean getBoolean(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * ��������ֵ�õ� Integer ֵ, ע�������ֵ�Ǵ� 1 ��ʼ, ����ͬ {@link java.sql.ResultSet#getInt(int)}, 
	 * �������ֵ�����ļ��е������, ������ null. <p>
	 * ���ô˷���, ˵������Ϊ����Ӧ����һ�� Integer ���͵�ֵ, ϵͳ����ͼ���ļ��е� String ֵת��Ϊ Integer ����, ���ת��ʧ��, �� throw {@link TypeConversionErrorException},
	 * �����㲻��Ҫ������쳣, ���ִ��쳣˵���ͻ��ϴ����ļ������Ͳ���ȷ, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * ������Ӧ�����������ʾ�� ui ��, ���������Ҫ���ü���
	 * 
	 * @see java.sql.ResultSet#getInt(int)
	 * @param columnIndex the columnIndex
	 * @return the Integer value
	 * @throws TypeConversionErrorException �������ת�����ִ���, �벻Ҫ������쳣, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} �н�������Ӧ����
	 */
	Integer getInteger(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * ��������ֵ�õ� Long ֵ, ע�������ֵ�Ǵ� 1 ��ʼ, ����ͬ {@link java.sql.ResultSet#getLong(int)}, 
	 * �������ֵ�����ļ��е������, ������ null. <p>
	 * ���ô˷���, ˵������Ϊ����Ӧ����һ�� Long ���͵�ֵ, ϵͳ����ͼ���ļ��е� String ֵת��Ϊ Long ����, ���ת��ʧ��, �� throw {@link TypeConversionErrorException},
	 * �����㲻��Ҫ������쳣, ���ִ��쳣˵���ͻ��ϴ����ļ������Ͳ���ȷ, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * ������Ӧ�����������ʾ�� ui ��, ���������Ҫ���ü���
	 * 
	 * @see java.sql.ResultSet#getLong(int)
	 * @param columnIndex the columnIndex
	 * @return the Long value
	 * @throws TypeConversionErrorException �������ת�����ִ���, �벻Ҫ������쳣, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} �н�������Ӧ����
	 */
	Long getLong(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * ��������ֵ�õ� Float ֵ, ע�������ֵ�Ǵ� 1 ��ʼ, ����ͬ {@link java.sql.ResultSet#getFloat(int)}, 
	 * �������ֵ�����ļ��е������, ������ null. <p>
	 * ���ô˷���, ˵������Ϊ����Ӧ����һ�� Float ���͵�ֵ, ϵͳ����ͼ���ļ��е� String ֵת��Ϊ Float ����, ���ת��ʧ��, �� throw {@link TypeConversionErrorException},
	 * �����㲻��Ҫ������쳣, ���ִ��쳣˵���ͻ��ϴ����ļ������Ͳ���ȷ, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * ������Ӧ�����������ʾ�� ui ��, ���������Ҫ���ü���
	 * 
	 * @see java.sql.ResultSet#getFloat(int)
	 * @param columnIndex the columnIndex
	 * @return the Integer value
	 * @throws TypeConversionErrorException �������ת�����ִ���, �벻Ҫ������쳣, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} �н�������Ӧ����
	 */
	Float getFloat(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * ��������ֵ�õ� BigDecimal ֵ, ע�������ֵ�Ǵ� 1 ��ʼ, ����ͬ {@link java.sql.ResultSet#getBigDecimal(int)}, 
	 * �������ֵ�����ļ��е������, ������ null. <p>
	 * ���ô˷���, ˵������Ϊ����Ӧ����һ�� BigDecimal ���͵�ֵ, ϵͳ����ͼ���ļ��е� String ֵת��Ϊ BigDecimal ����, ���ת��ʧ��, �� throw {@link TypeConversionErrorException},
	 * �����㲻��Ҫ������쳣, ���ִ��쳣˵���ͻ��ϴ����ļ������Ͳ���ȷ, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * ������Ӧ�����������ʾ�� ui ��, ���������Ҫ���ü���
	 * 
	 * @see java.sql.ResultSet#getBigDecimal(int)
	 * @param columnIndex the columnIndex
	 * @return the Integer value
	 * @throws TypeConversionErrorException �������ת�����ִ���, �벻Ҫ������쳣, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} �н�������Ӧ����
	 */
	BigDecimal getBigDecimal(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * ��������ֵ�õ� Date ֵ, ע�������ֵ�Ǵ� 1 ��ʼ, ����ͬ {@link java.sql.ResultSet#getDate(int)}, 
	 * �������ֵ�����ļ��е������, ������ null. <p>
	 * ���ô˷���, ˵������Ϊ����Ӧ����һ�� Date ���͵�ֵ, ϵͳ��ͨ�������õ� dateFormat ��ͼ���ļ��е� String ֵת��Ϊ Date ����, ���ת��ʧ��, �� throw {@link TypeConversionErrorException},
	 * �����㲻��Ҫ������쳣, ���ִ��쳣˵���ͻ��ϴ����ļ������Ͳ���ȷ, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * ������Ӧ�����������ʾ�� ui ��, ���������Ҫ���ü���
	 * 
	 * @see java.sql.ResultSet#getDate(int)
	 * @see #getDateFormat()
	 * @param columnIndex the columnIndex
	 * @return the Integer value
	 * @throws TypeConversionErrorException �������ת�����ִ���, �벻Ҫ������쳣, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} �н�������Ӧ����
	 */
	Date getDate(int columnIndex) throws TypeConversionErrorException;
	
}
