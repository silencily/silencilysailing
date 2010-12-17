/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.meta;

import java.text.DateFormat;

/**
 * 导出数据需要的元信息, 包括日期格式和文本间隔符, 默认实现是 {@link com.coheg.framework.transfer.meta.DefaultTransferMetaData}
 * @since 2005-9-28
 * @author 王政
 * @version $Id: TransferMetaData.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface TransferMetaData {

	/**
	 * 得到日期格式, 如果未设置, 将会取 {@link com.coheg.framework.transfer.TransferRow#DEFAULT_DATE_FORMAT}
	 * @return the DateFormat
	 */
	DateFormat getDateFormat();
	
	/**
	 * 得到文本各属性值间的间隔符号, 如果未设置, 将会取 {@link com.coheg.framework.transfer.TransferRow#DEFAULT_TXT_SEPARATOR}
	 * @return the separator
	 */
	String getTxtSeparator();
	
}
