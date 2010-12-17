/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.transfer.callbacks;

import java.io.Serializable;

/**
 * <class>TransferImportProcessDataCallback</class> 用于在导入数据成功后做一些后续处理, 比如删除错误数据和确认数据
 * @since 2005-9-26
 * @author 王政
 * @version $Id: TransferImportProcessDataCallback.java,v 1.1 2010/12/10 10:54:26 silencily Exp $
 */
public interface TransferImportProcessDataCallback {
	
	/**
	 * 处理数据, 可能会是删除错误数据或确认数据, 注意这里的参数是一个 id 而非 id 集合, 原因是在处理数据时可能会抛出异常, 
	 * 而 {@link com.coheg.framework.transfer.core.TransferImportTemplate#processDataAfterImported(Serializable[], TransferImportProcessDataCallback)}
	 * 需要在处理每条数据时 catch 此异常并作相应处理
	 * @param id 从 ui 上取得的实体 id 
	 * @throws Exception if any error happens
	 */
	void processData(Serializable id) throws Exception; 
	
}
