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
 * <class>TransferImportRow</class> 代表导入数据过程中的一行数据
 * @since 2005-9-25
 * @author 王政
 * @version $Id: TransferImportRow.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public interface TransferImportRow extends TransferRow {
	
	/** 美元前缀 */
	String USD_PREFIX = "$";
	
	/** 人民币前缀 */
	String CNY_PREFIX = "￥";
	
	/**
	 * 当前数据是否是最行一行
	 * @return whether it is the last 
	 */
	boolean isLast();
	
	
	/**
	 * 根据索引值得到 String 值, 注意此索引值是从 1 开始, 规则同 {@link java.sql.ResultSet#getString(int)}, 
	 * 如果索引值超过文件中的最大列, 将返回 null
	 * 
	 * @see java.sql.ResultSet#getString(int)
	 * @param columnIndex the columnIndex
	 * @return the String value
	 * @throws TypeConversionErrorException 如果类型转换出现错误, 请不要处理此异常, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} 中将会作相应处理
	 */
	String getString(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * 根据索引值得到 Boolean 值, 注意此索引值是从 1 开始, 规则同 {@link java.sql.ResultSet#getBoolean(int)}, 
	 * 如果索引值超过文件中的最大列, 将返回 null. <p>
	 * 调用此方法, 说明你认为此列应该是一个 Boolean 类型的值, 系统将试图将文件中的 String 值转换为 Boolean 类型, 如果转换失败, 将 throw {@link TypeConversionErrorException},
	 * 不过你不需要处理此异常, 出现此异常说明客户上传的文件中类型不正确, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * 会作相应处理并将结果显示到 ui 上, 根据你的需要调用即可
	 * 
	 * @see java.sql.ResultSet#getInt(int)
	 * @param columnIndex the columnIndex
	 * @return the Integer value
	 * @throws TypeConversionErrorException 如果类型转换出现错误, 请不要处理此异常, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} 中将会作相应处理
	 */
	Boolean getBoolean(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * 根据索引值得到 Integer 值, 注意此索引值是从 1 开始, 规则同 {@link java.sql.ResultSet#getInt(int)}, 
	 * 如果索引值超过文件中的最大列, 将返回 null. <p>
	 * 调用此方法, 说明你认为此列应该是一个 Integer 类型的值, 系统将试图将文件中的 String 值转换为 Integer 类型, 如果转换失败, 将 throw {@link TypeConversionErrorException},
	 * 不过你不需要处理此异常, 出现此异常说明客户上传的文件中类型不正确, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * 会作相应处理并将结果显示到 ui 上, 根据你的需要调用即可
	 * 
	 * @see java.sql.ResultSet#getInt(int)
	 * @param columnIndex the columnIndex
	 * @return the Integer value
	 * @throws TypeConversionErrorException 如果类型转换出现错误, 请不要处理此异常, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} 中将会作相应处理
	 */
	Integer getInteger(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * 根据索引值得到 Long 值, 注意此索引值是从 1 开始, 规则同 {@link java.sql.ResultSet#getLong(int)}, 
	 * 如果索引值超过文件中的最大列, 将返回 null. <p>
	 * 调用此方法, 说明你认为此列应该是一个 Long 类型的值, 系统将试图将文件中的 String 值转换为 Long 类型, 如果转换失败, 将 throw {@link TypeConversionErrorException},
	 * 不过你不需要处理此异常, 出现此异常说明客户上传的文件中类型不正确, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * 会作相应处理并将结果显示到 ui 上, 根据你的需要调用即可
	 * 
	 * @see java.sql.ResultSet#getLong(int)
	 * @param columnIndex the columnIndex
	 * @return the Long value
	 * @throws TypeConversionErrorException 如果类型转换出现错误, 请不要处理此异常, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} 中将会作相应处理
	 */
	Long getLong(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * 根据索引值得到 Float 值, 注意此索引值是从 1 开始, 规则同 {@link java.sql.ResultSet#getFloat(int)}, 
	 * 如果索引值超过文件中的最大列, 将返回 null. <p>
	 * 调用此方法, 说明你认为此列应该是一个 Float 类型的值, 系统将试图将文件中的 String 值转换为 Float 类型, 如果转换失败, 将 throw {@link TypeConversionErrorException},
	 * 不过你不需要处理此异常, 出现此异常说明客户上传的文件中类型不正确, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * 会作相应处理并将结果显示到 ui 上, 根据你的需要调用即可
	 * 
	 * @see java.sql.ResultSet#getFloat(int)
	 * @param columnIndex the columnIndex
	 * @return the Integer value
	 * @throws TypeConversionErrorException 如果类型转换出现错误, 请不要处理此异常, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} 中将会作相应处理
	 */
	Float getFloat(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * 根据索引值得到 BigDecimal 值, 注意此索引值是从 1 开始, 规则同 {@link java.sql.ResultSet#getBigDecimal(int)}, 
	 * 如果索引值超过文件中的最大列, 将返回 null. <p>
	 * 调用此方法, 说明你认为此列应该是一个 BigDecimal 类型的值, 系统将试图将文件中的 String 值转换为 BigDecimal 类型, 如果转换失败, 将 throw {@link TypeConversionErrorException},
	 * 不过你不需要处理此异常, 出现此异常说明客户上传的文件中类型不正确, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * 会作相应处理并将结果显示到 ui 上, 根据你的需要调用即可
	 * 
	 * @see java.sql.ResultSet#getBigDecimal(int)
	 * @param columnIndex the columnIndex
	 * @return the Integer value
	 * @throws TypeConversionErrorException 如果类型转换出现错误, 请不要处理此异常, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} 中将会作相应处理
	 */
	BigDecimal getBigDecimal(int columnIndex) throws TypeConversionErrorException;
	
	/**
	 * 根据索引值得到 Date 值, 注意此索引值是从 1 开始, 规则同 {@link java.sql.ResultSet#getDate(int)}, 
	 * 如果索引值超过文件中的最大列, 将返回 null. <p>
	 * 调用此方法, 说明你认为此列应该是一个 Date 类型的值, 系统将通过你设置的 dateFormat 试图将文件中的 String 值转换为 Date 类型, 如果转换失败, 将 throw {@link TypeConversionErrorException},
	 * 不过你不需要处理此异常, 出现此异常说明客户上传的文件中类型不正确, {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)}
	 * 会作相应处理并将结果显示到 ui 上, 根据你的需要调用即可
	 * 
	 * @see java.sql.ResultSet#getDate(int)
	 * @see #getDateFormat()
	 * @param columnIndex the columnIndex
	 * @return the Integer value
	 * @throws TypeConversionErrorException 如果类型转换出现错误, 请不要处理此异常, 
	 * {@link com.coheg.framework.transfer.core.TransferImportTemplate#importData(InputStream, FileType, Date, TransferImportCallback)} 中将会作相应处理
	 */
	Date getDate(int columnIndex) throws TypeConversionErrorException;
	
}
