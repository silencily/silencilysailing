/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.web.webwork;

import java.util.List;

import net.silencily.sailing.framework.transfer.meta.FileType;
import net.silencily.sailing.framework.transfer.meta.TransferMetaData;

/**
 * 使用导出功能时 action 需要实现的接口, 实现此接口的 action 可直接使用 {@link com.coheg.framework.web.webwork.dispatcher.TransferExportResult} 来输出文件到客户端
 * @see com.coheg.framework.web.webwork.dispatcher.TransferExportResult
 * @since 2005-9-28
 * @author 王政
 * @version $Id: TransferExportable.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public interface TransferExportable {
	
	/**
	 * 得到导出数据
	 * @return the list fill with {@link com.coheg.framework.transfer.TransferExportRow}
	 */
	List getTransferExportRows();
	
	/**
	 * 得到导出数据需要的元信息
	 * @return the transferMetaData
	 */
	TransferMetaData getTransferMetaData();
	
	/**
	 * 得到文件类型, 此参数应该由 {@link com.coheg.framework.web.webwork.converter.TransferFileTypeConverter} 转换得到
	 * @see FileType
	 * @see com.coheg.framework.web.webwork.converter.TransferFileTypeConverter
	 * @return the FileType
	 */
	FileType getFileType();
	
}
