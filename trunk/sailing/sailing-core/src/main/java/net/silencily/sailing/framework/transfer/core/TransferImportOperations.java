/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.core;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;


import net.silencily.sailing.framework.transfer.TransferImportResult;
import net.silencily.sailing.framework.transfer.callbacks.TransferImportCallback;
import net.silencily.sailing.framework.transfer.callbacks.TransferImportProcessDataCallback;
import net.silencily.sailing.framework.transfer.exceptions.ImportProcessDataCallbackException;
import net.silencily.sailing.framework.transfer.exceptions.TransferException;
import net.silencily.sailing.framework.transfer.meta.FileType;
import net.silencily.sailing.framework.transfer.meta.ImportMetaData;

/**
 * <class>TransferImportOperations</class> 提供了导入数据需要的操作, 其默认实现是 {@link com.coheg.framework.transfer.core.TransferImportTemplate}
 * @see com.coheg.framework.transfer.core.TransferImportTemplate
 * @since 2005-9-25
 * @author 王政
 * @version $Id: TransferImportOperations.java,v 1.1 2010/12/10 10:54:23 silencily Exp $
 */
public interface TransferImportOperations {
	
	/**
	 * 导入数据, 注意 inputStream 和 fileType 的对应性
	 * @param inputStream the inputStream of file
	 * @param fileType the fileType
	 * @param importDate 导入日期, 来自于页面
	 * @param importCallback the importCallback, 子类需要实现此方法
	 * @return 导入数据结果, 其中会存储一些需要的信息
	 * @throws TransferException if any exception happens
	 * @see TransferImportResult 
	 */
	TransferImportResult importData(final InputStream inputStream, final FileType fileType, final Date importDate, TransferImportCallback importCallback) throws TransferException;
	
	/**
	 * 处理已经导入到表中的数据, 一般会有两种操作 :　删除错误数据和确认数据.  注意, 在此方法中参数 originalImportResult 将会被修改,
	 * 目的是将最新的处理结果反馈到 ui 上, 具体的操作就是会将 {@link TransferImportResult#importCallbackSuccessReturnObjects} 中的已处理对象删除
	 * @param ids 从 ui 上取得的实体 id 
	 * @param processDataCallback the processDataCallback, 子类需要实现此方法
	 * @throws ImportProcessDataCallbackException 如果 callback 中出现任何异常
	 * @see TransferImportResult#importCallbackSuccessReturnObjects
	 * @see TransferImportProcessDataCallback
	 */
	void processDataAfterImported(final Serializable[] ids, TransferImportProcessDataCallback processDataCallback)
		throws ImportProcessDataCallbackException;
	

	/**
	 * 得到导入数据需要的元信息
	 * @return the ImportMetaData
	 */
	ImportMetaData getImportMetaData();
}
